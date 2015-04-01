 package cm.client.gui.product;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.jdesktop.swingx.JXDatePicker;

import cm.client.Session;
import cm.client.gui.common.CustomComboBox;
import cm.client.gui.common.CustomTextField;
import cm.client.gui.common.FloatPanel;
import cm.client.gui.common.KeyValuePanelBuilder;
import cm.common.entity.domain.Product;
import cm.common.logger.Logger;
import cm.common.types.ProductType;
import cm.util.Util;

@SuppressWarnings("serial")
public class EditProductPanel extends JPanel
{
	public EditProductPanel( Session client )
	{
		this( client, null );
	}

	public EditProductPanel( Session client, Product p )
	{
		this.client = client;

		KeyValuePanelBuilder b = new KeyValuePanelBuilder( this );
		b.add( "Type", typeCombo );
		b.add( "Name", nameCombo );
		b.add( "AccountNumber", accountNumberField );
		b.add( "DateOpened", dateOpenedField );
		b.add( "ReviewDate", reviewDateField );
		b.add( "InterestRate %", interestRatePanel );
		b.add( "OpeningBalance £", openingBalancePanel );

		if( p != null ) {
			b.add( "Active", activeCombo );
		}

		b.gap();

		typeCombo.addItem( ProductType.BANKING.toString() );
		typeCombo.addItem( ProductType.SAVINGS.toString() );
		typeCombo.addItem( ProductType.CREDIT_CARD.toString() );

		typeCombo.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				onType();
			} } );

		if( p != null ) {
			fromProduct( p );
		}
	}

	public void fromProduct( Product p )
	{
		this.product = p;

		if( isLegacy( p.type ) ) {
			typeCombo.addItem( p.type.toString() );
		}

		typeCombo.setSelectedItem( Util.nne( p.type ) );
		nameCombo.setSelectedItem( p.name );
		accountNumberField.setText( p.accountNumber );
		dateOpenedField.setDate( p.dateOpened );
		reviewDateField.setDate( p.reviewDate );
		interestRatePanel.fromFloat( p.interestRate );
		openingBalancePanel.fromFloat( p.openingBalance );

		if( p.active ) {
			activeCombo.setSelectedItem( "Y" );
		} else {
			activeCombo.setSelectedItem( "N" );
		}

		if( isLegacy( p.type ) ) {
			typeCombo.setSelectedItem( Util.nne( p.type ) );
		}
	}

	public Product toProduct()
	{
		product.type = getProductType();
		product.name = (String)nameCombo.getSelectedItem();
		product.accountNumber = accountNumberField.getText();
		product.dateOpened = dateOpenedField.getDate();
		product.reviewDate = reviewDateField.getDate();
		product.interestRate = interestRatePanel.toFloat();
		product.openingBalance = openingBalancePanel.toFloat();
		product.active = ((String)activeCombo.getSelectedItem()).compareTo( "Y" ) == 0;

		return product;
	}

	private void onType()
	{
		ProductType type = getProductType();
		nameCombo.removeAllItems();

		if( type != null )
		{
			if( !isLegacy( type ) )
			{
				try
				{
					String[] names = client.getConfiguration().getProductNames( type );
					for( String name : names ) {
						nameCombo.addItem( name );
					}
				}
				catch( Exception e )
				{
					Logger.getInstance().error( "Failed to update product names", e );
				}
			}
			else
			{
				nameCombo.addItem( product.name );
				nameCombo.setSelectedItem( product.name );
			}
	
			boolean enabled = true;
			if( type != ProductType.SAVINGS && type != ProductType.LEGACY_SAVINGS ) {
				enabled = false;
			}

			interestRatePanel.setEnabled( enabled );
			openingBalancePanel.setEnabled( enabled );

			// t says review date should only be enabled on savings products
			reviewDateField.setEnabled( type == ProductType.SAVINGS );
		}
	}

	private ProductType getProductType()
	{
		String t = (String)typeCombo.getSelectedItem();
		if( Util.nns( t ).length() > 0 ) {
			return ProductType.valueOf( t );
		} else {
			return null;
		}
	}

	private boolean isLegacy( ProductType t )
	{
		return t != null &&
			   t == ProductType.LEGACY_BANKING ||
			   t == ProductType.LEGACY_SAVINGS ||
			   t == ProductType.LEGACY_CREDIT_CARD;
	}

	private Session client;
	private Product product = new Product();

	private CustomComboBox typeCombo = ProductComponentFactory.newTypeComponent();
	private CustomComboBox nameCombo = ProductComponentFactory.newNameComponent();
	private CustomTextField accountNumberField = ProductComponentFactory.newAccountNumberComponent();
	private JXDatePicker dateOpenedField = ProductComponentFactory.newDateOpenedComponent();
	private JXDatePicker reviewDateField = ProductComponentFactory.newReviewDateComponent();
	private CustomComboBox activeCombo = ProductComponentFactory.newActiveComponent();

	private FloatPanel interestRatePanel = new FloatPanel();
	private FloatPanel openingBalancePanel = new FloatPanel();
}
