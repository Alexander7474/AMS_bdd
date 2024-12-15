package data.entity;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import data.IData;
import data.fieldType;

public class Contrat implements IData{
	private int idContrat;
	private String siret;
	private int idProduit;
	private double prixUni;
	private Date dateDebut;
	private Date dateFin;
	
	private String values;
	private HashMap<String, fieldType> map;
	
	@Override
	public void createStruct() {
		map = new HashMap<String, fieldType>();
	
		map.put("id_contrat", fieldType.SERIAL);
		map.put("siret", fieldType.VARCHAR);
		map.put("id_produit", fieldType.INT4);
		map.put("prix_uni", fieldType.FLOAT8);
		map.put("date_debut", fieldType.DATE);
		map.put("date_fin", fieldType.DATE);
		
		values = "(siret, id_produit, prix_uni, date_debut, date_fin) VALUES (?, ?, ?, ?, ?)"; 
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
			statement.setString(1, siret);
			statement.setInt(2, idProduit);
			statement.setDouble(3, prixUni);
			statement.setDate(4, dateDebut);
			statement.setDate(5, dateFin);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @brief A utiliser pour récupérer un contrat depuis la base
	 * 
	 * @param idContrat
	 * @param siret
	 * @param idProduit
	 * @param prixUni
	 * @param dateDebut
	 * @param dateFin
	 */
	public Contrat(int idContrat, String siret, int idProduit, double prixUni, Date dateDebut, Date dateFin) {
		super();
		this.idContrat = idContrat;
		this.siret = siret;
		this.idProduit = idProduit;
		this.prixUni = prixUni;
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
		createStruct();
	}

	/**
	 * @brief A utiliser pour ajouter un contrat à la base
	 * 
	 * @param siret
	 * @param idProduit
	 * @param prixUni
	 * @param dateDebut
	 * @param dateFin
	 */
	public Contrat(String siret, int idProduit, double prixUni, String dateDebut, String dateFin) {
		super();
		this.siret = siret;
		this.idProduit = idProduit;
		this.prixUni = prixUni;
		this.dateDebut = Date.valueOf(dateDebut);
		this.dateFin = Date.valueOf(dateFin);
		createStruct();
	}

	public String getSiret() {
		return siret;
	}

	public void setSiret(String siret) {
		this.siret = siret;
	}

	public int getIdProduit() {
		return idProduit;
	}

	public void setIdProduit(int idProduit) {
		this.idProduit = idProduit;
	}

	public double getPrixUni() {
		return prixUni;
	}

	public void setPrixUni(double prixUni) {
		this.prixUni = prixUni;
	}

	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(String dateDebut) {
		this.dateDebut = Date.valueOf(dateDebut);
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(String dateFin) {
		this.dateFin = Date.valueOf(dateFin);
	}

	public int getIdContrat() {
		return idContrat;
	}
}
