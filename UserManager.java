package swing_gui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserManager {
	
	// ===================================================================================================
	
	public static boolean verifyUser(String username, String password) {
		
		String query = "SELECT password FROM utilisateur WHERE username = ?"; // Pourquoi username = ? 
		try (Connection myCon = Connexion.getConnexion(); // Création d'une connexion vers la base de donnée
				PreparedStatement myStmt = myCon.prepareStatement(query)) { // Lancer la requête
			myStmt.setString(1, username); // Pourquoi ?
			ResultSet rs = myStmt.executeQuery(); // Résultat de la requête
			if(rs.next()) { // Si il existe un résultat à cette requête
				String storedPassword = rs.getString("password"); // On récupère le mot de passe correspondant à l'utilisateur trouvé
				if(password.equals(storedPassword)) { // Si le mot de passe correspond avec celui entré par l'utilisateur
					return true; // Connexion valide
				}
				else {
					return false; // La connexion n'est pas valide
				}
			}
		} catch (Exception e) {
			System.err.println("Erreur de connexion au logiciel de gestion : " + e.getMessage());
		}
		return false;
	}
	
	// ===================================================================================================
	
	public static String getUserRole(String username) {
		String query = "SELECT nom FROM user_group g " + "JOIN utilisateur u ON g.id_group = u.id_group " + "WHERE u.username = ?";
		try (Connection conn = Connexion.getConnexion();
				PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				return rs.getString("nom");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Rôle inconnu";
	}
	
}
