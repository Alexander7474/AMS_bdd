package tabs;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PurchasesTab {

	public static void loadPurchasesTab(JPanel contentPanel, JFrame frame) {

		JLabel titleLabelPurchases = new JLabel("Onglet des achats", SwingConstants.CENTER);
		titleLabelPurchases.setFont(new Font("Arial", Font.BOLD, 20));
		GridBagConstraints cstPurchases = new GridBagConstraints();
		cstPurchases.gridy = 0;
		cstPurchases.gridx = 0;
		cstPurchases.insets = new Insets(10, 10, 10, 10);
		contentPanel.add(titleLabelPurchases, cstPurchases);

		contentPanel.revalidate();
		contentPanel.repaint();

	}

}
