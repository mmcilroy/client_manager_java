package cm.client.gui.appointments;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import cm.client.ClientError;
import cm.client.Session;
import cm.client.gui.customer.SimpleCustomerSearchDialog;
import cm.common.appointment.AppointmentError;
import cm.common.appointment.AppointmentsForDate;
import cm.common.appointment.Time;
import cm.common.entity.domain.Appointment;
import cm.common.entity.domain.Customer;
import cm.common.types.AppointmentType;
import cm.util.Gbc;
import cm.util.Util;

@SuppressWarnings( "serial" )
public class EditAppointmentDialog extends JDialog
{
	private JTextArea notesArea;

	{
		notesArea = new JTextArea( 10, 60 );
		notesArea.setLineWrap( true );
		notesArea.setWrapStyleWord( true );
		notesArea.setText( "" );
	}

	public EditAppointmentDialog( Session client, String calendarName, Date date, Time time ) throws AppointmentError, ClientError
	{
		this.client = client;
		this.calendarName = calendarName;
		this.time = time;
		this.datePicker.setDate( date );

		try {
			this.appointments = client.getCustomerService().searchForAppointments( calendarName, date );
		} catch( Exception e ) {
			;
		}

		display();
	}

	public EditAppointmentDialog( Session client, Appointment appointment ) throws AppointmentError, ClientError
	{
		this.client = client;
		this.calendarName = appointment.calendar;

		try {
			this.appointments = client.getCustomerService().searchForAppointments( calendarName, appointment.day );
		} catch( Exception e ) {
			;
		}

		this.time = new Time( appointment.startTime );
		this.appointment = appointment;
		this.appointments.remove( appointment );

		display();
	}

	public void display()
	{
		initComponents();
		initLayout();

		refreshTypes();
		refreshDurations();
		refreshTimes();

		initEvents();

		pack();
		setLocationRelativeTo( null );
		setVisible( true );
	}

	protected void setTime( Time time ) {
		this.time = time;
	}

	protected AppointmentsForDate getAppointments() {
		return appointments;
	}

	protected void refreshTypes()
	{
		internalUpdate = true;

		typeCombo.removeAll();
		for( AppointmentType t : AppointmentType.values() )
		{
			typeCombo.addItem( t.toString() );

			if( appointment != null && t == appointment.type ) {
				typeCombo.setSelectedItem( t.toString() );
			}
		}

		internalUpdate = false;
	}

	protected void refreshDurations()
	{
		internalUpdate = true;
		durationCombo.removeAllItems();

		try
		{
			Time[] times = appointments.getCalendar().getDurationsForType( getSelectedType() );
			if( times != null )
			{
				for( Time t : times )
				{
					durationCombo.addItem( t );

					if( appointment != null && appointment.getDuration().compareTo( t ) == 0 ) {
						durationCombo.setSelectedItem( t );
					}
				}
			}
		}
		catch( AppointmentError e )
		{
			;
		}

		internalUpdate = false;
	}

	protected void refreshTimes()
	{
		internalUpdate = true;
		timeCombo.removeAllItems();

		try
		{
			Set<Time> times = appointments.getAvailableTimes( getSelectedDuration() );
			if( times != null )
			{
				// Populate the combo and select the item closest to the time requested
				int nearestTime = 24*60;
				for( Time t : times )
				{
					timeCombo.addItem( t );
					int diff = Math.abs( t.sub( time ).toMinutes() );
					if( diff < nearestTime )
					{
						timeCombo.setSelectedItem( t );
						nearestTime = diff;
					}
				}
			}
		}
		catch( AppointmentError e )
		{
			;
		}

		internalUpdate = false;
	}

	private AppointmentType getSelectedType() throws AppointmentError
	{
		String type = (String)typeCombo.getSelectedItem();
		if( type == null ) {
			throw new AppointmentError( "Type not selected" );
		}

		return AppointmentType.valueOf( type );
	}

	private Time getSelectedDuration() throws AppointmentError
	{
		Time time = (Time)durationCombo.getSelectedItem();
		if( time == null ) {
			throw new AppointmentError( "Duration not selected" );
		}

		return time;
	}

