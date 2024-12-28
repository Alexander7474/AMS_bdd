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
import javax.swing.JTextField;
import javax.swing.SwingConstants;

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

		List<IData> contractsList = new ArrayList<>(); // Les contrats seront stockés dans une List

		// Test de simulation
		Contrat contratTEST = new Contrat("14523652895412", 1, 5.23, "2024-12-12", "2024-12-16");// >!SQL
		Contrat contratTEST2 = new Contrat("0123456789", 1, 5.23, "2024-12-12", "2024-12-28"); // >!SQL
		contractsList.add(contratTEST);
		contractsList.add(contratTEST2);

		// ==============================================================

		// Bouton pour afficher les contrats

		// Création du bouton
		JButton viewContractsButton = new JButton("Afficher les contrats");
		viewContractsButton.setForeground(new Color(51, 51, 51));
		viewContractsButton.setBackground(new Color(255, 183, 77));
		cstContracts.gridy = 1;
		contentPanel.add(viewContractsButton, cstContracts);

		// Action au clique sur ce bouton
		viewContractsButton.addActionListener(e -> {
			StringBuilder contratList = new StringBuilder("Liste des contrats :\n\n");

			// Liste les contrats
			for (IData data : contractsList) {
				Contrat contrat = (Contrat) data;
				contratList.append("ID : ").append(contrat.getIdContrat()).append(" | Fournisseur SIRET : ")
						.append(contrat.getSiret()).append(" | Produit ID : ").append(contrat.getIdProduit())
						.append(" | Prix Unitaire : ").append(contrat.getPrixUni()).append(" | Début : ")
						.append(contrat.getDateDebut()).append(" | Fin : ").append(contrat.getDateFin()).append("\n");
			}

			JOptionPane.showMessageDialog(frame, contratList.toString(), "Contrats", JOptionPane.INFORMATION_MESSAGE);
		});

		// ==============================================================

		// Bouton pour ajouter un contrat

		// Création du bouton
		JButton addContractButton = new JButton("Ajouter un contrat");
		addContractButton.setForeground(new Color(51, 51, 51));
		addContractButton.setBackground(new Color(255, 183, 77));
		cstContracts.gridy = 1;
		contentPanel.add(addContractButton, cstContracts);

		// Action au clique sur ce bouton
		addContractButton.addActionListener(e -> {

			// Création des champs du formulaire
			JTextField siretField = new JTextField(15);
			JTextField produitField = new JTextField(15);
			JTextField prixField = new JTextField(15);
			JTextField dateDebutField = new JTextField(15);
			JTextField dateFinField = new JTextField(15);

			// Mise en forme du formulaire
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
					// Création d'un objet contrat pour le nouveau contrat à ajouter
					Contrat newContrat = new Contrat(siretField.getText(), Integer.parseInt(produitField.getText()),
							Double.parseDouble(prixField.getText()), dateDebutField.getText(), dateFinField.getText());
					contractsList.add(newContrat);
					Gestion.insert(newContrat, "contrat"); // Insérer le contrat dans la table contrat
					JOptionPane.showMessageDialog(frame, "Contrat ajouté avec succès.");
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, "Erreur dans les données saisies : " + ex.getMessage(),
							"Erreur", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// ==============================================================

		// Bouton pour modifier un contrat

		// Création du bouton
		JButton editContractButton = new JButton("Modifier un contrat");
		editContractButton.setForeground(new Color(51, 51, 51));
		editContractButton.setBackground(new Color(255, 183, 77));
		cstContracts.gridy = 3;
		contentPanel.add(editContractButton, cstContracts);

		// Action au clique sur ce bouton
		editContractButton.addActionListener(e -> {
			String idToEdit = JOptionPane.showInputDialog(frame, "Entrez l'ID du contrat à modifier :");
			if (idToEdit != null && !idToEdit.isEmpty()) {
				try {
					int id = Integer.parseInt(idToEdit);
					Contrat contratToEdit = null;
					for (IData data : contractsList) {
						Contrat contrat = (Contrat) data;
						if (contrat.getIdContrat() == id) {
							contratToEdit = contrat;
							break;
						}
					}

					if (contratToEdit == null) {
						JOptionPane.showMessageDialog(frame, "Contrat introuvable.", "Erreur",
								JOptionPane.ERROR_MESSAGE);
						return;
					}

					JTextField siretField = new JTextField(contratToEdit.getSiret(), 15);
					JTextField produitField = new JTextField(String.valueOf(contratToEdit.getIdProduit()), 15);
					JTextField prixField = new JTextField(String.valueOf(contratToEdit.getPrixUni()), 15);
					JTextField dateDebutField = new JTextField(contratToEdit.getDateDebut().toString(), 15);
					JTextField dateFinField = new JTextField(contratToEdit.getDateFin().toString(), 15);

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

						// Créer un objet pour l'ancien contrat
						Contrat oldContract = new Contrat(siretField.getText(),
								Integer.parseInt(produitField.getText()), Double.parseDouble(prixField.getText()),
								dateDebutField.getText(), dateFinField.getText());

						// Mise à jour des données du contat avec les nouvelles valeurs
						contratToEdit.setSiret(siretField.getText());
						contratToEdit.setIdProduit(Integer.parseInt(produitField.getText()));
						contratToEdit.setPrixUni(Double.parseDouble(prixField.getText()));
						contratToEdit.setDateDebut(dateDebutField.getText());
						contratToEdit.setDateFin(dateFinField.getText());

						// Créer un objet contrat après modification
						Contrat newContract = new Contrat(siretField.getText(),
								Integer.parseInt(produitField.getText()), Double.parseDouble(prixField.getText()),
								dateDebutField.getText(), dateFinField.getText());

						Gestion.update(oldContract, newContract, "contrat"); // Utilisation de update pour agir sur
																				// la BDD
						JOptionPane.showMessageDialog(frame, "Contrat modifié avec succès.");
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, "Erreur lors de la modification : " + ex.getMessage(),
							"Erreur", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// ==============================================================

		// Bouton pour supprimer un contrat

		// Création de ce bouton
		JButton deleteContractButton = new JButton("Supprimer un contrat");
		deleteContractButton.setForeground(new Color(51, 51, 51));
		deleteContractButton.setBackground(new Color(255, 183, 77));
		cstContracts.gridy = 2;
		contentPanel.add(deleteContractButton, cstContracts);

		// Action au clique sur ce bouton
		deleteContractButton.addActionListener(e -> {

			String idToDelete = JOptionPane.showInputDialog(frame, "Entrez l'ID du contrat à supprimer :");

			if (idToDelete != null && !idToDelete.isEmpty()) {
				try {
					int id = Integer.parseInt(idToDelete);

					Contrat contractToDelete = null;
					for (IData data : contractsList) {
						Contrat contract = (Contrat) data;
						if (contract.getIdContrat() == id) {
							contractToDelete = contract;
							break;
						}
					}

					if (contractToDelete == null) {
						JOptionPane.showMessageDialog(frame, "Contrat introuvable.", "Erreur",
								JOptionPane.ERROR_MESSAGE);
						return;
					}

					contractsList.remove(contractToDelete); // Supprimer le contrat de la List
					Gestion.delete(contractToDelete, "contrat"); // Supprimer le contrat de la BDD
					JOptionPane.showMessageDialog(frame, "Contrat supprimé avec succès.");
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, "Erreur lors de la suppression : " + ex.getMessage(), "Erreur",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// ==============================================================

		contentPanel.revalidate();
		contentPanel.repaint();

	}

}
