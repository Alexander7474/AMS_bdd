package tabs;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.sql.PreparedStatement;
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
		String[] columnNamesSales = { "Lot produit", "Date", "Prix unitaire", "Quantité", "Total" };
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
			JTextField quantityField = new JTextField(15);

			JPanel saleForm = new JPanel(new GridLayout(4, 2));
			saleForm.add(new JLabel("Lot Produit (ID) :"));
			saleForm.add(produitField);
			saleForm.add(new JLabel("Date (AAAA-MM-JJ) :"));
			saleForm.add(dateField);
			saleForm.add(new JLabel("Quantité :"));
			saleForm.add(quantityField);

			int result = JOptionPane.showConfirmDialog(frame, saleForm, "Ajouter une vente",
					JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				try {	
					
					String query = "SELECT * FROM lot_produit WHERE id_lot_produit = " + produitField.getText();
					try(PreparedStatement statement = Connexion.getConnexion().prepareStatement(query)){
						ResultSet rs = statement.executeQuery();
						if(rs.next()) {
							Vente newVente = new Vente(Integer.parseInt(produitField.getText()), dateField.getText(),
									rs.getDouble("prix_vente_uni"), Double.parseDouble(quantityField.getText()));
							if(rs.getDouble("quantite") >= newVente.getQuantite()) {
								
								String query2 = "UPDATE lot_produit SET quantite = quantite - " + newVente.getQuantite() + " WHERE id_lot_produit = " + newVente.getIdLotProduit();
								try(PreparedStatement statement2 = Connexion.getConnexion().prepareStatement(query2)){
									statement2.executeUpdate();
								}
								
								salesList.add(newVente);
								tableModelSales.addRow(
										new Object[] { newVente.getIdLotProduit(), newVente.getDate(), newVente.getPrixVenteUni(),
												newVente.getQuantite(), newVente.getPrixVenteUni() * newVente.getQuantite() });
								JOptionPane.showMessageDialog(frame, "Vente ajoutée avec succès !");
								Gestion.insert(newVente, "vente");
							}else {
								JOptionPane.showMessageDialog(frame, "Vente impossible car pas assez de stock !");
							}
						}else {
							JOptionPane.showMessageDialog(frame, "Vente impossible lot non trouvé !");
						}
					}
					
					
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, "Erreur lors de l'ajout de la vente : " + ex.getMessage(),
							"Erreur", JOptionPane.ERROR_MESSAGE);
				}
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
					
					String query2 = "UPDATE lot_produit SET quantite = quantite + " + selectedVente.getQuantite() + " WHERE id_lot_produit = " + selectedVente.getIdLotProduit();
					try(PreparedStatement statement2 = Connexion.getConnexion().prepareStatement(query2)){
						statement2.executeUpdate();
					}

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
