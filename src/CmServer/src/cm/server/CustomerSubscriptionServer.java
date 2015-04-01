package cm.server;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cm.common.entity.domain.Customer;

class CustomerSubscriptionServer
{
    CustomerSubscriptionServer( Server cmServer, int port )
    {
    	this.server = cmServer;
    
        ServerBootstrap bootstrap = new ServerBootstrap(
        	new NioServerSocketChannelFactory(
        		Executors.newCachedThreadPool(),
        		Executors.newCachedThreadPool() ) );

        bootstrap.setPipelineFactory( new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(
                	new ObjectEncoder(),
                	new ObjectDecoder( ClassResolvers.cacheDisabled( getClass().getClassLoader() ) ),
                	new ObjectServerHandler() );
            } } );

        bootstrap.bind( new InetSocketAddress( port ) );
    }

    public void publish( Customer customer )
    {
    	LOG.debug( "Publishing update to {} clients", channels.size() );

    	synchronized( channels )
    	{
    		for( Channel c : channels.values() ) {
    			c.write( customer );
    		}
    	}
    }

    private Server server;

    private Map<String, Channel> channels = new HashMap<String, Channel>();

    private class ObjectServerHandler extends SimpleChannelUpstreamHandler
    {
    	public void channelDisconnected( ChannelHandlerContext ctx, ChannelStateEvent e )
    	{
			server.onUserDisconnected( user );
			synchronized( channels ) {
				channels.remove( user );
			}
			user = null;
    	}

        public void messageReceived( ChannelHandlerContext ctx, MessageEvent e )
        {
        	Object o = e.getMessage();

        	if( o instanceof String )
        	{
        		if( user == null )
        		{
        			user = (String)o;
        			server.userConnectionConfirmed( user );

        			synchronized( channels ) {
        				channels.put( user, e.getChannel() );
        			}
        		}
        		else
        		{
        			LOG.error( "User id already established. Recvd: %s", (String)o );
        		}
        	}
        	else
        	{
    			LOG.error( "Unexpected object: %s", o.toString() );
        	}
        }

        private String user;
    }

    private static final Logger LOG = LoggerFactory.getLogger( CustomerSubscriptionServer.class );
}
