package cm.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cm.common.appointment.AppointmentCalendar;
import cm.common.appointment.AppointmentsForDate;
import cm.common.db.DbTransaction;
import cm.common.db.DbUtil;
import cm.common.entity.db.EntityDbConn;
import cm.common.entity.db.EntityDbError;
import cm.common.entity.db.EntityDbUtil;
import cm.common.entity.domain.Address;
import cm.common.entity.domain.AddressCollection;
import cm.common.entity.domain.Appointment;
import cm.common.entity.domain.Customer;
import cm.common.entity.domain.CustomerCollection;
import cm.common.entity.domain.PersonalDetails;
import cm.common.entity.domain.Product;
import cm.common.entity.domain.ProductCollection;
import cm.common.entity.domain.Relationship;
import cm.common.entity.domain.RelationshipCollection;
import cm.common.entity.domain.Task;
import cm.common.entity.domain.TaskCollection;
import cm.common.entity.domain.TaskHistory;
import cm.common.entity.domain.TaskHistoryCollection;
import cm.common.interfaces.ICustomerService;
import cm.common.types.CustomerSearchParameters;
import cm.common.types.ProductType;
import cm.common.types.TaskAction;
import cm.common.types.TaskSearchResult;
import cm.common.types.TaskStatus;
import cm.common.types.TaskType;
import cm.server.transactions.CreateCustomerTransaction;

@SuppressWarnings("serial")
public class CustomerService extends UnicastRemoteObject implements ICustomerService
{
	public CustomerService( EntityDbConn db, CustomerSubscriptionServer sub, String user ) throws RemoteException
	{
		super();

		this.db = db;
		this.sub = sub;
		this.user = user;

		productTypes.put( ProductType.SAVINGS, new String[] { "BRANCH SAVER", "INSTANT SAVER", "PASSBOOK", "1 YEAR BOND", "2 YEAR BOND", "18 MONTH BOND", "1 YEAR FIXED ISA", "2 YEAR FIXED ISA", "EASY ISA", "FLEXI KIDS SAVER", "FLEXI CARD / BOOK", "DIRECT ISA", "BM TELEPHONE SAVER" } );
		productTypes.put( ProductType.BANKING, new String[] { "123", "EVERYDAY" } );
		productTypes.put( ProductType.CREDIT_CARD, new String[] { "123", "SANTANDER" } );
	}

	public EntityDbConn getDb() {
		return db;
	}

	public String getUser() {
		return user;
	}

	public String[] getBusinessOwners() throws RemoteException {
		return new String[] { "BALLYCLARE", "DUNDONALD" };
	}

	public Map<ProductType, String[]> getProductTypes() throws RemoteException {
		return productTypes;
	}

	public synchronized Customer createCustomer( final Address a, final PersonalDetails pd ) throws RemoteException
	{
		CreateCustomerTransaction txn = new CreateCustomerTransaction( this, a, pd );

		if( txn.execute() )
		{
			if( txn.isExitingCustomer() ) {
				throw new RemoteException( "Customer already exists" );
			}

			Customer customer = txn.getCustomer();
			if( customer != null ) {
				return customer;
			}
		}

		throw new RemoteException( "Failed to create customer" );
	}

	public synchronized Product createProduct( final Product product, final boolean createAutoTasks )  throws RemoteException
	{
		DbTransaction txn = new DbTransaction( db, "createProduct", user )
		{
			protected void transaction( int id ) throws Exception
			{
				Customer broadcast = new Customer();

				product.transactionId = id;
				product.createdBy = user;
				db.insert( product );

				broadcast.add( product );

				if( createAutoTasks )
				{
					if( product.type == ProductType.SAVINGS )
					{
						broadcast.add( createNewBusinessTask( product ) );

						if( !product.name.contains( "BOND" ) ) {
							broadcast.add( createReviewTask( product ) );
						}
					}
					else
					if( product.type == ProductType.BANKING && product.name.compareTo( "123" ) == 0 )
					{
						broadcast.add( createNewBanking123Task1( product ) );
						broadcast.add( createNewBanking123Task2( product ) );
						broadcast.add( createNewBanking123Task3( product ) );
					}
					else
					if( product.type == ProductType.BANKING && product.name.compareTo( "EVERYDAY" ) == 0 )
					{
						broadcast.add( createNewBankingEverydayTask1( product ) );
						broadcast.add( createNewBankingEverydayTask2( product ) );
						broadcast.add( createNewBankingEverydayTask3( product ) );
					}
					else
					if( product.type == ProductType.CREDIT_CARD && product.name.compareTo( "123" ) == 0 )
					{
						broadcast.add( createNewCredit123Task1( product ) );
						broadcast.add( createNewCredit123Task2( product ) );
						broadcast.add( createNewCredit123Task3( product ) );
					}
					else
					if( product.type == ProductType.CREDIT_CARD && product.name.compareTo( "SANTANDER" ) == 0 )
					{
						broadcast.add( createNewCreditSantanderTask1( product ) );
						broadcast.add( createNewCreditSantanderTask2( product ) );
						broadcast.add( createNewCreditSantanderTask3( product ) );
					}
				}

				if( sub != null ) {
					publish( broadcast );
				}
			}
		};

		if( txn.execute() ) {
			return product;
		} else {
			throw new RemoteException( "Failed to create product" );
		}
	}

