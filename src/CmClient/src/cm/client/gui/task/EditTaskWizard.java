package cm.client.gui.task;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import cm.client.ClientError;
import cm.client.Session;
import cm.client.gui.common.wizard.WizardDialog;
import cm.client.gui.common.wizard.WizardPanel;
import cm.common.entity.domain.Customer;
import cm.common.entity.domain.Task;
import cm.common.types.TaskType;

@SuppressWarnings("serial")
public class EditTaskWizard extends WizardDialog
{
	public EditTaskWizard( Session client, Task task )
	{
		this.client = client;
		this.task = task;

		init();
	}

	public EditTaskWizard( Session client, Customer customer )
	{
		this.client = client;
		this.customer = customer;

		init();
	}

	private void init()
	{
		editTaskWizardPanel = new EditTaskWizardPanel();
		editNoteWizardPanel = new EditNoteWizardPanel();

		nextWizardPanel( editTaskWizardPanel );
		setTitle( "Edit Task" );
		setSize( 550, 320 );
		setLocationRelativeTo( null );
		setVisible( true );
	}

	private class EditTaskWizardPanel extends WizardPanel
	{
		EditTaskWizardPanel()
		{
			if( task == null )
			{
				editTaskPanel = new EditTaskPanel();
			}
			else
			{
				editTaskPanel = new EditTaskPanel( task );
				editTaskPanel.fromTask( task );
			}

			add( editTaskPanel );
		}

		public String getTitle()
		{
			return "Update task details";
		}

		protected void onNext()
		{
			task = editTaskPanel.toTask();
			nextWizardPanel( editNoteWizardPanel );
		}

		EditTaskPanel editTaskPanel;
	};

	private class EditNoteWizardPanel extends WizardPanel
	{
		EditNoteWizardPanel()
		{
			add( new JScrollPane( noteField ) );
		}

		public String getTitle()
		{
			return "Enter a note describing the update";
		}

		protected void onNext()
		{
			task.note = noteField.getText();

			if( task.note.length() > 1 )
			{
				new Thread( new EditTask() ).start();
			}
			else
			{
				ErrorInfo info = new ErrorInfo( "Info", "Please enter a few words describing the update", null, null, null, null, null ); 
				JXErrorPane.showDialog( this, info );
			}
		}

		private class EditTask implements Runnable
		{
			public void run()
			{
				boolean dispose = false;
				setBusy( true );

				try
				{
					if( task.id <= 0 )
					{
						task.customerId = customer.personalDetails.id;
						task.type = TaskType.MANUAL;

						if( client.getCustomerService().createTask( task ) == null ) {
							throw new ClientError( "Server operation failed" );
						}
					}
					else
					{
						client.getCustomerService().update( task );
					}

					dispose = true;
				}
				catch( Exception e )
				{
					ErrorInfo ei = new ErrorInfo( "Error", "An error occured", null, null, e, null, null );
					JXErrorPane.showDialog( EditTaskWizard.this, ei );
				}

				setBusy( false );

				if( dispose ) {
					dispose();
				}
			}
		}

		private JTextArea noteField = TaskComponentFactory.newNoteComponent();
	};

	private Session client;
	private Customer customer;
	private Task task;

	private EditTaskWizardPanel editTaskWizardPanel;
	private EditNoteWizardPanel editNoteWizardPanel;

}
