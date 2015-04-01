package cm.client.gui.main;

import java.rmi.RemoteException;

import cm.client.Session;
import cm.client.gui.customer.CustomerDialog;
import cm.client.gui.customer.CustomerSearchDialog;
import cm.client.gui.customer.CustomerTableManager;
import cm.client.gui.customer.NewCustomerWizard;
import cm.client.gui.layout.CustomerTabLayout;
import cm.common.entity.domain.Customer;
import cm.common.logger.Logger;

@SuppressWarnings("serial")
public class CustomerTab extends CustomerTabLayout
{
	public CustomerTab( final Session client )
	{
		super();

		this.client = client;
		this.tableMgr = new CustomerTableManager( table )
		{
			protected void onDoubleClick( Customer customer ) {
				new CustomerDialog( client, customer );
			}
		};
	}

	public void onCustomer( Customer customer ) {
		tableMgr.onCustomer( customer );
	}

	protected void onNewCustomer() {
		new NewCustomerWizard( client );
	}

	protected void onSearch()
	{
		final CustomerSearchDialog dlg = new CustomerSearchDialog();

		if( dlg.isOk() )
		{
			new Thread( new Runnable()
			{
				public void run()
				{
					searchPanel.setBusy( true );
	
					try {
						tableMgr.display( client.getCustomerService().searchCustomers( dlg.toSearchParameters() ) );
					} catch( RemoteException e ) {
						Logger.getInstance().error( "onSearch failed", e );
					}
	
					searchPanel.setBusy( false );
				}
			} ).start();
		}
	}

	private Session client;
	private CustomerTableManager tableMgr;
}
