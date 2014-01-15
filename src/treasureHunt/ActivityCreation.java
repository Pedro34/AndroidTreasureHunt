package treasureHunt;

import treasureHunt.db.DatabaseExternalManager;
import treasureHunt.db.DatabaseManager;
import treasureHunt.model.Treasure;

import com.example.treasurehunt2.R;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ActivityCreation extends Activity implements OnClickListener{

	Button validate_hunt;
	EditText new_hunt;
	EditText date_hunt;
	WifiManager wifi;
	//boolean recuper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_treasure_hunt_creation);
	    wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
	    if(!wifi.isWifiEnabled())
	    {
	    	AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
			localBuilder
			.setMessage("Le wifi est désactivé, mais celui-ci est nécessaire pour continuer.")
			.setCancelable(false)
			.setNeutralButton("Activer le wifi",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface paramDialogInterface, int paramInt) {
					wifi.setWifiEnabled(true);
				}
			}
					);
			localBuilder.create().show();
	    }
		validate_hunt = (Button)findViewById(R.id.validate_button_creation_hunt);
		validate_hunt.setOnClickListener(this);
		new_hunt = (EditText)findViewById(R.id.name_choosen);
		date_hunt = (EditText)findViewById(R.id.date_picker);
		DatabaseManager.getInstance(getApplicationContext());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.treasure_hunt, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		String recup=date_hunt.getText().toString();

		if(new_hunt.getText().length()!=0 && recup.matches("[0-9]{2}/[0-9]{2}/[2-9][0-9]{3}")){
			SQLiteDatabase db=DatabaseManager.getInstance(null).getWritableDatabase();
			if (DatabaseManager.getInstance(null).treasureNameNotAlreadyExistLocally(db, new_hunt.getText().toString())){

				DatabaseExternalManager dem=new DatabaseExternalManager();
				dem.action=2;
				dem.nom=new_hunt.getText().toString();
				Handler hand= new Handler();
				dem.hand=hand;
				dem.start();

				/*Message msg;
				try {
					wait(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Bundle b=msg.getData();
				boolean recuper=b.getBoolean("retour");
				boolean recuper=dem.isRetourDEM();
				System.out.println("Dans l'activité: "+recuper);
				if (!recuper){*/
					Intent intent;
					intent = new Intent(this, ActivityUtilityCreation.class);
					intent.putExtra("nomChasse", new_hunt.getText().toString());
					intent.putExtra("numIndice", "0");

					Treasure treasure=new Treasure(new_hunt.getText().toString(), recup,"local");
					DatabaseManager.getInstance(getApplicationContext()).insertIntoTreasure(db, treasure);
					startActivity(intent);
				/*}else{
					AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
					localBuilder
					.setMessage("DÃ©solÃ© mais un autre utilisateur a dÃ©jÃ  crÃ©Ã© une chasse au trÃ©sor avec ce nom.")
					.setCancelable(false)
					.setNeutralButton("Ok",
							new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface paramDialogInterface, int paramInt) {
						}
					}
							);
					localBuilder.create().show();
				}*/
			}else{
				AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
				localBuilder
				.setMessage("Désolé mais vous avez déjà  créer une chasse au trésor avec ce même nom.")
				.setCancelable(false)
				.setNeutralButton("Ok",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface paramDialogInterface, int paramInt) {
					}
				}
						);
				localBuilder.create().show();
			}
		}else{
			AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
			localBuilder
			.setMessage("Pour valider, il faut entrer le nom ainsi que la date de la chasse au trÃ©sor.")
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

}
