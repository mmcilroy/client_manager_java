package cm.common.entity.db;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import cm.common.db.DbConn;
import cm.common.entity.Entity;

public class EntityDbConn extends DbConn
{
	public EntityDbConn( String host, String db, String user, String pass ) throws ClassNotFoundException, SQLException
	{
		super( host, db, user, pass );
	}

	public void insert( Entity e ) throws EntityDbError
	{
		e.id = null;
		e.createdOn = new Date();

		Field[] fields = e.getClass().getFields();

		StringBuffer cols = new StringBuffer();
		for( int i=0; i<fields.length; i++ )
		{
			cols.append( fields[i].getName() );
			if( i != fields.length-1 ) {
				cols.append( "," );
			}
		}

		StringBuffer vals = new StringBuffer();
		for( int i=0; i<fields.length; i++ )
		{
			vals.append( "?" );
			if( i != fields.length-1 ) {
				vals.append( "," );
			}
		}

		try
		{
			PreparedStatement pstmt = prepareStatement( String.format(
				"insert into %s ( %s ) values ( %s )",
				e.getClass().getSimpleName(),
				cols,
				vals ) );
	
			for( int i=1; i<=fields.length; i++ ) {
				addField( e, fields[i-1], pstmt, i );
			}
	
			execute( pstmt );

			e.id = getLastInsertId();
		}
		catch( SQLException e1 )
		{
			throw new EntityDbError( "Failed to insert entity", e1 );
		}
	}

	public void update( Entity e ) throws EntityDbError
	{
		e.createdOn = new Date();

		Field[] fields = e.getClass().getFields();

		StringBuffer data = new StringBuffer();
		for( int i=0; i<fields.length; i++ )
		{
			data.append( fields[i].getName() + "=?" );
			if( i != fields.length-1 ) data.append( "," );
		}

		String table = e.getClass().getSimpleName();

		try
		{
			int version;

			ResultSet rs = execute( String.format( "select max( version ) from %sHistory where id=%d", table, e.id ) );
			if( rs.next() ) {
				version = rs.getInt( 1 ) + 1;
			} else {
				version = 1;
			}

			execute( String.format( "insert into %sHistory select *,%d as version from %s where id=%d", table, version, table, e.id ) );

			PreparedStatement pstmt = prepareStatement( String.format( "update %s set %s where id=%d",
				table,
				data,
				e.id ) );
	
			for( int i=1; i<=fields.length; i++ ) {
				addField( e, fields[i-1], pstmt, i );
			}
	
			execute( pstmt );
		}
		catch( SQLException e1 )
		{
			e1.printStackTrace();
			throw new EntityDbError( "Failed to update entity", e1 );
		}
	}

	public void deactivate( Entity e ) throws EntityDbError
	{
		e.createdOn = new Date();
		e.live = false;

		try {
			execute( String.format( "update %s set live=0 where id=%d", e.getClass().getSimpleName(), e.id ) );
		} catch( SQLException e1 ) {
			throw new EntityDbError( "Failed to deactivate entity", e1 );
		}
	}

	private void addField( Entity e, Field field, PreparedStatement pstmt, int i ) throws EntityDbError
	{
		try
		{
			Object o = field.get( e );
			if( o == null )
			{
				pstmt.setObject( i, null );
			}
			else
			{
				if( o instanceof Integer ) {
					pstmt.setInt( i, (Integer)o );
				}
				else
				if( o instanceof Float ) {
					pstmt.setFloat( i, (Float)o );
				}
				else
				if( o instanceof String ) {
					pstmt.setString( i, (String)o );
				}
				else
				if( o instanceof Boolean ) {
					pstmt.setBoolean( i, (Boolean)o );
				}
				else
				if( o instanceof Enum ) {
					pstmt.setString( i, (String)o.toString() );
				}
				else
				if( o instanceof Date )
				{
					// hack
					// need seperate date / time / datetime
					String entityName = e.getClass().getSimpleName();
					String fieldName = field.getName();
					Calendar utc = new GregorianCalendar( TimeZone.getTimeZone( "UTC" ) );

					if( entityName.startsWith( "PersonalDetails" ) && fieldName.compareTo( "birthDate" ) == 0 ) {
						pstmt.setDate( i, new java.sql.Date( ((Date)o).getTime() ), utc );
					}
					else
					if( entityName.startsWith( "Task" ) && fieldName.compareTo( "due" ) == 0 ) {
						pstmt.setDate( i, new java.sql.Date( ((Date)o).getTime() ), utc );
					}
					else
					if( entityName.startsWith( "Appointment" ) && ( fieldName.compareTo( "startTime" ) == 0 || fieldName.compareTo( "endTime" ) == 0 ) ) {
						pstmt.setTime( i, new java.sql.Time( ((Date)o).getTime() ), utc );
					}
					else
					if( entityName.startsWith( "Appointment" ) && fieldName.compareTo( "day" ) == 0 ) {
						pstmt.setDate( i, new java.sql.Date( ((Date)o).getTime() ), utc );
					}
					else {
						pstmt.setTimestamp( i, new java.sql.Timestamp( ((Date)o).getTime() ), utc );
					}
				}
			}
		}
		catch( Exception e1 )
		{
			throw new EntityDbError( "Failed to add field " + field.getName() + " to prepared statement", e1 );
		}
	}

//	private static final Logger LOG = LoggerFactory.getLogger( EntityDbConn.class );
}
