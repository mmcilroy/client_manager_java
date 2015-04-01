package cm.util;


import java.awt.GridBagConstraints;
import java.awt.Insets;

@SuppressWarnings("serial")
public class Gbc extends GridBagConstraints
{
	public Gbc( int fill, int x, int y )
	{
		this.insets = new Insets( 1,1,1,1 );
		this.weightx = 1;
		this.weighty = 1;
		this.fill = fill;
		this.gridx = x;
		this.gridy = y;
	}

	// Weight XY
	public Gbc XY( int X, int Y )
	{
		this.weightx = X;
		this.weighty = Y;
		return this;
	}

	// Width Height
	public Gbc wh( int w, int h )
	{
		this.gridwidth = w;
		this.gridheight = h;
		return this;
	}

	// Anchor
	public Gbc a( int a )
	{
		this.anchor = a;
		return this;
	}

	public Gbc i( int i )
	{
		this.insets = new Insets( i,i,i,i );
		return this;
	}

	public Gbc i( int t, int l, int b, int r )
	{
		this.insets = new Insets( t,l,b,r );
		return this;
	}
}
