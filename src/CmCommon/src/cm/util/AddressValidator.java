package cm.util;

import cm.common.entity.domain.Address;

public class AddressValidator
{
	public static String isValid( Address a )
	{
		if( Util.isNullOrEmpty( a.line1 ) ) {
			return "Line1 is manditory";
		}

		if( Util.isNullOrEmpty( a.town ) ) {
			return "Town is manditory";
		}
			
		if( a.county == null ) {
			return "County is manditory";
		}

		if( !PostcodeValidator.isValid( a.postcode ) ) {
			return "Invalid postcode format";
		}

		return null;
	}
}
