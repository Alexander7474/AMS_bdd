package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 * @author Alexandre LANTERNIER
 */
public class Gestion {
	
	/**
	 * @author Alexandre LANTERNIER
	 * @brief Renvoie la une hashmap donnant la structure d'une table de etd
	 * @details A utiliser avec la strutcure de nos class pour verfifier qu'une class va bien dans une table
	 * 
	 * @param table
	 * @param display
	 * 
	 * @return map
	 * @throws SQLException
	 */
	public static HashMap<String, fieldType> structTable(String table, boolean display) throws SQLException {
		//requete pour recup les info structurelle d'une table
		String request = "SELECT * FROM " + table + " LIMIT 0";
		
		//on ouver une connection à la db
		Connection connection = Connexion.getConnexion();
		
		//on créé un statement pour faire notre requete
		try(Statement statement = connection.createStatement()){
            try (ResultSet resultSet = statement.executeQuery(request)) {
            	if(display) {
            		System.out.println("Structure de la table : " + table);
                	System.out.println("-------------------------------------");
            	}
            	
            	//recup des métadonnées
            	ResultSetMetaData metaData = resultSet.getMetaData();
            	int columnCount = metaData.getColumnCount();
            	
            	//creation de la map à renvoyer
            	HashMap<String, fieldType> map = new HashMap<String, fieldType>();
            	
                // Parcourir les résultats
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String columnType = metaData.getColumnTypeName(i);
                    int columnSize = metaData.getColumnDisplaySize(i);
                    
                    //ajout dans la map
                    map.put(columnName, fieldType.getSqlType(columnType));
                    
                    //affichage des type
                    if(display)
                    	System.out.printf("Colonne: %s | Type: %s | Taille: %d%n",
                    			columnName, columnType, columnSize);
                }
                
                return map;
            }
		}catch (SQLException e) {
            System.err.println("Erreur creation "
            		+ "statement:" + e.getMessage());
            throw e;
		}
	}
	
	/**
	 * @author Alexandre LANTERNIER
	 * @brief Affiche une table
	 * 
	 * @param table
	 * @throws SQLException
	 */
	public static void displayTable(String table) throws SQLException {
		//requete pour recup les info structurelle d'une table
		String request = "SELECT * FROM "+table;
		
		//on ouver une connection à la db
		Connection connection = Connexion.getConnexion();
		
		//on créé un statement pour faire notre requete
		try(Statement statement = connection.createStatement()){
            
            try(ResultSet resultSet = statement.executeQuery(request)){
            	
            	//recup des métadonnées
            	ResultSetMetaData metaData = resultSet.getMetaData();
            	int columnCount = metaData.getColumnCount();
            	
            	// on affiche le nom des colonnes 
            	String toShow = "";
            	int colSize = 25;
            	for (int i = 1; i <= columnCount; i++) {
                    toShow+= StringTool.changeStringToSize(metaData.getColumnName(i),colSize) + " | ";
                }
            	System.out.println(toShow);
            	
            	//parcourir les colonnes
                while (resultSet.next()) {
                	toShow = "";
                	
                	// Parcourir la ligne
                    for (int i = 1; i <= columnCount; i++) {
                    	
                    	// on affiche le contenue de chaque ligne 
                        toShow+= StringTool.changeStringToSize(resultSet.getString(metaData.getColumnName(i)), colSize) + " | ";
                    }

                    System.out.println(toShow);
                }
                
            }
		}catch (SQLException e) {
            System.err.println("Erreur creation statement:" + e.getMessage());
            throw e;
		}
	}
	
	/**
	 * @author Alexandre LANTERNIER
	 * @brief Execute une requête autre que insert
	 * 
	 * @param query
	 * @throws SQLException
	 */
	public static void execute(String query) throws SQLException {
		
		if(query.contains("INSERT")) {
			System.err.println("Erreur requếte insert dans execute: illegal");
			return;
		}
		
		//on ouver une connection à la db
		Connection connection = Connexion.getConnexion();
		
		//on créé un statement pour faire notre requete
		try(Statement statement = connection.createStatement()){
            statement.executeQuery(query);
		}catch (SQLException e) {
            System.err.println("Erreur creation statement:" + e.getMessage());
            throw e;
		}
	}
	
	/**
	 * @author Alexandre LANTERNIER
	 * @brief insert un objet data dans une table
	 * 
	 * @param data
	 * @param table
	 */
	public static void insert(IData data, String table){
		
		//on récupère la map de filedType de la table
		HashMap<String, fieldType> tableMap = null;
		try {
			tableMap = structTable(table, false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// si la table est bien celle qui stock la data
		if(tableMap != null && data.check(tableMap)) {
			String query = "INSERT INTO "+ table + " " + data.getValues();
			try(PreparedStatement statement = Connexion.getConnexion().prepareStatement(query);) {
				data.composeStatement(statement);
				statement.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			System.err.println("Erreur lors de l'insertion dans " + table + ", la map de la table ne correspond pas !");
			for(String str : tableMap.keySet()) {
				System.out.println(str + ": " + tableMap.get(str));
			}
			for(String str : data.getMap().keySet()) {
				System.out.println(str + ": " + data.getMap().get(str));
			}
		}
	}
	
	public static void delete(IData data, String table) {
		
		//on récupère la map de filedType de la table
		HashMap<String, fieldType> tableMap = null;
		try {
			tableMap = structTable(table, false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// si la table est bien celle qui stock la data
		if(tableMap != null && data.check(tableMap)) {
			String query = "DELETE FROM "+ table + " WHERE " + data.getValuesEq();
			try(PreparedStatement statement = Connexion.getConnexion().prepareStatement(query);) {
				data.composeStatementEq(statement);
				statement.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			System.err.println("Erreur lors de la suppression " + table + ", la map de la table ne correspond pas !");
			for(String str : tableMap.keySet()) {
				System.out.println(str + ": " + tableMap.get(str));
			}
			for(String str : data.getMap().keySet()) {
				System.out.println(str + ": " + data.getMap().get(str));
			}
		}
	}
	
	public static void update(IData oldOne, IData newOne, String table) {
		//on récupère la map de filedType de la table
		HashMap<String, fieldType> tableMap = null;
		try {
			tableMap = structTable(table, false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// si la table est bien celle qui stock la data
		if(tableMap != null && oldOne.check(tableMap) && newOne.check(tableMap)) {
			String query = "UPDATE "+ table + " SET " + newOne.getValuesEq() + " WHERE " + oldOne.getValuesEq();
			try(PreparedStatement statement = Connexion.getConnexion().prepareStatement(query);) {
				newOne.composeStatementEq(statement);
				statement.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			System.err.println("Erreur lors de la suppression " + table + ", la map de la table ne correspond pas !");
			for(String str : tableMap.keySet()) {
				System.out.println(str + ": " + tableMap.get(str));
			}
			for(String str : oldOne.getMap().keySet()) {
				System.out.println(str + ": " + oldOne.getMap().get(str));
			}

			for(String str : newOne.getMap().keySet()) {
				System.out.println(str + ": " + newOne.getMap().get(str));
			}
		}
	}
	
}
