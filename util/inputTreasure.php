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
            $sqlTreasure=mysql_query("INSERT INTO Treasure (Nom,Date) VALUES('".$nomTreasure."','".$dateTreasure."')",$this->dm->database_link);
            for ($i=0;$i<count($huntTab);$i++){
                /*$huntTab['nom'];
                $huntTab['numIndice'];
                $huntTab['indice'];
                $huntTab['longitude'];
                $huntTab['latitude'];*/
                $link=$this->dm->database_link;
                $sqlHunt=mysql_query("INSERT INTO Hunt (Nom,NumIndice,Indice,Longitude,Latitude) VALUES (
                '".$huntTab['nomChasse']."',".$huntTab['numIndice'].",'".$huntTab['indice']."',
                ".$huntTab['longitude'].",".$huntTab['latitude']."",$link);
            }
            $this->dm->close();
            $output=array("retour"=>"success");
            print(json_encode($output));    
        }
    }  
$input=new InputTreasure();
$input->input();
?>