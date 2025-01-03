package data.dependance_multi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import data.Gestion;
import data.IData;
import data.fieldType;
import data.entity.Contact;
import data.entity.Fournisseur;

public class ContactFournisseur implements IData {
	private int idContact;
	private String siret;
	
	private String values;
	private String valuesEq;
	private HashMap<String, fieldType> map;
	
	@Override
	public void createStruct() {
		map = new HashMap<String, fieldType>();
	
		map.put("id_contact", fieldType.INT4);
		map.put("siret", fieldType.VARCHAR);
		
		values = "(id_contact, siret) VALUES (?, ?)"; 
		valuesEq = "(id_contact, siret) = (?, ?)"; 
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
			statement.setInt(1, idContact);
			statement.setString(2, siret);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void composeStatementEq(PreparedStatement statement) {
		try {
			statement.setInt(1, idContact);
			statement.setString(2, siret);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ContactFournisseur(int idContact, String siret) {
		super();
		this.idContact = idContact;
		this.siret = siret;
		createStruct();
	}
	
	public ContactFournisseur(Contact c, Fournisseur f) {
		super();
		this.idContact = c.getIdContact();
		this.siret = f.getSiret();
		createStruct();
	}
	
	/**
	 * @brief constructeur à utiliser lors de la récupération des liaison contact-forunisseur dans la base
	 * 
	 * @param rs ResultSet contenant de objet de la table
	 */
	public ContactFournisseur(ResultSet rs) {
		super();
		
		createStruct();
		
		try {
			if(check(Gestion.structTable(rs.getMetaData().getTableName(1), false))) {
				this.idContact = rs.getInt("id_contact");
				this.siret = rs.getString("siret");
			}else {
				System.err.println("Erreur: pas le bonne objet/table pour la récupération de liaison contact fourniseur");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override 
	public IData build(ResultSet rs) {
		return new ContactFournisseur(rs);
	}

	public int getIdContact() {
		return idContact;
	}

	public void setIdContact(int idContact) {
		this.idContact = idContact;
	}

	public String getSiret() {
		return siret;
	}

	public void setSiret(String siret) {
		this.siret = siret;
	}

	
}
