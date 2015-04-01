 package cm.client.gui.task;

import javax.swing.JPanel;

import org.jdesktop.swingx.JXDatePicker;

import cm.client.gui.common.CustomComboBox;
import cm.client.gui.common.KeyValuePanelBuilder;
import cm.common.entity.domain.Task;
import cm.common.types.TaskAction;
import cm.common.types.TaskStatus;
import cm.util.Util;

@SuppressWarnings("serial")
public class EditTaskPanel extends JPanel
{
	public EditTaskPanel( Task t )
	{
		KeyValuePanelBuilder b = new KeyValuePanelBuilder( this );

		// action should only be an option when creating a new task 
		if( t == null ) {
			b.add( "Action", actionCombo );
		}

		b.add( "Due", dueField );
		b.add( "Status", statusCombo );
		b.gap();

		if( t != null ) {
			fromTask( t );
		}
	}

	public EditTaskPanel()
	{
		this( null );
	}

	public void fromTask( Task t )
	{
		this.task = t;

		dueField.setDate( t.due );
		actionCombo.setSelectedItem( t.action.toString() );
		statusCombo.setSelectedItem( t.status.toString() );
	}

	public Task toTask()
	{
		task.due = dueField.getDate();

		String ta = (String)actionCombo.getSelectedItem();
		if( Util.nns( ta ).length() == 0 ) {
			task.action = null;
		} else {
			task.action = TaskAction.valueOf( ta );
		}

		String ts = (String)statusCombo.getSelectedItem();
		if( Util.nns( ts ).length() == 0 ) {
			task.status = null;
		} else {
			task.status = TaskStatus.valueOf( ts );
		}

		return task;
	}

	private Task task = new Task();

	private JXDatePicker dueField = TaskComponentFactory.newDueComponent();
	private CustomComboBox actionCombo = TaskComponentFactory.newActionComponent();
	private CustomComboBox statusCombo = TaskComponentFactory.newStatusComponent();
}
