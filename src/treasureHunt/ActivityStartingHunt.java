package treasureHunt;

import com.example.treasurehunt2.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

public class ActivityStartingHunt extends Activity {
	Bundle hunt;
	String huntName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hunt_starting);
		hunt = getIntent().getExtras();
		huntName = hunt.getString("nomChasse");
		Toast.makeText(this,
				String.format("La chasse \"%s\" va débuter !", huntName),
				Toast.LENGTH_LONG).show();
		
		//TODO Mise en place de la BDD ou pas
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.treasure_hunt, menu);
		return true;
	}
}
