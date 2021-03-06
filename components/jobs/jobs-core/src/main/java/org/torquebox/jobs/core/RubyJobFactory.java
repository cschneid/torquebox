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

package org.torquebox.jobs.core;

import org.jruby.Ruby;
import org.jruby.runtime.builtin.IRubyObject;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.torquebox.interp.core.InstantiatingRubyComponentResolver;
import org.torquebox.interp.spi.RubyRuntimePool;

public class RubyJobFactory implements JobFactory {

    public static final String RUBY_CLASS_NAME_KEY = "torquebox.ruby.class.name";
    public static final String RUBY_REQUIRE_PATH_KEY = "torquebox.ruby.require.path";

    private RubyRuntimePool runtimePool;
    private JobComponentInitializer componentInitializer;
    private boolean alwaysReload;

    public RubyJobFactory(boolean reload) {
        this.componentInitializer = new JobComponentInitializer();
        this.alwaysReload = reload;
    }

    public void setRubyRuntimePool(RubyRuntimePool runtimePool) {
        this.runtimePool = runtimePool;
    }

    public RubyRuntimePool getRubyRuntimePool() {
        return this.runtimePool;
    }

    @Override
    public Job newJob(TriggerFiredBundle bundle) throws SchedulerException {
        RubyJob rubyJob = null;

        JobDetail jobDetail = bundle.getJobDetail();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();

        InstantiatingRubyComponentResolver resolver = new InstantiatingRubyComponentResolver();
        resolver.setAlwaysReload( this.alwaysReload );
        resolver.setComponentInitializer( this.componentInitializer );
        resolver.setComponentName( "jobs." + jobDetail.getFullName() );
        resolver.setRubyClassName( jobDataMap.getString( RUBY_CLASS_NAME_KEY ) );
        resolver.setRubyRequirePath( jobDataMap.getString( RUBY_REQUIRE_PATH_KEY ) );

        try {
            Ruby ruby = this.runtimePool.borrowRuntime();
            IRubyObject rubyObject = resolver.resolve( ruby );
            rubyJob = new RubyJob( this.runtimePool, rubyObject );
        } catch (Exception e) {
            throw new SchedulerException( e );
        }

        return rubyJob;
    }

}
