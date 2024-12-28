package tabs;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ProductsTab {
	
	public static void loadProductsTab(JPanel contentPanel, JFrame frame) {
		
		JLabel titleLabelProducts = new JLabel("Onglet des produits", SwingConstants.CENTER);
		titleLabelProducts.setFont(new Font("Arial", Font.BOLD, 20));
		GridBagConstraints cstProducts = new GridBagConstraints();
		cstProducts.gridy = 0;
		cstProducts.gridx = 0;
		cstProducts.insets = new Insets(10, 10, 10, 10);
		contentPanel.add(titleLabelProducts, cstProducts);

		contentPanel.revalidate();
		contentPanel.repaint();
		
	}

}
