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

import org.cloudml.core.*;
import org.cloudml.core.validation.Report;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public abstract class ComponentInstanceTest extends WithResourcesTest {

    @Override
    public final WithResources aSampleWithResources(String name) {
        return aSampleComponentInstance(name);
    }

    public abstract ComponentInstance<? extends Component> aSampleComponentInstance(String name);

    public ComponentInstance<? extends Component> aSampleComponentInstance() {
        return aSampleComponentInstance("foo");
    }

    @Test
    public void validationDetectsMissingOwner() {
        final ComponentInstance<? extends Component> sut = aSampleComponentInstance();
        sut.getOwner().discard();

        final Report validation = sut.validate();

        assertThat("missing owner not detected", validation.hasErrorAbout("no", "owner"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullIsRejectedAsType() {
        final ComponentInstance<? extends Component> sut = aSampleComponentInstance();
        sut.setType(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void providedPortsCannotBeAdded() {
        final ComponentInstance<? extends Component> sut = aSampleComponentInstance();
        sut.getProvidedPorts().add(new ProvidedPortInstance("foo", new ProvidedPort("foo type", true)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void providedPortsCannotBeRemoved() {
        final ComponentInstance<? extends Component> sut = aSampleComponentInstance();
        sut.getProvidedPorts().clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void providedExecutionPlatformCannotBeAdded() {
        final ComponentInstance<? extends Component> sut = aSampleComponentInstance();
        sut.getProvidedExecutionPlatforms().add(new ProvidedExecutionPlatformInstance("foo", new ProvidedExecutionPlatform("foo type")));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void providedExecutionPlatformsCannotBeRemoved() {
        final ComponentInstance<? extends Component> sut = aSampleComponentInstance();
        sut.getProvidedExecutionPlatforms().clear();
    }

    @Test
    public abstract void detectsComponentsThatCanBeHosted();

    @Test
    public abstract void detectsComponentsThatCannotBeHosted();
}
