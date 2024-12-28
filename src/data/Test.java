package data;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import data.entity.Contact;
import data.entity.Contrat;
import data.entity.Fournisseur;
import data.entity.Produit;
import data.entity.Vente;

public class Test {
	public static void main(String[] args) {
		
		// pour tester toutes les entités de la base
		Fournisseur fournisseur = new Fournisseur("14523652895412", "the market", "64 avenue de l'avenue", "0505050505", "themarket@market.fr");
		Produit produit = new Produit(1, "pomme", "c'est des pommes", "fruit");
		Contrat contrat = new Contrat("14523652895412", 1, 5.23, "2024-12-12", "2024-12-16");
		Contact contact = new Contact("jhon", "doe", "0606060606", "jhon.doe@gmail.com");
		Vente vente = new Vente(1, "2024-12-12", 5.23, 1.2);
		try {
			Gestion.displayTable("produit");
			System.out.println();
			Gestion.displayTable("fournisseur");
			System.out.println();
			Gestion.displayTable("contact");
			System.out.println();
			Gestion.displayTable("contrat");
			System.out.println();
			Gestion.displayTable("vente");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String testQuery = "SELECT * FROM vente";
		try {
			Statement statement = Connexion.getConnexion().createStatement();
			ResultSet rs = statement.executeQuery(testQuery);
			while(rs.next()) {
				Vente v = new Vente(rs);
				//System.out.println(v.getIdProduit());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
