<?php
    require("database_manager.php");
	/**
	 * Permet de gérer les requêtes des clients sous forme d'API REST
     * qui répond sous le format JSON
	 */
	class Treasure {
		
		public $dm=null;
		
		function __construct() {
			$this->dm=new DatabaseManager();
			$this->dm->open();
		}
		
		public function jsonAPI(){
		    
			/*
			 *Permet de vérifier si le nom de la chasse au trésor n'a pas été créée par un autre utilisateur
			 */
			$output;
			if (isset($_REQUEST['treasureNameAlreadyExist'])){
				if (mysql_query("SELECT nom FROM Treasure WHERE nom='".$_REQUEST['treasureNameAlreadyExist']."'",$this->dm->database_link)){
					$output=array("retour"=>true);
				}else{
					$output=array("retour"=>false);
				}
				print(json_encode($output));
			}
			/*
			 * Permet à un utilisateur d'exporter sa chasse aux tésors qu'il a 
			 * créé vers la BD externe
			 * On donnera les données à expoter sous format json
			 */
			if (isset($_REQUEST['exportDataFromAndroid'])){
				$input=json_decode($_REQUEST['exportData']);
				
			}
			/*
			 * Permet de vérifier si la date d'organisation de la 
			 * chasse au trésor correspond à celle du jour courant
			 * et si c'est le cas envoie les données sous format json au demandeur
			 * On donnera le nom de la chasse en paramètre
			 */ 
			if (isset($_REQUEST['verifyNameAndDateBeforeParticipating'])){
			    $output;
				$sql=mysql_query("SELECT date FROM Treasure WHERE nom='".$_REQUEST['verifyNameAndDateBeforeParticipating']."'",$this->dm->database_link);
				if ($sql){
					$date;
					while($row=mysql_fetch_assoc($sql)){
						$date=$row['date'];
					}
					$date_system=date("Y-m-d");
					if ($date==$date_system){
						$output=$this->importDataToAndroid($_REQUEST['verifyNameAndDateBeforeParticipating']);
					}else{
						$output=array("retour"=>false);
					}
				}else{
					//le nom de la chasse au trésor indiqué par l'utilisateur n'existe pas
					$output=array("retour"=>false);
				}
				print(json_encode($output));
			}
			$this->dm->close();
		}
		
		private function importDataToAndroid($name){
			$sql=mysql_query("SELECT * FROM Treasure t,Hunt h WHERE t.nom='".$name."' AND h.nom='".$name."'",$this->dm->database_link);
			$output;
			$treasure;
			$hunt;
			while($row=mysql_fetch_assoc($sql)){
				$treasure=array("nom"=>htmlentities($row['Nom'], ENT_QUOTES, 'iso-8859-1'),"date"=>$row['Date']);
				$hunt=array("nom"=>htmlentities($row['Nom'], ENT_QUOTES, 'iso-8859-1'),
				"numIndice"=>$row['NumIndice'],
				"indice"=>htmlentities($row['Indice'], ENT_QUOTES, 'iso-8859-1'),
				"longitude"=>$row['Longitude'],
				"latitude"=>$row['Latitude']);
			}
			$output=array("treasure"=>$treasure,"hunt"=>$hunt);
			return $output;
		}
	}
	
	$treasure=new Treasure();
    $treasure->jsonAPI();
  	/*$sql=mysql_query("SELECT * FROM tblVille WHERE Nom_ville like '".$_REQUEST['ville']."%'");
  	while($row=mysql_fetch_assoc($sql))
  	$output[]=$row;

  	print(json_encode($output));
 	 mysql_close();*/
?>