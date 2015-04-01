package cm.client.gui.product;

import javax.swing.JPanel;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import cm.client.Session;
import cm.client.gui.common.OkCancelDialog;
import cm.common.entity.domain.Customer;
import cm.common.entity.domain.Product;
import cm.util.ProductValidator;

@SuppressWarnings("serial")
public class EditProductDialog extends OkCancelDialog
{
	public EditProductDialog( Session client, Product product )
	{
		super( "Edit Product" );

		this.client = client;
		this.panel = new EditProductPanel( client, product );

		init();
	}

	public EditProductDialog( Session client, Customer customer )
	{
		super( "New Personal Details" );

		this.client = client;
		this.customer = customer;
		this.panel = new EditProductPanel( client );

		init();
	}

	protected JPanel initContent() {
		return panel;
	}

	protected void onOk()
	{
		Product p = panel.toProduct();
		String err = ProductValidator.isValid( p );

		if( err != null )
		{
			error( err, null );
		}
		else
		{
			try
			{
				if( p.id <= 0 )
				{
					p.customerId = customer.personalDetails.id;
					client.getCustomerService().createProduct( p, true );
				}
				else
				{
					client.getCustomerService().update( p );
				}
		
				dispose();
			}
			catch( Exception e )
			{
				error( "An error occured", e );
			}
		}
	}

	private void error( String text, Exception e )
	{
		ErrorInfo ei = new ErrorInfo( "Error", text, null, null, e, null, null );
		JXErrorPane.showDialog( this, ei );
	}

	private Session client;
	private Customer customer;
	private EditProductPanel panel;
}