	public synchronized Task createTask( final Task task )  throws RemoteException
	{
		DbTransaction txn = new DbTransaction( db, "createTask", user )
		{
			protected void transaction( int id ) throws Exception
			{
				Customer broadcast = new Customer();

				task.transactionId = id;
				task.createdBy = user;
				db.insert( task );

				broadcast.add( task );
				publish( broadcast );
			}
		};

		if( txn.execute() ) {
			return task;
		} else {
			throw new RemoteException( "Failed to create task" );
		}
	}

	public synchronized Relationship createRelationship( final Relationship relation ) throws RemoteException
	{
		DbTransaction txn = new DbTransaction( db, "createRelationship", user )
		{
			protected void transaction( int id ) throws Exception
			{
				relation.transactionId = id;
				relation.createdBy = user;
				db.insert( relation );
			}
		};

		if( txn.execute() )
		{
			Customer broadcast = new Customer();
			broadcast.add( relation );
			publish( broadcast );

			return relation;
		}
		else
		{
			throw new RemoteException( "Failed to create relationship" );
		}
	}

	public synchronized Appointment createAppointment( final Appointment appointment ) throws RemoteException
	{
		DbTransaction txn = new DbTransaction( db, "createAppointment", user )
		{
			protected void transaction( int id ) throws Exception
			{
				Customer broadcast = new Customer();

				appointment.transactionId = id;
				appointment.createdBy = user;
				db.insert( appointment );

				broadcast.add( appointment );
				publish( broadcast );
			}
		};

		if( txn.execute() ) {
			return appointment;
		} else {
			throw new RemoteException( "Failed to create appointment" );
		}
	}

	public synchronized PersonalDetails update( final PersonalDetails details ) throws RemoteException
	{
		DbTransaction txn = new DbTransaction( db, "updatePersonalDetails", user )
		{
			protected void transaction( int id ) throws Exception
			{
				details.transactionId = id;
				details.createdBy = user;
				db.update( details );

				Customer broadcast = new Customer();
				broadcast.personalDetails = details;
				publish( broadcast );
			}
		};

		if( txn.execute() ) {
			return details;
		} else {
			throw new RemoteException( "Update personal details failed" );
		}
	}

	public synchronized Task update( final Task task ) throws RemoteException
	{
		DbTransaction txn = new DbTransaction( db, "updateTask", user )
		{
			protected void transaction( int id ) throws Exception
			{
				task.transactionId = id;
				task.createdBy = user;
				db.update( task );

				Customer broadcast = new Customer();
				broadcast.add( task );
				publish( broadcast );
			}
		};

		if( txn.execute() ) {
			return task;
		} else {
			throw new RemoteException( "Update task failed" );
		}
	}

