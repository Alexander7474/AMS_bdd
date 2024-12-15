package data.entity;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import data.IData;
import data.fieldType;

public class Produit implements IData{
	private int idProduit;
	private String nom;
	private String desc;
	private String cate;
	
	private String values;
	private HashMap<String, fieldType> map;
	
	public Produit(int idProduit, String nom, String desc, String categorie) {
		super();
		this.idProduit = idProduit;
		this.nom = nom;
		this.desc = desc;
		this.cate = categorie;
		createStruct();
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
		
		map.put("id_produit", fieldType.INT4);
		map.put("nom", fieldType.VARCHAR);
		map.put("description", fieldType.VARCHAR);
		map.put("categorie", fieldType.VARCHAR);
		
		values = "(id_produit, nom, description, categorie) VALUES (?, ?, ?, ?)"; 
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
			statement.setString(2, nom);
			statement.setString(3, desc);
			statement.setString(4, cate);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
