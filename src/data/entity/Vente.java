package data.entity;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import data.Gestion;
import data.IData;
import data.fieldType;

public class Vente implements IData {
	private int idVente;
	private int idProduit;
	private Date date;
	private double prixVenteUni;
	private double quantite;
	
	private String values;
	private HashMap<String, fieldType> map;
	
	@Override
	public void createStruct() {
		map = new HashMap<String, fieldType>();
	
		map.put("id_vente", fieldType.BIGSERIAL);
		map.put("id_produit", fieldType.INT4);
		map.put("date_vente", fieldType.DATE);
		map.put("prix_vente_uni", fieldType.FLOAT8);
		map.put("quantite", fieldType.FLOAT8);
		
		values = "(id_produit, date_vente, prix_vente_uni, quantite) VALUES (?, ?, ?, ?)"; 
	}
	
	@Override
	public HashMap<String, fieldType> getStruct() {
		return map;
	}


	@Override
	public String getValues() {
		// TODO Auto-generated method stub
		return values;
	}

	@Override
	public HashMap<String, fieldType> getMap() {
		// TODO Auto-generated method stub
		return map;
	}

	@Override
	public boolean check(HashMap<String, fieldType> tableStruct) {
		// TODO Auto-generated method stub
		return map.equals(tableStruct);
	}
	
	@Override
	public void composeStatement(PreparedStatement statement) {
		try {
			statement.setInt(1, idProduit);
			statement.setDate(2, date);
			statement.setDouble(3, prixVenteUni);
			statement.setDouble(4, quantite);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @brief constructeur à utiliser lors de la récupération des ventes dans la base
	 * 
	 * @param rs ResultSet contenant de objet de la tablea vente
	 */
	public Vente(ResultSet rs) {
		super();
		
		createStruct();
		
		try {
			if(check(Gestion.structTable(rs.getMetaData().getTableName(1), false))) {
				this.idVente = rs.getInt("id_vente");
				this.idProduit = rs.getInt("id_produit");
				this.date = rs.getDate("date");
				this.prixVenteUni = rs.getDouble("prix_vente_uni");
				this.quantite = rs.getDouble("quantite");
			}else {
				System.err.println("Erreur: pas le bonne objet/table pour la récupération de vente");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @brief Constructeur à utiliser créer une vente a ajouter dans la base
	 * 
	 * @param date
	 * @param prixVenteUni
	 * @param quantite
	 */
	public Vente(int idProduit, String date, double prixVenteUni, double quantite) {
		super();
		this.idProduit = idProduit;
		this.date = Date.valueOf(date);
		this.prixVenteUni = prixVenteUni;
		this.quantite = quantite;
		createStruct();
	}

	public int getIdProduit() {
		return idProduit;
	}

	public void setIdProduit(int idProduit) {
		this.idProduit = idProduit;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = Date.valueOf(date);;
	}

	public double getPrixVenteUni() {
		return prixVenteUni;
	}

	public void setPrixVenteUni(double prixVenteUni) {
		this.prixVenteUni = prixVenteUni;
	}

	public double getQuantite() {
		return quantite;
	}

	public void setQuantite(double quantite) {
		this.quantite = quantite;
	}

	public int getIdVente() {
		return idVente;
	}
	
}