	public synchronized Product update( final Product product ) throws RemoteException
	{
		final Customer broadcast = new Customer();

		DbTransaction txn = new DbTransaction( db, "updateProduct", user )
		{
			protected void transaction( int id ) throws Exception
			{
				TaskCollection tasks = searchForTasksRelatedToProduct( product.id );
				if( !product.active )
				{
					for( Task task : tasks.values() )
					{
						if( task.status == TaskStatus.OUTSTANDING )
						{
							task.transactionId = id;
							task.createdBy = user;
							task.status = TaskStatus.COMPLETE;
							task.note = "Completed due to product deactivation";
							db.update( task );

							broadcast.add( task );
						}
					}
				}
				else
				{
					Date nbd = getNewBusinessTaskDate( product );
					Date rd = getReviewTaskDate( product );

					int numNewBusinessTasks = 0;
					int numReviewTasks = 0;

					for( Task task : tasks.values() )
					{
						if( task.status == TaskStatus.OUTSTANDING &&
							task.type == TaskType.AUTO && 
							task.action == TaskAction.NEW_BUSINESS )
						{
							++numNewBusinessTasks;
						}
						else
						if( task.status == TaskStatus.OUTSTANDING &&
							task.type == TaskType.AUTO && 
							task.action == TaskAction.REVIEW )
						{
							++numReviewTasks;
						}
					}

					LOG.debug( "numNewBusinessTasks: {}", numNewBusinessTasks );
					LOG.debug( "numReviewTasks: {}", numReviewTasks );

					boolean reviewTaskExists = false;

					for( Task task : tasks.values() )
					{
						boolean addToBcast = false;

						if( task.type == TaskType.AUTO && task.action == TaskAction.REVIEW && task.status == TaskStatus.OUTSTANDING) {
							reviewTaskExists = true;
						};

						if( numNewBusinessTasks == 1 &&
							task.status == TaskStatus.OUTSTANDING &&
							task.type == TaskType.AUTO && 
							task.action == TaskAction.NEW_BUSINESS &&
							task.due.compareTo( nbd ) != 0 )
						{
							task.due = nbd;
							task.note = "Due date updated to reflect change to products opening date";
							addToBcast = true;
						}
						else
						if( numReviewTasks == 1 &&
							task.status == TaskStatus.OUTSTANDING &&
							task.type == TaskType.AUTO && 
							task.action == TaskAction.REVIEW &&
							task.due.compareTo( rd ) != 0 )
						{
							task.due = rd;
							task.note = "Due date updated to reflect change to products review date";
							addToBcast = true;
						}

						if( addToBcast )
						{
							db.update( task );
							broadcast.add( task );
						}
					}

					if( product.reviewDate != null && !reviewTaskExists ) {
						broadcast.add( createReviewTask( product ) );
					}
				}

				product.transactionId = id;
				product.createdBy = user;
				db.update( product );

				broadcast.add( product );
			}
		};

		if( txn.execute() )
		{
			publish( broadcast );
			return product;
		}
		else
		{
			throw new RemoteException( "Update product failed" );
		}
	}

	public synchronized Relationship update( final Relationship relationship ) throws RemoteException
	{
		DbTransaction txn = new DbTransaction( db, "updateRelationship", user )
		{
			protected void transaction( int id ) throws Exception
			{
				relationship.transactionId = id;
				relationship.createdBy = user;
				db.update( relationship );
			}
		};

		if( txn.execute() )
		{
			Customer broadcast = new Customer();
			broadcast.add( relationship );
			publish( broadcast );

			return relationship;
		}
		else
		{
			throw new RemoteException( "Update relationship failed" );
		}
	}

	public synchronized Appointment update( final Appointment appointment ) throws RemoteException
	{
		DbTransaction txn = new DbTransaction( db, "updateAppointment", user )
		{
			protected void transaction( int id ) throws Exception
			{
				Customer broadcast = new Customer();

				appointment.transactionId = id;
				appointment.createdBy = user;
				db.update( appointment );

				broadcast.add( appointment );
				publish( broadcast );
			}
		};

		if( txn.execute() ) {
			return appointment;
		} else {
			throw new RemoteException( "Update appointment failed" );
		}
	}

	public synchronized Address update( final Customer customer, final boolean forAll ) throws RemoteException
	{
		DbTransaction txn = new DbTransaction( db, "updateAddress", user )
		{
			protected void transaction( int id ) throws Exception
			{
				int origAddressId = 0;

				// check where the customer lives now
				ResultSet rs = db.execute( "select * from PersonalDetails where id=" + customer.personalDetails.id + " and PersonalDetails.live=1" );
				if( rs.next() )
				{
					PersonalDetails pd = new PersonalDetails();
					EntityDbUtil.toEntity( rs, pd );
					origAddressId = pd.addressId;
				}

				if( origAddressId > 0 )
				{
					List<Customer> broadcast = new LinkedList<Customer>();
					Address a = customer.address;
					PersonalDetails pd = customer.personalDetails;

					// if address does not exist create it
					if( ( a = searchForExistingAddress( a ) ) == null )
					{
						a = customer.address;
						a.transactionId = id;
						a.createdBy = user;
						db.insert( a );
					}

					// now update customers to point at the address
					if( a != null && a.id > 0 )
					{
						// update for supplied customer
						pd.transactionId = id;
						pd.createdBy = user;
						pd.addressId = a.id;
						db.update( pd );
	
						Customer c = new Customer();
						c.personalDetails = pd;
						c.address = a;
						broadcast.add( c );

						// update for all other customers at the address
						if( forAll )
						{
							rs = db.execute( "select * from PersonalDetails where addressId=" + origAddressId + " and PersonalDetails.live=1" );
							while( rs.next() )
							{
								pd = new PersonalDetails();
								EntityDbUtil.toEntity( rs, pd );
								pd.transactionId = id;
								pd.createdBy = user;
								pd.addressId = a.id;
								db.update( pd );
	
								c = new Customer();
								c.personalDetails = pd;
								c.address = a;
								broadcast.add( c );
							}
						}
					}
					else
					{
						throw new RemoteException( "Address undefined" );
					}
	
					if( broadcast.size() > 0 )
					{
						for( Customer b : broadcast ) {
							publish( b );
						}
					}
				}
				else
				{
					throw new RemoteException( "Customers current address not found" );
				}
			}
		};

		if( txn.execute() ) {
			return customer.address;
		} else {
			throw new RemoteException( "Update address failed" );
		}
	}

