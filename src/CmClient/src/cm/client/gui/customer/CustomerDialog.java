package cm.client.gui.customer;

import java.awt.CardLayout;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import cm.client.Session;
import cm.client.gui.address.EditAddressWizard;
import cm.client.gui.common.BusyPanel;
import cm.client.gui.common.ErrorDialog;
import cm.client.gui.layout.CustomerDialogLayout;
import cm.client.gui.layout.ProductPanelLayout;
import cm.client.gui.layout.SummaryPanelLayout;
import cm.client.gui.layout.TaskPanelLayout;
import cm.client.gui.product.EditProductDialog;
import cm.client.gui.relation.CustomerRelation;
import cm.client.gui.relation.CustomerRelationCollection;
import cm.client.gui.relation.EditRelationsWizard;
import cm.client.gui.relation.NewRelationDialog;
import cm.client.gui.relation.ViewRelationsPanel;
import cm.client.gui.task.EditTaskWizard;
import cm.client.gui.task.TaskTableManager;
import cm.common.entity.domain.Address;
import cm.common.entity.domain.Customer;
import cm.common.entity.domain.PersonalDetails;
import cm.common.entity.domain.Product;
import cm.common.entity.domain.ProductCollection;
import cm.common.entity.domain.Relationship;
import cm.common.entity.domain.Task;
import cm.common.entity.domain.TaskCollection;
import cm.common.interfaces.ICustomerService;
import cm.common.logger.Logger;
import cm.common.pubsub.CustomerObserver;
import cm.common.pubsub.CustomerSubscription;
import cm.common.types.TaskStatus;
import cm.util.Gbc;
import cm.util.Util;

@SuppressWarnings("serial")
public class CustomerDialog extends JDialog implements CustomerObserver
{
	public CustomerDialog( Session session, Customer customer )
	{
		this.session = session;

		initComponents();
		initLayout();
		loadAsync( customer );

		CustomerSubscription sub = session.getCustomerSubscription();
		sub.addObserver( this );
		setVisible( true );
		sub.removeObserver( this );
	}

	public CustomerDialog( Session session, Task task )
	{
		this.session = session;

		initComponents();
		initLayout();
		loadAsync( task );

		CustomerSubscription sub = session.getCustomerSubscription();
		sub.addObserver( this );
		setVisible( true );
		sub.removeObserver( this );
	}

	private void initComponents()
	{
		setDefaultCloseOperation( DISPOSE_ON_CLOSE );
		setModalityType( ModalityType.APPLICATION_MODAL );
		setSize( 900, 550 );
		setLocationRelativeTo( null );
	}

	private void initLayout()
	{
		JButton delButton = dialogLayout.getDeleteButton();
		delButton.setEnabled( session.getConfiguration().isAdminUser() );

		getContentPane().add( dialogLayout );

		JPanel content = dialogLayout.getContentPanel().getContentPanel();
		content.setLayout( cardLayout );
		content.add( summaryPanel, SUMMARY_PANEL );
		content.add( productPanel, PRODUCT_PANEL );
		content.add( taskPanel, TASK_PANEL );
	}

	private void loadAsync( final Customer customer )
	{
		new Thread( new Runnable()
		{
			public void run()
			{
				BusyPanel bp = dialogLayout.getContentPanel();
				bp.setBusy( true );

				try
				{
					loadCustomer( customer );
					displaySummary();
				}
				catch( Exception e )
				{
					ErrorDialog.show( CustomerDialog.this, "Failed to load customer", e );
					Logger.getInstance().error( "loadCustomer failed", e );
					dispose();
				}

				bp.setBusy( false );
			}
		} ).start();
	}

	private void loadAsync( final Task task )
	{
		new Thread( new Runnable()
		{
			public void run()
			{
				BusyPanel bp = dialogLayout.getContentPanel();
				bp.setBusy( true );
				try
				{
					Customer customer = session.getCustomerService().searchForCustomersById( task.customerId );
					loadCustomer( customer );
					displayTask( task );
				}
				catch( Exception e )
				{
					Logger.getInstance().error( "loadAsync failed", e );
				}
				bp.setBusy( false );
			}
		} ).start();
	}

	private void loadRelations() throws RemoteException
	{
		ICustomerService server = session.getCustomerService();
		customer.relations = server.searchForCustomersRelations( customer.personalDetails.id );

		relations = new CustomerRelationCollection( customer );
		synchronized( relations )
		{
			for( Relationship r : customer.relations.values() )
			{
				Customer c = session.getCustomerService().searchForCustomersById( r.customer2 );
				relations.add( new CustomerRelation( c, r ) );
			}
		}
	}

