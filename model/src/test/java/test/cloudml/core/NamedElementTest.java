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
import org.cloudml.core.NamedElement;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;


@RunWith(JUnit4.class)
public abstract class NamedElementTest extends TestCase {
    
    public abstract NamedElement aSampleElementWithDefaultName();
    
    public abstract NamedElement aSampleElementWithName(String name);
    
    
    @Test(expected = IllegalArgumentException.class)
    public void emptyStringIsRejectedAsNameByConstructor() {
        aSampleElementWithName("");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void nullIsRejectedAsNameByConstructor() {
        aSampleElementWithName(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void nullIsRejectedAsName() {
        final NamedElement sut = aSampleElementWithDefaultName();
        sut.setName(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void emptyStringIsRejectedAsName() {
        final NamedElement sut = aSampleElementWithDefaultName();
        sut.setName("");
    }

    
    @Test
    public void nameInitialisationInConstructor() {
        final String name = "my name";
        final NamedElement sut = aSampleElementWithName(name);
        
        assertThat("wrong name retrieved", sut.getName(), is(equalTo(name)));
    }
    
    @Test
    public void nameUpdate() {
        final String name = "my name";
        final NamedElement sut = aSampleElementWithDefaultName();
        
        sut.setName(name);
        
        assertThat("wrong name retrived", sut.getName(), is(equalTo(name)));
    }
}
