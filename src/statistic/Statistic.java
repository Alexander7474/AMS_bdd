package statistic;

import java.sql.Date;
import java.sql.PreparedStatement;
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
		String query = "SELECT p.* FROM vente v "
				+ "JOIN lot_produit lp ON lp.id_lot_produit = v.id_lot_produit "
				+ "JOIN produit p ON p.id_produit = lp.id_produit "
				+ "GROUP BY p.id_produit "
				+ "ORDER BY SUM(v.quantite) DESC "
				+ "LIMIT  " + cnt;
		
		try(Statement statement = Connexion.getConnexion().createStatement()){
			try(ResultSet rs = statement.executeQuery(query)){
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
	
	/**
	 * @author Alexandre LANTERNIER
	 * @brief renvoie la quantité de produits vendue dans lordre des bestseller (dans la limit de cnt)
	 * 
	 * @param cnt
	 * @return
	 */
	public static Vector<Double> getBestSellerQua(int cnt){
		
		Vector<Double> vec = new Vector<Double>();		
		String query = "SELECT SUM(v.quantite) AS qua FROM vente v "
				+ "JOIN lot_produit lp ON lp.id_lot_produit = v.id_lot_produit "
				+ "JOIN produit p ON p.id_produit = lp.id_produit "
				+ "GROUP BY p.id_produit "
				+ "ORDER BY SUM(v.quantite) DESC "
				+ "LIMIT  " + cnt;
		
		try(Statement statement = Connexion.getConnexion().createStatement()){
			try(ResultSet rs = statement.executeQuery(query)){
				while(rs.next()) {
					Double n = rs.getDouble("qua");
					vec.add(n);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return vec;
	}
	
	/**
	 * @author Alexandre LANTERNIER
	 * @brief renvoie les produits bestsellers qui font le plus de CA (dans la limit de cnt)
	 * 
	 * @param cnt
	 * @return
	 */
	public static Vector<Produit> getBestSellerValue(int cnt){
		
		Vector<Produit> vec = new Vector<Produit>();		
		String query = "SELECT p.* FROM vente v "
				+ "JOIN lot_produit lp ON lp.id_lot_produit = v.id_lot_produit "
				+ "JOIN produit p ON p.id_produit = lp.id_produit "
				+ "GROUP BY p.id_produit "
				+ "ORDER BY SUM(v.prix_vente_uni * v.quantite) DESC "
				+ "LIMIT  " + cnt;
		
		try(Statement statement = Connexion.getConnexion().createStatement()){
			try(ResultSet rs = statement.executeQuery(query)){
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
	
	/**
	 * @author Alexandre LANTERNIER
	 * @brief renvoie la (quuantité * prix de vente) de produits vendue dans lordre des bestseller (dans la limit de cnt)
	 * 
	 * @param cnt
	 * @return
	 */
	public static Vector<Double> getBestSellerValueQua(int cnt){
		
		Vector<Double> vec = new Vector<Double>();		
		String query = "SELECT SUM(v.quantite * v.prix_vente_uni) AS qua FROM vente v "
				+ "JOIN lot_produit lp ON lp.id_lot_produit = v.id_lot_produit "
				+ "JOIN produit p ON p.id_produit = lp.id_produit "
				+ "GROUP BY p.id_produit "
				+ "ORDER BY SUM(v.quantite * v.prix_vente_uni) DESC "
				+ "LIMIT  " + cnt;
		
		try(Statement statement = Connexion.getConnexion().createStatement()){
			try(ResultSet rs = statement.executeQuery(query)){
				while(rs.next()) {
					Double n = rs.getDouble("qua");
					vec.add(n);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return vec;
	}
	
	/**
	 * @author Alexandre LANTERNIER
	 * @brief calcule le benéfice fait sur un jour
	 * 
	 * @param date
	 * @return
	 */
	public static double getJournalyBenefit(Date date) {
		double benefit = 0;
		
		String query = " SELECT SUM((v.prix_vente_uni * v.quantite)-(a.prix_achat_uni * v.quantite)) AS benefit FROM vente v \n"
				+ " JOIN lot_produit lp ON lp.id_lot_produit = v.id_lot_produit \n"
				+ " JOIN achat a ON a.id_achat = lp.id_achat \n"
				+ " WHERE v.date_vente = ?";
		
		try(PreparedStatement statement = Connexion.getConnexion().prepareStatement(query)){
			statement.setDate(1, date);
			try(ResultSet rs = statement.executeQuery()){
				while(rs.next()) {
					benefit = rs.getDouble("benefit");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return benefit;
	}
	
	/**
	 * @author Alexandre LANTERNIER
	 * @brief calcule le CA fait sur un jour
	 * 
	 * @param date
	 * @return
	 */
	public static double getJournalyCA(Date date) {
		double benefit = 0;
		
		String query = " SELECT SUM(v.prix_vente_uni * v.quantite) AS ca FROM vente v \n"
				+ " WHERE v.date_vente = ?";
		
		try(PreparedStatement statement = Connexion.getConnexion().prepareStatement(query)){
			statement.setDate(1, date);
			try(ResultSet rs = statement.executeQuery()){
				while(rs.next()) {
					benefit = rs.getDouble("ca");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return benefit;
	}
	
	public static double getJournalyPerte(Date date) {
		double perte = 0;
		
		String query = "SELECT v.quantite,lp.prix_vente_uni "
				+ "FROM vente v JOIN lot_produit lp ON lp.id_lot_produit = v.id_lot_produit "
				+ "WHERE v.prix_vente_uni = 0 AND date_vente = ?";
		
		try(PreparedStatement statement = Connexion.getConnexion().prepareStatement(query)){
			statement.setDate(1, date);
			try(ResultSet rs = statement.executeQuery()){
				while(rs.next()) {
					perte += rs.getDouble("quantite") * rs.getDouble("prix_vente_uni");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return perte;
	}
}
