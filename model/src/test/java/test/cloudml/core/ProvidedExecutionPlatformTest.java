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

import org.cloudml.core.ProvidedExecutionPlatform;
import org.cloudml.core.RequiredExecutionPlatform;
import org.cloudml.core.WithResources;
import org.cloudml.core.validation.Report;
import org.junit.Test;

import static org.cloudml.core.builders.Commons.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class ProvidedExecutionPlatformTest extends WithResourcesTest {

    @Override
    public final WithResources aSampleWithResources(String name) {
        return aSampleProvidedExecutionPlatform(name);
    }

    private ProvidedExecutionPlatform aSampleProvidedExecutionPlatform(String name) {
        return aProvidedExecutionPlatform()
                .named(name)
                .offering("operating system", "Ubuntu 12.04")
                .build();
    }
    
    private ProvidedExecutionPlatform aSampleProvidedExecutionPlatform() {
        return aSampleProvidedExecutionPlatform("sut");
    }

    @Test
    public void validationDetectsMissingOwner() {
        final ProvidedExecutionPlatform provided = aSampleProvidedExecutionPlatform();
        provided.getOwner().discard();
        
        final Report validation = provided.validate();
        
        assertThat("missing owner detected", validation.hasErrorAbout("no", "owner"));
    }
    
    @Test 
    public void validationDetectsEmptyOfferings() {
        final ProvidedExecutionPlatform provided = aSampleProvidedExecutionPlatform();
        provided.getOffers().clear(); 
        
        final Report validation = provided.validate();
        
        assertThat("missing offers detected", validation.hasWarningAbout("no", "offer"));
    }

    @Test
    public void detectsCompatibleRequiredExecutionPlatforms() {
        final ProvidedExecutionPlatform provided = aSampleProvidedExecutionPlatform();
        final RequiredExecutionPlatform required = aRequiredExecutionPlatform()
                .demanding("operating system", "Ubuntu 12.04")
                .build();

        assertThat("shall match", provided.match(required));
    }

    @Test
    public void detectsIncompatibleRequiredExecutionPlatforms() {
        final ProvidedExecutionPlatform provided =  aSampleProvidedExecutionPlatform(); 
        final RequiredExecutionPlatform required = aRequiredExecutionPlatform()
                .demanding("operating system", "Windows 7")
                .build();

        assertThat("shall not match", !provided.match(required));
    }
    
}
