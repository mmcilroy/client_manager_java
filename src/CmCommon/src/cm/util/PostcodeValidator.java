package cm.util;

import java.util.HashSet;
import java.util.Set;

public class PostcodeValidator
{
	public static boolean isValid( String postcode )
	{
		if( postcode != null ) {
			return validFormats.contains( StringFormat.toFormat( postcode ) );
		}

		return false;
	}

	private static Set<String> validFormats = new HashSet<String>();

	static
	{
		validFormats.add( "AA9A 9AA" );
		validFormats.add( "A9A 9AA" );
		validFormats.add( "A9 9AA" );
		validFormats.add( "A99 9AA" );
		validFormats.add( "AA9 9AA" );
		validFormats.add( "AA99 9AA" );
	}
}
