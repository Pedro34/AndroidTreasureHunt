<?php
	/**
	 * Permet de gérer la base de données tant pour l'ouvertre
     * que pour la fermeture
	 */
	class DatabaseManager {
		
        /**
         * Un lien ouvert de la BD
         */
		public $database_link=null;
		
		function __construct() {
			$this->database_link=mysql_connect("localhost","lodacom","Lo124625") or die ("La connexion a echoue "); 
		}
		
        /**
         * Permet d'accéder à la base de donnée externe
         */
		public function open(){
			mysql_select_db("TreasureHunt",$this->database_link) or die ("La base de donnee n'existe pas");
		}
		
        /**
         * Permet de fermer la conncexion à la base de donnée externe
         */
		public function close(){
			mysql_close($this->database_link);
		}
	}
	
?>