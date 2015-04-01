package cm.client.gui.task;

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
import cm.common.entity.domain.Task;
import cm.common.types.TaskSearchResult;
import cm.common.types.TaskStatus;
import cm.util.Util;

public class TaskSearchTableManager
{
	public TaskSearchTableManager( final JXTable table )
	{
		this.table = table;

        table.setHighlighters( HighlighterFactory.createAlternateStriping() );
        table.setShowGrid( false );
        table.addMouseListener( new MouseAdapter()
        {
             public void mouseClicked( MouseEvent e )
             {
                 if( e.getClickCount() == 2 && tasks != null )
                 {
                     int row = table.getSelectedRow();
                     if( row > -1 )
                     {
                         TaskSearchResult.Entry task = tasks.get( (Integer)table.getValueAt( row, 0 ) );
                         if( task != null ) {
                        	 onDoubleClick( task.task );
                         }
                     }
                 }
             }
        } );
	}

	public void onTask( Task task )
	{
		if( model != null ) {
			model.onTask( task );
		}
	}

	public void display( TaskSearchResult tasks )
	{
		this.tasks = tasks;

		model = new TaskTableModel( tasks ); 
        table.setModel( model );

		TableColumnExt ext = table.getColumnExt( 0 );
		ext.setComparator( new Comparator<Integer>() {
			public int compare( Integer a, Integer b ) {
				return a.compareTo( b );
			} } );

		ext = table.getColumnExt( 2 );
		ext.setCellRenderer( new DateCellRenderer() );
		ext.setComparator( new Comparator<Date>() {
			public int compare( Date a, Date b ) {
				return a.compareTo( b );
			} } );

        table.doLayout();
	}

	protected void onDoubleClick( Task task ) {
		;
	}

	private JXTable table;
	private TaskTableModel model;
	private TaskSearchResult tasks;

	@SuppressWarnings("serial")
	private class TaskTableModel extends AbstractTableModel
	{
		public TaskTableModel( TaskSearchResult tasks )
	    {
	        rowToTask = new TaskSearchResult.Entry[tasks.size()];

	        int i=0;
	        for( TaskSearchResult.Entry task : tasks.values() )
	        {
	            rowToTask[i] = task;
				taskToRow.put( task.task.id, i++ );
	        }
	    }

		public void onTask( Task task )
		{
			if( task != null && taskToRow.containsKey( task.id ) )
			{
				int row = taskToRow.get( task.id );
				if( row > -1 )
				{
					rowToTask[row].task = task;
					fireTableRowsUpdated( row, row );
				}
			}
		}

	    public int getColumnCount() {
	        return columnNames.length;
	    }

	    public int getRowCount() {
	        return rowToTask.length;
	    }

		public String getColumnName( int col ) {
			return columnNames[col];
		}

		public Object getValueAt( int row, int col )
		{
			TaskSearchResult.Entry task = getTaskAtRow( row );
			if( task != null )
			{
				if( col == 0 ) {
					return task.task.id;
				}
				else if ( col == 1 ) {
					return Util.nne( task.task.action );
				}
				else if ( col == 2 ) {
					return task.task.due;
				}
				else if ( col == 3 )
				{
					if( task.task.status == null || task.task.status == TaskStatus.OUTSTANDING ) {
						return "<html><body color='red'><b>" + Util.nne( task.task.status );
					} else {
						return Util.nne( task.task.status );
					}
				}
				else if( col == 4 )
				{
					return Util.toCustomerName( task.details );
				}
				else if( col == 5 )
				{
					return Util.nns( task.address.line1 );
				}
				else if( col == 6 )
				{
					return Util.nns( task.address.postcode );
				}
			}

			return null;
		}

	    private TaskSearchResult.Entry getTaskAtRow( int row ) {
	        return rowToTask[row];
	    }

		private TaskSearchResult.Entry[] rowToTask;
		private Map<Integer, Integer> taskToRow = new HashMap<Integer, Integer>();
		private String[] columnNames = new String[] { "Id", "Action", "Due", "Status", "Name", "Address", "Postcode" };
	}
}
