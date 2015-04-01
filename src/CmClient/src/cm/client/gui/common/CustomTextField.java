package cm.client.gui.common;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

@SuppressWarnings("serial")
public class CustomTextField extends JTextField
{
    public CustomTextField( int col ) {
        super( col );
    }

    public void setAllowAlpha( boolean allowAlpha ) {
		this.allowAlpha = allowAlpha;
	}

	public void setAllowDigit( boolean allowDigit ) {
		this.allowDigit = allowDigit;
	}

	public void setAllowSpace( boolean allowSpace ) {
		this.allowSpace = allowSpace;
	}

	public void setAllowSpecial( boolean allowSpecial ) {
		this.allowSpecial = allowSpecial;
	}

	public void setConvertToUpper( boolean convertToUpper ) {
		this.convertToUpper = convertToUpper;
	}

	public void setMaximumLength( int maximumLength ) {
		this.maximumLength = maximumLength;
	}

    protected Document createDefaultModel() {
        return new UpperCaseDocument();
    }

    private boolean allowAlpha = false;
    private boolean allowDigit = false;
	private boolean allowSpace = false;
	private boolean allowSpecial = false;
	private boolean convertToUpper = false;
    private int maximumLength = -1;

	class UpperCaseDocument extends PlainDocument
    {
        public void insertString( int offset, String s1, AttributeSet a ) throws BadLocationException
        {
            if( s1 == null ) {
                return;
            }

            if( maximumLength > 0 && CustomTextField.this.getText().length() > maximumLength-1 ) {
            	return;
            }

            StringBuffer s2 = new StringBuffer();
            for( int i=0; i < s1.length(); i++ )
            {
            	char c = s1.charAt( i );
            	boolean append = false;

            	if( Character.isLetter( c ) && allowAlpha ) {
            		append = true;
            	}
            	else
            	if( Character.isDigit( c ) && allowDigit ) {
            		append = true;
            	}
            	else
            	if( c == ' ' && allowSpace ) {
            		append = true;
            	}
            	else
            	if( allowSpecial ) {
            		append = true;
            	}

            	if( append )
            	{
            		if( convertToUpper ) {
            			s2.append( Character.toUpperCase( c ) );
            		} else {
            			s2.append( c );
            		}
            	}
            }

            super.insertString( offset, s2.toString(), a );
        }
    }
}