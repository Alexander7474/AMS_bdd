package tabs;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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

		// Création des couleurs

		Color grisDoux = new Color(51, 51, 51);
		Color jauneDoux = new Color(255, 183, 77);

		// ==============================================================

		// Création du titre de l'onglet

		JLabel titleLabelSuppliers = new JLabel("Onglet des Fournisseurs", SwingConstants.CENTER);
		titleLabelSuppliers.setFont(new Font("Arial", Font.BOLD, 20));
		titleLabelSuppliers.setForeground(grisDoux);
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
			try (ResultSet rs = statement.executeQuery("SELECT * FROM fournisseur")) {
				while (rs.next()) {
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
		insertSupplierButton.setForeground(grisDoux);
		insertSupplierButton.setBackground(jauneDoux);
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
		editSupplierButton.setForeground(grisDoux);
		editSupplierButton.setBackground(jauneDoux);
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
			JTextField nomField = new JTextField(row.get(1));
			JTextField adresseField = new JTextField(row.get(2));
			JTextField numeroTelField = new JTextField(row.get(3));
			JTextField emailField = new JTextField(row.get(4));

			// Créer un objet fournisseur pour update
			Fournisseur oldSupplier = new Fournisseur(row.get(0), row.get(1), row.get(2), row.get(3), row.get(4));

			// Mise en forme du formulaire
			JPanel form = new JPanel(new GridLayout(5, 2));
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
				Fournisseur fournisseur = new Fournisseur(oldSupplier.getSiret(), nomField.getText(),
						adresseField.getText(), numeroTelField.getText(), emailField.getText());
				Gestion.update(fournisseur, "fournisseur", "siret", fournisseur.getSiret());
			}
		});

		// ==============================================================

		// Bouton pour supprimer un fournisseur

		JButton deleteSupplierButton = new JButton("Supprimer un fournisseur");
		deleteSupplierButton.setForeground(grisDoux);
		deleteSupplierButton.setBackground(jauneDoux);
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

		
		
		
		//===============================================================
		// Titre de l'onglet contact
		JLabel titleLabelContact = new JLabel("Onglet des Contact", SwingConstants.CENTER);
		titleLabelContact.setFont(new Font("Arial", Font.BOLD, 20));
		GridBagConstraints cstContact = new GridBagConstraints();
		cstContact.gridy = 0;
		cstContact.gridx = 50;
		cstContact.insets = new Insets(10, 10, 10, 10);
		contentPanel.add(titleLabelContact, cstContact);

		String[] columnNamesContact = { "ID contact" , "Nom", "Prénom" , "Téléphone", "Email"};
		DefaultTableModel tableModelContact = new DefaultTableModel(columnNamesContact, 0);
		JTable contactTable = new JTable(tableModelContact);
		JScrollPane scrollPaneContact = new JScrollPane(contactTable);
		cstContact.gridy = 1;
		contentPanel.add(scrollPaneContact, cstContact);
		
		List<IData> contactsList = new ArrayList<>(); 
		
		ListSelectionModel selectionModel = suppliersTable.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {
        	
  
        	@Override
        	public void valueChanged(ListSelectionEvent ev) {
        		
        		tableModelContact.setRowCount(0);
        		contactsList.clear();
        	
				int selectedRowC = suppliersTable.getSelectedRow();
				if (selectedRowC != -1) {
					// recup des info dans la base
					
					try {
						Statement statement = Connexion.getConnexion().createStatement();
						try (ResultSet rs = statement.executeQuery("SELECT c.* "
								+ "FROM contact_fournisseur cf "
								+ "JOIN contact c ON cf.id_contact = c.id_contact "
								+ "WHERE siret = '" + tableData.get(selectedRowC).get(0) + "'")) {
							while (rs.next()) {
								Contact c = new Contact(rs);
								contactsList.add(c);
							}
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		
					for (IData item : contactsList) {
						if (item instanceof Contact contact) {
							tableModelContact.addRow(new Object[] { contact.getIdContact(), contact.getNom(), contact.getPrenom(), contact.getNumeroTel(), contact.getEmail() });
						}
					}
				}
				
				titleLabelContact.setText("Onglet des Contacts ("+tableData.get(selectedRowC).get(1)+")");
        	}
        });
        
     // ==============================================================

 		// Bouton pour lier les contact
 		
 		JButton linkContactButton = new JButton("Lier contact/fournisseur");
 		linkContactButton.setForeground(grisDoux);
 		linkContactButton.setBackground(jauneDoux);
 		cstSuppliers.gridy = 5;
 		cstSuppliers.gridx = 0;
 		contentPanel.add(linkContactButton, cstSuppliers);

 		// Action au clique sur ce bouton
 		linkContactButton.addActionListener(deleteContactEvent -> {
     	
 			int selectedRowC = contactTable.getSelectedRow();
 			int selectedRowS = suppliersTable.getSelectedRow();
 			
 			if (selectedRowC != -1 && selectedRowS != -1) {
 				
 				ContactFournisseur cf = new ContactFournisseur((Contact) contactsList.get(selectedRowC), (Fournisseur) data.get(selectedRowS));
 				
 				Gestion.insert(cf, "contact_fournisseur");
 			}
 		});
 		
 	     // ==============================================================

 	 		// Bouton pour delier les contact
 	 		
 	 		JButton unlinkContactButton = new JButton("Délier contact/fournisseur");
 	 		unlinkContactButton.setForeground(grisDoux);
 	 		unlinkContactButton.setBackground(jauneDoux);
 	 		cstSuppliers.gridy = 6;
 	 		cstSuppliers.gridx = 0;
 	 		contentPanel.add(unlinkContactButton, cstSuppliers);

 	 		// Action au clique sur ce bouton
 	 		unlinkContactButton.addActionListener(deleteContactEvent -> {
 	     	
 	 			int selectedRowC = contactTable.getSelectedRow();
 	 			int selectedRowS = suppliersTable.getSelectedRow();
 	 			if (selectedRowC != -1 && selectedRowS != -1) {
 	 				ContactFournisseur cf = new ContactFournisseur((Contact) contactsList.get(selectedRowC), (Fournisseur) data.get(selectedRowS));
 	 				
 	 				Gestion.delete(cf, "contact_fournisseur");
 	 			}
 	 		});
        
		// ==============================================================

		// Bouton pour afficher les contacts
		
		JButton showContactButton = new JButton("Afficher tous les contact(s)");
		showContactButton.setForeground(grisDoux);
		showContactButton.setBackground(jauneDoux);
		cstSuppliers.gridy = 4;
		cstSuppliers.gridx = 50;
		contentPanel.add(showContactButton, cstSuppliers);

		// Action au clique sur ce bouton
		showContactButton.addActionListener(deleteContactEvent -> {
			tableModelContact.setRowCount(0);
    		contactsList.clear();
    	
			int selectedRowC = suppliersTable.getSelectedRow();
			if (selectedRowC != -1) {
				// recup des info dans la base
				
				try {
					Statement statement = Connexion.getConnexion().createStatement();
					try (ResultSet rs = statement.executeQuery("SELECT * FROM contact")) {
						while (rs.next()) {
							Contact c = new Contact(rs);
							contactsList.add(c);
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
				for (IData item : contactsList) {
					if (item instanceof Contact contact) {
						tableModelContact.addRow(new Object[] { contact.getIdContact(), contact.getNom(), contact.getPrenom(), contact.getNumeroTel(), contact.getEmail() });
					}
				}
				
				titleLabelContact.setText("Onglet des Contacts (ALL)");
			}
		});

		// Bouton pour ajouter un contact

		JButton insertContactButton = new JButton("Ajouter un contact");
		insertContactButton.setForeground(grisDoux);
		insertContactButton.setBackground(jauneDoux);
		cstSuppliers.gridy = 2;
		cstSuppliers.gridx = 50;
		contentPanel.add(insertContactButton, cstSuppliers);

		insertContactButton.addActionListener(insertContactEvent -> {
			int selectedRow = 1;
			
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
					Gestion.insert(contact, "contact");
					JOptionPane.showMessageDialog(frame, "Contact ajouté avec succès !");
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, "Erreur lors de l'ajout du contact : " + ex.getMessage(),
							"Erreur", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		// ==============================================================

		// Bouton pour supprimer un contact

		// Création d'un bouton
		JButton deleteContactButton = new JButton("Supprimer un contact");
		deleteContactButton.setForeground(grisDoux);
		deleteContactButton.setBackground(jauneDoux);
		cstSuppliers.gridy = 3;
		cstSuppliers.gridx = 50;
		contentPanel.add(deleteContactButton, cstSuppliers);

		// Action au clique sur ce bouton
		deleteContactButton.addActionListener(deleteContactEvent -> {
			int selectedRow = contactTable.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un contact pour supprimer.",
						"Erreur", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Récupération du contact
			Contact selectedContact = (Contact) contactsList.get(selectedRow);

			try {
				Gestion.execute("DELETE FROM contact_fournisseur WHERE id_contact = " + selectedContact.getIdContact());
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Gestion.delete(selectedContact, "contact");

		});


		contentPanel.revalidate();
		contentPanel.repaint();

	};
	
	// ==============================================================

};
