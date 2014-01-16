package treasureHunt;

import treasureHunt.db.DatabaseManager;
import treasureHunt.model.Hunt;
import treasureHunt.sensors.GpsLocationManager;

import com.example.treasurehunt2.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityStartingHunt extends Activity {
	public Bundle hunt;
	public String huntName;
	public EditText indice;
	public int numIndice;
	public Hunt huntInformations;
	public EditText distance;
	public static double longitude;
	public static double latitude;
	public GpsLocationManager gpsLocationListener;
	public LocationManager locationManager;
	public Location myPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hunt_starting);
		distance=(EditText)findViewById(R.id.distance);
		gpsLocationListener = GpsLocationManager.getInstance();
		gpsLocationListener.context=getApplicationContext();
		hunt = getIntent().getExtras();
		huntName = hunt.getString("nomChasse");
		numIndice=Integer.parseInt(hunt.getString("numIndice"));

		Toast.makeText(this,
				String.format("La chasse \"%s\" va débuter !", huntName),
				Toast.LENGTH_LONG).show();
		indice=(EditText)findViewById(R.id.clue_given);
		
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Intent localIntent = new Intent(this, PermissionGps.class);
			localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(localIntent);
		}
		
		SQLiteDatabase db=DatabaseManager.getInstance(getApplicationContext()).getReadableDatabase();
		huntInformations=DatabaseManager.getInstance(getApplicationContext()).lastClueToSolve(db, numIndice, huntName);
		
		currentPosition();
		//TODO Mise en place de la BDD ou pas
	}

	public void currentPosition(){
		setProgressBarIndeterminateVisibility(true);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, gpsLocationListener );
		setProgressBarIndeterminateVisibility(false);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Trouvé !");
		builder.setMessage("HAHAHAHAH");
		builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, gpsLocationListener);
		indice.setText(huntInformations.getIndice());
		Location treasureLocation = new Location(LOCATION_SERVICE);
		treasureLocation.setLatitude(huntInformations.getLatitude());
		treasureLocation.setLongitude(huntInformations.getLongitude());
		
		myPosition=new Location(LOCATION_SERVICE);
		myPosition.setLongitude(longitude);
		myPosition.setLatitude(latitude);
		
		System.out.println("Ma position: "+longitude+" "+latitude+" La position du trésor: "+treasureLocation.getLongitude()
				+" "+treasureLocation.getLatitude());
		float dist = myPosition.distanceTo(treasureLocation);
		distance.setText(Float.toString(dist));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.treasure_hunt, menu);
		return true;
	}


}
