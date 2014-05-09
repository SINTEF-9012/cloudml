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

import org.cloudml.core.Deployment;
import org.cloudml.core.WithProperties;
import org.cloudml.core.builders.WithPropertyBuilder;
import org.junit.Test;

import static org.cloudml.core.samples.SensApp.*;

import static org.cloudml.core.builders.Commons.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class DeploymentBuilderTest extends WithPropertiesBuilderTest {

    @Override
    public WithPropertyBuilder<? extends WithProperties, ? extends WithPropertyBuilder<?, ?>> aSampleWithpropertiesBuilder() {
        return aDeployment(); 
    }
    
    
    @Test
    public void testBuildSensApp() {
        final Deployment sensapp = 
                completeSensApp()
                .build();
        
        assertThat("something is built", sensapp, is(not(nullValue())));
        assertThat("provider count", sensapp.getProviders(), hasSize(3));
        assertThat("type count", sensapp.getComponents(), hasSize(6));
        assertThat("relationship count", sensapp.getRelationships(), hasSize(2));
        assertThat("component instance count", sensapp.getComponentInstances(), hasSize(7));
        assertThat("executeOn count", sensapp.getExecuteInstances(), hasSize(5)); 
        assertThat("clouds count", sensapp.getClouds(), is(empty()));
    }

    
    
}
