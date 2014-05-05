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

import org.cloudml.core.NamedElement;
import org.cloudml.core.builders.NamedElementBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@RunWith(JUnit4.class)
public abstract class NamedElementBuilderTest {

    public abstract NamedElementBuilder<? extends NamedElement, ? extends NamedElementBuilder<?, ?>> aSampleNamedElement();
    
    @Test
    public void testBuildWithTheDefaultName() {
        final NamedElement sut = aSampleNamedElement()
                .build();
        
        assertThat("the name shall not be null", sut.getName(), is(not(nullValue())));
        assertThat("the name shall be properly set", sut.isNamed(NamedElement.DEFAULT_NAME));
    }

    
    @Test
    public void testBuildWithASpecificName() {
        final String name = "the specific name";
        
        final NamedElement sut = aSampleNamedElement()
                .named(name)
                .build();
        
        assertThat("the name shall not be null", sut.getName(), is(not(nullValue())));
        assertThat("the name shall be properly set", sut.isNamed(name));
    }
    
}
