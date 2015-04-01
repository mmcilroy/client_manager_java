package cm.client.gui.layout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import org.jdesktop.swingx.JXBusyLabel;

@SuppressWarnings("serial")
public abstract class WizardDialogLayout extends JDialog {

	protected JButton prevButton;
	protected JButton nextButton;
	protected JLabel titleLabel;
	protected JPanel wizardContentPanel;
	protected JXBusyLabel busyLabel;

	protected abstract void onPrev();
	protected abstract void onNext();

	public WizardDialogLayout() {
		getContentPane().setMinimumSize(new Dimension(400, 0));
		setResizable(false);
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBorder(new MatteBorder(1, 0, 0, 0, (Color) Color.LIGHT_GRAY));
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		prevButton = new JButton("Prev");
		prevButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onPrev();
			}
		});
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(prevButton);
		
		nextButton = new JButton("Next");
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onNext();
			}
		});
		panel.add(nextButton);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new MatteBorder(0, 0, 1, 0, (Color) Color.LIGHT_GRAY));
		panel_1.setBackground(Color.WHITE);
		getContentPane().add(panel_1, BorderLayout.NORTH);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		titleLabel = new JLabel("Title label");
		titleLabel.setOpaque(true);
		titleLabel.setBackground(Color.WHITE);
		GridBagConstraints gbc_titleLabel = new GridBagConstraints();
		gbc_titleLabel.insets = new Insets(5, 5, 5, 5);
		gbc_titleLabel.weighty = 1.0;
		gbc_titleLabel.weightx = 1.0;
		gbc_titleLabel.fill = GridBagConstraints.BOTH;
		gbc_titleLabel.gridx = 0;
		gbc_titleLabel.gridy = 0;
		panel_1.add(titleLabel, gbc_titleLabel);
		
		busyLabel = new JXBusyLabel();
		GridBagConstraints gbc_busyLabel = new GridBagConstraints();
		gbc_busyLabel.insets = new Insets(5, 5, 5, 5);
		gbc_busyLabel.gridx = 1;
		gbc_busyLabel.gridy = 0;
		panel_1.add(busyLabel, gbc_busyLabel);
		
		wizardContentPanel = new JPanel();
		getContentPane().add(wizardContentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_wizardContentPanel = new GridBagLayout();
		gbl_wizardContentPanel.columnWidths = new int[]{0};
		gbl_wizardContentPanel.rowHeights = new int[]{0};
		gbl_wizardContentPanel.columnWeights = new double[]{Double.MIN_VALUE};
		gbl_wizardContentPanel.rowWeights = new double[]{Double.MIN_VALUE};
		wizardContentPanel.setLayout(gbl_wizardContentPanel);

	}
}
