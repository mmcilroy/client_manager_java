package cm.client.gui.layout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

import cm.client.gui.common.BusyPanel;

@SuppressWarnings("serial")
public class CustomerDialogLayout extends JPanel
{
	private BusyPanel contentPanel;
	private JLabel titleLabel;
	private JButton deleteButton;

	protected void onHome() {
		;
	}

	protected void onDelete() {
		;
	}

	public CustomerDialogLayout()
	{
		setLayout(new BorderLayout(0, 0));
		
		JPanel topPanel = new JPanel();
		topPanel.setBorder(new MatteBorder(0, 0, 1, 0, (Color) Color.LIGHT_GRAY));
		topPanel.setBackground(Color.WHITE);
		add(topPanel, BorderLayout.NORTH);
		GridBagLayout gbl_topPanel = new GridBagLayout();
		gbl_topPanel.columnWidths = new int[]{391, 0, 59, 0};
		gbl_topPanel.rowHeights = new int[]{23, 0};
		gbl_topPanel.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_topPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		topPanel.setLayout(gbl_topPanel);
		
		titleLabel = new JLabel("");
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_titleLabel = new GridBagConstraints();
		gbc_titleLabel.fill = GridBagConstraints.BOTH;
		gbc_titleLabel.insets = new Insets(0, 10, 0, 10);
		gbc_titleLabel.gridx = 0;
		gbc_titleLabel.gridy = 0;
		topPanel.add(titleLabel, gbc_titleLabel);
		
		JButton homeButton = new JButton("Home");
		homeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onHome();
			}
		});
		
		deleteButton = new JButton("Delete");
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onDelete();
			}
		});
		GridBagConstraints gbc_deleteButton = new GridBagConstraints();
		gbc_deleteButton.insets = new Insets(0, 0, 0, 5);
		gbc_deleteButton.gridx = 1;
		gbc_deleteButton.gridy = 0;
		topPanel.add(deleteButton, gbc_deleteButton);
		GridBagConstraints gbc_homeButton = new GridBagConstraints();
		gbc_homeButton.insets = new Insets(0, 0, 0, 10);
		gbc_homeButton.gridx = 2;
		gbc_homeButton.gridy = 0;
		topPanel.add(homeButton, gbc_homeButton);
		
		contentPanel = new BusyPanel();
		add(contentPanel, BorderLayout.CENTER);
	}

	public BusyPanel getContentPanel() {
		return contentPanel;
	}

	public JLabel getTitleLabel() {
		return titleLabel;
	}

	public JButton getDeleteButton() {
		return deleteButton;
	}
}
