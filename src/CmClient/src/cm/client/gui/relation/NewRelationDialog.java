package cm.client.gui.relation;

import javax.swing.JPanel;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import cm.client.Session;
import cm.client.gui.common.OkCancelDialog;
import cm.client.gui.layout.NewRelationPanelLayout;
import cm.common.entity.domain.Customer;
import cm.common.entity.domain.Relationship;
import cm.common.types.RelationshipType;

@SuppressWarnings("serial")
public class NewRelationDialog extends OkCancelDialog
{
	public NewRelationDialog( Session client, Customer customer )
	{
		super( "New Relation" );

		this.client = client;
		this.customer = customer;

		panel.getSearchPanel().init( client );

		init( 700, 500 );
	}

	protected JPanel initContent()
	{
		return panel;
	}

	protected void onOk()
	{
		try
		{
			Customer c = panel.getSearchPanel().getSelectedCustomer();
			Relationship r = new Relationship();

			String s = (String)panel.getRelationshipCombo().getSelectedItem();
			if( s != null && s.length() > 0 ) {
				r.type = RelationshipType.valueOf( s );
			}

			if( c != null && r.type != null &&
				c.personalDetails.id.compareTo( customer.personalDetails.id ) != 0 )
			{
				r.customer1 = customer.personalDetails.id;
				r.customer2 = c.personalDetails.id;
				client.getCustomerService().createRelationship( r );
				dispose();
			}
		}
		catch( Exception e )
		{
			ErrorInfo ei = new ErrorInfo( "Error", "Could not create relationship", null, null, e, null, null );
			JXErrorPane.showDialog( panel, ei );
		}
	}

	private Session client;
	private Customer customer;
	private NewRelationPanelLayout panel = new NewRelationPanelLayout();
}
