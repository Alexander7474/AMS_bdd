package tabs;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import data.Connexion;
import data.Gestion;
import data.IData;
import data.entity.Commande;
import data.entity.Vente;

public class SalesTab {

	public static void loadSalesTab(JPanel contentPanel, JFrame frame) {

		// ==============================================================

		// Création des couleurs

		Color grisDoux = new Color(51, 51, 51);
		Color jauneDoux = new Color(255, 183, 77);

		// ==============================================================

		// Création du titre de l'onglet

		JLabel titleLabelSales = new JLabel("Onglet des ventes", SwingConstants.CENTER);
		titleLabelSales.setFont(new Font("Arial", Font.BOLD, 20));
		GridBagConstraints cstSales = new GridBagConstraints();
		cstSales.gridy = 0;
		cstSales.gridx = 0;
		cstSales.insets = new Insets(10, 10, 10, 10);
		contentPanel.add(titleLabelSales, cstSales);

		// ==============================================================

		// Création de la List dans laquelle seront stockées les ventes (exemple)

		List<IData> salesList = new ArrayList<>();
		// recup des info dans la base
		try {
			Statement statement = Connexion.getConnexion().createStatement();
			try(ResultSet rs = statement.executeQuery("SELECT * FROM vente")){
				while(rs.next()) {
					Vente v = new Vente(rs);
					salesList.add(v);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ==============================================================

		// Affichage du tableau des ventes
		String[] columnNamesSales = { "Produit", "Date", "Prix unitaire", "Quantité", "Total" };
		DefaultTableModel tableModelSales = new DefaultTableModel(columnNamesSales, 0);

		for (IData item : salesList) {
			if (item instanceof Vente vente) {
				tableModelSales.addRow(new Object[] { vente.getIdLotProduit(), vente.getDate(), vente.getPrixVenteUni(),
						vente.getQuantite(), vente.getPrixVenteUni() * vente.getQuantite() });
			}
		}

		JTable salesTable = new JTable(tableModelSales);
		JScrollPane scrollPaneSales = new JScrollPane(salesTable);
		cstSales.gridy = 1;
		contentPanel.add(scrollPaneSales, cstSales);

		// ==============================================================

		// Bouton pour ajouter une vente

		JButton addSaleButton = new JButton("Ajouter une vente");
		addSaleButton.setForeground(grisDoux);
		addSaleButton.setBackground(jauneDoux);
		cstSales.gridy = 2;
		contentPanel.add(addSaleButton, cstSales);

		// Action au clique sur ce bouton
		addSaleButton.addActionListener(addSaleEvent -> {
			JTextField produitField = new JTextField(15);
			JTextField dateField = new JTextField(15);
			JTextField priceField = new JTextField(15);
			JTextField quantityField = new JTextField(15);

			JPanel saleForm = new JPanel(new GridLayout(4, 2));
			saleForm.add(new JLabel("Produit (ID) :"));
			saleForm.add(produitField);
			saleForm.add(new JLabel("Date (AAAA-MM-JJ) :"));
			saleForm.add(dateField);
			saleForm.add(new JLabel("Prix unitaire :"));
			saleForm.add(priceField);
			saleForm.add(new JLabel("Quantité :"));
			saleForm.add(quantityField);

			int result = JOptionPane.showConfirmDialog(frame, saleForm, "Ajouter une vente",
					JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				try {
					Vente newVente = new Vente(Integer.parseInt(produitField.getText()), dateField.getText(),
							Double.parseDouble(priceField.getText()), Integer.parseInt(quantityField.getText()));

					salesList.add(newVente);
					tableModelSales.addRow(
							new Object[] { newVente.getIdLotProduit(), newVente.getDate(), newVente.getPrixVenteUni(),
									newVente.getQuantite(), newVente.getPrixVenteUni() * newVente.getQuantite() });

					Gestion.insert(newVente, "vente");

					JOptionPane.showMessageDialog(frame, "Vente ajoutée avec succès !");
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, "Erreur lors de l'ajout de la vente : " + ex.getMessage(),
							"Erreur", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// ==============================================================

		// Bouton pour modifier une vente
		JButton modifySaleButton = new JButton("Modifier une vente");
		modifySaleButton.setForeground(grisDoux);
		modifySaleButton.setBackground(jauneDoux);
		cstSales.gridy = 3;
		contentPanel.add(modifySaleButton, cstSales);

		// Action au clique sur ce bouton
		modifySaleButton.addActionListener(modifySaleEvent -> {
			int selectedRow = salesTable.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(frame, "Veuillez sélectionner une vente à modifier.", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				Vente selectedVente = (Vente) salesList.get(selectedRow);

				JTextField produitField = new JTextField(String.valueOf(selectedVente.getIdLotProduit()));
				JTextField dateField = new JTextField(String.valueOf(selectedVente.getDate()));
				JTextField priceField = new JTextField(String.valueOf(selectedVente.getPrixVenteUni()));
				JTextField quantityField = new JTextField(String.valueOf(selectedVente.getQuantite()));

				JPanel saleForm = new JPanel(new GridLayout(4, 2));
				saleForm.add(new JLabel("Produit (ID) :"));
				saleForm.add(produitField);
				saleForm.add(new JLabel("Date (AAAA-MM-JJ) :"));
				saleForm.add(dateField);
				saleForm.add(new JLabel("Prix unitaire :"));
				saleForm.add(priceField);
				saleForm.add(new JLabel("Quantité :"));
				saleForm.add(quantityField);

				int result = JOptionPane.showConfirmDialog(frame, saleForm, "Modifier une vente",
						JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {

					// Mise à jour de l'objet selectedVente
					selectedVente.setDate(dateField.getText());
					selectedVente.setPrixVenteUni(Double.parseDouble(priceField.getText()));
					selectedVente.setQuantite(Integer.parseInt(quantityField.getText())); // Ajout de la mise à jour de
																							// la quantité

					// Création de l'objet Vente mis à jour
					Vente updatedVente = new Vente(selectedVente.getIdLotProduit(),
							String.valueOf(selectedVente.getDate()), selectedVente.getPrixVenteUni(),
							selectedVente.getQuantite());

					// Mise à jour dans la base de données
					Gestion.update(selectedVente, updatedVente, "vente");

					// Mise à jour dans le tableau
					tableModelSales.setValueAt(selectedVente.getDate(), selectedRow, 1);
					tableModelSales.setValueAt(selectedVente.getPrixVenteUni(), selectedRow, 2);
					tableModelSales.setValueAt(selectedVente.getQuantite(), selectedRow, 3);
					tableModelSales.setValueAt(selectedVente.getPrixVenteUni() * selectedVente.getQuantite(),
							selectedRow, 4);

					JOptionPane.showMessageDialog(frame, "Vente modifiée avec succès !");
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(frame, "Erreur lors de la modification de la vente : " + ex.getMessage(),
						"Erreur", JOptionPane.ERROR_MESSAGE);
			}
		});

		// ==============================================================

		// Bouton pour supprimer une vente
		JButton deleteSaleButton = new JButton("Supprimer une vente");
		deleteSaleButton.setForeground(grisDoux);
		deleteSaleButton.setBackground(jauneDoux);
		cstSales.gridy = 4;
		contentPanel.add(deleteSaleButton, cstSales);

		// Action au clique sur ce bouton
		deleteSaleButton.addActionListener(deleteSaleEvent -> {
			int selectedRow = salesTable.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(frame, "Veuillez sélectionner une vente à supprimer.", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				Vente selectedVente = (Vente) salesList.get(selectedRow);

				int confirmDelete = JOptionPane.showConfirmDialog(frame,
						"Êtes-vous sûr de vouloir supprimer cette vente ?", "Confirmer la suppression",
						JOptionPane.YES_NO_OPTION);

				if (confirmDelete == JOptionPane.YES_OPTION) {
					salesList.remove(selectedVente);
					tableModelSales.removeRow(selectedRow);

					Gestion.delete(selectedVente, "vente");

					JOptionPane.showMessageDialog(frame, "Vente supprimée avec succès !");
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(frame, "Erreur lors de la suppression de la vente : " + ex.getMessage(),
						"Erreur", JOptionPane.ERROR_MESSAGE);
			}
		});

		// ==============================================================

		contentPanel.revalidate();
		contentPanel.repaint();
	}
}
