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

import org.cloudml.core.Deployment;
import org.cloudml.core.ExternalComponent;
import org.cloudml.core.ExternalComponentInstance;
import org.cloudml.core.VMInstance;
import org.cloudml.core.validation.Report;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.cloudml.core.builders.Commons.*;


import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import static org.cloudml.core.samples.SshClientServer.*;

@RunWith(JUnit4.class)
public class VMInstanceTest extends ExternalComponentInstanceTest {

    @Override
    public final ExternalComponentInstance<? extends ExternalComponent> aSampleExternalComponentInstance(String name) {
        return aSampleVMInstance(name);
    }

    private VMInstance aSampleVMInstance(String instanceName) {
        Deployment model = aDeployment()
                .with(amazonEc2())
                .with(ec2LargeLinux())
                .with(aVMInstance()
                    .named(instanceName)
                    .ofType(EC2_LARGE_LINUX))
                .build();

        return model.getComponentInstances().onlyVMs().firstNamed(instanceName);
    }

    private VMInstance aSampleVMInstance() {
        return aSampleVMInstance("foo");
    }

    @Test
    public void testNoCloudsByDefault() {
        final VMInstance sut = aSampleVMInstance();

        assertThat("brand new vm instance shall not have a cloud group", sut.clouds(), is(empty()));
    }

    @Test
    public void validationPassesWhenValid() {
        final VMInstance sut = aSampleVMInstance();
        final Report validation = sut.validate();

        assertThat("Issues detected in a valid VM instance", validation.pass(Report.WITHOUT_WARNING));
    }

    @Test
    public void testIsExternal() {
        final VMInstance sut = aSampleVMInstance();

        assertThat("vm shall be external", sut.isExternal());
        assertThat("vm shall not be internal", !sut.isInternal());
    }

    @Test
    public void testIsVM() {
        final VMInstance sut = aSampleVMInstance();

        assertThat("is a VM", sut.isVM());
    }

   
}
