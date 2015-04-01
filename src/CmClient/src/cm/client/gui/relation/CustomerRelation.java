package cm.client.gui.relation;

import cm.common.entity.domain.Customer;
import cm.common.entity.domain.Relationship;

public class CustomerRelation
{
	public CustomerRelation( Customer customer, Relationship relationship )
	{
		this.customer = customer;
		this.relationship = relationship;
	}
	
	public Customer customer;
	public Relationship relationship;
}
