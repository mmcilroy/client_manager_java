package cm.common.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;

import cm.common.logger.Logger;

public class DbConn
{
	public DbConn( String host, String db, String user, String pass ) throws ClassNotFoundException, SQLException
	{
		connStr = String.format( "jdbc:mysql://%s/%s?user=%s&password=%s", host, db, user, pass );
		
		LOG.debug( "Loading MYSQL driver" );
		Class.forName( "com.mysql.jdbc.Driver" );

		LOG.debug( "Connecting to {}", connStr );
		connection = DriverManager.getConnection( connStr );
	}

	public synchronized void setAutoCommit( boolean enable ) throws SQLException {
		execute( "set autocommit=" + ( enable ? "1" : "0" ) );
	}

	public synchronized void setKeepAlive( int sec )
	{
		//timer.cancel();
		timer.schedule( new KeepAliveTask(), sec*1000, sec*1000 );
	}

	public synchronized int getLastInsertId() throws SQLException
	{
		ResultSet rs = execute( "select last_insert_id()" );
		if( rs.next() )
		{
			int id = rs.getInt( 1 );
			LOG.debug( "last_insert_id = {}", id );
			return id;
		}

		throw new SQLException( "getLastInsertId failed" );
	}

	public synchronized PreparedStatement prepareStatement( String sql ) throws SQLException
	{
		PreparedStatement pstmt = connection.prepareStatement( sql );
		return pstmt;
	}

	public synchronized ResultSet execute( PreparedStatement stmt ) throws SQLException
	{
		LOG.debug( "Execute: {}", stmt );

		stmt.execute();
		return stmt.getResultSet();
	}

	public synchronized ResultSet execute( String sql ) throws SQLException
	{
		LOG.debug( "Execute: {}", sql );
		
		System.out.println( "Execute: " + sql );

		Statement stmt = connection.createStatement();
		stmt.execute( sql );
		return stmt.getResultSet();
	}

	private String connStr;
	private Connection connection;

	private class KeepAliveTask extends TimerTask
	{
		public void run()
		{
			try {
				execute( "select now()" );
			} catch( SQLException e ) {
				LOG.error( "Keepalive error", e );
			}
		}
	}

	private Timer timer = new Timer();
	private static final Logger LOG = Logger.getInstance();
}
