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

import junit.framework.TestCase;
import org.cloudml.core.*;
import org.cloudml.core.actions.StandardLibrary;
import org.cloudml.core.actions.Uninstall;
import org.cloudml.core.samples.SensApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.cloudml.core.samples.SshClientServer.*;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

@RunWith(JUnit4.class)
public class UninstallTest {

    @Test
    public void uninstallReinstallANewMissingServerForRemainingClientsIfNeeded() {
        Deployment model = getOneClientConnectedToOneServer().build();

        InternalComponentInstance artefact = model.getComponentInstances().onlyInternals().firstNamed(SERVER_1);
        Uninstall command = new Uninstall(new StandardLibrary(), artefact);

        command.applyTo(model);

        assertThat("Linux instance count", model.getComponentInstances().onlyVMs().ofType(EC2_LARGE_LINUX).size(), is(equalTo(1)));
        assertThat("Windows instance count", model.getComponentInstances().onlyVMs().ofType(EC2_XLARGE_WINDOWS_7).size(), is(equalTo(1)));
        assertThat("Client instance count", model.getComponentInstances().onlyInternals().ofType(SSH_CLIENT).size(), is(equalTo(1)));
        assertThat("Server instance count", model.getComponentInstances().onlyInternals().ofType(SSH_SERVER).size(), is(equalTo(1)));
        assertThat("SSH connection count", model.getRelationshipInstances().ofType(SSH_CONNECTION).size(), is(equalTo(1)));
        assertThat("ExecuteOn count", model.getExecuteInstances().size(), is(equalTo(2)));
    }

    @Test
    public void uninstallDoesNotReinstallMissingServerForRemainingClientsWhenAlternativeExist() {
        Deployment model = getTwoClientsConnectedToTwoServers().build();

        InternalComponentInstance artefact = model.getComponentInstances().onlyInternals().firstNamed(SERVER_1);
        Uninstall command = new Uninstall(new StandardLibrary(), artefact);

        command.applyTo(model);

        assertThat("Linux instance count", model.getComponentInstances().onlyVMs().ofType(EC2_LARGE_LINUX).size(), is(equalTo(2)));
        assertThat("Windows instance count", model.getComponentInstances().onlyVMs().ofType(EC2_XLARGE_WINDOWS_7).size(), is(equalTo(2)));
        assertThat("Client instance count", model.getComponentInstances().onlyInternals().ofType(SSH_CLIENT).size(), is(equalTo(2)));
        assertThat("Server instance count", model.getComponentInstances().onlyInternals().ofType(SSH_SERVER).size(), is(equalTo(1)));
        assertThat("SSH connection count", model.getRelationshipInstances().ofType(SSH_CONNECTION).size(), is(equalTo(2)));
        assertThat("ExecuteOn count", model.getExecuteInstances().size(), is(equalTo(3)));
    }
    
    @Test
    public void uninstallingAHostShouldTriggerAMigration() {
        final Deployment model = SensApp.completeSensApp().build();
        
        assertThat("ExecuteOn count", model.getExecuteInstances().size(), is(equalTo(5)));
        
        
        final InternalComponentInstance app = model.getComponentInstances().onlyInternals().firstNamed(SensApp.SENSAPP_1);
        final InternalComponentInstance expectedHost = model.getComponentInstances().onlyInternals().firstNamed(SensApp.JETTY_2);

        final InternalComponentInstance host = model.getComponentInstances().onlyInternals().firstNamed(SensApp.JETTY_1);
        final Uninstall command = new Uninstall(new StandardLibrary(), host);
        command.applyTo(model);
        
        assertThat("ExecuteOn count", model.getExecuteInstances().size(), is(equalTo(4)));
        assertThat("properly migrated", expectedHost.isHosting(app));
    }
}
