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
import data.dependance_multi.ProduitFournisseur;
import data.entity.*;
import swing_gui.TabManager;

public class ProductsTab {

	public static void loadProductsTab(JPanel contentPanel, JFrame frame) {

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
		// recup des infos sur les fournisseurs 
		
		Vector<IData> data = Gestion.getAllFromTable("fournisseur", new Fournisseur());
		
		// ==============================================================

		// Afficher les Fournisseurs dans un tableau

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

		// Bouton pour insérer un Fournisseur

		JButton insertSupplierButton = TabManager.getButton("Ajouter un Fournisseur");
		cstSuppliers.gridy = 2;
		contentPanel.add(insertSupplierButton, cstSuppliers);

		insertSupplierButton.addActionListener(e -> {

			// Formulaire pour saisir les informations du Fournisseur
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

			int result = JOptionPane.showConfirmDialog(frame, form, "Ajouter un Fournisseur",
					JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				// Création de l'objet Fournisseur avec les valeurs entrés par l'utilisateur
				Fournisseur fournisseur = new Fournisseur(siretField.getText(), nomField.getText(),
						adresseField.getText(), numeroTelField.getText(), emailField.getText());

				try {
					// Insertion du Fournisseur dans la base avec Gestion
					Gestion.insert(fournisseur, "fournisseur");
					JOptionPane.showMessageDialog(frame, "Fournisseur ajouté avec succès !");
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, "Erreur lors de l'ajout du Fournisseur : " + ex.getMessage());
				}
			}
		});

		// ==============================================================

		// Bouton pour modifier un Fournisseur

		JButton editSupplierButton = TabManager.getButton("Modifier un Fournisseur");
		cstSuppliers.gridy = 3;
		contentPanel.add(editSupplierButton, cstSuppliers);

		editSupplierButton.addActionListener(editSupplierEvent -> {
			int selectedRow = suppliersTable.getSelectedRow(); // Cliquer sur le Fournisseur que l'on souhaite
																// modifier
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un Fournisseur à modifier.", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			Fournisseur selectedF = (Fournisseur) data.get(selectedRow);
			
			// Récupération des données de ce Fournisseur
			JTextField nomField = new JTextField(selectedF.getNom());
			JTextField adresseField = new JTextField(selectedF.getAdresse());
			JTextField numeroTelField = new JTextField(selectedF.getNumero_tel());
			JTextField emailField = new JTextField(selectedF.getEmail());

			// Créer un objet Fournisseur pour update
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

			int result = JOptionPane.showConfirmDialog(frame, form, "Modifier un Fournisseur",
					JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				Fournisseur fournisseur = new Fournisseur(oldSupplier.getSiret(), nomField.getText(),
						adresseField.getText(), numeroTelField.getText(), emailField.getText());
				
				Gestion.update(fournisseur, "fournisseur", "siret", fournisseur.getSiret());
			}
		});

		// ==============================================================

		// Bouton pour supprimer un Fournisseur

		JButton deleteSupplierButton = TabManager.getButton("Supprimer un Fournisseur");
		cstSuppliers.gridy = 4;
		contentPanel.add(deleteSupplierButton, cstSuppliers);

