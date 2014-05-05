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
import org.cloudml.core.Provider;
import org.cloudml.core.WithResources;
import org.cloudml.core.validation.Report;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.cloudml.core.builders.Commons.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import static org.cloudml.core.samples.SshClientServer.*;

@RunWith(JUnit4.class)
public class ProviderTest extends WithResourcesTest {


    @Override
    public WithResources aSampleWithResources(String name) {
        return aProvider().named(name).build();
    }

    @Test
    public void validationPassesWhenValid() {
        final Provider sut = aSampleProvider();

        final Report validation = sut.validate();

        assertThat("is valid", validation.pass(Report.WITHOUT_WARNING));
    }

    @Test
    public void testContainmentInDeployment() {
        final Deployment deployment = aDeployment()
                .with(amazonEc2())
                .with(ec2LargeLinux())
                .build();

        final Provider sut = deployment.getProviders().firstNamed(AMAZON_EC2);

        assertThat("provider created", sut, is(not(nullValue())));
        assertThat("provider's owner", sut.getOwner().get(), is(sameInstance(deployment)));
    }

    @Test
    public void testProvidedTypes() {
        final Provider sut = aSampleProvider();
        final ExternalComponent theVM = sut.getDeployment().getComponents().onlyExternals().firstNamed(EC2_LARGE_LINUX); 
        
        assertThat("provider created", sut, is(not(nullValue())));
        assertThat("provided types", sut.providedComponents(), containsInAnyOrder(theVM));
    }
    
    
    private Provider aSampleProvider() {
        final Deployment context = aDeployment()
                .with(amazonEc2())
                .with(ec2LargeLinux())
                .build(); 
        
        return context.getProviders().firstNamed(AMAZON_EC2);
    }
}
