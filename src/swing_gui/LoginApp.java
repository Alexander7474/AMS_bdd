package swing_gui;

import javax.swing.*;

import tabs.TabManager;

import java.awt.*;

public class LoginApp {

	public static void main(String[] args) {

		// ==============================================================

		// Création de la fenêtre
		JFrame frame = new JFrame("AMS - Page de connexion");
		
		frame.setSize(800,600);
		frame.setExtendedState(JFrame.NORMAL);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Fermeture du programme à la fermeture de la fenêtre

		// ==============================================================

		// Création d'un conteneur
		JPanel panel = new JPanel(new GridBagLayout()); // Organise les composants dans une grille
		panel.setBackground(new Color(249, 249, 249)); // Beige clair
		GridBagConstraints cst = new GridBagConstraints(); // Définir la position, la taille, et l'alignement des
															// composants dans la grille
		cst.insets = new Insets(10, 10, 10, 10); // Définis des marges pour chaque composant (10 pixels en haut, à
													// gauche, en bas, à droite)

		// ==============================================================

		// Ajout d'un titre centré "Connexion"
		JLabel titleLabel = new JLabel("Connexion", SwingConstants.CENTER);
		titleLabel.setForeground(new Color(51, 51, 51)); // Gris doux
		titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
		cst.gridx = 0; // Positionné à la colonne 0
		cst.gridy = 0; // Positionné à la ligne 0
		cst.gridwidth = 2; // Le titre s'étend sur deux colonnes
		panel.add(titleLabel, cst);

		// ==============================================================

		// Ajout d'un label "Nom d'utilisateur"
		JLabel userLabel = new JLabel("Nom d'utilisateur : ");
		userLabel.setForeground(new Color(51, 51, 51));
		cst.gridy = 1; // Positionné à la ligne 1 (en dessous du titre)
		cst.gridwidth = 1;
		panel.add(userLabel, cst);

		// ==============================================================

		// Ajout d'un champ texte pour entrer le nom d'utilisateur
		JTextField userText = new JTextField(15);
		cst.gridx = 1; // Positionné à droite du label "Nom d'utilisateur"
		panel.add(userText, cst);

		// ==============================================================

		// Ajout d'un label "Mot de passe"
		JLabel passwordLabel = new JLabel("Mot de passe : ");
		passwordLabel.setForeground(new Color(51, 51, 51));
		cst.gridx = 0;
		cst.gridy = 2;
		panel.add(passwordLabel, cst);

		// ==============================================================

		// Ajout d'un champ texte pour entrer le mot de passe
		JPasswordField passwordText = new JPasswordField();
		cst.gridx = 1; // Positionné à droite du label "Mot de passe"
		cst.fill = GridBagConstraints.HORIZONTAL; // Nécessaire
		panel.add(passwordText, cst);

		// ==============================================================

		// Ajout d'un bouton pour se connecter
		JButton loginButton = TabManager.getButton("Se connecter");
		cst.gridx = 0;
		cst.gridy = 3; // Le bouton sera en dessous des composants qui concernent le mot de passe
		cst.gridwidth = 2;
		panel.add(loginButton, cst);

		// ==============================================================

		// Ajout d'un label message d'information pour la connexion à l'application
		JLabel messageLabel = new JLabel("", SwingConstants.CENTER);
		messageLabel.setForeground(Color.red);
		cst.gridy = 4; // Positionné en dessous du bouton
		cst.gridwidth = 2; // S'étend sur 2 colonnes
		panel.add(messageLabel, cst);

		// ==============================================================

		// Lorsque l'on clique sur le bouton "Se connecter"
		loginButton.addActionListener(e -> {

			// !!! A MODIFIER J'AI MIS UN AUTOLOADER !!!
			// Récupération des champs
			String username = "admin";  //userText.getText();
			String password = "admin"; //new String(passwordText.getPassword());

			if ((username.isEmpty()) || (password.isEmpty())) {
				messageLabel.setText("Les champs ne peuvent pas être vides");
			}

			if (UserManager.verifyUser(username, password)) { // Vérifier si les champs entrés par l'utilisateur sont
																// valides
				frame.dispose(); // Ferme la fenêtre
				ManagementSoftware.loadMainApplication(username); // Charge le logiciel de gestion

			} else {
				messageLabel.setText("Nom d'utilisateur ou mot de passe incorrect.");
			}

		});

		// ==============================================================

		frame.add(panel); // Ajoute le conteneur à la fenêtre
		frame.setVisible(true); // Rendre la fenêtre visible

	}

}