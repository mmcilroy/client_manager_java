package cm.server;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Date;

import cm.common.interfaces.ILogService;
import cm.common.types.LogLevel;

@SuppressWarnings( "serial" )
class LogService extends UnicastRemoteObject implements ILogService
{
	protected LogService( LogLevel level, String filename ) throws IOException
	{
		super();
		this.level = level;

		out = new FileWriter( filename, true );
	}

	public void debug( String fmt, Object... args ) throws RemoteException
	{
		log( LogLevel.DEBUG, fmt, args );
	}

	public void info( String fmt, Object... args ) throws RemoteException
	{
		log( LogLevel.INFO, fmt, args );
	}

	public void warn( String fmt, Object... args ) throws RemoteException
	{
		log( LogLevel.WARN, fmt, args );
	}

	public void error( String fmt, Object... args ) throws RemoteException
	{
		log( LogLevel.ERROR, fmt, args );
	}

	public void error( String msg, Exception e ) throws RemoteException
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter( sw );
		e.printStackTrace( pw );

		log( LogLevel.ERROR, msg + "\n" + sw.toString() );
	}

	private boolean shouldLog( LogLevel level )
	{
		return this.level.compareTo( level ) <= 0;
	}

	private void log( LogLevel level, String fmt, Object... args )
	{
		if( shouldLog( level ) )
		{
			String s1 = String.format( fmt + "\n", args );
			String s2 = String.format( "%s %s - %s", getTimestamp(), level.toString(), s1 );

			try
			{
				out.write( s2 );
				out.flush();
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
		}
	}

	private String getTimestamp()
	{
		return timeFormat.format( new Date() );
	}

	private LogLevel level;
	private FileWriter out;

	private static SimpleDateFormat timeFormat = new SimpleDateFormat( "HH:mm:ss.SSS" );

}
