package tabs;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.sql.Date;
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
import data.entity.Achat;
import data.entity.Commande;
import data.entity.LotProduit;
import data.entity.Produit;

public class OrdersTab {

	public static void loadOrdersTab(JPanel contentPanel, JFrame frame) {

		// ==============================================================

		// Création des couleurs

		Color grisDoux = new Color(51, 51, 51);
		Color jauneDoux = new Color(255, 183, 77);

		// ==============================================================

		// Titre de l'onglet
		JLabel titleLabelOrders = new JLabel("Onglet des commandes", SwingConstants.CENTER);
		titleLabelOrders.setFont(new Font("Arial", Font.BOLD, 20));
		GridBagConstraints cstOrders = new GridBagConstraints();
		cstOrders.gridy = 0;
		cstOrders.gridx = 0;
		cstOrders.insets = new Insets(10, 10, 10, 10);
		contentPanel.add(titleLabelOrders, cstOrders);

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

		String[] columnNamesOrders = { "Produit", "Fournisseur", "Quantité" };
		DefaultTableModel tableModelOrders = new DefaultTableModel(columnNamesOrders, 0);

		for (IData item : ordersList) {
			if (item instanceof Commande commande) {
				tableModelOrders
						.addRow(new Object[] { commande.getIdProduit(), commande.getSiret(), commande.getQuantite() });
			}
		}

		JTable ordersTable = new JTable(tableModelOrders);
		JScrollPane scrollPaneOrders = new JScrollPane(ordersTable);
		cstOrders.gridy = 1;
		contentPanel.add(scrollPaneOrders, cstOrders);

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

				// Test de création d'un objet achat manuellement
				Achat achat = new Achat(selectedCommande.getIdProduit(), 20.0, new Date(System.currentTimeMillis()));
				
				Gestion.insert(achat, "achat"); // Ajout de la commande transformée en achat dans la table achat
				
				//commande acheté donc nouveau lot de produit 
				LotProduit lp = new LotProduit(selectedCommande.getIdProduit(), 
						20.0, 
						selectedCommande.getQuantite(), 
						new Date(System.currentTimeMillis()+99999999), 
						0);
				
				//on recup l'id de l'achat passé pour le mettre dans le lot de produit 

				
				Gestion.insert(lp, "lot_produit"); // Ajout du lot de produit dans les stocks

				// Supprimer de la List

				//ordersList.remove(selectedCommande);
				//tableModelOrders.removeRow(selectedRow);
				
				//Gestion.delete(selectedCommande, "commande"); // Supprimer la commande de la table commande

				JOptionPane.showMessageDialog(frame, "Commande validée et transformée en achat !");
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
