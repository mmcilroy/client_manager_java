package cm.common.pubsub;

import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;

import cm.common.entity.domain.Customer;
import cm.common.logger.Logger;

public class CustomerSubscription
{
    public CustomerSubscription( String user, String host, int port )
    {
    	this.user = user;
    	this.host = host;
        this.port = port;
    }

	public void addObserver( CustomerObserver o )
	{
		synchronized( observers ) {
			observers.add( o );
		}

		Logger.getInstance().debug( "%d active customer observers", observers.size() );
	}

	public void removeObserver( CustomerObserver o )
	{
		synchronized( observers ) {
			observers.remove( o );
		}

		Logger.getInstance().debug( "%d active customer observers", observers.size() );
	}

    public void run()
    {
        ClientBootstrap bootstrap = new ClientBootstrap(
        	new NioClientSocketChannelFactory(
        		Executors.newCachedThreadPool(),
        		Executors.newCachedThreadPool() ) );

        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(
                	new ObjectEncoder(),
                    new ObjectDecoder( ClassResolvers.cacheDisabled( getClass().getClassLoader() ) ),
                    new ObjectClientHandler() );
            } } );

        bootstrap.connect( new InetSocketAddress( host, port ) );
    }

	private String user; 
    private String host;
    private int port;

	private List<CustomerObserver> observers = new LinkedList<CustomerObserver>();

	public class ObjectClientHandler extends SimpleChannelUpstreamHandler
    {
        public void channelConnected( ChannelHandlerContext ctx, ChannelStateEvent e )
        {
            e.getChannel().write( user );
        }

        public void channelDisconnected( ChannelHandlerContext ctx, ChannelStateEvent e )
        {
        	Logger.getInstance().info( "Customer subscription connection lost: %s", user );

        	synchronized( observers )
    		{
		        for( CustomerObserver co : observers ) {
		        	co.onConnectionLost();
		        }
    		}
        }

        public void messageReceived( ChannelHandlerContext ctx, MessageEvent e )
        {
        	Object o = e.getMessage();

        	if( o instanceof Customer )
        	{
        		synchronized( observers )
        		{
			        for( CustomerObserver co : observers ) {
			        	co.onCustomer( (Customer)o );
			        }
        		}
        	}
        }
    }
}
