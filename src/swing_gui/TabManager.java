package swing_gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public class TabManager {
	
	/**
	 * Créé un tableau 
	 * 
	 * @param width
	 * @param tableModel
	 * @return
	 */
	public static JTable getTable(int width, DefaultTableModel tableModel) {
		JTable table = new JTable(tableModel);

		// Personnaliser l'en-tête de la table
		JTableHeader header = table.getTableHeader();
		header.setBackground(Palette.BUTTON_ACTIVE); // Couleur de fond de l'en-tête
		header.setForeground(Palette.TEXT_LIGHT); // Couleur du texte de l'en-tête
		header.setFont(new Font("Arial", Font.BOLD, 14)); // Police de l'en-tête

		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		table.setBackground(new Color(230, 230, 250)); // Couleur de fond de la table (lavande)
		table.setForeground(Color.BLACK); // Couleur du texte
		table.setGridColor(Color.GRAY); // Couleur des bordures entre les cellules
		table.setSelectionBackground(new Color(100, 149, 237)); // Couleur de sélection (bleu clair)
		table.setSelectionForeground(Color.WHITE); // Couleur du texte lors de la sélection

		// Personnaliser la hauteur des lignes
		table.setRowHeight(30); // Hauteur de ligne plus grande

		// détermine la taille de la table
		int columnCnt = table.getColumnCount();
		int prefSizeX = width / columnCnt;

		for (int column = 0; column < columnCnt; column++) {
			table.getColumnModel().getColumn(column).setPreferredWidth(prefSizeX);
		}

		table.setPreferredScrollableViewportSize(new Dimension(prefSizeX * columnCnt, 400));

		// Personnaliser le rendu des cellules
		table.setDefaultRenderer(Object.class, new TableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				// Créer un composant de cellule
				JLabel label = new JLabel(value.toString());

				// Appliquer un style spécifique pour les cellules
				if (isSelected) {
					label.setBackground(Palette.BUTTON_ACTIVE); // Fond bleu clair pour les cellules sélectionnées
					label.setForeground(Palette.TEXT_LIGHT); // Texte blanc pour les cellules sélectionnées
				} else {
					// Personnaliser la couleur de fond selon la ligne
					if (row % 2 == 0) {
						label.setBackground(Palette.BACKGROUND_LIGHT_DARK); // Fond bleu clair pour les lignes paires
					} else {
						label.setBackground(Palette.BACKGROUND_LIGHT); // Fond blanc pour les lignes impaires
					}
					label.setForeground(Palette.TEXT_DARK); // Texte noir
				}

				// Rendre la cellule opaque
				label.setOpaque(true);
				label.setHorizontalAlignment(SwingConstants.CENTER); // Centrer le texte

				return label;
			}
		});

		table.revalidate();
		table.repaint();
		
		return table;
	}
	
	/**
	 * @brief créé une scrollPane pour un tableau
	 * 
	 * @param table
	 * @return
	 */
	public static JScrollPane getScrollPane(JTable table) {
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		return scrollPane;
	}
	
	/**
	 * @brief créé un bouton a utiliser dans les tabs de gestion
	 * 
	 * @param text
	 * @return
	 */
	public static JButton getButton(String text) {
		JButton button = new JButton(text);
		button.setForeground(Palette.TEXT_DARK);
		button.setBackground(Palette.BUTTON_ACTIVE);
		
		return button;
	}
}
