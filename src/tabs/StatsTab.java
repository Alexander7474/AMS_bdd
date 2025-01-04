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
		
		int tabX = 400;

		// ==============================================================
		
		GridBagConstraints cst = new GridBagConstraints();
		cst.insets = new Insets(10, 10, 10, 10);
		
		for (int i = 0; i < 5; i++) {
			
			JLabel titleLabel;
			DefaultTableModel tableModel;
			
			switch (i) {
			
			case 0: {
				titleLabel = new JLabel("Produits les plus vendues", SwingConstants.CENTER);

				Vector<Produit> produitList = Statistic.getBestSeller(10);
				Vector<Double> produitListQua = Statistic.getBestSellerQua(10);

				// Affichage du tableau des ventes
				String[] columnNames = { "ID produit", "Nom", "Descritpion" ,"Catégorie", "Quantité vendue"  };
				tableModel = new DefaultTableModel(columnNames, 0);
				
				for (int y = 0; y < produitList.size(); y++) {
					if (produitList.get(y) instanceof Produit produit) {
						tableModel.addRow(new Object[] { produit.getIdProduit(), produit.getNom(), produit.getDesc(), produit.getCategorie(), produitListQua.get(y) });
					}
				}
				break;
			}
			
			case 1: {
				titleLabel = new JLabel("Produits qui génère le plus de chiffre d'affaires", SwingConstants.CENTER);
				
				Vector<Produit> produitList = Statistic.getBestSellerValue(10);
				Vector<Double> produitListQua = Statistic.getBestSellerValueQua(10);

				// Affichage du tableau des ventes
				String[] columnNames = { "ID produit", "Nom", "Descritpion" ,"Catégorie", "Total valeur vendue (€)" };
				tableModel = new DefaultTableModel(columnNames, 0);

				for (int y = 0; y < produitList.size(); y++) {
					if (produitList.get(i) instanceof Produit produit) {
						tableModel.addRow(new Object[] { produit.getIdProduit(), produit.getNom(), produit.getDesc(), produit.getCategorie(), produitListQua.get(y) + "€" });
					}
				}
				break;
			}
			
			case 2: {
				titleLabel = new JLabel("Bénéfice des 30 derniers jours", SwingConstants.CENTER);
				
				// Obtenir la date d'aujourd'hui
		        LocalDate aujourdHui = LocalDate.now();

				// Affichage du tableau des ventes
				String[] columnNames = { "Date", "Bénéfice (€)" };
				tableModel = new DefaultTableModel(columnNames, 0);

				// Boucle sur les 30 derniers jours
		        for (int y = 0; y < 30; y++) {
		            // Calculer la date - 1 jour
		            LocalDate date = aujourdHui.minusDays(y);
		            tableModel.addRow(new Object[] { Date.valueOf(date), Statistic.getJournalyBenefit(Date.valueOf(date)) + "€" });
		        }
		        break;
			}
			
			case 3: {
				titleLabel = new JLabel("CA des 30 derniers jours", SwingConstants.CENTER);
				
				// Obtenir la date d'aujourd'hui
		        LocalDate aujourdHui = LocalDate.now();

				// Affichage du tableau des ventes
				String[] columnNames = { "Date", "CA (€)" };
				tableModel = new DefaultTableModel(columnNames, 0);

				// Boucle sur les 30 derniers jours
		        for (int y = 0; y < 30; y++) {
		            // Calculer la date - 1 jour
		            LocalDate date = aujourdHui.minusDays(y);
		            tableModel.addRow(new Object[] { Date.valueOf(date), Statistic.getJournalyCA(Date.valueOf(date)) + "€" });
		        }
		        break;
			}
			
			case 4: {
				titleLabel = new JLabel("Valeur perdue des 30 derniers jours", SwingConstants.CENTER);
				
				// Obtenir la date d'aujourd'hui
		        LocalDate aujourdHui= LocalDate.now();

				// Affichage du tableau des ventes
				String[] columnNames = { "Date", "Pertes (€)" };
				tableModel = new DefaultTableModel(columnNames, 0);

				// Boucle sur les 30 derniers jours
		        for (int y = 0; y < 30; y++) {
		            // Calculer la date - 1 jour
		            LocalDate date = aujourdHui.minusDays(y);
		            System.out.println(date);
		            tableModel.addRow(new Object[] { Date.valueOf(date), Statistic.getJournalyPerte(Date.valueOf(date)) + "€" });
		        }
		        break;
			}
			
			default:
				throw new IllegalArgumentException("Unexpected value: " + i);
			}
			
			titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
			
			cst.gridy = (i/3) * 2;
			cst.gridx = i%3;
			contentPanel.add(titleLabel, cst);
			
			JTable table = TabManager.getTable(tabX, tableModel);
			cst.gridy = ((i/3) * 2)+1;
			cst.gridx = i%3;
			contentPanel.add(TabManager.getScrollPane(table), cst);
		}

		contentPanel.revalidate();
		contentPanel.repaint();
	}
}