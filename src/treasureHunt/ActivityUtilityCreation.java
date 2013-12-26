package treasureHunt;

import com.example.treasurehunt2.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityUtilityCreation extends Activity implements LocationListener {

	public  LocationManager locationManager;
	public  TextView mTxtViewlat;
	public  TextView mTxtViewlong;
	public  Location myPosition = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creation_treasure_hunt_course);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Intent localIntent = new Intent(this, PermissionGps.class);
			localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(localIntent);
		}
		currentPosition();
		mTxtViewlat = (TextView) findViewById(R.id.textlat);
		mTxtViewlong = (TextView) findViewById(R.id.textlong);
		//TODO Mise en place de la BDD ou pas
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.treasure_hunt, menu);
		return true;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		mTxtViewlat.setText(" "+location.getLatitude());
		mTxtViewlong.setText(" "+location.getLongitude());

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		//Lorsque la source (GSP ou réseau GSM) est désactivé
		//...on affiche un Toast pour le signaler à l'utilisateur
		Toast.makeText(this,
						String.format("La source \"%s\" a été désactivé", provider),
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
		builder.setTitle("Trouvé !");
		builder.setMessage("HAHAHAHAH");
		builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);
		mTxtViewlat = (TextView) findViewById(R.id.textlat);
		mTxtViewlong = (TextView) findViewById(R.id.textlong);
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
