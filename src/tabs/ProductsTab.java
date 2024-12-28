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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import data.Gestion;
import data.IData;
import data.entity.Produit;

public class ProductsTab {

	public static void loadProductsTab(JPanel contentPanel, JFrame frame) {

		// ==============================================================

		// Création des couleurs

		Color grisDoux = new Color(51, 51, 51);
		Color jauneDoux = new Color(255, 183, 77);

		// ==============================================================

		// Création du titre de l'onglet

		JLabel titleLabelProducts = new JLabel("Onglet des produits", SwingConstants.CENTER);
		titleLabelProducts.setFont(new Font("Arial", Font.BOLD, 20));
		GridBagConstraints cstProducts = new GridBagConstraints();
		cstProducts.gridy = 0;
		cstProducts.gridx = 0;
		cstProducts.insets = new Insets(10, 10, 10, 10);
		contentPanel.add(titleLabelProducts, cstProducts);

		// ==============================================================

		// Création de la List dans laquelle seront stockés les produits

		List<IData> productsList = new ArrayList<>();

		// Tester manuellement les fonctionnalités des produits
		productsList.add(new Produit(1, "Produit A", "Description", "Catégorie 1")); // >!SQL
		productsList.add(new Produit(2, "Produit B", "Description 2", "Catégorie 2")); // >!SQL

		// ==============================================================

		// Affichage d'un tableau des produits

		String[] columnNamesProducts = { "ID Produit", "Nom", "Description", "Catégorie" }; // Nom des colonnes
		DefaultTableModel tableModelProducts = new DefaultTableModel(columnNamesProducts, 0);

		// Récupération des produits
		for (IData item : productsList) {
			if (item instanceof Produit produit) {
				tableModelProducts.addRow(new Object[] { produit.getIdProduit(), produit.getNom(), produit.getDesc(),
						produit.getCategorie() });
			}
		}

		JTable productsTable = new JTable(tableModelProducts);
		JScrollPane scrollPaneProducts = new JScrollPane(productsTable);
		cstProducts.gridy = 1;
		contentPanel.add(scrollPaneProducts, cstProducts);

		// ==============================================================

		// Bouton pour ajouter un produit

		JButton addProductButton = new JButton("Ajouter un produit");
		addProductButton.setForeground(grisDoux);
		addProductButton.setBackground(jauneDoux);
		cstProducts.gridy = 2;
		contentPanel.add(addProductButton, cstProducts);

		// Action au clique sur ce bouton
		addProductButton.addActionListener(addProductEvent -> {

			// Création des champs
			JTextField idField = new JTextField(15);
			JTextField nameField = new JTextField(15);
			JTextField descriptionField = new JTextField(15);
			JTextField categoryField = new JTextField(15);

			// Mise en forme du formulaire
			JPanel productForm = new JPanel(new GridLayout(4, 2));
			productForm.add(new JLabel("ID Produit :"));
			productForm.add(idField);
			productForm.add(new JLabel("Nom :"));
			productForm.add(nameField);
			productForm.add(new JLabel("Description :"));
			productForm.add(descriptionField);
			productForm.add(new JLabel("Catégorie :"));
			productForm.add(categoryField);

			int result = JOptionPane.showConfirmDialog(frame, productForm, "Ajouter un produit",
					JOptionPane.OK_CANCEL_OPTION);

			if (result == JOptionPane.OK_OPTION) {
				try {

					Produit newProduit = new Produit(Integer.parseInt(idField.getText()), nameField.getText(),
							descriptionField.getText(), categoryField.getText()); // Création d'un produit

					productsList.add(newProduit); // Ajouter le produit dans la liste

					tableModelProducts.addRow(new Object[] { newProduit.getIdProduit(), newProduit.getNom(),
							newProduit.getDesc(), newProduit.getCategorie() });

					Gestion.insert(newProduit, "produit"); // Insérer le produit dans produit

					JOptionPane.showMessageDialog(frame, "Produit ajouté avec succès !");
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, "Erreur lors de l'ajout du produit : " + ex.getMessage(),
							"Erreur", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// ==============================================================

		// Bouton pour supprimer un produit

		JButton deleteProductButton = new JButton("Supprimer un produit");
		deleteProductButton.setForeground(grisDoux);
		deleteProductButton.setBackground(jauneDoux);
		cstProducts.gridy = 3;
		contentPanel.add(deleteProductButton, cstProducts);

		deleteProductButton.addActionListener(deleteProductEvent -> {
			int selectedRow = productsTable.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un produit à supprimer.", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				Produit selectedProduit = (Produit) productsList.get(selectedRow);

				productsList.remove(selectedProduit);
				tableModelProducts.removeRow(selectedRow);

				Gestion.delete(selectedProduit, "produit");

				JOptionPane.showMessageDialog(frame, "Produit supprimé avec succès !");
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(frame, "Erreur lors de la suppression : " + ex.getMessage(), "Erreur",
						JOptionPane.ERROR_MESSAGE);
			}
		});

		// ==============================================================

		contentPanel.revalidate();
		contentPanel.repaint();
	}
}