		deleteSupplierButton.addActionListener(deleteSupplierEvent -> {
			int selectedRow = suppliersTable.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un Fournisseur à supprimer.", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Création du Fournisseur à supprimer
			Fournisseur supplierToDelete = (Fournisseur) data.get(selectedRow);

			int confirm = JOptionPane.showConfirmDialog(frame, "Êtes-vous sûr de vouloir supprimer ce Fournisseur ?",
					"Confirmation", JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				try {
					data.remove(supplierToDelete);
					
					Gestion.delete(supplierToDelete, "fournisseur");
					JOptionPane.showMessageDialog(frame, "Fournisseur supprimé avec succès !");
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, "Erreur : " + ex.getMessage(), "Erreur",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});	
		
		
		//===============================================================
		// Titre de l'onglet Produit
		JLabel titleLabelProduit = new JLabel("Onglet des Produits", SwingConstants.CENTER);
		titleLabelProduit.setFont(new Font("Arial", Font.BOLD, 20));
		GridBagConstraints cstProduit = new GridBagConstraints();
		cstProduit.gridy = 0;
		cstProduit.gridx = 50;
		cstProduit.insets = new Insets(10, 10, 10, 10);
		contentPanel.add(titleLabelProduit, cstProduit);
		
		//===============================================================

		String[] columnNamesProduit = { "ID Produit" , "Nom", "Description" , "Catégorie"};
		DefaultTableModel tableModelProduit = new DefaultTableModel(columnNamesProduit, 0);
		JTable produitTable = TabManager.getTable(500, tableModelProduit);
		cstProduit.gridy = 1;
		
		contentPanel.add(TabManager.getScrollPane(produitTable), cstProduit);
		
		List<IData> produitsList = new ArrayList<>(); 
		
		ListSelectionModel selectionModel = suppliersTable.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {
	        	
	    	@Override
	    	public void valueChanged(ListSelectionEvent ev) {
	    		
	    		tableModelProduit.setRowCount(0);
	    		produitsList.clear();
	    	
				int selectedRowC = suppliersTable.getSelectedRow();
				if (selectedRowC != -1) {
					// recup des info dans la base
					
					try {
						Statement statement = Connexion.getConnexion().createStatement();
						try (ResultSet rs = statement.executeQuery("SELECT c.* "
								+ "FROM produit_fournisseur cf "
								+ "JOIN produit c ON cf.id_produit = c.id_produit "
								+ "WHERE siret = '" + ((Fournisseur) data.get(selectedRowC)).getSiret() + "'")) {
							while (rs.next()) {
								Produit p = new Produit(rs);
								produitsList.add(p);
							}
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		
					for (IData item : produitsList) {
						if (item instanceof Produit produit) {
							tableModelProduit.addRow(new Object[] { produit.getIdProduit(), produit.getNom(), produit.getDesc(), produit.getCategorie()});
						}
					}
					
					titleLabelProduit.setText("Onglet des Produits ("+ ((Fournisseur) data.get(selectedRowC)).getSiret() +")");
				}
	    	}
        });
        
     // ==============================================================

 		// Bouton pour lier les Produit
 		
 		JButton linkProduitButton = TabManager.getButton("Lier Produit/Fournisseur");
 		cstSuppliers.gridy = 5;
 		cstSuppliers.gridx = 0;
 		contentPanel.add(linkProduitButton, cstSuppliers);

 		// Action au clique sur ce bouton
 		linkProduitButton.addActionListener(deleteProduitEvent -> {
     	
 			int selectedRowC = produitTable.getSelectedRow();
 			int selectedRowS = suppliersTable.getSelectedRow();
 			
 			if (selectedRowC != -1 && selectedRowS != -1) {
 				
 				ProduitFournisseur pf = new ProduitFournisseur((Produit) produitsList.get(selectedRowC), (Fournisseur) data.get(selectedRowS), 0.0);
 				
 				Gestion.insert(pf, "produit_fournisseur");
 				
 				JOptionPane.showMessageDialog(frame, "Fournisseur lié au produit avec succès !");
 			}
 		});
 		
 	     // ==============================================================

 	 		// Bouton pour delier les Produit
 	 		
 	 		JButton unlinkProduitButton = TabManager.getButton("Délier Produit/Fournisseur");
 	 		cstSuppliers.gridy = 6;
 	 		cstSuppliers.gridx = 0;
 	 		contentPanel.add(unlinkProduitButton, cstSuppliers);

 	 		// Action au clique sur ce bouton
 	 		unlinkProduitButton.addActionListener(deleteProduitEvent -> {
 	     	
 	 			int selectedRowC = produitTable.getSelectedRow();
 	 			int selectedRowS = suppliersTable.getSelectedRow();
 	 			if (selectedRowC != -1 && selectedRowS != -1) {
 	 				ProduitFournisseur cf = new ProduitFournisseur((Produit) produitsList.get(selectedRowC), (Fournisseur) data.get(selectedRowS) , 0.0);

					Gestion.delete(cf, "produit_fournisseur");
					
					JOptionPane.showMessageDialog(frame, "Fournisseur délié au produit avec succès !");
					tableModelProduit.removeRow(selectedRowC);
 	 			}
 	 		});
        
		// ==============================================================

		// Bouton pour afficher les Produits
		
		JButton showProduitButton = TabManager.getButton("Afficher tous les Produit(s)");
		cstSuppliers.gridy = 4;
		cstSuppliers.gridx = 50;
		contentPanel.add(showProduitButton, cstSuppliers);

		// Action au clique sur ce bouton
		showProduitButton.addActionListener(deleteProduitEvent -> {
			tableModelProduit.setRowCount(0);
    		produitsList.clear();
    	
			int selectedRowC = suppliersTable.getSelectedRow();
			if (selectedRowC != -1) {
				// recup des info dans la base
				
				try {
					Statement statement = Connexion.getConnexion().createStatement();
					try (ResultSet rs = statement.executeQuery("SELECT * FROM produit")) {
						while (rs.next()) {
							Produit c = new Produit(rs);
							produitsList.add(c);
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
				for (IData item : produitsList) {
					if (item instanceof Produit produit) {
						tableModelProduit.addRow(new Object[] { produit.getIdProduit(), produit.getNom(), produit.getDesc(), produit.getCategorie()  });
					}
				}
				titleLabelProduit.setText("Onglet des Produits (ALL)");
			}
		});

		// Bouton pour ajouter un Produit

		JButton insertProduitButton = TabManager.getButton("Ajouter un Produit");
		cstSuppliers.gridy = 2;
		cstSuppliers.gridx = 50;
		contentPanel.add(insertProduitButton, cstSuppliers);

		insertProduitButton.addActionListener(insertProduitEvent -> {
			int selectedRow = 1;
			
			JTextField nomField = new JTextField(15);
			JTextField descField = new JTextField(15);
			JTextField cateField = new JTextField(15);

			JPanel form = new JPanel(new GridLayout(4, 2));
			form.add(new JLabel("Nom : "));
			form.add(nomField);
			form.add(new JLabel("Description : "));
			form.add(descField);
			form.add(new JLabel("Catégorie : "));
			form.add(cateField);

			int result = JOptionPane.showConfirmDialog(frame, form, "Ajouter un Produit", JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				try {
					Produit produit = new Produit(nomField.getText(), descField.getText(),
							cateField.getText());
					Gestion.insert(produit, "produit");
					JOptionPane.showMessageDialog(frame, "Produit ajouté avec succès !");
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, "Erreur lors de l'ajout du Produit : " + ex.getMessage(),
							"Erreur", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		// ==============================================================

		// Bouton pour supprimer un Produit

		// Création d'un bouton
		JButton deleteProduitButton = TabManager.getButton("Supprimer un Produit");
		cstSuppliers.gridy = 3;
		cstSuppliers.gridx = 50;
		contentPanel.add(deleteProduitButton, cstSuppliers);

		// Action au clique sur ce bouton
		deleteProduitButton.addActionListener(deleteProduitEvent -> {
			int selectedRow = produitTable.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un Produit pour supprimer.",
						"Erreur", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Récupération du Produit
			Produit selectedProduit = (Produit) produitsList.get(selectedRow);

			Gestion.delete(selectedProduit, "produit");

		});


		contentPanel.revalidate();
		contentPanel.repaint();

	};
	
	// ==============================================================

};