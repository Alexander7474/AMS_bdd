package data.entity;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import data.IData;
import data.fieldType;

public class Contact implements IData{
	private int idContact;
	private String nom;
	private String prenom;
	private String numeroTel;
	private String email;
	
	private String values;
	private HashMap<String, fieldType> map;
	
	@Override
	public void createStruct() {
		map = new HashMap<String, fieldType>();
	
		map.put("id_contact", fieldType.SERIAL);
		map.put("nom", fieldType.VARCHAR);
		map.put("prenom", fieldType.VARCHAR);
		map.put("numero_tel", fieldType.VARCHAR);
		map.put("email", fieldType.VARCHAR);
		
		values = "(nom, prenom, numero_tel, email) VALUES (?, ?, ?, ?)"; 
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
			statement.setString(1, nom);
			statement.setString(2, prenom);
			statement.setString(3, numeroTel);
			statement.setString(4, email);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Contact(String nom, String prenom, String numeroTel, String email) {
		super();
		this.nom = nom;
		this.prenom = prenom;
		this.numeroTel = numeroTel;
		this.email = email;
		createStruct();
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getNumeroTel() {
		return numeroTel;
	}

	public void setNumeroTel(String numeroTel) {
		this.numeroTel = numeroTel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getIdContact() {
		return idContact;
	}
}
