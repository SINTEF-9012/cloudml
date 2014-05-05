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

import org.cloudml.core.*;

import static org.cloudml.core.builders.Commons.*;
import static org.hamcrest.Matchers.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.cloudml.core.samples.SshClientServer.*;

@RunWith(JUnit4.class)
public class ProvidedPortInstanceTest extends PortInstanceTest {

    public ProvidedPortInstance aSampleProvidedPortInstance(String name) {
       final String componentName = "app";
        final String vmName = "host";
        final String componentTypeName = "App Type";
        
        final Deployment context = aDeployment()
                .with(amazonEc2())
                .with(ec2LargeLinux())
                .with(anInternalComponent()
                    .named(componentTypeName)
                    .with(aRequiredExecutionPlatform()
                        .named(UBUNTU_1204))
                    .with(aProvidedPort()
                        .named(name)))
                .with(aVMInstance()
                    .named(vmName)
                    .ofType(EC2_LARGE_LINUX))
                .with(anInternalComponentInstance()
                    .named(componentName)
                    .ofType(componentTypeName)
                    .hostedBy(vmName))
                .build();
        
        final ProvidedPortInstance sut = context.getComponentInstances().onlyInternals()
                                           .firstNamed(componentName)
                                           .getProvidedPorts()
                                           .firstNamed(name);
        
        assertThat("sut", sut, is(not(nullValue())));
        return sut;
    }

    @Override
    public final PortInstance<? extends Port> aSamplePortInstance(String name) {
        return aSampleProvidedPortInstance(name);
    }
    
    public final ProvidedPortInstance aSampleProvidedPortInstance() {
        return aSampleProvidedPortInstance("sut");
    }

    @Test
    public void isContainedByAComponentInstance() {
        final ProvidedPortInstance port = aSampleProvidedPortInstance();
        final ComponentInstance<? extends Component> owner = port.getOwner().get();

        assertThat("provided port owner", owner, is(not(nullValue())));
        assertThat("contained by its owner",
                   owner.getProvidedPorts(), contains(port));
    }
   
    
}
