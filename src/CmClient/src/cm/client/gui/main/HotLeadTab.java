package cm.client.gui.main;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Map;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import cm.client.Session;
import cm.client.gui.common.BusyPanel;
import cm.common.entity.domain.Customer;
import cm.common.entity.domain.Task;
import cm.common.logger.Logger;
import cm.common.pubsub.CustomerObserver;
import cm.common.types.TaskAction;

@SuppressWarnings("serial")
public class HotLeadTab extends BusyPanel implements CustomerObserver
{
	public HotLeadTab( final Session client )
	{
		this.client = client;

		client.getCustomerSubscription().addObserver( this );

		dataset = new DefaultCategoryDataset();
		chart = ChartFactory.createBarChart( "Hot Leads", "User", "Hot Leads", dataset, PlotOrientation.VERTICAL, false, true, false );
		cpanel = new ChartPanel( chart );

		CategoryPlot categoryplot = chart.getCategoryPlot();
		categoryplot.setRangeGridlinePaint( Color.red );
		NumberAxis rangeAxis = (NumberAxis)categoryplot.getRangeAxis();
		NumberTickUnit ntu = new NumberTickUnit( 1.0 );
		rangeAxis.setTickUnit( ntu );

		JPanel cp = getContentPanel();
		cp.setLayout( new GridLayout( 0, 1, 0, 0 ) );
		cp.add( cpanel );
	}

	public synchronized void refresh()
	{
		new Thread( new Runnable()
		{
			public void run()
			{
				setBusy( true );

				try
				{
					String[] users = { "bmccone", "cbrett", "dpankhurst", "lgault" };

					Map<String, Integer> hlpu = client.getCustomerService().searchForHotLeadsPerUser();

					dataset.clear();

					for( String user : users )
					{
						Integer c = hlpu.get( user );
						if( c == null ) {
							dataset.setValue( 0, "Hot Leads", user );
						} else {
							dataset.setValue( c, "Hot Leads", user );
						}
					}
				}
				catch( Exception e )
				{
					Logger.getInstance().error( "searchForHotLeadsPerUser failed", e );
				}

				setBusy( false );
			}
		} ).start();
	}

	private DefaultCategoryDataset dataset;
	private ChartPanel cpanel;
	private JFreeChart chart;
	private Session client;

	public void onConnectionLost() {
		;
	}

	public void onCustomer( Customer customer )
	{
		// new hot leads should cause a refresh
		if( customer.tasks != null )
		{
			for( Task task : customer.tasks.values() )
			{
				if( task.action == TaskAction.HOT_LEAD )
				{
					refresh();
					return;
				}
			}
		}
	}
}
