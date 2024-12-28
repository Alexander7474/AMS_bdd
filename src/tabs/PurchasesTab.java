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
import data.entity.Fournisseur;

public class PurchasesTab {

	public static void loadPurchasesTab(JPanel contentPanel, JFrame frame) {

		// ==============================================================

		// Création des couleurs

		Color grisDoux = new Color(51, 51, 51);
		Color jauneDoux = new Color(255, 183, 77);

		// ==============================================================

		// Création Titre de l'onglet

		JLabel titleLabelPurchases = new JLabel("Onglet des achats", SwingConstants.CENTER);
		titleLabelPurchases.setFont(new Font("Arial", Font.BOLD, 20));
		GridBagConstraints cstPurchases = new GridBagConstraints();
		cstPurchases.gridy = 0;
		cstPurchases.gridx = 0;
		cstPurchases.insets = new Insets(10, 10, 10, 10);
		contentPanel.add(titleLabelPurchases, cstPurchases);

		// ==============================================================

		// Création de la liste des commandes

		List<IData> commandList = new ArrayList<>();

		// recup des info dans la base
		try {
			Statement statement = Connexion.getConnexion().createStatement();
			try(ResultSet rs = statement.executeQuery("SELECT * FROM commande")){
				while(rs.next()) {
					Commande c = new Commande(rs);
					commandList.add(c);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ==============================================================

		// Tableau pour afficher les commandes

		String[] columnNamesPurchases = { "Produit", "Fournisseur", "Quantité", "Date de péremption" };
		DefaultTableModel tableModelPurchases = new DefaultTableModel(columnNamesPurchases, 0);

		for (IData item : commandList) {
			if (item instanceof Commande commande) {
				tableModelPurchases.addRow(new Object[] { commande.getIdProduit(), commande.getSiret(),
						commande.getQuantite(), "2024-12-29" // exemple Date de péremption < SQL
				});
			}
		}

		JTable purchasesTable = new JTable(tableModelPurchases);
		JScrollPane scrollPanePurchases = new JScrollPane(purchasesTable);
		cstPurchases.gridy = 1;
		contentPanel.add(scrollPanePurchases, cstPurchases);

		// ==============================================================

		// Bouton pour créer une commande en fonction des stocks

		JButton suggestOrdersButton = new JButton("Proposer des commandes");
		suggestOrdersButton.setForeground(grisDoux);
		suggestOrdersButton.setBackground(jauneDoux);
		cstPurchases.gridy = 2;
		contentPanel.add(suggestOrdersButton, cstPurchases);

		suggestOrdersButton.addActionListener(e -> {
			// Test commande proposée < SQL
			Commande suggestedCommand = new Commande(3, "24446666668888888", 8.0);
			JOptionPane.showMessageDialog(frame,
					"Faire une commande du produit " + suggestedCommand.getIdProduit() + " avec le fournisseur "
							+ suggestedCommand.getSiret() + " en quantité " + suggestedCommand.getQuantite(),
					"Commande Proposée", JOptionPane.INFORMATION_MESSAGE);
		});

		// ==============================================================

		// Bouton pour valider ou modifier une commande

		JButton validateOrderButton = new JButton("Valider / Modifier");
		validateOrderButton.setForeground(grisDoux);
		validateOrderButton.setBackground(jauneDoux);
		cstPurchases.gridy = 3;
		contentPanel.add(validateOrderButton, cstPurchases);

		// Action au clique sur le bouton
		validateOrderButton.addActionListener(e -> {
			int selectedRow = purchasesTable.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(frame, "Veuillez sélectionner une commande à valider ou modifier.",
						"Erreur", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Création des champs du formulaire
			JTextField produitField = new JTextField(purchasesTable.getValueAt(selectedRow, 0).toString(), 15);
			JTextField fournisseurField = new JTextField(purchasesTable.getValueAt(selectedRow, 1).toString(), 15);
			JTextField quantiteField = new JTextField(purchasesTable.getValueAt(selectedRow, 2).toString(), 15);

			// Mise en forme du formulaire
			JPanel modifyForm = new JPanel(new GridLayout(3, 2));
			modifyForm.add(new JLabel("ID Produit :"));
			modifyForm.add(produitField);
			modifyForm.add(new JLabel("SIRET Fournisseur :"));
			modifyForm.add(fournisseurField);
			modifyForm.add(new JLabel("Quantité :"));
			modifyForm.add(quantiteField);

			int result = JOptionPane.showConfirmDialog(frame, modifyForm, "Modifier ou Valider la Commande",
					JOptionPane.OK_CANCEL_OPTION);

			if (result == JOptionPane.OK_OPTION) {
				try {

					// Création de l'objet commande
					Commande newCommande = new Commande(Integer.parseInt(produitField.getText()),
							fournisseurField.getText(), Double.parseDouble(quantiteField.getText()));

					// Ajout dans la liste
					commandList.set(selectedRow, newCommande);

					// Mise à jour des valeurs si elles ont été modifiées ou pas
					tableModelPurchases.setValueAt(newCommande.getIdProduit(), selectedRow, 0);
					tableModelPurchases.setValueAt(newCommande.getSiret(), selectedRow, 1);
					tableModelPurchases.setValueAt(newCommande.getQuantite(), selectedRow, 2);

					// Ajout de la commande transformée en achat
					Achat achat = new Achat(newCommande.getIdProduit(), newCommande.getQuantite(),
							new Date(System.currentTimeMillis()));

					Gestion.insert(achat, "achat"); // On ajoute la commande qui est devenue un achat dans la table
													// achat
					Gestion.delete(newCommande, "commande"); // On supprime la commande qui n'est plus une commande
																// puisque la commande a été passée donc c'est devenu un
																// achat

					JOptionPane.showMessageDialog(frame, "Commande validée et transformée en achat !");
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, "Erreur lors de la validation : " + ex.getMessage(), "Erreur",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// ==============================================================

		contentPanel.revalidate();
		contentPanel.repaint();
	}
}
