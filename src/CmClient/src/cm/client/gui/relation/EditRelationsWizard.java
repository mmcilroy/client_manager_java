package cm.client.gui.relation;

import java.rmi.RemoteException;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import cm.client.Session;
import cm.client.gui.common.wizard.WizardDialog;
import cm.client.gui.common.wizard.WizardPanel;
import cm.common.entity.domain.Customer;
import cm.common.entity.domain.CustomerCollection;
import cm.common.entity.domain.Relationship;
import cm.common.types.RelationshipType;

@SuppressWarnings("serial")
public class EditRelationsWizard extends WizardDialog
{
	public EditRelationsWizard( Session client, CustomerRelationCollection crc ) 
	{
		this.client = client;
		this.existingRelations = crc;

		editAddressPanel = new EditAddressRelationsPanel();
		editExistingPanel = new EditExistingRelationsPanel();
		editExistingPanel.display();

		if( crc.size() > 0 )
		{
			nextWizardPanel( editExistingPanel );
		}
		else
		{
			new Thread( new Runnable()
			{
				public void run()
				{
					setBusy( true );
					editAddressPanel.display();
					nextWizardPanel( editAddressPanel );
					setBusy( false );
				}
			} ).start();
		}

		setTitle( "Edit Relations" );
		setSize( 400, 400 );
		setLocationRelativeTo( null );
		setVisible( true );
	}

	private Session client;
	private CustomerRelationCollection existingRelations;
	
	private EditExistingRelationsPanel editExistingPanel;
	private EditAddressRelationsPanel editAddressPanel;

	class EditExistingRelationsPanel extends WizardPanel
	{
		EditExistingRelationsPanel() {
			add( relationsPanel );
		}

		void display() {
			relationsPanel.display( existingRelations );
		}

		public String getTitle() {
			return "Edit existing relationships";
		}

		protected void onNext() {
			new Thread( new UpdateExisting() ).start();
		}

		private class UpdateExisting implements Runnable
		{
			public void run()
			{
				setBusy( true );

				try
				{
					synchronized( existingRelations )
					{
						for( CustomerRelation r : existingRelations.values() )
						{
							RelationshipType newType = relationsPanel.getRelationshipType( r.customer );
							if( newType == null )
							{
								client.getCustomerService().deactivate( r.relationship );
							}
							else
							if( newType != null && newType.compareTo( r.relationship.type ) != 0 )
							{
								r.relationship.type = newType;
								client.getCustomerService().update( r.relationship );
							}
						}
					}

					editAddressPanel.display();
					nextWizardPanel( editAddressPanel );
				}
				catch( Exception e )
				{
					ErrorInfo ei = new ErrorInfo( "Error", "An error occured", null, null, e, null, null );
					JXErrorPane.showDialog( EditExistingRelationsPanel.this, ei );
				}

				setBusy( false );
			}
		}

		private EditRelationsPanel relationsPanel = new EditRelationsPanel();
	}

	private class EditAddressRelationsPanel extends WizardPanel
	{
		EditAddressRelationsPanel() {
			add( relationsPanel );
		}

		void display()
		{
			try
			{
				relationsAtAddress = new CustomerRelationCollection( existingRelations.getCustomer() );
				CustomerCollection cc = client.getCustomerService().searchForCustomersAtAddress( existingRelations.getCustomer().address.id );
				for( Customer c : cc.values() )
				{
					CustomerRelation cr = existingRelations.getByCustomerId( c.personalDetails.id );
					if( cr == null && c.personalDetails.id.compareTo( existingRelations.getCustomer().personalDetails.id ) != 0 )
					{
						Relationship r = new Relationship();
						r.customer1 = relationsAtAddress.getCustomer().personalDetails.id;
						r.customer2 = c.personalDetails.id;
						relationsAtAddress.add( new CustomerRelation( c, r ) );
					}
				}

				relationsPanel.display( relationsAtAddress );
			}
			catch( RemoteException e )
			{
				ErrorInfo ei = new ErrorInfo( "Error", "An error occured", null, null, e, null, null );
				JXErrorPane.showDialog( EditAddressRelationsPanel.this, ei );
			}
		}

		public String getTitle() {
			return "Edit relationships with others at the same address";
		}

		protected void onNext() {
			new Thread( new UpdateAtAddress() ).start();
		}

		private class UpdateAtAddress implements Runnable
		{
			public void run()
			{
				boolean dispose = false;
				setBusy( true );

				try
				{
					for( CustomerRelation cr : relationsAtAddress.values() )
					{
						RelationshipType newType = relationsPanel.getRelationshipType( cr.customer );

						if( newType != null && cr.relationship.id <= 0 )
						{
							// add a new relationship
							cr.relationship.type = newType;
							client.getCustomerService().createRelationship( cr.relationship );
						}
						else
						if( newType != null && newType.compareTo( cr.relationship.type ) != 0 && cr.relationship.id > 0 )
						{
							// update existing relationship
							cr.relationship.type = newType;
							client.getCustomerService().update( cr.relationship );
						}
						else
						if( newType == null && cr.relationship.id > 0 )
						{
							// delete existing relationship
							client.getCustomerService().deactivate( cr.relationship );
						}
					}

					dispose = true;
				}
				catch( Exception e )
				{
					ErrorInfo ei = new ErrorInfo( "Error", "An error occured", null, null, e, null, null );
					JXErrorPane.showDialog( EditAddressRelationsPanel.this, ei );
				}

				setBusy( false );

				if( dispose ) {
					dispose();
				}
			}
		}

		private CustomerRelationCollection relationsAtAddress;
		private EditRelationsPanel relationsPanel = new EditRelationsPanel();
	}
}
