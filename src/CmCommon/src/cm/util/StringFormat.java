package cm.util;

public class StringFormat
{
	public static String toFormat( String in )
	{
		String format = "";

		if( in != null )
		{
			for( int i=0; i<in.length(); i++ )
			{
				char c = in.charAt( i );

				if( c >= 'A' && c <= 'Z' ) {
					format = format + "A";
				} else if( c >= '0' && c <= '9' ) {
					format = format + "9";
				} else {
					format = format + c;
				}
			}
		}

		return format;
	}
}
