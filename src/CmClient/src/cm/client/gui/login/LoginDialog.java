package cm.client.gui.login;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;

import cm.client.gui.common.OkCancelDialog2;

@SuppressWarnings("serial")
public class LoginDialog extends OkCancelDialog2
{
	public LoginDialog()
	{
		setTitle( "Login" );
		setModalityType( ModalityType.APPLICATION_MODAL );
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		//setSize( 350, 250 );
		setLocationRelativeTo( null );
		setResizable( false );

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblUsername = new JLabel("<html><b>Username");
		GridBagConstraints gbc_lblUsername = new GridBagConstraints();
		gbc_lblUsername.anchor = GridBagConstraints.SOUTHEAST;
		gbc_lblUsername.insets = new Insets(0, 0, 5, 5);
		gbc_lblUsername.gridx = 0;
		gbc_lblUsername.gridy = 0;
		panel.add(lblUsername, gbc_lblUsername);
		
		JLabel lblPassword = new JLabel("<html><b>Password");
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.anchor = GridBagConstraints.NORTHEAST;
		gbc_lblPassword.insets = new Insets(0, 0, 0, 5);
		gbc_lblPassword.gridx = 0;
		gbc_lblPassword.gridy = 1;
		panel.add(lblPassword, gbc_lblPassword);
		
		usernameField = new JTextField();
		GridBagConstraints gbc_usernameField = new GridBagConstraints();
		gbc_usernameField.anchor = GridBagConstraints.SOUTHWEST;
		gbc_usernameField.insets = new Insets(0, 0, 5, 0);
		gbc_usernameField.gridx = 1;
		gbc_usernameField.gridy = 0;
		panel.add(usernameField, gbc_usernameField);
		usernameField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setColumns(10);
		GridBagConstraints gbc_getPasswordField = new GridBagConstraints();
		gbc_getPasswordField.anchor = GridBagConstraints.NORTHWEST;
		gbc_getPasswordField.gridx = 1;
		gbc_getPasswordField.gridy = 1;
		panel.add(passwordField, gbc_getPasswordField);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new MatteBorder(0, 0, 1, 0, (Color) Color.LIGHT_GRAY));
		panel_1.setBackground(Color.WHITE);
		getContentPane().add(panel_1, BorderLayout.NORTH);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWeights = new double[]{1.0};
		gbl_panel_1.rowWeights = new double[]{1.0};
		panel_1.setLayout(gbl_panel_1); 
		
		JLabel lblNewLabel = new JLabel("<html><h3 color='red'>Enter login details...");
		lblNewLabel.setOpaque(true);
		lblNewLabel.setBackground(Color.WHITE);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(10, 10, 10, 0);
		gbc_lblNewLabel.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		panel_1.add(lblNewLabel, gbc_lblNewLabel);
	}

	public String getUsername()
	{
		return usernameField.getText();
	}

	public String getPassword()
	{
		return new String( passwordField.getPassword() );
	}

	private JTextField usernameField;
	private JPasswordField passwordField;
}
