package org.torquebox.ruby.enterprise.web.comet;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.jboss.logging.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.torquebox.ruby.enterprise.web.comet.bayeux.BayeuxHandler;
import org.torquebox.ruby.enterprise.web.comet.bayeux.BayeuxServerPipelineFactory;

public class CometServer {

	private static final Logger log = Logger.getLogger( CometServer.class );
	private ChannelFactory channelFactory;
	private int port = 9090;
	private Channel channel;

	public CometServer() {
		
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public int getPort() {
		return this.port;
	}
	
	public void setChannelFactory(ChannelFactory channelFactory) {
		this.channelFactory = channelFactory;
	}
	
	public ChannelFactory getChannelFactory() {
		return this.channelFactory;
	}

	public void start() {
		log.info( "starting with channel-factory: " + channelFactory );

        BayeuxHandler bayeuxHandler = new BayeuxHandler();
        BayeuxServerPipelineFactory pipelineFactory = new BayeuxServerPipelineFactory( bayeuxHandler );
        
        ServerBootstrap bootstrap = new ServerBootstrap(this.channelFactory);
        
        bootstrap.setPipelineFactory(pipelineFactory);
        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);

        // Bind and start to accept incoming connections.
        this.channel = bootstrap.bind(new InetSocketAddress(this.port));
	}
	
	public void stop() throws InterruptedException {
		/*
		log.info( "Disconnecting" );
		ChannelFuture disconnectFuture = this.channel.disconnect();
		disconnectFuture.await( 5, TimeUnit.SECONDS );
		log.info( "Closing" );
		ChannelFuture closeFuture = this.channel.close();
		closeFuture.await( 5, TimeUnit.SECONDS );
		this.channelFactory.releaseExternalResources();
		*/
	}

}
