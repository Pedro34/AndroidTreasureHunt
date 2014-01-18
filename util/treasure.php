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
			$result = mysql_query("SELECT nom FROM Treasure WHERE nom='".$_REQUEST['treasureNameAlreadyExist']."'",$this->dm->database_link);
			$reponse = mysql_num_rows($result);
				if ($reponse >= 1){
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
             * 
             * pool de test {"treasure":{"nom":"coucou","date":"2014-01-05"},"hunt":[{"nom":"coucou","numIndice":"1","indice":"Vous devez vous rendre à l'hôtel le plus connu de la capital de France.","longitude":"12.175867834","latitude":"56.846897829"}]}
			 */
			if (isset($_REQUEST['exportDataFromAndroid'])){
				$input=json_decode($_REQUEST['exportDataFromAndroid'],true);
				$output;
				
                if(!$input){
                    print("Le JSON n'est pas parsable");
                    $output=array("retour"=>"fail");
                }
				$treasureTab=$input['treasure'];
				$huntTab=$input['hunt'];
                $nomTreasure=$treasureTab['nomChasse'];
                $dateTreasure=$treasureTab['dateOrganisation'];//TODO: devra certainement avoir besoinde transformation... 
                //echo $treasureTab." ".$huntTab;
                $sqlTreasure=mysql_query("INSERT INTO Treasure (Nom,Date) VALUES('".$nomTreasure."','".$dateTreasure."')", $this->dm->database_link);
                for ($i=0;$i<count($huntTab);$i++){
                    /*$huntTab['nom'];
                    $huntTab['numIndice'];
                    $huntTab['indice'];
                    $huntTab['longitude'];
                    $huntTab['latitude'];*/
                    $sqlHunt=mysql_query("INSERT INTO Hunt (Nom,NumIndice,Indice,Longitude,Latitude) VALUES (
                    '".$huntTab['nomChasse']."',".$huntTab['numIndice'].",'".$huntTab['indice']."',
                    ".$huntTab['longitude'].",".$huntTab['latitude']."", $this->dm->database_link);
                }
                $output=array("retour"=>"success");
                print(json_encode($output));   
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
				if (mysql_num_rows($sql)>=1){
					$date;
					while($row=mysql_fetch_assoc($sql)){
						$date=$row['date'];
					}
					$date_system=date("Y-m-d");
					if ($date==$date_system){
						$output=$this->importDataToAndroid($_REQUEST['verifyNameAndDateBeforeParticipating']);
					}else{
						$output=array("retour"=>htmlentities("La chasse au trésor n'est pas prévue à ce jour", ENT_QUOTES, 'utf-8'));
					}
				}else{
					//le nom de la chasse au trésor indiqué par l'utilisateur n'existe pas
					$output=array("retour"=>htmlentities("Le nom de la chasse au trésor n'existe pas", ENT_QUOTES, 'utf-8'));
				}
				print(json_encode($output));
			}
			$this->dm->close();
		}
		
		private function importDataToAndroid($name){
			$sql=mysql_query("SELECT * FROM Treasure WHERE nom='".$name."'",$this->dm->database_link);
			$sql_hunt=mysql_query("SELECT * FROM Hunt WHERE nom='".$name."'",$this->dm->database_link);
			$output;
			$treasure;
			$hunt;
            $hunt_final=array();
			while($row=mysql_fetch_assoc($sql)){
				$treasure=array("nom"=>htmlentities($row['Nom'], ENT_QUOTES, 'iso-8859-1'),"date"=>$row['Date']);
			}
            while($row_hunt=mysql_fetch_assoc($sql_hunt)){
                $hunt=array("nom"=>htmlentities($row_hunt['Nom'], ENT_QUOTES, 'iso-8859-1'),
                "numIndice"=>$row_hunt['NumIndice'],
                "indice"=>htmlentities($row_hunt['Indice'], ENT_QUOTES, 'iso-8859-1'),
                "longitude"=>$row_hunt['Longitude'],
                "latitude"=>$row_hunt['Latitude']);
                array_push($hunt_final,$hunt);
            }
			$output=array("treasure"=>$treasure,"hunt"=>$hunt_final);
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