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

import org.cloudml.core.*;
import org.cloudml.core.validation.Report;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import static org.cloudml.core.builders.Commons.*;
import static org.cloudml.core.samples.SshClientServer.*; 

@RunWith(JUnit4.class)
public class RelationshipInstanceTest extends WithResourcesTest {

    @Override
    public WithResources aSampleWithResources(String name) {
        return aSampleRelationshipInstance(name);
    }
    
    private RelationshipInstance aSampleRelationshipInstance(String sutName) {
        final String clientHost = "client's host";
        final String serverHost = "server's host";
        final String client = "client";
        final String server = "server";
        final Deployment deployment = aDeployment()
                .with(amazonEc2())
                .with(ec2LargeLinux())
                .with(ec2XLargeWindows7())
                .with(sshClient())
                .with(sshServer())
                .with(sshConnection())
                .with(aVMInstance()
                    .named(clientHost)
                    .ofType(EC2_LARGE_LINUX))
                .with(anInternalComponentInstance()
                    .named(client)
                    .ofType(SSH_CLIENT)
                    .hostedBy(clientHost))
                .with(aVMInstance()
                    .named(serverHost) 
                    .ofType(EC2_XLARGE_WINDOWS_7)) 
                .with(anInternalComponentInstance()
                    .named(server)
                    .ofType(SSH_SERVER)
                    .hostedBy(serverHost))
                .with(aRelationshipInstance()
                    .named(sutName)
                    .from(client, CLIENT_PORT)
                    .to(server, SERVER_PORT)
                    .ofType(SSH_CONNECTION))
                .build();
        
        return deployment.getRelationshipInstances().firstNamed(sutName);
    }
    
     private RelationshipInstance aSampleRelationshipInstance() {
        return aSampleRelationshipInstance("sut");
    }
    
    @Test
    public void testContainementInDeployment() {
        final RelationshipInstance instance = aSampleRelationshipInstance();
        final Deployment owner = instance.getOwner().get();
        
        assertThat("shall have an owner", owner, is(not(nullValue())));
        assertThat("shall be contained by its owner", owner.getRelationshipInstances(), contains(instance));
    }

    @Test
    public void validationPassesWhenValid() {
        final RelationshipInstance instance = aSampleRelationshipInstance();
        final Report validation = instance.validate();

        assertThat("valid relationship shall not have any issue",
                   validation.pass(Report.WITHOUT_WARNING));
    } 

    @Test
    public void validationDetectsIllegalProvidedEnd() {
        final RelationshipInstance instance = aSampleRelationshipInstance();
        instance.getProvidedEnd().setType(new ProvidedPort("Foo", true));

        final Report validation = instance.validate();

        assertThat("illegal type in provided end shall be detected",
                   validation.hasErrorAbout("illegal", "relationship", "instance", "type", "provided"));
    }

    @Test
    public void validationDetectsIllegalRequiredEnd() {
        final RelationshipInstance instance = aSampleRelationshipInstance();
        instance.getRequiredEnd().setType(new RequiredPort("Foo"));

        final Report report = instance.validate();

        assertThat("illegal type in required end shall be detected",
                   report.hasErrorAbout("illegal", "relationship", "instance", "type", "required"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullIsRejectedAsRequiredEnd() {
        RelationshipInstance instance = aSampleRelationshipInstance();
        instance.setRequiredEnd(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullIsRejectedAsProvidedEnd() {
        final RelationshipInstance instance = aSampleRelationshipInstance();
        instance.setProvidedEnd(null);
    }

   
}
