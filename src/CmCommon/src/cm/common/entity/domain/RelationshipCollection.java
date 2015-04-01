package cm.common.entity.domain;

import java.util.LinkedHashMap;

public class RelationshipCollection extends LinkedHashMap<Integer, Relationship>
{
	public void add( Relationship r ) {
		put( r.id, r );
	}

	private static final long serialVersionUID = 6261451438556927236L;
}
