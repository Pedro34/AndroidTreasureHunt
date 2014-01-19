AndroidTreasureHunt
===================

Application Android permettant de créer et de participer à une chasse aux trésors.

Détails techniques pour les développeurs
----------------------------------------
Si vous voulez tester notre application rien de plus simple:

1. Tout d'abord il faut évidemment importer le projet

2. Ensuite si vous ne posséder pas l'un des trois systèmes (tout dépend du système d'exploitation
que vous utilisez) WAMP, LAMP ou MAMP il faut que vous l'installiez.

3.Puis importer la BD avec l'utilitaire phpmyadmin prévu à cet effet avec le fichier lolo.sql
dans le répertoire util

4.Créer dans www(sous Windows et Linux) ou dans htdocs(sous Mac) un dossier TreasureHunt dans lequel
vous mettrez tous les fichiers php présents là encore dans util

5.Puis pour que le projet fonctionne correctement modifier l'IP local indiquée à la ligne 
40 et 41 de la classe DatabaseExternalManager (package treasureHunt.db). L'IP local correspond 
à l'IP attribuée par votre box ou le proxy pour votre ordinateur. 

6. Il ne faut pas oublier de modifier le fichier httpd.conf de Apache puis modifier le code à partir
de la ligne 268 en remplaçant par le code suivant (modifier évidemment la ligne changeante):
#    Require all granted
#   onlineoffline tag - don't remove
     Order Deny,Allow
     Allow from all
     Allow from 127.0.0.1
     Allow from ::1
     Allow from localhost

 7. Enfin n'oublier pas de laisser WAMP, LAMP ou MAMP tourner en tâche de fond sur votre 
 ordinateur, ainsi votre mobile pourra communiquer avec l'API REST PHP que nous avons 
 mis en place.
 