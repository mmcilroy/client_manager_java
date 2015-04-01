package cm.client.gui.customer;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.table.TableColumnExt;

import cm.client.gui.common.DateCellRenderer;
import cm.common.entity.domain.Customer;
import cm.common.entity.domain.CustomerCollection;
import cm.util.Util;

public class CustomerTableManager
{
	public CustomerTableManager( final JXTable table )
	{
		this.table = table;

        table.setHighlighters( HighlighterFactory.createAlternateStriping() );
        table.setShowGrid( false );
        table.addMouseListener( new MouseAdapter()
        {
             public void mouseClicked( MouseEvent e )
             {
                 if( e.getClickCount() == 2 && customers != null )
                 {
                     int row = table.getSelectedRow();
                     if( row > -1 )
                     {
                         Customer customer = customers.get( (Integer)table.getValueAt( row, 0 ) );
                         if( customer != null ) {
                        	 onDoubleClick( customer );
                         }
                     }
                 }
             }
        } );
	}

	public void onCustomer( Customer customer )
	{
		if( model != null ) {
			model.onCustomer( customer );
		}
	}

	public void display( CustomerCollection customers )
	{
		this.customers = customers;
		model = new CustomerTableModel( customers );
        table.setModel( model );

		TableColumnExt ext = table.getColumnExt( 0 );
		ext.setComparator( new Comparator<Integer>() {
			public int compare( Integer a, Integer b ) {
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

	public Customer getSelectedCustomer()
	{
        int row = table.getSelectedRow();
        if( row > -1 ) {
            return customers.get( (Integer)table.getValueAt( row, 0 ) );
        }

        return null;
	}

	protected void onDoubleClick( Customer customer ) {
		;
	}

	private JXTable table;
	private CustomerTableModel model;
	private CustomerCollection customers;

	@SuppressWarnings("serial")
	private class CustomerTableModel extends AbstractTableModel
	{
		public CustomerTableModel( CustomerCollection customers )
	    {
	        rowToCustomer = new Customer[customers.size()];

	        int i=0;
	        for( Customer customer : customers.values() )
	        {
	            rowToCustomer[i] = customer;
				customerToRow.put( customer.personalDetails.id, i );
				addressToRow.put( customer.address.id, i++ );
	        }
	    }

		public void onCustomer( Customer customer )
		{
			if( customer.personalDetails != null && customerToRow.containsKey( customer.getId() ) )
			{
				int row = customerToRow.get( customer.getId() );
				if( row > -1 )
				{
					rowToCustomer[row].personalDetails = customer.personalDetails;
					fireTableRowsUpdated( row, row );
				}
			}

			if( customer.address != null && addressToRow.containsKey( customer.address.id ) )
			{
				int row = addressToRow.get( customer.address.id );
				if( row > -1 )
				{
					rowToCustomer[row].address = customer.address;
					fireTableRowsUpdated( row, row );
				}
			}
		}

	    public int getColumnCount() {
	        return columnNames.length;
	    }

	    public int getRowCount() {
	        return rowToCustomer.length;
	    }

		public String getColumnName( int col ) {
			return columnNames[col];
		}

	    public Object getValueAt( int row, int col )
	    {
	        Customer customer = getCustomerAtRow( row );
	        if( customer != null )
	        {
	        	if( customer.personalDetails != null )
	        	{
		            if( col == 0 ) {
		                return customer.personalDetails.id;
		            }
		            else if ( col == 1 ) {
		                return Util.nns( customer.personalDetails.businessOwner );
		            }
		            else if ( col == 2 ) {
		                return Util.nne( customer.personalDetails.title );
		            }
		            else if ( col == 3 ) {
		                return Util.nns( customer.personalDetails.forename );
		            }
		            else if ( col == 4 ) {
		                return Util.nns( customer.personalDetails.surname );
		            }
		            else if ( col == 5 ) {
		                return customer.personalDetails.birthDate;
		            }
	        	}

	        	if( customer.address != null )
	        	{
		            if( col == 6 ) {
		                return Util.nns( customer.address.line1 ) + " " + Util.nns( customer.address.line2 );
		            }
		            else if( col == 7 ) {
		                return Util.nns( customer.address.town );
		            }
		            else if( col == 8 ) {
		                return Util.nne( customer.address.county );
		            }
		            else if( col == 9 ) {
		                return Util.nns( customer.address.postcode );
		            }
	        	}
	        }

	        return null;
	    }

	    private Customer getCustomerAtRow( int row ) {
	        return rowToCustomer[row];
	    }

	    private Customer[] rowToCustomer;
		private Map<Integer, Integer> customerToRow = new HashMap<Integer, Integer>();
		private Map<Integer, Integer> addressToRow = new HashMap<Integer, Integer>();

	    private String[] columnNames = new String[] { "Id", "BusinessOwner", "Title", "Forename", "Surname", "BirthDate", "Address", "Town", "County", "Postcode" };
	}
}
