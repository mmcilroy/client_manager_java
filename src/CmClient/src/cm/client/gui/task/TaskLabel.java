package cm.client.gui.task;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import cm.common.entity.domain.Task;
import cm.util.Util;

@SuppressWarnings("serial")
public class TaskLabel extends JLabel
{
	public TaskLabel( Task task )
	{
		setBorder( BorderFactory.createEtchedBorder() );
		setBackground( new Color( 255, 255, 150 ) );
		setOpaque( true );

		if( task != null )
		{
			setText( String.format( 
				"<html><body bgcolor='#FFFF99'><table width=300 hspace=5 vspace=5 cellpadding=1 cellspacing=0>" +
				"<tr><td><b>Created</b></td><td>%s by <b>%s</b></td></tr>" +
				"<tr><td><b>Due</b></td><td>%s</td></tr>" +
				"<tr><td colspan=2>%s</td></tr>",
				Util.toDateTimeString( task.createdOn ),
				Util.nns( task.createdBy ),
				Util.toDateString( task.due ),
				Util.nns( task.note ).replace( "\n", "<br/>" ) ) );
		}
	}
}
