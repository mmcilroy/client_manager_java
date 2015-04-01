package cm.client.gui.layout;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.jdesktop.swingx.JXTable;

import cm.client.gui.customer.PersonalDetailsComponentFactory;

@SuppressWarnings("serial")
public class CustomerSearchPanelLayout extends JPanel
{
	public CustomerSearchPanelLayout()
	{
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[]{1.0};
		gridBagLayout.rowWeights = new double[]{1.0};
		setLayout(gridBagLayout);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(10, 10, 10, 10);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{46, 0, 0};
		gbl_panel.rowHeights = new int[]{14, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblNewLabel = new JLabel("Forename");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel.anchor = GridBagConstraints.NORTH;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		panel.add(lblNewLabel, gbc_lblNewLabel);
		
		forenameField = PersonalDetailsComponentFactory.newForenameComponent();
		GridBagConstraints gbc_forenameField = new GridBagConstraints();
		gbc_forenameField.insets = new Insets(0, 0, 5, 0);
		gbc_forenameField.fill = GridBagConstraints.HORIZONTAL;
		gbc_forenameField.gridx = 1;
		gbc_forenameField.gridy = 0;
		panel.add(forenameField, gbc_forenameField);
		forenameField.setColumns(20);
		
		JLabel lblSurname = new JLabel("Surname");
		lblSurname.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_lblSurname = new GridBagConstraints();
		gbc_lblSurname.anchor = GridBagConstraints.EAST;
		gbc_lblSurname.insets = new Insets(0, 0, 5, 5);
		gbc_lblSurname.gridx = 0;
		gbc_lblSurname.gridy = 1;
		panel.add(lblSurname, gbc_lblSurname);
		
		surnameField = PersonalDetailsComponentFactory.newSurnameComponent();
		surnameField.setColumns(20);
		GridBagConstraints gbc_surnameField = new GridBagConstraints();
		gbc_surnameField.insets = new Insets(0, 0, 5, 0);
		gbc_surnameField.fill = GridBagConstraints.HORIZONTAL;
		gbc_surnameField.gridx = 1;
		gbc_surnameField.gridy = 1;
		panel.add(surnameField, gbc_surnameField);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onSearch();
			}
		});
		GridBagConstraints gbc_btnSearch = new GridBagConstraints();
		gbc_btnSearch.insets = new Insets(0, 0, 5, 0);
		gbc_btnSearch.anchor = GridBagConstraints.EAST;
		gbc_btnSearch.gridx = 1;
		gbc_btnSearch.gridy = 2;
		panel.add(btnSearch, gbc_btnSearch);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(null);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 3;
		panel.add(scrollPane, gbc_scrollPane);
		
		table = new JXTable();
		scrollPane.setViewportView(table);

	}

	public JXTable getTable() {
		return table;
	}

	public JTextField getForenameField() {
		return forenameField;
	}

	public JTextField getSurnameField() {
		return surnameField;
	}

	protected void onSearch() {
		;
	}

	private JTextField forenameField;
	private JTextField surnameField;
	private JXTable table;
}
