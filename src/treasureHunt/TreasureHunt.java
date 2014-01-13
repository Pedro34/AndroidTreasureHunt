package treasureHunt;

import com.example.treasurehunt2.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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
		intent = new Intent(this, ActivityHuntToParticipate.class);
		startActivity(intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.local:
				intent = new Intent(this,ActivityManageHunts.class);
				intent.putExtra("mode", "local");
				startActivity(intent);
				return true;
			case R.id.imported:
				intent = new Intent(this,ActivityManageHunts.class);
				intent.putExtra("mode", "imported");
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