	public synchronized Relationship deactivate( final Relationship relationship ) throws RemoteException
	{
		DbTransaction txn = new DbTransaction( db, "deactivateRelationship", user )
		{
			protected void transaction( int id ) throws Exception
			{
				relationship.transactionId = id;
				relationship.createdBy = user;
				db.deactivate( relationship );
			}
		};

		if( txn.execute() )
		{
			Customer broadcast = new Customer();
			broadcast.add( relationship );
			publish( broadcast );

			return relationship;
		}
		else
		{
			throw new RemoteException( "Deactivate relationship failed" );
		}
	}

	public synchronized void delete( final Customer c ) throws RemoteException
	{
		DbTransaction txn = new DbTransaction( db, "deleteCustomer", user )
		{
			protected void transaction( int id ) throws Exception
			{
				int customerId = c.personalDetails.id;

				PreparedStatement pstmt = db.prepareStatement( "delete from PersonalDetails where id=?" );
				pstmt.setInt( 1, customerId );
				db.execute( pstmt );

				pstmt = db.prepareStatement( "delete from Product where customerId=?" );
				pstmt.setInt( 1, customerId );
				db.execute( pstmt );

				pstmt = db.prepareStatement( "delete from Task where customerId=?" );
				pstmt.setInt( 1, customerId );
				db.execute( pstmt );

				pstmt = db.prepareStatement( "delete from Appointment where customerId=?" );
				pstmt.setInt( 1, customerId );
				db.execute( pstmt );

				pstmt = db.prepareStatement( "delete from Relationship where customer1=? or customer2=?" );
				pstmt.setInt( 1, customerId );
				pstmt.setInt( 2, customerId );
				db.execute( pstmt );
			}
		};

		if( !txn.execute() ) {
			throw new RemoteException( "Delete customer failed" );
		}
	}

	public synchronized Customer searchForCustomersById( int customerId ) throws RemoteException
	{
		try
		{
			ResultSet rs = db.execute( "select * from PersonalDetails, Address where PersonalDetails.addressId=Address.id and PersonalDetails.id=" + customerId + " and PersonalDetails.live=1" );
			if( rs.next() )
			{
				Customer c = new Customer();
				PersonalDetails pd = new PersonalDetails();
				c.personalDetails = (PersonalDetails)EntityDbUtil.toEntity( rs, pd );
	
				Address a = new Address();
				c.address = (Address)EntityDbUtil.toEntity( rs, a );
	
				return c;
			}
		}
		catch( Exception e )
		{
			throw new RemoteException( "searchForCustomersById failed", e );
		}

		return null;
	}

	public synchronized CustomerCollection searchForCustomersByName( String forename, String surname ) throws RemoteException
	{
		try
		{
			CustomerCollection customers = new CustomerCollection();

			ResultSet rs = db.execute( String.format(
				"select * from PersonalDetails, Address " + 
				"where PersonalDetails.addressId=Address.id " +
				"and PersonalDetails.forename like '%%%s%%' " + 
				"and PersonalDetails.surname like '%%%s%%' " +
				"and PersonalDetails.live=1",
				forename, surname ) );

			while( rs.next() )
			{
				Customer c = new Customer();
				PersonalDetails pd = new PersonalDetails();
				Address a = new Address();
				c.personalDetails = (PersonalDetails)EntityDbUtil.toEntity( rs, pd );
				c.address = (Address)EntityDbUtil.toEntity( rs, a );

				customers.add( c );
			}

			return customers;
		}
		catch( Exception e )
		{
			throw new RemoteException( "searchForCustomersByName failed", e );
		}
	}

	public synchronized CustomerCollection searchForCustomersAtAddress( int addressId ) throws RemoteException
	{
		try
		{
			CustomerCollection customers = new CustomerCollection();

			ResultSet rs = db.execute( "select * from PersonalDetails, Address where PersonalDetails.addressId=Address.id and PersonalDetails.live=1 and Address.id=" + addressId );
			while( rs.next() )
			{
				Customer customer = new Customer();
				PersonalDetails pd = new PersonalDetails();
				Address a = new Address();
				customer.personalDetails = (PersonalDetails)EntityDbUtil.toEntity( rs, pd );
				customer.address = (Address)EntityDbUtil.toEntity( rs, a );
				customers.add( customer );
			}

			return customers;
		}
		catch( Exception e )
		{
			throw new RemoteException( "searchForCustomersAtAddress failed", e );
		}
	}

