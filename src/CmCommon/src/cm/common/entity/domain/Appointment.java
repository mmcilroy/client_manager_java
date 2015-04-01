package cm.common.entity.domain;

import java.io.Serializable;
import java.util.Date;

import cm.common.appointment.Time;
import cm.common.entity.Entity;
import cm.common.types.AppointmentType;

public class Appointment extends Entity implements Serializable
{
	public AppointmentType type;
	public String calendar;
	public Date day;
	public Date startTime;
	public Date endTime;
	public String note;
	public Integer customerId;

	public Time getDuration() {
		return new Time( endTime ).sub( new Time( startTime ) );
	}

	public boolean equals( Object o )
	{
		if( o instanceof Appointment )
		{
			Appointment a = (Appointment)o;

			return( startTime.compareTo( a.startTime ) == 0 &&
					endTime.compareTo( a.endTime ) == 0 &&
					type.compareTo( a.type ) == 0 );
		}

		return false;
	}

	private static final long serialVersionUID = -5753646178486439577L;
}
