package cm.common.appointment;

public class Reservation
{
	public Reservation( Time startTime, Time endTime )
	{
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public Time startTime;
	public Time endTime;
}
