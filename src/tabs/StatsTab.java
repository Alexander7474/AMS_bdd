package tabs;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import data.Connexion;
import data.IData;
import data.entity.Produit;
import data.entity.Vente;
import statistic.Statistic;



public class StatsTab {

	public static void loadStatsTab(JPanel contentPanel, JFrame frame) {

		// ==============================================================

		// Création des couleurs

		Color grisDoux = new Color(51, 51, 51);
		Color jauneDoux = new Color(255, 183, 77);

		// ==============================================================

		// panel 1 -> quantité de produit vendue

		JLabel titleLabel1 = new JLabel("Produits les plus vendues", SwingConstants.CENTER);
		titleLabel1.setFont(new Font("Arial", Font.BOLD, 20));
		GridBagConstraints cst1 = new GridBagConstraints();
		cst1.gridy = 0;
		cst1.gridx = 0;
		cst1.insets = new Insets(10, 10, 10, 10);
		contentPanel.add(titleLabel1, cst1);
		
		Vector<Produit> produitList1 = Statistic.getBestSeller(10);
		Vector<Double> produitListQua1 = Statistic.getBestSellerQua(10);

		// Affichage du tableau des ventes
		String[] columnNames1 = { "ID produit", "Nom", "Descritpion" ,"Catégorie", "Quantité vendue"  };
		DefaultTableModel tableModel1 = new DefaultTableModel(columnNames1, 0);

		for (int i = 0; i < produitList1.size(); i++) {
			if (produitList1.get(i) instanceof Produit produit) {
				tableModel1.addRow(new Object[] { produit.getIdProduit(), produit.getNom(), produit.getDesc(), produit.getCategorie(), produitListQua1.get(i) });
			}
		}

		JTable table1 = new JTable(tableModel1);
		JScrollPane scrollPane1 = new JScrollPane(table1);
		cst1.gridy = 1;
		cst1.gridx = 0;
		contentPanel.add(scrollPane1, cst1);
		// ==============================================================
		
		// panel 2 -> chiffre affaire par produit

		JLabel titleLabel2 = new JLabel("Produits qui génère le plus de chiffre d'affaires", SwingConstants.CENTER);
		titleLabel2.setFont(new Font("Arial", Font.BOLD, 20));
		GridBagConstraints cst2 = new GridBagConstraints();
		cst2.gridy = 0;
		cst2.gridx = 1;
		cst2.insets = new Insets(10, 10, 10, 10);
		contentPanel.add(titleLabel2, cst2);
		
		Vector<Produit> produitList2 = Statistic.getBestSellerValue(10);
		Vector<Double> produitListQua2 = Statistic.getBestSellerValueQua(10);

		// Affichage du tableau des ventes
		String[] columnNames2 = { "ID produit", "Nom", "Descritpion" ,"Catégorie", "Total valeur vendue (€)" };
		DefaultTableModel tableModel2 = new DefaultTableModel(columnNames2, 0);

		for (int i = 0; i < produitList2.size(); i++) {
			if (produitList2.get(i) instanceof Produit produit) {
				tableModel2.addRow(new Object[] { produit.getIdProduit(), produit.getNom(), produit.getDesc(), produit.getCategorie(), produitListQua2.get(i) + "€" });
			}
		}

		JTable table2 = new JTable(tableModel2);
		JScrollPane scrollPane2 = new JScrollPane(table2);
		cst2.gridy = 1;
		cst2.gridx = 1;
		contentPanel.add(scrollPane2, cst2);

		// ==============================================================
		
		// panel 3 -> bénéfice journalier

		JLabel titleLabel3 = new JLabel("Bénéfice des 30 derniers jours", SwingConstants.CENTER);
		titleLabel3.setFont(new Font("Arial", Font.BOLD, 20));
		GridBagConstraints cst3 = new GridBagConstraints();
		cst3.gridy = 0;
		cst3.gridx = 2;
		cst3.insets = new Insets(10, 10, 10, 10);
		contentPanel.add(titleLabel3, cst3);
		
		// Obtenir la date d'aujourd'hui
        LocalDate aujourdHui = LocalDate.now();

		// Affichage du tableau des ventes
		String[] columnNames3 = { "Date", "Bénéfice (€)" };
		DefaultTableModel tableModel3 = new DefaultTableModel(columnNames3, 0);

		// Boucle sur les 30 derniers jours
        for (int i = 0; i < 30; i++) {
            // Calculer la date - 1 jour
            LocalDate date = aujourdHui.minusDays(i);
            tableModel3.addRow(new Object[] { Date.valueOf(date), Statistic.getJournalyBenefit(Date.valueOf(date)) + "€" });
        }

		JTable table3 = new JTable(tableModel3);
		JScrollPane scrollPane3 = new JScrollPane(table3);
		cst3.gridy = 1;
		cst3.gridx = 2;
		contentPanel.add(scrollPane3, cst3);

		// ==============================================================
		
		// panel 4 -> CA journalier

		JLabel titleLabel4 = new JLabel("CA des 30 derniers jours", SwingConstants.CENTER);
		titleLabel4.setFont(new Font("Arial", Font.BOLD, 20));
		GridBagConstraints cst4 = new GridBagConstraints();
		cst4.gridy = 2;
		cst4.gridx = 2;
		cst4.insets = new Insets(10, 10, 10, 10);
		contentPanel.add(titleLabel4, cst4);
		
		// Obtenir la date d'aujourd'hui
        LocalDate aujourdHui4 = LocalDate.now();

		// Affichage du tableau des ventes
		String[] columnNames4 = { "Date", "CA (€)" };
		DefaultTableModel tableModel4 = new DefaultTableModel(columnNames4, 0);

		// Boucle sur les 30 derniers jours
        for (int i = 0; i < 30; i++) {
            // Calculer la date - 1 jour
            LocalDate date = aujourdHui4.minusDays(i);
            tableModel4.addRow(new Object[] { Date.valueOf(date), Statistic.getJournalyCA(Date.valueOf(date)) + "€" });
        }

		JTable table4 = new JTable(tableModel4);
		JScrollPane scrollPane4= new JScrollPane(table4);
		cst4.gridy = 3;
		cst4.gridx = 2;
		contentPanel.add(scrollPane4, cst4);

		// ==============================================================
		// ==============================================================
		
		// panel 5 -> perte journaliere

		JLabel titleLabel5 = new JLabel("Valeur perdue des 30 derniers jours", SwingConstants.CENTER);
		titleLabel5.setFont(new Font("Arial", Font.BOLD, 20));
		GridBagConstraints cst5 = new GridBagConstraints();
		cst5.gridy = 2;
		cst5.gridx = 0;
		cst5.insets = new Insets(10, 10, 10, 10);
		contentPanel.add(titleLabel5, cst5);
		
		// Obtenir la date d'aujourd'hui
        LocalDate aujourdHui5= LocalDate.now();

		// Affichage du tableau des ventes
		String[] columnNames5 = { "Date", "Pertes (€)" };
		DefaultTableModel tableModel5 = new DefaultTableModel(columnNames4, 0);

		// Boucle sur les 30 derniers jours
        for (int i = 0; i < 30; i++) {
            // Calculer la date - 1 jour
            LocalDate date = aujourdHui5.minusDays(i);
            tableModel5.addRow(new Object[] { Date.valueOf(date), Statistic.getJournalyPerte(Date.valueOf(date)) + "€" });
        }

		JTable table5 = new JTable(tableModel5);
		JScrollPane scrollPane5= new JScrollPane(table5);
		cst5.gridy = 3;
		cst5.gridx = 0;
		contentPanel.add(scrollPane5, cst5);

		// ==============================================================

		contentPanel.revalidate();
		contentPanel.repaint();
	}
}