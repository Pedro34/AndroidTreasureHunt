package treasureHunt;

import java.util.concurrent.Semaphore;

import treasureHunt.db.DatabaseExternalManager;
import treasureHunt.db.DatabaseManager;

import com.example.treasurehunt2.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class ActivityHuntToParticipate extends Activity {
	
	EditText hunt_name;
	public static Semaphore mut=new Semaphore(0);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hunt_to_participate);
		hunt_name=(EditText)findViewById(R.id.name_hunt_choosen);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.treasure_hunt, menu);
		return true;
	}
	
	public void launch(View v){
		if (hunt_name.getText().length()!=0){
			SQLiteDatabase  db=DatabaseManager.getInstance(getApplicationContext()).getReadableDatabase();
			if(!DatabaseManager.getInstance(getApplicationContext()).treasureNameAlreadyImported(db, hunt_name.getText().toString())){
			
				DatabaseExternalManager dem = new DatabaseExternalManager();
				dem.action=1;//correspond à l'import
				dem.nom=hunt_name.getText().toString();
				dem.context=getApplicationContext();
				dem.start();
				
				try {
					Thread.sleep(1);
					mut.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String retour = dem.getRetourImport();
				if(retour.equals("sucess")){
				String name = hunt_name.getText().toString();
				Intent intent = new Intent(this, ActivityStartingHunt.class);
				intent.putExtra("nomChasse",name);
				intent.putExtra("numIndice", String.valueOf(1));
				startActivity(intent);
				}else{
					AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
					localBuilder
					.setMessage("Chasse au trésor inexistante ou n'est pas prévue à ce jour.")
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
				.setMessage("Vous avez déjà importé la chasse aux trésors: "+hunt_name.getText().toString()+".")
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
			.setMessage("Pour participer il faut rentrer un de chasse au trésor.")
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
