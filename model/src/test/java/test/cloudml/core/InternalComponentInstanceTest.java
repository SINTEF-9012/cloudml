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
import org.cloudml.core.builders.DeploymentBuilder;
import org.cloudml.core.builders.InternalComponentBuilder;
import org.cloudml.core.builders.InternalComponentInstanceBuilder;
import org.cloudml.core.builders.VMInstanceBuilder;
import org.cloudml.core.samples.SensApp;
import org.cloudml.core.validation.Report;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.cloudml.core.builders.Commons.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import static org.cloudml.core.samples.SshClientServer.*;

@RunWith(JUnit4.class)
public class InternalComponentInstanceTest extends ComponentInstanceTest {

    private static final String REQUIRED_PORT_NAME = "rp#1";

    @Test
    public void testContainmentInDeploymentModel() {
        final InternalComponentInstance instance = aSampleInternalComponentInstance();

        final Deployment owner = instance.getOwner().get();

        assertThat("missing owner", owner, is(not(nullValue())));
        assertThat("contained in its owner", owner.getComponentInstances().onlyInternals(), contains(instance));
    }

    @Override
    public final ComponentInstance<? extends Component> aSampleComponentInstance(String name) {
        return aSampleInternalComponentInstance(name);
    }

    private InternalComponentInstance aSampleInternalComponentInstance() {
        return aSampleInternalComponentInstance("foo");
    }

    private InternalComponentInstance aSampleInternalComponentInstance(String name) {
        final String vmName = "the vm";
        final String applicationTypeName = "Sample Application";
        final Deployment model = aDeployment()
                .with(amazonEc2())
                .with(ec2LargeLinux())
                .with(aVMInstance()
                    .named(vmName)
                    .ofType(EC2_LARGE_LINUX))
                .with(anInternalComponent()
                    .named(applicationTypeName)
                    .with(aProvidedPort()
                        .named("pp#1"))
                    .with(aRequiredPort()
                .named(REQUIRED_PORT_NAME))
                        .with(aRequiredExecutionPlatform()
                            .named(UBUNTU_1204)))
                        .with(anInternalComponentInstance()
                .named(name)
                .ofType(applicationTypeName)
                .hostedBy(vmName))
                .build();

        return model.getComponentInstances().onlyInternals().firstNamed(name);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void requiredPortsCannotBeAdded() {
        final InternalComponentInstance sut = aSampleInternalComponentInstance();
        sut.getRequiredPorts().add(aRequiredPortInstance());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void requiredPortsCannotBeRemoved() {
        final InternalComponentInstance sut = aSampleInternalComponentInstance();
        sut.getRequiredPorts().remove(aRequiredPortInstance());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void requiredPortsCannotBeCleared() {
        final InternalComponentInstance sut = aSampleInternalComponentInstance();
        sut.getRequiredPorts().clear();
    }

    @Test
    public void validationPassesWhenValid() {
        final InternalComponentInstance instance = aSampleInternalComponentInstance();

        final Report validation = instance.validate();

        assertThat("no issues shall be detected on a valid internal component instance",
                   validation.pass(Report.WITHOUT_WARNING));
    }

    private RequiredPortInstance aRequiredPortInstance() {
        return new RequiredPortInstance("foo", new RequiredPort("foo type", false));
    }

    @Override
    public void detectsComponentsThatCanBeHosted() {
        final Deployment deployment = aDeploymentWithAJetty()
                .with(aWarApplication("JSR 154"))
                .build();

        final InternalComponentInstance sut = deployment.getComponentInstances().onlyInternals().firstNamed("sut");
        final InternalComponent toBeDeployed = deployment.getComponents().onlyInternals().firstNamed("war app"); 

        assertThat("can be hosted", sut.canHost(toBeDeployed));
    }

    @Override
    public void detectsComponentsThatCannotBeHosted() {
        final Deployment deployment = aDeploymentWithAJetty()
                .with(aWarApplication("JSR 155"))
                .build();

        final InternalComponentInstance sut = deployment.getComponentInstances().onlyInternals().firstNamed("sut");
        final InternalComponent toBeDeployed = deployment.getComponents().onlyInternals().firstNamed("war app");

        assertThat("cannot be hosted", !sut.canHost(toBeDeployed));
    }
    
    @Test
    public void testExternalHost() {
        final Deployment deployment = 
                SensApp.completeSensApp()
                .build();
        
        final InternalComponentInstance sut = deployment
                .getComponentInstances()
                .onlyInternals()
                .firstNamed(SensApp.SENSAPP_ADMIN_1);
        
        final VMInstance externalHost = deployment
                .getComponentInstances()
                .onlyVMs()
                .firstNamed(SensApp.SENSAPP_ADMIN_VM);
        
        final VMInstance host = sut.externalHost().asVM();  
        
        assertThat("external host", host, is(sameInstance(externalHost)));
    }

    private InternalComponentBuilder jetty() {
        return anInternalComponent()
                .named("Jetty")
                .with(aProvidedExecutionPlatform()
                .offering("War Container", "JSR 154"));
    }

    private InternalComponentBuilder aWarApplication(String jsr) {
        return anInternalComponent()
                .named("war app")
                .with(aRequiredExecutionPlatform()
                .demanding("War Container", jsr));
    }

    private VMInstanceBuilder oneEc2LargeLinux() {
        return aVMInstance()
                .named("host")
                .ofType(EC2_LARGE_LINUX);
    }

    private InternalComponentInstanceBuilder oneJettyInstance() {
        return anInternalComponentInstance()
                .named("sut")
                .ofType("Jetty")
                .hostedBy("host");
    }

    private DeploymentBuilder aDeploymentWithAJetty() {
        return aDeployment()
                .with(amazonEc2())
                .with(ec2LargeLinux())
                .with(oneEc2LargeLinux())
                .with(jetty())
                .with(oneJettyInstance());
    }
}
