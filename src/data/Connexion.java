package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Connexion {
    private static final String URL = "jdbc:postgresql://localhost:5432/etd";
    private static final String USER = "uapv2401246";
    private static final String PASSWORD = "password";
    private static Connection connection;
    
    
    public static Connection getConnexion() throws SQLException{
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connexion réussie");
            } catch (SQLException e) {
                System.err.println("Erreur connexion :" + e.getMessage());
                throw e;
            }
        }else {
        	System.err.println("Connexion déjà établie");
        }
        return connection;
    }

    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connexion fermée.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur fermeture connexion :" + e.getMessage());
        }
    }

    
    public static void executeQuery(String query) {
        try (Connection c = getConnexion(); Statement stmt = c.createStatement()) {
            stmt.executeUpdate(query);
            System.out.println("Requête exécutée :" + query);
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'exécution :" + e.getMessage());
        }
    }
}