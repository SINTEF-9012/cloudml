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
import org.cloudml.core.Artefact;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.actions.Install;
import org.cloudml.core.actions.StandardLibrary;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import org.cloudml.core.samples.SshClientServer;
import static org.cloudml.core.samples.SshClientServer.BINDING_TYPE;
import static org.cloudml.core.samples.SshClientServer.CLIENT_TYPE;
import static org.cloudml.core.samples.SshClientServer.SERVER_TYPE;
import org.cloudml.core.validation.DeploymentValidator;
import org.cloudml.core.validation.Report;
import static org.hamcrest.MatcherAssert.assertThat;

public class InstallTest extends TestCase {

    @Test
    public void testInstall() {
        final DeploymentModel model = new SshClientServer()
                .getTypes()
                .build();

        final Artefact artefact = model.getArtefactTypes().named(SshClientServer.CLIENT_TYPE);


        new Install(new StandardLibrary(), artefact).applyTo(model);

        assertThat("Node instance count", model.getNodeInstances().size(), is(equalTo(1))); 
        assertThat("Client instance count", model.getArtefactInstances().ofType(CLIENT_TYPE).size(), is(equalTo(1)));
        assertThat("Server instance count", model.getArtefactInstances().ofType(SERVER_TYPE).size(), is(equalTo(1)));
        assertThat("SSH connection count", model.getBindingInstances().ofType(BINDING_TYPE).size(), is(equalTo(1))); 

        Report validation = new DeploymentValidator().validate(model);
        assertThat("valid output", validation.pass(Report.WITHOUT_WARNING));
    }
}