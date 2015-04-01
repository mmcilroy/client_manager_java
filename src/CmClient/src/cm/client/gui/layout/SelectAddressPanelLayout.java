package cm.client.gui.layout;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import cm.client.gui.common.wizard.WizardPanel;
import cm.common.entity.domain.Address;
import cm.util.Util;

@SuppressWarnings("serial")
public class SelectAddressPanelLayout extends WizardPanel
{
	private class AddressContainer
	{
		AddressContainer( Address address ) {
			this.address = address;
		}

		public String toString()
		{
			return String.format( "%s %s %s %s %s",
				Util.nns( address.line1 ),
				Util.nns( address.line2 ),
				Util.nns( address.town ),
				Util.nne( address.county ),
				Util.nns( address.postcode ) );
		}

		Address address;
	}

	protected void add( Address a ) {
		addressModel.addElement( new AddressContainer( a ) );
	}

	protected void clearAddressList() {
		addressList.removeAll();
	}

	protected Address getSelectedAddress()
	{
		AddressContainer c = addressList.getSelectedValue();
		if( c != null ) {
			return c.address;
		}

		return null;
	}

	protected boolean createNewAddress() {
		return createNewRadio.isSelected();
	}

	protected boolean useSelectedAddress() {
		return useSelectedRadio.isSelected();
	}

	private final ButtonGroup buttonGroup = new ButtonGroup();

	private JRadioButton useSelectedRadio;
	private JRadioButton createNewRadio;
	private JList<AddressContainer> addressList;
	private DefaultListModel<AddressContainer> addressModel = new DefaultListModel<AddressContainer>();
	private JScrollPane scrollPane;

	public SelectAddressPanelLayout() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{113, 0};
		gridBagLayout.rowHeights = new int[]{23, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.weighty = 1.0;
		gbc_scrollPane.weightx = 1.0;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		add(scrollPane, gbc_scrollPane);
		
		addressList = new JList<AddressContainer>( addressModel );
		scrollPane.setViewportView(addressList);
		addressList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		useSelectedRadio = new JRadioButton("Customer lives at the selected address");
		buttonGroup.add(useSelectedRadio);
		GridBagConstraints gbc_rdbtnNewRadioButton = new GridBagConstraints();
		gbc_rdbtnNewRadioButton.anchor = GridBagConstraints.NORTHWEST;
		gbc_rdbtnNewRadioButton.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnNewRadioButton.gridx = 0;
		gbc_rdbtnNewRadioButton.gridy = 1;
		add(useSelectedRadio, gbc_rdbtnNewRadioButton);
		
		createNewRadio = new JRadioButton("Customer lives at a different address");
		buttonGroup.add(createNewRadio);
		GridBagConstraints gbc_rdbtnNewRadioButton_1 = new GridBagConstraints();
		gbc_rdbtnNewRadioButton_1.anchor = GridBagConstraints.NORTHWEST;
		gbc_rdbtnNewRadioButton_1.gridx = 0;
		gbc_rdbtnNewRadioButton_1.gridy = 2;
		add(createNewRadio, gbc_rdbtnNewRadioButton_1);
	}
}
