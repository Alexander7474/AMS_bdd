package tabs;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import swing_gui.UserManager;

public class ParametersTab {

	public static void loadParametersTab(JPanel contentPanel, JFrame frame) {
		
		// ==============================================================
		
		// Création du titre de l'onglet

		JLabel titleLabelParameters = new JLabel("Onglet des paramètres", SwingConstants.CENTER);
		titleLabelParameters.setFont(new Font("Arial", Font.BOLD, 20));
		GridBagConstraints cstParameters = new GridBagConstraints();
		cstParameters.gridy = 0;
		cstParameters.gridx = 0;
		cstParameters.insets = new Insets(10, 10, 10, 10);
		contentPanel.add(titleLabelParameters, cstParameters);
		
		// ==============================================================

		// Ajouter un utilisateur
		
		// Création du bouton
		JButton addUserButton = new JButton("Ajouter un utilisateur");
		addUserButton.setForeground(new Color(51, 51, 51));
		addUserButton.setBackground(new Color(255, 183, 77));
		cstParameters.gridy = 1;
		contentPanel.add(addUserButton, cstParameters);

		// Action au clique sur ce bouton
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
		
		// ==============================================================

		// Modifier le mot de passe
		
		// Création du bouton
		JButton updateUserButton = new JButton("Modifier le mot de passe");
		updateUserButton.setForeground(new Color(51, 51, 51));
		updateUserButton.setBackground(new Color(255, 183, 77));
		cstParameters.gridy = 2;
		contentPanel.add(updateUserButton, cstParameters);

		// Action au clique sur ce bouton
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
		
		// Création de ce bouton
		JButton viewUsersButton = new JButton("Afficher les utilisateurs");
		viewUsersButton.setForeground(new Color(51, 51, 51));
		viewUsersButton.setBackground(new Color(255, 183, 77));
		cstParameters.gridy = 3;
		contentPanel.add(viewUsersButton, cstParameters);

		viewUsersButton.addActionListener(viewUsersEvent -> {
			List<String[]> users = UserManager.getAllUsers();

			StringBuilder userList = new StringBuilder("Liste des utilisateurs :\n\n");

			for (String[] user : users)
				userList.append("Nom : ").append(user[0]).append(" | Mot de passe : ").append(user[1]).append("\n");

			JOptionPane.showMessageDialog(frame, userList.toString(), "Utilisateurs", JOptionPane.INFORMATION_MESSAGE);
		});
		
		// ==============================================================

		// Supprimer un utilisateur
		JButton deleteUserButton = new JButton("Supprimer un utilisateur");
		deleteUserButton.setForeground(new Color(51, 51, 51));
		deleteUserButton.setBackground(new Color(255, 183, 77));
		cstParameters.gridy = 4;
		contentPanel.add(deleteUserButton, cstParameters);

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

	}

}
