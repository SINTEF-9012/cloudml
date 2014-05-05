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

import org.cloudml.core.Component;
import org.cloudml.core.ComponentInstance;
import org.cloudml.core.Deployment;
import org.cloudml.core.InternalComponentInstance;
import org.cloudml.core.builders.ComponentInstanceBuilder;
import org.cloudml.core.builders.InternalComponentInstanceBuilder;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import static org.cloudml.core.builders.Commons.*;

import static org.cloudml.core.samples.SshClientServer.*;

public class InternalComponentInstanceBuilderTest extends ComponentInstanceBuilderTest {

    @Override
    public ComponentInstanceBuilder<? extends ComponentInstance<? extends Component>, ? extends ComponentInstanceBuilder<?, ?>> aSampleComponentInstanceBuilder() {
        return aSampleInternalComponentInstanceBuilder();
    }
    
    
    private InternalComponentInstanceBuilder aSampleInternalComponentInstanceBuilder() {
        return anInternalComponentInstance();
    }

    
    @Test
    public void buildsWithNoHost() {
        final InternalComponentInstance sut = aSampleInternalComponentInstanceBuilder()
                .build();
        
        assertThat("no host", sut.getHost(), is(nullValue())); 
    }
    
    @Test
    public void integrationInDeployment() {
        final String hostName = "host";
        final String sutName = "sut";
        final Deployment deployment = aDeployment()
                .with(amazonEc2())
                .with(ec2LargeLinux())
                .with(aVMInstance()
                    .named(hostName)
                    .ofType(EC2_LARGE_LINUX))
                .with(sshClient())
                .with(anInternalComponentInstance()
                    .named(sutName)
                    .ofType(SSH_CLIENT)
                    .hostedBy(hostName))
                .build();
        
        final InternalComponentInstance sut = deployment.getComponentInstances().onlyInternals().firstNamed(sutName);
        
        assertThat("sut exists", sut, is(not(nullValue())));
        assertThat("sut name", sut.getName(), is(equalTo(sutName)));
        assertThat("sut type", sut.getType(), is(not(nullValue())));
        assertThat("sut type", deployment.getComponents().onlyInternals(), contains(sut.getType()));
        assertThat("executeOn created", deployment.getExecuteInstances(), hasSize(1));
        assertThat("sut has host", sut.getHost(), is(not(nullValue())));
    }
    
    
}
