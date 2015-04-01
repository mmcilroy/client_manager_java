package cm.client.gui.layout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.MatteBorder;

import org.jdesktop.swingx.JXTable;

import cm.client.gui.common.BusyPanel;
import cm.util.Gbc;

@SuppressWarnings("serial")
public abstract class TaskTabLayout extends JPanel
{
	protected JScrollPane scrollPane;
	protected JXTable table;
	protected BusyPanel searchPanel;
	private JComboBox<String> businessOwnerCombo;

	protected void onOverdue() {
		;
	}

	protected void onToday() {
		;
	}

	protected void onTomorrow() {
		;
	}


	public TaskTabLayout()
	{
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBorder(new MatteBorder(0, 0, 1, 0, (Color) Color.LIGHT_GRAY));
		panel.setBackground(Color.WHITE);
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		
		JLabel lblBusinessownder = new JLabel("BusinessOwner");
		panel.add(lblBusinessownder);
		
		businessOwnerCombo = new JComboBox<String>();
		panel.add(businessOwnerCombo);
		
		JLabel label = new JLabel("  ");
		panel.add(label);
		
		JButton overdueButton = new JButton("Overdue");
		overdueButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onOverdue();
			}
		});
		panel.add(overdueButton);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel.add(panel_1);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton todayButton = new JButton("Today");
		todayButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onToday();
			}
		});
		panel_1.add(todayButton);
		
		JButton tomorrowButton = new JButton("Tomorrow");
		tomorrowButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onTomorrow();
			}
		});
		panel_1.add(tomorrowButton);

		table = new JXTable();
		scrollPane = new JScrollPane( table );

		searchPanel = new BusyPanel();
		add(searchPanel, BorderLayout.CENTER);

		JPanel c = searchPanel.getContentPanel();
		GridBagLayout gridBagLayout = new GridBagLayout();
		c.setLayout( gridBagLayout );
		c.add( scrollPane, new Gbc( Gbc.BOTH, 0, 0 ).i( 10 ).XY( 1, 1 ) );
	}
	public JComboBox<String> getBusinessOwnerCombo() {
		return businessOwnerCombo;
	}
}
