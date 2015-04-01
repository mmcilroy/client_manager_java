package cm.common.appointment;

@SuppressWarnings("serial")
public class AppointmentError extends Exception
{
	public AppointmentError( String msg ) {
		super( msg );
	}

	public AppointmentError( String msg, Exception e ) {
		super( msg, e );
	}
}
