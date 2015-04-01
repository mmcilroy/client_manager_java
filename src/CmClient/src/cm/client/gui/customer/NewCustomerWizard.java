package cm.client.gui.customer;

import java.rmi.RemoteException;

import javax.swing.JLabel;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import cm.client.Session;
import cm.client.gui.address.AddressComponentFactory;
import cm.client.gui.address.EditAddressPanel;
import cm.client.gui.common.CustomTextField;
import cm.client.gui.common.wizard.WizardDialog;
import cm.client.gui.common.wizard.WizardPanel;
import cm.client.gui.layout.SelectAddressPanelLayout;
import cm.common.entity.domain.Address;
import cm.common.entity.domain.AddressCollection;
import cm.common.entity.domain.Customer;
import cm.common.entity.domain.PersonalDetails;
import cm.util.AddressValidator;
import cm.util.PersonalDetailsValidator;
import cm.util.PostcodeValidator;

@SuppressWarnings("serial")
public class NewCustomerWizard extends WizardDialog
{
	public NewCustomerWizard( Session client )
	{
		this.client = client;

		this.enterPostcodePanel = new EnterPostcodePanel();
		this.selectAddressPanel = new SelectAddressPanel();
		this.editAddressWizardPanel = new EditAddressWizardPanel();
		this.editCustomerWizardPanel = new EditCustomerWizardPanel();

		nextWizardPanel( enterPostcodePanel );
		setTitle( "New Customer" );
		setSize( 400, 600 );
		setLocationRelativeTo( null );
		setVisible( true );
	}

	private void error( String text, Exception e )
	{
		ErrorInfo ei = new ErrorInfo( "Error", text, null, null, e, null, null );
		JXErrorPane.showDialog( this, ei );
	}

	private EnterPostcodePanel enterPostcodePanel;
	private SelectAddressPanel selectAddressPanel;
	private EditAddressWizardPanel editAddressWizardPanel;
	private EditCustomerWizardPanel editCustomerWizardPanel;

	private Session client;

	private Address address;
	private PersonalDetails details;

	private class EnterPostcodePanel extends WizardPanel
	{
		EnterPostcodePanel()
		{
			add( new JLabel( "Customer Postcode" ) );
			add( postcodeField );
		}

		public String getTitle() {
			return "Enter customers postcode";
		}

		protected void onNext() {
			new Thread( new FindPostcode() ).start();
		}

		private class FindPostcode implements Runnable
		{
			public void run()
			{
				setBusy( true );

				String pc = postcodeField.getText();
				if( !PostcodeValidator.isValid( pc ) )
				{
					error( "Invalid postcode format", null );
				}
				else
				{
					try
					{
						AddressCollection addresses = client.getCustomerService().searchForAddressByPostcode( pc );
						if( addresses != null && addresses.size() > 0 )
						{
							selectAddressPanel.display( addresses );
							nextWizardPanel( selectAddressPanel );
						}
						else
						{
							address = new Address();
							address.postcode = pc; 
							editAddressWizardPanel.getEditAddressPanel().fromAddress( address );
							nextWizardPanel( editAddressWizardPanel );
						}
					}
					catch( Exception e )
					{
						error( "An error occured", e );
					}
				}

				setBusy( false );
			}
		}

		private CustomTextField postcodeField = AddressComponentFactory.newPostcodeComponent();
	};

	private class SelectAddressPanel extends SelectAddressPanelLayout
	{
		public void display( AddressCollection addresses )
		{
			clearAddressList();

			if( addresses != null )
			{
				for( Address address : addresses.values() ) {
					add( address );
				}
			}
		}

		public String getTitle() {
			return "Select an existing address or enter a new address";
		}

		protected void onNext()
		{
			Address address = getSelectedAddress();
			if( useSelectedAddress() && address != null )
			{
				NewCustomerWizard.this.address = address;
				nextWizardPanel( editCustomerWizardPanel );
			}
			else
			if( createNewAddress() )
			{
				if( address != null ) {
					editAddressWizardPanel.getEditAddressPanel().fromAddress( address );
				} else {
					editAddressWizardPanel.getEditAddressPanel().reset();
				}

				nextWizardPanel( editAddressWizardPanel );
			}
		}
	};

	private class EditAddressWizardPanel extends WizardPanel
	{
		EditAddressWizardPanel() {
			add( editAddressPanel );
		}

		public String getTitle() {
			return "Enter address details";
		}

		EditAddressPanel getEditAddressPanel() {
			return editAddressPanel;
		}

		protected void onNext()
		{
			address = editAddressPanel.toAddress();
			address.id = 0;

			String err = AddressValidator.isValid( address );
			if( err == null ) {
				nextWizardPanel( editCustomerWizardPanel );
			} else {
				error( err, null );
			}
		}

		EditAddressPanel editAddressPanel = new EditAddressPanel();
	};

	private class EditCustomerWizardPanel extends WizardPanel
	{
		EditCustomerWizardPanel() {
			add( editDetailsPanel );
		}

		public String getTitle() {
			return "Enter customers details";
		}

		protected void onNext()
		{
			details = editDetailsPanel.toPersonalDetails();
			new Thread( new NewCustomer() ).start();
		}

		private class NewCustomer implements Runnable
		{
			public void run()
			{
				Customer customer = null;
				boolean dispose = false;
				setBusy( true );

				String err = PersonalDetailsValidator.isValid( details );
				if( err != null )
				{
					error( err, null );
				}
				else
				{
					try
					{
						customer = client.getCustomerService().createCustomer( address, details );
						dispose = true;
					}
					catch( RemoteException e )
					{
						error( "An error occured", e );
					}
				}

				setBusy( false );

				if( dispose ) {
					dispose();
				}

				if( customer != null ) {
					new CustomerDialog( client, customer );
				}
			}
		}

		EditPersonalDetailsPanel editDetailsPanel = new EditPersonalDetailsPanel( client );
	};
}
