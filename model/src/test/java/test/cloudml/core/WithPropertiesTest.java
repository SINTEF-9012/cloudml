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

import org.cloudml.core.NamedElement;
import org.cloudml.core.Property;
import org.cloudml.core.WithProperties;
import org.junit.Test;


import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public abstract class WithPropertiesTest extends NamedElementTest {

    @Override
    public final NamedElement aSampleElementWithDefaultName() {
        return aSampleWithProperties();
    }

    @Override
    public final NamedElement aSampleElementWithName(String name) {
        return aSampleWithProperties(name);
    }
    
    public abstract WithProperties aSampleWithProperties(String name);

    public WithProperties aSampleWithProperties() {
        return aSampleWithProperties(NamedElement.DEFAULT_NAME); 
    }
    
    
    @Test
    public void testNoPropertiesByDefault() {
        final WithProperties sut = aSampleWithProperties();
        
        assertThat("no properties", sut.getProperties().isEmpty());
    }
    
    
    @Test
    public void testAddingOneProperty() {
        final WithProperties sut = aSampleWithProperties();
        final String key = "foo";
        final String value = "bar";
        
        sut.getProperties().add(new Property(key, value));
        
        assertThat("property key is defined", sut.hasProperty(key));
        assertThat("property key has value", sut.hasProperty(key, value));
        assertThat("property count", sut.getProperties().size(), is(equalTo(1)));
    }
    
    @Test
    public void propertiesShouldBeRemovable() {
        final WithProperties sut = aSampleWithProperties();
        final String key = "foo";
        final String value = "bar";
        
        sut.getProperties().add(new Property(key, value));
        sut.getProperties().remove(new Property(key, value));
        
        assertThat("property is not defined", !sut.hasProperty(key));
        assertThat("property does not has the given value", !sut.hasProperty(key, value));
        assertThat("property count", sut.getProperties().size(), is(equalTo(0)));
        
    }
    
    
    @Test
    public void removingAPropertyThatDoesNotExistsShouldNotFail() {
        final WithProperties sut = aSampleWithProperties();
         final String key = "foo";
        final String value = "bar";
        
        sut.getProperties().remove(new Property(key, value));
                
        assertThat("property is not defined", !sut.hasProperty(key));
        assertThat("property does not has the given value", !sut.hasProperty(key, value));
        assertThat("property count", sut.getProperties().size(), is(equalTo(0)));
        
    }
    
}
