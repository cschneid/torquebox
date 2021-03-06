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

package org.torquebox.interp.deployers;

import static org.junit.Assert.*;

import java.net.URL;

import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.junit.Before;
import org.junit.Test;
import org.torquebox.interp.metadata.RubyRuntimeMetaData;
import org.torquebox.test.mc.vdf.AbstractDeployerTestCase;

public class RubyYamlParsingDeployerTest extends AbstractDeployerTestCase {
    private RubyYamlParsingDeployer deployer;

    @Before
    public void setUpDeployer() throws Throwable {
        this.deployer = new RubyYamlParsingDeployer();
        addDeployer( this.deployer );
    }

    @Test
    public void testNoRuntimeMetaData() throws Exception {
        URL rubyYml = getClass().getResource( "ruby-1.8.yml" );

        String deploymentName = addDeployment( rubyYml, "ruby.yml" );
        processDeployments( true );

        DeploymentUnit unit = getDeploymentUnit( deploymentName );
        assertTrue( unit.getAllMetaData( RubyRuntimeMetaData.class ).isEmpty() );
    }

    @Test
    public void testWithRuntimeMetaData18() throws Exception {
        URL rubyYml = getClass().getResource( "ruby-1.8.yml" );

        String deploymentName = addDeployment( rubyYml, "ruby.yml" );

        DeploymentUnit unit = getDeploymentUnit( deploymentName );

        RubyRuntimeMetaData runtimeMetaData = new RubyRuntimeMetaData();
        unit.addAttachment( RubyRuntimeMetaData.class, runtimeMetaData );

        processDeployments( true );

        RubyRuntimeMetaData runtimeMetaData2 = unit.getAttachment( RubyRuntimeMetaData.class );

        assertSame( runtimeMetaData, runtimeMetaData2 );
        assertEquals( RubyRuntimeMetaData.Version.V1_8, runtimeMetaData.getVersion() );
    }

    @Test
    public void testWithRuntimeMetaData19() throws Exception {
        URL rubyYml = getClass().getResource( "ruby-1.9.yml" );

        String deploymentName = addDeployment( rubyYml, "ruby.yml" );

        DeploymentUnit unit = getDeploymentUnit( deploymentName );

        RubyRuntimeMetaData runtimeMetaData = new RubyRuntimeMetaData();
        unit.addAttachment( RubyRuntimeMetaData.class, runtimeMetaData );

        processDeployments( true );

        RubyRuntimeMetaData runtimeMetaData2 = unit.getAttachment( RubyRuntimeMetaData.class );

        assertSame( runtimeMetaData, runtimeMetaData2 );
        assertEquals( RubyRuntimeMetaData.Version.V1_9, runtimeMetaData.getVersion() );
    }

}
