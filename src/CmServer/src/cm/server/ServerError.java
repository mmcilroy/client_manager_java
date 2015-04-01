package cm.server;

@SuppressWarnings("serial")
class ServerError extends Exception
{
	public ServerError( String s ) {
		super( s );
	}

	public ServerError( String s, Exception e ) {
		super( s, e );
	}
}
