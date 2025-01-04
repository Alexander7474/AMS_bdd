package data.entity;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import data.Gestion;
import data.IData;
import data.fieldType;

public class LotProduit implements IData {
	private int idLotProduit;
	private int idProduit;
	private double prixVenteUni;
	private double quantite;
	private Date peremption;
	private int idAchat;
		
	private String values;
	private String valuesEq;
	private HashMap<String, fieldType> map;
	
	@Override
	public void createStruct() {
		map = new HashMap<String, fieldType>();
	
		map.put("id_lot_produit", fieldType.BIGSERIAL);
		map.put("id_produit", fieldType.INT4);
		map.put("prix_vente_uni", fieldType.FLOAT8);
		map.put("quantite", fieldType.FLOAT8);
		map.put("peremption", fieldType.DATE);
		map.put("id_achat", fieldType.INT4);
		
		values = "(id_produit, prix_vente_uni, quantite, peremption, id_achat) VALUES (?, ?, ?, ?, ?)"; 
		valuesEq = "(id_lot_produit, id_produit, prix_vente_uni, quantite, peremption, id_achat) = (?, ?, ?, ?, ?, ?)";
	}

	@Override
	public String getValues() {
		// TODO Auto-generated method stub
		return values;
	}
	
	@Override
	public String getValuesEq() {
		// TODO Auto-generated method stub
		return valuesEq;
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
			statement.setDouble(2, prixVenteUni);
			statement.setDouble(3, quantite);
			statement.setDate(4, peremption);
			statement.setInt(5, idAchat);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void composeStatementEq(PreparedStatement statement) {
		try {
			statement.setInt(1, idLotProduit);
			statement.setInt(2, idProduit);
			statement.setDouble(3, prixVenteUni);
			statement.setDouble(4, quantite);
			statement.setDate(5, peremption);
			statement.setInt(6, idAchat);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @brief constructeur à utiliser lors de la récupération des lot de produits dans la base
	 * 
	 * @param rs ResultSet contenant de objet de la table lot de produit
	 */
	public LotProduit(ResultSet rs) {
		super();
		
		createStruct();
		
		try {
			if(check(Gestion.structTable(rs.getMetaData().getTableName(1), false))) {
				this.idLotProduit = rs.getInt("id_lot_produit");
				this.idProduit = rs.getInt("id_produit");
				this.prixVenteUni = rs.getDouble("prix_vente_uni");
				this.quantite = rs.getDouble("quantite");
				this.peremption = rs.getDate("peremption");
				this.idAchat = rs.getInt("id_achat");
			}else {
				System.err.println("Erreur: pas le bonne objet/table pour la récupération de produit");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override 
	public IData build(ResultSet rs) {
		return new LotProduit(rs);
	}

	public LotProduit(int idProduit, double prixVenteUni, double quantite, Date peremption, int idAchat) {
		super();
		this.idProduit = idProduit;
		this.prixVenteUni = prixVenteUni;
		this.quantite = quantite;
		this.peremption = peremption;
		this.idAchat = idAchat;
		createStruct();
	}
	
	

	public LotProduit(int idLotProduit, int idProduit, double prixVenteUni, double quantite, Date peremption, int idAchat) {
		super();
		this.idLotProduit = idLotProduit;
		this.idProduit = idProduit;
		this.prixVenteUni = prixVenteUni;
		this.quantite = quantite;
		this.peremption = peremption;
		this.idAchat = idAchat;
		createStruct();
	}

	public LotProduit() {
		// TODO Auto-generated constructor stub
	}

	public int getIdProduit() {
		return idProduit;
	}

	public void setIdProduit(int idProduit) {
		this.idProduit = idProduit;
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

	public Date getPeremption() {
		return peremption;
	}

	public void setPeremption(Date peremption) {
		this.peremption = peremption;
	}

	public int getIdLotProduit() {
		return idLotProduit;
	}

	public int getIdAchat() {
		return idAchat;
	}

	public void setIdAchat(int idAchat) {
		this.idAchat = idAchat;
	}
	
}
