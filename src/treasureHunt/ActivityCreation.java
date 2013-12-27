package treasureHunt;

import java.text.SimpleDateFormat;

import com.example.treasurehunt2.R;
import com.example.treasurehunt2.R.layout;
import com.example.treasurehunt2.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ActivityCreation extends Activity implements OnClickListener{
	Button validate_hunt;
	EditText new_hunt;
	EditText date_hunt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_treasure_hunt_creation);
		validate_hunt = (Button)findViewById(R.id.validate_button_creation_hunt);
		validate_hunt.setOnClickListener(this);
		new_hunt = (EditText)findViewById(R.id.name_choosen);
		date_hunt = (EditText)findViewById(R.id.date_picker);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.treasure_hunt, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		SimpleDateFormat ss = new SimpleDateFormat("dd/MM/yyyy");
		String recup=null;
		try{
			recup=ss.format(date_hunt.getText().toString());
			if(new_hunt.getText().length()!=0 && recup.matches("[0-9]{2}/[0-9]{2}/[2-9][0-9]{3}")){
				Intent intent;
				intent = new Intent(this, ActivityUtilityCreation.class);
				intent.putExtra("nomChasse", new_hunt.getText());
				intent.putExtra("numIndice", 1);
				
				startActivity(intent);
			}else{
				AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
				localBuilder
				.setMessage("Pour valider, il faut entrer le nom ainsi que la date de la chasse au trésor.")
				.setCancelable(false)
				.setNeutralButton("Ok",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface paramDialogInterface, int paramInt) {
					}
				}
						);
				localBuilder.create().show();
			}
		}finally{
			AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
			localBuilder
			.setMessage("Pour valider, il faut entrer une date valide dans le format dd/MM/yyyy")
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
	
	@Override
	public void onBackPressed(){
		
	}

}
