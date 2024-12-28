package tabs;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import data.Connexion;
import data.IData;
import data.entity.Fournisseur;
import data.entity.LotProduit;

public class DashboardTab {

	public static void loadDashboardTab(JPanel contentPanel, JFrame frame) {

		// ==============================================================
		
		// Création des couleurs 
		
		Color grisDoux = new Color(51, 51, 51);
		Color jauneDoux = new Color(255, 183, 77);
		
		// ==============================================================

		// Création du titre de l'onglet

		JLabel titleLabelDashboard = new JLabel("Tableau de bord", SwingConstants.CENTER);
		titleLabelDashboard.setFont(new Font("Arial", Font.BOLD, 20));
		GridBagConstraints cstDashboard = new GridBagConstraints();
		cstDashboard.gridy = 0;
		cstDashboard.gridx = 0;
		cstDashboard.insets = new Insets(10, 10, 10, 10);
		contentPanel.add(titleLabelDashboard, cstDashboard);

		// ==============================================================

		List<IData> lotProduitList = new ArrayList<>();

		// recup des info dans la base
		try {
			Statement statement = Connexion.getConnexion().createStatement();
			try(ResultSet rs = statement.executeQuery("SELECT * FROM lot_produit")){
				while(rs.next()) {
					LotProduit lp = new LotProduit(rs);
					lotProduitList.add(lp);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ==============================================================

		// Tableau des lots de produits approchant leur date de péremption

		String[] columnNamesExpiry = { "ID Lot", "ID Produit", "Quantité", "Prix Unitaire", "Date de péremption", "ID achat" };
		DefaultTableModel expiryTableModel = new DefaultTableModel(columnNamesExpiry, 0);

		for (IData item : lotProduitList) {
			if ((item instanceof LotProduit lotProduit)
					&& (lotProduit.getPeremption().compareTo(Date.valueOf("2024-12-28")) < 0)) { // < SQL vérifier la
																									// date de
																									// péremption

				// Ajouter les produits qui correspondent qui ont bien leur date de péremption qui approche
				expiryTableModel.addRow(new Object[] { lotProduit.getIdLotProduit(), lotProduit.getIdProduit(),
						lotProduit.getQuantite(), lotProduit.getPrixVenteUni(), lotProduit.getPeremption(), lotProduit.getIdAchat() });
			}
		}

		JTable expiryTable = new JTable(expiryTableModel);
		JScrollPane expiryScrollPane = new JScrollPane(expiryTable);
		cstDashboard.gridy = 1;
		contentPanel.add(expiryScrollPane, cstDashboard);

		// ==============================================================

		// Bouton pour afficher les statistiques

		JButton statsButton = new JButton("Afficher les statistiques");
		statsButton.setForeground(grisDoux);
		statsButton.setBackground(jauneDoux);
		cstDashboard.gridy = 2;
		contentPanel.add(statsButton, cstDashboard);

		// Action au clique sur le bouton
		statsButton.addActionListener(e -> {

			// ta partie ? j'ai affiché les lots de produits qui approchent de leur date de péremption normalement ça marche
			// les calculs de statistiques (voir cahier des charges) sont à faire

		});

		// ==============================================================

		contentPanel.revalidate();
		contentPanel.repaint();
	}
}
