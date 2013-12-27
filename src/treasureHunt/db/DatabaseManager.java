package treasureHunt.db;

import treasureHunt.db.DatabaseContract.HuntEntry;
import treasureHunt.db.DatabaseContract.TreasureEntry;
import treasureHunt.model.Hunt;
import treasureHunt.model.Treasure;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper{
	private static DatabaseManager instance=null;
	
	public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TreasureHunt.db";
    
    private static final String TEXT_TYPE = " TEXT";
	private static final String INTEGER_TYPE = " INTEGER";
	private static final String DATE_TYPE = " DATE";
	private static final String DOUBLE_TYPE =" DOUBLE";
	private static final String COMMA_SEP = ",";
	
    private static final String SQL_CREATE_TREASURE_TABLE="CREATE TABLE "+TreasureEntry.TABLE_NAME +
    		" ("+
    		TreasureEntry._ID+" INTEGER PRIMARY KEY,"+
    		TreasureEntry.COLUMN_NAME_TREASURE_NAME+TEXT_TYPE+COMMA_SEP+
    		TreasureEntry.COLUMN_NAME_TREASURE_DATE+DATE_TYPE+
    		" )";
    
    private static final String SQL_CREATE_HUNT_TABLE="CREATE TABLE "+HuntEntry.TABLE_NAME+
    		" ("+
    		HuntEntry._ID+" INTEGER PRIMARY KEY,"+
    		HuntEntry.COLUMN_NAME_HUNT_NAME+TEXT_TYPE+COMMA_SEP+
    		HuntEntry.COLUMN_NAME_HUNT_CLUE_NUM+INTEGER_TYPE+COMMA_SEP+
    		HuntEntry.COLUMN_NAME_CLUE+TEXT_TYPE+COMMA_SEP+
    		HuntEntry.COLUMN_NAME_LONG+DOUBLE_TYPE+COMMA_SEP+
    		HuntEntry.COLUMN_NAME_LAT+DOUBLE_TYPE+
    		" )";
    
    private static final String SQL_DELETE_Treasure="DROP TABLE IF EXISTS "+TreasureEntry.TABLE_NAME;
    private static final String SQL_DELETE_Hunt="DROP TABLE IF EXISTS "+HuntEntry.TABLE_NAME;
    
	private DatabaseManager(Context context){
		 super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public static DatabaseManager getInstance(Context context){
		if (instance==null){
			instance=new DatabaseManager(context);
		}
		return instance;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_TREASURE_TABLE);
		db.execSQL(SQL_CREATE_HUNT_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_Treasure);
		db.execSQL(SQL_DELETE_Hunt);
		onCreate(db);
	}

	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
	
	public void insertIntoTreasure(SQLiteDatabase db,Treasure treasure){
		ContentValues values = new ContentValues();
		values.put(TreasureEntry.COLUMN_NAME_TREASURE_NAME, treasure.getNomChasse());
		values.put(TreasureEntry.COLUMN_NAME_TREASURE_DATE, treasure.getDateOrganisation());
		db.insert(TreasureEntry.TABLE_NAME, null, values);
	}
	
	public void insertIntoHunt(SQLiteDatabase db,Hunt hunt){
		ContentValues values=new ContentValues();
		values.put(HuntEntry.COLUMN_NAME_HUNT_NAME, hunt.getNomChasse());
		values.put(HuntEntry.COLUMN_NAME_HUNT_CLUE_NUM, hunt.getNumIndice());
		values.put(HuntEntry.COLUMN_NAME_CLUE, hunt.getIndice());
		values.put(HuntEntry.COLUMN_NAME_LONG,hunt.getLongitude());
		values.put(HuntEntry.COLUMN_NAME_LAT, hunt.getLatitude());
		db.insert(HuntEntry.TABLE_NAME, null, values);
	}
}
