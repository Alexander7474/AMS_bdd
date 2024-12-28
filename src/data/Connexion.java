package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Connexion {
    private static final String URL = "jdbc:postgresql://localhost:5432/etd";
    private static final String USER = "florent";
    private static final String PASSWORD = "mypassword";
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
}