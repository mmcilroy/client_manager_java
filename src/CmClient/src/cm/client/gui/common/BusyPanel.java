package cm.client.gui.common;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.jdesktop.swingx.JXBusyLabel;
 
@SuppressWarnings("serial")
public class BusyPanel extends JPanel
{
      public BusyPanel()
      {
    	  setLayout( cardLayout );
 
    	  busyLabel.setHorizontalAlignment( SwingConstants.CENTER );
    	  busyPanel.setLayout( new BorderLayout() );
    	  busyPanel.add( busyLabel, BorderLayout.CENTER );
 
    	  add( contentPanel, CONTENT_PANEL );
    	  add( busyPanel, BUSY_PANEL );
 
    	  cardLayout.show( this, CONTENT_PANEL );
      }
 
      public JPanel getContentPanel() {
    	  return contentPanel;
      }
 
      public void setBusy( boolean busy )
      {
    	  if( busy ) {
    		  cardLayout.show( this, BUSY_PANEL );
    	  } else {
    		  cardLayout.show( this, CONTENT_PANEL );
    	  }
    	  busyLabel.setBusy( busy );
      }
 
      private JPanel busyPanel = new JPanel();
      private JPanel contentPanel = new JPanel();
      private JXBusyLabel busyLabel = new JXBusyLabel();
      private CardLayout cardLayout = new CardLayout();
 
      private static String BUSY_PANEL = "B";
      private static String CONTENT_PANEL = "C";
}