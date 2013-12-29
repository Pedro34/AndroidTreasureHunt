package treasureHunt.db;

import treasureHunt.db.DatabaseContract.HuntEntry;
import treasureHunt.db.DatabaseContract.TreasureEntry;
import treasureHunt.model.Hunt;
import treasureHunt.model.Treasure;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

public class DatabaseManager extends SQLiteOpenHelper{
	private static DatabaseManager instance=null;
	
	public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TreasureHunt.db";
    
    private static final String TEXT_TYPE = " TEXT";
	private static final String INTEGER_TYPE = " INTEGER";
	private static final String DATE_TYPE = " DATE";
	private static final String DOUBLE_TYPE =" DOUBLE";
	private static final String COMMA_SEP = ",";
	
	public static final String DESC = " DESC";
    public static final String ASC = " ASC";
    
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
	
	/**
	 * 
	 * @param db La BD à pour insérer
	 * @param treasure L'objet complexe {@link Treasure} à insérer
	 */
	public void insertIntoTreasure(SQLiteDatabase db,Treasure treasure){
		ContentValues values = new ContentValues();
		values.put(TreasureEntry.COLUMN_NAME_TREASURE_NAME, treasure.getNomChasse());
		values.put(TreasureEntry.COLUMN_NAME_TREASURE_DATE, treasure.getDateOrganisation());
		values.put(TreasureEntry.COLUMN_NAME_TREASURE_MODE, treasure.getMode());
		db.insert(TreasureEntry.TABLE_NAME, null, values);
	}
	
	/**
	 * Permet de supprimer la chasse au trésor dans la table Treasure après l'export
	 * @param db La BD à modifier
	 * @param nom Le nom de la chasse au trésor à supprimer
	 */
	public void deleteAfterExportTreasure(SQLiteDatabase db,String nom){
		String selection=TreasureEntry.COLUMN_NAME_TREASURE_NAME+" = '"+nom+"'";
		db.delete(TreasureEntry.TABLE_NAME,selection,null);
	}
	
	/**
	 * Permet de supprimer la chasse au trésor dans la table Hunt après l'export
	 * @param db La BD à modifier
	 * @param nom Le nom de la chasse au trésor à supprimer
	 */
	public void deleteAfterExportHunt(SQLiteDatabase db,String nom){
		String selection=HuntEntry.COLUMN_NAME_HUNT_NAME+" = '"+nom+"'";
		db.delete(HuntEntry.TABLE_NAME,selection,null);
	}
	
	/**
	 * Permet de supprimer un trésor lors de la participation d'un utilisateur
	 * après qu'il l'ait trouvé.
	 * @param db La BD à modifier
	 * @param numIndice Le numéro d'indice à supprimer
	 */
	public void deleteAfterTreasureFound(SQLiteDatabase db,int numIndice){
		String selection=HuntEntry.COLUMN_NAME_HUNT_CLUE_NUM+" = "+numIndice;
		db.delete(HuntEntry.TABLE_NAME, selection, null);
	}
	
	/**
	 * 
	 * @param db La BD à pour insérer
	 * @param hunt L'objet complexe {@link Hunt} à insérer
	 */
	public void insertIntoHunt(SQLiteDatabase db,Hunt hunt){
		ContentValues values=new ContentValues();
		values.put(HuntEntry.COLUMN_NAME_HUNT_NAME, hunt.getNomChasse());
		values.put(HuntEntry.COLUMN_NAME_HUNT_CLUE_NUM, hunt.getNumIndice());
		values.put(HuntEntry.COLUMN_NAME_CLUE, hunt.getIndice());
		values.put(HuntEntry.COLUMN_NAME_LONG,hunt.getLongitude());
		values.put(HuntEntry.COLUMN_NAME_LAT, hunt.getLatitude());
		db.insert(HuntEntry.TABLE_NAME, null, values);
	}
	
	/**
	 * 
	 * @param db La BD à interroger
	 * @param nom Le nom de la chasse à trouver
	 * @return Vrai si le nom donné en paramètre n'existe pas en local
	 * (cad sur le téléphone).Faux sinon
	 */
	public boolean treasureNameNotAlreadyExistLocally(SQLiteDatabase db,String nom){
		SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
		String[] projection = {TreasureEntry.COLUMN_NAME_TREASURE_NAME};
		String selection=TreasureEntry.COLUMN_NAME_TREASURE_NAME+" = ? ";
		String[] selectionArgs={nom};
		
		_QB.setTables(TreasureEntry.TABLE_NAME);
		Cursor curseur=_QB.query(db,projection,selection,selectionArgs,null,null,null);
		int rows=curseur.getCount();
		curseur.close();
		return rows==0;
	}
	
	/**
	 * Renvoie un curseur pointant sur toutes les chasses aux trésors 
	 * créés par l'utilisateur en fonction du mode passé en param
	 * @param mode Le mode de la chasse aux trésors
	 * @param db La BD à interroger
	 * @return Renvoie un {@link Cursor} pointant sur tous les chasses aux trésors 
	 * créés par l'utilisateur
	 */
	public Cursor treasures(SQLiteDatabase db,String mode){
		SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
		_QB.setTables(TreasureEntry.TABLE_NAME);
		String[] projection = {TreasureEntry.FULL_ID,TreasureEntry.COLUMN_NAME_TREASURE_NAME,TreasureEntry.COLUMN_NAME_TREASURE_DATE};
		String selection=TreasureEntry.COLUMN_NAME_TREASURE_MODE+" = ? ";
		String[] selectionArgs={mode};
		String sortOrder =TreasureEntry.COLUMN_NAME_TREASURE_NAME+ASC;
		return _QB.query(db,projection,selection,selectionArgs,null,null,sortOrder);
	}
	
	/**
	 * 
	 * @param db La BD à interroger
	 * @param nom Le nom de la chasse à trouver
	 * @return Renvoie le dernier indice inséré par l'utilisateur en 
	 * fonction du nom de la chasse donnée en paramètre
	 */
	public int numIndiceMax(SQLiteDatabase db,String nom){
		String[] selectionArgs={nom};
		Cursor curs=db.rawQuery("SELECT MAX("+HuntEntry.COLUMN_NAME_HUNT_CLUE_NUM+") AS max "
				+ "FROM "+HuntEntry.TABLE_NAME
				+" WHERE "+HuntEntry.COLUMN_NAME_HUNT_NAME+" = ? ", selectionArgs);
		
		int retour=1;
		while (curs.moveToNext()){
			retour=curs.getInt(0);
		}
		return retour;
	}
}
