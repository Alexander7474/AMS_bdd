package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import data.entity.Contrat;
import data.entity.LotProduit;
import data.entity.Vente;

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
		
		
		//on créé un statement pour faire notre requete
		try(Statement statement = Connexion.getConnexion().createStatement()){
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
	public static int insert(IData data, String table){
		
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
			try(PreparedStatement statement = Connexion.getConnexion().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);) {
				data.composeStatement(statement);
				statement.executeUpdate();
				ResultSet rs = statement.getGeneratedKeys();
				if(rs.next()) {
					return rs.getInt(1);
				}else {
					return -1;
				}
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
		
		return -1;
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
			System.err.println("Erreur lors de la suppression dans " + table + ", la map de la table ne correspond pas !");
			for(String str : tableMap.keySet()) {
				System.out.println(str + ": " + tableMap.get(str));
			}
			for(String str : data.getMap().keySet()) {
				System.out.println(str + ": " + data.getMap().get(str));
			}
		}
	}
	
	/**
	 * @brief Permet de mettre a jour un table de la base
	 * 
	 * @param newOne Nouvelle objet
	 * @param table Table ou mettre à jour
	 * @param sqlRef attribut SQL à utilisé pour déterminer l'objet a modifier
	 * @param id Valeur de cette attribut SQL (int)
	 */
	public static void update(IData newOne,String table, String sqlRef, int id) {
		//on récupère la map de filedType de la table
		HashMap<String, fieldType> tableMap = null;
		try {
			tableMap = structTable(table, false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(tableMap != null && newOne.check(tableMap)) {
			String query = "UPDATE " + table + " SET " + newOne.getValuesEq() + " WHERE " + sqlRef + " = " + id; // on n'utilise pas le ? car id n'est rentrée par l'utilisateur donc pas d'injection possible 
			try(PreparedStatement statement = Connexion.getConnexion().prepareStatement(query);) {
				newOne.composeStatementEq(statement);
				statement.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			System.err.println("Erreur lors de la mofifcation de " + table + ", la map de la table ne correspond pas !");
			for(String str : tableMap.keySet()) {
				System.out.println(str + ": " + tableMap.get(str));
			}
			for(String str : newOne.getMap().keySet()) {
				System.out.println(str + ": " + newOne.getMap().get(str));
			}
		}
	}
	
	/**
	 * @brief Permet de mettre a jour un table de la base
	 * 
	 * @param newOne Nouvelle objet
	 * @param table Table ou mettre à jour
	 * @param sqlRef attribut SQL à utilisé pour déterminer l'objet a modifier
	 * @param id Valeur de cette attribut SQL (String)
	 */
	public static void update(IData newOne,String table, String sqlRef, String id) {
		//on récupère la map de filedType de la table
		HashMap<String, fieldType> tableMap = null;
		try {
			tableMap = structTable(table, false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(tableMap != null && newOne.check(tableMap)) {
			String query = "UPDATE " + table + " SET " + newOne.getValuesEq() + " WHERE " + sqlRef + " = '" + id + "'"; // on n'utilise pas le ? car id n'est rentrée par l'utilisateur donc pas d'injection possible 
			try(PreparedStatement statement = Connexion.getConnexion().prepareStatement(query);) {
				newOne.composeStatementEq(statement);
				statement.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			System.err.println("Erreur lors de la mofifcation de " + table + ", la map de la table ne correspond pas !");
			for(String str : tableMap.keySet()) {
				System.out.println(str + ": " + tableMap.get(str));
			}
			for(String str : newOne.getMap().keySet()) {
				System.out.println(str + ": " + newOne.getMap().get(str));
			}
		}
	}
	
	/**
	 * @param table
	 * @param data Objet 
	 * @return
	 */
	public static Vector<IData> getAllFromTable(String table, IData data){
		Vector<IData> vec = new Vector<IData>();
		
		// recup des info dans la base
		try {
			Statement statement = Connexion.getConnexion().createStatement();
			try(ResultSet rs = statement.executeQuery("SELECT * FROM " + table)){
				while(rs.next()) {
					IData d = data.build(rs); // on build avec l'instance en paramètre pour utiliser le bon constructeur
					vec.add(d);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return vec;
	}
	
	/**
	 * @brief méthode à utiliser au démarrage de l'application pour faire certaine automatisation
	 * 
	 * @details Détection des lots de produits périmé(s) / ...
	 */
	public static void appStart() {
		//On vérifie tous les lots possiblement périmé pour en faire des ventes à 0€
		Vector<IData> invList = new Vector<>();
		
		// recup des info dans la base
		try {
			Statement statement = Connexion.getConnexion().createStatement();
			try(ResultSet rs = statement.executeQuery("SELECT * FROM lot_produit WHERE peremption < CURRENT_DATE")){
				while(rs.next()) {
					LotProduit lp = new LotProduit(rs);
					invList.add(lp);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(IData item : invList) {
			if(item instanceof LotProduit lot) {
				//déclarer une vente des produits restant à 0€
				Vente updatedVente = new Vente(lot.getIdLotProduit(),
						String.valueOf(LocalDate.now()), 0,
						lot.getQuantite());
				Gestion.insert(updatedVente, "vente");
				
				// Mise à jour dans la base de données pour changé la quantité du lot de produit
				lot.setQuantite(0);
				String query = "UPDATE lot_produit SET " + lot.getValuesEq() + " WHERE id_lot_produit = " + lot.getIdLotProduit();
				try(PreparedStatement statement = Connexion.getConnexion().prepareStatement(query)){
					lot.composeStatementEq(statement);
					statement.executeUpdate();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					System.err.println("Impossible de mettre a jour les lots de produit : ");
					e1.printStackTrace();
				}
			}
		}
	}
}
