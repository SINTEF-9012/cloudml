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
import org.cloudml.core.Relationship;
import org.cloudml.core.WithResources;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.cloudml.core.builders.Commons.*;
import org.cloudml.core.validation.Report;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import static org.cloudml.core.samples.SshClientServer.*;

@RunWith(JUnit4.class)
public class RelationshipTest extends WithResourcesTest {

    @Override
    public final WithResources aSampleWithResources(String name) {
        return aSampleRelationship(name);
    }

    private Relationship aSampleRelationship(String name) {
        final Deployment model = aDeployment()
                .with(sshClient())
                .with(sshServer())
                .with(sshConnection().named(name))
                .build();

        return model.getRelationships().firstNamed(name);
    }

    private Relationship aSampleRelationship() {
        return aSampleRelationship(SSH_CONNECTION);
    }
            
    @Test
    public void testContainementInDeployment() {
        final Relationship relationship = aSampleRelationship();

        final Deployment owner = relationship.getOwner().get();

        assertThat("no owner", owner, is(not(nullValue())));
        assertThat("is contained by its owner", owner.getRelationships(), contains(relationship));
    }

    @Test
    public void validationPassWhenValid() {
        final Relationship relationship = aSampleRelationship();

        final Report validation = relationship.validate();

        assertThat("no issue shall be found in a valid relationship", validation.pass(Report.WITHOUT_WARNING));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRelationshipsRejectNullAsRequiredEnd() {
        final Relationship relationship = aSampleRelationship();
        relationship.setRequiredEnd(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRelationshipsRejectsNullAsProvidedServerEnd() {
        final Relationship relationship = aSampleRelationship();
        relationship.setProvidedEnd(null);
    }

    @Test
    public void validationDetectsLocalClientsConnectedToRemoteServers() {
        final Relationship relationship = aSampleRelationship();
        relationship.getRequiredEnd().setLocal(true);

        final Report validation = relationship.validate();

        assertThat(
                "relationships between a local client and a remote server shall be forbidden",
                validation.hasErrorAbout("local client", "remote server"));
    }

    @Test
    public void validationDetectsRemoteClientsConnectedToLocalServers() {
        final Relationship relationship = aSampleRelationship();
        relationship.getProvidedEnd().setLocal(true);

        final Report validation = relationship.validate();

        assertThat(
                "relationships between a remote client and local server shall be forbidden",
                validation.hasErrorAbout("remote client", "local server"));
    }
}
