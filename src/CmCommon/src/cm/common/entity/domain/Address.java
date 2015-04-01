package cm.common.entity.domain;

import java.io.Serializable;

import cm.common.entity.Entity;
import cm.common.types.County;

public class Address extends Entity implements Serializable
{
	public String line1;
	public String line2;
	public String town;
	public County county;
	public String postcode;

	public String toString() {
		return String.format( "%d : %s : %s : %s : %s : %s", id, line1, line2, town, county, postcode );
	}

	private static final long serialVersionUID = 5087251780859894829L;
}
