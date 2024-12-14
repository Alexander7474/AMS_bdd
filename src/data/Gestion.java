package data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class Gestion {
	/**
	 * @brief Renvoie la une hashmap donnant la structure d'une table de etd
	 * @details A utiliser avec la strutcure de nos class pour verfifier qu'une class va bien dans une table
	 * 
	 * @param table
	 * @param display
	 * 
	 * @return map
	 * @throws SQLException
	 */
	public HashMap<String, fieldType> structTable(String table, boolean display) throws SQLException {
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
            System.err.println("Erreur creation prepared statement:" + e.getMessage());
            throw e;
		}
	}
	
	public void close() {
		Connexion.close();
	}
}
