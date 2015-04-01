package cm.server.transactions;

import cm.common.db.DbTransaction;
import cm.common.entity.domain.Address;
import cm.common.entity.domain.Customer;
import cm.common.entity.domain.PersonalDetails;
import cm.server.CustomerService;

public class CreateCustomerTransaction extends DbTransaction
{
	public CreateCustomerTransaction( CustomerService server, Address address, PersonalDetails details )
	{
		super( server.getDb(), "createCustomer", server.getUser() );

		this.server = server;
		this.address = address;
		this.details = details;
	}

	public Customer getCustomer()
	{
		return customer;
	}

	public boolean isExitingCustomer()
	{
		return exitingCustomer;
	}

	protected void transaction( int id ) throws Exception
	{
		// store the transaction id on both entities
		address.transactionId = id;
		details.transactionId = id;

		// store the user
		address.createdBy = server.getUser();
		details.createdBy = server.getUser();

		// create a new address if required
		if( address.id == 0 )
		{
			Address existingAddress = server.searchForExistingAddress( address );
			if( existingAddress == null ) {
				server.getDb().insert( address );
			} else {
				address = existingAddress;
			}
		}

		// link our address to the details
		details.addressId = address.id;

		// now add in the personal details
		PersonalDetails existingDetails;
		if( ( existingDetails = server.searchForExistingPersonalDetails( details ) ) == null )
		{
			server.getDb().insert( details );
			customer.address = address;
			customer.personalDetails = details;
		}
		else
		{
			customer.address = address;
			customer.personalDetails = existingDetails;
			exitingCustomer = true;
		}
	}

	private CustomerService server;
	private Address address;
	private PersonalDetails details;
	private Customer customer = new Customer();
	boolean exitingCustomer = false;
}
