package cm.common.entity.db;

@SuppressWarnings("serial")
public class EntityDbError extends Exception
{
	public EntityDbError( String s ) {
		super( s );
	}

	public EntityDbError( String s, Exception e ) {
		super( s, e );
	}
}
