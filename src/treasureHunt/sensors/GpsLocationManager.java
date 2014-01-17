package treasureHunt.sensors;

import treasureHunt.ActivityStartingHunt;
import treasureHunt.ActivityUtilityCreation;
import treasureHunt.TreasureHunt;
import treasureHunt.db.DatabaseManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.view.ViewDebug.FlagToString;
import android.widget.Toast;

public class GpsLocationManager implements LocationListener {

	public Context context;
	public String nomChasse;
	public LocationManager locationManager;
	
	private static volatile GpsLocationManager instance = null;

	private GpsLocationManager(){

	}

	public final static GpsLocationManager getInstance(){
		if(GpsLocationManager.instance==null){
			synchronized (GpsLocationManager.class) {
				if(GpsLocationManager.instance==null){
					GpsLocationManager.instance=new GpsLocationManager();
				}
			}
		}
		return GpsLocationManager.instance;
	}

	@Override
	public void onLocationChanged(Location location) {

		if(ActivityUtilityCreation.mTxtViewlat != null){
			ActivityUtilityCreation.mTxtViewlat.setText(Double.toString(location.getLatitude()));
			ActivityUtilityCreation.mTxtViewlong.setText(Double.toString(location.getLongitude()));
		}
		//Oui c'est moche et alors!!!!
		if(ActivityStartingHunt.distance != null){
			ActivityStartingHunt.latitude=location.getLatitude();
			ActivityStartingHunt.longitude=location.getLongitude();
			System.out.println("Ma position: "+location.getLatitude()+" "+location.getLongitude()+" La position du trésor: "+ActivityStartingHunt.treasureLocation.getLatitude()
					+" "+ActivityStartingHunt.treasureLocation.getLongitude());
			float dist = ActivityStartingHunt.treasureLocation.distanceTo(location);
			System.out.println("Distance : "+dist);
			if(dist<=20){
				Toast.makeText(context,
						String.format("Vous avez trouvé le trésor !"),
						Toast.LENGTH_LONG).show();
				SQLiteDatabase db=DatabaseManager.getInstance(context).getReadableDatabase();
				int numIndice=DatabaseManager.getInstance(context).retreiveFirstClueToSolve(db, nomChasse);
				DatabaseManager.getInstance(context).deleteAfterTreasureFound(db, numIndice, nomChasse);
				numIndice=DatabaseManager.getInstance(context).retreiveFirstClueToSolve(db, nomChasse);
				System.out.println("Numéro d'indice: "+numIndice);
				if(numIndice==-1){
					DatabaseManager.getInstance(context).deleteAfterExportTreasure(db, nomChasse);
					Toast.makeText(context,
							String.format("Vous avez fini la chasse aux trésors "+nomChasse+" !"),
							Toast.LENGTH_LONG).show();
					Intent intent=new Intent(context,TreasureHunt.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
					locationManager.removeUpdates(this);
				}else{
					Intent intent=new Intent(context,ActivityStartingHunt.class);
					intent.putExtra("nomChasse", nomChasse);
					intent.putExtra("numIndice",numIndice);
					context.startActivity(intent);
				}
			}
			if (dist>=1000){
				ActivityStartingHunt.distance.setText(Float.toString(dist/1000)+" km");
			}else{
				ActivityStartingHunt.distance.setText(Float.toString(dist)+" m");
			}
			if(location.bearingTo(ActivityStartingHunt.treasureLocation)<0){
				ActivityStartingHunt.degree.setText(Float.toString(location.bearingTo(ActivityStartingHunt.treasureLocation)+360)+" °");
			}else{
				ActivityStartingHunt.degree.setText(Float.toString(location.bearingTo(ActivityStartingHunt.treasureLocation))+" °");
			}

			/*System.out.println("Ma position: "+ActivityStartingHunt.myPosition.getLatitude()+" "+ActivityStartingHunt.myPosition.getLongitude()+" La position du trésor: "+ActivityStartingHunt.treasureLocation.getLatitude()
					+" "+ActivityStartingHunt.treasureLocation.getLongitude());*/
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		//Lorsque la source (GSP ou réseau GSM) est désactivé
		//...on affiche un Toast pour le signaler à l'utilisateur
		Toast.makeText(context,
				String.format("La source \"%s\" a été désactivée", provider),
				Toast.LENGTH_LONG).show();
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

}
