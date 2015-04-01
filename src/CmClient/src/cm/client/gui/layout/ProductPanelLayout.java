package cm.client.gui.layout;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTitledPanel;

import cm.common.entity.domain.Task;

@SuppressWarnings("serial")
public class ProductPanelLayout extends JPanel
{
	public ProductPanelLayout()
	{
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(10, 10, 10, 10);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 0;
		add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0};
		gbl_panel_1.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JXTitledPanel titledPanel = new JXTitledPanel();
		titledPanel.setTitle("Product");
		GridBagConstraints gbc_titledPanel = new GridBagConstraints();
		gbc_titledPanel.fill = GridBagConstraints.BOTH;
		gbc_titledPanel.insets = new Insets(0, 0, 0, 5);
		gbc_titledPanel.gridx = 0;
		gbc_titledPanel.gridy = 0;
		panel_1.add(titledPanel, gbc_titledPanel);
		GridBagLayout gridBagLayout_1 = new GridBagLayout();
		gridBagLayout_1.columnWidths = new int[]{0, 0};
		gridBagLayout_1.rowHeights = new int[]{0, 0};
		gridBagLayout_1.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout_1.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		titledPanel.getContentContainer().setLayout(gridBagLayout_1);
		
		JPanel panel_2 = new JPanel();
		panel_2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				onEditProduct();
			}
		});
		panel_2.setBackground(Color.WHITE);
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 0;
		titledPanel.getContentContainer().add(panel_2, gbc_panel_2);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{0, 0};
		gbl_panel_2.rowHeights = new int[]{0, 0};
		gbl_panel_2.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);
		
		productLabel = new JLabel("New label");
		productLabel.setOpaque(true);
		productLabel.setBackground(Color.WHITE);
		GridBagConstraints gbc_productLabel = new GridBagConstraints();
		gbc_productLabel.fill = GridBagConstraints.BOTH;
		gbc_productLabel.gridx = 0;
		gbc_productLabel.gridy = 0;
		panel_2.add(productLabel, gbc_productLabel);
		
		JXTitledPanel titledPanel_1 = new JXTitledPanel();
		titledPanel_1.setTitle("Related Tasks");
		GridBagConstraints gbc_titledPanel_1 = new GridBagConstraints();
		gbc_titledPanel_1.fill = GridBagConstraints.BOTH;
		gbc_titledPanel_1.gridx = 1;
		gbc_titledPanel_1.gridy = 0;
		panel_1.add(titledPanel_1, gbc_titledPanel_1);
		GridBagLayout gridBagLayout_2 = new GridBagLayout();
		gridBagLayout_2.columnWidths = new int[]{0, 0};
		gridBagLayout_2.rowHeights = new int[]{0, 0};
		gridBagLayout_2.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout_2.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		titledPanel_1.getContentContainer().setLayout(gridBagLayout_2);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		titledPanel_1.getContentContainer().add(scrollPane, gbc_scrollPane);
		
		taskTable = new JXTable();
		scrollPane.setViewportView(taskTable);
	}

	public JLabel getProductLabel() {
		return productLabel;
	}

	public JXTable getTaskTable() {
		return taskTable;
	}

	protected void onEditProduct() {
		;
	}

	protected void onTaskSelected( Task task ) {
		;
	}

	private JLabel productLabel;
	private JXTable taskTable;
}
