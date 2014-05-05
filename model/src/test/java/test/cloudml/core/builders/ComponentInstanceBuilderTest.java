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
import org.cloudml.core.ComponentInstance;
import org.cloudml.core.WithResources;
import org.cloudml.core.builders.ComponentInstanceBuilder;
import org.cloudml.core.builders.WithResourcesBuilder;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public abstract class ComponentInstanceBuilderTest extends WithResourcesBuilderTest {

    @Override
    public WithResourcesBuilder<? extends WithResources, ? extends WithResourcesBuilder<?, ?>> aSampleWithResourceBuilder() {
        return aSampleComponentInstanceBuilder();
    }

    public abstract ComponentInstanceBuilder<? extends ComponentInstance<? extends Component>, ? extends ComponentInstanceBuilder<?,?>> aSampleComponentInstanceBuilder();

    
    @Test
    public void buildsATypeByDefault() {
        final ComponentInstance<? extends Component> sut = aSampleComponentInstanceBuilder().build();
        
        assertThat("build something", sut, is(not(nullValue())));
        assertThat("has no owner", sut.getOwner().isUndefined());
        assertThat("build a default type", sut.getType(), is(not(nullValue())));
    }
    
    
    @Test
    public void buildATypeWithTheGivenName() {
        final String typeName = "My Type";
        
        final ComponentInstance<? extends Component> sut = aSampleComponentInstanceBuilder()
                .ofType(typeName)
                .build();
        
        assertThat("type is properly named", sut.getType().getName(), is(equalTo(typeName)));
    }
    
}
