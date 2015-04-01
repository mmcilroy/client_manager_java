package cm.common.interfaces;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ILogService extends Remote
{
	public void debug( String fmt, Object... args ) throws RemoteException;

	public void info( String fmt, Object... args ) throws RemoteException;

	public void warn( String fmt, Object... args ) throws RemoteException;

	public void error( String fmt, Object... args ) throws RemoteException;

	public void error( String msg, Exception e ) throws RemoteException;
}
