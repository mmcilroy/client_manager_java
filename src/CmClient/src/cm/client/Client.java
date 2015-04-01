package cm.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.UIManager;

import cm.client.gui.common.ErrorDialog;
import cm.client.gui.login.LoginDialog;
import cm.client.gui.main.MainFrame;
import cm.common.entity.domain.Customer;
import cm.common.pubsub.CustomerObserver;
import cm.common.pubsub.CustomerSubscription;

public class Client implements CustomerObserver
{
	public static void main( String[] args ) throws IOException
	{
		new Client( args );
	}

	private Client( String[] args ) throws IOException
	{
		String propFile = "cm/client/resource/cmclient-prod.prop";

		if( args != null && args.length >= 1 ) {
			propFile = args[0];
		}

		appConfig = loadProperties( propFile );
		host = appConfig.getProperty( "host" );
		port = Integer.parseInt( appConfig.getProperty( "port" ) );

		try {
			UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
		} catch( Exception e ) {
			;
		}

		mainFrame = new MainFrame();

		login();
	}

	public void onConnectionLost()
	{
		loginAccepted = false;
		mainFrame.onLoggedOff();
		login();
	}

	public void onCustomer( Customer c )
	{
		;
	}

	void login()
	{
		String user = appConfig.getProperty( "user" );
		String pass = appConfig.getProperty( "pass" );

		if( user != null && pass != null )
		{
			doLogin( user, pass );
		}
		else
		{
			loginDialog = new LoginDialog();
			loginDialog.setEventHandler( new LoginEventHandler() );
			loginDialog.setVisible( true );
	
			if( !loginAccepted ) {
				onLogonCancelled();
			}
		}
	}

	void onLogonAccepted( String user )
	{
		loginAccepted = true;
		mainFrame.onLoggedOn( session );
	}

	void onLogonCancelled()
	{
		mainFrame.dispose();
		System.exit( 0 );
	}

	InputStream loadResource( String r )
	{
		return this.getClass().getClassLoader().getResourceAsStream( r );
	}

	Properties loadProperties( String p ) throws IOException
	{
		InputStream is = loadResource( p );

		if( is == null ) {
			throw new IOException( "Resource not found: " + p );
		}

		Properties props = new Properties();
		props.load( is );

		return props;
	}

	void doLogin( String user, String pass )
	{
		try
		{
			session = new Session( host, port, user, pass );

			CustomerSubscription subscription = session.getCustomerSubscription();
			subscription.addObserver( Client.this );
			onLogonAccepted( user );
		}
		catch( Exception e )
		{
			ErrorDialog.show( loginDialog, "Login Failure", e );
		}
	}

	class LoginEventHandler implements LoginDialog.IEventHandler
	{
		public void onOk()
		{
			Thread t = new Thread()
			{
				public void run()
				{
					loginDialog.setBusy( true );
					doLogin( loginDialog.getUsername(), loginDialog.getPassword() );
					loginDialog.setBusy( false );
					loginDialog.dispose();
				}
			};

			t.start();
		}

		public void onCancel()
		{
			loginDialog.dispose();
			onLogonCancelled();
		}
	}

	private Properties appConfig;
	private String host;
	private int port;

	private Session session;
	
	private MainFrame mainFrame;
	private LoginDialog loginDialog;
	private boolean loginAccepted = false;
}
