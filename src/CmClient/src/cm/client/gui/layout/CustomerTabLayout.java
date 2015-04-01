package cm.client.gui.layout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.MatteBorder;

import org.jdesktop.swingx.JXTable;

import cm.client.gui.common.BusyPanel;
import cm.util.Gbc;

@SuppressWarnings("serial")
public abstract class CustomerTabLayout extends JPanel
{
	protected JScrollPane scrollPane;
	protected JXTable table;
	protected BusyPanel searchPanel;

	protected abstract void onNewCustomer();
	protected abstract void onSearch();

	public CustomerTabLayout()
	{
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBorder(new MatteBorder(0, 0, 1, 0, (Color) Color.LIGHT_GRAY));
		panel.setBackground(Color.WHITE);
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel.add(panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JButton button = new JButton("New");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onNewCustomer();
			}
		});
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.anchor = GridBagConstraints.WEST;
		gbc_button.insets = new Insets(0, 0, 0, 5);
		gbc_button.gridx = 0;
		gbc_button.gridy = 0;
		panel_1.add(button, gbc_button);
		
		JButton button_1 = new JButton("Search");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onSearch();
			}
		});
		GridBagConstraints gbc_button_1 = new GridBagConstraints();
		gbc_button_1.anchor = GridBagConstraints.WEST;
		gbc_button_1.insets = new Insets(0, 0, 0, 5);
		gbc_button_1.gridx = 1;
		gbc_button_1.gridy = 0;
		panel_1.add(button_1, gbc_button_1);
		
		table = new JXTable();
		scrollPane = new JScrollPane( table );

		searchPanel = new BusyPanel();
		add(searchPanel, BorderLayout.CENTER);

		JPanel c = searchPanel.getContentPanel();
		GridBagLayout gridBagLayout = new GridBagLayout();
		c.setLayout( gridBagLayout );
		c.add( scrollPane, new Gbc( Gbc.BOTH, 0, 0 ).i( 10 ).XY( 1, 1 ) );
	}
}