	private Time getSelectedTime() throws AppointmentError
	{
		Time time = (Time)timeCombo.getSelectedItem();
		if( time == null ) {
			throw new AppointmentError( "Time not selected" );
		}

		return time;
	}

	private void initComponents()
	{
		setTitle( "Edit Appointment" );
		setResizable( false );
		setDefaultCloseOperation( DISPOSE_ON_CLOSE );
		setModalityType( ModalityType.APPLICATION_MODAL );
		setLayout( new GridBagLayout() );

		datePicker.setFormats( new String[] { "dd/MM/yyyy" } );
		clientLabel.setBorder( BorderFactory.createEtchedBorder() );
		notesArea.setBorder( BorderFactory.createEtchedBorder() );

		if( appointment != null )
		{
			datePicker.setDate( appointment.day );
			notesArea.setText( appointment.note );

			if( appointment.customerId != null )
			{
				try
				{
					Customer c = client.getCustomerService().searchForCustomersById( appointment.customerId );
					if( c != null ) {
						clientLabel.setText( Util.toCustomerName( c.personalDetails ) );
					}
				}
				catch( Exception e )
				{
					;
				}
			}
		}
	}

	private void initLayout()
	{
		add( new JLabel( "<html><b>Date" ), new Gbc( Gbc.EAST, 0, 1 ).XY( 0, 0 ).i( 10, 10, 2, 5 ) );
		add( datePicker, new Gbc( Gbc.HORIZONTAL, 1, 1 ).XY( 1, 0 ).i( 10, 5, 2, 10 ) );

		add( new JLabel( "<html><b>Time" ), new Gbc( Gbc.EAST, 2, 1 ).XY( 0, 0 ).i( 2, 10, 2, 5 ) );
		add( timeCombo, new Gbc( Gbc.HORIZONTAL, 3, 1 ).XY( 1, 0 ).i( 2, 5, 2, 10 ) );

		add( new JLabel( "<html><b>Type" ), new Gbc( Gbc.EAST, 0, 2 ).XY( 0, 0 ).i( 2, 10, 2, 5 ) );
		add( typeCombo, new Gbc( Gbc.HORIZONTAL, 1, 2 ).XY( 1, 0 ).i( 2, 5, 2, 10 ) );

		add( new JLabel( "<html><b>Duration" ), new Gbc( Gbc.EAST, 2, 2 ).XY( 0, 0 ).i( 2, 10, 2, 5 ) );
		add( durationCombo, new Gbc( Gbc.HORIZONTAL, 3, 2 ).XY( 1, 0 ).i( 2, 5, 2, 10 ) );

		add( new JLabel( "<html><b>Client" ), new Gbc( Gbc.EAST, 0, 3 ).XY( 0, 0 ).i( 2, 10, 2, 5 ) );

		{
			JPanel panel = new JPanel()
			{ {
				setLayout( new GridBagLayout() );
				add( clientLabel, new Gbc( Gbc.HORIZONTAL, 0, 0 ).XY( 1, 0 ) );
				add( editButton, new Gbc( Gbc.EAST, 1, 0 ).XY( 0, 0 ) );
				add( clearButton, new Gbc( Gbc.EAST, 2, 0 ).XY( 0, 0 ).i( 0 ) );
			} };

			add( panel, new Gbc( Gbc.HORIZONTAL, 1, 3 ).XY( 1, 0 ).wh( 3, 1 ).i( 2, 5, 2, 10 ) );
		}

		add( new JLabel( "<html><b>Notes" ), new Gbc( Gbc.EAST, 0, 4 ).XY( 0, 0 ).a( Gbc.NORTH ).i( 2, 10, 2, 5 ) );
		add( notesArea, new Gbc( Gbc.BOTH, 1, 4 ).wh( 3, 1 ).i( 2, 5, 2, 10 ) );

		{
			JPanel panel = new JPanel()
			{ {
				add( okButton );
	
				if( appointment != null ) {
					add( deleteButton );
				}
	
				add( cancelButton );
			} };
	
			add( panel, new Gbc( Gbc.CENTER, 0, 5 ).XY( 1, 0 ).wh( 4, 0 ) );
		}
	}

