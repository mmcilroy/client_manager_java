package cm.tools;

import java.sql.SQLException;

import org.apache.log4j.BasicConfigurator;

import cm.common.db.DbConn;
import cm.common.db.DbTransaction;
import cm.common.entity.db.EntityDbConn;
import cm.common.entity.domain.PersonalDetails;

public class TransactionFailureTest
{
	public static void main( String[] args ) throws ClassNotFoundException, SQLException
	{
		BasicConfigurator.configure();
		new TransactionFailureTest();
	}

	TransactionFailureTest() throws ClassNotFoundException, SQLException
	{
		db = new EntityDbConn( "31.222.191.71:8001", "DundonaldAgencies", "root", "radiat0r" );
		
		BadTransaction t = new BadTransaction( db );
		t.execute();
	}

	class BadTransaction extends DbTransaction
	{
		public BadTransaction( DbConn db )
		{
			super( db, "BadTransaction", "mmcilroy" );
		}

		protected void transaction( int id ) throws Exception
		{
			PersonalDetails pd = new PersonalDetails();
			pd.forename = "MARK";
			pd.surname = "MCILROY";

			db.insert( pd );
		}
	}

	private EntityDbConn db;
}
