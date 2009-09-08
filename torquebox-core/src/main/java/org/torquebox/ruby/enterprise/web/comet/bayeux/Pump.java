package org.torquebox.ruby.enterprise.web.comet.bayeux;

import java.util.HashMap;
import java.util.Map;

import org.jboss.logging.Logger;
import org.jboss.netty.handler.codec.bayeux.BayeuxData;
import org.jboss.netty.handler.codec.bayeux.BayeuxRouter;
import org.jboss.netty.handler.codec.bayeux.PublishRequest;

public class Pump {

	private static Logger log = Logger.getLogger(Pump.class);
	private Thread thread;
	private BayeuxRouter router;

	public Pump() {
		this.router = BayeuxRouter.getInstance();
	}
	
	public void start() {
		thread = new Thread() {
			public void run() {
				while (true) {
					try {
						BayeuxData data = new BayeuxData();
						data.put( "server_time", System.currentTimeMillis() );
						PublishRequest request = new PublishRequest( "/pump", data );
						router.onPublish( request );
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						break;
					}
				}
			}
		};

		thread.start();
	}

	public void stop() {
		this.thread.interrupt();
	}

}
