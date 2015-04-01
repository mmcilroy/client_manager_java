package cm.common.pubsub;

import cm.common.entity.domain.Customer;

public interface CustomerObserver
{
	public void onConnectionLost();
	public void onCustomer( Customer customer );
}
