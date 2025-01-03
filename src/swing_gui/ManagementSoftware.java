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

	private static JPanel sidebar;
	private static JFrame frame;
	private static Theme theme = Theme.LIGHT; // pas encore implémenté

	public static void loadMainApplication(String username) {

		// ==============================================================
		ManagementSoftware.clearApp();
		Gestion.appStart();

		// ==============================================================

		// Création de la frame et de la sidebar

		getFrame();
		getSidebar();

		// ==============================================================

		// Création de l'affichage du profil utilisateur

		// Affichage de l'utilisateur connecté
		JLabel userLabel = ManagementSoftware.getSidebarText("Utilisateur : " + username);

		// Affichage du rôle de l'utilisateur connecté
		JLabel roleLabel = ManagementSoftware.getSidebarText("Rôle : " + UserManager.getUserRole(username));

		// ==============================================================

		// Création des boutons

		// Bouton pour accéder à l'inventaire du magasin et au mode caisse
		JButton inv = ManagementSoftware.getSidebarButton("Inventaire/Lots & Caisse");

		// Bouton pour accéder à la gestion des fournisseurs
		JButton suppliers = ManagementSoftware.getSidebarButton("Fournisseurs/Contacts");

		// Bouton pour accéder à la gestion des produits
		JButton products = ManagementSoftware.getSidebarButton("Fournisseurs/Produits");

		// Bouton pour accéder à la gestion des contrats
		JButton contracts = ManagementSoftware.getSidebarButton("Contrats");

		// Bouton pour accéder à la gestion des commandes
		JButton orders = ManagementSoftware.getSidebarButton("Commandes/Achats");

		// Bouton pour accéder aux paramètres de l'application
		JButton stats = ManagementSoftware.getSidebarButton("Statistiques");

		// Bouton pour accéder aux paramètres de l'application
		JButton parameters = ManagementSoftware.getSidebarButton("Paramètres");

		// ==============================================================

		// Bouton pour se déconnecter
		JButton logout = ManagementSoftware.getSidebarButton("Déconnexion");

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

		// Onglet des inventaires / lots + format caisse

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

		// Onglet des produits

		products.addActionListener(e -> {

			contentPanel.removeAll();

			ProductsTab.loadProductsTab(contentPanel, frame);

		});

		// ==============================================================

		// !!! LE MODE VENTE SERA DANS L'ONGLET INVENTAIRE
		// POURQUOI ? ÉVITER LA REDONDANCE -> POUR LE MODE CAISSE IL FAUT UN TABLEAU
		// AVEC L'INVENTAIRE DES LOTS DE PRODUITS DISPO EN MAGASIN -> C'EST CE QUE FAIT
		// L'ONGLET INVENTAIRE DONC AUTANT EN PROFITER POUR RAJOUTER LA FONCTIONNALITÉ
		// CAISSE À COTÉ ET EN MÊME TEMPS SUPPRIMER UN ONGLET !!!

		/*
		 * sales.addActionListener(e -> {
		 * 
		 * contentPanel.removeAll();
		 * 
		 * SalesTab.loadSalesTab(contentPanel, frame);
		 * 
		 * });
		 */

		// ==============================================================

		// Onglet des contrats

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

		// Onglet des statistiques = tableau de bord

		stats.addActionListener(e -> {

			contentPanel.removeAll(); // Supprime tous les composants = page vide

			StatsTab.loadStatsTab(contentPanel, frame);

		});

		// ==============================================================

		// Onglet des paramètres

		parameters.addActionListener(e -> {

			contentPanel.removeAll(); // Supprime tous les composants = page vide

			ParametersTab.loadParametersTab(contentPanel, frame);

		});

		// ==============================================================

		// Ajouter la sidebar et le contenu principal à la frame
		frame.add(sidebar, BorderLayout.WEST);
		frame.add(contentPanel, BorderLayout.CENTER);

		frame.setExtendedState(JFrame.NORMAL); // Maximiser la fenêtre pour qu'elle occupe tout l'écran
		frame.setVisible(true); // Rendre la fenêtre visible

	};

	// ==============================================================

	/**
	 * @brief permet de clear l'affichage de l'application
	 * 
	 * @details à utiliser après la connection d'un utilisateur pour éviter de
	 *          dupliquer certain item;
	 */
	public static void clearApp() {
		sidebar = null;
		frame = null;
	}

	/**
	 * @brief Créé un bouton avec les caractéristique de la sidebar et l'ajoute
	 *        automatiquement à la sidebar
	 * 
	 * @param text
	 * @return
	 */
	public static JButton getSidebarButton(String text) {
		JButton button = new JButton(text);

		// caractèristiques du bouton
		Dimension bDim = new Dimension(ManagementSoftware.getSidebar().getPreferredSize().width, 40);
		button.setMaximumSize(bDim);
		button.setBackground(Palette.BUTTON_DARK_ACTIVE);
		button.setBorderPainted(false);

		// on ajoute le boutton à la sidebar
		ManagementSoftware.getSidebar().add(button);

		return button;
	}

	public static JLabel getSidebarText(String text) {
		JLabel label = new JLabel(text);

		// caractèristiques du label
		label.setFont(new Font("Arial", Font.BOLD, 20));
		label.setForeground(Palette.TEXT_LIGHT);

		ManagementSoftware.getSidebar().add(label);

		return label;
	}

	/**
	 * @brief Créé la sidebar
	 * 
	 * @return
	 */
	public static JPanel getSidebar() {
		// Création de la barre latérale
		if (sidebar == null) {
			sidebar = new JPanel();
			sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
			sidebar.setPreferredSize(new Dimension(300, ManagementSoftware.getFrame().getHeight()));
			sidebar.setBackground(Palette.BACKGROUND_DARK_SIDEBAR); // Vert menthe
		}

		return sidebar;
	}

	/**
	 * @brief créé la frame
	 * 
	 * @return
	 */
	public static JFrame getFrame() {
		// Création de la fenêtre
		if (frame == null) {
			frame = new JFrame("AMS - Logiciel de gestion");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(1920, 1080);
		}

		return frame;
	}

};
