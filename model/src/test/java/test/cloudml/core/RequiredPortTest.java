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
package test.cloudml.core;

import org.cloudml.core.InternalComponent;
import org.cloudml.core.Port;
import org.cloudml.core.RequiredPort;

import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.cloudml.core.samples.SshClientServer.*;


@RunWith(JUnit4.class)
public class RequiredPortTest extends PortTest {

    @Override
    public Port aSamplePort() {
        return aSampleRequiredPort();
    }
    
    public RequiredPort aSampleRequiredPort() {
        return sshClient().build().getRequiredPorts().firstNamed(CLIENT_PORT);
    }
    
    @Test
    public void testContainmentInComponent() {
        final RequiredPort port = aSampleRequiredPort();
        final InternalComponent owner = (InternalComponent) port.getOwner().get();

        assertThat("missing owner", owner, is(not(nullValue())));
        assertThat("contained in its owner", owner.getRequiredPorts(), contains(port));
    }
    
    @Test
    public void testIsMandatory() {
        final RequiredPort port = aSampleRequiredPort();
        port.setOptional(true);
        
        assertThat("Shall be optional", port.isOptional());
        assertThat("Shall not be mandatory", !port.isMandatory());
    }
}
