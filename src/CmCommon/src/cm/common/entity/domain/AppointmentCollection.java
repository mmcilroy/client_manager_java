package cm.common.entity.domain;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class AppointmentCollection extends LinkedHashMap<Integer, Appointment> implements Serializable
{
	public void add( Appointment a ) {
		put( a.id, a );
	}

	private static final long serialVersionUID = 6902716804757027797L;
}
