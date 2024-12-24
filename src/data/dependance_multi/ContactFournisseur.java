package data.dependance_multi;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import data.IData;
import data.fieldType;
import data.entity.Contact;
import data.entity.Fournisseur;

public class ContactFournisseur implements IData {
	private int idContact;
	private String siret;
	
	private String values;
	private HashMap<String, fieldType> map;
	
	@Override
	public void createStruct() {
		map = new HashMap<String, fieldType>();
	
		map.put("id_contact", fieldType.INT4);
		map.put("siret", fieldType.VARCHAR);
		
		values = "(id_contact, siret) VALUES (?, ?)"; 
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
	}
	
	public ContactFournisseur(Contact c, Fournisseur f) {
		super();
		this.idContact = c.getIdContact();
		this.siret = f.getSiret();
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
