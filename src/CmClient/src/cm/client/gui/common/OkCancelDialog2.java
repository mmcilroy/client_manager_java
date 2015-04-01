package cm.client.gui.common;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class OkCancelDialog2 extends BusyDialog
{
	public interface IEventHandler
	{
		public void onOk();
		public void onCancel();
	}

	public OkCancelDialog2()
	{
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if( eventHandler != null ) {
							eventHandler.onOk(); ok = true;
						} } } );

				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}

			{
				cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if( eventHandler != null ) {
							eventHandler.onCancel();
						} } } );

				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	public void setEventHandler( IEventHandler eventHandler ) {
		this.eventHandler = eventHandler;
	}

	public void setBusy( boolean b )
	{
		okButton.setEnabled( !b );
		cancelButton.setEnabled( !b );
		super.setBusy( b );
	}

	public boolean isOk() {
		return ok;
	}

	private final JPanel contentPanel = new JPanel();

	private IEventHandler eventHandler;
	private JButton okButton;
	private JButton cancelButton;

	private boolean ok = false;
}
