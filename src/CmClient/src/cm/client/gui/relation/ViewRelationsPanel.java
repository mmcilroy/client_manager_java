package cm.client.gui.relation;

import java.awt.Color;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import cm.client.gui.common.Link;
import cm.common.entity.domain.Customer;
import cm.util.Gbc;
import cm.util.Util;

@SuppressWarnings("serial")
public class ViewRelationsPanel extends JPanel
{
	public ViewRelationsPanel()
	{
		setLayout( new GridBagLayout() );
		setBackground( Color.white );
	}

	public void display( CustomerRelationCollection crc )
	{
//		this.crc = crc;

		setVisible( false );
		removeAll();

		int y = 0; for( CustomerRelation cr : crc.values() )
		{
			if( cr.relationship.live )
			{
				add( new CustomerLink( cr.customer ), new Gbc( Gbc.BOTH, 0, y ) );
				add( new JLabel( cr.relationship.type.toString() ), new Gbc( Gbc.BOTH, 1, y++ ) );
			}
		}
 
		setVisible( true );
	}

	protected void onCustomerSelected( Customer c ) {
		;
	}

//	private CustomerRelationCollection crc;

	private class CustomerLink extends Link
	{
		CustomerLink( Customer c )
		{
			super( Util.toCustomerName( c.personalDetails ) );
			this.c = c;
		}

		protected void onClick() {
			onCustomerSelected( c );
		}

		private Customer c;
	}
}
