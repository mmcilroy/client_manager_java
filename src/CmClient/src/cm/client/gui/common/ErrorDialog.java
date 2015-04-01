package cm.client.gui.common;

import java.awt.Component;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

public class ErrorDialog
{
	public static void show( Component owner, String title, String message )
	{
		ErrorInfo ei = new ErrorInfo( title, message, null, null, null, null, null );
		JXErrorPane.showDialog( owner, ei );
	}

	public static void show( Component owner, String title, Throwable t )
	{
		ErrorInfo ei = new ErrorInfo( title, t.getMessage(), null, null, t, null, null );
		JXErrorPane.showDialog( owner, ei );
	}
}
