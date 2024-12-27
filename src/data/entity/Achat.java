package data.entity;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import data.Gestion;
import data.IData;
import data.fieldType;

public class Achat implements IData{
	private int idAchat;
	private int idCommande;
	private double prixAchatUni;
	private Date dateAchat;
	
	private String values;
	private String valuesEq;
	private HashMap<String, fieldType> map;
	
	@Override
	public void createStruct() {
		map = new HashMap<String, fieldType>();
	
		map.put("id_achat", fieldType.BIGSERIAL);
		map.put("id_commande", fieldType.INT4);
		map.put("prix_achat_uni", fieldType.FLOAT8);
		map.put("date_achat", fieldType.DATE);
		
		values = "(id_commande, prix_achat_uni, date_achat) VALUES (?, ?, ?)"; 
		valuesEq = "(id_achat, id_commande, prix_achat_uni, date_achat) = (?, ?, ?, ?)"; 
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
			statement.setInt(1, idCommande);
			statement.setDouble(2, prixAchatUni);
			statement.setDate(3, dateAchat);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void composeStatementEq(PreparedStatement statement) {
		try {
			statement.setInt(1, idAchat);
			statement.setInt(2, idCommande);
			statement.setDouble(3, prixAchatUni);
			statement.setDate(4, dateAchat);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @brief constructeur à utiliser lors de la récupération des achats dans la base
	 * 
	 * @param rs ResultSet contenant de objet de la table achat
	 */
	public Achat(ResultSet rs) {
		super();
		
		createStruct();
		
		try {
			if(check(Gestion.structTable(rs.getMetaData().getTableName(1), false))) {
				this.idAchat = rs.getInt("id_achat");
				this.idCommande = rs.getInt("id_commande");
				this.prixAchatUni = rs.getDouble("prix_achat_uni");
				this.dateAchat = rs.getDate("date_achat");
			}else {
				System.err.println("Erreur: pas le bonne objet/table pour la récupération d'achat");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Achat(int idCommande, double prixAchatUni, Date dateAchat) {
		super();
		this.idCommande = idCommande;
		this.prixAchatUni = prixAchatUni;
		this.dateAchat = dateAchat;
		
		createStruct();
	}

	public int getIdCommande() {
		return idCommande;
	}

	public void setIdCommande(int idCommande) {
		this.idCommande = idCommande;
	}

	public double getPrixAchatUni() {
		return prixAchatUni;
	}

	public void setPrixAchatUni(double prixAchatUni) {
		this.prixAchatUni = prixAchatUni;
	}

	public Date getDateAchat() {
		return dateAchat;
	}

	public void setDateAchat(Date dateAchat) {
		this.dateAchat = dateAchat;
	}

	public int getIdAchat() {
		return idAchat;
	}
	
}
