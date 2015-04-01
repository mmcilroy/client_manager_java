package cm.tools;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.BasicConfigurator;

import cm.common.db.DbUtil;
import cm.common.entity.db.EntityDbConn;

public class PopulateReviewDates
{
	public static void main( String[] args ) throws Exception
	{
		BasicConfigurator.configure();
		new PopulateReviewDates();
	}

	PopulateReviewDates() throws ClassNotFoundException, SQLException
	{
		EntityDbConn db = new EntityDbConn( "31.222.191.71:8001", "DundonaldAgencies", "root", "radiat0r" );
		
		ResultSet rs = db.execute( "select * from Task where type='AUTO' and action='REVIEW' and productId is not null order by due" );
		while( rs.next() )
		{
			Date due = rs.getDate( "due" );
			Integer productId = rs.getInt( "productId" );

			Calendar c = new GregorianCalendar();
			c.setTime( due );
			c.add( Calendar.DAY_OF_MONTH, 28 );

			PreparedStatement pstmt = db.prepareStatement( "update Product set reviewDate=? where id=?" );
			pstmt.setDate( 1, DbUtil.sqlDate( c.getTime() ) );
			pstmt.setInt( 2, productId );
			db.execute( pstmt );
		}
	}
}
