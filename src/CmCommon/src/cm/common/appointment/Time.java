package cm.common.appointment;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.TreeSet;

public class Time implements Comparable<Time>, Serializable
{
	public Time() {
		;
	}

	public Time( int h, int m ) throws AppointmentError
	{
		if( h < 0 || h > 23 ) {
			throw new AppointmentError( "Invalid hour value " + h );
		}

		if( m < 0 || m > 59 ) {
			throw new AppointmentError( "Invalid minute value " + m );
		}

		hour = h;
		minute = m;
	}

	public Time( Time t )
	{
		hour = t.hour;
		minute = t.minute;
	}

	public Time( Date d )
	{
		Calendar cal = new GregorianCalendar();
		cal.setTime( d );
		hour = cal.get( Calendar.HOUR_OF_DAY );
		minute = cal.get( Calendar.MINUTE );
	}

	public Time add( Time t ) {
		return new Time( toMinutes() + t.toMinutes() );
	}

	public Time sub( Time t ) {
		return new Time( toMinutes() - t.toMinutes() );
	}

	public int getHour() {
		return hour;
	}

	public int getMinutes() {
		return minute;
	}

	public int compareTo( Time t )
	{
		int a = this.toMinutes();
		int b = t.toMinutes();

		if( a == b ) {
			return 0;
		} else if( a < b ) {
			return -1;
		} else {
			return 1;
		}
	}

	public int toMinutes() {
		return hour * 60 + minute;
	}

	public String toString() {
		return String.format( "%02d:%02d", hour, minute );
	}

	public Date toDate()
	{
		Calendar cal = new GregorianCalendar();
		cal.set( Calendar.HOUR_OF_DAY, hour );
		cal.set( Calendar.MINUTE, minute );
		return cal.getTime();
	}

	public Time fromMinutes( int minutes )
	{
		this.hour = minutes / 60;
		this.minute = minutes % 60;
		return this;
	}

	public static Set<Time> sortedSet( Time startTime, Time endTime, Time duration )
	{
		Set<Time> set = new TreeSet<Time>();

		Time t = new Time( startTime );
		while( t.compareTo( endTime ) != 0 )
		{
			set.add( new Time( t ) );
			t = t.add( duration );
		}

		return set;
	}

	public static Set<Time> sortedSet( Date startTime, Date endTime, Time duration ) {
		return sortedSet( new Time( startTime ), new Time( endTime ), duration );
	}

	private Time( int m ) {
		fromMinutes( m );
	}

	private int hour = 0;
	private int minute = 0;

	private static final long serialVersionUID = 1394198837660270833L;
}