	public synchronized ProductCollection searchForCustomersProducts( int customerId ) throws RemoteException
	{
		try
		{
			ProductCollection products = new ProductCollection();

			ResultSet rs = db.execute( "select * from Product where live=1 and customerId=" + customerId );
			while( rs.next() )
			{
				Product p = new Product();
				EntityDbUtil.toEntity( rs, p );
				products.put( p.id, p );
			}

			return products;
		}
		catch( Exception e1 )
		{
			throw new RemoteException( "searchForCustomersProducts failed", e1 );
		}
	}

	public synchronized TaskCollection searchForCustomersTasks( int customerId ) throws RemoteException
	{
		try
		{
			TaskCollection tasks = new TaskCollection();

			ResultSet rs = db.execute( "select * from Task where live=1 and customerId=" + customerId + " order by due" );
			while( rs.next() )
			{
				Task t = new Task();
				EntityDbUtil.toEntity( rs, t );
				tasks.put( t.id, t );
			}

			return tasks;
		}
		catch( Exception e1 )
		{
			throw new RemoteException( "searchForCustomersTasks failed", e1 );
		}
	}

	public synchronized RelationshipCollection searchForCustomersRelations( int id ) throws RemoteException
	{
		try
		{
			RelationshipCollection rc = new RelationshipCollection();

			ResultSet rs = db.execute( String.format( "select * from Relationship where live=1 and ( customer1=%d or customer2=%d )", id, id ) );
			while( rs.next() )
			{
				Relationship relationship = new Relationship();
				EntityDbUtil.toEntity( rs, relationship );

				// make sure id is always customer1
				if( relationship.customer2 == id )
				{
					relationship.customer2 = relationship.customer1;
					relationship.customer1 = id;
				}

				rc.add( relationship );
			}

			return rc;
		}
		catch( Exception e1 )
		{
			throw new RemoteException( "searchForCustomersRelations failed", e1 );
		}
	}

	public synchronized PersonalDetails searchForExistingPersonalDetails( PersonalDetails details ) throws RemoteException
	{
		try
		{
			PreparedStatement pstmt = db.prepareStatement( 
				"select * from PersonalDetails where live=1 and title=? and forename=? and middlename=? and surname=? and birthDate=? and addressId=?" );

			pstmt.setString( 1, details.title.toString() );
			pstmt.setString( 2, details.forename );
			pstmt.setString( 3, details.middlename );
			pstmt.setString( 4, details.surname );
			pstmt.setDate( 5, DbUtil.sqlDate( details.birthDate ) );
			pstmt.setInt( 6, details.addressId );
			ResultSet rs = db.execute( pstmt );

			if( rs.next() )
			{
				PersonalDetails pd = new PersonalDetails();
				EntityDbUtil.toEntity( rs, pd );
				return pd;
			}

			return null;
		}
		catch( Exception e1 )
		{
			throw new RemoteException( "searchForExistingPersonalDetails failed", e1 );
		}
	}

	public synchronized AddressCollection searchForAddressByPostcode( String postcode ) throws RemoteException
	{
		AddressCollection addresses = new AddressCollection();

		try
		{
			ResultSet rs = db.execute( "select * from Address where live=1 and postcode='" + postcode + "'" );
			while( rs.next() )
			{
				Address address = new Address();
				EntityDbUtil.toEntity( rs, address );
				addresses.put( address.id, address );
			}
		}
		catch( Exception e1 )
		{
			throw new RemoteException( "searchForAddressByPostcode failed", e1 );
		}

		return addresses;
	}

	public synchronized Address searchForExistingAddress( Address address ) throws RemoteException
	{
		try
		{
			ResultSet rs = db.execute( String.format( "select * from Address where live=1 and line1='%s' and line2='%s' and town='%s' and county='%s' and postcode='%s'",
				address.line1,
				address.line2,
				address.town,
				address.county,
				address.postcode ) );
	
			if( rs.next() )
			{
				Address a = new Address();
				EntityDbUtil.toEntity( rs, a );
				return a;
			}

			return null;
		}
		catch( Exception e1 )
		{
			throw new RemoteException( "searchForExistingAddress failed", e1 );
		}
	}

	public synchronized TaskHistoryCollection searchForTaskHistory( int id ) throws RemoteException
	{
		TaskHistoryCollection tc = new TaskHistoryCollection();

		try
		{
			ResultSet rs = db.execute( "select * from TaskHistory where id=" + id + " order by createdOn" );
			while( rs.next() )
			{
				TaskHistory task = new TaskHistory();
				EntityDbUtil.toEntity( rs, task );
				tc.add( task );
			}
		}
		catch( Exception e )
		{
			throw new RemoteException( "searchForTaskHistory failed", e );
		}

		return tc;
	}

