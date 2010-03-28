package org.torquebox.rack.core;

import org.torquebox.common.pool.DefaultPool;
import org.torquebox.rack.spi.RackApplication;
import org.torquebox.rack.spi.RackApplicationFactory;
import org.torquebox.rack.spi.RackApplicationPool;

public class DefaultRackApplicationPool extends DefaultPool<RackApplication> implements RackApplicationPool {
	
	public DefaultRackApplicationPool(RackApplicationFactory factory) {
		super( factory );
	}

	@Override
	public RackApplication borrowApplication() throws Exception {
		return borrowInstance();
	}

	@Override
	public void releaseApplication(RackApplication app) {
		releaseInstance( app );
	}

}