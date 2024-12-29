package swing_gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import tabs.*;

import data.Gestion;
import data.IData;
import data.entity.Achat;
import data.entity.Commande;
import java.awt.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ManagementSoftware {

	public static void loadMainApplication(String username) {

		// ==============================================================

		// Création de la fenêtre
		JFrame frame = new JFrame("AMS - Logiciel de gestion");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// ==============================================================

		// Création de la barre latérale
		JPanel sidebar = new JPanel();
		sidebar.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
		sidebar.setPreferredSize(new Dimension(300, 0));
		sidebar.setBackground(new Color(168, 213, 186)); // Vert menthe

		// ==============================================================

		// Affichage de l'utilisateur connecté
		JLabel userLabel = new JLabel("Utilisateur : " + username);
		userLabel.setFont(new Font("Arial", Font.BOLD, 20));
		userLabel.setForeground(new Color(74, 144, 226)); // Bleu moyen
		sidebar.add(userLabel);

		// ==============================================================

		// Affichage du rôle de l'utilisateur connecté
		JLabel roleLabel = new JLabel("Rôle : " + UserManager.getUserRole(username));
		roleLabel.setFont(new Font("Arial", Font.BOLD, 20));
		roleLabel.setForeground(new Color(194, 149, 69)); // Beige doré saturé
		sidebar.add(roleLabel);
		sidebar.add(Box.createVerticalStrut(50));

		// ==============================================================

		// Bouton pour accéder à la gestion des fournisseurs
		JButton inv = new JButton("Inventaire");
		inv.setBackground(new Color(255, 183, 77));
		sidebar.add(inv);
		sidebar.add(Box.createVerticalStrut(15));

		// ==============================================================

		// Bouton pour accéder à la gestion des fournisseurs
		JButton suppliers = new JButton("Fournisseurs");
		suppliers.setBackground(new Color(255, 183, 77));
		sidebar.add(suppliers);
		sidebar.add(Box.createVerticalStrut(15));

		// ==============================================================

		// Bouton pour accéder à la gestion des produits
		JButton products = new JButton("Produits");
		products.setBackground(new Color(255, 183, 77));
		sidebar.add(products);
		sidebar.add(Box.createVerticalStrut(15));

		// ==============================================================

		// Bouton pour accéder à la gestion des ventes
		JButton sales = new JButton("Ventes");
		sales.setBackground(new Color(255, 183, 77));
		sidebar.add(sales);
		sidebar.add(Box.createVerticalStrut(15));

		// ==============================================================

		// Bouton pour accéder à la gestion des contrats
		JButton contracts = new JButton("Contrats");
		contracts.setBackground(new Color(255, 183, 77));
		sidebar.add(contracts);
		sidebar.add(Box.createVerticalStrut(15));

		// ==============================================================

		// Bouton pour accéder à la gestion des commandes
		JButton orders = new JButton("Commandes/Achats");
		orders.setBackground(new Color(255, 183, 77));
		sidebar.add(orders);
		sidebar.add(Box.createVerticalStrut(15));

		// ==============================================================

		// Bouton pour accéder aux paramètres de l'application
		JButton parameters = new JButton("Paramètres");
		parameters.setBackground(new Color(255, 183, 77));
		sidebar.add(parameters);
		sidebar.add(Box.createVerticalStrut(15));

		// ==============================================================

		// Bouton pour se déconnecter
		JButton logout = new JButton("Déconnexion");
		logout.setBackground(new Color(102, 187, 106)); // Vert foncé
		sidebar.add(logout);
		sidebar.add(Box.createVerticalStrut(15));

		logout.addActionListener(e -> {
			frame.dispose();
			LoginApp.main(new String[] {}); // Revenir à la page de connexion
		});

		// ==============================================================

		// Contenu à l'ouverture de l'application
		JPanel contentPanel = new JPanel(new GridBagLayout());
		GridBagConstraints cst = new GridBagConstraints();

		cst.insets = new Insets(10, 10, 10, 10);

		JLabel titleLabel = new JLabel("Bienvenue sur le logiciel de gestion", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
		titleLabel.setForeground(new Color(51, 51, 51));
		cst.gridy = 0;
		cst.gridx = 0;
		contentPanel.add(titleLabel);

		// ==============================================================
		
		//inventaire
		inv.addActionListener(e -> {

			contentPanel.removeAll();

			InvTab.loadInvTab(contentPanel, frame);
			
		});
		// ==============================================================

		// Onglet des fournisseurs
		suppliers.addActionListener(e -> {

			contentPanel.removeAll(); // Rénitialise le contenu du contentPanel = page vide

			SuppliersTab.loadSuppliersTab(contentPanel, frame); // Utilisation du fichier SuppliersTab.java

		});

		// ==============================================================

		products.addActionListener(e -> {

			contentPanel.removeAll();

			ProductsTab.loadProductsTab(contentPanel, frame);

		});
		
		// ==============================================================

		sales.addActionListener(e -> {

			contentPanel.removeAll();

			SalesTab.loadSalesTab(contentPanel, frame);
			
		});
		
		// ==============================================================

		contracts.addActionListener(e -> {

			contentPanel.removeAll();
			
			ContractsTab.loadContractsTab(contentPanel, frame);

		});
		
		// ==============================================================

		// Onglet des commandes
		orders.addActionListener(e -> {

			contentPanel.removeAll();

			OrdersTab.loadOrdersTab(contentPanel, frame);
			
		});
		
		
		// ==============================================================

		// Quand on clique sur l'onglet des paramètres

		parameters.addActionListener(e -> {

			contentPanel.removeAll(); // Supprime tous les composants = page vide

			ParametersTab.loadParametersTab(contentPanel, frame);
			
		});
		
		// ==============================================================

		// Ajouter la sidebar et le contenu principal à la frame
		frame.add(sidebar, BorderLayout.WEST);
		frame.add(contentPanel, BorderLayout.CENTER);

		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximiser la fenêtre pour qu'elle occupe tout l'écran
		frame.setVisible(true); // Rendre la fenêtre visible

	};

};
