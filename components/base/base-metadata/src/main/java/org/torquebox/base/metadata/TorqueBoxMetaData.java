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

package org.torquebox.base.metadata;

import java.util.HashMap;
import java.util.Map;

import org.jboss.vfs.VFS;
import org.jboss.vfs.VirtualFile;

public class TorqueBoxMetaData {

    public static final String EXTERNAL = TorqueBoxMetaData.class.getName() + "$external";
    private Map<String, Object> data;

    public TorqueBoxMetaData(Map<String, Object> data) {
        this.data = data;
    }

    public Object getSection(String name) {
        return this.data.get( name );
    }

    @SuppressWarnings("unchecked")
    public String getApplicationRoot() {
        Map<String, String> applicationSection = (Map<String, String>) getSection( "application" );
        if (applicationSection != null) {
            return getOneOf( applicationSection, "root", "RAILS_ROOT", "RACK_ROOT" );
        }
        return null;
    }

    public VirtualFile getApplicationRootFile() {
        String path = getApplicationRoot();

        if (path == null) {
            return null;
        }
        
        String sanitizedPath = null;

        if (path.indexOf( "\\\\" ) >= 0) {
            sanitizedPath = path.replaceAll( "\\\\\\\\", "/" );
            sanitizedPath = sanitizedPath.replaceAll( "\\\\", "" );
        } else {
            sanitizedPath = path.replaceAll( "\\\\", "/" );
        }
        VirtualFile root = VFS.getChild( sanitizedPath );

        return root;
    }

    @SuppressWarnings("unchecked")
    public String getApplicationEnvironment() {
        Map<String, String> applicationSection = (Map<String, String>) getSection( "application" );
        if (applicationSection != null) {
            return getOneOf( applicationSection, "env", "RAILS_ENV", "RACK_ENV" );
        }
        return null;
    }

    protected String getOneOf(Map<String, String> map, String... keys) {
        for (String each : keys) {
            for (String key : map.keySet()) {
                if (each.equalsIgnoreCase( key )) {
                    return map.get( key );
                }
            }
        }
        return null;
    }

    protected String determineEnvironmentKey(Map<String, String> section) {
        if (section.containsKey( "RAILS_ENV" )) {
            return "RAILS_ENV";
        }

        if (section.containsKey( "RACK_ENV" )) {
            return "RACK_ENV";
        }

        return "env";
    }

    protected String determineRootKey(Map<String, String> section) {
        if (section.containsKey( "RAILS_ROOT" )) {
            return "RAILS_ROOT";
        }

        if (section.containsKey( "RACK_ROOT" )) {
            return "RACK_ROOT";
        }

        return "root";
    }

    @SuppressWarnings("unchecked")
    public TorqueBoxMetaData overlayOnto(TorqueBoxMetaData baseMetaData) {
        Map<String, Object> thisData = this.data;
        Map<String, Object> baseData = baseMetaData.data;

        Map<String, Object> mergedData = new HashMap<String, Object>();
        mergedData.putAll( baseData );

        for (String key : thisData.keySet()) {
            if (key.equals( "application" )) {
                // From the application: section, only overly
                // env/RACK_ENV/RAILS_ENV
                // and do it smartly using whatever key(s) are in use by the
                // base
                // and overlay data maps.

                Map<String, String> thisAppSection = (Map<String, String>) thisData.get( "application" );
                String envKey = determineEnvironmentKey( thisAppSection );
                String envName = thisAppSection.get( envKey );

                if (envName != null && !envName.trim().equals( "" )) {
                    Map<String, String> mergedAppSection = (Map<String, String>) mergedData.get( "application" );

                    if (mergedAppSection == null) {
                        mergedAppSection = new HashMap<String, String>();
                        mergedData.put( "application", mergedAppSection );
                    }

                    envKey = determineEnvironmentKey( mergedAppSection );
                    mergedAppSection.put( envKey, envName );
                }

            } else if (key.equals( "environment" )) {
                Map<String, String> thisEnvSection = (Map<String, String>) thisData.get( "environment" );
                Map<String, String> baseEnvSection = (Map<String, String>) baseData.get( "environment" );
                Map<String, String> mergedEnvSection = new HashMap<String, String>();

                if (baseEnvSection != null) {
                    mergedEnvSection.putAll( baseEnvSection );
                }
                mergedEnvSection.putAll( thisEnvSection );

                mergedData.put( "environment", mergedEnvSection );
            } else {
                mergedData.put( key, thisData.get( key ) );
            }
        }

        return new TorqueBoxMetaData( mergedData );
    }

    public String toString() {
        return "[TorqueBoxMetaData: data=" + this.data + "]";
    }
}
