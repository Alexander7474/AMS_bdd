package data.dependance_multi;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import data.IData;
import data.fieldType;
import data.entity.Fournisseur;
import data.entity.Produit;

public class ProduitFournisseur implements IData{
	private int idProduit;
	private String siret;
	
	private String values;
	private HashMap<String, fieldType> map;
	
	@Override
	public void createStruct() {
		map = new HashMap<String, fieldType>();
	
		map.put("id_produit", fieldType.INT4);
		map.put("siret", fieldType.VARCHAR);
		
		values = "(id_produit, siret) VALUES (?, ?)"; 
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
	}
	
	public ProduitFournisseur(Produit p, Fournisseur f) {
		super();
		this.idProduit = p.getIdProduit();
		this.siret = f.getSiret();
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

}
