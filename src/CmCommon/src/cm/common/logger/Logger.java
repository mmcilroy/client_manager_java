package cm.common.logger;
import java.rmi.RemoteException;

import cm.common.interfaces.ILogService;

// Simple wrapper around ILogService designed to catch exceptions
public class Logger
{
	private Logger()
	{
		;
	}

	public static Logger getInstance()
	{
		if( instance == null ) {
			instance = new Logger();
		}

		return instance;		
	}

	public void setLogService( ILogService log )
	{
		this.log = log;
	}

	public void debug( String fmt, Object... args )
	{
		if( log == null ) {
			return;
		}

		try {
			log.debug( fmt, args );
		} catch( RemoteException e ) {
			e.printStackTrace();
		}
	}

	public void info( String fmt, Object... args )
	{
		if( log == null ) {
			return;
		}

		try {
			log.info( fmt, args );
		} catch( RemoteException e ) {
			e.printStackTrace();
		}
	}

	public void warn( String fmt, Object... args )
	{
		if( log == null ) {
			return;
		}

		try {
			log.warn( fmt, args );
		} catch( RemoteException e ) {
			e.printStackTrace();
		}
	}

	public void error( String fmt, Object... args )
	{
		if( log == null ) {
			return;
		}

		try {
			log.error( fmt, args );
		} catch( RemoteException e ) {
			e.printStackTrace();
		}
	}

	public void error( String msg, Exception e )
	{
		if( log == null ) {
			return;
		}

		try {
			log.error( msg, e );
		} catch( RemoteException re ) {
			re.printStackTrace();
		}
	}

	private ILogService log;

	private static Logger instance;
}
