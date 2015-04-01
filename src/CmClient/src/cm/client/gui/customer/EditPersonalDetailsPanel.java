 package cm.client.gui.customer;

import javax.swing.JPanel;

import org.jdesktop.swingx.JXDatePicker;

import cm.client.Session;
import cm.client.gui.common.CustomComboBox;
import cm.client.gui.common.CustomTextField;
import cm.client.gui.common.KeyValuePanelBuilder;
import cm.common.entity.domain.PersonalDetails;
import cm.common.types.EmploymentStatus;
import cm.common.types.MaritalStatus;
import cm.common.types.Title;
import cm.util.Util;

@SuppressWarnings("serial")
public class EditPersonalDetailsPanel extends JPanel
{
	public EditPersonalDetailsPanel( Session session )
	{
		ownerCombo.addItem( "" );
		for( String biz : session.getConfiguration().getBusinessOwners() ) {
			ownerCombo.addItem( biz );
		}

		KeyValuePanelBuilder b = new KeyValuePanelBuilder( this );
		b.add( "BusinessOwner", ownerCombo );
		b.add( "Title", titleCombo );
		b.add( "Forename", forenameField );
		b.add( "Middlename", middlenameField );
		b.add( "Surname", surnameField );
		b.add( "Mailingname", mailingnameField );
		b.add( "Salutation", salutationField );
		b.add( "BirthDate", birthDateField );
		b.add( "HomePhone", homePhoneField );
		b.add( "MobilePhone", mobilePhoneField );
		b.add( "Email", emailField );
		b.add( "EmploymentStatus", employmentStatusField );
		b.add( "MartialStatus", martialStatusField );
		b.add( "DataPermission", dataPermissionCombo );
		b.add( "FFNI", ffniCombo );
		b.gap();
	}

	public EditPersonalDetailsPanel( Session client, PersonalDetails pd )
	{
		this( client );
		fromPersonalDetails( pd );
	}

	public void fromPersonalDetails( PersonalDetails details )
	{
		this.details = details;

		ownerCombo.setSelectedItem( details.businessOwner );
		titleCombo.setSelectedItem( Util.nne( details.title ) );
		forenameField.setText( details.forename );
		middlenameField.setText( details.middlename );
		surnameField.setText( details.surname );
		mailingnameField.setText( details.mailingname );
		salutationField.setText( details.salutation );
		birthDateField.setDate( details.birthDate );
		homePhoneField.setText( details.homePhone );
		mobilePhoneField.setText( details.mobilePhone );
		emailField.setText( details.email );
		employmentStatusField.setSelectedItem( Util.nne( details.employmentStatus ) );
		martialStatusField.setSelectedItem( Util.nne( details.maritalStatus ) );

		if( details.dataPermission ) {
			dataPermissionCombo.setSelectedItem( "Y" );
		} else {
			dataPermissionCombo.setSelectedItem( "N" );
		}

		if( details.ffni ) {
			ffniCombo.setSelectedItem( "Y" );
		} else {
			ffniCombo.setSelectedItem( "N" );
		}
	}
 
	public PersonalDetails toPersonalDetails()
	{
		details.businessOwner = (String)ownerCombo.getSelectedItem();
		details.forename = forenameField.getText();
		details.middlename = middlenameField.getText();
		details.surname = surnameField.getText();
		details.mailingname = mailingnameField.getText();
		details.salutation = salutationField.getText();
		details.birthDate = birthDateField.getDate();
		details.homePhone = homePhoneField.getText();
		details.mobilePhone = mobilePhoneField.getText();
		details.email = emailField.getText();

		String t = (String)titleCombo.getSelectedItem();
		if( Util.nns( t ).length() == 0 ) {
			details.title = null;
		} else {
			details.title = Title.valueOf( t );
		}

		String es = (String)employmentStatusField.getSelectedItem();
		if( Util.nns( es ).length() == 0 ) {
			details.employmentStatus = null;
		} else {
			details.employmentStatus = EmploymentStatus.valueOf( es );
		}

		String ms = (String)martialStatusField.getSelectedItem();
		if( Util.nns( ms ).length() == 0 ) {
			details.maritalStatus = null;
		} else {
			details.maritalStatus = MaritalStatus.valueOf( ms );
		}

		details.dataPermission = ((String)dataPermissionCombo.getSelectedItem()).compareTo( "Y" ) == 0;
		details.ffni = ((String)ffniCombo.getSelectedItem()).compareTo( "Y" ) == 0;

		return details;
	}

	private PersonalDetails details = new PersonalDetails();

	private CustomComboBox ownerCombo = PersonalDetailsComponentFactory.newBusinessOwnerComponent();
	private CustomComboBox titleCombo = PersonalDetailsComponentFactory.newTitleComponent();
	private CustomTextField forenameField = PersonalDetailsComponentFactory.newForenameComponent();
	private CustomTextField middlenameField = PersonalDetailsComponentFactory.newMiddlenameComponent();
	private CustomTextField surnameField = PersonalDetailsComponentFactory.newSurnameComponent();
	private CustomTextField mailingnameField = PersonalDetailsComponentFactory.newMailingnameComponent();
	private CustomTextField salutationField = PersonalDetailsComponentFactory.newSalutationComponent();
	private CustomTextField homePhoneField = PersonalDetailsComponentFactory.newHomephoneComponent();
	private CustomTextField mobilePhoneField = PersonalDetailsComponentFactory.newMobilephoneComponent();
	private CustomTextField emailField = PersonalDetailsComponentFactory.newEmailComponent();
	private CustomComboBox employmentStatusField = PersonalDetailsComponentFactory.newEmploymentStatusComponent();
	private CustomComboBox martialStatusField = PersonalDetailsComponentFactory.newMartialStatusComponent();
	private CustomComboBox dataPermissionCombo = PersonalDetailsComponentFactory.newDataPermissionComponent();
	private CustomComboBox ffniCombo = PersonalDetailsComponentFactory.newFFNIComponent();
	private JXDatePicker birthDateField = PersonalDetailsComponentFactory.newBirthDateComponent();
}
