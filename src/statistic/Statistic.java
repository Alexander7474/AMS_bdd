package statistic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import data.Connexion;
import data.entity.*;

public class Statistic {
	
	/**
	 * @author Alexandre LANTERNIER
	 * @brief renvoie les produits bestsellers (dans la limit de cnt)
	 * 
	 * @param cnt
	 * @return
	 */
	public static Vector<Produit> getBestSeller(int cnt){
		
		Vector<Produit> vec = new Vector<Produit>();		
		String query = "SELECT p.* FROM vente v JOIN produit p ON p.id_produit = v.id_produit GROUP BY p.id_produit ORDER BY SUM(v.prix_vente_uni * v.quantite) DESC LIMIT " + cnt;
		
		try(Statement statement = Connexion.getConnexion().createStatement()){
			try(ResultSet rs = statement.getResultSet()){
				while(rs.next()) {
					Produit p = new Produit(rs);
					vec.add(p);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return vec;
	}
}