	private void loadCustomer( Customer customer ) throws Exception
	{
		this.customer = customer;

		setTitle( "Customer " + customer.personalDetails.id );

		ICustomerService server = session.getCustomerService();

		// make sure the customer is still there
		if( server.searchForCustomersById( customer.personalDetails.id ) == null ) {
			throw new Exception( "Could not find customer " + customer.personalDetails.id );
		}

		customer.products = server.searchForCustomersProducts( customer.personalDetails.id );
		customer.tasks = server.searchForCustomersTasks( customer.personalDetails.id );
		loadRelations();

		displayTitle();
	}

	private void loadRelationsAsync()
	{
		new Thread( new Runnable()
		{
			public void run()
			{
				BusyPanel bp = dialogLayout.getContentPanel();
				bp.setBusy( true );

				try
				{
					loadRelations();
					summaryPanel.display( relations );
				}
				catch( Exception e )
				{
					Logger.getInstance().error( "loadRelationsAsync failed", e );
				}

				bp.setBusy( false );
			}
		} ).start();
	}

	private void displayTitle()
	{
		dialogLayout.display( customer.personalDetails );
	}

	private void displaySummary()
	{
		summaryPanel.display( customer );
		cardLayout.show( dialogLayout.getContentPanel().getContentPanel(), SUMMARY_PANEL );
	}

	private void displayProduct( Product product )
	{
		productPanel.display( product );
		cardLayout.show( dialogLayout.getContentPanel().getContentPanel(), PRODUCT_PANEL );
	}

	private void displayTask( Task task )
	{
		taskPanel.display( task );
		cardLayout.show( dialogLayout.getContentPanel().getContentPanel(), TASK_PANEL );
	}

	private void deleteCustomer()
	{
		if( JOptionPane.showConfirmDialog( null, "All products, tasks and appointments related to this customer will be deleted. Are you sure?", "", JOptionPane.OK_CANCEL_OPTION ) == 0 )
		{
			try
			{
				session.getCustomerService().delete( customer );
				dispose();
			}
			catch( RemoteException e )
			{
				ErrorInfo info = new ErrorInfo( "Error", "Delete failed", null, null, null, null, null ); 
				JXErrorPane.showDialog( this, info );
			}
		}
	}

	public void onCustomer( Customer broadcast )
	{
		if( broadcast.personalDetails != null )
		{
			Logger.getInstance().debug( "Broadcast recvd: PersonalDetails %d", broadcast.personalDetails.id );

			if( broadcast.personalDetails.id.compareTo( customer.personalDetails.id ) == 0 )
			{
				customer.personalDetails = broadcast.personalDetails;
				summaryPanel.display( customer.personalDetails );
				displayTitle();
			}

			if( relations.isRelated( broadcast ) )
			{
				relations.update( broadcast );
				summaryPanel.display( relations );
			}
		}

		if( broadcast.address != null && broadcast.address.id.compareTo( customer.personalDetails.addressId ) == 0 )
		{
			Logger.getInstance().debug( "Broadcast recvd: Address %d", broadcast.address.id );

			customer.address = broadcast.address;
			summaryPanel.display( customer.address );
		}

		if( broadcast.products != null )
		{
			boolean refresh = false;
			for( Product product : broadcast.products.values() )
			{
				Logger.getInstance().debug( "Broadcast recvd: Product %d", product.id );

				if( product.customerId.compareTo( customer.personalDetails.id ) == 0 ) {
					customer.products.add( product ); refresh = true;
				}

				Product p = productPanel.getProduct();
				if( p != null && p.id.compareTo( product.id ) == 0 ) {
					productPanel.display( product );
				}

				p = taskPanel.getProduct();
				if( p != null && p.id.compareTo( product.id ) == 0 ) {
					taskPanel.display( product );
				}
			}
	
			if( refresh ) {
				summaryPanel.display( customer.products );
			}
		}

		if( broadcast.tasks != null )
		{
			boolean refresh = false;
			for( Task task : broadcast.tasks.values() )
			{
				Logger.getInstance().debug( "Broadcast recvd: Task %d", task.id );

				if( task.customerId.compareTo( customer.personalDetails.id ) == 0 ) {
					customer.tasks.add( task ); refresh = true;
				}

				Task t = taskPanel.getTask();
				if( t != null && t.id.compareTo( task.id ) == 0 ) {
					taskPanel.display( task );
				}
			}
	
			if( refresh )
			{
				summaryPanel.display( customer.tasks );
				productPanel.displayTasks();
			}
		}

		if( broadcast.relations != null )
		{
			boolean refresh = false;
			for( Relationship r : broadcast.relations.values() )
			{
				Logger.getInstance().debug( "Broadcast recvd: Relationship %d", r.id );

				if( r.customer1.compareTo( customer.personalDetails.id ) == 0 ||
					r.customer2.compareTo( customer.personalDetails.id ) == 0 ) {
					refresh = true;
				}

				if( refresh ) {
					loadRelationsAsync();
				}
			}
		}
	}

	public void onConnectionLost()
	{
		;
	}

	private Session session;
	private Customer customer;
	private CustomerRelationCollection relations;

