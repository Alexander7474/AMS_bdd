package swing_gui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest; // Crypte les données sensisbles (ex : mots de passe)

import java.util.ArrayList; // Renvoie les utilisateurs 
import java.util.List;

import data.Connexion;

public class UserManager {

	// ==============================================================

	// Hachage du mot de passe avec SHA-256

	private static String hashPassword(String password) {

		try {

			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hashBytes = digest.digest(password.getBytes("UTF-8"));
			StringBuilder hexString = new StringBuilder();

			for (byte b : hashBytes) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}

			return hexString.toString();

		} catch (Exception e) {
			throw new RuntimeException("Erreur lors du hachage du mot de passe", e);
		}

	}

	// ==============================================================

	// Vérifier qu'un utilisateur existe bien dans la table

	public static boolean verifyUser(String username, String password) {
		String query = "SELECT password FROM utilisateur WHERE username = ?";

		try (Connection conn = Connexion.getConnexion(); // Création d'une connexion vers la base de donnée
				PreparedStatement stmt = conn.prepareStatement(query)) { // Lance la requête

			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery(); // Contient le résultat de la requête

			if (rs.next()) { // Si il existe un résultat à cette requête
				String storedPassword = rs.getString("password"); // Récupération du mot de passe correspondant à
																	// l'utilisateur trouvé
				String hashedPassword = hashPassword(password); // Hachage du mot de passe entré par l'utilisateur

				if (hashedPassword.equals(storedPassword))
					return true; // Connexion valide si le mot de passe correspond avec celui entré par
									// l'utilisateur
				else
					return false; // Connexion invalide
			}

		} catch (Exception e) {
			System.err.println("Erreur de connexion au logiciel de gestion : " + e.getMessage());
		}

		return false;

	}

	// ==============================================================

	// Récupérer le rôle d'un utilisateur

	public static String getUserRole(String username) {

		String query = "SELECT nom FROM user_group g " + "JOIN utilisateur u ON g.id_group = u.id_group "
				+ "WHERE u.username = ?";

		try (Connection conn = Connexion.getConnexion(); PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();

			if (rs.next())
				return rs.getString("nom");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "Rôle inconnu";

	}

	// ==============================================================

	// Création d'un nouvel utilisateur

	public static boolean addUser(String newUserUsername, String newUserPassword) {

		String hashedPassword = hashPassword(newUserPassword); // Hache le mot de passe
		String query = "INSERT INTO utilisateur (username, password, id_group) VALUES (?, ?, ?)";

		try (Connection conn = Connexion.getConnexion(); PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setString(1, newUserUsername);
			stmt.setString(2, hashedPassword);
			stmt.setInt(3, 2); // 2 pour employé car 1 = admin (première ligne de la table) -> on part du
								// principe qu'il n'y a qu'un seul admin

			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0; // Renvoie vrai si l'utilisateur a bien été ajouté

		} catch (Exception e) {
			System.err.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
		}

		return false;

	}

	// ==============================================================

	// Modifier le mot de passe d'un utilisateur

	public static boolean updatePassword(String username, String newPassword) {

		String hashedPassword = hashPassword(newPassword); // Hache le nouveau mot de passe
		String query = "UPDATE utilisateur SET password = ? WHERE username = ?";

		try (Connection conn = Connexion.getConnexion(); PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setString(1, hashedPassword);
			stmt.setString(2, username);

			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0; // Renvoie vrai si le mot de passe a bien été modifié

		} catch (Exception e) {
			System.err.println("Erreur lors de la modification du mot de passe : " + e.getMessage());
		}

		return false;

	}

	// ==============================================================

	// Récupérer tous les utilisateurs existants et leurs mots de passe

	public static List<String[]> getAllUsers() {

		String query = "SELECT username, password FROM utilisateur";
		List<String[]> users = new ArrayList<>();

		try (Connection conn = Connexion.getConnexion();
				PreparedStatement stmt = conn.prepareStatement(query);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				String username = rs.getString("username");
				String password = rs.getString("password");
				users.add(new String[] { username, password });
			}

		} catch (Exception e) {
			System.err.println("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
		}

		return users;

	}

	// ==============================================================

	// Supprimer un utilisateur donné

	public static boolean deleteUser(String username) {
		String query = "DELETE FROM utilisateur WHERE username = ?";
		try (Connection conn = Connexion.getConnexion(); PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setString(1, username);
			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;

		} catch (Exception e) {
			System.err.println("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
		}

		return false;

	}

	// ==============================================================

	// Savoir si un nom d'utilisateur est déjà utilisé

	public static boolean isUsernameAlreadyTaken(String username) {
		String query = "SELECT COUNT(username) FROM utilisateur WHERE username = ?";
		try (Connection conn = Connexion.getConnexion(); PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

}
