package data.entity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import data.Gestion;
import data.IData;
import data.fieldType;

public class Fournisseur implements IData{
	private String siret;
	private String nom;
	private String adresse;
	private String numeroTel;
	private String email;
	
	private String values;
	private String valuesEq;
	private HashMap<String, fieldType> map;
	
	@Override
	public void createStruct() {
		map = new HashMap<String, fieldType>();
	
		map.put("siret", fieldType.VARCHAR);
		map.put("nom", fieldType.VARCHAR);
		map.put("adresse", fieldType.VARCHAR);
		map.put("numero_tel", fieldType.VARCHAR);
		map.put("email", fieldType.VARCHAR);
		
		values = "(siret, nom, adresse, numero_tel, email) VALUES (?, ?, ?, ?, ?)"; 
		valuesEq = "(siret, nom, adresse, numero_tel, email) = (?, ?, ?, ?, ?)"; 
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
		if(map == null) {
			createStruct();
		}
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
			statement.setString(1, siret);
			statement.setString(2, nom);
			statement.setString(3, adresse);
			statement.setString(4, numeroTel);
			statement.setString(5, email);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void composeStatementEq(PreparedStatement statement) {
		try {
			statement.setString(1, siret);
			statement.setString(2, nom);
			statement.setString(3, adresse);
			statement.setString(4, numeroTel);
			statement.setString(5, email);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Fournisseur(String siret, String nom, String adresse, String numero_tel, String email) {
		super();
		this.siret = siret;
		this.nom = nom;
		this.adresse = adresse;
		this.numeroTel = numero_tel;
		this.email = email;
		createStruct();
	}
	
	/**
	 * @brief constructeur à utiliser lors de la récupération des fournisseurs dans la base
	 * 
	 * @param rs ResultSet contenant de objet de la tablea forunisseur
	 */
	public Fournisseur(ResultSet rs) {
		super();
		
		createStruct();
		
		try {
			if(check(Gestion.structTable(rs.getMetaData().getTableName(1), false))) {
				this.siret = rs.getString("siret");
				this.nom = rs.getString("nom");
				this.adresse = rs.getString("adresse");
				this.numeroTel = rs.getString("numero_tel");
				this.email = rs.getString("email");
			}else {
				System.err.println("Erreur: pas le bonne objet/table pour la récupération de fournisseur");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getSiret() {
		return siret;
	}

	public void setSiret(String siret) {
		this.siret = siret;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public String getNumero_tel() {
		return numeroTel;
	}

	public void setNumero_tel(String numero_tel) {
		this.numeroTel = numero_tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
}
