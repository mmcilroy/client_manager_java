package cm.client.gui.common;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class OkCancelDialog extends JDialog
{
	public OkCancelDialog( String title )
	{
		this.title = title;
	}

	public void init()
	{
		initComponents();
		initLayout();
		pack();
		setLocationRelativeTo( null );
		setVisible( true );
	}

	public void init( int x, int y )
	{
		initComponents();
		initLayout();
		setSize( x, y );
		setLocationRelativeTo( null );
		setVisible( true );
	}

	public void isOk( boolean b ) {
		ok = b;
	}

	public boolean isOk() {
		return ok;
	}

	protected abstract JPanel initContent();
	protected abstract void onOk();

	private void initComponents()
	{
		setTitle( title );
		setDefaultCloseOperation( DISPOSE_ON_CLOSE );
		setModalityType( ModalityType.APPLICATION_MODAL );
		setLayout( new GridBagLayout() );
		setResizable( false );

		okButton.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent arg0 ) {
				onOk();
			} } );

		cancelButton.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent arg0 ) {
				dispose();
			} } );
	}

	private void initLayout()
	{
		Container content = getContentPane();
		content.setLayout( new BorderLayout() );

		JPanel bottomPanel = new JPanel();
		bottomPanel.add( okButton );
		bottomPanel.add( cancelButton );

		content.add( bottomPanel, BorderLayout.SOUTH );
		content.add( initContent(), BorderLayout.CENTER );

		pack();
	}

	private String title;
	private boolean ok;

	private JButton okButton = new JButton( "OK" );
	private JButton cancelButton = new JButton( "Cancel" );
}
