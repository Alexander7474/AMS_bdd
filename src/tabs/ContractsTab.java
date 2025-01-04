package tabs;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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
import java.util.Vector;

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
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import data.Connexion;
import data.Gestion;
import data.IData;
import data.entity.Commande;
import data.entity.Contrat;
import swing_gui.Palette;
import swing_gui.TabManager;

public class ContractsTab {

	public static void loadContractsTab(JPanel contentPanel, JFrame frame) {

		// ==============================================================

		// Création des couleurs

		Color grisDoux = new Color(51, 51, 51);
		Color jauneDoux = new Color(255, 183, 77);

		// ==============================================================

		// Création du titre de l'onglet

		JLabel titleLabelContracts = new JLabel("Onglet des contrats", SwingConstants.CENTER);
		titleLabelContracts.setFont(new Font("Arial", Font.BOLD, 20));
		GridBagConstraints cstContracts = new GridBagConstraints();
		cstContracts.gridy = 0;
		cstContracts.gridx = 0;
		cstContracts.insets = new Insets(10, 10, 10, 10);
		contentPanel.add(titleLabelContracts, cstContracts);

		// ==============================================================

		// Création de la List dans laquelle seront stockés les contrats

		Vector<IData> contractsList = Gestion.getAllFromTable("contrat", new Contrat());

		// ==============================================================

		// Affichage d'un tableau des contrats

		String[] columnNamesContracts = { "ID Contrat", "SIRET", "ID Produit", "Prix Unitaire", "Date Début",
				"Date Fin" };
		
		DefaultTableModel tableModelContracts = new DefaultTableModel(columnNamesContracts, 0);
		
		for (IData item : contractsList) {
			if (item instanceof Contrat contrat) {
				tableModelContracts.addRow(new Object[] { contrat.getIdContrat(), contrat.getSiret(),
						contrat.getIdProduit(), contrat.getPrixUni(), contrat.getDateDebut(), contrat.getDateFin() });
			}
		}
		
		JTable contractsTable = TabManager.getTable(1000, tableModelContracts);
		
		cstContracts.gridy = 1;
		contentPanel.add(TabManager.getScrollPane(contractsTable), cstContracts);

		// ==============================================================

		// Bouton pour ajouter un contrat

		JButton addContractButton = TabManager.getButton("Ajouter un contrat");
		cstContracts.gridy = 2;
		contentPanel.add(addContractButton, cstContracts);

		addContractButton.addActionListener(addContractEvent -> {
			JTextField siretField = new JTextField(15);
			JTextField produitField = new JTextField(15);
			JTextField prixField = new JTextField(15);
			JTextField dateDebutField = new JTextField(15);
			JTextField dateFinField = new JTextField(15);

			JPanel contractForm = new JPanel(new GridLayout(5, 2));
			contractForm.add(new JLabel("SIRET fournisseur :"));
			contractForm.add(siretField);
			contractForm.add(new JLabel("ID Produit :"));
			contractForm.add(produitField);
			contractForm.add(new JLabel("Prix unitaire :"));
			contractForm.add(prixField);
			contractForm.add(new JLabel("Date début (YYYY-MM-DD) :"));
			contractForm.add(dateDebutField);
			contractForm.add(new JLabel("Date fin (YYYY-MM-DD) :"));
			contractForm.add(dateFinField);

			int result = JOptionPane.showConfirmDialog(frame, contractForm, "Ajouter un contrat",
					JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				try {

					Contrat newContrat = new Contrat(siretField.getText(), Integer.parseInt(produitField.getText()),
							Double.parseDouble(prixField.getText()), dateDebutField.getText(), dateFinField.getText());

					String query = "SELECT * FROM produit_fournisseur WHERE id_produit = ? AND siret = ?";
					boolean canMakeContrat = false;

					try (PreparedStatement statement = Connexion.getConnexion().prepareStatement(query)) {
						statement.setInt(1, newContrat.getIdProduit());
						statement.setString(2, newContrat.getSiret());
						ResultSet rs = statement.executeQuery();
						if (rs.next()) {
							canMakeContrat = true;
						}
					}

					if (canMakeContrat) {
						contractsList.add(newContrat);
						tableModelContracts.addRow(new Object[] { newContrat.getIdContrat(), newContrat.getSiret(),
								newContrat.getIdProduit(), newContrat.getPrixUni(), newContrat.getDateDebut(),
								newContrat.getDateFin() });

						Gestion.insert(newContrat, "contrat");
						JOptionPane.showMessageDialog(frame, "Contrat ajouté avec succès !");
					} else {
						JOptionPane.showMessageDialog(frame,
								"Impossible de créer le contrat, le fournisseur n'a pas le produit voulue !");
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, "Erreur lors de l'ajout du contrat : " + ex.getMessage(),
							"Erreur", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// ==============================================================

		// Bouton pour supprimer un contrat

		JButton deleteContractButton = TabManager.getButton("Supprimer un contrat");
		cstContracts.gridy = 3;
		contentPanel.add(deleteContractButton, cstContracts);

		deleteContractButton.addActionListener(deleteContractEvent -> {
			int selectedRow = contractsTable.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un contrat à supprimer.", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				Contrat selectedContrat = (Contrat) contractsList.get(selectedRow);

				contractsList.remove(selectedContrat);
				tableModelContracts.removeRow(selectedRow);

				Gestion.delete(selectedContrat, "contrat");

				JOptionPane.showMessageDialog(frame, "Contrat supprimé avec succès !");
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(frame, "Erreur lors de la suppression : " + ex.getMessage(), "Erreur",
						JOptionPane.ERROR_MESSAGE);
			}
		});

		// ==============================================================

		// Bouton pour modifier un contrat

		JButton editContractButton = TabManager.getButton("Modifier un contrat");
		cstContracts.gridy = 4;
		contentPanel.add(editContractButton, cstContracts);

		editContractButton.addActionListener(editContractEvent -> {
			int selectedRow = contractsTable.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un contrat à modifier.", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			Contrat selectedContrat = (Contrat) contractsList.get(selectedRow);

			JTextField siretField = new JTextField(selectedContrat.getSiret(), 15);
			JTextField produitField = new JTextField(String.valueOf(selectedContrat.getIdProduit()), 15);
			JTextField prixField = new JTextField(String.valueOf(selectedContrat.getPrixUni()), 15);
			JTextField dateDebutField = new JTextField(String.valueOf(selectedContrat.getDateDebut()), 15);
			JTextField dateFinField = new JTextField(String.valueOf(selectedContrat.getDateFin()), 15);

			JPanel editForm = new JPanel(new GridLayout(5, 2));
			editForm.add(new JLabel("SIRET fournisseur :"));
			editForm.add(siretField);
			editForm.add(new JLabel("ID Produit :"));
			editForm.add(produitField);
			editForm.add(new JLabel("Prix unitaire :"));
			editForm.add(prixField);
			editForm.add(new JLabel("Date début (YYYY-MM-DD) :"));
			editForm.add(dateDebutField);
			editForm.add(new JLabel("Date fin (YYYY-MM-DD) :"));
			editForm.add(dateFinField);

			int result = JOptionPane.showConfirmDialog(frame, editForm, "Modifier le contrat",
					JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				try {
					selectedContrat.setSiret(siretField.getText());
					selectedContrat.setIdProduit(Integer.parseInt(produitField.getText()));
					selectedContrat.setPrixUni(Double.parseDouble(prixField.getText()));
					selectedContrat.setDateDebut(dateDebutField.getText());
					selectedContrat.setDateFin(dateFinField.getText());

					tableModelContracts.setValueAt(selectedContrat.getSiret(), selectedRow, 1);
					tableModelContracts.setValueAt(selectedContrat.getIdProduit(), selectedRow, 2);
					tableModelContracts.setValueAt(selectedContrat.getPrixUni(), selectedRow, 3);
					tableModelContracts.setValueAt(selectedContrat.getDateDebut(), selectedRow, 4);
					tableModelContracts.setValueAt(selectedContrat.getDateFin(), selectedRow, 5);

					Gestion.update(selectedContrat, "contrat", "id_contrat", selectedContrat.getIdContrat());
					JOptionPane.showMessageDialog(frame, "Contrat modifié avec succès !");
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, "Erreur lors de la modification : " + ex.getMessage(),
							"Erreur", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// ==============================================================

		contentPanel.revalidate();
		contentPanel.repaint();
	}
}
