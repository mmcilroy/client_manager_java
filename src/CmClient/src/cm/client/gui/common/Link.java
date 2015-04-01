package cm.client.gui.common;

import java.awt.event.ActionEvent;

import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.hyperlink.AbstractHyperlinkAction;

@SuppressWarnings("serial")
public class Link extends JXHyperlink
{
	@SuppressWarnings({ "rawtypes" })
	public Link( String text )
	{
		setAction( new AbstractHyperlinkAction() {
			public void actionPerformed( ActionEvent e ) {
				onClick();
			} } );

		setText( text );
	}

	protected void onClick() {
		;
	}
}
