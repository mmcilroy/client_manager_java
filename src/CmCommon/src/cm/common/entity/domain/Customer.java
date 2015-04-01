package cm.common.entity.domain;

import java.io.Serializable;

public class Customer implements Serializable
{
	public PersonalDetails personalDetails;
	public Address address;
	public ProductCollection products = new ProductCollection();
	public TaskCollection tasks = new TaskCollection();
	public RelationshipCollection relations = new RelationshipCollection();
	public AppointmentCollection appointments = new AppointmentCollection(); 

	public void add( Product p ) {
		if( p != null ) products.add( p );
	}

	public void add( Task t ) {
		if( t != null ) tasks.add( t );
	}

	public void add( Relationship r ) {
		if( r != null ) relations.add( r );
	}

	public void add( Appointment a ) {
		if( a != null ) appointments.add( a );
	}

	public Integer getId()
	{
		if( personalDetails != null ) {
			return personalDetails.id;
		}

		return null;
	}

	public Product getRelatedProduct( Task task ) {
		return products.get( task.productId );
	}

	public TaskCollection getRelatedTasks( Product product )
	{
		TaskCollection relatedTasks = new TaskCollection();

		if( tasks != null && product != null)
		{
			for( Task task : tasks.values() )
			{
				if( task.productId != null && task.productId.compareTo( product.id ) == 0 ) {
					relatedTasks.add( task );
				}
			}
		}

		return relatedTasks;
	}

	private static final long serialVersionUID = 6366876163781219353L;
}
