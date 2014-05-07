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
package test.cloudml.core;

import junit.framework.TestCase;
import org.cloudml.core.*;
import org.cloudml.core.validation.Report;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.cloudml.core.builders.Commons.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import static org.cloudml.core.samples.SshClientServer.*;


@RunWith(JUnit4.class)
public class ExecuteOnTest extends TestCase {
    //TODO: test propagation withSubject demands and offers for internal components

    private ExecuteInstance aSampleExecuteInstance() {
        final String appInstanceName = "the client";
        final String vmInstanceName = "vm instance";
        final Deployment model = aDeployment()
                .with(amazonEc2())
                .with(ec2LargeLinux())
                .with(sshClient())
                .with(aVMInstance()
                    .named(vmInstanceName)
                    .ofType(EC2_LARGE_LINUX))
                .with(anInternalComponentInstance()
                    .named(appInstanceName)
                    .ofType(SSH_CLIENT)
                    .hostedBy(vmInstanceName))
                .build();
        String requiredName=model.getComponentInstances().onlyInternals().firstNamed(appInstanceName).getRequiredExecutionPlatform().getName();
        String providedName=model.getComponentInstances().onlyVMs().firstNamed(vmInstanceName).getProvidedExecutionPlatforms().toList().get(0).getName();
        final ExecuteInstance instance = model.getExecuteInstances().between(appInstanceName, requiredName, vmInstanceName, providedName);
        assertThat("shall not be null", instance, is(not(nullValue())));
        
        return instance;
    }

    @Test
    public void testContainmentInDeployment() {
        final ExecuteInstance execute = aSampleExecuteInstance();
        final Deployment owner = execute.getOwner().get();

        assertThat("shall have an owner", owner, is(not(nullValue())));
        assertThat("shall be contained in its owner", owner.getExecuteInstances(), contains(execute));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void nullIsRejectedAsProvidedEnd() {
        final ExecuteInstance execute = aSampleExecuteInstance();
        execute.setProvidedEnd(null); 
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void nullIsRejectedAsRequiredEnd() {
        final ExecuteInstance execute = aSampleExecuteInstance();
        execute.setRequiredEnd(null);
    }
        
    @Test
    public void testValidationDetectsMismatchBetweenOffersAndDemands() {
        final ExecuteInstance execute = aSampleExecuteInstance();
        execute.getProvidedEnd().getType().getOffers().add(new Property("type", "Linux"));
        execute.getRequiredEnd().getType().getDemands().add(new Property("type", "Windows"));
        
        final Report validation = execute.validate();
        
        assertThat("mismatch between offers and demands must be forbidden (found: " + validation + ")",
                   validation.hasErrorAbout("Mismatch",  "type", "offered", "Linux", "Demanded", "Windows"));
    }
    
}
