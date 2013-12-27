package treasureHunt.db;

import android.provider.BaseColumns;

public class DatabaseContract {

	public DatabaseContract(){
		
	}
	
	public static abstract class TreasureEntry implements BaseColumns{
		public static final String TABLE_NAME="treasure";
		public static final String COLUMN_NAME_TREASURE_NAME="name";
		public static final String COLUMN_NAME_TREASURE_DATE = "date";
	}
	
	public static abstract class HuntEntry implements BaseColumns{
		public static final String TABLE_NAME="hunt";
		public static final String COLUMN_NAME_HUNT_NAME="name";
		public static final String COLUMN_NAME_HUNT_CLUE_NUM="numindice";
		public static final String COLUMN_NAME_CLUE="clue";
		public static final String COLUMN_NAME_LONG="longitude";
		public static final String COLUMN_NAME_LAT="latitude";
	}
}
