package cm.client.gui.main;

import java.rmi.RemoteException;

import javax.swing.JComboBox;

import cm.client.Session;
import cm.client.gui.customer.CustomerDialog;
import cm.client.gui.layout.TaskTabLayout;
import cm.client.gui.task.TaskSearchTableManager;
import cm.common.entity.domain.Task;
import cm.common.logger.Logger;

@SuppressWarnings("serial")
public class TaskTab extends TaskTabLayout
{
	public TaskTab( final Session session )
	{
		super();

		this.client = session;

		tableMgr = new TaskSearchTableManager( table )
		{
			protected void onDoubleClick( Task task ) {
				new CustomerDialog( session, task );
			}
		};

		JComboBox<String> cb = getBusinessOwnerCombo();
		cb.addItem( "" );
		for( String owner : session.getConfiguration().getBusinessOwners() ) {
			cb.addItem( owner );
		}
	}

	public void onTask( Task task ) {
		tableMgr.onTask( task );
	}

	protected void onOverdue() {
		new Thread( new Overdue() ).start();
	}

	protected void onToday() {
		new Thread( new Today() ).start();
	}

	protected void onTomorrow() {
		new Thread( new Tomorrow() ).start();
	}

	private Session client;
	private TaskSearchTableManager tableMgr;

	private class Overdue implements Runnable
	{
		public void run()
		{
			searchPanel.setBusy( true );

			try
			{
				String businessOwner = (String)getBusinessOwnerCombo().getSelectedItem();
				if( businessOwner != null && businessOwner.length() > 0 ) {
					tableMgr.display( client.getCustomerService().searchForOverdueTasks( businessOwner ) );
				}
			}
			catch( RemoteException e )
			{
				Logger.getInstance().error( "Overdue failed", e );
			}

			searchPanel.setBusy( false );
		}
	}

	private class Today implements Runnable
	{
		public void run()
		{
			searchPanel.setBusy( true );

			try
			{
				String businessOwner = (String)getBusinessOwnerCombo().getSelectedItem();
				if( businessOwner != null && businessOwner.length() > 0 ) {
					tableMgr.display( client.getCustomerService().searchForTodaysTasks( businessOwner ) );
				}
			}
			catch( RemoteException e )
			{
				Logger.getInstance().error( "Overdue failed", e );
			}

			searchPanel.setBusy( false );
		}
	}

	private class Tomorrow implements Runnable
	{
		public void run()
		{
			searchPanel.setBusy( true );

			try
			{
				String businessOwner = (String)getBusinessOwnerCombo().getSelectedItem();
				if( businessOwner != null && businessOwner.length() > 0 ) {
					tableMgr.display( client.getCustomerService().searchForTomorrowsTasks( businessOwner ) );
				}
			}
			catch( RemoteException e )
			{
				Logger.getInstance().error( "Overdue failed", e );
			}

			searchPanel.setBusy( false );
		}
	}
}
