package data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

public class Gestion {
	public HashMap<String, fieldType> structTable(String table, boolean display) throws SQLException {
        try {
            Connection c = Connexion.getConnexion();

            String query = "INSERT INTO produit (nom,description,categorie) VALUES ('chips','desc','cate')";
            Connexion.executeQuery(query);

            Connexion.fermer();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return null;
	}
}
