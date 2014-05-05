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
import org.cloudml.core.Deployment;
import org.cloudml.core.RequiredPortInstance;
import org.cloudml.core.actions.Bind;
import org.cloudml.core.actions.StandardLibrary;
import static org.cloudml.core.builders.Commons.*;
import org.cloudml.core.validation.DeploymentValidator;
import org.cloudml.core.validation.Report;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.*;

import static org.cloudml.core.samples.SshClientServer.*;

public class BindTest extends TestCase {

    @Test
    public void testBinder() {
        final Deployment model = aDeployment()
                .with(amazonEc2())
                .with(ec2LargeLinux())
                .with(ec2XLargeWindows7())
                .with(aVMInstance()
                    .named("host 1")
                    .ofType(EC2_LARGE_LINUX))
                .with(aVMInstance()
                    .named("host 2")
                    .ofType(EC2_XLARGE_WINDOWS_7))
                .with(sshClient())
                .with(sshServer())
                .with(sshConnection())
                .with(anInternalComponentInstance()
                    .ofType(SSH_CLIENT)
                    .named("client")
                    .hostedBy("host 1"))
                .with(anInternalComponentInstance()
                    .ofType(SSH_SERVER)
                    .named("server")
                    .hostedBy("host 2"))
                .build();
        
       
        final RequiredPortInstance clientPort = model
                .getComponentInstances().onlyInternals().firstNamed("client")
                .getRequiredPorts().firstNamed(CLIENT_PORT);    
    
        final StandardLibrary deployer = new StandardLibrary();

        new Bind(deployer, clientPort).applyTo(model);
        
        Report validation = new DeploymentValidator().validate(model);
        assertThat("valid output", validation.pass(Report.WITHOUT_WARNING));         
    }
}