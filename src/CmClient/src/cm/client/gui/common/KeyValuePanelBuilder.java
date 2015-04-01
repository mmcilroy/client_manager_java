package cm.client.gui.common;

import java.awt.Component;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jdesktop.swingx.JXTitledSeparator;

import cm.util.Gbc;
import cm.util.Util;

public class KeyValuePanelBuilder
{
	public static void main( String[] args ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
	{
		UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );

		JPanel p = new JPanel();
		KeyValuePanelBuilder b = new KeyValuePanelBuilder( p );
		b.add( "one", new JTextField( 10 ) );
		b.add( "two", new JTextField( 10 ) );
		b.add( "3re", new JTextField( 10 ) );
		b.add( "one", new JTextField( 10 ) );
		b.add( "two", new JTextField( 10 ) );
		b.add( "3re", new JTextField( 10 ) );
		b.gap();

		Util.showPanel( p );
	}

	public KeyValuePanelBuilder( JPanel panel )
	{
		this.panel = panel;

		panel.setVisible( false );
		panel.removeAll();
		panel.setLayout( new GridBagLayout() );
		panel.setVisible( true );
	}

	public void separator( String title )
	{
		int t, r, b, l;

		if( y == 0 ) {
			t = r = l = 10; b = 2;
		} else {
			l = r = 10; t = b = 2;
		}

		panel.add( new JXTitledSeparator( title ), new Gbc( Gbc.BOTH, 0, y++ ).wh( 2, 1 ).i( t, r, b, l ) );
	}

	public void add( String n, Component c )
	{
		int t, r, b, l;

		if( y == 0 ) {
			t = r = l = 10; b = 2;
		} else {
			l = r = 10; t = b = 2;
		}

		panel.add( new JLabel( "<html><b>" + n + " " ), new Gbc( Gbc.NONE, 0, y ).XY( 0, 0 ).a( Gbc.EAST ).i( t, r, b, l ) );
		panel.add( c, new Gbc( Gbc.NONE, 1, y++ ).XY( 0, 0 ).a( Gbc.WEST ).i( t, r, b, l ) );
	}

	public void gap() {
		panel.add( new JLabel( " " ), new Gbc( Gbc.NONE, 0, y++ ).XY( 0, 1 ) );
	}

	private JPanel panel;
	private int y = 0;
}
