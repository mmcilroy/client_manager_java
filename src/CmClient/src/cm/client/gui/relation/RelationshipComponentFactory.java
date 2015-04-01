package cm.client.gui.relation;

import cm.client.gui.common.CustomComboBox;
import cm.common.types.RelationshipType;

public class RelationshipComponentFactory
{
	public static CustomComboBox newRelationshipCombo( RelationshipType type )
	{
		CustomComboBox ccb = new CustomComboBox();
		ccb.addItem( "" );

		for( RelationshipType t : RelationshipType.values() ) {
			ccb.addItem( t.toString() );
		}

		if( type != null ) {
			ccb.setSelectedItem( type.toString() );
		}

		return ccb;
	}
}
