package org.torquebox.ruby.enterprise.web.comet.deployers;

import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.vfs.spi.deployer.AbstractSimpleVFSRealDeployer;
import org.jboss.deployers.vfs.spi.structure.VFSDeploymentUnit;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.virtual.VirtualFile;
import org.torquebox.ruby.enterprise.web.comet.CometServer;
import org.torquebox.ruby.enterprise.web.rack.metadata.RackWebApplicationMetaData;

public class CometApplicationDeployer extends AbstractSimpleVFSRealDeployer<RackWebApplicationMetaData> {
	
	private ChannelFactory channelFactory;

	public CometApplicationDeployer() {
		super( RackWebApplicationMetaData.class );
		addOutput(BeanMetaData.class);
	}

	@Override
	public void deploy(VFSDeploymentUnit unit, RackWebApplicationMetaData metaData) throws DeploymentException {
		VirtualFile root = unit.getRoot();
		log.info( "Deploying " + root );
		
		BeanMetaDataBuilder builder = BeanMetaDataBuilder.createBuilder( "foo", CometServer.class.getName() );
		builder.addPropertyMetaData( "channelFactory", getChannelFactory() );
		unit.addAttachment( BeanMetaData.class.getName() + "$fooComet", builder.getBeanMetaData(), BeanMetaData.class );
	}
	
	public void setChannelFactory(ChannelFactory channelFactory) {
		this.channelFactory = channelFactory;
	}
	
	public ChannelFactory getChannelFactory() {
		return this.channelFactory;
	}
	


}
