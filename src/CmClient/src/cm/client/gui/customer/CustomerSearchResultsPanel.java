package cm.client.gui.customer;

import java.rmi.RemoteException;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import cm.client.Session;
import cm.client.gui.layout.CustomerSearchPanelLayout;
import cm.common.entity.domain.Customer;
import cm.common.entity.domain.CustomerCollection;

@SuppressWarnings("serial")
public class CustomerSearchResultsPanel extends CustomerSearchPanelLayout
{
	public void init( Session client )
	{
		this.client = client;
		this.tableMgr = new CustomerTableManager( getTable() );
	}

	public Customer getSelectedCustomer() {
		return tableMgr.getSelectedCustomer();
	}

	protected void onSearch()
	{
		CustomerCollection cc;
		try
		{
			cc = client.getCustomerService().searchForCustomersByName( getForenameField().getText(), getSurnameField().getText() );
			tableMgr.display( cc );
		}
		catch( RemoteException e )
		{
			ErrorInfo ei = new ErrorInfo( "Error", "An error occured", null, null, e, null, null );
			JXErrorPane.showDialog( this, ei );
		}
	}
	
	private Session client;
	private CustomerTableManager tableMgr;
}
