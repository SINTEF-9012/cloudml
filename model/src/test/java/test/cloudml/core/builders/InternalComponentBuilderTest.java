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
package test.cloudml.core.builders;

import org.cloudml.core.Component;
import org.cloudml.core.InternalComponent;
import org.cloudml.core.RequiredExecutionPlatform;
import org.cloudml.core.RequiredPort;
import org.cloudml.core.builders.ComponentBuilder;
import org.cloudml.core.builders.InternalComponentBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.cloudml.core.builders.Commons.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@RunWith(JUnit4.class)
public class InternalComponentBuilderTest extends ComponentBuilderTest {

    @Override
    public final ComponentBuilder<? extends Component, ? extends ComponentBuilder<?, ?>> aSampleComponentBuilder() {
        return aSampleInternalComponentBuilder();
    }

    public InternalComponentBuilder aSampleInternalComponentBuilder() {
        return anInternalComponent();
    }

    @Test
    public void testBuildWithoutAnyRequiredPort() {
        final InternalComponent sut = aSampleInternalComponentBuilder().build();

        assertThat("shall not have any required port", sut.getRequiredPorts(), is(empty()));
    }

    @Test
    public void testWithASingleRequiredPort() {
        final String portName = "foo port";

        final InternalComponent sut = aSampleInternalComponentBuilder()
                .with(aRequiredPort().named(portName))
                .build();

        assertThat("requires only 1 port", sut.getRequiredPorts(), hasSize(1));
        final RequiredPort port = sut.getRequiredPorts().firstNamed(portName);
        assertThat("port is properly named", port.isNamed(portName));
        assertThat("port's owner is properly set", port.getOwner().get(), is(sameInstance((Component) sut)));

    }

    @Test
    public void testBuildWithDefaultRequiredExecutionPlatform() {
        final InternalComponent sut = aSampleInternalComponentBuilder().build();

        assertThat("shall have a default required execution platform", sut.getRequiredExecutionPlatform(), is(not(nullValue())));
    }

    @Test
    public void testBuildWithASpecificRequiredExecutionPlaform() {
        final String platformName = "Ubuntu 12.04";

        final InternalComponent sut = aSampleInternalComponentBuilder()
                .with(aRequiredExecutionPlatform().named(platformName))
                .build();

        final RequiredExecutionPlatform platform = sut.getRequiredExecutionPlatform();

        assertThat("requires execution platform", platform, is(not(nullValue())));
        assertThat("requires the correct execution plaform", platform.isNamed(platformName));
        assertThat("required execution platform's owner is set", platform.getOwner().get(), is(sameInstance((Component) sut)));
    }
}
