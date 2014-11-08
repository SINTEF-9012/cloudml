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

package test.cloudml.core.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.cloudml.core.Deployment;
import org.cloudml.core.samples.SensApp;
import org.cloudml.core.samples.SshClientServer;
import org.cloudml.core.validation.DeploymentValidator;
import org.cloudml.core.validation.Report;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Check whether the samples are valid (they should be)
 */
@RunWith(Parameterized.class)
public class SampleValidationTest {

    private final String name;
    private final Deployment sampleDeployment;

    public SampleValidationTest(String name, Deployment sampleDeployment) {
        this.name = name;
        this.sampleDeployment = sampleDeployment;
    }
    
    
    @Parameterized.Parameters(name="{0}")
    public static Collection<Object[]> samples(){
        final List<Object[]> results = new ArrayList<Object[]>(10);
        
        results.add(new Object[]{"SensApp", SensApp.completeSensApp().build()});
        results.add(new Object[]{"SSH - Client/Server", SshClientServer.getOneClientConnectedToOneServer().build()});
        results.add(new Object[]{"SSH - 2 Clients / 1 Server", SshClientServer.getTwoClientsConnectedToOneServer().build()});
        results.add(new Object[]{"SSH - 2 Clients / 2 Servers", SshClientServer.getTwoClientsConnectedToTwoServers().build()});
        
        return results;
    }
    
    @Test 
    public void validationOf() {
        DeploymentValidator validator = new DeploymentValidator();
        Report validation = validator.validate(sampleDeployment);
        
        
        assertThat(validation.toString(), validation.pass(Report.WITHOUT_WARNING));
       
    }
    
}
