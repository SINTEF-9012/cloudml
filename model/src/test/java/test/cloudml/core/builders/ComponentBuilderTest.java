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
package test.cloudml.core.builders;

import org.cloudml.core.Component;
import org.cloudml.core.ProvidedExecutionPlatform;
import org.cloudml.core.ProvidedPort;
import org.cloudml.core.WithResources;
import org.cloudml.core.builders.ComponentBuilder;
import org.cloudml.core.builders.WithResourcesBuilder;
import org.junit.Test;

import static org.cloudml.core.builders.Commons.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public abstract class ComponentBuilderTest extends WithResourcesBuilderTest {

    @Override
    public final WithResourcesBuilder<? extends WithResources, ? extends WithResourcesBuilder<?, ?>> aSampleWithResourceBuilder() {
        return aSampleComponentBuilder();
    }

    public abstract ComponentBuilder<? extends Component, ? extends ComponentBuilder<?, ?>> aSampleComponentBuilder();

    @Test
    public void testBuildWithoutAnyProvidedPort() {
        final Component sut = aSampleComponentBuilder().build();

        assertThat("shall not have any provided ports", sut.getProvidedPorts(), is(empty()));
    }

    @Test
    public void testBuildWithOneProvidedPort() {
        final String portName = "foo port";

        final Component sut = aSampleComponentBuilder()
                .with(aProvidedPort().named(portName))
                .build();

        assertThat("shall not have one provided port", sut.getProvidedPorts(), hasSize(1));
        final ProvidedPort port = sut.getProvidedPorts().firstNamed(portName);
        assertThat("port shall exist", port, is(not(nullValue())));
        assertThat("port's name shall be correct", port.isNamed(portName));
        assertThat("port's owner shall be set", port.getOwner().get(), is(sameInstance(sut)));
    }

    @Test
    public void testBuildWithDefaultProvidedExecutionPlatform() {
        final Component sut = aSampleComponentBuilder().build();

        assertThat("shall not provide any execution platform", sut.getProvidedExecutionPlatforms().isEmpty());
    }

    @Test
    public void testBuildWithOneSpecificProvidedExecutionPlatform() {
        final String platformName = "Ubuntu 12.04";

        final Component sut = aSampleComponentBuilder()
                .with(aProvidedExecutionPlatform().named(platformName))
                .build();

        assertThat("shall porvide only 1 execution platform", sut.getProvidedExecutionPlatforms(), hasSize(1));
        final ProvidedExecutionPlatform platform = sut.getProvidedExecutionPlatforms().toList().get(0);
        assertThat("provided execution platform name", platform.isNamed(platformName));
        assertThat("provided platform owner shall be set", platform.getOwner().get(), is(sameInstance(sut)));

    }
}
