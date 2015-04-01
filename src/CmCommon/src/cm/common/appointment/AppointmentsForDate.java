package cm.common.appointment;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cm.common.entity.domain.Appointment;

public class AppointmentsForDate implements Serializable
{
	public AppointmentsForDate( AppointmentCalendar calendar, Date date )
	{
		this.calendar = calendar;
		this.date = date;
	}

	public void add( Appointment a ) {
		appointments.add( a );
	}

	public void remove( Appointment a ) {
		appointments.remove( a );
	}

	public AppointmentCalendar getCalendar() {
		return calendar;
	}

	public List<Appointment> getAppointments() {
		return appointments;
	}

	public Date getDate() {
		return date;
	}

	public Set<Time> getAvailableTimes()
	{
		// Create a set that represents the day with no appointments
		Set<Time> availableTimes = Time.sortedSet( calendar.getStartTime(), calendar.getEndTime(), calendar.getSlotDuration() );

		// Create a set representing each appointment and reservation then intersect
		// against the available set. This will leave us with a set which gives
		// us all free slots for the day
		for( Appointment a : appointments ) {
			availableTimes.removeAll( Time.sortedSet( a.startTime, a.endTime, calendar.getSlotDuration() ) );
		}

		for( Reservation r : calendar.getReservations( date ) ) {
			availableTimes.removeAll( Time.sortedSet( r.startTime, r.endTime, calendar.getSlotDuration() ) );
		}

		return availableTimes;
	}

	public Set<Time> getAvailableTimes( Time duration )
	{
		// Create a set that represents the day with no appointments
		Set<Time> availableTimes = Time.sortedSet( calendar.getStartTime(), calendar.getEndTime(), calendar.getSlotDuration() );

		// Create a set representing each appointment and perform and intersection
		// against the available set. This will leave us with a set which gives
		// us all free slots for the day
		for( Appointment a : appointments ) {
			availableTimes.removeAll( Time.sortedSet( new Time( a.startTime ).sub( duration.sub( calendar.getSlotDuration() ) ), new Time( a.endTime ), calendar.getSlotDuration() ) );
		}

		for( Reservation r : calendar.getReservations( date ) ) {
			availableTimes.removeAll( Time.sortedSet( r.startTime.sub( duration.sub( calendar.getSlotDuration() ) ), r.endTime, calendar.getSlotDuration() ) );
		}

		// Remove any slots that would result in the appointment running past endtime
		Time t = calendar.getSlotDuration();
		while( t.compareTo( duration ) == -1 )
		{
			availableTimes.remove( calendar.getEndTime().sub( t ) );
			t = t.add( calendar.getSlotDuration() );
		}

		return availableTimes;
	}

	private Date date;
	private List<Appointment> appointments = new LinkedList<Appointment>();
	private AppointmentCalendar calendar;

	private static final long serialVersionUID = -7630430809166887120L;
}
