package cm.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cm.common.db.DbConn;
import cm.common.interfaces.ILoginService;
import cm.common.types.LoginResponse;
import cm.common.types.LoginResult;

@SuppressWarnings("serial")
public class LoginService extends UnicastRemoteObject implements ILoginService
{
	protected LoginService( Server server, DbConn db ) throws RemoteException
	{
		super();
		this.server = server;
		this.db = db;
	}

	public LoginResponse login( String user, String pass ) throws RemoteException
	{
		LOG.debug( "Got a login request: '{}', '{}'", user, pass );

		try
		{
			if( user == null || pass == null )
			{
				LOG.error( "Null login: '{}' '{}'", user, pass );
				return new LoginResponse( LoginResult.LOGIN_INCORRECT );
			}
		
			if( !authenticate( user, pass ) )
			{
				LOG.error( "Incorrect login: '{}' '{}'", user, pass );
				return new LoginResponse( LoginResult.LOGIN_INCORRECT );
			}
		}
		catch( SQLException e )
		{
			LOG.error( "Unexpected authentication error", e );
			return new LoginResponse( LoginResult.COULD_NOT_AUTHENTICATE );
		}

		try
		{
			if( server.isUserConnected( user ) ) {
				return new LoginResponse( LoginResult.ALREADY_LOGGED_IN );
			}

			return server.logon( user );
		}
		catch( ServerError e )
		{
			LOG.error( "Failed to initialize user", e );
			return new LoginResponse( LoginResult.COULD_NOT_INITIALIZE );
		}
	}

	private boolean authenticate( String user, String pass ) throws SQLException
	{
		ResultSet rs = db.execute( String.format( "select * from Authentication where user='%s' and pass='%s'", user, pass ) );
		return rs.next();
	}

	private Server server;
	private DbConn db;

	private static final Logger LOG = LoggerFactory.getLogger( LoginService.class );
}
