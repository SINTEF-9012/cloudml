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

import org.cloudml.core.NamedElement;
import org.cloudml.core.WithProperties;
import org.cloudml.core.builders.NamedElementBuilder;
import org.cloudml.core.builders.WithPropertyBuilder;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public abstract class WithPropertiesBuilderTest extends NamedElementBuilderTest {

    @Override
    public final NamedElementBuilder<? extends NamedElement, ? extends NamedElementBuilder<?, ?>> aSampleNamedElement() {
        return aSampleWithpropertiesBuilder();
    }

    public abstract WithPropertyBuilder<? extends WithProperties, ? extends WithPropertyBuilder<?, ?>> aSampleWithpropertiesBuilder();
    
    @Test
    public void testBuildWithoutProperty() {
        final WithProperties sut = aSampleWithpropertiesBuilder().build();
        
        assertThat("shall not be null", sut, is(not(nullValue())));
        assertThat("shall not have any property", sut.getProperties().isEmpty());
    }
    
    @Test
    public void testBuildWithOneProperty() {
        final String value = "a value";
        final String key = "a property";
        
        final WithProperties sut = aSampleWithpropertiesBuilder()
                .withProperty(key, value).build();
        
        assertThat("shall not be null", sut, is(not(nullValue())));
        assertThat("only one property", sut.getProperties().size(), is(equalTo(1)));
        assertThat("key is defined", sut.hasProperty(key));
        assertThat("key has proper value", sut.hasProperty(key, value));
    }
    
    
}
