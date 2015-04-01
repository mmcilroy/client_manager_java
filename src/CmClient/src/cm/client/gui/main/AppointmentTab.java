package cm.client.gui.main;

import java.util.Date;

import cm.client.Session;
import cm.client.gui.appointments.WeekViewPanel;
import cm.client.gui.layout.AppointmentTabLayout;

@SuppressWarnings("serial")
public class AppointmentTab extends AppointmentTabLayout
{
	public AppointmentTab( Session client )
	{
		this.client = client;
		getDatePicker().setDate( new Date() );
	}

	public void refresh()
	{
		Date date = getDatePicker().getDate();
		String cal = (String)getCalendarCombo().getSelectedItem();

		if( date != null && cal != null && cal.length() > 0 )
		{
			WeekViewPanel weekPanel = getWeekViewPanel();
			weekPanel.setDate( date );
			weekPanel.setCalendar( cal );
			weekPanel.display( client );
		}
	}

	protected void onSelection( Date date, String calendar ) {
		refresh();
	}

	private Session client;
}
