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
import org.cloudml.core.Artefact;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.Node;
import org.cloudml.core.Provider;
import org.cloudml.core.validation.Report;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.cloudml.core.builders.Commons.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class DeploymentModelTest extends TestCase {

    @Test
    public void addedProvidersAreContained() {
        DeploymentModel model = new DeploymentModel();
        final Provider provider = new Provider("ec2");

        model.addProvider(provider);

        assertThat("provider included", model.contains(provider), is(true));
    }

    @Test
    public void removedProviderAreNotContainedAnymore() {
        DeploymentModel model = new DeploymentModel();
        final Provider provider = new Provider("ec2");
        model.addProvider(provider);

        model.removeProvider(provider);

        assertThat("provider not contained", model.contains(provider), is(false));
    }

    @Test(expected = IllegalStateException.class)
    public void rejectRemovingProvidersThatAreStillInUse() {
        DeploymentModel model = aDeployment()
                .withProvider(aProvider().named("ec2"))
                .withNodeType(aNode()
                .named("Large Linux")
                .providedBy("ec2"))
                .build();
        Provider provider = model.findProviderByName("ec2");
        model.removeProvider(provider);
    }

    /*
     * Node manipulation
     */
    @Test
    public void addedNodeAreContained() {
        DeploymentModel model = new DeploymentModel();
        final Provider provider = new Provider("ec2");
        model.addProvider(provider);

        final Node node = new Node("my node", provider);
        model.addNode(node);

        assertThat("node is contained", model.contains(node), is(true));
    }

    @Test(expected = IllegalStateException.class)
    public void rejectAddingNodeWithForeignProvider() {
        DeploymentModel model = new DeploymentModel();
        Node node = new Node("my node", new Provider("ec2"));

        model.addNode(node);
    }

    @Test
    public void removedNodeAreNotContainedAnymore() {
        DeploymentModel model = aDeployment()
                .withProvider(aProvider().named("ec2"))
                .withNodeType(aNode()
                .named("My Node")
                .providedBy("ec2"))
                .build();

        final Node node = model.findNodeByName("My Node");
        model.removeNode(node);

        assertFalse(model.contains(node));
    }

    @Test(expected = IllegalStateException.class)
    public void rejectRemoveNodeThatStillHaveInstances() {
        DeploymentModel model = aDeployment()
                .withProvider(aProvider().named("ec2"))
                .withNodeType(aNode()
                .named("Linux")
                .providedBy("ec2"))
                .withNodeInstance(aNodeInstance()
                .named("i1")
                .ofType("Linux"))
                .build();

        Node node = model.findNodeByName("Linux");
        model.removeNode(node);
    }

    @Test
    public void addedArtefactAreContained() {
        DeploymentModel model = new DeploymentModel();
        assertTrue(model.isEmpty());

        final Artefact artefact = new Artefact("my artefact");
        model.addArtefact(artefact);

        assertTrue(model.contains(artefact));
    }

    @Test
    public void removedArtefactAreNotContained() {
        DeploymentModel model = new DeploymentModel();
        final Artefact artefact = new Artefact("my artefact");
        model.addArtefact(artefact);
        assertTrue(model.contains(artefact));

        model.removeArtefact(artefact);

        assertFalse(model.contains(artefact));
    }

    @Test(expected = IllegalStateException.class)
    public void rejectRemovingArtefactsThatAreStillInUse() {
        DeploymentModel model = aDeployment()
                .withProvider(aProvider().named("EC2"))
                .withNodeType(aNode().named("Linux").providedBy("EC2"))
                .withNodeInstance(aNodeInstance()
                    .named("host no. 1")
                    .ofType("Linux"))
                .withArtefact(anArtefact()
                    .named("My Artefact")
                    .withServerPort(aServerPort()
                        .named("server")))
                .withArtefactInstance(anArtefactInstance()
                    .named("My instance")
                    .ofType("My Artefact")
                    .hostedBy("host no. 1"))
                .build();
        
        Artefact artefact = model.findArtefactByName("My Artefact");
        model.removeArtefact(artefact);
    }

    @Test
    public void validateWithoutWarningsWhenValid() {
        DeploymentModel model = new DeploymentModel();

        Report validation = model.validate();

        assertTrue(validation.pass(Report.WITHOUT_WARNING));
    }

    @Test
    public void validationReportsEmptyModel() {
        DeploymentModel model = new DeploymentModel();

        Report validation = model.validate();

        assertTrue(validation.hasWarningAbout("empty", "deployment", "model"));
    }
}
