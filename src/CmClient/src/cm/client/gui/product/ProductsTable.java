package cm.client.gui.product;

import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.table.TableColumnExt;

import cm.client.gui.common.DateCellRenderer;
import cm.common.entity.domain.Product;
import cm.common.entity.domain.ProductCollection;
import cm.util.Gbc;
import cm.util.Util;

@SuppressWarnings("serial")
public class ProductsTable extends JPanel
{
	public ProductsTable()
	{
		initLayout();
		initComponents();
	}

	public void initLayout()
	{
		setLayout( new GridBagLayout() );
		add( new JScrollPane( table ), new Gbc( Gbc.BOTH, 0, 0 ) );
	}

	public void initComponents()
	{
        table.setHighlighters( HighlighterFactory.createAlternateStriping() );
        table.setShowGrid( false );
        table.addMouseListener( new MouseAdapter()
        {
             public void mouseClicked( MouseEvent e )
             {
                 if( e.getClickCount() == 2 && products != null )
                 {
                     int row = table.getSelectedRow();
                     if( row > -1 )
                     {
                         Product product = products.get( (Integer)table.getValueAt( row, 0 ) );
                         if( product != null ) {
                        	 onDoubleClick( product );
                         }
                     }
                 }
             }
        } );
	}

	public void display( ProductCollection products )
	{
		this.products = products;

		model = new ProductTableModel( products ); 
        table.setModel( model );

		TableColumnExt ext = table.getColumnExt( 4 );
		ext.setCellRenderer( new DateCellRenderer() );
		ext.setComparator( new Comparator<Date>() {
			public int compare( Date a, Date b ) {
				return a.compareTo( b );
			} } );

		ext = table.getColumnExt( 5 );
		ext.setCellRenderer( new DateCellRenderer() );
		ext.setComparator( new Comparator<Date>() {
			public int compare( Date a, Date b ) {
				return a.compareTo( b );
			} } );

        table.doLayout();
	}

	protected void onDoubleClick( Product product )
	{
		;
	}

	private class ProductTableModel extends AbstractTableModel
	{
		public ProductTableModel( ProductCollection products )
	    {
	        rowToProduct = new Product[products.size()];

	        int i=0;
	        for( Product product : products.values() )
	        {
				productToRow.put( product.id, i );
	            rowToProduct[i++] = product;
	        }
	    }

	    public int getColumnCount()
	    {
	        return columnNames.length;
	    }

	    public int getRowCount()
	    {
	        return rowToProduct.length;
	    }

		public String getColumnName( int col )
		{
			return columnNames[col];
		}

		public Object getValueAt( int row, int col )
		{
			Product product = getProductAtRow( row );
			if( product != null )
			{
				if( col == 0 ) {
					return product.id;
				}
				else if ( col == 1 ) {
					return Util.nne( product.type );
				}
				else if ( col == 2 ) {
					return Util.nns( product.name );
				}
				else if ( col == 3 ) {
					return Util.nns( product.accountNumber );
				}
				else if ( col == 4 ) {
					return product.dateOpened;
				}
				else if ( col == 5 ) {
					return product.reviewDate;
				}
				else if ( col == 6 ) {
					return product.active == null ? "N" : ( product.active ? "Y" : "N" );
				}
			}

			return null;
		}

	    private Product getProductAtRow( int row )
	    {
	        return rowToProduct[row];
	    }

		private Product[] rowToProduct;

		private Map<Integer, Integer> productToRow = new HashMap<Integer, Integer>();
		
		private String[] columnNames = new String[] { "Id", "Type", "Name", "Number", "DateOpened", "ReviewDate", "Active" };

	}

	private ProductCollection products;
	private ProductTableModel model;
	private JXTable table = new JXTable();
}
