package treasureHunt;

import org.json.JSONObject;

import treasureHunt.db.DatabaseExternalManager;
import treasureHunt.db.DatabaseManager;
import treasureHunt.model.Hunt;

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

public class ActivityUtilityCreation extends Activity implements LocationListener {

	public LocationManager locationManager;
	public EditText mTxtViewlat;
	public EditText mTxtViewlong;
	public EditText indice;
	public Location myPosition = null;
	public Intent creationRecup;
	public String nomChasse;
	public int numIndice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creation_treasure_hunt_course);
		creationRecup=getIntent();

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

	@Override
	public void onLocationChanged(Location location) {
		mTxtViewlat.setText(Double.toString(location.getLatitude()));
		mTxtViewlong.setText(Double.toString(location.getLongitude()));

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		//Lorsque la source (GSP ou réseau GSM) est désactivé
		//...on affiche un Toast pour le signaler à l'utilisateur
		Toast.makeText(this,
				String.format("La source \"%s\" a �t� d�sactiv�e", provider),
				Toast.LENGTH_LONG).show();
		//... et on spécifie au service que l'on ne souhaite plus avoir de mise à jour
		//TreasureHunt.myPosition.removeUpdates(this);
		//... on stop le cercle de chargement
		//setProgressBarIndeterminateVisibility(false);

	}

	public void currentPosition(){
		setProgressBarIndeterminateVisibility(true);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);
		setProgressBarIndeterminateVisibility(false);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Trouv� !");
		builder.setMessage("HAHAHAHAH");
		builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);
		mTxtViewlat = (EditText) findViewById(R.id.textlat);
		mTxtViewlong = (EditText) findViewById(R.id.textlong);
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
					String.format("Indice n� \"%s\" ajout� !", numIndice),
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
		String lati=mTxtViewlat.getText().toString();
		String longi=mTxtViewlong.getText().toString();
		Hunt hunt=new Hunt(nomChasse,numIndice,Double.parseDouble(longi),Double.parseDouble(lati));
		SQLiteDatabase db=DatabaseManager.getInstance(getApplicationContext()).getWritableDatabase();
		DatabaseManager.getInstance(getApplicationContext()).insertIntoHunt(db, hunt);
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
		.setMessage("Chasse Cr��e !")
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
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

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
				ActivityUtilityCreation.this.finish();
			}
		}
				);
		localBuilder.create().show();
	}
}
