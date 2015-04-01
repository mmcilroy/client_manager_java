package cm.client.gui.address;

import cm.client.gui.common.CustomComboBox;
import cm.client.gui.common.CustomTextField;
import cm.common.types.County;

public class AddressComponentFactory
{
	public static CustomTextField newAddress1Component()
	{
		CustomTextField ctf = new CustomTextField( 20 );
        ctf.setAllowAlpha( true );
        ctf.setAllowDigit( true );
        ctf.setAllowSpace( true );
        ctf.setConvertToUpper( true );

        return ctf;
	}

	public static CustomTextField newAddress2Component()
	{
		return newAddress1Component();
	}

	public static CustomTextField newTownComponent()
	{
		CustomTextField ctf = new CustomTextField( 20 );
        ctf.setAllowAlpha( true );
        ctf.setAllowDigit( true );
        ctf.setConvertToUpper( true );

        return ctf;
	}

	public static CustomComboBox newCountyComponent()
	{
		CustomComboBox ccb = new CustomComboBox();
		ccb.addItem( "" );

		for( County c : County.values() ) {
			ccb.addItem( c.toString() );
		}

		return ccb;
	}

	public static CustomTextField newPostcodeComponent()
	{
		CustomTextField ctf = new CustomTextField( 10 );
        ctf.setAllowAlpha( true );
        ctf.setAllowDigit( true );
        ctf.setAllowSpace( true );
        ctf.setConvertToUpper( true );
        ctf.setMaximumLength( 8 );

        return ctf;
	}
}
