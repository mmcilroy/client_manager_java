package cm.client.gui.product;

import org.jdesktop.swingx.JXDatePicker;

import cm.client.gui.common.CustomComboBox;
import cm.client.gui.common.CustomTextField;
import cm.client.gui.common.DecimalTextField;

public class ProductComponentFactory
{
	public static CustomComboBox newTypeComponent()
	{
		CustomComboBox ccb = new CustomComboBox();
		ccb.addItem( "" );

		return ccb;
	}

	public static CustomComboBox newNameComponent()
	{
		CustomComboBox ccb = new CustomComboBox();
		ccb.addItem( "" );

		return ccb;
	}

	public static CustomTextField newAccountNumberComponent()
	{
		CustomTextField ctf = new CustomTextField( 20 );
        ctf.setAllowAlpha( true );
        ctf.setAllowDigit( true );
        ctf.setAllowSpecial( true );
        ctf.setConvertToUpper( true );

        return ctf;
	}

	public static JXDatePicker newDateOpenedComponent()
	{
		JXDatePicker dp = new JXDatePicker();
		dp.setFormats( new String[] { "dd/MM/yyyy" } );
		return dp;
	}

	public static JXDatePicker newReviewDateComponent()
	{
		JXDatePicker dp = new JXDatePicker();
		dp.setFormats( new String[] { "dd/MM/yyyy" } );
		return dp;
	}

	public static DecimalTextField newInterestRateComponent()
	{
		return new DecimalTextField( 10 );
	}

	public static DecimalTextField newOpeningBalanceComponent()
	{		
		return new DecimalTextField( 10 );
	}

	public static CustomComboBox newActiveComponent()
	{
		CustomComboBox ccb = new CustomComboBox();
		ccb.addItem( "Y" );
		ccb.addItem( "N" );

		return ccb;
	}
}
