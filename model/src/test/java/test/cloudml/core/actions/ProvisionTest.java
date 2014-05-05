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
import org.cloudml.core.InternalComponent;
import org.cloudml.core.Deployment;
import org.cloudml.core.actions.Provision;
import org.cloudml.core.actions.StandardLibrary;

import org.junit.Test;
import org.cloudml.core.validation.DeploymentValidator;
import org.cloudml.core.validation.Report;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

import static org.cloudml.core.samples.SshClientServer.*;

public class ProvisionTest extends TestCase {

    @Test
    public void testProvision() {
        final Deployment model = getSshTypes().build(); 

        final InternalComponent sshClient = model.getComponents().onlyInternals().firstNamed(SSH_CLIENT);

        new Provision(new StandardLibrary(), sshClient).applyTo(model);

        assertThat("VM instance count", model.getComponentInstances().onlyVMs().size(), is(equalTo(2))); 
        assertThat("Client instance count", model.getComponentInstances().onlyInternals().ofType(SSH_CLIENT).size(), is(equalTo(1)));
        assertThat("Server instance count", model.getComponentInstances().onlyInternals().ofType(SSH_SERVER).size(), is(equalTo(1)));
        assertThat("SSH connection count", model.getRelationshipInstances().ofType(SSH_CONNECTION).size(), is(equalTo(1))); 

        Report validation = new DeploymentValidator().validate(model);
        assertThat("valid output", validation.pass(Report.WITHOUT_WARNING));
    }
}