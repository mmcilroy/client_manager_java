package cm.common.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Map;

import cm.common.appointment.AppointmentsForDate;
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
import cm.common.entity.domain.TaskHistoryCollection;
import cm.common.types.CustomerSearchParameters;
import cm.common.types.ProductType;
import cm.common.types.TaskSearchResult;

public interface ICustomerService extends Remote
{
	public String[] getBusinessOwners() throws RemoteException;

	public Map<ProductType, String[]> getProductTypes() throws RemoteException;

	public Customer createCustomer( final Address a, final PersonalDetails pd ) throws RemoteException;

	public Product createProduct( Product product, boolean createAutoTasks ) throws RemoteException;

	public Task createTask( Task task ) throws RemoteException;

	public Relationship createRelationship( Relationship relation ) throws RemoteException;

	public Appointment createAppointment( Appointment appointment ) throws RemoteException;

	public PersonalDetails update( PersonalDetails details ) throws RemoteException;

	public Product update( Product product ) throws RemoteException;

	public Task update( Task task ) throws RemoteException;

	public Relationship update( Relationship relationship ) throws RemoteException;

	public Appointment update( Appointment appointment ) throws RemoteException;

	public Address update( Customer customer, boolean forAll ) throws RemoteException;

	public Relationship deactivate( Relationship relationship ) throws RemoteException;
	
	public void delete( Customer c ) throws RemoteException;

	public Customer searchForCustomersById( int customerId ) throws RemoteException;

	public CustomerCollection searchForCustomersByName( String forename, String surname ) throws RemoteException;

	public CustomerCollection searchForCustomersAtAddress( int addressId ) throws RemoteException;

	public ProductCollection searchForCustomersProducts( int customerId ) throws RemoteException;

	public TaskCollection searchForCustomersTasks( int customerId ) throws RemoteException;

	public RelationshipCollection searchForCustomersRelations( int customerId ) throws RemoteException;

	public PersonalDetails searchForExistingPersonalDetails( PersonalDetails details ) throws RemoteException;

	public AddressCollection searchForAddressByPostcode( String postcode ) throws RemoteException;

	public Address searchForExistingAddress( Address address ) throws RemoteException;

	public TaskHistoryCollection searchForTaskHistory( int id ) throws RemoteException;

	public CustomerCollection searchCustomers( CustomerSearchParameters params ) throws RemoteException;

	public TaskSearchResult searchForTodaysTasks( String businessOwner ) throws RemoteException;

	public TaskSearchResult searchForTomorrowsTasks( String businessOwner ) throws RemoteException;

	public TaskSearchResult searchForOverdueTasks( String businessOwner ) throws RemoteException;

	public AppointmentsForDate searchForAppointments( String calendar, Date date ) throws RemoteException;

	public Map<String, Integer> searchForHotLeadsPerUser() throws RemoteException;
}
