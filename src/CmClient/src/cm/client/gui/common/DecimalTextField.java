package cm.client.gui.common;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

@SuppressWarnings("serial")
public class DecimalTextField extends JTextField
{
    public DecimalTextField( int col ) {
        super( col );
    }

    protected Document createDefaultModel() {
        return new UpperCaseDocument();
    }

	class UpperCaseDocument extends PlainDocument
    {
        public void insertString( int offset, String s1, AttributeSet a ) throws BadLocationException
        {
            if( s1 == null ) {
                return;
            }

            if( s1.length() == 1 )
            {
	            StringBuffer s2 = new StringBuffer();
	            for( int i=0; i < s1.length(); i++ )
	            {
	            	char c = s1.charAt( i );
	            	boolean append = false;
	
	            	if( Character.isDigit( c ) )
	            	{
	            		append = true;
	            	}
	            	else
	            	{
	            		if( c == '.' )
	            		{
	            			String t = DecimalTextField.this.getText();
	            			if( t != null && !t.contains( "." ) && t.length() > 0 ) {
	            				append = true;
	            			}
	            		}
	            	}
	
	            	if( append ) {
	        			s2.append( c );
	            	}

	                super.insertString( offset, s2.toString(), a );
	            }
            }
            else
            {
                super.insertString( offset, s1, a );
            }
        }
    }
}