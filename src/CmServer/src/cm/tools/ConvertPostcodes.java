package cm.tools;

import java.sql.ResultSet;
import java.sql.SQLException;

import cm.common.entity.db.EntityDbConn;
import cm.util.StringFormat;

public class ConvertPostcodes
{
	public static void main( String[] args ) throws Exception
	{
		new ConvertPostcodes();
	}

	ConvertPostcodes() throws ClassNotFoundException, SQLException
	{
		EntityDbConn db = new EntityDbConn( "localhost:8001", "DundonaldAgencies", "root", "radiat0r" );

		System.out.println( "Querying..." );
		ResultSet rs = db.execute( "select * from Address" );
		System.out.println( "Query complete" );

		int count = 0;

		while( rs.next() )
		{
			String np = "";
			String op = rs.getString( "postcode" );
			String format = StringFormat.toFormat( op );

			if( format.compareTo( "AA9A9AA" ) == 0 )
			{
				np = op.substring( 0,1 )+
					 op.substring( 1,2 )+
					 op.substring( 2,3 )+
					 op.substring( 3,4 )+" "+
					 op.substring( 4,5 )+
					 op.substring( 5,6 )+
					 op.substring( 6,7 );
			}
			else
			if( format.compareTo( "A9A9AA" ) == 0 )
			{
				np = op.substring( 0,1 )+
				     op.substring( 1,2 )+
					 op.substring( 2,3 )+" "+
					 op.substring( 3,4 )+
					 op.substring( 4,5 )+
					 op.substring( 5,6 );
			}
			else
			if( format.compareTo( "A99AA" ) == 0 )
			{
				np = op.substring( 0,1 )+
				     op.substring( 1,2 )+" "+
					 op.substring( 2,3 )+
					 op.substring( 3,4 )+
					 op.substring( 4,5 );
			}
			else
			if( format.compareTo( "A999AA" ) == 0 )
			{
				np = op.substring( 0,1 )+
				     op.substring( 1,2 )+
					 op.substring( 2,3 )+" "+
					 op.substring( 3,4 )+
					 op.substring( 4,5 )+
					 op.substring( 5,6 );
			}
			else
			if( format.compareTo( "AA99AA" ) == 0 )
			{
				np = op.substring( 0,1 )+
				     op.substring( 1,2 )+
					 op.substring( 2,3 )+" "+
					 op.substring( 3,4 )+
					 op.substring( 4,5 )+
					 op.substring( 5,6 );
			}
			else
			if( format.compareTo( "AA999AA" ) == 0 )
			{
				np = op.substring( 0,1 )+
					 op.substring( 1,2 )+
					 op.substring( 2,3 )+
					 op.substring( 3,4 )+" "+
					 op.substring( 4,5 )+
					 op.substring( 5,6 )+
					 op.substring( 6,7 );
			}
			else
			{
				System.out.println( "bad format! " + op );
			}

			if( np.length() > 0 )
			{
				++count;
				System.out.println( op +  " -> " + np );
				db.execute( "update Address set postcode='" + np + "' where postcode='" + op + "'" );
			}
		}

		System.out.println( "converted " + count );
	}
}
