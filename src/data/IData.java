package data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

//LES CLASSES IMPLEMENTANT CETTE INTERFACE DOIVENT DISPOSEES DES ATTRIBUTS SUPPLEMENTAIRES : private String values ; private HashMap<String, fieldType> map;

public interface IData {
	//REMPLIE DANS LA CLASSE UNE HashMap<String, fieldType> LE NOM DES CHAMPS ET LE TYPE DE VARIABLE ET CREE LA CHAINE values ...
	public void createStruct();
	
	//RETOURNE UNE CHAINE DE CARACTERE PRE-REMPLIE PERMETTANT DE COMPOSER LA REQUETE INSERT ...
	public String getValues();
	
	//RETOURNE UNE CHAINE DE CARACTERE PRE-REMPLIE PERMETTANT DE COMPOSER UNE COMPARAISON DANS LA REQUETE
	public String getValuesEq();
	
	//remplie un statement avec ces valeurs
	public void composeStatement(PreparedStatement statement);
	public void composeStatementEq(PreparedStatement statement);
	
	//permet de build un objet IData à partir des résultat d'une requête SQL
	public IData build(ResultSet rs);
	
	//GETTER DE LA MAP CREE AVEC LA METHODE getStruct ...
	public HashMap<String, fieldType> getMap();
	
	//METHODE PERMETTANT DE VERIFIER QUE LA TABLE ET L'INSTANCE PARTAGE LES MEMES ATTRIBUTS ET MEMES TYPES
	//PREND EN PARAMETRE LA MAP ATTRIBUT/TYPE DE LA TABLE ...
	public boolean check(HashMap<String, fieldType> tableStruct);
}
