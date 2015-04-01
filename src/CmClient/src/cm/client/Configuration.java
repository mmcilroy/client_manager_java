package cm.client;

import java.util.Map;

import cm.common.interfaces.ICustomerService;
import cm.common.logger.Logger;
import cm.common.types.ProductType;

public class Configuration
{
	public Configuration( Session session ) throws ClientError
	{
		this.session = session;

		ICustomerService cs = session.getCustomerService();
		Logger LOG = Logger.getInstance();

		try {
			productTypes = cs.getProductTypes();
		} catch( Exception e ) {
			throw new ClientError( "Failed to download product types from server", e );
		}

		if( productTypes == null || productTypes != null && productTypes.size() == 0 ) {
			throw new ClientError( "Invalid product data provided by server" );
		}

		LOG.debug( "loaded %d product types", productTypes.size() );

		try {
			bizOwners = cs.getBusinessOwners();
		} catch( Exception e ) {
			throw new ClientError( "Failed to retrieve static data from server", e );
		}

		LOG.debug( "loaded %d business owners", bizOwners.length );
	}

	public String[] getProductNames( ProductType type ) throws ClientError
	{
		String[] names = productTypes.get( type );
		if( names == null ) {
			throw new ClientError( "Unknown product type: " + type );
		}

		return names;
	}

	public String[] getBusinessOwners()
	{
		return bizOwners;
	}

	public boolean isAdminUser()
	{
		String user = session.getUser();
		return user.compareTo( "tmcilroy" ) == 0 || user.compareTo( "m" ) == 0;
	}

	private Session session;
	private String[] bizOwners;
	private Map<ProductType, String[]> productTypes;
}
