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

import junit.framework.TestCase;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import org.cloudml.core.Artefact;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.Node;
import org.cloudml.core.validation.Report;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.cloudml.core.builders.DeploymentModelBuilder;
import static org.cloudml.core.builders.Commons.*;

/**
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public class DeploymentModelTest extends TestCase {

    @Test
    public void addedNodeAreContained() {
        DeploymentModel model = new DeploymentModel();
        assertTrue(model.isEmpty());

        final Node node = new Node("my node");
        model.addNode(node);

        assertTrue(model.contains(node));
    }

    @Test
    public void addedNodeCanBeFound() {
        DeploymentModel model = new DeploymentModel();
        assertTrue(model.isEmpty());

        final String nodeName = "my node";
        final Node expected = new Node(nodeName);
        model.addNode(expected);
        Node actual = model.findNodeByName(nodeName);

        assertEquals(expected, actual);
    }

    @Test
    public void removedNodeAreNotContainedAnymore() {
        DeploymentModel model = new DeploymentModel();
        final Node node = new Node("my node");
        model.addNode(node);
        assertTrue(model.contains(node));

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
