package cm.client.gui.customer;

import java.rmi.RemoteException;

import javax.swing.JPanel;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import cm.client.Session;
import cm.client.gui.common.OkCancelDialog;
import cm.common.entity.domain.PersonalDetails;
import cm.util.PersonalDetailsValidator;

@SuppressWarnings("serial")
public class EditPersonalDetailsDialog extends OkCancelDialog
{
	public EditPersonalDetailsDialog( Session client, PersonalDetails details )
	{
		super( "Edit Personal Details" );

		this.client = client;
		this.panel = new EditPersonalDetailsPanel( client, details );

		init();
	}

	protected JPanel initContent() {
		return panel;
	}

	protected void onOk()
	{
		PersonalDetails pd = panel.toPersonalDetails();
		String err = PersonalDetailsValidator.isValid( pd );
		if( err != null )
		{
			error( err, null );
		}
		else
		{
			try
			{
				client.getCustomerService().update( panel.toPersonalDetails() );
				dispose();
			}
			catch( RemoteException e )
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
	private EditPersonalDetailsPanel panel;
}
