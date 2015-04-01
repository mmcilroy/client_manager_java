package cm.client.gui.main;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import cm.client.Session;

@SuppressWarnings("serial")
public class MainFrame extends JFrame
{
	public MainFrame() 
	{
		super();

		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setTitle( "ClientManager" );
		setSize( 1000, 700 );
		setLocationRelativeTo( null );
		setVisible( true );
	}

	public void onLoggedOn( Session session )
	{
		Container cp = getContentPane();
		cp.setVisible( false );
		cp.removeAll();

		//hotLeadTab = new HotLeadTab( session );
		customerTab = new CustomerTab( session );
		taskTab = new TaskTab( session );
		appointmentTab = new AppointmentTab( session );

		setTitle( "ClientManager | " + session.getUser() );

		JTabbedPane tp = new JTabbedPane();
		//tp.add( "Hot Leads", hotLeadTab );
		tp.add( "Customers", customerTab );
		tp.add( "Tasks", taskTab );
		tp.add( "Appointments", appointmentTab );
		cp.add( tp );
		cp.setVisible( true );

		//hotLeadTab.refresh();
	}

	public void onLoggedOff()
	{
		;
	} 

	//private HotLeadTab hotLeadTab;
	private CustomerTab customerTab;
	private TaskTab taskTab;
	private AppointmentTab appointmentTab;
}
