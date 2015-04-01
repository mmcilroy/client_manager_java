package cm.client.gui.relation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import cm.common.entity.domain.Customer;

public class CustomerRelationCollection
{
	public CustomerRelationCollection( Customer customer ) {
		this.customer = customer;
	}

	public synchronized void add( CustomerRelation r )
	{
		byCustomerId.put( r.customer.personalDetails.id, r );
		byRelationshipId.put( r.relationship.id, r );
	}

	public int size() {
		return byCustomerId.size();
	}

	public Customer getCustomer() {
		return customer;
	}

	public CustomerRelation getByCustomerId( int id ) {
		return byCustomerId.get( id );
	}

	public CustomerRelation getByRelationshipId( int id ) {
		return byRelationshipId.get( id );
	}

	public Collection<CustomerRelation> values() {
		return byCustomerId.values();
	}

	public boolean isRelated( Customer c ) {
		return byCustomerId.containsKey( c.personalDetails.id );
	}

	public synchronized void update( Customer c )
	{
		CustomerRelation r = getByCustomerId( c.personalDetails.id );
		if( r != null ) {
			r.customer = c;
		}
	}

	/*
	public synchronized void update( Relationship r )
	{
		// make sure this relationship applies to us 
		if( r.customer1.compareTo( customer.getId() ) == 0 || r.customer2.compareTo( customer.getId() ) == 0 )
		{
			// flip ids if required
			if( r.customer2 == customer.getId() )
			{
				r.customer2 = r.customer1;
				r.customer1 = customer.getId();
			}
		}

		// now work out if we need to add or remove it
		CustomerRelation cr = byRelationshipId.get( r.id );
		if( cr == null )
		{
			// insert the new relationship
			cr = new CustomerRelation( customer, r );
			byCustomerId.put( r.customer2, cr );
			byRelationshipId.put( r.id, cr );
		}
		else
		{
			// update existing 
			if( r.live )
			{
				cr.relationship = r;
			}
			else
			{
				byCustomerId.remove( r.customer2 );
				byRelationshipId.remove( r.id );
			}
		}
	}
	*/

	private Map<Integer, CustomerRelation> byCustomerId = new HashMap<Integer, CustomerRelation>();
	private Map<Integer, CustomerRelation> byRelationshipId = new HashMap<Integer, CustomerRelation>();
	private Customer customer;
}
