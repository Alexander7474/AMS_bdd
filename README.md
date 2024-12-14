# AMS_bdd
Projet AMS java et modélisation de base de données 

# Setup 

Impossible de se connecter sur etd avec ssh donc on va utiliser notre propre server postegrsql et faudra tester à la fin sur pedago

### Installation postgresql

Installation   
``` sudo apt-get install postgresql-client postgresql ```   
Connection sur l'utilisateur postgre   
``` sudo -i -u postgre ```   
Connection à la base   
``` psql ```   
Creation d'un superutilisateur   
``` CREATE USER yourusername WITH PASSWORD password ```   
``` ALTER USER yourusername WITH SUPERUSER ```   
Creation d'un role pour utiliser psql depuis son compte linux local   
``` CREATE ROLE yourlocalusername WITH LOGIN ```   
``` ALTER ROLE yourlocaluserrname WITH SUPERUSER ```   

# Status