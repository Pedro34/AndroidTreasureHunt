package treasureHunt.db;

import android.provider.BaseColumns;

/**
 * Classe permettant de décrire la Base de données SQLite
 * 
 * @author Burc Pierre, Duplouy Olivier
 *
 */
public class DatabaseContract {

	public DatabaseContract(){
		
	}
	
	public static abstract class TreasureEntry implements BaseColumns{
		public static final String TABLE_NAME="treasure";
		public static final String FULL_ID=TABLE_NAME+"."+_ID;
		public static final String COLUMN_NAME_TREASURE_NAME="name";
		public static final String COLUMN_NAME_TREASURE_DATE ="date";
		/**
		 * Le mode peut prendre deux valeurs:
		 * <ul>
		 * 	<li>
		 * 	imported correspond à une chasse aux trésors pour laquelle
		 * 	l'utilisateur participe
		 * 	</li>
		 * 	<li>
		 * 	local correspond à une chasse aux trésors pour laquelle
		 * 	l'utilisateur est créateur
		 * 	</li>
		 * </ul>
		 */
		public static final String COLUMN_NAME_TREASURE_MODE="mode";
	}
	
	public static abstract class HuntEntry implements BaseColumns{
		public static final String TABLE_NAME="hunt";
		public static final String FULL_ID=TABLE_NAME+"."+_ID;
		public static final String COLUMN_NAME_HUNT_NAME="name";
		public static final String COLUMN_NAME_HUNT_CLUE_NUM="numindice";
		public static final String COLUMN_NAME_CLUE="clue";
		public static final String COLUMN_NAME_LONG="longitude";
		public static final String COLUMN_NAME_LAT="latitude";
	}
}