	private void initEvents()
	{
		datePicker.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				onDateSelected( datePicker.getDate() );
			} } );

		durationCombo.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				onDurationSelected();
			} } );

		typeCombo.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				onTypeSelected();
			} } );

		timeCombo.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				onTimeSelected();
			} } );

		okButton.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				onOk();
			} } );

		deleteButton.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				onDelete();
			} } );

		cancelButton.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				onCancel();
			} } );

		editButton.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				onEdit();
			} } );

		clearButton.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				onClear();
			} } );
	}

	protected void onDateSelected( Date date )
	{
		try
		{
			appointments = client.getCustomerService().searchForAppointments( calendarName, date );
			if( appointment != null ) {
				appointments.remove( appointment );
			}
		}
		catch( Exception e )
		{
			;
		}

		refreshTimes();
	}

	protected void onTypeSelected()
	{
		if( !internalUpdate )
		{
			refreshDurations();
			refreshTimes();
		}
	}

	protected void onDurationSelected()
	{
		if( !internalUpdate ) {
			refreshTimes();
		}
	}

	protected void onTimeSelected()
	{
		try
		{
			if( !internalUpdate ) {
				time = getSelectedTime();
			}
		}
		catch( Exception e )
		{
			;
		}
	}

	protected void onOk()
	{
		try
		{
			if( appointment == null ) {
				appointment = new Appointment();
			}

			appointment.type = getSelectedType();
			appointment.calendar = calendarName;
			appointment.day = appointments.getDate();
			appointment.startTime = getSelectedTime().toDate();
			appointment.endTime = getSelectedTime().add( getSelectedDuration() ).toDate();
			appointment.note = notesArea.getText();

			if( customer != null ) {
				appointment.customerId = customer.personalDetails.id;
			}

			if( appointment.id != null && appointment.id > 0 ) {
				client.getCustomerService().update( appointment );
			} else {
				client.getCustomerService().createAppointment( appointment );
			}

			dispose();
		}
		catch( Exception e )
		{
			error( "An error occured", e );
		}
	}

	protected void onDelete()
	{
		if( appointment != null && appointment.id > 0 )
		{
			appointment.live = false;
			
			try
			{
				client.getCustomerService().update( appointment );
				dispose();
			}
			catch( Exception e )
			{
				error( "An error occured", e );
			}
		}
	}

	protected void onCancel() {
		dispose();
	}

	protected void onEdit()
	{
		SimpleCustomerSearchDialog dialog = new SimpleCustomerSearchDialog( client );
		if( dialog.isOk() )
		{
			customer = dialog.getCustomer();
			clientLabel.setText( Util.toCustomerName( customer.personalDetails ) );
		}
	}

	protected void onClear()
	{
		customer = null;
		clientLabel.setText( " " );
	}

	private void error( String text, Exception e )
	{
		ErrorInfo ei = new ErrorInfo( "Error", text, null, null, e, null, null );
		JXErrorPane.showDialog( this, ei );
	}

	private Session client;
	private Customer customer;
	private AppointmentsForDate appointments;
	private Appointment appointment;
	private String calendarName;
	private Time time;

	private JXDatePicker datePicker = new JXDatePicker( new Date() );
	private JComboBox<Time> timeCombo = new JComboBox<Time>();
	private JComboBox<Time> durationCombo = new JComboBox<Time>();
	private JComboBox<String> typeCombo = new JComboBox<String>();
	private JButton okButton = new JButton( "<html>Ok" );
	private JButton deleteButton = new JButton( "<html>Delete" );
	private JButton cancelButton = new JButton( "<html>Cancel" );
	private JButton editButton = new JButton( "<html>Edit" );
	private JButton clearButton = new JButton( "<html>Clear" );
	private JLabel clientLabel = new JLabel( " " );
	private boolean internalUpdate = false;
}
