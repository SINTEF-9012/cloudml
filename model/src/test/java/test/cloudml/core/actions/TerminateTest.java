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
package test.cloudml.core.actions;


import org.cloudml.core.*;
import org.cloudml.core.actions.StandardLibrary;
import org.cloudml.core.actions.Terminate;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.cloudml.core.samples.SshClientServer.*;

@RunWith(JUnit4.class)
public class TerminateTest {

    @Test
    public void terminateAndMigrateApplication() {
        final Deployment model = getTwoClientsConnectedToOneServer()
                .build();

        final VMInstance clientHost = model.getComponentInstances().onlyVMs().firstNamed(VM_OF_CLIENT_1);
        final Terminate command = new Terminate(new StandardLibrary(), clientHost);

        command.applyTo(model);

        assertThat("Linux instance count", model.getComponentInstances().onlyVMs().ofType(EC2_LARGE_LINUX).size(), is(equalTo(1)));
        assertThat("Windows instance count", model.getComponentInstances().onlyVMs().ofType(EC2_XLARGE_WINDOWS_7).size(), is(equalTo(1)));
        assertThat("Client instance count", model.getComponentInstances().onlyInternals().ofType(SSH_CLIENT).size(), is(equalTo(2)));
        assertThat("Server instance count", model.getComponentInstances().onlyInternals().ofType(SSH_SERVER).size(), is(equalTo(1)));
        assertThat("SSH connection count", model.getRelationshipInstances().ofType(SSH_CONNECTION).size(), is(equalTo(2))); 
    }

}