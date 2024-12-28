package tabs;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.sql.Date;
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

import data.IData;
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

		// Liste des lots de produits pour simulation

		List<IData> lotProduitList = new ArrayList<>();

		// Test de simulation
		lotProduitList.add(new LotProduit(1, 10.5, 50, Date.valueOf("2023-12-30"))); // >SQL
		lotProduitList.add(new LotProduit(2, 15.0, 100, Date.valueOf("2024-01-10"))); // >SQL

		// ==============================================================

		// Tableau des lots de produits approchant leur date de péremption

		String[] columnNamesExpiry = { "ID Lot", "ID Produit", "Quantité", "Prix Unitaire", "Date de péremption" };
		DefaultTableModel expiryTableModel = new DefaultTableModel(columnNamesExpiry, 0);

		for (IData item : lotProduitList) {
			if ((item instanceof LotProduit lotProduit)
					&& (lotProduit.getPeremption().compareTo(Date.valueOf("2024-12-28")) < 0)) { // < SQL vérifier la
																									// date de
																									// péremption

				// Ajouter les produits qui correspondent qui ont bien leur date de péremption qui approche
				expiryTableModel.addRow(new Object[] { lotProduit.getIdLotProduit(), lotProduit.getIdProduit(),
						lotProduit.getQuantite(), lotProduit.getPrixVenteUni(), lotProduit.getPeremption() });
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
