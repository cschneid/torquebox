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

import javax.management.JMX;

import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.deployer.DeploymentStages;
import org.jboss.deployers.spi.deployer.helpers.AbstractDeployer;
import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.torquebox.base.metadata.RubyApplicationMetaData;
import org.torquebox.jobs.core.RubyScheduler;
import org.torquebox.jobs.core.ScheduledJob;
import org.torquebox.jobs.core.ScheduledJobMBean;
import org.torquebox.jobs.metadata.ScheduledJobMetaData;
import org.torquebox.mc.AttachmentUtils;
import org.torquebox.mc.jmx.JMXUtils;

/**
 * <pre>
 * Stage: REAL
 *    In: ScheduledJobMetaData
 *   Out: ScheduledJob
 * </pre>
 * 
 * Creates objects from metadata
 */
public class RubyJobDeployer extends AbstractDeployer {

    public RubyJobDeployer() {
        setAllInputs( true );
        addInput( RubyApplicationMetaData.class );
        addInput( ScheduledJobMetaData.class );
        addOutput( BeanMetaData.class );
        setStage( DeploymentStages.REAL );
    }

    public void deploy(DeploymentUnit unit) throws DeploymentException {
        Set<? extends ScheduledJobMetaData> allMetaData = unit.getAllMetaData( ScheduledJobMetaData.class );

        log.debug( "Deployable jobs: " + unit );
        log.debug( "  " + allMetaData );

        if (allMetaData.size() == 0) {
            return;
        }

        for (ScheduledJobMetaData metaData : allMetaData) {
            deploy( unit, metaData );
        }

    }

    protected void deploy(DeploymentUnit unit, ScheduledJobMetaData metaData) throws DeploymentException {
        RubyApplicationMetaData rubyAppMetaData = unit.getAttachment(  RubyApplicationMetaData.class  );
        String beanName = AttachmentUtils.beanName( unit, ScheduledJob.class, metaData.getName() );

        log.debug( "deploying job: " + beanName );

        BeanMetaDataBuilder builder = BeanMetaDataBuilder.createBuilder( beanName, ScheduledJob.class.getName() );

        builder.addPropertyMetaData( "group", metaData.getGroup() );
        builder.addPropertyMetaData( "name", metaData.getName() );
        builder.addPropertyMetaData( "rubyClassName", metaData.getRubyClassName() );
        builder.addPropertyMetaData( "rubyRequirePath", metaData.getRubyRequirePath() );
        builder.addPropertyMetaData( "description", metaData.getDescription() );
        builder.addPropertyMetaData( "cronExpression", metaData.getCronExpression() );
        
        String mbeanName = JMXUtils.jmxName( "torquebox.jobs", rubyAppMetaData.getApplicationName() ).with( "name", metaData.getName() ).name();
        String jmxAnno = "@org.jboss.aop.microcontainer.aspects.jmx.JMX(name=\""+ mbeanName + "\", exposedInterface=" + ScheduledJobMBean.class.getName() + ".class)";
        builder.addAnnotation( jmxAnno );

        String schedulerBeanName = metaData.getRubySchedulerName();
        if (schedulerBeanName == null) {
            schedulerBeanName = AttachmentUtils.beanName( unit, RubyScheduler.class );
        }
        ValueMetaData schedulerInjection = builder.createInject( schedulerBeanName, "scheduler" );
        builder.addPropertyMetaData( "scheduler", schedulerInjection );

        BeanMetaData beanMetaData = builder.getBeanMetaData();

        AttachmentUtils.attach( unit, beanMetaData );
    }
}
