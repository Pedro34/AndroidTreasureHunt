package treasureHunt;

import treasureHunt.db.DatabaseContract.TreasureEntry;
import treasureHunt.db.DatabaseManager;

import com.example.treasurehunt2.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class ActivityManageHunts extends Activity {

	public SimpleCursorAdapter simpleAdapter;
	protected Object actionMode;
	public int selectedItem = -1;
	public Cursor cursor;
	public Intent intentLocal;
	public Intent intentImported;
	public Intent mode;
	public String modeString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_hunts);
		
		intentLocal=new Intent(this,ActivityUtilityCreation.class);
		intentImported=new Intent(this,ActivityParticipation.class);
		
		mode=getIntent();
		modeString=mode.getStringExtra("mode");
		
		SQLiteDatabase db=DatabaseManager.getInstance(getApplicationContext()).getReadableDatabase();
		
		cursor=DatabaseManager.getInstance(null).treasures(db,modeString);
		
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
		listTasks.setOnItemLongClickListener(new OnItemLongClickListener() {
			
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				if (actionMode != null) {
					return false;
				}
				
				selectedItem = position;

				actionMode = ActivityManageHunts.this
						.startActionMode(mActionModeCallback);
				view.setSelected(true);
				Log.println(Log.INFO, "" + view,
						view.toString() + " , " + view.getId());
				return true;
			}
		});
	}

	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback(){

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			case R.id.remove_treasure_item:
				show();
				removeItem();
				mode.finish();
				return true;
			case R.id.modify_treasure_item:
				if (modeString.equals("local")){
					completeIntentLocal();//pour continuer la création d'une chasse aux trésors
					startActivity(intentLocal);
				}else{
					completeIntentImported();//pour continuer une partie de chasse aux trésors 
					startActivity(intentImported);
				}
				return true;
			default:
				return false;
			}
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.remove_treasure, menu);
			return true;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			actionMode=null;
			selectedItem=-1;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}
		
	};
	
	private void show(){
		cursor.moveToFirst();
		cursor.moveToPosition(selectedItem);
		String treasureName=cursor.getString(1);
		Toast.makeText(
				ActivityManageHunts.this,
				getResources().getString(R.string.remove_treasure) + " " + treasureName,
				Toast.LENGTH_LONG).show();
	}
	
	private void removeItem(){
		cursor.moveToPosition(selectedItem);
		String treasureName=cursor.getString(1);
		SQLiteDatabase db=DatabaseManager.getInstance(getApplicationContext()).getReadableDatabase();
		DatabaseManager.getInstance(getApplicationContext()).deleteAfterExportTreasure(db, treasureName);
		DatabaseManager.getInstance(getApplicationContext()).deleteAfterExportHunt(db, treasureName);
		Log.println(Log.INFO, "La chasse aux trésors: ", "" + treasureName);
		this.recreate();
	}
	
	private void completeIntentLocal(){
		cursor.moveToFirst();
		cursor.moveToPosition(selectedItem);
		String treasureName=cursor.getString(1);
		intentLocal.putExtra("nomChasse",treasureName);
		SQLiteDatabase db=DatabaseManager.getInstance(getApplicationContext()).getReadableDatabase();
		int max=DatabaseManager.getInstance(getApplicationContext()).numIndiceMax(db, treasureName);
		System.out.println("Max :"+max+" Name :" +treasureName);
		intentLocal.putExtra("numIndice", Integer.toString(max));
	}
	
	
	private void completeIntentImported(){
		cursor.moveToFirst();
		cursor.moveToPosition(selectedItem);
		String treasureName=cursor.getString(1);
		intentImported.putExtra("nomChasse",treasureName);
		SQLiteDatabase db=DatabaseManager.getInstance(getApplicationContext()).getReadableDatabase();
		int max=DatabaseManager.getInstance(null).numIndiceMax(db, treasureName);
		intentImported.putExtra("numIndice", Integer.toString(max));//on donne l'indice sur lequel l'utilisateur
		//s'est arrété
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.treasure_hunt, menu);
		return true;
	}
	
}
