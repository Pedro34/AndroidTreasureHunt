package treasureHunt;

import com.example.treasurehunt2.R;
import com.example.treasurehunt2.R.layout;
import com.example.treasurehunt2.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class TreasureHunt extends Activity {
	Button button_create;
	Button button_participate;
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_treasure_hunt);
		button_create = (Button)findViewById(R.id.create_hunt);
		button_participate = (Button)findViewById(R.id.participate_hunt);
		//TODO Mise en place de la BDD ou pas
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.treasure_hunt, menu);
		return true;
	}

	public void create_hunt(View v){
		intent = new Intent(this, ActivityCreation.class);
		startActivity(intent);
	}
	
	public void participate_hunt(View v){
		intent = new Intent(this, ActivityParticipation.class);
		startActivity(intent);
	}
	
	public void manage_hunts(View v){
		intent = new Intent(this,ActivityManageHunts.class);
		startActivity(intent);
	}

}
