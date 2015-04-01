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
import cm.common.entity.domain.TaskCollection;
import cm.common.types.TaskStatus;
import cm.util.Util;

public class TaskTableManager
{
	public TaskTableManager( final JXTable table )
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
                         Task task = tasks.get( (Integer)table.getValueAt( row, 0 ) );
                         if( task != null ) {
                        	 onDoubleClick( task );
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

	public void display( TaskCollection tasks )
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
	private TaskCollection tasks;

	@SuppressWarnings("serial")
	private class TaskTableModel extends AbstractTableModel
	{
		public TaskTableModel( TaskCollection tasks )
	    {
	        rowToTask = new Task[tasks.size()];

	        int i=0;
	        for( Task task : tasks.values() )
	        {
	            rowToTask[i] = task;
				taskToRow.put( task.id, i++ );
	        }
	    }

		public void onTask( Task task )
		{
			if( task != null && taskToRow.containsKey( task.id ) )
			{
				int row = taskToRow.get( task.id );
				if( row > -1 )
				{
					rowToTask[row] = task;
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
			Task task = getTaskAtRow( row );
			if( task != null )
			{
				if( col == 0 ) {
					return task.id;
				}
				else if ( col == 1 ) {
					return Util.nne( task.action );
				}
				else if ( col == 2 ) {
					return task.due;
				}
				else if ( col == 3 )
				{
					if( task.status == null || task.status == TaskStatus.OUTSTANDING ) {
						return "<html><body color='red'><b>" + Util.nne( task.status );
					} else {
						return Util.nne( task.status );
					}
				}
			}

			return null;
		}

	    private Task getTaskAtRow( int row ) {
	        return rowToTask[row];
	    }

		private Task[] rowToTask;
		private Map<Integer, Integer> taskToRow = new HashMap<Integer, Integer>();
		private String[] columnNames = new String[] { "Id", "Action", "Due", "Status" };
	}
}
