package data;

import java.sql.SQLException;
import java.util.HashMap;

public class Test {
	public static void main(String[] args) {
		Gestion gestion = new Gestion();
		
		Produit produit = new Produit();
		try {
			HashMap<String, fieldType> map = gestion.structTable("fournisseur", false);
			for(String key : map.keySet()) {
				System.out.println("column : " + key + " fieldType: " + map.get(key));
			}
			
			System.out.println(produit.check(map));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
