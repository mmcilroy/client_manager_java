package cm.client.gui.appointments;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cm.client.Session;
import cm.client.gui.common.BusyPanel;
import cm.common.appointment.AppointmentCalendar;
import cm.common.appointment.AppointmentsForDate;
import cm.common.appointment.Time;
import cm.common.entity.domain.Appointment;
import cm.common.entity.domain.Customer;
import cm.common.logger.Logger;
import cm.util.Gbc;

@SuppressWarnings("serial")
public class WeekViewPanel extends BusyPanel
{
	public WeekViewPanel() {
		getContentPanel().setLayout( new GridBagLayout() );
	}

	public void setDate( Date date ) {
		this.date = date;
	}

	public void setCalendar( String calendar ) {
		this.calendar = calendar;
	}

	public void display( final Session client )
	{
		this.client = client;

		new Thread( new Runnable()
		{
			public void run()
			{
				setBusy( true );
				refreshAppointments();

				JPanel panel = getContentPanel();
				panel.removeAll();
		
				// Add an empty label for every slot to ensure button heights are consistent
				for( int y = 0 ; y < appointments[0].getCalendar().getNumSlots(); y++ ) {
					panel.add( new JLabel( " " ), new Gbc( Gbc.NONE, 0, y++ ).XY( 0, 0 ) );
				}
		
				for( int x=1 ; x<=appointments.length; x++ )
				{
					AppointmentsForDate app = appointments[x-1];
					AppointmentCalendar cal = app.getCalendar(); 
		
					JLabel l = new JLabel( "<html><b>" + dateFormat.format( app.getDate() ) );
					l.setHorizontalAlignment( JLabel.CENTER );
					panel.add( l, new Gbc( Gbc.HORIZONTAL, x, 0 ).XY( 1, 0 ).i( 0 ) );
			
					// Display available times
					for( Time time: app.getAvailableTimes() )
					{
						int y = ( time.sub( cal.getStartTime() ).toMinutes() ) / cal.getSlotDuration().toMinutes() + 1;
						panel.add( new BookButton( "<html><p align='center'><b>" + time + "-" + time.add( cal.getSlotDuration() ) + " <font color='red'>Available", app.getDate(), time ), new Gbc( Gbc.BOTH, x, y ).XY( 1, 1 ).i( 0 ) );			
					}
		
					// Display appointments
					for( Appointment a : app.getAppointments() )
					{
						String clientName = "";
						if( a.customerId != null )
						{
							try
							{
								Customer c = client.getCustomerService().searchForCustomersById( a.customerId );
								if( c != null ) {
									clientName = String.format( "%c %s", c.personalDetails.forename.charAt( 0 ), c.personalDetails.surname );						
								}
							}
							catch( Exception e )
							{
								clientName = "???";
							}
						}
		
						try
						{
							Time st = new Time( a.startTime );
							Time et = new Time( a.endTime );
		
							int y = ( st.sub( cal.getStartTime() ) ).toMinutes() / cal.getSlotDuration().toMinutes() + 1;
							int h = ( et.sub( st ) ).toMinutes() / cal.getSlotDuration().toMinutes();
							panel.add( new EditButton( "<html><p align='center'><b>" + st + "-" + et + " <font color='blue'>" + clientName + "</font></b><br/>" + a.type.toString(), a ), new Gbc( Gbc.BOTH, x, y ).XY( 1, 1 ).wh( 1, h ).i( 0 ) );
						}
						catch( Exception e )
						{
							Logger.getInstance().error( "Failed to build appointments for day " + x, e );
						}
					}
				}
		
				setBusy( false );
			}
		} ).start();
	}

	private void refreshAppointments()
	{
		Calendar c = new GregorianCalendar();
		c.setTime( date );

		// Go to Monday
		if( c.get( Calendar.DAY_OF_WEEK ) == Calendar.SUNDAY )
		{
			c.add( Calendar.DAY_OF_WEEK, 1 );
		}
		else
		{
			while( c.get( Calendar.DAY_OF_WEEK ) != Calendar.MONDAY ) {
				c.add( Calendar.DAY_OF_WEEK, -1 );
			}
		}

		// Get all appointments for the current week
		try
		{
			for( int i=0; i<appointments.length; i++ )
			{
				appointments[i] = client.getCustomerService().searchForAppointments( calendar, c.getTime() );
				c.add( Calendar.DAY_OF_MONTH, 1 );
			}
		}
		catch( Exception e )
		{
			Logger.getInstance().error( "Failed to refresh appointments", e );
		}
	}

	private class BookButton extends JButton
	{
		BookButton( String title, final Date date, final Time time )
		{
			super( title );

			this.addActionListener( new ActionListener()
			{
				public void actionPerformed( ActionEvent e )
				{
					try {
						new EditAppointmentDialog( client, calendar, date, time );
					} catch( Exception ae ) {
						Logger.getInstance().error( "Failed to book appointment", ae );
					}
				}
			} );
		}
	}

	private class EditButton extends JButton
	{
		EditButton( String title, final Appointment appointment )
		{
			super( title );

			this.addActionListener( new ActionListener()
			{
				public void actionPerformed( ActionEvent e )
				{
					try {
						new EditAppointmentDialog( client, appointment );
					} catch( Exception ae ) {
						Logger.getInstance().error( "Failed to edit appointment", ae );
					}
				}
			} );
		}
	}

	private Session client;
	private AppointmentsForDate[] appointments = new AppointmentsForDate[6];
	private Date date;
	private String calendar;

	private SimpleDateFormat dateFormat = new SimpleDateFormat( "E dd/MM/yyyy" );
}
