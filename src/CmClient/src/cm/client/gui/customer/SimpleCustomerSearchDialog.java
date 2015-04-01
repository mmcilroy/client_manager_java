package cm.client.gui.customer;

import javax.swing.JPanel;

import cm.client.Session;
import cm.client.gui.common.OkCancelDialog;
import cm.common.entity.domain.Customer;

@SuppressWarnings("serial")
public class SimpleCustomerSearchDialog extends OkCancelDialog
{
	public SimpleCustomerSearchDialog( Session client )
	{
		super( "Customer Search" );

		panel.init( client );
		init( 800, 500 );
	}

	public Customer getCustomer() {
		return customer;
	}

	protected JPanel initContent() {
		return panel;
	}

	protected void onOk()
	{
		customer = panel.getSelectedCustomer();
		if( customer != null )
		{
			isOk( true );
			dispose();
		}
	}

	private Customer customer;
	private CustomerSearchResultsPanel panel = new CustomerSearchResultsPanel();
}
