package treasureHunt.sensors;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

public class OrientationManager extends Activity implements SensorEventListener {

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
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
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
		OrientationManager.azimuth = azimuth;
	}

	public static float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		OrientationManager.pitch = pitch;
	}

	public static float getRoll() {
		return roll;
	}

	public void setRoll(float roll) {
		OrientationManager.roll = roll;
	}

}
