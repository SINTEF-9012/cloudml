/**
 * This file is part of CloudML [ http://cloudml.org ]
 *
 * Copyright (C) 2012 - SINTEF ICT
 * Contact: Franck Chauvel <franck.chauvel@sintef.no>
 *
 * Module: root
 *
 * CloudML is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * CloudML is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with CloudML. If not, see
 * <http://www.gnu.org/licenses/>.
 */
/*
 */
package test.cloudml.core;

import junit.framework.TestCase;
import static junit.framework.TestCase.assertTrue;
import org.cloudml.core.Port;
import org.cloudml.core.validation.Report;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;


@RunWith(JUnit4.class)
public abstract class PortTest extends TestCase {

    public abstract Port aSamplePort();

    @Test
    public void testDefaultPortNumberUsedByDefault() {
        final Port port = aSamplePort();
        
        assertThat("default port number is not used", port.getPortNumber(), is(equalTo(Port.DEFAULT_PORT_NUMBER)));
    }
    
    @Test
    public void portNumberCanBeSetAndRetrieved() {
        final int HTTP_PORT = 8080;

        final Port port = aSamplePort();
        port.setPortNumber(HTTP_PORT);
        
        assertThat("wrong getter/setter of port number", port.getPortNumber(),is(equalTo(HTTP_PORT)));
    }
    
    @Test
    public void testThatValidationsPassesWhenPortIsValid() {
        final Port port = aSamplePort();

        final Report validation = port.validate();

        assertTrue(validation.pass(Report.WITHOUT_WARNING));
    }

    @Test
    public void testValidationDetectsMissingOwner() {
        final Port port = aSamplePort();
        port.getOwner().discard();

        final Report validation = port.validate();

        assertThat("missing owner shall be detected", validation.hasErrorAbout("no", "owner"));
    }
    
    
    @Test
    public void testRemoteCanBeSetAndRetrieved() {
        final Port port = aSamplePort();

        port.setRemote(false);
        
        assertThat("Shall not be remote", !port.isRemote());
        assertThat("Shall be local", port.isLocal());
    }
    

}
