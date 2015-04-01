package cm.client.gui.customer;

import javax.swing.JPanel;

import cm.client.gui.address.AddressComponentFactory;
import cm.client.gui.common.CustomComboBox;
import cm.client.gui.common.CustomTextField;
import cm.client.gui.common.KeyValuePanelBuilder;
import cm.client.gui.common.OkCancelDialog;
import cm.common.types.CustomerSearchParameters;

@SuppressWarnings("serial")
public class CustomerSearchDialog extends OkCancelDialog
{
	public CustomerSearchDialog()
	{
		super( "Search" );
		init();
	}

	protected JPanel initContent()
	{
		KeyValuePanelBuilder b = new KeyValuePanelBuilder( panel );
		b.separator( "Name" );
		b.add( "Id", idField );
		b.add( "Forename", forenameField );
		b.add( "Surname", surnameField );
		b.gap();
		b.separator( "Address" );
		b.add( "Line1", line1Field );
		b.add( "Town", townField );
		b.add( "County", countyField );
		b.add( "Postcode", postcodeField );
		b.gap();

		return panel;
	}

	public CustomerSearchParameters toSearchParameters()
	{
		CustomerSearchParameters csp = new CustomerSearchParameters();
		csp.id = idField.getText();
		csp.forename = forenameField.getText();
		csp.surname = surnameField.getText();
		csp.line1 = line1Field.getText();
		csp.town = townField.getText();
		csp.county = (String)countyField.getSelectedItem();
		csp.postcode = postcodeField.getText();

		return csp;
	}

	protected void onOk()
	{
		isOk( true );
		dispose();
	}

	private JPanel panel = new JPanel();

	private CustomTextField idField = PersonalDetailsComponentFactory.newIdComponent();
	private CustomTextField forenameField = PersonalDetailsComponentFactory.newForenameComponent();
	private CustomTextField surnameField = PersonalDetailsComponentFactory.newSurnameComponent();
	private CustomTextField line1Field = AddressComponentFactory.newAddress1Component();
	private CustomTextField townField = AddressComponentFactory.newTownComponent();
	private CustomComboBox countyField = AddressComponentFactory.newCountyComponent();
	private CustomTextField postcodeField = AddressComponentFactory.newPostcodeComponent();
}
