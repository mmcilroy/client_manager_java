package cm.common.entity;

@SuppressWarnings("serial")
public class EntityError extends Exception
{
	public EntityError( String msg ) {
		super( msg );
	}

	public EntityError( String msg, Exception e ) {
		super( msg, e );
	}
}
