<?php
	mysql_connect("localhost","root","");
  	mysql_select_db("TreasureHunt");
	
	/*
	 *Permet de vérifier si le nom de la chasse au trésor n'a pas été créée par un autre utilisateur
	 */
	if (isset($_REQUEST['treasureNameAlreadyExist'])){
		if (mysql_query("SELECT Nom FROM Treasur WHERE Nom='".$_REQUEST['treasureNameAlreadyExist']."'")){
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
	 * On donnera le nom de la chasse en paramètre
	 */ 
	if (isset($_REQUEST['verifyNameAndDateBeforeParticipating'])){
		$sql=mysql_query("SELECT Date FROM Treasure WHERE Nom='".$_REQUEST['verifyDateBeforeParticipating']."'");
		if ($sql){
			$date;
			while($row=mysql_fetch_assoc($sql)){
				$date=$row['date'];
			}
			$date_system=date("d/m/Y");
			if ($date==$date_system){
				//$output[]=array("true");
				importDataToAndroid($_REQUEST['verifyNameAndDateBeforeParticipating']);
			}else{
				$output=array("retour"=>false);
			}
		}else{
			//le nom de la chasse au trésor indiqué par l'utilisateur n'existe pas
			$output=array("retour"=>false);
		}
		print(json_encode($output));
	}
	mysql_close();
	
	function importDataToAndroid($name){
		mysql_query("SELECT * FROM Treasure t,Hunt h WHERE t.Nom='"+nom+"' AND h.Nom='"+nom+"'");
		
	}
  	/*$sql=mysql_query("SELECT * FROM tblVille WHERE Nom_ville like '".$_REQUEST['ville']."%'");
  	while($row=mysql_fetch_assoc($sql))
  	$output[]=$row;

  	print(json_encode($output));
 	 mysql_close();*/
?>