package cm.client.gui.address;

import javax.swing.JPanel;

import cm.client.gui.common.CustomComboBox;
import cm.client.gui.common.CustomTextField;
import cm.client.gui.common.KeyValuePanelBuilder;
import cm.common.entity.domain.Address;
import cm.common.types.County;
import cm.util.Util;

@SuppressWarnings("serial")
public class EditAddressPanel extends JPanel
{
	public EditAddressPanel()
	{
		KeyValuePanelBuilder b = new KeyValuePanelBuilder( this );
		b.add( "Address1", line1Field );
		b.add( "Address2", line2Field );
		b.add( "Town", townField );
		b.add( "County", countyCombo );
		b.add( "Postcode", postcodeField );
		b.gap();
	}

	public void reset()
	{
		line1Field.setText( "" );
		line2Field.setText( "" );
		townField.setText( "" );
		countyCombo.setSelectedItem( "" );
		postcodeField.setText( "" );
	}

	public void fromAddress( Address a )
	{
		this.address = a;

		line1Field.setText( a.line1 );
		line2Field.setText( a.line2 );
		townField.setText( a.town );
		postcodeField.setText( a.postcode );
		countyCombo.setSelectedItem( Util.nne( a.county ) );
	}

	public Address toAddress()
	{
		address.line1 = line1Field.getText();
		address.line2 = line2Field.getText();
		address.town = townField.getText();
		address.postcode = postcodeField.getText();

		String c = (String)countyCombo.getSelectedItem();
		if( Util.nns( c ).length() == 0 ) {
			address.county = null;
		} else {
			address.county = County.valueOf( c );
		}

		return address;
	}

	private Address address = new Address();

	private CustomTextField line1Field = AddressComponentFactory.newAddress1Component();
	private CustomTextField line2Field = AddressComponentFactory.newAddress2Component();
	private CustomTextField townField = AddressComponentFactory.newTownComponent();
	private CustomComboBox countyCombo = AddressComponentFactory.newCountyComponent();
	private CustomTextField postcodeField = AddressComponentFactory.newPostcodeComponent();
}