	public synchronized CustomerCollection searchCustomers( CustomerSearchParameters params ) throws RemoteException
	{
		CustomerCollection cc = new CustomerCollection();

		try
		{
			String query;
			if( params.id.length() == 0 )
			{
				query = String.format(
					"select * from PersonalDetails, Address where PersonalDetails.addressId=Address.id and " +
					"PersonalDetails.live=1 and " +
					"PersonalDetails.forename like '%%%s%%' and " +
					"PersonalDetails.surname like '%%%s%%' and " +
					"Address.line1 like '%%%s%%' and " +
					"Address.town like '%%%s%%' and " +
					"Address.county like '%%%s%%' and " +
					"Address.postcode like '%%%s%%'",
					params.forename,
					params.surname,
					params.line1,
					params.town,
					params.county,
					params.postcode );
			}
			else
			{
				query = String.format(
					"select * from PersonalDetails, Address where PersonalDetails.addressId=Address.id and " +
					"PersonalDetails.live=1 and PersonalDetails.id=" +
					params.id );
			}

			ResultSet rs = db.execute( query );
			while( rs.next() )
			{
				Customer c = new Customer();
				PersonalDetails pd = new PersonalDetails();
				c.personalDetails = (PersonalDetails)EntityDbUtil.toEntity( rs, pd );
	
				Address a = new Address();
				c.address = (Address)EntityDbUtil.toEntity( rs, a );
	
				cc.put( pd.id, c );
			}
		}
		catch( Exception e )
		{
			throw new RemoteException( "searchCustomers failed", e );
		}

		return cc;
	}

	public synchronized TaskSearchResult searchForTodaysTasks( String businessOwner ) throws RemoteException {
		return searchTasks( "select * from PersonalDetails,Address,Task where PersonalDetails.live=1 and Task.live=1 and PersonalDetails.addressId=Address.id and Task.customerId=PersonalDetails.id and Task.due=CURDATE() and PersonalDetails.businessOwner='" + businessOwner + "'" );
	}

	public synchronized TaskSearchResult searchForTomorrowsTasks( String businessOwner ) throws RemoteException {
		return searchTasks( "select * from PersonalDetails,Address,Task where PersonalDetails.live=1 and Task.live=1 and PersonalDetails.addressId=Address.id and Task.customerId=PersonalDetails.id and Task.due=ADDDATE(CURDATE(), INTERVAL 1 DAY) and PersonalDetails.businessOwner='" + businessOwner + "'" );
	}

	public synchronized TaskSearchResult searchForOverdueTasks( String businessOwner ) throws RemoteException {
		return searchTasks( String.format( "select * from PersonalDetails,Address,Task where PersonalDetails.live=1 and Task.live=1 and PersonalDetails.addressId=Address.id and Task.customerId=PersonalDetails.id and Task.due<CURDATE() and Task.status='%s' and PersonalDetails.businessOwner='%s'", TaskStatus.OUTSTANDING, businessOwner ) );
	}

	public synchronized AppointmentsForDate searchForAppointments( String calendarName, Date date ) throws RemoteException
	{
		LOG.debug( "searchForAppointments {} {}", calendarName, date );

		try
		{
			AppointmentCalendar calendar = new AppointmentCalendar( calendarName );
			AppointmentsForDate appointments = new AppointmentsForDate( calendar, date );
			PreparedStatement pstmt = db.prepareStatement( "select * from Appointment where live=1 and calendar=? and day=? and live=1" );
			pstmt.setString( 1, calendarName );
			pstmt.setDate( 2, DbUtil.sqlDate( date ) );
			ResultSet rs = db.execute( pstmt );

			while( rs.next() )
			{
				Appointment a = new Appointment();
				appointments.add( (Appointment)EntityDbUtil.toEntity( rs, a ) );
			}

			LOG.debug( "searchForAppointments complete" );

			return appointments;
		}
		catch( Exception e )
		{
			throw new RemoteException( "searchForAppointments failed", e );
		}
	}

