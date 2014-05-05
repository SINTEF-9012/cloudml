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

import org.cloudml.core.Deployment;
import org.cloudml.core.ExternalComponent;
import org.cloudml.core.VM;
import org.cloudml.core.VMInstance;
import org.cloudml.core.validation.Report;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


import static org.cloudml.core.builders.Commons.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import static org.cloudml.core.samples.SshClientServer.*;

@RunWith(JUnit4.class)
public class VMTest extends ExternalComponentTest {
    
    @Override
    public final ExternalComponent aSampleExternalComponent(String name) {
        return aSampleVM(name);
    }
    
    private VM aSampleVM(String name) {
        Deployment context = aDeployment()
                .with(amazonEc2())
                .with(aVM()
                    .named(name)
                    .providedBy(AMAZON_EC2))
                .build();
        
        return context.getComponents().onlyVMs().firstNamed(name);
    }
    
    private VM aSampleVM() {
        return aSampleVM("SUT");
    }
    
    @Test
    public void testValidVMhaveNoIssue() {
        final VM node = aSampleVM();
        final Report validation = node.validate();
        
        assertThat("shall have no issue, but found " + validation,
                   validation.pass(Report.WITHOUT_WARNING));
    }
    
    @Test
    public void testInstantiates() {
        final String instanceName = "The instance";
        
        final VM type = ec2LargeLinux().build();
        
        final VMInstance instance = type.instantiates(instanceName);
        
        Verify.correctVMInstantiation(type, instance);
    }
    
    @Test
    public void testEqualToItself() {
        final VM sut = aSampleVM();
        assertThat("equals itself", sut, is(equalTo(sut)));
    }
    
    @Test
    public void testNotEqualToNull() {
        final VM vm = aSampleVM();
        final VM nullVM = null;
        
        assertThat("not equal to null", vm, is(not(equalTo(nullVM))));
    }
    
    @Test
    public void testNotEqualToAnotherType() {
        final VM vm = aSampleVM();
        final Object object = new Double(23);
        
        assertThat("not equal to other object", vm, is(not(equalTo(object))));
    }
}
