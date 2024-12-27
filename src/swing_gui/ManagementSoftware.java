package swing_gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import data.Connexion;
import data.Gestion;
import data.IData;
import data.dependance_multi.ContactFournisseur;
import data.entity.Achat;
import data.entity.Commande;
import data.entity.Contact;
import data.entity.Contrat;
import data.entity.Fournisseur;
import java.awt.*;
import java.net.ConnectException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ManagementSoftware {

	public static void loadMainApplication(String username) {

		JFrame frame = new JFrame("AMS - Logiciel de gestion");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Création de la barre latérale
		JPanel sidebar = new JPanel();
		sidebar.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
		sidebar.setPreferredSize(new Dimension(300, 0));
		sidebar.setBackground(new Color(168, 213, 186)); // Vert menthe

		// Affichage de l'utilisateur connecté
		JLabel userLabel = new JLabel("Utilisateur : " + username);
		userLabel.setFont(new Font("Arial", Font.BOLD, 20));
		userLabel.setForeground(new Color(74, 144, 226)); // Bleu moyen
		sidebar.add(userLabel);

		// Affichage du rôle de l'utilisateur connecté
		JLabel roleLabel = new JLabel("Rôle : " + UserManager.getUserRole(username));
		roleLabel.setFont(new Font("Arial", Font.BOLD, 20));
		roleLabel.setForeground(new Color(194, 149, 69)); // Beige doré saturé
		sidebar.add(roleLabel);
		sidebar.add(Box.createVerticalStrut(50));

		// Bouton pour accéder à la gestion des fournisseurs
		JButton suppliers = new JButton("Fournisseurs");
		suppliers.setBackground(new Color(255, 183, 77));
		sidebar.add(suppliers);
		sidebar.add(Box.createVerticalStrut(15));

		// Bouton pour accéder à la gestion des produits
		JButton products = new JButton("Produits");
		products.setBackground(new Color(255, 183, 77));
		sidebar.add(products);
		sidebar.add(Box.createVerticalStrut(15));

		// Bouton pour accéder à la gestion des ventes
		JButton sales = new JButton("Ventes");
		sales.setBackground(new Color(255, 183, 77));
		sidebar.add(sales);
		sidebar.add(Box.createVerticalStrut(15));

		// Bouton pour accéder à la gestion des contrats
		JButton contracts = new JButton("Contrats");
		contracts.setBackground(new Color(255, 183, 77));
		sidebar.add(contracts);
		sidebar.add(Box.createVerticalStrut(15));

		// Bouton pour accéder à la gestion des commandes
		JButton orders = new JButton("Commandes");
		orders.setBackground(new Color(255, 183, 77));
		sidebar.add(orders);
		sidebar.add(Box.createVerticalStrut(15));

		// Bouton pour accéder à la gestion des achats
		JButton purchases = new JButton("Achats");
		purchases.setBackground(new Color(255, 183, 77));
		sidebar.add(purchases);
		sidebar.add(Box.createVerticalStrut(15));

		// Bouton pour accéder aux paramètres de l'application
		JButton parameters = new JButton("Paramètres");
		parameters.setBackground(new Color(255, 183, 77));
		sidebar.add(parameters);
		sidebar.add(Box.createVerticalStrut(15));

		// Bouton pour se déconnecter
		JButton logout = new JButton("Déconnexion");
		logout.setBackground(new Color(102, 187, 106)); // Vert foncé
		sidebar.add(logout);
		sidebar.add(Box.createVerticalStrut(15));

		logout.addActionListener(e -> {
			frame.dispose();
			LoginApp.main(new String[] {}); // Revenir à la page de connexion
		});

		// Contenu à l'ouverture de l'application
		JPanel contentPanel = new JPanel(new GridBagLayout());
		GridBagConstraints cst = new GridBagConstraints();

		cst.insets = new Insets(10, 10, 10, 10);

		JLabel titleLabel = new JLabel("Bienvenue sur le logiciel de gestion", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
		titleLabel.setForeground(new Color(51, 51, 51));
		cst.gridy = 0;
		cst.gridx = 0;
		contentPanel.add(titleLabel);

		// Onglet des fournisseurs
		suppliers.addActionListener(e -> {

			contentPanel.removeAll(); // Rénitialise le contenu du contentPanel = page vide

			// Création du titre de l'onglet
			JLabel titleLabelSuppliers = new JLabel("Onglet des fournisseurs", SwingConstants.CENTER);
			titleLabelSuppliers.setFont(new Font("Arial", Font.BOLD, 20));
			titleLabelSuppliers.setForeground(new Color(51, 51, 51));
			GridBagConstraints cstSuppliers = new GridBagConstraints();
			cstSuppliers.gridy = 0;
			cstSuppliers.gridx = 0;
			cstSuppliers.insets = new Insets(10, 10, 10, 10);
			contentPanel.add(titleLabelSuppliers, cstSuppliers);

			// Afficher les fournisseurs dans un tableau
			String[] columnNames = { "SIRET", "Nom", "Adresse", "Téléphone", "Email" }; // Colonnes du tableau
			Vector<IData> data = new Vector<>();
			// Test du code en ajoutant manuellement un fournisseur dans le vecteur data
			data.add(new Fournisseur("14523652895412", "the market", "64 avenue de l'avenue", "0505050505",
					"themarket@market.fr"));

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

			// Bouton pour insérer un fournisseur
			JButton insertSupplierButton = new JButton("Ajouter un fournisseur");
			cstSuppliers.gridy = 2;
			contentPanel.add(insertSupplierButton, cstSuppliers);

			insertSupplierButton.addActionListener(insertSupplierEvent -> {

				// Formulaire pour saisir les informations du fournisseur
				JTextField siretField = new JTextField(15);
				JTextField nomField = new JTextField(15);
				JTextField adresseField = new JTextField(15);
				JTextField numeroTelField = new JTextField(15);
				JTextField emailField = new JTextField(15);

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
						Gestion.insert(fournisseur, "fournisseurs");
						JOptionPane.showMessageDialog(frame, "Fournisseur ajouté avec succès !");
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(frame,
								"Erreur lors de l'ajout du fournisseur : " + ex.getMessage());
					}
				}
			});

			// Bouton pour modifier un fournisseur
			// !!! EN ATTENTE DE LA FONCTION UPDATE DANS LA CLASSE GESTION !!!
			JButton editSupplierButton = new JButton("Modifier un fournisseur");
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

				Vector<String> row = tableData.get(selectedRow);
				JTextField siretField = new JTextField(row.get(0));
				JTextField nomField = new JTextField(row.get(1));
				JTextField adresseField = new JTextField(row.get(2));
				JTextField numeroTelField = new JTextField(row.get(3));
				JTextField emailField = new JTextField(row.get(4));

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
						// !!! EN ATTENTE DE LA FONCTION UPDATE DANS LA CLASSE GESTION !!!
						// Gestion.update(fournisseur, "fournisseurs");
						JOptionPane.showMessageDialog(frame, "Fournisseur modifié avec succès !");
						suppliers.doClick();
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(frame, "Erreur : " + ex.getMessage(), "Erreur",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			});

			// Bouton pour supprimer un fournisseur
			// !!! EN ATTENTE DE LA FONCTION DELETE DANS LA CLASSE GESTION !!!
			JButton deleteSupplierButton = new JButton("Supprimer un fournisseur");
			cstSuppliers.gridy = 4;
			contentPanel.add(deleteSupplierButton, cstSuppliers);

			deleteSupplierButton.addActionListener(deleteSupplierEvent -> {
				int selectedRow = suppliersTable.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un fournisseur à supprimer.", "Erreur",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				String siret = tableData.get(selectedRow).get(0); // Récupérer le SIRET -> identifie de manière unique
																	// un fournisseur
				int confirm = JOptionPane.showConfirmDialog(frame,
						"Êtes-vous sûr de vouloir supprimer ce fournisseur ?", "Confirmation",
						JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					try {
						// !! EN ATTENTE DE LA FONCTION DELETE DANS LA CLASSE GESTION !!!
						// Gestion.delete(siret, "fournisseurs");
						JOptionPane.showMessageDialog(frame, "Fournisseur supprimé avec succès !");
						suppliers.doClick(); // Rafraîchir l'onglet
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(frame, "Erreur : " + ex.getMessage(), "Erreur",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			});

			// Bouton pour ajouter un contact
			JButton insertContactButton = new JButton("Ajouter un contact");
			cstSuppliers.gridy = 5;
			contentPanel.add(insertContactButton, cstSuppliers);

			insertContactButton.addActionListener(insertContactEvent -> {
				int selectedRow = suppliersTable.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(frame,
							"Veuillez sélectionner un fournisseur pour ajouter un contact.", "Erreur",
							JOptionPane.ERROR_MESSAGE);
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

				int result = JOptionPane.showConfirmDialog(frame, form, "Ajouter un contact",
						JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					try {
						Contact contact = new Contact(nomField.getText(), prenomField.getText(),
								numeroTelField.getText(), emailField.getText());
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

			// Bouton pour afficher les contacts
			JButton viewContactsButton = new JButton("Afficher les contacts");
			cstSuppliers.gridy = 6;
			contentPanel.add(viewContactsButton, cstSuppliers);

			viewContactsButton.addActionListener(viewContactsEvent -> {
				int selectedRow = suppliersTable.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(frame,
							"Veuillez sélectionner un fournisseur pour afficher ses contacts.", "Erreur",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				Vector<Contact> contacts = new Vector<>();
				// Test de vérification des contacts manuellement
				String query = "SELECT * FROM contact";
				try(Statement statement = Connexion.getConnexion().createStatement()){
		            ResultSet rs = statement.executeQuery(query);
		            while(rs.next()) {
		            	Contact c = new Contact(rs);
		            	contacts.add(c);
		            }
				}catch (SQLException exp) {
		            System.err.println("Erreur creation statement:" + exp.getMessage());
				}


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

			contentPanel.revalidate();
			contentPanel.repaint();
		});

		products.addActionListener(e -> {

			contentPanel.removeAll();

			JLabel titleLabelProducts = new JLabel("Onglet des produits", SwingConstants.CENTER);
			titleLabelProducts.setFont(new Font("Arial", Font.BOLD, 20));
			GridBagConstraints cstProducts = new GridBagConstraints();
			cst.gridy = 0;
			cst.gridx = 0;
			cst.insets = new Insets(10, 10, 10, 10);
			contentPanel.add(titleLabelProducts, cstProducts);

			contentPanel.revalidate();
			contentPanel.repaint();

		});

		sales.addActionListener(e -> {

			contentPanel.removeAll();

			JLabel titleLabelSales = new JLabel("Onglet des ventes", SwingConstants.CENTER);
			titleLabelSales.setFont(new Font("Arial", Font.BOLD, 20));
			GridBagConstraints cstSales = new GridBagConstraints();
			cst.gridy = 0;
			cst.gridx = 0;
			cst.insets = new Insets(10, 10, 10, 10);
			contentPanel.add(titleLabelSales, cstSales);

			contentPanel.revalidate();
			contentPanel.repaint();
		});

		contracts.addActionListener(e -> {

			contentPanel.removeAll();

			JLabel titleLabelContracts = new JLabel("Onglet des contrats", SwingConstants.CENTER);
			titleLabelContracts.setFont(new Font("Arial", Font.BOLD, 20));
			GridBagConstraints cstContracts = new GridBagConstraints();
			cstContracts.gridy = 0;
			cstContracts.gridx = 0;
			cstContracts.insets = new Insets(10, 10, 10, 10);
			contentPanel.add(titleLabelContracts, cstContracts);

			List<IData> contractsList = new ArrayList<>(); // Les contrats seront stockés dans une List

			// Test de simulation
			String query = "SELECT * FROM contrat";
			try(Statement statement = Connexion.getConnexion().createStatement()){
	            ResultSet rs = statement.executeQuery(query);
	            while(rs.next()) {
	            	Contrat c = new Contrat(rs);
	            	contractsList.add(c);
	            }
			}catch (SQLException exp) {
	            System.err.println("Erreur creation statement:" + exp.getMessage());
			}

			// Bouton pour afficher les contrats

			JButton viewContractsButton = new JButton("Afficher les contrats");
			cstContracts.gridy = 1;
			contentPanel.add(viewContractsButton, cstContracts);

			viewContractsButton.addActionListener(viewContractsEvent -> {
				StringBuilder contratList = new StringBuilder("Liste des contrats :\n\n");

				for (IData data : contractsList) {
					Contrat contrat = (Contrat) data;
					contratList.append("ID : ").append(contrat.getIdContrat()).append(" | Fournisseur SIRET : ")
							.append(contrat.getSiret()).append(" | Produit ID : ").append(contrat.getIdProduit())
							.append(" | Prix Unitaire : ").append(contrat.getPrixUni()).append(" | Début : ")
							.append(contrat.getDateDebut()).append(" | Fin : ").append(contrat.getDateFin())
							.append("\n");
				}

				JOptionPane.showMessageDialog(frame, contratList.toString(), "Contrats",
						JOptionPane.INFORMATION_MESSAGE);
			});

			// Bouton pour ajouter un contrat

			JButton addContractButton = new JButton("Ajouter un contrat");
			cstContracts.gridy = 1;
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
								Double.parseDouble(prixField.getText()), dateDebutField.getText(),
								dateFinField.getText());
						contractsList.add(newContrat);
						Gestion.insert(newContrat, "contrat");
						JOptionPane.showMessageDialog(frame, "Contrat ajouté avec succès.");
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(frame, "Erreur dans les données saisies : " + ex.getMessage(),
								"Erreur", JOptionPane.ERROR_MESSAGE);
					}
				}
			});

			// Modifier un contrat
			JButton editContractButton = new JButton("Modifier un contrat");
			cstContracts.gridy = 3;
			contentPanel.add(editContractButton, cstContracts);

			editContractButton.addActionListener(editContractEvent -> {
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
							contratToEdit.setSiret(siretField.getText());
							contratToEdit.setIdProduit(Integer.parseInt(produitField.getText()));
							contratToEdit.setPrixUni(Double.parseDouble(prixField.getText()));
							contratToEdit.setDateDebut(dateDebutField.getText());
							contratToEdit.setDateFin(dateFinField.getText());
							// Gestion.update(contratToEdit, "contrat");
							JOptionPane.showMessageDialog(frame, "Contrat modifié avec succès.");
						}
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(frame, "Erreur lors de la modification : " + ex.getMessage(),
								"Erreur", JOptionPane.ERROR_MESSAGE);
					}
				}
			});

			// Bouton pour supprimer un contrat

			JButton deleteContractButton = new JButton("Supprimer un contrat");
			cstContracts.gridy = 2;
			contentPanel.add(deleteContractButton, cstContracts);

			deleteContractButton.addActionListener(deleteContractEvent -> {
				String idToDelete = JOptionPane.showInputDialog(frame, "Entrez l'ID du contrat à supprimer :");
				if (idToDelete != null && !idToDelete.isEmpty()) {
					try {
						int id = Integer.parseInt(idToDelete);
						contractsList.removeIf(contrat -> ((Contrat) contrat).getIdContrat() == id);
						// Gestion.delete(idToDelete, "contrat");
						JOptionPane.showMessageDialog(frame, "Contrat supprimé avec succès.");
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(frame, "Erreur lors de la suppression : " + ex.getMessage(),
								"Erreur", JOptionPane.ERROR_MESSAGE);
					}
				}
			});

			contentPanel.revalidate();
			contentPanel.repaint();
		});

		// Onglet des commandes
		orders.addActionListener(e -> {

			contentPanel.removeAll();

			// Titre de l'onglet
			JLabel titleLabelOrders = new JLabel("Onglet des commandes", SwingConstants.CENTER);
			titleLabelOrders.setFont(new Font("Arial", Font.BOLD, 20));
			GridBagConstraints cstOrders = new GridBagConstraints();
			cst.gridy = 0;
			cst.gridx = 0;
			cst.insets = new Insets(10, 10, 10, 10);
			contentPanel.add(titleLabelOrders, cstOrders);

			List<IData> ordersList = new ArrayList<>();
			ordersList.add(new Commande(10, "14523652895412", 5.0)); // Test d'une fausse commande

			// Affichage d'un tableau des commandes
			String[] columnNamesOrders = { "Produit", "Fournisseur", "Quantité" };
			DefaultTableModel tableModelOrders = new DefaultTableModel(columnNamesOrders, 0);

			for (IData item : ordersList) {
				if (item instanceof Commande commande) {
					tableModelOrders.addRow(
							new Object[] { commande.getIdProduit(), commande.getSiret(), commande.getQuantite() });
				}
			}

			JTable ordersTable = new JTable(tableModelOrders);
			JScrollPane scrollPaneOrders = new JScrollPane(ordersTable);
			cstOrders.gridy = 1;
			contentPanel.add(scrollPaneOrders, cstOrders);

			// Bouton pour passer une commande
			JButton addOrderButton = new JButton("Passer une commande");
			cstOrders.gridy = 2;
			contentPanel.add(addOrderButton, cstOrders);

			addOrderButton.addActionListener(addOrderEvent -> {
				JTextField produitField = new JTextField(15);
				JTextField fournisseurField = new JTextField(15);
				JTextField quantiteField = new JTextField(15);

				JPanel orderForm = new JPanel(new GridLayout(3, 2));
				orderForm.add(new JLabel("ID Produit :"));
				orderForm.add(produitField);
				orderForm.add(new JLabel("SIRET Fournisseur :"));
				orderForm.add(fournisseurField);
				orderForm.add(new JLabel("Quantité :"));
				orderForm.add(quantiteField);

				int result = JOptionPane.showConfirmDialog(frame, orderForm, "Passer une commande",
						JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					try {
						Commande newCommande = new Commande(Integer.parseInt(produitField.getText()),
								fournisseurField.getText(), Double.parseDouble(quantiteField.getText()));

						ordersList.add(newCommande);

						tableModelOrders.addRow(new Object[] { newCommande.getIdProduit(), newCommande.getSiret(),
								newCommande.getQuantite() });

						Gestion.insert(newCommande, "commande");

						JOptionPane.showMessageDialog(frame, "Commande ajoutée avec succès !");
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(frame,
								"Erreur lors de l'ajout de la commande : " + ex.getMessage(), "Erreur",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			});

			// Bouton pour valider une commande
			JButton validateOrderButton = new JButton("Valider une commande");
			cstOrders.gridy = 3;
			contentPanel.add(validateOrderButton, cstOrders);

			validateOrderButton.addActionListener(validateOrderEvent -> {
				int selectedRow = ordersTable.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(frame, "Veuillez sélectionner une commande à valider.", "Erreur",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				try {
					Commande selectedCommande = (Commande) ordersList.get(selectedRow);

					// Test de création d'un objet achat manuellement
					Achat achat = new Achat(selectedCommande.getIdProduit(), 20.0, new Date(System.currentTimeMillis()) // Date
																														// actuelle
					);

					Gestion.insert(achat, "achat");

					ordersList.remove(selectedCommande);
					tableModelOrders.removeRow(selectedRow);

					JOptionPane.showMessageDialog(frame, "Commande validée et transformée en achat !");
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame,
							"Erreur lors de la validation de la commande : " + ex.getMessage(), "Erreur",
							JOptionPane.ERROR_MESSAGE);
				}
			});

			contentPanel.revalidate();
			contentPanel.repaint();
		});

		purchases.addActionListener(e -> {

			contentPanel.removeAll();

			JLabel titleLabelPurchases = new JLabel("Onglet des achats", SwingConstants.CENTER);
			titleLabelPurchases.setFont(new Font("Arial", Font.BOLD, 20));
			GridBagConstraints cstPurchases = new GridBagConstraints();
			cst.gridy = 0;
			cst.gridx = 0;
			cst.insets = new Insets(10, 10, 10, 10);
			contentPanel.add(titleLabelPurchases, cstPurchases);

			contentPanel.revalidate();
			contentPanel.repaint();
		});

		// Quand on clique sur l'onglet des paramètres

		parameters.addActionListener(e -> {

			contentPanel.removeAll(); // Supprime tous les composants = page vide

			JLabel titleLabelParameters = new JLabel("Onglet des paramètres", SwingConstants.CENTER);
			titleLabelParameters.setFont(new Font("Arial", Font.BOLD, 20));
			GridBagConstraints cstParameters = new GridBagConstraints();
			cst.gridy = 0;
			cst.gridx = 0;
			cst.insets = new Insets(10, 10, 10, 10);
			contentPanel.add(titleLabelParameters, cstParameters);

			// Ajouter un utilisateur
			JButton addUserButton = new JButton("Ajouter un utilisateur");
			cst.gridy = 1;
			contentPanel.add(addUserButton, cst);

			addUserButton.addActionListener(addUserEvent -> {

				String newUserUsername = JOptionPane.showInputDialog("Nom d'utilisateur"); // Ouverture d'un formulaire
																							// pour entrer le nom
																							// d'utilisateur

				// Vérification d'un nom d'utilisateur possible
				if ((newUserUsername == null) || (newUserUsername.isEmpty())) {
					JOptionPane.showMessageDialog(frame, "Le nom d'utilisateur ne peut pas être vide.", "Erreur",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (UserManager.isUsernameAlreadyTaken(newUserUsername)) {
					JOptionPane.showMessageDialog(frame, "Le nom d'utilisateur est déjà utilisé.", "Erreur",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				String newUserPassword = JOptionPane.showInputDialog("Mot de passe"); // Ouverture d'un formulaire pour
																						// entrer le mot de passe
				if ((newUserPassword == null) || (newUserPassword.isEmpty())) {
					JOptionPane.showMessageDialog(frame, "Le mot de passe ne peut pas être vide.", "Erreur",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				boolean success = UserManager.addUser(newUserUsername, newUserPassword); // Création de l'utilisateur
																							// avec les champs donnés

				// Vérification du résultat de l'ajout
				if (success)
					JOptionPane.showMessageDialog(null, "Utilisateur ajouté avec succès !");
				else
					JOptionPane.showMessageDialog(null, "Échec de l'ajout de l'utilisateur.");
			});

			// Modifier le mot de passe
			JButton updateUserButton = new JButton("Modifier le mot de passe");
			cst.gridy = 2;
			contentPanel.add(updateUserButton, cst);

			updateUserButton.addActionListener(updateUserEvent -> {
				String usernameToUpdate = JOptionPane.showInputDialog("Nom d'utilisateur");
				String newPassword = JOptionPane.showInputDialog("Nouveau mot de passe");

				boolean success = UserManager.updatePassword(usernameToUpdate, newPassword); // Modification du mot de
																								// passe

				if (success)
					JOptionPane.showMessageDialog(null, "Mot de passe mis à jour avec succès !");
				else
					JOptionPane.showMessageDialog(null, "Échec de la mise à jour du mot de passe.");
			});

			// Afficher les utilisateurs
			JButton viewUsersButton = new JButton("Afficher les utilisateurs");
			cst.gridy = 3;
			contentPanel.add(viewUsersButton, cst);

			viewUsersButton.addActionListener(viewUsersEvent -> {
				List<String[]> users = UserManager.getAllUsers();

				StringBuilder userList = new StringBuilder("Liste des utilisateurs :\n\n");

				for (String[] user : users)
					userList.append("Nom : ").append(user[0]).append(" | Mot de passe : ").append(user[1]).append("\n");

				JOptionPane.showMessageDialog(frame, userList.toString(), "Utilisateurs",
						JOptionPane.INFORMATION_MESSAGE);
			});

			// Supprimer un utilisateur
			JButton deleteUserButton = new JButton("Supprimer un utilisateur");
			cst.gridy = 4;
			contentPanel.add(deleteUserButton, cst);

			deleteUserButton.addActionListener(deleteUserEvent -> {
				String usernameToDelete = JOptionPane.showInputDialog("Nom d'utilisateur à supprimer :");

				if (usernameToDelete != null && !usernameToDelete.trim().isEmpty()) {
					boolean success = UserManager.deleteUser(usernameToDelete);

					if (success)
						JOptionPane.showMessageDialog(frame, "Utilisateur supprimé avec succès.", "Succès",
								JOptionPane.INFORMATION_MESSAGE);
					else
						JOptionPane.showMessageDialog(frame, "Échec de la suppression de l'utilisateur.", "Erreur",
								JOptionPane.ERROR_MESSAGE);

				} else
					JOptionPane.showMessageDialog(frame, "Veuillez entrer un nom d'utilisateur valide.", "Erreur",
							JOptionPane.ERROR_MESSAGE);
			});

			contentPanel.revalidate();
			contentPanel.repaint();
		});

		// Ajouter la sidebar et le contenu principal à la frame
		frame.add(sidebar, BorderLayout.WEST);
		frame.add(contentPanel, BorderLayout.CENTER);

		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximiser la fenêtre pour qu'elle occupe tout l'écran
		frame.setVisible(true); // Rendre la fenêtre visible

	};

};