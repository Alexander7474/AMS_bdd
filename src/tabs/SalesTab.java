package tabs;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class SalesTab {
	
	public static void loadSalesTab(JPanel contentPanel, JFrame frame) {
		
		JLabel titleLabelSales = new JLabel("Onglet des ventes", SwingConstants.CENTER);
		titleLabelSales.setFont(new Font("Arial", Font.BOLD, 20));
		GridBagConstraints cstSales = new GridBagConstraints();
		cstSales.gridy = 0;
		cstSales.gridx = 0;
		cstSales.insets = new Insets(10, 10, 10, 10);
		contentPanel.add(titleLabelSales, cstSales);
		
		contentPanel.revalidate();
		contentPanel.repaint();
		
	}

}