	public synchronized Map<String, Integer> searchForHotLeadsPerUser() throws RemoteException
	{
		LOG.debug( "searchForHotLeadsPerUser" );

		try
		{
			Map<String, Integer> hlpu = new HashMap<String, Integer>();
			ResultSet rs = db.execute( "select * from Task where action='HOT_LEAD' and transactionId in ( select id from Transaction where name='createTask' and createdOn>='2012-09-24' )" );
	
			while( rs.next() )
			{
				String user = rs.getString( "createdBy" ).toLowerCase();
				Integer i = hlpu.get( user );
	
				if( i == null ) {
					i = new Integer( 0 );
				}
	
				hlpu.put( user, i+1 );
			}

			rs = db.execute( "select * from TaskHistory where action='HOT_LEAD' and transactionId in ( select id from Transaction where name='createTask' and createdOn>='2012-09-24' )" );

			while( rs.next() )
			{
				String user = rs.getString( "createdBy" ).toLowerCase();
				Integer i = hlpu.get( user );
	
				if( i == null ) {
					i = new Integer( 0 );
				}
	
				hlpu.put( user, i+1 );
			}

			return hlpu;
		}
		catch( Exception e )
		{
			throw new RemoteException( "searchForHotLeadsPerUser failed", e );
		}
	}

	private TaskSearchResult searchTasks( String query ) throws RemoteException
	{
		TaskSearchResult res = new TaskSearchResult();

		try
		{
			ResultSet rs = db.execute( query );

			while( rs.next() )
			{
				res.add( (Task)EntityDbUtil.toEntity( rs, new Task() ),
						 (PersonalDetails)EntityDbUtil.toEntity( rs, new PersonalDetails() ),
						 (Address)EntityDbUtil.toEntity( rs, new Address() ) );
			}

			return res;
		}
		catch( Exception e )
		{
			throw new RemoteException( "searchTasks failed: '" + query + "'", e );
		}
	}

	private TaskCollection searchForTasksRelatedToProduct( int id ) throws RemoteException
	{
		TaskCollection tc = new TaskCollection();

		try
		{
			ResultSet rs = db.execute( "select * from Task where live=1 and productId=" + id );
			while( rs.next() )
			{
				Task t = new Task();
				EntityDbUtil.toEntity( rs, t );
				tc.add( t );
			}
		}
		catch( Exception e )
		{
			throw new RemoteException( "searchForTasksRelatedToProduct failed", e );
		}

		return tc;
	}

	private Date getNewBusinessTaskDate( Product p )
	{
		if( p.dateOpened != null )
		{
			Calendar c = new GregorianCalendar();
			c.setTime( p.dateOpened );
			c.add( Calendar.DAY_OF_MONTH, 14 );
			return c.getTime();
		}

		return null;
	}

	private Date getReviewTaskDate( Product p )
	{
		if( p.reviewDate != null )
		{
			Calendar c = new GregorianCalendar();
			c.setTime( p.reviewDate );
			c.add( Calendar.DAY_OF_MONTH, -28 );
			return c.getTime();
		}

		return null;
	}

	private Task createNewBusinessTask( Product p ) throws EntityDbError
	{
		Task t = new Task();
		t.due = getNewBusinessTaskDate( p );

		if( t.due != null )
		{
			t.createdBy = user;
			t.productId = p.id;
			t.transactionId = p.transactionId;
			t.customerId = p.customerId;
			t.action = TaskAction.NEW_BUSINESS;
			t.status = TaskStatus.OUTSTANDING;
			t.type = TaskType.AUTO;
			t.note = "Happy with the service? Confirm the customer has received account ownership.\n" +
					 "Bond / Fixed Rate ISA's = Certificate\n" +
					 "ISA, Branch Saver, Flexi Saver = Cash Card and PIN";

			db.insert( t );

			return t;
		}

		return null;
	}

	private Task createReviewTask( Product p ) throws EntityDbError
	{
		Task t = new Task();
		t.due = getReviewTaskDate( p );

		if( t.due != null )
		{
			t.createdBy = user;
			t.productId = p.id;
			t.transactionId = p.transactionId;
			t.customerId = p.customerId;
			t.action = TaskAction.REVIEW;
			t.status = TaskStatus.OUTSTANDING;
			t.type = TaskType.AUTO;
			t.note = "Related product is due for review. Call and arrange review appointment";

			db.insert( t );

			return t;
		}

		return null;
	}

	private Task buildTask( Product p, TaskAction action, int days )
	{
		Task t = new Task();

		if( p.dateOpened != null )
		{
			Calendar c = new GregorianCalendar();
			c.setTime( p.dateOpened );
			c.add( Calendar.DAY_OF_MONTH, days );
			t.due = c.getTime();
		}

		if( t.due != null )
		{
			t.createdBy = user;
			t.productId = p.id;
			t.transactionId = p.transactionId;
			t.customerId = p.customerId;
			t.action = action;
			t.status = TaskStatus.OUTSTANDING;
			t.type = TaskType.AUTO;

			return t;
		}

		return null;
	}

