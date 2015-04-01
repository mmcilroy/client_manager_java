package cm.common.appointment;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cm.common.logger.Logger;
import cm.common.types.AppointmentType;

public class AppointmentCalendar implements Serializable
{
	public AppointmentCalendar( String name ) throws AppointmentError
	{
		this.name = name;
		this.startTime = new Time( 9,30 );
		this.endTime = new Time( 21,30 );
		this.slotDuration = new Time( 0,15 );
	}

	public String getName() {
		return name;
	}

	public Time getStartTime() {
		return startTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public int getNumSlots() {
		return endTime.sub( startTime ).toMinutes() / slotDuration.toMinutes();
	}

	public Time getSlotDuration() {
		return slotDuration;
	}

	public Time[] getDurationsForType( AppointmentType type ) {
		return typeToDurationMap.get( type );
	}

	public List<Reservation> getReservations( Date date )
	{
		List<Reservation> reservations = new LinkedList<Reservation>();

		Calendar c = new GregorianCalendar();
		c.setTime( date );

		try
		{
			if( name.compareTo( "TENNIELLE" ) == 0 )
			{
				if( c.get( Calendar.DAY_OF_WEEK ) == Calendar.MONDAY )
				{
					reservations.add( new Reservation( new Time( 9,30 ), new Time( 12,00 ) ) );
				}
				else
				if( c.get( Calendar.DAY_OF_WEEK ) == Calendar.TUESDAY ||
					c.get( Calendar.DAY_OF_WEEK ) == Calendar.THURSDAY ||
					c.get( Calendar.DAY_OF_WEEK ) == Calendar.FRIDAY )
				{
					reservations.add( new Reservation( new Time( 17,00 ), new Time( 21,30 ) ) );
				}
				else
				if( c.get( Calendar.DAY_OF_WEEK ) == Calendar.WEDNESDAY )
				{
					reservations.add( new Reservation( new Time( 9,30 ), new Time( 18,00 ) ) );
				}
				else
				{
					reservations.add( new Reservation( new Time( 9,30 ), new Time( 21,30 ) ) );
				}
			}
			else
			if( name.compareTo( "CIARAN" ) == 0 )
			{
				if( c.get( Calendar.DAY_OF_WEEK ) == Calendar.MONDAY )
				{
					reservations.add( new Reservation( new Time( 9,30 ), new Time( 12,00 ) ) );
				}
				else
				if( c.get( Calendar.DAY_OF_WEEK ) == Calendar.TUESDAY ||
					c.get( Calendar.DAY_OF_WEEK ) == Calendar.THURSDAY ||
					c.get( Calendar.DAY_OF_WEEK ) == Calendar.FRIDAY )
				{
					reservations.add( new Reservation( new Time( 17,00 ), new Time( 21,30 ) ) );
				}
				else
				if( c.get( Calendar.DAY_OF_WEEK ) == Calendar.WEDNESDAY )
				{
				}
				else
				{
					reservations.add( new Reservation( new Time( 9,30 ), new Time( 21,30 ) ) );
				}
			}
		}
		catch( AppointmentError e )
		{
			Logger.getInstance().error( "Failed to remove unavailable time slots", e );
		}

		return reservations;
	}

	private boolean isFirstWorkingDay( Date date )
	{
		GregorianCalendar c1 = new GregorianCalendar();
		c1.setTime( date );
		c1.set( Calendar.DAY_OF_MONTH, 1 );

		while( c1.get( Calendar.DAY_OF_WEEK ) != Calendar.MONDAY ) {
			c1.add( Calendar.DAY_OF_MONTH, 1 );
		}

		GregorianCalendar c2 = new GregorianCalendar();
		c2.setTime( date );

		return c1.get( Calendar.DAY_OF_MONTH ) == c2.get( Calendar.DAY_OF_MONTH );
	}

	private boolean isLastWorkingDay( Date date )
	{
		GregorianCalendar c1 = new GregorianCalendar();
		c1.setTime( date );
		c1.set( Calendar.DAY_OF_MONTH, c1.getActualMaximum( Calendar.DAY_OF_MONTH ) );

		while( c1.get( Calendar.DAY_OF_WEEK ) != Calendar.FRIDAY ) {
			c1.add( Calendar.DAY_OF_MONTH, -1 );
		}
		
		GregorianCalendar c2 = new GregorianCalendar();
		c2.setTime( date );

		return c1.get( Calendar.DAY_OF_MONTH ) == c2.get( Calendar.DAY_OF_MONTH );
	}

	private String name;
	private Time startTime;
	private Time endTime;
	private Time slotDuration;

	private static Map<AppointmentType,Time[]> typeToDurationMap = new HashMap<AppointmentType,Time[]>();

	static
	{
		try
		{
			typeToDurationMap.put( AppointmentType.CREDIT_CARD_OPENING, new Time[] {
				new Time( 1,00 ) } );
			typeToDurationMap.put( AppointmentType.CURRENT_ACCOUNT_OPENING, new Time[] {
				new Time( 1,00 ) } );
			typeToDurationMap.put( AppointmentType.SAVINGS_REVIEW, new Time[] {
				new Time( 1,00 ) } );
			typeToDurationMap.put( AppointmentType.MORTGAGE_REVIEW, new Time[] {
				new Time( 2,00 ) } );
			typeToDurationMap.put( AppointmentType.FFNI_REVIEW, new Time[] {
					new Time( 2,00 ) } );
			typeToDurationMap.put( AppointmentType.OTHER, new Time[] {
				new Time( 0,15 ),
				new Time( 0,30 ),
				new Time( 0,45 ),
				new Time( 1,00 ),
				new Time( 1,15 ),
				new Time( 1,30 ),
				new Time( 2,00 ),
				new Time( 3,00 ),
				new Time( 4,00 ),
				new Time( 5,00 ),
				new Time( 6,00 ),
				new Time( 7,00 ),
				new Time( 8,00 ),
				new Time( 9,00 ),
				new Time( 10,00 ),
				new Time( 11,00 ),
				new Time( 12,00 )} );
		}
		catch( Exception e )
		{
			Logger.getInstance().error( "Failed during typeToDurationMap initialization", e );
		}
	}

	private static final long serialVersionUID = 2183531156492934104L;
}
