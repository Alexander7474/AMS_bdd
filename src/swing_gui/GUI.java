package swing_gui;

import javax.swing.*;
import java.awt.*;

public class GUI {

    public static void main(String[] args) {
    	
    	JFrame frame = new JFrame("AMS - Page de connexion"); // Créer une nouvelle fenêtre (JFrame) 
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Arrêter le programme lorsque l'on ferme la fenêtre
    	
    	JPanel panel = new JPanel(new GridBagLayout()); // Organiser les composants dans une grille
    	GridBagConstraints cst = new GridBagConstraints(); // Définir la position, la taille et l'alignement des composants dans la grille
    	
    	cst.insets = new Insets(10, 10, 10, 10); // Définir des marges pour chaque composant (10 pixels en haut, à gauche, en bas, à droite)
    	
    	// Ajouter un titre centré "Connexion"
    	JLabel titleLabel = new JLabel("Connexion", SwingConstants.CENTER); 
    	titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
    	cst.gridx = 0; // Positionné à la colonne 0
    	cst.gridy = 0; // Positionné à la ligne 0
    	cst.gridwidth = 2; // Le titre s'étend sur deux colonnes
    	panel.add(titleLabel, cst);
    	
    	// Ajouter un label "Nom d'utilisateur"
    	JLabel userLabel = new JLabel("Nom d'utilisateur : "); 
    	cst.gridx = 0; 
    	cst.gridy = 1; // Positionné à la ligne 1 (en dessous du titre)
    	cst.gridwidth = 1; 
    	panel.add(userLabel, cst);
    	
    	// Ajouter un champ texte pour entrer le nom d'utilisateur
    	JTextField userText = new JTextField(15);
    	cst.gridx = 1; // Positionné à droite du label
    	cst.gridy = 1; // Positionné à la même ligne que le label
    	panel.add(userText, cst);
    	
    	// Ajouter un label "Mot de passe"
    	JLabel passwordLabel = new JLabel("Mot de passe : "); 
    	cst.gridx = 0; 
    	cst.gridy = 2; // Le label est à la ligne 2 (en dessous des composants pour le nom d'utilisateur)
    	panel.add(passwordLabel, cst);
    	
    	// Ajouter un champ texte pour entrer le mot de passe
    	JTextField passwordText = new JTextField();
    	cst.gridx = 1; // Le champ sera à droite du label "Mot de passe"
    	cst.gridy = 2;
    	cst.fill = GridBagConstraints.HORIZONTAL;
    	panel.add(passwordText, cst);
    	
    	// Ajouter un bouton pour se connecter
    	JButton loginButton = new JButton("Se connecter");
    	cst.gridx = 0; 
    	cst.gridy = 3; // Le bouton sera en dessous des composants pour le mot de passe
    	cst.gridwidth = 2;
    	panel.add(loginButton, cst);
    	
    	frame.add(panel); // Ajouter le panel avec tout ses composants à la fenêtre
    	frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximiser la fenêtre pour qu'elle occupe tout l'écran
    	frame.setVisible(true); // Rendre la fenêtre visible
    	
    }
}
