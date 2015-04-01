package cm.client.gui.task;

import javax.swing.JTextArea;

import org.jdesktop.swingx.JXDatePicker;

import cm.client.gui.common.CustomComboBox;
import cm.common.types.TaskAction;
import cm.common.types.TaskStatus;

public class TaskComponentFactory
{
	public static JXDatePicker newDueComponent()
	{
		JXDatePicker dp = new JXDatePicker();
		dp.setFormats( new String[] { "dd/MM/yyyy" } );
		return dp;
	}

	public static CustomComboBox newActionComponent()
	{
		CustomComboBox ccb = new CustomComboBox();
		ccb.addItem( "" );

		for( TaskAction ta : TaskAction.values() ) {
			ccb.addItem( ta.toString() );
		}

		return ccb;
	}

	public static CustomComboBox newStatusComponent()
	{
		CustomComboBox ccb = new CustomComboBox();

		for( TaskStatus ts : TaskStatus.values() ) {
			ccb.addItem( ts.toString() );
		}

		return ccb;
	}

	public static JTextArea newNoteComponent()
	{
		JTextArea textArea = new JTextArea( 10, 60 );
		textArea.setLineWrap( true );
		textArea.setWrapStyleWord( true );
		textArea.setText( "" );

		return textArea;
	}
}
