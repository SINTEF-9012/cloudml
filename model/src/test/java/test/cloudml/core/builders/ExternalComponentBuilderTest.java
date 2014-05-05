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
import org.cloudml.core.ExternalComponent;
import org.cloudml.core.Provider;
import org.cloudml.core.builders.ComponentBuilder;
import org.cloudml.core.builders.ExternalComponentBuilder;
import org.junit.Test;

import static org.cloudml.core.builders.Commons.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class ExternalComponentBuilderTest extends ComponentBuilderTest {

    @Override
    public final ComponentBuilder<? extends Component, ? extends ComponentBuilder<?, ?>> aSampleComponentBuilder() {
        return aSampleExternalComponentBuilder();
    }

    public ExternalComponentBuilder<? extends ExternalComponent, ? extends ExternalComponentBuilder<?, ?>> aSampleExternalComponentBuilder() {
        return anExternalComponent();
    }

    @Test
    public void testBuildWithDefaultProvider() {
        final ExternalComponent sut = aSampleExternalComponentBuilder().build();

        assertThat("shall have a provider", sut.getProvider(), is(not(nullValue())));
    }

    @Test
    public void testBuildWithASpecificProvider() {
        final String providerName = "Amazon EC2";

        final ExternalComponent sut = aSampleExternalComponentBuilder()
                .providedBy(providerName)
                .build();

        final Provider provider = sut.getProvider();

        assertThat("has a provider", provider, is(not(nullValue())));
        assertThat("proper provider", provider.getName(), is(equalTo(providerName)));
    }
}
