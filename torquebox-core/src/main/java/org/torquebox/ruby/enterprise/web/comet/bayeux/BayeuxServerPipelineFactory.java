package org.torquebox.ruby.enterprise.web.comet.bayeux;

import org.jboss.logging.Logger;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.DefaultChannelPipeline;
import org.jboss.netty.handler.codec.bayeux.BayeuxDecoder;
import org.jboss.netty.handler.codec.bayeux.BayeuxEncoder;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.logging.LoggingHandler;

public class BayeuxServerPipelineFactory implements ChannelPipelineFactory {
	
	private static final Logger log = Logger.getLogger(BayeuxServerPipelineFactory.class );

	private BayeuxHandler handler;

	public BayeuxServerPipelineFactory(){
	}
	
	public BayeuxServerPipelineFactory(BayeuxHandler handler) {
		this.handler = handler;
	}
	
	public void setBayeuxHandler(BayeuxHandler handler) {
		this.handler = handler;
	}
	
	public BayeuxHandler getBayeuxHandler() {
		return this.handler;
	}
	
    @Override
    public ChannelPipeline getPipeline() throws Exception {
        // Create a default pipeline implementation.
        ChannelPipeline pipeline = new DefaultChannelPipeline();

        // Uncomment the following line if you want HTTPS
        //SSLEngine engine = SecureChatSslContextFactory.getServerContext().createSSLEngine();
        //engine.setUseClientMode(false);
        //pipeline.addLast("ssl", new SslHandler(engine));

        pipeline.addLast("httpDecoder", new HttpRequestDecoder() );
        pipeline.addLast("bayeuxDecoder", new BayeuxDecoder() );
        // Uncomment the following line if you don't want to handle HttpChunks.
        //pipeline.addLast("aggregator", new HttpChunkAggregator(1048576));
        
        pipeline.addLast("httpEncoder", new HttpResponseEncoder() );
        pipeline.addLast("bayeuxEncoder", new BayeuxEncoder() );
        pipeline.addLast("handler", getBayeuxHandler() );
        //pipeline.addLast("handler", new BayeuxHandler("/home/daijun/Desktop/Root"));//Change Root folder here
        return pipeline;
    }

}