package swing_gui;

import javax.swing.*;

import data.Connexion;
import data.Gestion;
import data.IData;
import data.entity.LotProduit;
import data.entity.Vente;
import tabs.*;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class ManagementSoftware {

	public static void loadMainApplication(String username) {
		
		//On vérifie tous les lots possiblement périmé pour en faire des ventes à 0€

		List<IData> invList = new ArrayList<>();
		// recup des info dans la base
		try {
			Statement statement = Connexion.getConnexion().createStatement();
			try(ResultSet rs = statement.executeQuery("SELECT * FROM lot_produit WHERE peremption < CURRENT_DATE")){
				while(rs.next()) {
					LotProduit lp = new LotProduit(rs);
					invList.add(lp);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(IData item : invList) {
			if(item instanceof LotProduit lot) {
				//déclarer une vente des produits restant à 0€
				Vente updatedVente = new Vente(lot.getIdLotProduit(),
						String.valueOf(LocalDate.now()), 0,
						lot.getQuantite());
				Gestion.insert(updatedVente, "vente");
				
				// Mise à jour dans la base de données pour changé la quantité du lot de produit
				lot.setQuantite(0);
				String query = "UPDATE lot_produit SET " + lot.getValuesEq() + " WHERE id_lot_produit = " + lot.getIdLotProduit();
				try(PreparedStatement statement = Connexion.getConnexion().prepareStatement(query)){
					lot.composeStatementEq(statement);
					statement.executeUpdate();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					System.err.println("Impossible de mettre a jour les lots de produit : ");
					e1.printStackTrace();
				}
			}
		}

		// ==============================================================

		// Création de la fenêtre
		JFrame frame = new JFrame("AMS - Logiciel de gestion");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// ==============================================================

		// Création de la barre latérale
		JPanel sidebar = new JPanel();
		sidebar.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 15));
		sidebar.setPreferredSize(new Dimension(200, 0));
		sidebar.setBackground(new Color(168, 213, 186)); // Vert menthe

		// ==============================================================

		// Affichage de l'utilisateur connecté
		JLabel userLabel = new JLabel("Utilisateur : " + username);
		userLabel.setFont(new Font("Arial", Font.BOLD, 20));
		sidebar.add(userLabel);

		// ==============================================================

		// Affichage du rôle de l'utilisateur connecté
		JLabel roleLabel = new JLabel("Rôle : " + UserManager.getUserRole(username));
		roleLabel.setFont(new Font("Arial", Font.BOLD, 20));
		sidebar.add(roleLabel);

		// ==============================================================

		// Bouton pour accéder à la gestion des fournisseurs
		JButton inv = new JButton("Inventaire/Lots");
		inv.setBackground(new Color(255, 183, 77));
		sidebar.add(inv);
		sidebar.add(Box.createVerticalStrut(15));

		// ==============================================================

		// Bouton pour accéder à la gestion des fournisseurs
		JButton suppliers = new JButton("Fournisseurs/Contacts");
		suppliers.setBackground(new Color(255, 183, 77));
		sidebar.add(suppliers);

		// ==============================================================

		// Bouton pour accéder à la gestion des produits
		JButton products = new JButton("Fournisseurs/Produits");
		products.setBackground(new Color(255, 183, 77));
		sidebar.add(products);

		// ==============================================================

		// Bouton pour accéder à la gestion des ventes
		JButton sales = new JButton("Ventes");
		sales.setBackground(new Color(255, 183, 77));
		sidebar.add(sales);

		// ==============================================================

		// Bouton pour accéder à la gestion des contrats
		JButton contracts = new JButton("Contrats");
		contracts.setBackground(new Color(255, 183, 77));
		sidebar.add(contracts);

		// ==============================================================

		// Bouton pour accéder à la gestion des commandes
		JButton orders = new JButton("Commandes/Achats");
		orders.setBackground(new Color(255, 183, 77));
		sidebar.add(orders);

		// ==============================================================

		// Bouton pour accéder aux paramètres de l'application
		JButton stats = new JButton("Statistiques");
		stats.setBackground(new Color(255, 183, 77));
		sidebar.add(stats);
				
		// ==============================================================

		// Bouton pour accéder aux paramètres de l'application
		JButton parameters = new JButton("Paramètres");
		parameters.setBackground(new Color(255, 183, 77));
		sidebar.add(parameters);
		
		// ==============================================================

		// Bouton pour se déconnecter
		JButton logout = new JButton("Déconnexion");
		logout.setBackground(new Color(102, 187, 106)); // Vert foncé
		sidebar.add(logout);

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

		// Quand on clique sur l'onglet des stast

		stats.addActionListener(e -> {

			contentPanel.removeAll(); // Supprime tous les composants = page vide

			StatsTab.loadStatsTab(contentPanel, frame);
			
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
