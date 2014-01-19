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
			/*System.out.println("Ma position: "+location.getLatitude()+" "+location.getLongitude()+" La position du trésor: "+ActivityStartingHunt.treasureLocation.getLatitude()
					+" "+ActivityStartingHunt.treasureLocation.getLongitude());*/
			float dist = ActivityStartingHunt.treasureLocation.distanceTo(location);
			System.out.println(location.getLatitude()+" "+location.getLongitude()+" "+ActivityStartingHunt.treasureLocation.getLatitude()+" "+ActivityStartingHunt.treasureLocation.getLongitude());
			System.out.println("Distance : "+dist);
			if(dist<=40){
				Toast.makeText(context,
						String.format("Vous avez trouvé le trésor !"),
						Toast.LENGTH_LONG).show();
				SQLiteDatabase db=DatabaseManager.getInstance(context).getReadableDatabase();
				int numIndice=DatabaseManager.getInstance(context).retreiveFirstClueToSolve(db, nomChasse);
				DatabaseManager.getInstance(context).deleteAfterTreasureFound(db, numIndice, nomChasse);
				numIndice=DatabaseManager.getInstance(context).retreiveFirstClueToSolve(db, nomChasse);
				if(numIndice==-1){
					DatabaseManager.getInstance(context).deleteAfterExportTreasure(db, nomChasse);
					Toast.makeText(context,
							String.format("Vous avez fini la chasse aux trésors "+nomChasse+" !"),
							Toast.LENGTH_LONG).show();
					Intent intent=new Intent(context,TreasureHunt.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					locationManager.removeUpdates(this);
					context.startActivity(intent);
				}else{
					Intent intent=new Intent(context,ActivityStartingHunt.class);
					intent.putExtra("nomChasse", nomChasse);
					intent.putExtra("numIndice",String.valueOf(numIndice));
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					locationManager.removeUpdates(this);
					context.startActivity(intent);
				}
			}
			if (dist>=1000){
				ActivityStartingHunt.distance.setText(Float.toString(dist/1000)+" km");
			}else{
				ActivityStartingHunt.distance.setText(Float.toString(dist)+" m");
			}
			
			/*Intent intent =new Intent(context,OrientationManager.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);*/
			
			float azimuth=ActivityStartingHunt.getAzimuth();//angle de l'utilisateur par rapport au Nord
			float treasureDirection=location.bearingTo(ActivityStartingHunt.treasureLocation);//angle du trésor par rapport au Nord
			/*if(treasureDirection<0){
				treasureDirection+=360;
				if (azimuth<=treasureDirection){
					ActivityStartingHunt.degree.setText(Float.toString(treasureDirection-azimuth)+" °");
				}else{
					ActivityStartingHunt.degree.setText(Float.toString(-(azimuth-treasureDirection))+" °");
				}
			}else{*/
			System.out.println(" Angle util / Nord : "+azimuth);
			System.out.println(" Angle Trésor / Nord : "+treasureDirection);
			if (azimuth<0){
				azimuth = 360 + azimuth;
			}
			
			if (treasureDirection<0){
				treasureDirection = 360 + treasureDirection;
			}
			System.out.println("Transformation de l'angle : Angle util / Nord : "+azimuth);
			System.out.println("Transformation de l'angle : Angle Trésor / Nord : "+treasureDirection);
			float result;
				if (azimuth<=treasureDirection){
					result = treasureDirection-azimuth;
					ActivityStartingHunt.degree.setText(Float.toString(treasureDirection-azimuth)+" °");
					System.out.println("Azimuth < Trésor : "+result);
				}else{
					ActivityStartingHunt.degree.setText(Float.toString(-(azimuth-treasureDirection))+" °");
					result = azimuth-treasureDirection;
					System.out.println("Azimuth > Trésor : "+result);

				}
			//}
			
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
