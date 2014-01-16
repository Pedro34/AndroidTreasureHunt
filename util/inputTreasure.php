<?php
    require("database_manager.php");

    /**
     * 
     */
    class InputTreasure {
        
        public $dm=null;
        
        function __construct() {
            $this->dm=new DatabaseManager();
            $this->dm->open();
        }
        
        function input(){
		$json=file_get_contents('php://input');
            //$input=json_decode($_REQUEST['exportDataFromAndroid'],true);
			$input=json_decode($json, true);
            $output;
                
            if(!$input){
                print("Le JSON n'est pas parsable");
                $output=array("retour"=>"fail");
            }else{
			//print_r($input);
            $treasureTab=$input['treasure'];
            $huntTab=$input['hunt'];
            $recup = json_decode($treasureTab,true);
			$nomTreasure = $recup['nomChasse'];
			$dateTreasure = $recup['dateOrganisation'];
            $sqlTreasure=mysql_query("INSERT INTO Treasure (Nom,Date) VALUES('".$nomTreasure."','".$dateTreasure."')",$this->dm->database_link);
			$recup2 = json_decode($huntTab,true);
			//print_r($recup2);
            for ($i=0;$i<count($recup2);$i++){
				$attributs = $recup2[$i];
                /*$huntTab['nom'];
                $huntTab['numIndice'];
                $huntTab['indice'];
                $huntTab['longitude'];
                $huntTab['latitude'];*/
                $link=$this->dm->database_link;
				echo $attributs['nomChasse']." ".$attributs['numIndice']." ".$attributs['indice']." ".$attributs['longitude'];
                $sqlHunt=mysql_query("INSERT INTO Hunt (Nom,NumIndice,Indice,Longitude,Latitude) VALUES (
                '".$attributs['nomChasse']."',".$attributs['numIndice'].",'".$attributs['indice']."',
                ".$attributs['longitude'].",".$attributs['latitude'].")");
            }
            $this->dm->close();
            $output=array("retour"=>"success");
			}
            //print(json_encode($output));    
        }
    }  
$input=new InputTreasure();
$input->input();
?>