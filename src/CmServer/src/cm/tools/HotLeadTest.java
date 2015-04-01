package cm.tools;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.BasicConfigurator;

import cm.common.entity.db.EntityDbConn;

public class HotLeadTest
{
	public static void main( String[] args ) throws Exception
	{
		BasicConfigurator.configure();
		new HotLeadTest();
	}

	HotLeadTest() throws ClassNotFoundException, SQLException
	{
		EntityDbConn db = new EntityDbConn( "31.222.191.71:8001", "DundonaldAgencies", "root", "radiat0r" );

		System.out.println( "Querying..." );

		Map<String, Integer> res = new HashMap<String, Integer>();
		ResultSet rs = db.execute( "select * from Task where action='HOT_LEAD' and transactionId in ( select id from Transaction where name='createTask' and createdOn>='2010-09-24' )" );

		System.out.println( "Query complete" );

		while( rs.next() )
		{
			String user = rs.getString( "createdBy" ).toLowerCase();
			Integer i = res.get( user );

			if( i == null ) {
				i = new Integer( 0 );
			}

			res.put( user, i+1 );
		}

		for( String u : res.keySet() ) {
			System.out.println( u + " = " + res.get( u ) );
		}

		System.out.println( "Done" );
	}
}
