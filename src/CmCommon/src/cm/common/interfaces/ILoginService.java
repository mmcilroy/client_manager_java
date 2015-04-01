package cm.common.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import cm.common.types.LoginResponse;

public interface ILoginService extends Remote
{
	public LoginResponse login( String user, String pass ) throws RemoteException;
}
