package swing_gui;

import javax.swing.*;
import java.awt.*;

public class LoginApp {
	
	public static void main(String[] args) {
		
		JFrame frame = new JFrame("AMS - Page de connexion"); // Créer une nouvelle fenêtre (JFrame)
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Arrêter le programme quand on ferme la fenêtre
		
		// ===================================================================================================
		
		JPanel panel = new JPanel(new GridBagLayout()); // Organiser les composants dans une grille
		GridBagConstraints cst = new GridBagConstraints(); // Définir la position, la taille, et l'alignement des composants dans la grille
		
		cst.insets = new Insets(10, 10, 10, 10); // Définir des marges pour chaque composant (10 pixels en haut, à gauche, en bas, à droite)
		
		// ===================================================================================================
		
		// Ajouter un titre centré "Connexion
		JLabel titleLabel = new JLabel("Connexion", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
		cst.gridx = 0; // Positionné à la colonne 0
		cst.gridy = 0; // Positionné à la ligne 0
		cst.gridwidth = 2; // Le titre s'étend sur deux colonnes
		panel.add(titleLabel, cst);
		
		// ===================================================================================================
		
		// Ajouter un label "Nom d'utilisateur"
		JLabel userLabel = new JLabel("Nom d'utilisateur : ");
		cst.gridx = 0; 
		cst.gridy = 1; // Positionné à la ligne 1 (en dessous du titre)
		cst.gridwidth = 1;
		panel.add(userLabel, cst);
		
		// ===================================================================================================
		
		// Ajouter un champ texte pour entrer le nom d'utilisateur 
		JTextField userText = new JTextField(15);
		cst.gridx = 1; // Positionné à droite du label "Nom d'utilisateur"
		cst.gridy = 1; // Positionné à la même ligne que le label
		panel.add(userText, cst);
		
		// ===================================================================================================
		
		// Ajouter un label "Mot de passe"
		JLabel passwordLabel = new JLabel("Mot de passe : ");
		cst.gridx = 0;
		cst.gridy = 2;
		panel.add(passwordLabel, cst);
		
		// ===================================================================================================
		
		// Ajouter un champ texte pour entrer le mot de passe 
		JPasswordField passwordText = new JPasswordField();
		cst.gridx = 1; // Positionné à droite du label "Mot de passe"
		cst.gridy = 2; 
		cst.fill = GridBagConstraints.HORIZONTAL; 
		panel.add(passwordText, cst);
		
		// ===================================================================================================
		
		// Ajouter un bouton pour se connecter
		JButton loginButton = new JButton("Se connecter");
		cst.gridx = 0;
		cst.gridy = 3; // Le bouton sera en dessous des composants pour le mot de passe
		cst.gridwidth = 2;
		panel.add(loginButton, cst);
		
		// ===================================================================================================
		
		// Ajouter un label message d'information pour la connexion à l'application
		JLabel messageLabel = new JLabel("", SwingConstants.CENTER);
		cst.gridx = 0;
		cst.gridy = 4; // Positionné en dessous du bouton
		cst.gridwidth = 2; // S'étend sur 2 colonnes
		panel.add(messageLabel, cst);
		
		// ===================================================================================================
		
		// Action quand on clique sur le bouton
		loginButton.addActionListener(e -> {
			String username = userText.getText();
			String password = new String(passwordText.getPassword());
			
			// BONUS : FERMER LA PAGE DE CONNEXION SI PLUS DE 3 ERREURS DE CONNEXION
			if(UserManager.verifyUser(username, password)) { // Vérifier si les champs entrés par l'utilisateur sont valides
				messageLabel.setText("Connexion réussie !");
				frame.dispose(); // Fermer la fenêtre LoginApp
				ManagementSoftware.loadMainApplication(username); // Charger le logiciel de gestion
			} else {
				// BONUS : VERIFIER SI C'EST LE NOM D'UTILISATEUR ET/OU LE MOT DE PASSE QUI POSE(NT) PROBLEME
				messageLabel.setText("Nom d'utilisateur ou mot de passe incorrect.");
			}
			
		});
		
		// ===================================================================================================
		
		frame.add(panel); // Ajouter le panel avec tout ses composants à la fenêtre
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximiser la fenêtre pour qu'elle occupe tout l'écran
		frame.setVisible(true); // Rendre la fenêtre visible
		
	}
	
}