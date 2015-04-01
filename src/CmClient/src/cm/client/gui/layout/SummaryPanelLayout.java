package cm.client.gui.layout;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTitledPanel;

import cm.client.gui.product.ProductsTable;
import cm.common.entity.domain.Product;
import cm.common.entity.domain.Task;

@SuppressWarnings("serial")
public class SummaryPanelLayout extends JPanel
{
	public ProductsTable getProductsPanel() {
		return productsPanel;
	}

	public JXTable getTaskTable() {
		return taskTable;
	}

	public JLabel getPersonalDetailsLabel() {
		return detailsLabel;
	}
	
	public JLabel getAddressLabel() {
		return addressLabel;
	}
	
	public JPanel getRelationsPanel() {
		return relationsPanel;
	}

	protected void onNewProduct() {
		;
	}

	protected void onNewTask() {
		;
	}

	protected void onNewRelation() {
		;
	}

	protected void onEditAddress() {
		;
	}

	protected void onEditPersonalDetails() {
		;
	}
	
	protected void onEditRelations() {
		;
	}

	protected void onProductSelected( Product product ) {
		;
	}

	protected void onTaskSelected( Task task ) {
		;
	}

	public SummaryPanelLayout()
	{
		GridBagLayout gridBagLayout_4 = new GridBagLayout();
		gridBagLayout_4.columnWidths = new int[]{166, 0};
		gridBagLayout_4.rowHeights = new int[]{143, 0};
		gridBagLayout_4.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout_4.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout_4);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(10, 10, 10, 10);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.insets = new Insets(0, 0, 0, 5);
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 0;
		panel.add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panel_1.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JXTitledPanel titledPanel = new JXTitledPanel();
		titledPanel.getContentContainer().setBackground(Color.WHITE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{46, 0};
		gridBagLayout.rowHeights = new int[]{14, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		titledPanel.getContentContainer().setLayout(gridBagLayout);
		
		detailsLabel = new JLabel("");
		detailsLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				onEditPersonalDetails();
			}
		});
		detailsLabel.setHorizontalAlignment(SwingConstants.LEFT);
		detailsLabel.setVerticalAlignment(SwingConstants.TOP);
		detailsLabel.setOpaque(true);
		detailsLabel.setBackground(Color.WHITE);
		GridBagConstraints gbc_detailsLabel = new GridBagConstraints();
		gbc_detailsLabel.fill = GridBagConstraints.BOTH;
		gbc_detailsLabel.gridx = 0;
		gbc_detailsLabel.gridy = 0;
		titledPanel.getContentContainer().add(detailsLabel, gbc_detailsLabel);
		titledPanel.setTitle("Details");
		GridBagConstraints gbc_titledPanel = new GridBagConstraints();
		gbc_titledPanel.fill = GridBagConstraints.BOTH;
		gbc_titledPanel.insets = new Insets(0, 0, 5, 0);
		gbc_titledPanel.gridx = 0;
		gbc_titledPanel.gridy = 0;
		panel_1.add(titledPanel, gbc_titledPanel);
		
		JXTitledPanel titledPanel_1 = new JXTitledPanel();
		titledPanel_1.setTitle("Address");
		GridBagConstraints gbc_titledPanel_1 = new GridBagConstraints();
		gbc_titledPanel_1.fill = GridBagConstraints.BOTH;
		gbc_titledPanel_1.insets = new Insets(0, 0, 5, 0);
		gbc_titledPanel_1.gridx = 0;
		gbc_titledPanel_1.gridy = 1;
		panel_1.add(titledPanel_1, gbc_titledPanel_1);
		GridBagLayout gridBagLayout_1 = new GridBagLayout();
		gridBagLayout_1.columnWidths = new int[]{0, 0};
		gridBagLayout_1.rowHeights = new int[]{0, 0};
		gridBagLayout_1.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout_1.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		titledPanel_1.getContentContainer().setLayout(gridBagLayout_1);
		
		addressLabel = new JLabel("");
		addressLabel.addMouseListener( new MouseAdapter() {
			public void mouseClicked( MouseEvent arg0 ) {
				onEditAddress();
			}
		});
		addressLabel.setHorizontalAlignment(SwingConstants.LEFT);
		addressLabel.setVerticalAlignment(SwingConstants.TOP);
		addressLabel.setOpaque(true);
		addressLabel.setBackground(Color.WHITE);
		GridBagConstraints gbc_addressLabel = new GridBagConstraints();
		gbc_addressLabel.fill = GridBagConstraints.BOTH;
		gbc_addressLabel.gridx = 0;
		gbc_addressLabel.gridy = 0;
		titledPanel_1.getContentContainer().add(addressLabel, gbc_addressLabel);
		
		JXTitledPanel titledPanel_2 = new JXTitledPanel();
		titledPanel_2.setTitle("Relations");
		GridBagConstraints gbc_titledPanel_2 = new GridBagConstraints();
		gbc_titledPanel_2.fill = GridBagConstraints.BOTH;
		gbc_titledPanel_2.gridx = 0;
		gbc_titledPanel_2.gridy = 2;
		panel_1.add(titledPanel_2, gbc_titledPanel_2);
		GridBagLayout gridBagLayout_5 = new GridBagLayout();
		gridBagLayout_5.columnWidths = new int[]{0, 0};
		gridBagLayout_5.rowHeights = new int[]{0, 0, 0};
		gridBagLayout_5.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout_5.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		titledPanel_2.getContentContainer().setLayout(gridBagLayout_5);
		
		relationsPanel = new JPanel();
		relationsPanel.setBackground(Color.WHITE);
		GridBagConstraints gbc_relationsPanel = new GridBagConstraints();
		gbc_relationsPanel.fill = GridBagConstraints.BOTH;
		gbc_relationsPanel.gridx = 0;
		gbc_relationsPanel.gridy = 0;
		titledPanel_2.getContentContainer().add(relationsPanel, gbc_relationsPanel);
		GridBagLayout gbl_relationsPanel = new GridBagLayout();
		gbl_relationsPanel.columnWidths = new int[]{0, 0};
		gbl_relationsPanel.rowHeights = new int[]{0, 0};
		gbl_relationsPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_relationsPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		relationsPanel.setLayout(gbl_relationsPanel);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.WHITE);
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 0;
		gbc_panel_3.gridy = 1;
		titledPanel_2.getContentContainer().add(panel_3, gbc_panel_3);
		
		JButton btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onEditRelations();
			}
		});
		
		JButton btnNew = new JButton("New");
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onNewRelation();
			}
		});
		panel_3.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel_3.add(btnNew);
		panel_3.add(btnEdit);
		
		JPanel panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 1;
		gbc_panel_2.gridy = 0;
		panel.add(panel_2, gbc_panel_2);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{0, 0};
		gbl_panel_2.rowHeights = new int[]{0, 0, 0};
		gbl_panel_2.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);
		
		JXTitledPanel titledPanel_3 = new JXTitledPanel();
		titledPanel_3.setTitle("Products");
		GridBagConstraints gbc_titledPanel_3 = new GridBagConstraints();
		gbc_titledPanel_3.fill = GridBagConstraints.BOTH;
		gbc_titledPanel_3.insets = new Insets(0, 0, 5, 0);
		gbc_titledPanel_3.gridx = 0;
		gbc_titledPanel_3.gridy = 0;
		panel_2.add(titledPanel_3, gbc_titledPanel_3);
		GridBagLayout gridBagLayout_2 = new GridBagLayout();
		gridBagLayout_2.columnWidths = new int[]{0, 0, 0};
		gridBagLayout_2.rowHeights = new int[]{0, 0};
		gridBagLayout_2.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout_2.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		titledPanel_3.getContentContainer().setLayout(gridBagLayout_2);

		JButton button = new JButton("New");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onNewProduct();
			}
		});
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.insets = new Insets(10, 0, 10, 10);
		gbc_button.anchor = GridBagConstraints.NORTHWEST;
		gbc_button.gridx = 1;
		gbc_button.gridy = 0;
		titledPanel_3.getContentContainer().add(button, gbc_button);
		
		productsPanel = new ProductsTable() {
			protected void onDoubleClick( Product product ) {
				onProductSelected( product );
			}
		};

		GridBagConstraints gbc_productsPanel = new GridBagConstraints();
		gbc_productsPanel.insets = new Insets(10, 10, 10, 5);
		gbc_productsPanel.fill = GridBagConstraints.BOTH;
		gbc_productsPanel.gridx = 0;
		gbc_productsPanel.gridy = 0;
		titledPanel_3.getContentContainer().add(productsPanel, gbc_productsPanel);
		
		JXTitledPanel titledPanel_4 = new JXTitledPanel();
		titledPanel_4.setTitle("Tasks");
		GridBagConstraints gbc_titledPanel_4 = new GridBagConstraints();
		gbc_titledPanel_4.fill = GridBagConstraints.BOTH;
		gbc_titledPanel_4.gridx = 0;
		gbc_titledPanel_4.gridy = 1;
		panel_2.add(titledPanel_4, gbc_titledPanel_4);
		GridBagLayout gridBagLayout_3 = new GridBagLayout();
		gridBagLayout_3.columnWidths = new int[]{0, 0, 0};
		gridBagLayout_3.rowHeights = new int[]{0, 0};
		gridBagLayout_3.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout_3.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		titledPanel_4.getContentContainer().setLayout(gridBagLayout_3);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(10, 10, 10, 5);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		titledPanel_4.getContentContainer().add(scrollPane, gbc_scrollPane);
		
		taskTable = new JXTable();
		scrollPane.setViewportView(taskTable);
		
		JButton button_1 = new JButton("New");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onNewTask();
			}
		});
		GridBagConstraints gbc_button_1 = new GridBagConstraints();
		gbc_button_1.insets = new Insets(10, 0, 10, 10);
		gbc_button_1.anchor = GridBagConstraints.NORTHWEST;
		gbc_button_1.gridx = 1;
		gbc_button_1.gridy = 0;
		titledPanel_4.getContentContainer().add(button_1, gbc_button_1);
	}

	private ProductsTable productsPanel;
	private JXTable taskTable;
	private JLabel detailsLabel;
	private JLabel addressLabel;
	private JPanel relationsPanel;
}
