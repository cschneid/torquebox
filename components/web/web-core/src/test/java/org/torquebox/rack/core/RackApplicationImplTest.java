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

package org.torquebox.rack.core;

import java.util.Enumeration;
import java.util.Vector;

import org.jboss.vfs.VFS;
import org.jruby.Ruby;
import org.jruby.runtime.builtin.IRubyObject;
import org.junit.Test;
import org.torquebox.test.ruby.AbstractRubyTestCase;

public class RackApplicationImplTest extends AbstractRubyTestCase {

    @Test
    public void testConstruct() throws Exception {
        Ruby ruby = createRuby();
        ruby.evalScriptlet( "RACK_ROOT='/test/app'\n" );

        String rackup = "run Proc.new {|env| [200, {'Content-Type' => 'text/html'}, env.inspect]}";
        RackApplicationImpl rackApp = new RackApplicationImpl( ruby, rackup, VFS.getChild( "/test/path/config.ru" ) );
        IRubyObject rubyApp = rackApp.getRubyApplication();
        assertNotNil( rubyApp );
    }

    /*
     * @SuppressWarnings("unchecked")
     * 
     * @Test public void testEnvironment() throws Exception { Ruby ruby =
     * createRuby(); ruby.evalScriptlet("RACK_ROOT='/test/app'\n"); String
     * rackup =
     * "run Proc.new {|env| [200, {'Content-Type' => 'text/html'}, env.inspect]}"
     * ; RackApplicationImpl rackApp = new RackApplicationImpl(ruby, rackup,
     * VFS.getChild("/test/path/config.ru"));
     * 
     * final ServletContext servletContext = mock(ServletContext.class); final
     * HttpServletRequest servletRequest = mock(HttpServletRequest.class);
     * 
     * // final InputStream inputStream = new ByteArrayInputStream( //
     * "howdy".getBytes() ); final ServletInputStream inputStream = new
     * MockServletInputStream(new ByteArrayInputStream("".getBytes()));
     * 
     * when(servletRequest.getInputStream()).thenReturn(inputStream);
     * when(servletRequest.getMethod()).thenReturn("GET");
     * when(servletRequest.getContextPath()).thenReturn("/");
     * when(servletRequest.getServletPath()).thenReturn("myapp/");
     * when(servletRequest.getPathInfo()).thenReturn("the_path");
     * when(servletRequest
     * .getQueryString()).thenReturn("cheese=cheddar&bob=mcwhirter");
     * when(servletRequest.getServerName()).thenReturn("torquebox.org");
     * when(servletRequest.getScheme()).thenReturn("https");
     * when(servletRequest.getServerPort()).thenReturn(8080);
     * when(servletRequest.getContentType()).thenReturn("text/html");
     * when(servletRequest.getContentLength()).thenReturn(0);
     * when(servletRequest.getRemoteAddr()).thenReturn("10.42.42.42");
     * when(servletRequest.getHeaderNames()).thenReturn(enumeration("header1",
     * "header2"));
     * when(servletRequest.getHeader("header1")).thenReturn("header_value1");
     * when(servletRequest.getHeader("header2")).thenReturn("header_value2");
     * 
     * IRubyObject rubyEnv = (IRubyObject)
     * rackApp.createEnvironment(servletContext, servletRequest);
     * assertNotNull(rubyEnv);
     * 
     * Map<String, Object> javaEnv = (Map<String, Object>)
     * rubyEnv.toJava(Map.class); assertNotNull(javaEnv);
     * 
     * assertEquals("GET", javaEnv.get("REQUEST_METHOD"));
     * assertEquals("/myapp/the_path", javaEnv.get("REQUEST_URI"));
     * assertEquals("cheese=cheddar&bob=mcwhirter",
     * javaEnv.get("QUERY_STRING")); assertEquals("torquebox.org",
     * javaEnv.get("SERVER_NAME")); assertEquals("https",
     * javaEnv.get("rack.url_scheme")); assertEquals("8080",
     * javaEnv.get("SERVER_PORT")); assertEquals("text/html",
     * javaEnv.get("CONTENT_TYPE")); assertEquals(0L,
     * javaEnv.get("CONTENT_LENGTH")); assertEquals("10.42.42.42",
     * javaEnv.get("REMOTE_ADDR"));
     * 
     * assertEquals("header_value1", javaEnv.get("HTTP_HEADER1"));
     * assertEquals("header_value2", javaEnv.get("HTTP_HEADER2"));
     * 
     * assertNotNull(javaEnv.get("rack.input"));
     * assertNotNull(javaEnv.get("rack.errors")); assertSame(servletRequest,
     * javaEnv.get("servlet_request")); assertSame(servletRequest,
     * javaEnv.get("java.servlet_request"));
     * 
     * }
     */

    @SuppressWarnings("unchecked")
    protected Enumeration enumeration(Object... values) {
        Vector v = new Vector();
        for (Object each : values) {
            v.add( each );
        }
        return v.elements();
    }
}
