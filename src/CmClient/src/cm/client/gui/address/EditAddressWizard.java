package cm.client.gui.address;

import java.rmi.RemoteException;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import cm.client.Session;
import cm.client.gui.common.CustomTextField;
import cm.client.gui.common.wizard.WizardDialog;
import cm.client.gui.common.wizard.WizardPanel;
import cm.client.gui.layout.SelectAddressPanelLayout;
import cm.common.entity.domain.Address;
import cm.common.entity.domain.AddressCollection;
import cm.common.entity.domain.Customer;
import cm.util.AddressValidator;
import cm.util.PostcodeValidator;

@SuppressWarnings("serial")
public class EditAddressWizard extends WizardDialog
{
	public EditAddressWizard( Session session, Customer customer )
	{
		this.session = session;
		this.customer = customer;

		this.selectEditTypePanel = new SelectEditTypePanel();
		this.enterPostcodePanel = new EnterPostcodePanel();
		this.selectAddressPanel = new SelectAddressPanel();
		this.editAddressWizardPanel = new EditAddressWizardPanel();

		enterPostcodePanel.postcodeField.setText( customer.address.postcode );

		nextWizardPanel( selectEditTypePanel );
		setTitle( "Edit Address" );
		setSize( 400, 400 );
		setLocationRelativeTo( null );
		setVisible( true );
	}

	private void error( String text, Exception e )
	{
		ErrorInfo ei = new ErrorInfo( "Error", text, null, null, e, null, null );
		JXErrorPane.showDialog( this, ei );
	}

	private class SelectEditTypePanel extends WizardPanel
	{
		SelectEditTypePanel()
		{
			ButtonGroup buttonGroup = new ButtonGroup();
			updateSingleRadio = new JRadioButton( "Change address for this customer only" );
			updateAllRadio = new JRadioButton( "Change address for all customers at this address" );
			buttonGroup.add( updateSingleRadio );
			buttonGroup.add( updateAllRadio );
			add( updateSingleRadio );
			add( updateAllRadio );
		}

		public String getTitle() {
			return "Select update type";
		}

		protected void onNext()
		{
			if( updateSingleRadio.isSelected() || updateAllRadio.isSelected() )
			{
				if( updateSingleRadio.isSelected() ) {
					updateForAll = false;
				} else {
					updateForAll = true;
				}

				nextWizardPanel( enterPostcodePanel );
			}
		}

		private JRadioButton updateSingleRadio;
		private JRadioButton updateAllRadio;
	}

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
						AddressCollection addresses = session.getCustomerService().searchForAddressByPostcode( pc );
						if( addresses != null && addresses.size() > 0 )
						{
							selectAddressPanel.display( addresses );
							nextWizardPanel( selectAddressPanel );
						}
						else
						{
							customer.address = new Address();
							customer.address.postcode = pc; 
							editAddressWizardPanel.getEditAddressPanel().fromAddress( customer.address );
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
				customer.address = address;
				new Thread( new EditAddress() ).start();
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
			customer.address = editAddressPanel.toAddress();
			new Thread( new EditAddress() ).start();
		}

		EditAddressPanel editAddressPanel = new EditAddressPanel();
	};

	private class EditAddress implements Runnable
	{
		public void run()
		{
			boolean dispose = false;
			setBusy( true );

			String err = AddressValidator.isValid( customer.address );
			if( err != null )
			{
				error( err, null );
			}
			else
			{
				try
				{
					session.getCustomerService().update( customer, updateForAll );
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
		}
	}

	private SelectEditTypePanel selectEditTypePanel;
	private EnterPostcodePanel enterPostcodePanel;
	private SelectAddressPanel selectAddressPanel;
	private EditAddressWizardPanel editAddressWizardPanel;

	private Session session;
	private Customer customer;

	private boolean updateForAll = false;
}