	private Task createNewBanking123Task1( Product p ) throws EntityDbError
	{
		Task t = buildTask( p, TaskAction.NEW_BUSINESS, 14 );
		t.note = "Confirm rcd card / PIN / chq book\n"+
				 "Confirm sort code / account number and ensure in sales book\n"+
				 "Have used card at cash machine\n"+
				 "Have used card to do a switch transaction\n"; 

		db.insert( t );
		return t;
	}

	private Task createNewBanking123Task2( Product p ) throws EntityDbError
	{
		Task t = buildTask( p, TaskAction.PBA_CHECK, 28 );
		t.note = "Confirmed £500 per mth regular credit set up (record details)\n"+
				 "Confirmed 2 direct debits set up (record details)";

		db.insert( t );
		return t;
	}

	private Task createNewBanking123Task3( Product p ) throws EntityDbError
	{
		Task t = buildTask( p, TaskAction.PBA_CHECK, 56 );
		t.note = "LAST CHANCE CHECK TO AVOID CLAWBACK\n"+
				 "Confirmed £500 per mth regular credit set up (record details)\n"+
				 "Confirmed 2 direct debits set up (record details)";

		db.insert( t );
		return t;
	}

	private Task createNewBankingEverydayTask1( Product p ) throws EntityDbError
	{
		Task t = buildTask( p, TaskAction.NEW_BUSINESS, 14 );
		t.note = "Confirm rcd card / PIN / chq book\n"+
			     "Confirm sort code / account number and ensure in sales book\n"+
				 "Have used card at cash machine\n"+
				 "Have used card to do a switch transaction\n"; 

		db.insert( t );
		return t;
	}

	private Task createNewBankingEverydayTask2( Product p ) throws EntityDbError
	{
		Task t = buildTask( p, TaskAction.PBA_CHECK, 28 );
		t.note = "Confirmed £100 per mth regular credit set up OR £100+ balance (record details)";

		db.insert( t );
		return t;
	}

	private Task createNewBankingEverydayTask3( Product p ) throws EntityDbError
	{
		Task t = buildTask( p, TaskAction.PBA_CHECK, 56 );
		t.note = "LAST CHANCE CHECK TO AVOID CLAWBACK\n"+
			     "Confirmed £100 per mth regular credit set up OR £100+ balance (record details)";

		db.insert( t );
		return t;
	}

	private Task createNewCredit123Task1( Product p ) throws EntityDbError
	{
		Task t = buildTask( p, TaskAction.NEW_BUSINESS, 14 );
		t.note = "Confirm rcd card & PIN\n"+
				 "Confirm 16 digits and ensure recorded in sales book\n"+
				 "Has the customer activated their card\n"+
				 "Have they used the card to do a switch transaction";

		db.insert( t );
		return t;
	}

	private Task createNewCredit123Task2( Product p ) throws EntityDbError
	{
		Task t = buildTask( p, TaskAction.PBA_CHECK, 28 );
		t.note = "Has the customer completed a transaction (update policy screen)";

		db.insert( t );
		return t;
	}

	private Task createNewCredit123Task3( Product p ) throws EntityDbError
	{
		Task t = buildTask( p, TaskAction.PBA_CHECK, 56 );
		t.note = "LAST CHANCE CHECK TO AVOID CLAWBACK\n"+
				 "Has the Customer completed a transaction (update policy screen)\n";

		db.insert( t );
		return t;
	}

	private Task createNewCreditSantanderTask1( Product p ) throws EntityDbError
	{
		Task t = buildTask( p, TaskAction.NEW_BUSINESS, 14 );
		t.note = "Confirm rcd card & PIN\n"+
				 "Confirm 16 digits and ensure recorded in sales book\n"+
				 "Has the customer activated their card\n"+
				 "Have they completed their balance transfer (update policy screen) ";

		db.insert( t );
		return t;
	}

	private Task createNewCreditSantanderTask2( Product p ) throws EntityDbError
	{
		Task t = buildTask( p, TaskAction.PBA_CHECK, 28 );
		t.note = "Has customer completed the balance transfer (update policy screen)";

		db.insert( t );
		return t;
	}

	private Task createNewCreditSantanderTask3( Product p ) throws EntityDbError
	{
		Task t = buildTask( p, TaskAction.PBA_CHECK, 56 );
		t.note = "LAST CHANCE CHECK TO AVOID CLAWBACK\n"+
				 "Has the customer completed a balance transfer (update policy screen)";

		db.insert( t );
		return t;
	}

	private void publish( Customer broadcast )
	{
		if( sub != null ) {
			sub.publish( broadcast );
		}
	}

	private EntityDbConn db;

	private CustomerSubscriptionServer sub;

	private String user;

	private Map<ProductType, String[]> productTypes = new HashMap<ProductType, String[]>();

	private static final Logger LOG = LoggerFactory.getLogger( CustomerService.class );
}
