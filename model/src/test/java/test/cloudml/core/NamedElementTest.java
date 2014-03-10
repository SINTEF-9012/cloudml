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

import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import org.cloudml.core.NamedElement;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class NamedElementTest extends TestCase {
    
    @Test(expected = IllegalArgumentException.class)
    public void emptyStringAsNameIsRejectedByConstructor() {
        new NamedElement("") {
        };
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void nullAsNameIsRejectedByConstructor() {
        new NamedElement(null) {
        };
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void setNameRejectsNull() {
        NamedElement namedElement = new NamedElement("name") {
        };
        namedElement.setName(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void setNameRejectsEmptyString() {
        NamedElement namedElement = new NamedElement("name") {
        };
        namedElement.setName("");
    }
    
    @Test
    public void constructorUsedTheDefaultName() {
        NamedElement namedElement = new NamedElement() {
        };
        assertEquals("name", NamedElement.DEFAULT_NAME, namedElement.getName());
    }
    
    @Test
    public void nameCanBeSetInConstructorAndRetrived() {
        final String name = "my name";
        NamedElement namedElement = new NamedElement(name) {
        };
        assertEquals("name", name, namedElement.getName());
    }
    
    @Test
    public void nameCanBeSetAndRetrived() {
        final String name = "my name";
        NamedElement namedElement = new NamedElement() {
        };
        namedElement.setName(name);
        assertEquals("name", name, namedElement.getName());
    }
}
