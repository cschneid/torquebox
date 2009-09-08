package org.torquebox.ruby.enterprise.web.comet.bayeux;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jboss.logging.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineCoverage;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.bayeux.BayeuxConnection;
import org.jboss.netty.handler.codec.bayeux.BayeuxMessage;
import org.jboss.netty.handler.codec.bayeux.ConnectRequest;
import org.jboss.netty.handler.codec.bayeux.ConnectResponse;
import org.jboss.netty.handler.codec.bayeux.HandshakeRequest;
import org.jboss.netty.handler.codec.bayeux.HandshakeResponse;
import org.jboss.netty.handler.codec.bayeux.PublishRequest;
import org.jboss.netty.handler.codec.bayeux.PublishResponse;
import org.jboss.netty.handler.codec.bayeux.SubscribeRequest;
import org.jboss.netty.handler.codec.bayeux.SubscribeResponse;
import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.handler.codec.http.CookieDecoder;
import org.jboss.netty.handler.codec.http.CookieEncoder;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;

@ChannelPipelineCoverage("one")
public class BayeuxHandler extends SimpleChannelUpstreamHandler {

	private Logger log = Logger.getLogger(BayeuxHandler.class);

	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		log.info("messageReceived(" + e + ")");
		log.info("  message=" + e.getMessage());
		log.info("  message.class=" + e.getMessage().getClass().getName() );
		if (e.getMessage() instanceof BayeuxConnection) {
			BayeuxConnection connection = (BayeuxConnection) e.getMessage();
			BayeuxMessage bayeux = connection.pollFromUpstream();
			List<BayeuxMessage> list = new ArrayList<BayeuxMessage>();
			while (bayeux != null) {
				if (bayeux instanceof HandshakeRequest) {
					log.info( "    = handshake" );
					HandshakeRequest handshakeRequest = (HandshakeRequest) bayeux;
					HandshakeResponse handshakeResponse = new HandshakeResponse( handshakeRequest );
					handshakeResponse.setSuccessful( Boolean.TRUE );
					handshakeResponse.setSupportedConnectionTypes( new BayeuxConnection.TYPE[] {
							BayeuxConnection.TYPE.LONG_POLLING_JSON_ENCODED,
							BayeuxConnection.TYPE.LONG_POLLING,
							BayeuxConnection.TYPE.CALLBACK_POLLING,
					} );
					//connection.receiveToQueue( handshakeRequest );
					//connection.sendToQueue( handshakeResponse );
					list.add( handshakeRequest );
				} else if ( bayeux instanceof ConnectRequest ) {
					log.info( "    = connect " );
					ConnectRequest connectRequest = (ConnectRequest) bayeux;
					list.add( connectRequest );
				} else if (bayeux instanceof PublishRequest) {
					log.info( "    = publish" );
					PublishRequest publishRequest = (PublishRequest) bayeux;
					//PublishResponse publishResponse = new PublishResponse( publishRequest );
					//publishResponse.setSuccessful( Boolean.TRUE );
					list.add( publishRequest );
					//connection.sendToQueue( publishResponse );
				} else if (bayeux instanceof SubscribeRequest) {
					log.info( "    = subscribe" );
					SubscribeRequest subscribeRequest = (SubscribeRequest) bayeux;
					//SubscribeResponse subscribeResponse = new SubscribeResponse( subscribeRequest );
					//subscribeResponse.setSuccessful( Boolean.TRUE );
					list.add( subscribeRequest );
					//connection.sendToQueue( subscribeResponse );
				} else {
					list.add( bayeux );
				}
				bayeux = connection.pollFromUpstream();
			}
			log.info( "adding to queue: " + list );
			connection.receiveToQueue(list);
			ctx.getChannel().write(connection);
		} else {
			log.info( "non Bayeux request: " + e + " -- " + e.getMessage() );
			writeResponse( "Hi there", e );
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		log.error( "  EXCEPTION", e.getCause() );
	}
	
    private void writeResponse(String responseText, MessageEvent e) {
    	HttpRequest request = (HttpRequest) e.getMessage();
        ChannelBuffer buf = ChannelBuffers.copiedBuffer(responseText, "UTF-8");

        // Decide whether to close the connection or not.
        boolean close =
                HttpHeaders.Values.CLOSE.equalsIgnoreCase(request.getHeader(HttpHeaders.Names.CONNECTION)) || request.getProtocolVersion().equals(HttpVersion
.HTTP_1_0) && !HttpHeaders.Values.KEEP_ALIVE.equalsIgnoreCase(request.getHeader(HttpHeaders.Names.CONNECTION));

        // Build the response object.
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.setContent(buf);
        response.setHeader(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=UTF-8");

        if (!close) {
            // There's no need to add 'Content-Length' header
            // if this is the last response.
            response.setHeader(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(buf.readableBytes()));
        }

        String cookieString = request.getHeader(HttpHeaders.Names.COOKIE);
        if (cookieString != null) {
            CookieDecoder cookieDecoder = new CookieDecoder();
            Set<Cookie> cookies = cookieDecoder.decode(cookieString);
            if (!cookies.isEmpty()) {
                // Reset the cookies if necessary.
                CookieEncoder cookieEncoder = new CookieEncoder(true);
                for (Cookie cookie : cookies) {
                    cookieEncoder.addCookie(cookie);
                }
                response.addHeader(HttpHeaders.Names.SET_COOKIE, cookieEncoder.encode());
            }
        }

        // Write the response.
        ChannelFuture future = e.getChannel().write(response);

        // Close the connection after the write operation is done if necessary.
        if (close) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }
	

}
