package tabs;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import data.Connexion;
import data.Gestion;
import data.GestionException;
import data.IData;
import data.dependance_multi.ContactFournisseur;
import data.entity.*;
import swing_gui.TabManager;

public class SuppliersTab {

	public static void loadSuppliersTab(JPanel contentPanel, JFrame frame) {

		// ==============================================================

		// Création des couleurs

		Color grisDoux = new Color(51, 51, 51);

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

		
		Vector<IData> data = Gestion.getAllFromTable("fournisseur", new Fournisseur());
 
		String[] columnNames = { "SIRET", "Nom", "Adresse", "Téléphone", "Email" }; // Colonnes du tableau
		
		DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

		for (IData item : data) {
			if (item instanceof Fournisseur fournisseur) {
				tableModel.addRow(new Object[] {
						fournisseur.getSiret(),
						fournisseur.getNom(),
						fournisseur.getAdresse(),
						fournisseur.getNumero_tel(),
						fournisseur.getEmail()
				});
			}
		}
		JTable suppliersTable = TabManager.getTable(500, tableModel);
		cstSuppliers.gridy = 1;
		
		contentPanel.add(TabManager.getScrollPane(suppliersTable), cstSuppliers);

		// ==============================================================

		// Bouton pour insérer un fournisseur

		JButton insertSupplierButton = TabManager.getButton("Ajouter un fournisseur");
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

		JButton editSupplierButton = TabManager.getButton("Modifier un fournisseur");
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

Fournisseur selectedF = (Fournisseur) data.get(selectedRow);
			
			// Récupération des données de ce Fournisseur
			JTextField nomField = new JTextField(selectedF.getNom());
			JTextField adresseField = new JTextField(selectedF.getAdresse());
			JTextField numeroTelField = new JTextField(selectedF.getNumero_tel());
			JTextField emailField = new JTextField(selectedF.getEmail());


			// Créer un objet fournisseur pour update
			Fournisseur oldSupplier = new Fournisseur(selectedF);

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

		JButton deleteSupplierButton = TabManager.getButton("Supprimer un fournisseur");
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
			Fournisseur supplierToDelete = (Fournisseur) data.get(selectedRow);

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
		JTable contactTable = TabManager.getTable(500, tableModelContact);
		cstContact.gridy = 1;
		
		contentPanel.add(TabManager.getScrollPane(contactTable), cstContact);
		
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
								+ "WHERE siret = '" + ((Fournisseur) data.get(selectedRowC)).getSiret() + "'")) {
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
				
				titleLabelContact.setText("Onglet des Contacts ("+((Fournisseur) data.get(selectedRowC)).getSiret()+")");
        	}
        });
        
     // ==============================================================

 		// Bouton pour lier les contact
 		
 		JButton linkContactButton = TabManager.getButton("Lier contact/fournisseur");
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
 				
 				JOptionPane.showMessageDialog(frame, "Fournisseur lié au contact avec succès !");
 			}
 		});
 		
 	     // ==============================================================

 	 		// Bouton pour delier les contact
 	 		
 	 		JButton unlinkContactButton = TabManager.getButton("Délier contact/fournisseur");
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
 	 				JOptionPane.showMessageDialog(frame, "Fournisseur délié au contact avec succès !");
 	 				tableModelContact.removeRow(selectedRowC);
 	 			}
 	 		});
        
		// ==============================================================

		// Bouton pour afficher les contacts
		
		JButton showContactButton = TabManager.getButton("Afficher tous les contact(s)");
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

		JButton insertContactButton = TabManager.getButton("Ajouter un contact");
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
		JButton deleteContactButton = TabManager.getButton("Supprimer un contact");
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

			Gestion.delete(selectedContact, "contact");

		});


		contentPanel.revalidate();
		contentPanel.repaint();

	};
	
	// ==============================================================

};
