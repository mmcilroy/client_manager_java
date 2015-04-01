package cm.common.entity.domain;

import java.util.LinkedHashMap;

public class CustomerCollection extends LinkedHashMap<Integer, Customer>
{
	public void add( Customer customer ) {
		put( customer.personalDetails.id, customer );
	}

	private static final long serialVersionUID = -1087164405722013182L;
}
