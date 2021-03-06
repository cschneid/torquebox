/*
 * Copyright 2008-2011 Red Hat, Inc, and individual contributors.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 * 
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.torquebox.jobs.deployers;

import java.util.Set;

import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.deployer.DeploymentStages;
import org.jboss.deployers.spi.deployer.helpers.AbstractDeployer;
import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.torquebox.base.metadata.RubyApplicationMetaData;
import org.torquebox.interp.spi.RubyRuntimePool;
import org.torquebox.jobs.core.RubyScheduler;
import org.torquebox.jobs.metadata.ScheduledJobMetaData;
import org.torquebox.mc.AttachmentUtils;

/**
 * <pre>
 * Stage: REAL
 *    In: EnvironmentMetaData, ScheduledJobMetaData
 *   Out: RubyScheduler
 * </pre>
 * 
 * Creates a RubyScheduler bean if there are any job meta data
 */
public class RubySchedulerDeployer extends AbstractDeployer {

    private String runtimePoolName;

    public RubySchedulerDeployer() {
        setAllInputs( true );
        addInput( RubyApplicationMetaData.class );
        addOutput( BeanMetaData.class );
        setStage( DeploymentStages.REAL );
    }

    public void setRubyRuntimePoolName(String runtimePoolName) {
        this.runtimePoolName = runtimePoolName;
    }

    public String getRubyRuntimePoolName() {
        return this.runtimePoolName;
    }

    public void deploy(DeploymentUnit unit) throws DeploymentException {
        Set<? extends ScheduledJobMetaData> allMetaData = unit.getAllMetaData( ScheduledJobMetaData.class );

        if (allMetaData.isEmpty()) {
            return;
        }
        
        log.debug( "Deploying scheduler: " + unit );

        String beanName = AttachmentUtils.beanName( unit, RubyScheduler.class );
        BeanMetaDataBuilder builder = BeanMetaDataBuilder.createBuilder( beanName, RubyScheduler.class.getName() );

        builder.addPropertyMetaData( "name", "RubyScheduler$" + unit.getSimpleName() );
        RubyApplicationMetaData envMetaData = unit.getAttachment( RubyApplicationMetaData.class );
        if (envMetaData != null) {
            builder.addPropertyMetaData( "alwaysReload", envMetaData.isDevelopmentMode() );
        } else {
            log.warn( "No EnvironmentMetaData found for " + unit.getSimpleName() );
        }

        String runtimePoolBeanName = this.runtimePoolName;

        if (runtimePoolBeanName == null) {
            runtimePoolBeanName = AttachmentUtils.beanName( unit, RubyRuntimePool.class, "jobs" );
        }

        ValueMetaData runtimePoolInjection = builder.createInject( runtimePoolBeanName );
        builder.addPropertyMetaData( "rubyRuntimePool", runtimePoolInjection );

        AttachmentUtils.attach( unit, builder.getBeanMetaData() );
    }

}
