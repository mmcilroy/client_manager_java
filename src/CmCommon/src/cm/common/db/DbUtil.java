package cm.common.db;

public class DbUtil
{
	public static java.sql.Date sqlDate()
	{
		return new java.sql.Date( new java.util.Date().getTime() );
	}

	public static java.sql.Date sqlDate( java.util.Date d )
	{
		if( d != null ) {
			return new java.sql.Date( d.getTime() );
		} else {
			return null;
		}
	}
}
