package swing_gui;

import javax.swing.*;

import data.Connexion;
import data.Gestion;
import data.entity.Fournisseur;
import data.entity.Produit;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

public class ManagementSoftware {

	public static void loadMainApplication(String username) {

		JFrame frame = new JFrame("AMS - Logiciel de gestion");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// ==============================================================

		// Barre latérale

		JPanel sidebar = new JPanel(new BorderLayout());
		sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
		sidebar.setBackground(Color.LIGHT_GRAY);
		sidebar.setPreferredSize(new Dimension(300, 0));
		sidebar.add(Box.createVerticalStrut(300));

		// ==============================================================

		// Affichage de l'utilisateur connecté

		JLabel userLabel = new JLabel("Utilisateur : " + username);
		userLabel.setFont(new Font("Arial", Font.BOLD, 20));
		sidebar.add(userLabel);
		sidebar.add(Box.createVerticalStrut(10));

		// ==============================================================

		// Affichage du rôle de l'utilisateur connecté

		JLabel roleLabel = new JLabel("Rôle : " + UserManager.getUserRole(username));
		roleLabel.setFont(new Font("Arial", Font.BOLD, 20));
		sidebar.add(roleLabel);
		sidebar.add(Box.createVerticalStrut(50));

		// ==============================================================

		// Bouton pour accéder à la gestion des fournisseurs

		JButton suppliers = new JButton("Fournisseurs");
		sidebar.add(suppliers);
		sidebar.add(Box.createVerticalStrut(15));

		// ==============================================================

		// Bouton pour accéder à la gestion des contacts

		JButton contacts = new JButton("Contacts");
		sidebar.add(contacts);
		sidebar.add(Box.createVerticalStrut(15));

		// ==============================================================

		// Bouton pour accéder à la gestion des produits

		JButton products = new JButton("Produits");
		sidebar.add(products);
		sidebar.add(Box.createVerticalStrut(15));

		// ==============================================================

		// Bouton pour accéder à la gestion des ventes

		JButton sales = new JButton("Ventes");
		sidebar.add(sales);
		sidebar.add(Box.createVerticalStrut(15));

		// ==============================================================

		// Bouton pour accéder à la gestion des contrats

		JButton contracts = new JButton("Contrats");
		sidebar.add(contracts);
		sidebar.add(Box.createVerticalStrut(15));

		// ==============================================================

		// Bouton pour accéder à la gestion des commandes

		JButton orders = new JButton("Commandes");
		sidebar.add(orders);
		sidebar.add(Box.createVerticalStrut(15));

		// ==============================================================

		// Bouton pour accéder à la gestion des achats

		JButton purchases = new JButton("Achats");
		sidebar.add(purchases);
		sidebar.add(Box.createVerticalStrut(15));

		// ==============================================================

		// Bouton pour accéder aux paramètres de l'application

		JButton parameters = new JButton("Paramètres");
		sidebar.add(parameters);
		sidebar.add(Box.createVerticalStrut(15));

		// ==============================================================

		// Contenu à l'ouverture de l'application

		JPanel contentPanel = new JPanel(new GridBagLayout());
		GridBagConstraints cst = new GridBagConstraints();

		cst.insets = new Insets(10, 10, 10, 10);

		JLabel titleLabel = new JLabel("Bienvenue sur le logiciel de gestion", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
		cst.gridy = 0;
		cst.gridx = 0;
		contentPanel.add(titleLabel);

		// AFFICHER LES STATISTIQUES DU MAGASIN À L'OUVERTURE DU LOGICIEL DE GESTION

		// ==============================================================

		// Affichage du contenu lié aux fournisseurs

		suppliers.addActionListener(e -> {

			contentPanel.removeAll(); // Page vide

			// Création du titre pour indiquer qu'on est sur l'onglet des fournisseurs
			JLabel titleLabelSuppliers = new JLabel("Onglet des fournisseurs", SwingConstants.CENTER);
			titleLabelSuppliers.setFont(new Font("Arial", Font.BOLD, 20));
			GridBagConstraints cstSuppliers = new GridBagConstraints();
			cst.gridy = 0;
			cst.gridx = 0;
			cst.insets = new Insets(10, 10, 10, 10);
			contentPanel.add(titleLabelSuppliers, cstSuppliers);

			// ==============================================================

			// Afficher les fournisseurs dans un tableau

			String[] columnNames = { "SIRET", "Nom", "Adresse", "Téléphone", "Email" }; // Colonnes du tableau des //
																
			

			// Ton interface graphique doit savoir utiliser des interface IData
			// Pas besoin de coder la partie requête vers la base
			// 
			
			// Ton code doit savoir gérer des listes ou des vector etc.... avec des interfaces IData dedans
			Vector<Fournisseur> data = new Vector<>(); // Données sur les fournisseurs
			// Et moi je vien rajouter la partie requête qui va chercher les infos de la base
			// en attendant créer quelque fausse interface IData pour tester ton code
			
			// !!! c'est moi qui dois faire cette portion de code !!!
			try {

				// Requête SQL pour récupérer les fournisseurs
				String query = "SELECT * FROM fournisseur";
				ResultSet rs = Connexion.getConnexion().createStatement().executeQuery(query);

				// Ajout des fournisseurs dans data
				while (rs.next()) {
					Fournisseur f = new Fournisseur(rs); 
					data.add(f);
				}

			} catch (SQLException eData) {
				JOptionPane.showMessageDialog(frame,
						"Erreur lors du chargement des fournisseurs : " + eData.getMessage());
			}
			// !!! !!!
			

			// je ne sais pas comment swing fonctionne donc il faut que tu t'arrange pour afficher des interfaces IData
			JTable suppliersTable = new JTable(data, columnNames); // !> marche pas
			JScrollPane scrollPane = new JScrollPane(suppliersTable);
			cst.gridy = 1; // En dessous du titre
			contentPanel.add(scrollPane, cst);

			// ==============================================================

			// Bouton pour ajouter un fournisseur

			JButton addSupplierButton = new JButton("Ajouter un fournisseur");
			cst.gridy = 2; // En dessous du tableau
			contentPanel.add(addSupplierButton, cst);

			addSupplierButton.addActionListener(addEvent -> {

				// Formulaire pour saisir les informations du fournisseur
				JTextField siretField = new JTextField(15);
				JTextField nomField = new JTextField(15);
				JTextField adresseField = new JTextField(15);
				JTextField numeroTelField = new JTextField(15);
				JTextField emailField = new JTextField(15);

				JPanel form = new JPanel(new GridLayout(5, 2));
				form.add(new JLabel("SIRET :"));
				form.add(siretField);
				form.add(new JLabel("Nom :"));
				form.add(nomField);
				form.add(new JLabel("Adresse :"));
				form.add(adresseField);
				form.add(new JLabel("Téléphone :"));
				form.add(numeroTelField);
				form.add(new JLabel("Email :"));
				form.add(emailField);

				int result = JOptionPane.showConfirmDialog(frame, form, "Ajouter un fournisseur",
						JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					// Création de l'objet fournisseur avec les valeurs entrés par l'utilisateur
					Fournisseur fournisseur = new Fournisseur(siretField.getText(), nomField.getText(),
							adresseField.getText(), numeroTelField.getText(), emailField.getText());

					try {
						// Insertion du fournisseur dans la base
						Gestion.insert(fournisseur, "fournisseurs"); //>! utilise cette méthode pour insérer dans la base des interfaces IData
						//             IData 		 table 
						JOptionPane.showMessageDialog(frame, "Fournisseur ajouté avec succès !");
					} catch (SQLException ex) {
						JOptionPane.showMessageDialog(frame,
								"Erreur lors de l'ajout du fournisseur : " + ex.getMessage());
					}
				}
			});

			contentPanel.revalidate();
			contentPanel.repaint();

		});

		// ==============================================================

		contacts.addActionListener(e -> {

			contentPanel.removeAll();

			JLabel titleLabelContacts = new JLabel("Onglet des contacts", SwingConstants.CENTER);
			titleLabelContacts.setFont(new Font("Arial", Font.BOLD, 20));
			GridBagConstraints cstContacts = new GridBagConstraints();
			cst.gridy = 0;
			cst.gridx = 0;
			cst.insets = new Insets(10, 10, 10, 10);
			contentPanel.add(titleLabelContacts, cstContacts);

			contentPanel.revalidate();
			contentPanel.repaint();

		});

		// ==============================================================

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

		// ==============================================================

		sales.addActionListener(salesE -> {

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

		// ==============================================================

		contracts.addActionListener(contractsE -> {

			contentPanel.removeAll();

			JLabel titleLabelContracts = new JLabel("Onglet des contrats", SwingConstants.CENTER);
			titleLabelContracts.setFont(new Font("Arial", Font.BOLD, 20));
			GridBagConstraints cstContracts = new GridBagConstraints();
			cst.gridy = 0;
			cst.gridx = 0;
			cst.insets = new Insets(10, 10, 10, 10);
			contentPanel.add(titleLabelContracts, cstContracts);

			contentPanel.revalidate();
			contentPanel.repaint();
		});

		// ==============================================================

		orders.addActionListener(ordersE -> {

			contentPanel.removeAll();

			JLabel titleLabelOrders = new JLabel("Onglet des commandes", SwingConstants.CENTER);
			titleLabelOrders.setFont(new Font("Arial", Font.BOLD, 20));
			GridBagConstraints cstOrders = new GridBagConstraints();
			cst.gridy = 0;
			cst.gridx = 0;
			cst.insets = new Insets(10, 10, 10, 10);
			contentPanel.add(titleLabelOrders, cstOrders);

			contentPanel.revalidate();
			contentPanel.repaint();
		});

		// ==============================================================

		purchases.addActionListener(purchasesE -> {

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

		// ==============================================================

		// Quand on clique sur l'onglet des paramètres

		parameters.addActionListener(parametersE -> {

			contentPanel.removeAll(); // Supprime tous les composants = page vide

			JLabel titleLabelParameters = new JLabel("Onglet des paramètres", SwingConstants.CENTER);
			titleLabelParameters.setFont(new Font("Arial", Font.BOLD, 20));
			GridBagConstraints cstParameters = new GridBagConstraints();
			cst.gridy = 0;
			cst.gridx = 0;
			cst.insets = new Insets(10, 10, 10, 10);
			contentPanel.add(titleLabelParameters, cstParameters);

			// ==============================================================

			// Ajouter un utilisateur

			JButton addUserButton = new JButton("Ajouter un utilisateur");
			cst.gridy = 1;
			contentPanel.add(addUserButton, cst);

			addUserButton.addActionListener(addUserEvent -> {
				// Ouvrir un formulaire pour entrer les champs
				String newUserUsername = JOptionPane.showInputDialog("Nom d'utilisateur");
				String newUserPassword = JOptionPane.showInputDialog("Mot de passe");

				boolean success = UserManager.addUser(newUserUsername, newUserPassword); // Création de l'utilisateur
																							// avec les champs donnés

				// Vérification du résultat de l'ajout
				if (success)
					JOptionPane.showMessageDialog(null, "Utilisateur ajouté avec succès !");
				else
					JOptionPane.showMessageDialog(null, "Échec de l'ajout de l'utilisateur.");
			});

			// ==============================================================

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

			// ==============================================================

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

			// ==============================================================

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

			// ==============================================================

			contentPanel.revalidate();
			contentPanel.repaint();
		});

		// ==============================================================

		// Ajouter la sidebar et le contenu principal à la frame
		frame.add(sidebar, BorderLayout.WEST);
		frame.add(contentPanel, BorderLayout.CENTER);

		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximiser la fenêtre pour qu'elle occupe tout l'écran
		frame.setVisible(true); // Rendre la fenêtre visible

	};

};
