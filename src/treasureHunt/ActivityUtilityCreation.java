package treasureHunt;

import org.json.JSONObject;

import treasureHunt.db.DatabaseExternalManager;
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
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityUtilityCreation extends Activity {

	public LocationManager locationManager;
	public static EditText mTxtViewlat;
	public static EditText mTxtViewlong;
	public EditText indice;
	public Intent creationRecup;
	public String nomChasse;
	public int numIndice;
	GpsLocationManager gpsLocationListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creation_treasure_hunt_course);
		creationRecup=getIntent();
		gpsLocationListener = GpsLocationManager.getInstance();
		gpsLocationListener.context=getApplicationContext();
		
		nomChasse=creationRecup.getStringExtra("nomChasse");
		numIndice=Integer.parseInt(creationRecup.getStringExtra("numIndice"));

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Intent localIntent = new Intent(this, PermissionGps.class);
			localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(localIntent);
		}
		currentPosition();
		mTxtViewlat = (EditText) findViewById(R.id.textlat);
		mTxtViewlong = (EditText) findViewById(R.id.textlong);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.treasure_hunt, menu);
		return true;
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
		mTxtViewlat = (EditText) findViewById(R.id.textlat);
		mTxtViewlong = (EditText) findViewById(R.id.textlong);
		System.out.println("LOL?!");
	}

	public void addTreasure(View v){

		indice=(EditText)findViewById(R.id.clue);
		if (indice.getText().length()!=0){
			String lati=mTxtViewlat.getText().toString();
			String longi=mTxtViewlong.getText().toString();

			numIndice++;
			Hunt hunt=new Hunt(nomChasse,numIndice,indice.getText().toString(),Double.parseDouble(longi),Double.parseDouble(lati));
			SQLiteDatabase db=DatabaseManager.getInstance(getApplicationContext()).getWritableDatabase();
			DatabaseManager.getInstance(getApplicationContext()).insertIntoHunt(db, hunt);

			Toast.makeText(this,
					String.format("Indice N° \"%s\" ajouté !", numIndice),
					Toast.LENGTH_LONG).show();
		}else{
			AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
			localBuilder
			.setMessage("Pour valider, il faut entrer un indice.")
			.setCancelable(false)
			.setNeutralButton("Ok",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface paramDialogInterface, int paramInt) {
				}
			}
					);
			localBuilder.create().show();
		}
	}

	public void endCourse(View v){
		SQLiteDatabase db=DatabaseManager.getInstance(getApplicationContext()).getWritableDatabase();
		//TODO export des données à effectuer
		//......................................
		JSONObject json=DatabaseManager.getInstance(getApplicationContext()).retreiveInformation(db, nomChasse);
		DatabaseExternalManager dem=new DatabaseExternalManager();
		dem.action=3;
		dem.toSend=json;
		dem.start();
		//..............................................
		//Après l'export on enlève de la BD
		DatabaseManager.getInstance(getApplicationContext()).deleteAfterExportTreasure(db, nomChasse);
		DatabaseManager.getInstance(getApplicationContext()).deleteAfterExportHunt(db, nomChasse);
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
		localBuilder
		.setMessage("Chasse Créée !")
		.setCancelable(false)
		.setNeutralButton("Revenir au menu principal",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface paramDialogInterface, int paramInt) {
				Intent terminate = new Intent(ActivityUtilityCreation.this, TreasureHunt.class);
				startActivity(terminate);
				ActivityUtilityCreation.this.finish();
			}
		}
				);
		localBuilder.create().show();
	}

	@Override
	public void onBackPressed(){
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
		localBuilder
		.setMessage("Vous pourrez terminer votre cr�ation via la gestion de vos chasses.")
		.setCancelable(false)
		.setNeutralButton("Ok",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface paramDialogInterface, int paramInt) {
				Intent terminate = new Intent(ActivityUtilityCreation.this, TreasureHunt.class);
				startActivity(terminate);
				ActivityUtilityCreation.this.onStop();
			}
		}
				);
		localBuilder.create().show();
	}
	
}
