package data.entity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import data.Gestion;
import data.IData;
import data.fieldType;

public class Produit implements IData{
	private int idProduit;
	private String nom;
	private String desc;
	private String cate;
	
	private String values;
	private String valuesEq;
	private HashMap<String, fieldType> map;
	
	public Produit(int idProduit, String nom, String desc, String categorie) {
		super();
		this.idProduit = idProduit;
		this.nom = nom;
		this.desc = desc;
		this.cate = categorie;
		createStruct();
	}
	
	
	public Produit(String nom, String desc, String categorie) {
		super();
		this.idProduit = 0;
		this.nom = nom;
		this.desc = desc;
		this.cate = categorie;
		createStruct();
	}
	
	/**
	 * @brief constructeur à utiliser lors de la récupération des produits dans la base
	 * 
	 * @param rs ResultSet contenant de objet de la table produit
	 */
	public Produit(ResultSet rs) {
		super();
		
		createStruct();
		
		try {
			if(check(Gestion.structTable(rs.getMetaData().getTableName(1), false))) {
				this.idProduit = rs.getInt("id_produit");
				this.nom = rs.getString("nom");
				this.desc = rs.getString("description");
				this.cate = rs.getString("categorie");
			}else {
				System.err.println("Erreur: pas le bonne objet/table pour la récupération de produit");
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

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getCategorie() {
		return cate;
	}

	public void setCategorie(String categorie) {
		this.cate = categorie;
	}

	@Override
	public void createStruct() {
		map = new HashMap<String, fieldType>();
		
		map.put("id_produit", fieldType.BIGSERIAL);
		map.put("nom", fieldType.VARCHAR);
		map.put("description", fieldType.VARCHAR);
		map.put("categorie", fieldType.VARCHAR);
		
		values = "(nom, description, categorie) VALUES (?, ?, ?)"; 
		valuesEq = "(id_produit, nom, description, categorie) = (?, ?, ?, ?)"; 
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
			statement.setString(1, nom);
			statement.setString(2, desc);
			statement.setString(3, cate);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void composeStatementEq(PreparedStatement statement) {
		try {
			statement.setInt(1, idProduit);
			statement.setString(2, nom);
			statement.setString(3, desc);
			statement.setString(4, cate);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
