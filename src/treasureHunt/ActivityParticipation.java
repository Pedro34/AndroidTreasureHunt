package treasureHunt;

import com.example.treasurehunt2.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class ActivityParticipation extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_participation_treasure_hunt);
		//TODO Mise en place de la BDD ou pas
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.treasure_hunt, menu);
		return true;
	}
	
	public void launch(View v){
		
	}
}
