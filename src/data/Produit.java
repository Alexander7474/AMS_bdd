package data;

import java.util.HashMap;

public class Produit implements IData{
	private int id_produit;
	private String nom;
	private String desc;
	private String cate;
	
	private String values;
	private HashMap<String, fieldType> map;
	
	public Produit(int id_produit, String nom, String desc, String categorie) {
		super();
		this.id_produit = id_produit;
		this.nom = nom;
		this.desc = desc;
		this.cate = categorie;
		createStruct();
	}
	
	public Produit() {
		super();
		createStruct();
	}

	public int getIdProduit() {
		return id_produit;
	}

	public void setIdProduit(int id_produit) {
		this.id_produit = id_produit;
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
		
		values = "(" + id_produit + ",'" + nom + "','" + desc + "','" + cate + "')"; 
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

}
