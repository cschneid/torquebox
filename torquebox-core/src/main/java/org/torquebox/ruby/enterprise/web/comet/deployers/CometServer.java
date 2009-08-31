package org.torquebox.ruby.enterprise.web.comet.deployers;

import org.jboss.logging.Logger;
import org.jboss.netty.channel.ChannelFactory;

public class CometServer {

	private static final Logger log = Logger.getLogger( CometServer.class );
	private ChannelFactory channelFactory;

	public CometServer() {

	}
	
	public void setChannelFactory(ChannelFactory channelFactory) {
		this.channelFactory = channelFactory;
	}

	public void start() {
		log.info( "starting with channel-factory: " + channelFactory );
	}
	
	public void stop() {
		
	}

}
