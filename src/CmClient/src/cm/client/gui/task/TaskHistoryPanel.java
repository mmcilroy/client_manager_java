package cm.client.gui.task;

import java.awt.GridBagLayout;

import javax.swing.JPanel;

import cm.client.Session;
import cm.client.gui.common.BusyPanel;
import cm.common.entity.domain.Task;
import cm.common.entity.domain.TaskHistory;
import cm.common.entity.domain.TaskHistoryCollection;
import cm.common.logger.Logger;
import cm.util.Gbc;

@SuppressWarnings("serial")
public class TaskHistoryPanel extends BusyPanel
{
	public TaskHistoryPanel()
	{
		initLayout();
	}

	private void initLayout()
	{
		getContentPanel().setLayout( new GridBagLayout() );
	}

	public void display( final Session client, final Task task )
	{
		new Thread( new Runnable() 
		{
			public void run()
			{
				setBusy( true );

				JPanel content = getContentPanel();
				content.removeAll();
				
				try
				{
					TaskHistoryCollection history = client.getCustomerService().searchForTaskHistory( task.id );
	
					int y=0; for( TaskHistory task : history.values() ) {
						content.add( new TaskLabel( task ), new Gbc( Gbc.NONE, 0, y++ ).XY( 0, 0 ) );
					}
				}
				catch( Exception e )
				{
					Logger.getInstance().error( "display failed", e );
				}

				setBusy( false );
			}
		} ).start();
	}
}
