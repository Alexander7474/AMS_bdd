package tabs;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
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

import data.Gestion;
import data.IData;
import data.entity.Contrat;

public class ContractsTab {

	public static void loadContractsTab(JPanel contentPanel, JFrame frame) {

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

		List<IData> contractsList = new ArrayList<>();

		// Test de simulation
		contractsList.add(new Contrat("14523652895412", 1, 5.23, "2024-12-12", "2024-12-16")); // >SQL
		contractsList.add(new Contrat("0123456789", 1, 5.23, "2024-12-12", "2024-12-28")); // >SQL

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

		JTable contractsTable = new JTable(tableModelContracts);
		JScrollPane scrollPaneContracts = new JScrollPane(contractsTable);
		cstContracts.gridy = 1;
		contentPanel.add(scrollPaneContracts, cstContracts);

		// ==============================================================

		// Bouton pour ajouter un contrat

		JButton addContractButton = new JButton("Ajouter un contrat");
		addContractButton.setForeground(new Color(51, 51, 51));
		addContractButton.setBackground(new Color(255, 183, 77));
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

					contractsList.add(newContrat);
					tableModelContracts.addRow(
							new Object[] { newContrat.getIdContrat(), newContrat.getSiret(), newContrat.getIdProduit(),
									newContrat.getPrixUni(), newContrat.getDateDebut(), newContrat.getDateFin() });

					Gestion.insert(newContrat, "contrat");
					JOptionPane.showMessageDialog(frame, "Contrat ajouté avec succès !");
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, "Erreur lors de l'ajout du contrat : " + ex.getMessage(),
							"Erreur", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// ==============================================================

		// Bouton pour supprimer un contrat

		JButton deleteContractButton = new JButton("Supprimer un contrat");
		deleteContractButton.setForeground(new Color(51, 51, 51));
		deleteContractButton.setBackground(new Color(255, 183, 77));
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

		JButton editContractButton = new JButton("Modifier un contrat");
		editContractButton.setForeground(new Color(51, 51, 51));
		editContractButton.setBackground(new Color(255, 183, 77));
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

					Gestion.update(selectedContrat, selectedContrat, "contrat");
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
