package cm.client.gui.common;

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class DateCellRenderer extends DefaultTableCellRenderer
{
	public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column )
	{
		super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );

		if ( value instanceof Date && value != null )
		{
			String strDate = df.format( (Date)value );
			setText( strDate );
		}

		return this;
	}

	private static final SimpleDateFormat df = new SimpleDateFormat( "dd/MM/yyyy" );
}