package treasureHunt;

import com.example.treasurehunt2.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class ActivityHuntToParticipate extends Activity {
	
	EditText hunt_name;
	
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
			//TODO import des données vers la BD interne
			
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
