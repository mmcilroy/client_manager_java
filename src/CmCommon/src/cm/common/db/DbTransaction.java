package cm.common.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import cm.common.logger.Logger;

public abstract class DbTransaction
{
	public DbTransaction( DbConn db, String name, String user )
	{
		this.db = db;
		this.name = name;
		this.user = user;
	}

	public DbConn getDb()
	{
		return db;
	}

	public boolean execute()
	{
		boolean success = false;

		try
		{
			db.execute( "begin" );

			PreparedStatement pstmt = db.prepareStatement( "insert into Transaction ( name,createdBy,createdOn ) values ( ?,?,now() )" );
			pstmt.setString( 1, name );
			pstmt.setString( 2, user );
			db.execute( pstmt );

			int txnId = db.getLastInsertId();
			transaction( txnId );

			db.execute( "commit" );
			success = true;
		}
		catch( Exception e1 )
		{
			try
			{
				LOG.warn( "Transaction " + name + " failed... rolling back", e1 );
				db.execute( "rollback" );
			}
			catch( SQLException e2 )
			{
				LOG.error( "Failed to rollback " + name, e2 );
			}
		}

		return success;
	}

	protected abstract void transaction( int id ) throws Exception;

	private DbConn db;
	private String name;
	private String user;

	private static final Logger LOG = Logger.getInstance();
}
