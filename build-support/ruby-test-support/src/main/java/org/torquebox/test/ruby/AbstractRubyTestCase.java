package org.torquebox.test.ruby;

import static org.junit.Assert.*;

import org.jruby.Ruby;
import org.jruby.runtime.builtin.IRubyObject;
import org.junit.Before;

public class AbstractRubyTestCase {

    private String os_prefix;

    protected Ruby createRuby() throws Exception {
        return TestRubyFactory.createRuby();
    }

    protected void assertNotNil(IRubyObject obj) {
        assertFalse("object is a Ruby nil", obj.isNil());
    }

    @Before
    public void determineOsPrefix() {
        this.os_prefix = "";

        if (System.getProperty("os.name").toLowerCase().matches(".*windows.*")) {
            this.os_prefix = "/C:";
        }
    }

}
