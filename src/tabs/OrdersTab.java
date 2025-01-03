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
import data.dependance_multi.ContactFournisseur;
import data.entity.Achat;
import data.entity.Commande;
import data.entity.Contact;
import data.entity.Contrat;
import data.entity.Fournisseur;
import data.entity.LotProduit;

public class OrdersTab {

	public static void loadOrdersTab(JPanel contentPanel, JFrame frame) {

		// ==============================================================

		// Création des couleurs

		Color grisDoux = new Color(51, 51, 51);
		Color jauneDoux = new Color(255, 183, 77);

		// ==============================================================

		// Titre de l'onglet commande
		
		JLabel titleLabelOrders = new JLabel("Onglet des commandes", SwingConstants.CENTER);
		titleLabelOrders.setFont(new Font("Arial", Font.BOLD, 20));
		GridBagConstraints cstOrders = new GridBagConstraints();
		cstOrders.gridy = 0;
		cstOrders.gridx = 0;
		cstOrders.insets = new Insets(10, 10, 10, 10);
		contentPanel.add(titleLabelOrders, cstOrders);
		
		// Titre de l'onglet achat 
		JLabel titleLabelPurchase = new JLabel("Onglet des achats", SwingConstants.CENTER);
		titleLabelPurchase.setFont(new Font("Arial", Font.BOLD, 20));
		GridBagConstraints cstPurchase = new GridBagConstraints();
		cstPurchase.gridy = 0;
		cstPurchase.gridx = 50;
		cstPurchase.insets = new Insets(10, 10, 10, 10);
		contentPanel.add(titleLabelPurchase, cstPurchase);

		// ==============================================================

		// Création de la List dans laquelle seront stockées les commandes

		List<IData> ordersList = new ArrayList<>(); 
		// recup des info dans la base
		try {
			Statement statement = Connexion.getConnexion().createStatement();
			try(ResultSet rs = statement.executeQuery("SELECT * FROM commande")){
				while(rs.next()) {
					Commande c = new Commande(rs);
					ordersList.add(c);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ==============================================================

		// Affichage d'un tableau des commandes

		String[] columnNamesOrders = { "ID commande", "Produit", "Fournisseur", "Quantité" };
		DefaultTableModel tableModelOrders = new DefaultTableModel(columnNamesOrders, 0);

		for (IData item : ordersList) {
			if (item instanceof Commande commande) {
				tableModelOrders
						.addRow(new Object[] { commande.getIdCommande() , commande.getIdProduit(), commande.getSiret(), commande.getQuantite() });
			}
		}

		JTable ordersTable = new JTable(tableModelOrders);
		JScrollPane scrollPaneOrders = new JScrollPane(ordersTable);
		cstOrders.gridy = 1;
		contentPanel.add(scrollPaneOrders, cstOrders);
		

		// ==============================================================
		// ==============================================================
		


		String[] columnNamesPurchase = { "ID achat" , "ID commande", "Prix achat Unitaire", "Date achat"};
		DefaultTableModel tableModelPurchase = new DefaultTableModel(columnNamesPurchase, 0);
		// Création de la List dans laquelle seront stockées les achats

		List<IData> purchasesList = new ArrayList<>(); 
		// recup des info dans la base
		try {
			Statement statement = Connexion.getConnexion().createStatement();
			try(ResultSet rs = statement.executeQuery("SELECT * FROM achat")){
				while(rs.next()) {
					Achat a = new Achat(rs);
					purchasesList.add(a);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ==============================================================

		// Affichage d'un tableau des achats

		for (IData item : purchasesList) {
			if (item instanceof Achat achat) {
				tableModelPurchase
						.addRow(new Object[] { achat.getIdAchat(), achat.getIdCommande(), achat.getPrixAchatUni(), achat.getDateAchat() });
			}
		}

		JTable purchaseTable = new JTable(tableModelPurchase);
		JScrollPane scrollPanePurchase = new JScrollPane(purchaseTable);
		cstPurchase.gridy = 1;
		contentPanel.add(scrollPanePurchase, cstPurchase);
		

		// ==============================================================
		// ==============================================================

		// Bouton pour passer une commande

		// Création du bouton
		JButton addOrderButton = new JButton("Passer une commande");
		addOrderButton.setForeground(grisDoux);
		addOrderButton.setBackground(jauneDoux);
		cstOrders.gridy = 2;
		contentPanel.add(addOrderButton, cstOrders);

		// Action au clique sur ce bouton
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
					JOptionPane.showMessageDialog(frame, "Erreur lors de l'ajout de la commande : " + ex.getMessage(),
							"Erreur", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// ==============================================================

		// Bouton pour valider une commande
		
		JButton validateOrderButton = new JButton("Valider une commande");
		validateOrderButton.setForeground(grisDoux);
		validateOrderButton.setBackground(jauneDoux);
		cstOrders.gridy = 3;
		contentPanel.add(validateOrderButton, cstOrders);

		// Action au clique sur ce bouton
		validateOrderButton.addActionListener(validateOrderEvent -> {
			int selectedRow = ordersTable.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(frame, "Veuillez sélectionner une commande à valider.", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				Commande selectedCommande = (Commande) ordersList.get(selectedRow);
				
				// on vérifie si il y a un contrat pour valider cette commande , si oui on utrilise le plus avantageux
				// récup du contrat
				String query = "SELECT * FROM contrat WHERE id_produit = ? AND siret = ? AND CURRENT_DATE BETWEEN date_debut AND date_fin ORDER BY prix_uni LIMIT 1";

				double achatPrice = -1;
				
				try(PreparedStatement statement = Connexion.getConnexion().prepareStatement(query)){
					statement.setInt(1, selectedCommande.getIdProduit());
					statement.setString(2, selectedCommande.getSiret());
					ResultSet rs = statement.executeQuery();
					if(rs.next()) {
						achatPrice = rs.getDouble("prix_uni");
					}
				}
				
				//si il n'y a pas de contrat a utilisé pour l'achat
				if(achatPrice == -1) {
					int confirm = JOptionPane.showConfirmDialog(frame, "Il n'y a pas de contrat avec ce fournisseur pour déterminer un prix d'achat. Faut-il le créer ?",
							"Confirmation", JOptionPane.YES_NO_OPTION);
					if (confirm == JOptionPane.YES_OPTION) {
						JTextField prixField = new JTextField(15);

						JPanel form = new JPanel(new GridLayout(4, 2));
						form.add(new JLabel("Prix d'achat : "));
						form.add(prixField);

						int result = JOptionPane.showConfirmDialog(frame, form, "Définir le prix", JOptionPane.OK_CANCEL_OPTION);
						if (result == JOptionPane.OK_OPTION) {
							try {
								//contart de 24h
								Contrat c = new Contrat(selectedCommande.getSiret(), 
										selectedCommande.getIdProduit(), 
										Double.parseDouble(prixField.getText()),
										System.currentTimeMillis(),
										(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
								
								//vérif si le fournisseur a le produit
								query = "SELECT * FROM produit_fournisseur WHERE id_produit = ? AND siret = ?";
								boolean canMakeContrat = false;
								
								try(PreparedStatement statement = Connexion.getConnexion().prepareStatement(query)){
									statement.setInt(1, c.getIdProduit());
									statement.setString(2, c.getSiret());
									ResultSet rs = statement.executeQuery();
									if(rs.next()) {
										canMakeContrat = true;
									}
								}
								
								if(canMakeContrat) {
									Gestion.insert(c, "contrat");
									achatPrice = Double.parseDouble(prixField.getText());
									JOptionPane.showMessageDialog(frame, "Contrat créé avec succès !");
								}else {
									JOptionPane.showMessageDialog(frame, "Impossible de créer le contrat, le fournisseur n'a pas le produit voulue !");
								}
							} catch (Exception ex) {
								JOptionPane.showMessageDialog(frame, "Erreur lors de la création du contrat : " + ex.getMessage(),
										"Erreur", JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				}

				if(achatPrice > 0) {
					int confirm = JOptionPane.showConfirmDialog(frame, "Êtes-vous sûr de valider une commande en achat, ceci est irréversible ?",
							"Confirmation", JOptionPane.YES_NO_OPTION);
					if (confirm == JOptionPane.YES_OPTION) {
						Achat achat = new Achat(selectedCommande.getIdCommande(), achatPrice, new Date(System.currentTimeMillis()));
						
						int achatId = Gestion.insert(achat, "achat"); // Ajout de la commande transformée en achat dans la table achat
						
						//commande acheté donc nouveau lot de produit avec comme prix de vente unitaire par défault le prix d'achat + 25%
						LotProduit lp = new LotProduit(selectedCommande.getIdProduit(), 
								(achatPrice+(0.25*achatPrice)), 
								selectedCommande.getQuantite(), 
								new Date(System.currentTimeMillis()+ 365 * 24 * 60 * 60 * 1000), // péremption après 1 ans
								achatId);
						
						Gestion.insert(lp, "lot_produit"); // Ajout du lot de produit dans les stocks
		
						// Supprimer de la List
		
						//ordersList.remove(selectedCommande);
						//tableModelOrders.removeRow(selectedRow);
						
						//Gestion.delete(selectedCommande, "commande"); // Supprimer la commande de la table commande
		
						JOptionPane.showMessageDialog(frame, "Commande validée et achat créé !");
					}
				}else {
					JOptionPane.showMessageDialog(frame, "Impossible de valider la commande car le contrat avec le fournisseur est fixé a 0 €, supprimez ce contrat pour continer !");
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(frame, "Erreur lors de la validation de la commande : " + ex.getMessage(),
						"Erreur", JOptionPane.ERROR_MESSAGE);
			}
		});

		// ==============================================================

		contentPanel.revalidate();
		contentPanel.repaint();

	}

}
