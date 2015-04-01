package cm.client;

@SuppressWarnings("serial")
public class ClientError extends Exception
{
	public ClientError( String s ) {
		super( s );
	}

	public ClientError( String s, Exception e ) {
		super( s, e );
	}
}
