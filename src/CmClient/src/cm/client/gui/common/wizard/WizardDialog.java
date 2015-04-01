package cm.client.gui.common.wizard;

import java.util.LinkedList;

import cm.client.gui.layout.WizardDialogLayout;
import cm.util.Gbc;

@SuppressWarnings( "serial" )
public class WizardDialog extends WizardDialogLayout
{
	public void nextWizardPanel( WizardPanel panel )
	{
		if( wizardPanel != null ) {
			history.add( wizardPanel );
		}

		showWizardPanel( panel );
	}

	public void showWizardPanel( WizardPanel panel )
	{
		if( history.size() > 0 ) {
			prevButton.setEnabled( true );
		} else {
			prevButton.setEnabled( false );
		}

		this.titleLabel.setText( panel.getTitle() );
		this.wizardPanel = panel;

		wizardContentPanel.setVisible( false );
		wizardContentPanel.removeAll();
		wizardContentPanel.add( panel, new Gbc( Gbc.BOTH, 0, 0 ).XY( 1, 1 ).i( 10 ) );
		wizardContentPanel.setVisible( true );
	}

	public void setBusy( boolean busy )
	{
		if( busy )
		{
			prevButton.setEnabled( false );
			nextButton.setEnabled( false );
		}
		else
		{
			nextButton.setEnabled( true );

			if( history.size() > 0 ) {
				prevButton.setEnabled( true );
			}
		}

		busyLabel.setBusy( busy );
	}

	protected void onPrev()
	{
		if( history.size() > 0 ) {
			showWizardPanel( history.removeLast() );
		}
	}

	protected void onNext()
	{
		if( wizardPanel != null ) {
			wizardPanel.onNext();
		}
	}

	protected void onOk()
	{
		if( wizardPanel != null ) {
			wizardPanel.onOk();
		}
	}

	private WizardPanel wizardPanel;
	private LinkedList<WizardPanel> history = new LinkedList<WizardPanel>();
}
