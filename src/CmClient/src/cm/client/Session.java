package cm.client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import cm.common.interfaces.ICustomerService;
import cm.common.interfaces.ILogService;
import cm.common.interfaces.ILoginService;
import cm.common.logger.Logger;
import cm.common.pubsub.CustomerSubscription;
import cm.common.types.LoginResponse;

public class Session
{
	public Session( String host, int port, String user, String pass ) throws ClientError
    {
		Registry registry = null;
		
		try {
			registry = LocateRegistry.getRegistry( host, port );
		} catch( Exception e ) {
			throw new ClientError( "Server is not responding", e );
		}

    	ILoginService loginSvc;

		try {
			loginSvc = (ILoginService)registry.lookup( ILoginService.class.getSimpleName() );
		} catch( Exception e ) {
			throw new ClientError( "Login service unavailable", e );
		}

		LoginResponse lr;

		try
		{
			lr = loginSvc.login( user, pass );
			switch( lr.result )
			{
				case LOGIN_INCORRECT: throw new ClientError( "Login incorrect" );
				case ALREADY_LOGGED_IN:  throw new ClientError( "User already logged in" );
				case COULD_NOT_AUTHENTICATE: throw new ClientError( "Could not authenticate login at this time" );
				case COULD_NOT_INITIALIZE: throw new ClientError( "Could not initialize at this time" );
			}
		}
		catch( RemoteException e )
		{
			throw new ClientError( "Unexcepted error occurred", e );
		}

		try {
			customerService = (ICustomerService)registry.lookup( lr.customerServiceId );
		} catch( Exception e ) {
			throw new ClientError( "Customer service unavailable", e );
		}

		try {
			Logger.getInstance().setLogService( (ILogService)registry.lookup( lr.logServiceId ) );
		} catch( Exception e ) {
			throw new ClientError( "Log service unavailable", e );
		}

		Logger.getInstance().info( "%s logged in", user );

    	configuration = new Configuration( this );

    	initSubscriptions( user, host, lr.subscriptionPort );

    	this.user = user;
    }

	public ICustomerService getCustomerService()
	{
		return customerService;
	}

	public CustomerSubscription getCustomerSubscription()
	{
		return customerSubscription;
	}

	public Configuration getConfiguration()
	{
		return configuration;
	}

    public String getUser()
    {
    	return user;
    }

	private void initSubscriptions( String user, String host, int port ) throws ClientError
	{
		Logger.getInstance().info( "connecting to subscription service at %s:%d", host, port );

		try
		{
			customerSubscription = new CustomerSubscription( user, host, port );
			customerSubscription.run();
		}
		catch( Exception e )
		{
			throw new ClientError( "Failed to initalize subscriptions", e );
		}
	}

    private ICustomerService customerService;
	private CustomerSubscription customerSubscription;
	private Configuration configuration;
    private String user;
}
