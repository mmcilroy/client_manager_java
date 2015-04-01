package cm.client.gui.common;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import cm.util.Util;

@SuppressWarnings("serial")
public class FloatPanel extends JPanel
{
	public FloatPanel()
	{
		integerComponent.setHorizontalAlignment( SwingConstants.RIGHT );

		FlowLayout l = new FlowLayout();
		l.setHgap( 0 );
		l.setVgap( 0 );
		setLayout( l );
		add( integerComponent );
		add( new JLabel( " . " ) );
		add( fractionComponent );
	}

	public CustomTextField createIntegerComponent()
	{
		CustomTextField ctf = new CustomTextField( 8 );
        ctf.setAllowDigit( true );

        return ctf;
	}

	public CustomTextField createFractionalComponent()
	{
		CustomTextField ctf = new CustomTextField( 3 );
        ctf.setAllowDigit( true );
		ctf.setMaximumLength( 2 );

        return ctf;
	}

	public String getInteger()
	{
		return Util.nns( integerComponent.getText() );
	}

	public String getFraction()
	{
		return Util.nns( fractionComponent.getText() );
	}

	public Float toFloat()
	{
		String i = getInteger();
		String f = getFraction();

		if( i.length() == 0 && f.length() == 0 ) {
			return null;
		}

		if( i.length() == 0 ) {
			i = "0";
		}

		if( f.length() == 0 ) {
			f = "0";
		}

		try {
			return Float.parseFloat( i+"."+f );
		} catch( Exception e ) {
			return null;
		}
	}

	public void fromFloat( Float f )
	{
		String fs = String.format( "%.2f", f );
		String s[] = fs.split( "\\." );

		integerComponent.setText( "" );
		fractionComponent.setText( "" );

		if( s != null )
		{
			if( s.length == 1 )
			{
				integerComponent.setText( s[0] );
				fractionComponent.setText( "" );
			}
			else
			if( s.length == 2 )
			{
				integerComponent.setText( s[0] );
				fractionComponent.setText( s[1] );
			}
		}
	}

	public void setEnabled( boolean e )
	{
		integerComponent.setEnabled( e );
		fractionComponent.setEnabled( e );
	}

	private CustomTextField integerComponent = createIntegerComponent();
	private CustomTextField fractionComponent = createFractionalComponent();
}
