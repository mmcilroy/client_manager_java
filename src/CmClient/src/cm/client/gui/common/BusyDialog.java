package cm.client.gui.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.JXPanel;

import cm.util.Gbc;

@SuppressWarnings("serial")
public class BusyDialog extends JDialog
{
	public BusyDialog()
	{
		statusLabel.setHorizontalAlignment( SwingConstants.CENTER );
		busyPaneChild.setLayout( new GridBagLayout() );
		busyPaneChild.add( busyLabel, new Gbc( Gbc.HORIZONTAL, 0, 0 ).a( Gbc.SOUTH ) );
		busyPaneChild.add( statusLabel, new Gbc( Gbc.HORIZONTAL, 0, 1 ).a( Gbc.NORTH ) );
		busyPaneChild.setBackground( Color.red );

		busyPaneParent.setLayout( new BorderLayout() );
		busyPaneParent.add( busyPaneChild, BorderLayout.CENTER );
		busyLabel.setHorizontalAlignment( SwingConstants.CENTER );
		busyLabel.setVerticalAlignment( SwingConstants.CENTER );

		setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
		setContentPane( contentPane );
		setGlassPane( busyPaneParent );
	}

	public JXPanel getContentPane()
	{
		return contentPane;
	}

	public void setContentPane( Container contentPane )
	{
		if( contentPane instanceof JXPanel )
		{
			this.contentPane = (JXPanel)contentPane;
			super.setContentPane( contentPane );
		}
	}

	public void setBusy( boolean b )
	{
		busyLabel.setBusy( b );
		busyPaneParent.setVisible( b );

		if( b )
		{
			setStatus( "" );
			busyPaneParent.setAlpha( 0.95f );
			busyPaneChild.setAlpha( 0.95f );
			contentPane.setAlpha( 0.20f );
		}
		else
		{
			contentPane.setAlpha( 1.0f );
		}
	}

	public void setStatus( String s )
	{
		statusLabel.setText( s );
	}

	private JXPanel contentPane = new JXPanel();
	private JXPanel busyPaneParent = new JXPanel();
	private JXPanel busyPaneChild = new JXPanel();
	private JXBusyLabel busyLabel = new JXBusyLabel();
	private JLabel statusLabel = new JLabel();
}
