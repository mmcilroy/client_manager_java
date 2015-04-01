package cm.common.types;

import java.io.Serializable;

public class CustomerSearchParameters implements Serializable
{
	public String id;
	public String forename;
	public String surname;
	public String line1;
	public String town;
	public String county;
	public String postcode;

	private static final long serialVersionUID = -149203391622818564L;
}
