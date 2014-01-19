package treasureHunt;

import treasureHunt.db.DatabaseManager;
import treasureHunt.model.Hunt;
import treasureHunt.sensors.GpsLocationManager;

import com.example.treasurehunt2.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Activité permettant à l'utilisateur de trouver les trésors. Grâce
 * aux indices et aux directives données sous forme d'angle.
 * 
 * @author Burc Pierre, Duplouy Olivier
 *
 */
public class ActivityStartingHunt extends Activity implements SensorEventListener {

	// Variables de base de la classe ActivityStartingHunt

	public Bundle hunt;
	public String huntName;
	public EditText indice;
	public int numIndice;
	public Hunt huntInformations;
	public static EditText distance;
	public static double longitude;
	public static double latitude;
	public GpsLocationManager gpsLocationListener;
	public LocationManager locationManager;
	public static Location treasureLocation;
	public static EditText degree;

	/***************************************************************************************************************/
	//**************** Variables utilisées pour le gyroscope et l'accéléromètre *************************************
	// Attribut de la classe pour calculer  l'orientation
	private float[] tabTest=new float[9];
	private float[] acceleromterVector=new float[3];
	private float[] magneticVector=new float[3];
	private float[] resultMatrix=new float [9];
	private float[] values=new float[3];
	boolean ready = false;

	private SensorManager sensorManager;
	protected Sensor magnetic;
	protected Sensor accelerometer;

	private static float azimuth;
	private static float pitch;
	private static float roll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hunt_starting);
		distance=(EditText)findViewById(R.id.distance);
		degree = (EditText)findViewById(R.id.degree);
		gpsLocationListener = GpsLocationManager.getInstance();
		gpsLocationListener.context=getApplicationContext();
		hunt = getIntent().getExtras();
		huntName = hunt.getString("nomChasse");
		numIndice=Integer.parseInt(hunt.getString("numIndice"));
		gpsLocationListener.nomChasse=huntName;

		//System.out.println(huntName+" "+numIndice);
		treasureLocation = new Location(LOCATION_SERVICE);
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
		gpsLocationListener.locationManager=locationManager;
		SQLiteDatabase db=DatabaseManager.getInstance(getApplicationContext()).getReadableDatabase();
		huntInformations=DatabaseManager.getInstance(getApplicationContext()).lastClueToSolve(db, numIndice, huntName);
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		// Instantiate the magnetic sensor and its max range
		magnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		// Instantiate the accelerometer
		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		boolean magneticSupported=sensorManager.registerListener(this, magnetic,SensorManager.SENSOR_DELAY_NORMAL);
		boolean accelerometerSupported=sensorManager.registerListener(this, accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
		if (!magneticSupported){
			System.out.println("Le capteur de champ magnétique n'est pas pris en charge par le téléphone");
		}
		if(!accelerometerSupported){
			System.out.println("Le capteur accéléromètre n'est pas pris en charge par le téléphone");
		}

		currentPosition();
		//TODO Mise en place de la BDD ou pas
	}

	/**
	 * Permet de mettre à jour la position de l'utilisateur de manière invisible.
	 */
	public void currentPosition(){
		setProgressBarIndeterminateVisibility(true);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, gpsLocationListener );
		setProgressBarIndeterminateVisibility(false);
		indice.setText(huntInformations.getIndice());
		treasureLocation.setLatitude(huntInformations.getLatitude());
		treasureLocation.setLongitude(huntInformations.getLongitude());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.treasure_hunt, menu);
		return true;
	}

	@Override
	public void onBackPressed(){
		locationManager.removeUpdates(gpsLocationListener);
		Intent intent=new Intent(this,TreasureHunt.class);
		this.finish();
		startActivity(intent);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// Mettre à jour la valeur de l'accéléromètre et du champ magnétique
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			acceleromterVector=event.values;
			if (acceleromterVector[0] != 0){
				ready = true;
			}
		} else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			magneticVector=event.values;
			if (magneticVector[2] != 0){
				ready = true;
			}
		}

		if (!ready){
			return ;
		}
		// Demander au sensorManager la matrice de Rotation (resultMatric)
		if(SensorManager.getRotationMatrix(resultMatrix, tabTest, acceleromterVector, magneticVector)){
			// Demander au SensorManager le vecteur d'orientation associé (values)
			SensorManager.getOrientation(resultMatrix, values);
			// l'azimuth
			setAzimuth((float) Math.toDegrees(values[0]));
			// le pitch
			setPitch((float) Math.toDegrees(values[1]));
			// le roll
			setRoll((float) Math.toDegrees(values[2]));
		}

	}

	public static float getAzimuth() {
		return azimuth;
	}

	public void setAzimuth(float azimuth) {
		ActivityStartingHunt.azimuth = azimuth;
	}

	public static float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		ActivityStartingHunt.pitch = pitch;
	}

	public static float getRoll() {
		return roll;
	}

	public void setRoll(float roll) {
		ActivityStartingHunt.roll = roll;
	}
}
