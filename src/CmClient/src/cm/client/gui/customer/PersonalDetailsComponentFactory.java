package cm.client.gui.customer;

import org.jdesktop.swingx.JXDatePicker;

import cm.client.gui.common.CustomComboBox;
import cm.client.gui.common.CustomTextField;
import cm.common.types.EmploymentStatus;
import cm.common.types.MaritalStatus;
import cm.common.types.Title;

public class PersonalDetailsComponentFactory
{
	public static CustomComboBox newBusinessOwnerComponent()
	{
		CustomComboBox ccb = new CustomComboBox();
		return ccb;
	}

	public static CustomComboBox newTitleComponent()
	{
		CustomComboBox ccb = new CustomComboBox();
		ccb.addItem( "" );

		for( Title t : Title.values() ) {
			ccb.addItem( t.toString() );
		}

		return ccb;
	}

	public static CustomTextField newIdComponent()
	{
		CustomTextField ctf = new CustomTextField( 20 );
        ctf.setAllowAlpha( false );
        ctf.setAllowDigit( true );

        return ctf;
	}

	public static CustomTextField newForenameComponent()
	{
		CustomTextField ctf = new CustomTextField( 20 );
        ctf.setAllowAlpha( true );
        ctf.setConvertToUpper( true );

        return ctf;
	}

	public static CustomTextField newMiddlenameComponent()
	{
		CustomTextField ctf = new CustomTextField( 20 );
        ctf.setAllowAlpha( true );
        ctf.setConvertToUpper( true );

        return ctf;
	}

	public static CustomTextField newSurnameComponent()
	{
		return newForenameComponent();
	}

	public static CustomTextField newMailingnameComponent()
	{
		CustomTextField ctf = new CustomTextField( 20 );
        ctf.setAllowAlpha( true );
        ctf.setAllowSpace( true );
        ctf.setConvertToUpper( true );

        return ctf;
	}

	public static CustomTextField newSalutationComponent()
	{
		return newMailingnameComponent();
	}

	public static CustomTextField newHomephoneComponent()
	{
		CustomTextField ctf = new CustomTextField( 20 );
		ctf.setAllowDigit( true );

		return ctf;
	}

	public static CustomTextField newMobilephoneComponent()
	{
		return newHomephoneComponent();
	}

	public static CustomTextField newEmailComponent()
	{
		CustomTextField ctf = new CustomTextField( 20 );
        ctf.setAllowAlpha( true );
        ctf.setAllowDigit( true );
        ctf.setAllowSpecial( true );
        ctf.setConvertToUpper( true );

        return ctf;
	}

	public static CustomComboBox newEmploymentStatusComponent()
	{
		CustomComboBox ccb = new CustomComboBox();
		ccb.addItem( "" );

		for( EmploymentStatus es : EmploymentStatus.values() ) {
			ccb.addItem( es.toString() );
		}

		return ccb;
	}

	public static CustomComboBox newMartialStatusComponent()
	{
		CustomComboBox ccb = new CustomComboBox();
		ccb.addItem( "" );

		for( MaritalStatus ms : MaritalStatus.values() ) {
			ccb.addItem( ms.toString() );
		}

		return ccb;
	}

	public static CustomComboBox newDataPermissionComponent()
	{
		CustomComboBox ccb = new CustomComboBox();
		ccb.addItem( "Y" );
		ccb.addItem( "N" );

		return ccb;
	}

	public static CustomComboBox newFFNIComponent()
	{
		CustomComboBox ccb = new CustomComboBox();
		ccb.addItem( "Y" );
		ccb.addItem( "N" );

		return ccb;
	}

	public static JXDatePicker newBirthDateComponent()
	{
		JXDatePicker dp = new JXDatePicker();
		dp.setFormats( new String[] { "dd/MM/yyyy" } );
		return dp;
	}
}
