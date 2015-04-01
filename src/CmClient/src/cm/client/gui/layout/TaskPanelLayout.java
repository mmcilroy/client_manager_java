package cm.client.gui.layout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.jdesktop.swingx.JXTitledPanel;

import cm.client.gui.task.TaskHistoryPanel;


@SuppressWarnings("serial")
public class TaskPanelLayout extends JPanel
{
	protected JLabel taskLabel;
	protected JLabel productLabel;
	protected TaskHistoryPanel taskHistoryPanel;

	protected void onEditTask() {
		;
	}

	protected void onProductSelected() {
		;
	}

	public TaskPanelLayout()
	{
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.rowWeights = new double[]{1.0};
        gridBagLayout.columnWeights = new double[]{1.0};
        setLayout(gridBagLayout);
        
        JPanel panel_4 = new JPanel();
        GridBagConstraints gbc_panel_4 = new GridBagConstraints();
        gbc_panel_4.insets = new Insets(10, 10, 10, 10);
        gbc_panel_4.fill = GridBagConstraints.BOTH;
        gbc_panel_4.gridx = 0;
        gbc_panel_4.gridy = 0;
        add(panel_4, gbc_panel_4);
        GridBagLayout gbl_panel_4 = new GridBagLayout();
        gbl_panel_4.columnWidths = new int[]{0, 0, 0};
        gbl_panel_4.rowHeights = new int[]{0, 0};
        gbl_panel_4.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
        gbl_panel_4.rowWeights = new double[]{1.0, Double.MIN_VALUE};
        panel_4.setLayout(gbl_panel_4);
       
        JPanel panel = new JPanel();
        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.fill = GridBagConstraints.BOTH;
        gbc_panel.insets = new Insets(0, 0, 0, 5);
        gbc_panel.gridx = 0;
        gbc_panel.gridy = 0;
        panel_4.add(panel, gbc_panel);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[]{0, 0};
        gbl_panel.rowHeights = new int[]{0, 0, 0};
        gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gbl_panel.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
        panel.setLayout(gbl_panel);
       
        JXTitledPanel panel_2 = new JXTitledPanel();
        panel_2.setTitle( "Task" );

        GridBagConstraints gbc_panel_2 = new GridBagConstraints();
        gbc_panel_2.insets = new Insets(0, 0, 5, 0);
        gbc_panel_2.fill = GridBagConstraints.BOTH;
        gbc_panel_2.gridx = 0;
        gbc_panel_2.gridy = 0;
        panel.add(panel_2, gbc_panel_2);
        GridBagLayout gridBagLayout_1 = new GridBagLayout();
        gridBagLayout_1.columnWidths = new int[]{0, 0};
        gridBagLayout_1.rowHeights = new int[]{0, 0};
        gridBagLayout_1.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gridBagLayout_1.rowWeights = new double[]{1.0, Double.MIN_VALUE};
        panel_2.getContentContainer().setLayout(gridBagLayout_1);
        
        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBorder(null);
        scrollPane_1.setMinimumSize(new Dimension(320, 200));
        GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
        gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
        gbc_scrollPane_1.gridx = 0;
        gbc_scrollPane_1.gridy = 0;
        panel_2.getContentContainer().add(scrollPane_1, gbc_scrollPane_1);
        
         taskLabel = new JLabel("New label");
         taskLabel.setBorder(null);
         taskLabel.setVerticalAlignment(SwingConstants.TOP);
         scrollPane_1.setViewportView(taskLabel);
         taskLabel.addMouseListener(new MouseAdapter() {
         	public void mouseClicked(MouseEvent arg0) {
         		onEditTask();
         	}
         });

        taskLabel.setOpaque(true);
        taskLabel.setBackground(Color.WHITE);
       
        JXTitledPanel panel_3 = new JXTitledPanel();
        panel_3.setTitle( "Related Product" );

        GridBagConstraints gbc_panel_3 = new GridBagConstraints();
        gbc_panel_3.fill = GridBagConstraints.BOTH;
        gbc_panel_3.gridx = 0;
        gbc_panel_3.gridy = 1;
        panel.add(panel_3, gbc_panel_3);
        GridBagLayout gridBagLayout_2 = new GridBagLayout();
        gridBagLayout_2.columnWidths = new int[]{0, 0};
        gridBagLayout_2.rowHeights = new int[]{0, 0};
        gridBagLayout_2.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gridBagLayout_2.rowWeights = new double[]{1.0, Double.MIN_VALUE};
        panel_3.getContentContainer().setLayout(gridBagLayout_2);
        
        JPanel panel_6 = new JPanel();
        panel_6.addMouseListener(new MouseAdapter() {
        	public void mouseClicked(MouseEvent arg0) {
        		onProductSelected();
        	}
        });
        panel_6.setBackground(Color.WHITE);
        GridBagConstraints gbc_panel_6 = new GridBagConstraints();
        gbc_panel_6.fill = GridBagConstraints.BOTH;
        gbc_panel_6.gridx = 0;
        gbc_panel_6.gridy = 0;
        panel_3.getContentContainer().add(panel_6, gbc_panel_6);
        GridBagLayout gbl_panel_6 = new GridBagLayout();
        gbl_panel_6.columnWidths = new int[]{0, 0};
        gbl_panel_6.rowHeights = new int[]{0, 0};
        gbl_panel_6.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gbl_panel_6.rowWeights = new double[]{0.0, Double.MIN_VALUE};
        panel_6.setLayout(gbl_panel_6);
       
        productLabel = new JLabel("New label");
        GridBagConstraints gbc_productLabel = new GridBagConstraints();
        gbc_productLabel.fill = GridBagConstraints.BOTH;
        gbc_productLabel.gridx = 0;
        gbc_productLabel.gridy = 0;
        panel_6.add(productLabel, gbc_productLabel);
        productLabel.setOpaque(true);
        productLabel.setBackground(Color.WHITE);
       
        JXTitledPanel panel_1 = new JXTitledPanel();
        GridBagConstraints gbc_panel_1 = new GridBagConstraints();
        gbc_panel_1.fill = GridBagConstraints.BOTH;
        gbc_panel_1.gridx = 1;
        gbc_panel_1.gridy = 0;
        panel_4.add(panel_1, gbc_panel_1);
        panel_1.setTitle( "Notes" );
        GridBagLayout gridBagLayout_3 = new GridBagLayout();
        gridBagLayout_3.columnWidths = new int[]{0, 0};
        gridBagLayout_3.rowHeights = new int[]{0, 0};
        gridBagLayout_3.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gridBagLayout_3.rowWeights = new double[]{1.0, Double.MIN_VALUE};
        panel_1.getContentContainer().setLayout(gridBagLayout_3);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setMinimumSize(new Dimension(320, 400));
        scrollPane.setSize(new Dimension(1, 1));
        scrollPane.setPreferredSize(new Dimension(800, 800));
        scrollPane.setBorder(null);
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 0;
        panel_1.getContentContainer().add(scrollPane, gbc_scrollPane);
       
        taskHistoryPanel = new TaskHistoryPanel();
        scrollPane.setViewportView(taskHistoryPanel);
        taskHistoryPanel.setOpaque(true);
        taskHistoryPanel.setBackground(Color.WHITE);
	}

	public JLabel getTaskLabel() {
		return taskLabel;
	}
	public JLabel getProductLabel() {
		return productLabel;
	}
	public TaskHistoryPanel getTaskHistoryPanel() {
		return taskHistoryPanel;
	}
}
