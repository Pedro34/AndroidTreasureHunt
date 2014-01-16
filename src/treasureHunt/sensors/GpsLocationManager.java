package treasureHunt.sensors;

import treasureHunt.ActivityStartingHunt;
import treasureHunt.ActivityUtilityCreation;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

public class GpsLocationManager implements LocationListener {

	public Context context;
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
		/*ActivityUtilityCreation.mTxtViewlat.setText(Double.toString(location.getLatitude()));
		ActivityUtilityCreation.mTxtViewlong.setText(Double.toString(location.getLongitude()));*/
		//Oui c'est moche et alors!!!!
		ActivityStartingHunt.latitude=location.getLatitude();
		ActivityStartingHunt.longitude=location.getLongitude();
		System.out.println("5 secondes :D");
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
