package swing_gui;

import javax.swing.*;
import java.awt.*;

public class ManagementSoftware {
	
    public static void loadMainApplication(String username) {
    	
        JFrame frame = new JFrame("AMS - Logiciel de gestion");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // ===================================================================================================

        // Barre latérale
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Color.LIGHT_GRAY);
        sidebar.setPreferredSize(new Dimension(200, 0));
        
        // ===================================================================================================

        // Utilisateur connecté
        JLabel userLabel = new JLabel("Utilisateur : " + username); // Afficher l'utilisateur
        sidebar.add(userLabel);
        sidebar.add(Box.createVerticalStrut(10)); // Créer un espacement
        
        // ===================================================================================================
        
        // Rôle de l'utilisateur connecté
        JLabel roleLabel = new JLabel("Rôle : " + UserManager.getUserRole(username)); // Afficher son rôle
        sidebar.add(roleLabel);
        sidebar.add(Box.createVerticalStrut(10));
        
        // ===================================================================================================

        // Onglet pour gérer des fournisseurs
        JButton fournisseurs = new JButton("Fournisseurs");
        sidebar.add(fournisseurs);
        sidebar.add(Box.createVerticalStrut(10));
        
        // ===================================================================================================
        
        // Onglet pour gérer les contacts
        JButton contacts = new JButton("Contacts");
        sidebar.add(contacts);
        sidebar.add(Box.createVerticalStrut(10));
        
        // ===================================================================================================
        
        // Onglet pour gérer les produits
        JButton produits = new JButton("Produits");
        sidebar.add(produits);
        sidebar.add(Box.createVerticalStrut(10));
        
        // ===================================================================================================
        
        // Onglet pour gérer les ventes
        JButton ventes = new JButton("Ventes");
        sidebar.add(ventes);
        sidebar.add(Box.createVerticalStrut(10));
        
        // ===================================================================================================
        
        // Onglet pour gérer les contrats
        JButton contrats = new JButton("Contrats");
        sidebar.add(contrats);
        sidebar.add(Box.createVerticalStrut(10));
        
        // ===================================================================================================
        
        // Onglet pour gérer les commandes
        JButton commandes = new JButton("Commandes");
        sidebar.add(commandes);
        sidebar.add(Box.createVerticalStrut(10));
        
        // ===================================================================================================
        
        // Onglet pour gérer les achats
        JButton achats = new JButton("Achats");
        sidebar.add(achats);
        sidebar.add(Box.createVerticalStrut(10));
        
        // ===================================================================================================
        
        // Contenu principal
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cst = new GridBagConstraints();
        
        cst.insets = new Insets(10, 10, 10, 10);
        
        // Texte au premier affichage du logiciel de gestion mais sera modifié pour y mettre l'onglet correspondant
        JLabel titleLabel = new JLabel("Bienvenue sur le logiciel de gestion", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        contentPanel.add(titleLabel);
        
        // ===================================================================================================
        
        // Modification du texte dans le contenu principal quand on clique sur l'onglet des fournisseurs
        fournisseurs.addActionListener(e -> {
        	titleLabel.setText("Gestion des fournisseurs");
        });
        
        // ===================================================================================================
        
        contacts.addActionListener(e -> {
        	titleLabel.setText("Gestion des contacts");
        });
        
        // ===================================================================================================
        
        produits.addActionListener(e -> {
        	titleLabel.setText("Gestion des produits");
        });
        
        // ===================================================================================================
        
        ventes.addActionListener(e -> {
        	titleLabel.setText("Gestion des ventes");
        });
        
        // ===================================================================================================
        
        contrats.addActionListener(e -> {
        	titleLabel.setText("Gestion des contrats");
        });
        
        // ===================================================================================================
        
        commandes.addActionListener(e -> {
        	titleLabel.setText("Gestion des commandes");
        });
        
        // ===================================================================================================
        
        achats.addActionListener(e -> {
        	titleLabel.setText("Gestion des achats");
        });
        
        // ===================================================================================================
        
        // Ajouter la sidebar et le contenu principal à la frame
        frame.add(sidebar, BorderLayout.WEST);
        frame.add(contentPanel, BorderLayout.CENTER);
        
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximiser la fenêtre pour qu'elle occupe tout l'écran
		frame.setVisible(true); // Rendre la fenêtre visible
        
    }
    
}
