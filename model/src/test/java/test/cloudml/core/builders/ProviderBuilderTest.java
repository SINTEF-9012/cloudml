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

import org.cloudml.core.Provider;
import org.cloudml.core.WithResources;
import org.cloudml.core.builders.ProviderBuilder;
import org.cloudml.core.builders.WithResourcesBuilder;
import org.junit.Test;

import static org.cloudml.core.builders.Commons.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class ProviderBuilderTest extends WithResourcesBuilderTest {

    @Override
    public WithResourcesBuilder<? extends WithResources, ? extends WithResourcesBuilder<?, ?>> aSampleWithResourceBuilder() {
        return aSampleProviderBuilder();
    }

    public ProviderBuilder aSampleProviderBuilder() {
        return aProvider();
    }

    @Test
    public void testProviderWithDefaultCredentials() {
        final Provider sut = aSampleProviderBuilder().build();

        assertThat("Has default credentials", sut.getCredentials(), is(not(nullValue())));
    }

//   FIXME: Should be uncommented when there are builder for various type of credentials
//    
//    @Test
//    public void testProviderWithSpecificCredentials() {
//        final String credentials = "sample credentials";
//        
//        final Provider sut = aSampleProviderBuilder()
//                .withCredentials(credentials)
//                .build();
//        
//        assertThat("has credentials", sut.getCredentials(), is(not(nullValue())));
//        assertThat("has correct credentials", sut.getCredentials(), is(equalTo(credentials)));
//    }
}
