package treasureHunt.db;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.json.*;

import treasureHunt.ActivityCreation;
import treasureHunt.model.Hunt;
import treasureHunt.model.Treasure;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class DatabaseExternalManager extends Thread{
	public static final String strURL = "http://192.168.1.19/TreasureHunt/treasure.php";
	public static final String strURLInput = "http://192.168.1.19/TreasureHunt/inputTreasure.php";
	public int action;
	public String nom;
	public Handler hand;
	private boolean retourDEM;
	public JSONObject toSend;
	public Context context;

	public DatabaseExternalManager(){

	}
	
	public DatabaseExternalManager(Handler hand){
		this.hand = hand;
	}
	
	@Override
	public void run(){
		super.run();
		switch(action){
		case 1:
			String retour=importDataToAndroid(nom);
			System.out.println(retour);
			break;
		case 2:
			try {
				System.out.println("Libération du mutex");
				ActivityCreation.mut.release();
				System.out.println("Tentative d'aquisition du mutex thread");
				ActivityCreation.mut.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*Message msg=hand.obtainMessage();
			Bundle data=new Bundle();
			data.putBoolean("retour", treasureNameAlreadyExist(nom));
			msg.setData(data);
			hand.sendMessage(msg);*/
			retourDEM=treasureNameAlreadyExist(nom);
			System.out.println("Valeur dans thread : "+treasureNameAlreadyExist(nom));
			ActivityCreation.mut.release();
			break;
		case 3:
			sendDataToServer(toSend);
		}
	}

	private void sendDataToServer(JSONObject obj){
		InputStream is=null;
		String result="";

		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(strURLInput);
			//httppost.setHeader("Content-type", "application/json");
			httppost.setHeader("json", obj.toString());

			//StringEntity se = new StringEntity(obj.toString()); 
			//se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

			//ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			//nameValuePairs.add(new BasicNameValuePair("exportDataFromAndroid",se.toString()));

			httppost.setEntity(new ByteArrayEntity(obj.toString().getBytes("UTF8"))); 

			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is=entity.getContent();
		}catch(Exception e){

		}

		// Conversion de la requête en string
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result=sb.toString();
		}catch(Exception e){
			Log.e("log_tag", "Error converting result " + e.toString());
		}
		System.out.println(result);
	}

	private JSONObject getServerData(String apiRequest,String data){
		InputStream is=null;
		String result="";
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair(apiRequest,data));

		// Envoie de la commande http
		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(strURL);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		}catch(Exception e){
			Log.e("log_tag", "Error in http connection " + e.toString());
		}

		// Convertion de la requête en string
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result=sb.toString();
		}catch(Exception e){
			Log.e("log_tag", "Error converting result " + e.toString());
		}
		// Parse les données JSON
		JSONObject jArray=null;
		try{
			jArray = new JSONObject(result);
			/*for(int i=0;i<jArray.length();i++){
				JSONObject json_data = jArray.getJSONObject(i);
				Log.i("log_tag","ID_ville: "+json_data.getInt("ID_ville")+
						", Nom_ville: "+json_data.getString("Nom_ville")
						);
				 Résultats de la requête
				returnString += "\n\t" + jArray.getJSONObject(i);
			}*/
		}catch(JSONException e){
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
		return jArray;
	}

	/**
	 * 
	 * @param nom Le nom de la chasse aux trésors
	 * @return Vrai s'il existe déjà dans la BD externe.
	 * Faux sinon.
	 */
	public boolean treasureNameAlreadyExist(String nom){
		JSONObject jArray=getServerData("treasureNameAlreadyExist", nom);
		boolean retour=true;
		try{
			retour=jArray.getBoolean("retour");
		}catch(JSONException e){
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
		return retour;
	}

	public String importDataToAndroid(String nom){
		JSONObject jArray=getServerData("verifyNameAndDateBeforeParticipating", nom);
		String retour="";
		Treasure treasureObj=new Treasure();
		Hunt huntObj=new Hunt();
		try{
			try{
				retour=jArray.getString("retour");
			}finally{
				JSONObject treasure=jArray.getJSONObject("treasure");//correspond à la table Treasure
				JSONArray hunt=jArray.getJSONArray("hunt");//correspond à la table Hunt
				treasureObj.setNomChasse(treasure.getString("nom"));
				treasureObj.setDateOrganisation(treasure.getString("date"));
				treasureObj.setMode("imported");
				SQLiteDatabase db=DatabaseManager.getInstance(context).getReadableDatabase();
				DatabaseManager.getInstance(context).insertIntoTreasure(db, treasureObj);
				for (int j=0;j<hunt.length();j++){
					JSONObject json_data_hunt = hunt.getJSONObject(j);
					huntObj.setNomChasse(json_data_hunt.getString("nom"));
					huntObj.setNumIndice(json_data_hunt.getInt("numIndice"));
					huntObj.setIndice(json_data_hunt.getString("indice"));
					huntObj.setLongitude(json_data_hunt.getDouble("longitude"));
					huntObj.setLatitude(json_data_hunt.getDouble("latitude"));
					DatabaseManager.getInstance(context).insertIntoHunt(db, huntObj);
				}
				retour="Vous venez d'importer la chasse aux trésors: "+nom;
			}

		}catch(JSONException e){
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
		return retour;
	}

	public boolean isRetourDEM() {
		return retourDEM;
	}

	public void setRetourDEM(boolean retourDEM) {
		this.retourDEM = retourDEM;
	}
}
