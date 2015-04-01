package cm.client.gui.layout;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import cm.client.gui.common.CustomComboBox;
import cm.client.gui.customer.CustomerSearchResultsPanel;
import cm.client.gui.relation.RelationshipComponentFactory;

@SuppressWarnings("serial")
public class NewRelationPanelLayout extends JPanel
{
	public NewRelationPanelLayout()
	{
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		searchPanel = new CustomerSearchResultsPanel();
		GridBagConstraints gbc_searchPanel = new GridBagConstraints();
		gbc_searchPanel.gridwidth = 2;
		gbc_searchPanel.insets = new Insets(0, 0, 5, 0);
		gbc_searchPanel.fill = GridBagConstraints.BOTH;
		gbc_searchPanel.gridx = 0;
		gbc_searchPanel.gridy = 0;
		add(searchPanel, gbc_searchPanel);
		
		JLabel lblNewLabel = new JLabel("Relationship");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 10, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 1;
		add(lblNewLabel, gbc_lblNewLabel);
		
		relationshipCombo = RelationshipComponentFactory.newRelationshipCombo( null );
		GridBagConstraints gbc_relationshipCombo = new GridBagConstraints();
		gbc_relationshipCombo.insets = new Insets(0, 0, 10, 10);
		gbc_relationshipCombo.fill = GridBagConstraints.HORIZONTAL;
		gbc_relationshipCombo.gridx = 1;
		gbc_relationshipCombo.gridy = 1;
		add(relationshipCombo, gbc_relationshipCombo);

	}
	
	public CustomerSearchResultsPanel getSearchPanel() {
		return searchPanel;
	}

	public CustomComboBox getRelationshipCombo() {
		return relationshipCombo;
	}

	private CustomerSearchResultsPanel searchPanel;
	private CustomComboBox relationshipCombo;
}
