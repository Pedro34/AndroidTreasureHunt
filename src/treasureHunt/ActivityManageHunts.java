package treasureHunt;

import treasureHunt.db.DatabaseContract.TreasureEntry;
import treasureHunt.db.DatabaseManager;

import com.example.treasurehunt2.R;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.widget.ListView;

public class ActivityManageHunts extends Activity {

	SimpleCursorAdapter simpleAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_hunts);
		
		SQLiteDatabase db=DatabaseManager.getInstance(getApplicationContext()).getReadableDatabase();
		Cursor cursor=DatabaseManager.getInstance(null).treasures(db);
		
		startManagingCursor(cursor);
		
		String[] itemListView ={
				TreasureEntry.COLUMN_NAME_TREASURE_NAME,
				TreasureEntry.COLUMN_NAME_TREASURE_DATE
				};
				
		int[] to = new int[] {R.id.nomChasse,R.id.dateOrganisation};
		
		simpleAdapter = new SimpleCursorAdapter(this, R.layout.treasure_items,
				cursor, itemListView, to);
		ListView listTasks = (ListView) findViewById(R.id.listTreasures);
		listTasks.setAdapter(simpleAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.treasure_hunt, menu);
		return true;
	}
}