	private CardLayout cardLayout = new CardLayout();
	private MyCustomerDialogLayout dialogLayout = new MyCustomerDialogLayout();
	private MySummaryPanelLayout summaryPanel = new MySummaryPanelLayout();
	private MyProductPanelLayout productPanel = new MyProductPanelLayout();
	private MyTaskPanelLayout taskPanel = new MyTaskPanelLayout();

	private class MyCustomerDialogLayout extends CustomerDialogLayout
	{
		public void display( PersonalDetails details ) {
			getTitleLabel().setText( String.format( "<html><h3>%s", Util.toCustomerName( customer.personalDetails ) ) );
		}

		protected void onHome() {
			displaySummary();
		}

		protected void onDelete() {
			deleteCustomer();
		}
	};

	private class MySummaryPanelLayout extends SummaryPanelLayout
	{
		public MySummaryPanelLayout()
		{
			taskTable = new TaskTableManager( getTaskTable() )
			{
				protected void onDoubleClick( Task task ) {
					displayTask( task );
				}
			};

			getRelationsPanel().add( viewRelationsPanel, new Gbc( Gbc.HORIZONTAL, 0, 0 ).a( Gbc.NORTH ) );
			
		}

		public void display( Customer customer )
		{
			display( customer.personalDetails );
			display( customer.address );
			display( customer.products );
			display( customer.tasks );
			display( relations );
		}

		public void display( PersonalDetails details ) {
			Util.display( details, getPersonalDetailsLabel() );
		}

		public void display( Address address ) {
			Util.display( address, getAddressLabel() );
		}

		public void display( ProductCollection products ) {
			getProductsPanel().display( products );
		}

		public void display( TaskCollection tasks ) {
			taskTable.display( tasks );
		}

		public void display( CustomerRelationCollection crc ) {
			viewRelationsPanel.display( crc );
		}

		protected void onNewProduct() {
			new EditProductDialog( session, customer );
		}

		protected void onNewTask() {
			new EditTaskWizard( session, customer );
		}

		protected void onNewRelation() {
			new NewRelationDialog( session, customer );
		}

		protected void onEditPersonalDetails() {
			new EditPersonalDetailsDialog( session, customer.personalDetails );
		}

		protected void onEditAddress() {
			new EditAddressWizard( session, customer );
		}

		protected void onEditRelations() {
			new EditRelationsWizard( session, relations );
		}

		protected void onProductSelected( Product product ) {
			displayProduct( product );
		}

		private MyViewRelationsPanel viewRelationsPanel = new MyViewRelationsPanel();
		private TaskTableManager taskTable;
	};

	private class MyProductPanelLayout extends ProductPanelLayout
	{
		public MyProductPanelLayout()
		{
			taskTable = new TaskTableManager( getTaskTable() )
			{
				protected void onDoubleClick( Task task ) {
					displayTask( task );
				}
			};
		}

		public void display( Product product )
		{
			this.product = product;
			Util.display( product, getProductLabel() );
			displayTasks();
		}

		public void displayTasks() {
			taskTable.display( customer.getRelatedTasks( product ) );
		}

		public Product getProduct() {
			return product;
		}

		protected void onEditProduct()
		{
			if( product.active != null && product.active )
			{
				new EditProductDialog( session, product );
			}
			else
			{
				ErrorInfo info = new ErrorInfo( "Info", "Cannot edit inactive products", null, null, null, null, null ); 
				JXErrorPane.showDialog( this, info );
			}
		}

		protected void onTaskSelected( Task task ) {
			displayTask( task );
		}

		private Product product;
		private TaskTableManager taskTable;
	};

	private class MyViewRelationsPanel extends ViewRelationsPanel
	{
		protected void onCustomerSelected( Customer c ) {
			loadAsync( c );
		}
	}

	private class MyTaskPanelLayout extends TaskPanelLayout
	{
		public void display( Task task )
		{
			this.task = task;
			Util.display( task, getTaskLabel() );
			getTaskHistoryPanel().display( session, task );
			display( customer.getRelatedProduct( task ) );
		}

		public void display( Product product )
		{
			this.product = product;
			Util.display( product, getProductLabel() );			
		}

		public Task getTask() {
			return task;
		}

		public Product getProduct() {
			return product;
		}

		protected void onEditTask()
		{
			if( task.status == TaskStatus.OUTSTANDING )
			{
				new EditTaskWizard( session, task );
			}
			else
			{
				ErrorInfo info = new ErrorInfo( "Info", "Cannot edit completed tasks", null, null, null, null, null ); 
				JXErrorPane.showDialog( this, info );
			}
		}
	
		protected void onProductSelected()
		{
			if( product != null ) {
				displayProduct( product );
			}
		}

		private Task task;
		private Product product;
	}

	private static String SUMMARY_PANEL = "A";
	private static String PRODUCT_PANEL = "P";
	private static String TASK_PANEL = "T";
}
