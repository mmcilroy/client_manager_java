package cm.client.gui.relation;

import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

import cm.client.gui.common.CustomComboBox;
import cm.common.entity.domain.Customer;
import cm.common.types.RelationshipType;
import cm.util.Gbc;
import cm.util.Util;

@SuppressWarnings("serial")
public class EditRelationsPanel extends JPanel
{
	public EditRelationsPanel()
	{
		setLayout( new GridBagLayout() );
	}

	public void display( CustomerRelationCollection crc )
	{
		if( crc != null && crc.size() > 0 )
		{
			int y = 0; for( CustomerRelation cr : crc.values() )
			{
				CustomComboBox combo = RelationshipComponentFactory.newRelationshipCombo( cr.relationship.type );
				comboMap.put( cr.customer.personalDetails.id, combo );
	
				add( new JLabel( Util.toCustomerName( cr.customer.personalDetails ) ), new Gbc( Gbc.NONE, 0, y ).XY( 0, 0 ).a( Gbc.EAST ) );
				add( combo, new Gbc( Gbc.NONE, 1, y++ ).XY( 0, 0 ).a( Gbc.WEST ) );
			}
		}
		else
		{
			add( new JLabel( "Nobody else lives at this address" ) );
		}
	}

	public RelationshipType getRelationshipType( Customer customer )
	{
		CustomComboBox combo = comboMap.get( customer.personalDetails.id );
		if( combo != null )
		{
			String val = (String)combo.getSelectedItem();
			if( val != null && val.length() > 0 ) {
				return RelationshipType.valueOf( val );
			}
		}

		return null;
	}

	private Map<Integer, CustomComboBox> comboMap = new HashMap<Integer, CustomComboBox>();
}
