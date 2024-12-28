package data.dependance_multi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import data.Gestion;
import data.IData;
import data.fieldType;
import data.entity.Fournisseur;
import data.entity.Produit;

public class ProduitFournisseur implements IData{
	private int idProduit;
	private String siret;
	private double prixVenteUni;
	
	private String values;
	private String valuesEq;
	private HashMap<String, fieldType> map;
	
	@Override
	public void createStruct() {
		map = new HashMap<String, fieldType>();
	
		map.put("id_produit", fieldType.INT4);
		map.put("siret", fieldType.VARCHAR);
		map.put("prix_vente_uni", fieldType.FLOAT8);
		
		values = "(id_produit, siret, prix_vente_uni) VALUES (?, ?, ?)"; 

		valuesEq = "(id_produit, siret, prix_ventre_uni) = (?, ?, ?)"; 
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void composeStatementEq(PreparedStatement statement) {
		try {
			statement.setInt(1, idProduit);
			statement.setString(2, siret);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ProduitFournisseur(int idProduit, String siret) {
		super();
		this.idProduit = idProduit;
		this.siret = siret;
		createStruct();
	}
	
	public ProduitFournisseur(Produit p, Fournisseur f) {
		super();
		this.idProduit = p.getIdProduit();
		this.siret = f.getSiret();
		createStruct();
	}
	
	/*
	 * @brief constructeur à utiliser lors de la récupération des liaison produit-forunisseur dans la base
	 * 
	 * @param rs ResultSet contenant de objet de la table
	 */
	public ProduitFournisseur(ResultSet rs) {
		super();
		
		createStruct();
		
		try {
			if(check(Gestion.structTable(rs.getMetaData().getTableName(1), false))) {
				this.idProduit = rs.getInt("id_produit");
				this.siret = rs.getString("siret");
			}else {
				System.err.println("Erreur: pas le bonne objet/table pour la récupération de liaison produit fourniseur");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	public double getPrixVenteUni() {
		return prixVenteUni;
	}

	public void setPrixVenteUni(double prixVenteUni) {
		this.prixVenteUni = prixVenteUni;
	}
	
	

}
