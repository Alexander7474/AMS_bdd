package tabs;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;

import data.Connexion;
import data.Gestion;
import data.IData;
import data.dependance_multi.ContactFournisseur;
import data.entity.*;

public class SuppliersTab {

	public static void loadSuppliersTab(JPanel contentPanel, JFrame frame) {

		// ==============================================================

		// Création du titre de l'onglet

		JLabel titleLabelSuppliers = new JLabel("Onglet des fournisseurs", SwingConstants.CENTER);
		titleLabelSuppliers.setFont(new Font("Arial", Font.BOLD, 20));
		titleLabelSuppliers.setForeground(new Color(51, 51, 51));
		GridBagConstraints cstSuppliers = new GridBagConstraints();
		cstSuppliers.gridy = 0;
		cstSuppliers.gridx = 0;
		cstSuppliers.insets = new Insets(10, 10, 10, 10);
		contentPanel.add(titleLabelSuppliers, cstSuppliers);

		// ==============================================================

		// Afficher les fournisseurs dans un tableau

		String[] columnNames = { "SIRET", "Nom", "Adresse", "Téléphone", "Email" }; // Colonnes du tableau
		Vector<IData> data = new Vector<>(); // Les fournisseurs seront dans le vecteur data
		
		// recup des info dans la base
		try {
			Statement statement = Connexion.getConnexion().createStatement();
			try(ResultSet rs = statement.executeQuery("SELECT * FROM fournisseur")){
				while(rs.next()) {
					Fournisseur f = new Fournisseur(rs);
					data.add(f);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		Vector<Vector<String>> tableData = new Vector<>();

		for (IData item : data) {
			if (item instanceof Fournisseur fournisseur) {
				Vector<String> row = new Vector<>();
				row.add(fournisseur.getSiret());
				row.add(fournisseur.getNom());
				row.add(fournisseur.getAdresse());
				row.add(fournisseur.getNumero_tel());
				row.add(fournisseur.getEmail());
				tableData.add(row);
			}
		}

		JTable suppliersTable = new JTable(tableData, new Vector<>(List.of(columnNames)));
		JScrollPane scrollPane = new JScrollPane(suppliersTable);
		cstSuppliers.gridy = 1;
		contentPanel.add(scrollPane, cstSuppliers);

		// ==============================================================

		// Bouton pour insérer un fournisseur

		JButton insertSupplierButton = new JButton("Ajouter un fournisseur");
		insertSupplierButton.setForeground(new Color(51, 51, 51));
		insertSupplierButton.setBackground(new Color(255, 183, 77));
		cstSuppliers.gridy = 2;
		contentPanel.add(insertSupplierButton, cstSuppliers);

		insertSupplierButton.addActionListener(e -> {

			// Formulaire pour saisir les informations du fournisseur
			JTextField siretField = new JTextField(15);
			JTextField nomField = new JTextField(15);
			JTextField adresseField = new JTextField(15);
			JTextField numeroTelField = new JTextField(15);
			JTextField emailField = new JTextField(15);

			// Mise en forme du formulaire
			JPanel form = new JPanel(new GridLayout(5, 2));
			form.add(new JLabel("SIRET : "));
			form.add(siretField);
			form.add(new JLabel("Nom : "));
			form.add(nomField);
			form.add(new JLabel("Adresse : "));
			form.add(adresseField);
			form.add(new JLabel("Téléphone : "));
			form.add(numeroTelField);
			form.add(new JLabel("Email : "));
			form.add(emailField);

			int result = JOptionPane.showConfirmDialog(frame, form, "Ajouter un fournisseur",
					JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				// Création de l'objet fournisseur avec les valeurs entrés par l'utilisateur
				Fournisseur fournisseur = new Fournisseur(siretField.getText(), nomField.getText(),
						adresseField.getText(), numeroTelField.getText(), emailField.getText());

				try {
					// Insertion du fournisseur dans la base avec Gestion
					Gestion.insert(fournisseur, "fournisseur");
					JOptionPane.showMessageDialog(frame, "Fournisseur ajouté avec succès !");
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, "Erreur lors de l'ajout du fournisseur : " + ex.getMessage());
				}
			}
		});

		// ==============================================================

		// Bouton pour modifier un fournisseur

		JButton editSupplierButton = new JButton("Modifier un fournisseur");
		editSupplierButton.setForeground(new Color(51, 51, 51));
		editSupplierButton.setBackground(new Color(255, 183, 77));
		cstSuppliers.gridy = 3;
		contentPanel.add(editSupplierButton, cstSuppliers);

		editSupplierButton.addActionListener(editSupplierEvent -> {
			int selectedRow = suppliersTable.getSelectedRow(); // Cliquer sur le fournisseur que l'on souhaite
																// modifier
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un fournisseur à modifier.", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			Vector<String> row = tableData.get(selectedRow); // Le fournisseur a modifié est stocké dans un vecteur
																// (row)
			// Récupération des données de ce fournisseur
			JTextField siretField = new JTextField(row.get(0));
			JTextField nomField = new JTextField(row.get(1));
			JTextField adresseField = new JTextField(row.get(2));
			JTextField numeroTelField = new JTextField(row.get(3));
			JTextField emailField = new JTextField(row.get(4));

			// Créer un objet fournisseur pour update
			Fournisseur oldSupplier = new Fournisseur(row.get(0), row.get(1), row.get(2), row.get(3), row.get(4));

			// Mise en forme du formulaire
			JPanel form = new JPanel(new GridLayout(5, 2));
			form.add(new JLabel("SIRET : "));
			form.add(siretField);
			form.add(new JLabel("Nom : "));
			form.add(nomField);
			form.add(new JLabel("Adresse : "));
			form.add(adresseField);
			form.add(new JLabel("Téléphone : "));
			form.add(numeroTelField);
			form.add(new JLabel("Email : "));
			form.add(emailField);

			int result = JOptionPane.showConfirmDialog(frame, form, "Modifier un fournisseur",
					JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				Fournisseur fournisseur = new Fournisseur(siretField.getText(), nomField.getText(),
						adresseField.getText(), numeroTelField.getText(), emailField.getText());
				try {
					Gestion.update(oldSupplier, fournisseur, "fournisseurs"); // !!! update n'est pas encore défini
					JOptionPane.showMessageDialog(frame, "Fournisseur modifié avec succès !");
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, "Erreur : " + ex.getMessage(), "Erreur",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// ==============================================================

		// Bouton pour supprimer un fournisseur

		JButton deleteSupplierButton = new JButton("Supprimer un fournisseur");
		deleteSupplierButton.setForeground(new Color(51, 51, 51));
		deleteSupplierButton.setBackground(new Color(255, 183, 77));
		cstSuppliers.gridy = 4;
		contentPanel.add(deleteSupplierButton, cstSuppliers);

		deleteSupplierButton.addActionListener(deleteSupplierEvent -> {
			int selectedRow = suppliersTable.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un fournisseur à supprimer.", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Récupération des données du fournisseur
			String siret = tableData.get(selectedRow).get(0);
			String nom = tableData.get(selectedRow).get(1);
			String adresse = tableData.get(selectedRow).get(2);
			String telephone = tableData.get(selectedRow).get(3);
			String email = tableData.get(selectedRow).get(4);

			// Création du fournisseur à supprimer
			Fournisseur supplierToDelete = new Fournisseur(siret, nom, adresse, telephone, email);

			int confirm = JOptionPane.showConfirmDialog(frame, "Êtes-vous sûr de vouloir supprimer ce fournisseur ?",
					"Confirmation", JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				try {
					Gestion.delete(supplierToDelete, "fournisseur");
					JOptionPane.showMessageDialog(frame, "Fournisseur supprimé avec succès !");
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, "Erreur : " + ex.getMessage(), "Erreur",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// ==============================================================

		// Bouton pour ajouter un contact

		JButton insertContactButton = new JButton("Ajouter un contact");
		insertContactButton.setForeground(new Color(51, 51, 51));
		insertContactButton.setBackground(new Color(255, 183, 77));
		cstSuppliers.gridy = 5;
		contentPanel.add(insertContactButton, cstSuppliers);

		insertContactButton.addActionListener(insertContactEvent -> {
			int selectedRow = suppliersTable.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un fournisseur pour ajouter un contact.",
						"Erreur", JOptionPane.ERROR_MESSAGE);
				return;
			}

			JTextField nomField = new JTextField(15);
			JTextField prenomField = new JTextField(15);
			JTextField numeroTelField = new JTextField(15);
			JTextField emailField = new JTextField(15);

			JPanel form = new JPanel(new GridLayout(4, 2));
			form.add(new JLabel("Nom : "));
			form.add(nomField);
			form.add(new JLabel("Prénom : "));
			form.add(prenomField);
			form.add(new JLabel("Téléphone : "));
			form.add(numeroTelField);
			form.add(new JLabel("Email : "));
			form.add(emailField);

			int result = JOptionPane.showConfirmDialog(frame, form, "Ajouter un contact", JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				try {
					Contact contact = new Contact(nomField.getText(), prenomField.getText(), numeroTelField.getText(),
							emailField.getText());
					Fournisseur selectedSupplier = (Fournisseur) data.get(selectedRow);
					ContactFournisseur item = new ContactFournisseur(contact, selectedSupplier);
					Gestion.insert(item, "contact_fournisseurs");
					JOptionPane.showMessageDialog(frame, "Contact ajouté avec succès !");
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, "Erreur lors de l'ajout du contact : " + ex.getMessage(),
							"Erreur", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// ==============================================================

		// Bouton pour afficher les contacts

		JButton viewContactsButton = new JButton("Afficher les contacts");
		viewContactsButton.setForeground(new Color(51, 51, 51));
		viewContactsButton.setBackground(new Color(255, 183, 77));
		cstSuppliers.gridy = 6;
		contentPanel.add(viewContactsButton, cstSuppliers);

		viewContactsButton.addActionListener(viewContactsEvent -> {
			int selectedRow = suppliersTable.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un fournisseur pour afficher ses contacts.",
						"Erreur", JOptionPane.ERROR_MESSAGE);
				return;
			}

			Vector<Contact> contacts = new Vector<>();
			// Test de vérification des contacts manuellement
			contacts.add(new Contact("Florent", "CAGNARD", "0101010101", "florent.cagnard@alumni.univ-avignon.fr"));
			contacts.add(new Contact("Alexandre", "LANTERNIER", "0202020202",
					"alexandre.lanternier@alumni.univ-avignon.fr"));

			String[] contactColumnNames = { "Nom", "Prénom", "Téléphone", "Email" };
			Vector<Vector<String>> contactTableData = new Vector<>();

			for (Contact contact : contacts) {
				Vector<String> row = new Vector<>();
				row.add(contact.getNom());
				row.add(contact.getPrenom());
				row.add(contact.getNumeroTel());
				row.add(contact.getEmail());
				contactTableData.add(row);
			}

			JTable contactsTable = new JTable(contactTableData, new Vector<>(List.of(contactColumnNames)));
			JScrollPane contactsScrollPane = new JScrollPane(contactsTable);

			JFrame contactsFrame = new JFrame("Contacts du fournisseur");
			contactsFrame.add(contactsScrollPane);
			contactsFrame.setSize(500, 300);
			contactsFrame.setVisible(true);
			contactsFrame.setLocationRelativeTo(null);
		});

		// ==============================================================

		// Bouton pour supprimer un contact

		// Création d'un bouton
		JButton deleteContactButton = new JButton("Supprimer un contact");
		deleteContactButton.setForeground(new Color(51, 51, 51));
		deleteContactButton.setBackground(new Color(255, 183, 77));
		cstSuppliers.gridy = 7;
		contentPanel.add(deleteContactButton, cstSuppliers);

		// Action au clique sur ce bouton
		deleteContactButton.addActionListener(deleteContactEvent -> {
			int selectedRow = suppliersTable.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un fournisseur pour supprimer un contact.",
						"Erreur", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Récupération du fournisseur
			Fournisseur selectedSupplier = (Fournisseur) data.get(selectedRow);

			// Affichage des contacts (comme pour le bouton afficher contacts)
			Vector<Contact> contacts = new Vector<>();

			String[] contactColumnNames = { "Nom", "Prénom", "Téléphone", "Email" };
			Vector<Vector<String>> contactTableData = new Vector<>();

			for (Contact contact : contacts) {
				Vector<String> row = new Vector<>();
				row.add(contact.getNom());
				row.add(contact.getPrenom());
				row.add(contact.getNumeroTel());
				row.add(contact.getEmail());
				contactTableData.add(row);
			}

			JTable contactsTable = new JTable(contactTableData, new Vector<>(List.of(contactColumnNames)));
			JScrollPane contactsScrollPane = new JScrollPane(contactsTable);

			JFrame contactsFrame = new JFrame("Contacts du fournisseur");
			contactsFrame.add(contactsScrollPane);
			contactsFrame.setSize(500, 300);
			contactsFrame.setVisible(true);
			contactsFrame.setLocationRelativeTo(null);

			// Récupérer le contact sur lequel on a cliqué dans contactsFrame
			contactsTable.getSelectionModel().addListSelectionListener(e -> {
				if (!e.getValueIsAdjusting()) {
					int contactRow = contactsTable.getSelectedRow();
					if (contactRow != -1) {
						deleteContactButton.setEnabled(true);
					}
				}
			});

			// Quand on clique sur le bouton après avoir cliqué sur un contact
			deleteContactButton.addActionListener(deleteContactAction -> {
				int contactRow = contactsTable.getSelectedRow();
				if (contactRow == -1) {
					JOptionPane.showMessageDialog(contactsFrame, "Veuillez sélectionner un contact à supprimer.",
							"Erreur", JOptionPane.ERROR_MESSAGE);
					return;
				}

				Contact contactToDelete = contacts.get(contactRow);
				int confirm = JOptionPane.showConfirmDialog(contactsFrame,
						"Êtes-vous sûr de vouloir supprimer ce contact ?", "Confirmation", JOptionPane.YES_NO_OPTION);

				if (confirm == JOptionPane.YES_OPTION) {
					try {
						Gestion.delete(contactToDelete, "contact_fournisseurs"); // Appel de la fonction delete dans
																					// gestion
						JOptionPane.showMessageDialog(contactsFrame, "Contact supprimé avec succès !");
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(contactsFrame,
								"Erreur lors de la suppression du contact : " + ex.getMessage(), "Erreur",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			});
		});

		contentPanel.revalidate();
		contentPanel.repaint();

	};

};
