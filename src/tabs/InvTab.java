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
import data.entity.LotProduit;

public class InvTab {

	public static void loadInvTab(JPanel contentPanel, JFrame frame) {

		// ==============================================================

		// !!! SERA GÉRÉ PAR PALETTE.JAVA !!!

		// Création des couleurs

		Color grisDoux = new Color(51, 51, 51);
		Color jauneDoux = new Color(255, 183, 77);

		// ==============================================================

		// Création du titre pour indiquer le tableau de l'inventaire du magasin

		JLabel titleLabelInv = new JLabel("Inventaire du magasin", SwingConstants.CENTER);
		titleLabelInv.setFont(new Font("Arial", Font.BOLD, 20));
		GridBagConstraints cstInv = new GridBagConstraints();
		cstInv.gridy = 0;
		cstInv.gridx = 0;
		cstInv.insets = new Insets(10, 10, 10, 10);
		contentPanel.add(titleLabelInv, cstInv);

		// ==============================================================

		// Création du titre pour indiquer le panier du client = mode caisse

		JLabel titleCart = new JLabel("Panier du client", SwingConstants.CENTER);
		titleCart.setFont(new Font("Arial", Font.BOLD, 20));
		GridBagConstraints cstCart = new GridBagConstraints();
		cstCart.gridy = 0;
		cstCart.gridx = 1; // Le panier du client sera à droite du tableau de l'inventaire du magasin
		cstCart.insets = new Insets(10, 10, 10, 10);
		contentPanel.add(titleCart, cstCart);

		// ==============================================================

		// Création de la List dans laquelle seront stockées les LotProduits (exemple)

		List<LotProduit> InvList = new ArrayList<>();

		// !!! TEST SQL !!!
		Date testInventoryDate = new Date(0);
		LotProduit testInventory = new LotProduit(10, 5, 25, testInventoryDate, 1);
		InvList.add(testInventory);

		// Récupérations des informations dans la BDD
		try {
			Statement statement = Connexion.getConnexion().createStatement();
			try (ResultSet rs = statement.executeQuery("SELECT * FROM lot_produit")) {
				while (rs.next()) {
					LotProduit lp = new LotProduit(rs);
					InvList.add(lp);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ==============================================================

		// Affichage du tableau des LotProduits
		String[] columnNamesInv = { "ID lot produit", "ID produit(Nom)", "Prix de Vente unitaire", "Quantité restante",
				"Date péremption", "ID achat du lot" }; // Colonne du tableau

		DefaultTableModel tableModelInv = new DefaultTableModel(columnNamesInv, 0); // Création du tableau

		// Ajout de chaque lot dans la List pour les afficher dans le tableau
		for (IData item : InvList) {
			if (item instanceof LotProduit lot) {
				tableModelInv.addRow(new Object[] { lot.getIdLotProduit(), lot.getIdProduit(), lot.getPrixVenteUni(),
						lot.getQuantite(), lot.getPeremption(), lot.getIdAchat() });
			}
		}

		JTable InvTable = new JTable(tableModelInv);
		JScrollPane scrollPaneInv = new JScrollPane(InvTable);
		cstInv.gridy = 1;
		contentPanel.add(scrollPaneInv, cstInv);

		// ==============================================================

		// Bouton pour modifier une LotProduit

		// Création du bouton
		JButton modifySaleButton = new JButton("Modifier un lot");
		modifySaleButton.setForeground(grisDoux);
		modifySaleButton.setBackground(jauneDoux);
		cstInv.gridy = 3;
		contentPanel.add(modifySaleButton, cstInv);

		// Action au clique sur ce bouton
		modifySaleButton.addActionListener(modifySaleEvent -> {

			int selectedRow = InvTable.getSelectedRow(); // Récupération de la ligne (= lot) sélectionné

			// Vérification que l'utilisateur a bien choisi une ligne sur laquelle agir
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un lot à modifier.", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {

				LotProduit selectedLotProduit = (LotProduit) InvList.get(selectedRow); // Récupération du lot
																						// sélectionné

				// Récupération des informations correspondants au lot sélectionné
				JTextField prixField = new JTextField(String.valueOf(selectedLotProduit.getPrixVenteUni()));
				JTextField quantiteField = new JTextField(String.valueOf(selectedLotProduit.getQuantite()));
				JTextField peremptionField = new JTextField(String.valueOf(selectedLotProduit.getPeremption()));

				// Affichage du formulaire avec les informations du lot préremplis pour pouvoir
				// les modifier
				JPanel saleForm = new JPanel(new GridLayout(4, 2));
				saleForm.add(new JLabel("Prix vente unitaire :"));
				saleForm.add(prixField);
				saleForm.add(new JLabel("Quantité restante :"));
				saleForm.add(quantiteField);
				saleForm.add(new JLabel("Date péremption :"));
				saleForm.add(peremptionField);

				int result = JOptionPane.showConfirmDialog(frame, saleForm, "Modifier une LotProduit",
						JOptionPane.OK_CANCEL_OPTION);

				if (result == JOptionPane.OK_OPTION) {

					// Création de l'objet LotProduit mis à jour
					LotProduit updatedLotProduit = new LotProduit(selectedLotProduit.getIdLotProduit(),
							selectedLotProduit.getIdProduit(), Double.parseDouble(prixField.getText()),
							Double.parseDouble(quantiteField.getText()), Date.valueOf(peremptionField.getText()),
							selectedLotProduit.getIdAchat());

					// Mise à jour dans la base de données
					Gestion.update(selectedLotProduit, "lot_produit", "id_lot_produit",
							selectedLotProduit.getIdLotProduit());

					// Mise à jour dans le tableau
					tableModelInv.setValueAt(selectedLotProduit.getPrixVenteUni(), selectedRow, 2);
					tableModelInv.setValueAt(selectedLotProduit.getQuantite(), selectedRow, 3);
					tableModelInv.setValueAt(selectedLotProduit.getPeremption(), selectedRow, 4);

					JOptionPane.showMessageDialog(frame, "LotProduit modifiée avec succès !");
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(frame,
						"Erreur lors de la modification de la LotProduit : " + ex.getMessage(), "Erreur",
						JOptionPane.ERROR_MESSAGE);
			}
		});

		// ==============================================================

		// Bouton pour supprimer une LotProduit

		JButton deleteSaleButton = new JButton("Supprimer un LotProduit");
		deleteSaleButton.setForeground(grisDoux);
		deleteSaleButton.setBackground(jauneDoux);
		cstInv.gridy = 4;
		contentPanel.add(deleteSaleButton, cstInv);

		// Action au clique sur ce bouton
		deleteSaleButton.addActionListener(deleteSaleEvent -> {

			int selectedRow = InvTable.getSelectedRow();

			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(frame, "Veuillez sélectionner une LotProduit à supprimer.", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {

				LotProduit selectedLotProduit = (LotProduit) InvList.get(selectedRow);

				int confirmDelete = JOptionPane.showConfirmDialog(frame,
						"Êtes-vous sûr de vouloir supprimer cette LotProduit ?", "Confirmer la suppression",
						JOptionPane.YES_NO_OPTION);

				if (confirmDelete == JOptionPane.YES_OPTION) {
					InvList.remove(selectedLotProduit);
					tableModelInv.removeRow(selectedRow);

					Gestion.delete(selectedLotProduit, "lot_produit");

					JOptionPane.showMessageDialog(frame, "LotProduit supprimée avec succès !");
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(frame,
						"Erreur lors de la suppression de la LotProduit : " + ex.getMessage(), "Erreur",
						JOptionPane.ERROR_MESSAGE);
			}
		});

		// ==============================================================

		// List dans laquelle on aura tous les produits ajoutés au panier
		List<IData> cartList = new ArrayList<>();

		// Création du tableau pour le panier du client
		String[] columnNamesCart = { "ID produit", "Nom produit", "Prix unitaire", "Quantité", "Total" }; // Création
																											// des
																											// colonnes
																											// du panier
																											// du client

		DefaultTableModel tableModelCart = new DefaultTableModel(columnNamesCart, 0); // Création du tableau

		JTable cartTable = new JTable(tableModelCart);
		JScrollPane scrollPaneCart = new JScrollPane(cartTable);
		cstCart.gridy = 1;
		contentPanel.add(scrollPaneCart, cstCart);

		// ==============================================================

		// Bouton pour ajouter un produit au panier

		// Création du bouton
		JButton addToCartButton = new JButton("Ajouter au panier");
		addToCartButton.setForeground(grisDoux);
		addToCartButton.setBackground(jauneDoux);
		cstInv.gridy = 2;
		contentPanel.add(addToCartButton, cstInv);

		// Action au clique sur ce bouton
		addToCartButton.addActionListener(addToCartEvent -> {

			int selectedRow = InvTable.getSelectedRow();

			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un produit à ajouter au panier.", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			LotProduit selectedLotProduit = (LotProduit) InvList.get(selectedRow);

			JTextField quantityField = new JTextField("1");
			JPanel addForm = new JPanel(new GridLayout(2, 2));
			addForm.add(new JLabel("Quantité :"));
			addForm.add(quantityField);

			int result = JOptionPane.showConfirmDialog(frame, addForm, "Ajouter au panier",
					JOptionPane.OK_CANCEL_OPTION);

			if (result == JOptionPane.OK_OPTION) {
				try {

					double quantity = Double.parseDouble(quantityField.getText());
					if (quantity <= 0 || quantity > selectedLotProduit.getQuantite()) {
						throw new IllegalArgumentException("Quantité invalide.");
					}

					double total = selectedLotProduit.getPrixVenteUni() * quantity;
					tableModelCart.addRow(new Object[] { selectedLotProduit.getIdProduit(),
							selectedLotProduit.getIdProduit(), selectedLotProduit.getPrixVenteUni(), quantity, total });
					cartList.add(new LotProduit(selectedLotProduit.getIdLotProduit(), selectedLotProduit.getIdProduit(),
							selectedLotProduit.getPrixVenteUni(), quantity, selectedLotProduit.getPeremption(),
							selectedLotProduit.getIdAchat()));

					selectedLotProduit.setQuantite(selectedLotProduit.getQuantite() - quantity);
					tableModelInv.setValueAt(selectedLotProduit.getQuantite(), selectedRow, 3);

					JOptionPane.showMessageDialog(frame, "Produit ajouté au panier !");
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, "Erreur lors de l'ajout au panier : " + ex.getMessage(),
							"Erreur", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// ==============================================================

		// Bouton pour modifier un produit dans le panier
		JButton modifyCartButton = new JButton("Modifier le panier");
		modifyCartButton.setForeground(grisDoux);
		modifyCartButton.setBackground(jauneDoux);
		cstCart.gridy = 2;
		contentPanel.add(modifyCartButton, cstCart);

		// Action au clique sur ce bouton
		modifyCartButton.addActionListener(modifyCartEvent -> {

			int selectedRow = cartTable.getSelectedRow();

			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un produit à modifier dans le panier.",
						"Erreur", JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {

				LotProduit selectedCartItem = (LotProduit) cartList.get(selectedRow);

				JTextField quantityField = new JTextField(String.valueOf(selectedCartItem.getQuantite()));
				JPanel modifyForm = new JPanel(new GridLayout(2, 2));
				modifyForm.add(new JLabel("Quantité :"));
				modifyForm.add(quantityField);

				int result = JOptionPane.showConfirmDialog(frame, modifyForm, "Modifier le panier",
						JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					double newQuantity = Double.parseDouble(quantityField.getText());
					if (newQuantity <= 0) {
						throw new IllegalArgumentException("Quantité invalide.");
					}

					double total = selectedCartItem.getPrixVenteUni() * newQuantity;
					tableModelCart.setValueAt(newQuantity, selectedRow, 3);
					tableModelCart.setValueAt(total, selectedRow, 4);

					double diffQuantity = newQuantity - selectedCartItem.getQuantite();
					selectedCartItem.setQuantite(newQuantity);
					LotProduit originalProduct = (LotProduit) InvList.stream()
							.filter(lp -> lp.getIdProduit() == selectedCartItem.getIdLotProduit()).findFirst()
							.orElse(null);
					if (originalProduct != null) {
						originalProduct.setQuantite(originalProduct.getQuantite() - diffQuantity);
						tableModelInv.setValueAt(originalProduct.getQuantite(), InvList.indexOf(originalProduct), 3);
					}

					JOptionPane.showMessageDialog(frame, "Produit modifié avec succès !");
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(frame, "Erreur lors de la modification : " + ex.getMessage(), "Erreur",
						JOptionPane.ERROR_MESSAGE);
			}
		});

		// ==============================================================

		// Bouton pour supprimer un produit du panier

		JButton deleteCartButton = new JButton("Supprimer du panier");
		deleteCartButton.setForeground(grisDoux);
		deleteCartButton.setBackground(jauneDoux);
		cstCart.gridy = 3;
		contentPanel.add(deleteCartButton, cstCart);

		// Action au clique sur ce bouton
		deleteCartButton.addActionListener(deleteCartEvent -> {

			int selectedRow = cartTable.getSelectedRow();

			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un produit à supprimer du panier.",
						"Erreur", JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {

				LotProduit selectedCartItem = (LotProduit) cartList.get(selectedRow);
				LotProduit originalProduct = (LotProduit) InvList.stream().filter(
						lp -> lp instanceof LotProduit && lp.getIdLotProduit() == selectedCartItem.getIdLotProduit())
						.findFirst().orElse(null);

				if (originalProduct != null) {
					originalProduct.setQuantite(originalProduct.getQuantite() + selectedCartItem.getQuantite());
					tableModelInv.setValueAt(originalProduct.getQuantite(), InvList.indexOf(originalProduct), 3);
				}

				cartList.remove(selectedRow);
				tableModelCart.removeRow(selectedRow);

				JOptionPane.showMessageDialog(frame, "Produit supprimé du panier !");
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(frame, "Erreur lors de la suppression : " + ex.getMessage(), "Erreur",
						JOptionPane.ERROR_MESSAGE);
			}
		});

		// ==============================================================

		// Bouton pour paiement en espèces
		JButton payCashButton = new JButton("Payer en espèces");
		payCashButton.setForeground(grisDoux);
		payCashButton.setBackground(Color.RED);
		cstCart.gridy = 4;
		cstCart.gridwidth = 2;
		contentPanel.add(payCashButton, cstCart);

		// Bouton pour paiement par carte bancaire
		JButton payCardButton = new JButton("Payer par carte bancaire");
		payCardButton.setForeground(grisDoux);
		payCardButton.setBackground(Color.GREEN);
		cstCart.gridy = 5;
		cstCart.gridwidth = 2;
		contentPanel.add(payCardButton, cstCart);

		// ==============================================================

		contentPanel.revalidate();
		contentPanel.repaint();
	}
}