package cm.client.gui.layout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.MatteBorder;

import org.jdesktop.swingx.JXDatePicker;

import cm.client.gui.appointments.WeekViewPanel;
import cm.client.gui.common.BusyPanel;
import cm.util.Gbc;

@SuppressWarnings("serial")
public abstract class AppointmentTabLayout extends JPanel
{
	protected JScrollPane scrollPane;
	protected BusyPanel searchPanel;
	private JXDatePicker datePicker;
	private JComboBox<String> calendarCombo;
	private WeekViewPanel weekViewPanel;
	private SimpleDateFormat dateFormat = new SimpleDateFormat( "dd/MM/yyyy" );

	protected void onSelection( Date date, String calendar ) {
		;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public AppointmentTabLayout()
	{
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBorder(new MatteBorder(0, 0, 1, 0, (Color) Color.LIGHT_GRAY));
		panel.setBackground(Color.WHITE);
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel.add(panel_1);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblDate = new JLabel("Date");
		panel_1.add(lblDate);
		
		datePicker = new JXDatePicker();
		datePicker.setFormats( dateFormat );
		datePicker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onSelection( datePicker.getDate(), (String)calendarCombo.getSelectedItem() );
			}
		});
		panel_1.add(datePicker);
		
		JLabel lblCalendar = new JLabel("  Calendar");
		panel.add(lblCalendar);
		
		calendarCombo = new JComboBox<String>();
		calendarCombo.setModel(new DefaultComboBoxModel(new String[] {
			"","TENNIELLE","CIARAN"}));

		calendarCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onSelection( datePicker.getDate(), (String)calendarCombo.getSelectedItem() );
			}
		});
		panel.add(calendarCombo);
		scrollPane = new JScrollPane( );

		searchPanel = new BusyPanel();
		add(searchPanel, BorderLayout.CENTER);

		JPanel c = searchPanel.getContentPanel();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[]{0.0};
		gridBagLayout.columnWeights = new double[]{1.0};
		c.setLayout( gridBagLayout );
		c.add( scrollPane, new Gbc( Gbc.BOTH, 0, 0 ).i( 10 ).XY( 1, 1 ) );
		
		weekViewPanel = new WeekViewPanel();
		scrollPane.setViewportView(weekViewPanel);
	}

	public WeekViewPanel getWeekViewPanel() {
		return weekViewPanel;
	}
	public JXDatePicker getDatePicker() {
		return datePicker;
	}
	public JComboBox<String> getCalendarCombo() {
		return calendarCombo;
	}
}
