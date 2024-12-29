package tabs;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.sql.Date;
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
import data.entity.LotProduit;
import data.entity.LotProduit;

public class InvTab {

	public static void loadInvTab(JPanel contentPanel, JFrame frame) {

		// ==============================================================

		// Création des couleurs

		Color grisDoux = new Color(51, 51, 51);
		Color jauneDoux = new Color(255, 183, 77);

		// ==============================================================

		// Création du titre de l'onglet

		JLabel titleLabelInv = new JLabel("Onglet Inventaire du magasin", SwingConstants.CENTER);
		titleLabelInv.setFont(new Font("Arial", Font.BOLD, 20));
		GridBagConstraints cstInv = new GridBagConstraints();
		cstInv.gridy = 0;
		cstInv.gridx = 0;
		cstInv.insets = new Insets(10, 10, 10, 10);
		contentPanel.add(titleLabelInv, cstInv);

		// ==============================================================

		// Création de la List dans laquelle seront stockées les LotProduits (exemple)

		List<IData> InvList = new ArrayList<>();
		// recup des info dans la base
		try {
			Statement statement = Connexion.getConnexion().createStatement();
			try(ResultSet rs = statement.executeQuery("SELECT * FROM lot_produit")){
				while(rs.next()) {
					LotProduit lp = new LotProduit(rs);
					InvList.add(lp);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ==============================================================

		// Affichage du tableau des LotProduits
		String[] columnNamesInv = { "ID lot produit", "ID produit(Nom)", "Prix de Vente unitaire" ,"Quantité restante", "Date péremption" ,"ID achat du lot" };
		DefaultTableModel tableModelInv = new DefaultTableModel(columnNamesInv, 0);

		for (IData item : InvList) {
			if (item instanceof LotProduit lot) {
				tableModelInv.addRow(new Object[] { lot.getIdLotProduit(), lot.getIdProduit(), lot.getPrixVenteUni(), lot.getQuantite(), lot.getPeremption(), lot.getIdAchat() });
			}
		}

		JTable InvTable = new JTable(tableModelInv);
		JScrollPane scrollPaneInv = new JScrollPane(InvTable);
		cstInv.gridy = 1;
		contentPanel.add(scrollPaneInv, cstInv);

		// ==============================================================

		
		// ==============================================================

		// Bouton pour modifier une LotProduit
		JButton modifySaleButton = new JButton("Modifier un lot");
		modifySaleButton.setForeground(grisDoux);
		modifySaleButton.setBackground(jauneDoux);
		cstInv.gridy = 3;
		contentPanel.add(modifySaleButton, cstInv);

		// Action au clique sur ce bouton
		modifySaleButton.addActionListener(modifySaleEvent -> {
			int selectedRow = InvTable.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un lot à modifier.", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				LotProduit selectedLotProduit = (LotProduit) InvList.get(selectedRow);

				JTextField prixField = new JTextField(String.valueOf(selectedLotProduit.getPrixVenteUni()));
				JTextField quantiteField = new JTextField(String.valueOf(selectedLotProduit.getQuantite()));
				JTextField peremptionField = new JTextField(String.valueOf(selectedLotProduit.getPeremption()));

				JPanel saleForm = new JPanel(new GridLayout(4, 2));
				saleForm.add(new JLabel("Prix vente unitaire :"));
				saleForm.add(prixField);
				saleForm.add(new JLabel("Quantité restante :"));
				saleForm.add(quantiteField);
				saleForm.add(new JLabel("Date péremption :"));
				saleForm.add(peremptionField);

				int result = JOptionPane.showConfirmDialog(frame, saleForm, "Modifier une LotProduit",
						JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {

					// Création de l'objet LotProduit mis à jour
					LotProduit updatedLotProduit = new LotProduit(selectedLotProduit.getIdLotProduit(),
							selectedLotProduit.getIdProduit(),
							Double.parseDouble(prixField.getText()),
							Double.parseDouble(quantiteField.getText()),
							Date.valueOf(peremptionField.getText()),
							selectedLotProduit.getIdAchat());

					// Mise à jour dans la base de données
					String query = "UPDATE lot_produit SET " + updatedLotProduit.getValuesEq() + " WHERE id_lot_produit = " +selectedLotProduit.getIdLotProduit();
					try(PreparedStatement statement = Connexion.getConnexion().prepareStatement(query)){
						updatedLotProduit.composeStatementEq(statement);
						statement.executeUpdate();
					}

					// Mise à jour dans le tableau
					tableModelInv.setValueAt(selectedLotProduit.getPrixVenteUni(), selectedRow, 2);
					tableModelInv.setValueAt(selectedLotProduit.getQuantite(), selectedRow, 3);
					tableModelInv.setValueAt(selectedLotProduit.getPeremption(), selectedRow, 4);

					JOptionPane.showMessageDialog(frame, "LotProduit modifiée avec succès !");
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(frame, "Erreur lors de la modification de la LotProduit : " + ex.getMessage(),
						"Erreur", JOptionPane.ERROR_MESSAGE);
			}
		});

		// ==============================================================

		// Bouton pour supprimer une LotProduit
		JButton deleteSaleButton = new JButton("Supprimer un LotProduit");
		deleteSaleButton.setForeground(grisDoux);
		deleteSaleButton.setBackground(jauneDoux);
		cstInv.gridy = 4;
		contentPanel.add(deleteSaleButton, cstInv);

		// Action au clique sur ce bouton
		deleteSaleButton.addActionListener(deleteSaleEvent -> {
			int selectedRow = InvTable.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(frame, "Veuillez sélectionner une LotProduit à supprimer.", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				LotProduit selectedLotProduit = (LotProduit) InvList.get(selectedRow);

				int confirmDelete = JOptionPane.showConfirmDialog(frame,
						"Êtes-vous sûr de vouloir supprimer cette LotProduit ?", "Confirmer la suppression",
						JOptionPane.YES_NO_OPTION);

				if (confirmDelete == JOptionPane.YES_OPTION) {
					InvList.remove(selectedLotProduit);
					tableModelInv.removeRow(selectedRow);

					Gestion.delete(selectedLotProduit, "lot_produit");

					JOptionPane.showMessageDialog(frame, "LotProduit supprimée avec succès !");
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(frame, "Erreur lors de la suppression de la LotProduit : " + ex.getMessage(),
						"Erreur", JOptionPane.ERROR_MESSAGE);
			}
		});

		// ==============================================================

		contentPanel.revalidate();
		contentPanel.repaint();
	}
}