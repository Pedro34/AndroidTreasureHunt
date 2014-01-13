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
import org.apache.http.entity.StringEntity;
import org.json.*;

import treasureHunt.model.Hunt;
import treasureHunt.model.Treasure;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class DatabaseExternalManager extends Thread{
	public static final String strURL = "http://192.168.1.19/TreasureHunt/treasure.php";
	public int action;
	public String nom;
	public DatabaseExternalManager(){

	}

	@Override
	public void run(){
		super.run();
		switch(action){
		case 1:
			String retour=importDataToAndroid(nom);
			System.out.println(retour);
			break;
		}
	}
	
	private void sendDataToServer(JSONObject obj){
		try{
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(strURL);
		httppost.setHeader("Content-type", "application/json");
		
		StringEntity se = new StringEntity(obj.toString()); 
		se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		httppost.setEntity(se); 
		HttpResponse response = httpclient.execute(httppost);
		}catch(Exception e){
			
		}
	}
	
	private JSONArray getServerData(String apiRequest,String data){
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
		System.out.println(result);
		// Parse les données JSON
		JSONArray jArray=null;
		try{
			jArray = new JSONArray(result);
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
		JSONArray jArray=getServerData("treasureNameAlreadyExist", nom);
		boolean retour=true;
		try{
			for(int i=0;i<jArray.length();i++){
				JSONObject json_data = jArray.getJSONObject(i);
				retour=json_data.getBoolean("retour");
			}
		}catch(JSONException e){
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
		return retour;
	}
	
	public String importDataToAndroid(String nom){
		JSONArray jArray=getServerData("treasureNameAlreadyExist", nom);
		String retour="";
		Treasure treasureObj=new Treasure();
		Hunt huntObj=new Hunt();
		try{
			for(int i=0;i<jArray.length();i++){
				JSONObject json_data = jArray.getJSONObject(i);
				try{
					retour=json_data.getString("retour");
				}finally{
					JSONArray treasure=json_data.getJSONArray("treasure");//correspond à la table Treasure
					JSONArray hunt=json_data.getJSONArray("hunt");//correspond à la table Hunt
					for(int j=0;j<treasure.length();j++){
						JSONObject json_data_treasure = jArray.getJSONObject(j);
						treasureObj.setNomChasse(json_data_treasure.getString("nom"));
						treasureObj.setDateOrganisation(json_data_treasure.getString("date"));
					}
					treasureObj.setMode("imported");
					SQLiteDatabase db=DatabaseManager.getInstance(null).getReadableDatabase();
					DatabaseManager.getInstance(null).insertIntoTreasure(db, treasureObj);
					for (int j=0;j<hunt.length();j++){
						JSONObject json_data_hunt = jArray.getJSONObject(j);
						huntObj.setNomChasse(json_data_hunt.getString("nom"));
						huntObj.setNumIndice(json_data_hunt.getInt("numIndice"));
						huntObj.setIndice(json_data_hunt.getString("indice"));
						huntObj.setLongitude(json_data_hunt.getDouble("longitude"));
						huntObj.setLatitude(json_data_hunt.getDouble("latitude"));
						DatabaseManager.getInstance(null).insertIntoHunt(db, huntObj);
					}
					retour="Vous venez d'importer la chasse aux trésors: "+nom;
				}
			}
		}catch(JSONException e){
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
		return retour;
	}
}
