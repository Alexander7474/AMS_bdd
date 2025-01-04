package data.entity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import data.Gestion;
import data.IData;
import data.fieldType;
import data.dependance_multi.ContactFournisseur;

public class Commande implements IData{
	private int idCommande;
	private int idProduit;
	private String siret;
	private double quantite;
	
	private String values;
	private String valuesEq;
	private HashMap<String, fieldType> map;
	
	@Override
	public void createStruct() {
		map = new HashMap<String, fieldType>();
	
		map.put("id_commande", fieldType.BIGSERIAL);
		map.put("id_produit", fieldType.INT4);
		map.put("siret", fieldType.VARCHAR);
		map.put("quantite", fieldType.FLOAT8);
		
		values = "(id_produit, siret, quantite) VALUES (?, ?, ?)"; 
		valuesEq = "(id_commande, id_produit, siret, quantite) VALUES (?, ?, ?, ?)";
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
			statement.setString(2, siret);
			statement.setDouble(3, quantite);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void composeStatementEq(PreparedStatement statement) {
		try {
			statement.setInt(1, idCommande);
			statement.setInt(2, idProduit);
			statement.setString(3, siret);
			statement.setDouble(4, quantite);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @brief constructeur à utiliser lors de la récupération des commandes dans la base
	 * 
	 * @param rs ResultSet contenant de objet de la table commande
	 */
	public Commande(ResultSet rs) {
		super();
		
		createStruct();
		
		try {
			if(check(Gestion.structTable(rs.getMetaData().getTableName(1), false))) {
				this.idCommande = rs.getInt("id_commande");
				this.idProduit = rs.getInt("id_produit");
				this.siret = rs.getString("siret");
				this.quantite = rs.getDouble("quantite");
			}else {
				System.err.println("Erreur: pas le bonne objet/table pour la récupération de commande");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override 
	public IData build(ResultSet rs) {
		return new Commande(rs);
	}

	public Commande(int idProduit, String siret, double quantite) {
		super();
		this.idProduit = idProduit;
		this.siret = siret;
		this.quantite = quantite;
		createStruct();
	}

	public Commande() {
		// TODO Auto-generated constructor stub
	}

	public int getIdProduit() {
		return idProduit;
	}

	public void setIdProduit(int idProduit) {
		this.idProduit = idProduit;
	}

	public String getSiret() {
		return siret;
	}

	public void setSiret(String siret) {
		this.siret = siret;
	}

	public double getQuantite() {
		return quantite;
	}

	public void setQuantite(double quantite) {
		this.quantite = quantite;
	}

	public int getIdCommande() {
		return idCommande;
	}
	
}
