package cm.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cm.common.db.DbConn;
import cm.common.entity.db.EntityDbConn;
import cm.common.interfaces.ICustomerService;
import cm.common.interfaces.ILogService;
import cm.common.interfaces.ILoginService;
import cm.common.types.LogLevel;
import cm.common.types.LoginResponse;
import cm.common.types.LoginResult;

class Server
{
    public static void main( String[] args ) throws ClassNotFoundException, SQLException, IOException
    {
    	BasicConfigurator.configure();
    	new Server( args );
    }

    private Server( String[] args ) throws ClassNotFoundException, SQLException, IOException
    {
    	LOG.info( "Loading configuration..." );

    	if( args != null && args.length >= 1 )
    	{
    		Properties appConfig = loadProperties( args[0] );
    		dbConn = appConfig.getProperty( "dbConn" );
    		dbName = appConfig.getProperty( "dbName" );
    		dbUser = appConfig.getProperty( "dbUser" );
    		dbPass = appConfig.getProperty( "dbPass" );
    		logPath = appConfig.getProperty( "logPath" );
    		subscriptionPort = Integer.parseInt( appConfig.getProperty( "subscriptionPort" ) );
    		rmiPort = Integer.parseInt( appConfig.getProperty( "rmiPort" ) );
    	}
    	else
    	{
    		usage();
    	}

    	LOG.info( "Initializing subscription server..." );

    	subscriptionServer = new CustomerSubscriptionServer( this, subscriptionPort );

    	LOG.info( "Initializing db connection..." );

    	DbConn db = new DbConn( dbConn, dbName, dbUser, dbPass );
		db.setKeepAlive( 120 );

    	LOG.info( "Initializing rmi interface..." );

    	LocateRegistry.createRegistry( rmiPort );
		Naming.rebind( String.format( "//:%d/%s", rmiPort, ILoginService.class.getSimpleName() ), new LoginService( this, db ) );

		LOG.info( "Ready" );
    }

    public LoginResponse logon( String user ) throws ServerError
    {
    	LoginResponse lr = new LoginResponse( LoginResult.LOGIN_OK );

    	if( usersConnected.contains( user ) ) {
    		throw new ServerError( user + " is already logged in" );
    	}

    	LOG.info( "User logon: {}", user );

    	try
    	{
    		EntityDbConn db = new EntityDbConn( dbConn, dbName, dbUser, dbPass );
    		db.setKeepAlive( 120 );

    		lr.customerServiceId = String.format( "%s.%s", ICustomerService.class.getSimpleName(), user ); 
    		LOG.debug( "Creating customer service {}", lr.customerServiceId );
    		Naming.rebind( String.format( "//:%d/%s", rmiPort, lr.customerServiceId ), new CustomerService( db, subscriptionServer, user ) );

    		lr.logServiceId = String.format( "%s.%s", ILogService.class.getSimpleName(), user ); 
    		LOG.debug( "Creating log service {}", lr.logServiceId );
    		Naming.rebind( String.format( "//:%d/%s", rmiPort, lr.logServiceId ), new LogService( LogLevel.DEBUG, logPath + File.separatorChar + user + ".log" ) );

    		lr.subscriptionPort = subscriptionPort;

    		return lr;
    	}
    	catch( Exception e )
    	{
    		throw new ServerError( "initUser failed for " + user, e );
    	}
    }

    boolean isUserConnected( String user )
    {
    	return usersConnected.contains( user );
    }

    void userConnectionConfirmed( String user )
    {
    	LOG.info( "User connection confirmed: {}", user );
    	usersConnected.add( user );
    }

    void onUserDisconnected( String user )
    {
    	LOG.info( "User disconnected: {}", user );
    	usersConnected.remove( user );
    }

    void usage()
    {
    	System.out.println( "Usage: Server props" );
    	System.exit( 1 );
    }

	Properties loadProperties( String p ) throws IOException
	{
		Properties props = new Properties();
		props.load( new FileInputStream( p ) );
		return props;
	}

    private String dbConn; 
    private String dbName;
    private String dbUser;
    private String dbPass;
    private String logPath;
    private Integer subscriptionPort;
    private Integer rmiPort;

	private CustomerSubscriptionServer subscriptionServer;
    private Set<String> usersConnected = new HashSet<String>();

    private static final Logger LOG = LoggerFactory.getLogger( Server.class );
}
