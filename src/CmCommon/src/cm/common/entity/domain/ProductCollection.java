package cm.common.entity.domain;

import java.util.LinkedHashMap;

public class ProductCollection extends LinkedHashMap<Integer, Product>
{
	public void add( Product p ) {
		put( p.id, p );
	}

	private static final long serialVersionUID = 6261451438556927236L;
}
