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

import org.cloudml.core.Component;
import org.cloudml.core.ComponentInstance;
import org.cloudml.core.Deployment;
import org.cloudml.core.ExternalComponent;
import org.cloudml.core.ExternalComponentInstance;
import org.cloudml.core.InternalComponent;
import org.cloudml.core.builders.DeploymentBuilder;
import org.cloudml.core.builders.ExternalComponentBuilder;
import org.cloudml.core.builders.ExternalComponentInstanceBuilder;
import org.cloudml.core.builders.InternalComponentBuilder;
import org.junit.Test;

import static org.cloudml.core.builders.Commons.*;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import static org.cloudml.core.samples.SshClientServer.*;

public class ExternalComponentInstanceTest extends ComponentInstanceTest {

    @Override
    public ComponentInstance<? extends Component> aSampleComponentInstance(String name) {
        return aSampleExternalComponentInstance(name);
    }

    public ExternalComponentInstance<? extends ExternalComponent> aSampleExternalComponentInstance(String name) {
        return anExternalComponentInstance().named(name).build();
    }

    public ExternalComponentInstance<? extends ExternalComponent> aSampleExternalComponentInstance() {
        return aSampleExternalComponentInstance("foo");
    }

    @Test
    public void testPublicIpAddressAccess() {
        final String anIp = "127.0.0.1";

        final ExternalComponentInstance<? extends ExternalComponent> sut = aSampleExternalComponentInstance();
        sut.setPublicAddress(anIp);

        assertThat("public IP", sut.getPublicAddress(), is(equalTo(anIp)));
    }

    @Override
    public void detectsComponentsThatCanBeHosted() {
        final Deployment deployment = aDeploymentWithOneRunningJetty()
                .with(aWarApplication("JSR 154"))
                .build();

        final ExternalComponentInstance sut = deployment.getComponentInstances().onlyExternals().firstNamed("sut");
        final InternalComponent toBeDeployed = deployment.getComponents().onlyInternals().firstNamed("War App");

        assertThat("can be hosted", sut.canHost(toBeDeployed));
    }

    @Override
    public void detectsComponentsThatCannotBeHosted() {
        final Deployment deployment = aDeploymentWithOneRunningJetty()
                .with(aWarApplication("JSR 155"))
                .build();

        final ExternalComponentInstance sut = deployment.getComponentInstances().onlyExternals().firstNamed("sut"); 
        final InternalComponent toBeDeployed = deployment.getComponents().onlyInternals().firstNamed("War App");

        assertThat("cannot be hosted", !sut.canHost(toBeDeployed));
    }

    private ExternalComponentBuilder jetty() {
        return anExternalComponent()
                .named("Jetty")
                .with(aProvidedExecutionPlatform()
                .offering("War Container", "JSR 154"))
                .providedBy(AMAZON_EC2);
    }

    private ExternalComponentInstanceBuilder oneRunningJetty() {
        return anExternalComponentInstance()
                .named("sut")
                .ofType("Jetty");
    }

    private InternalComponentBuilder aWarApplication(String jsr) {
        return anInternalComponent()
                .named("War App")
                .with(aRequiredExecutionPlatform()
                .demanding("War Container", jsr));
    }

    private DeploymentBuilder aDeploymentWithOneRunningJetty() {
        return aDeployment()
                .with(amazonEc2())
                .with(jetty())
                .with(oneRunningJetty());
    }
}
