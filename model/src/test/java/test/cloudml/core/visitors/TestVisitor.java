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
package test.cloudml.core.visitors;

import junit.framework.TestCase;
import org.cloudml.core.Artefact;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.Node;
import org.cloudml.core.NodeInstance;
import org.cloudml.core.Provider;
import org.cloudml.core.visitors.ContainmentVisitor;
import org.cloudml.core.visitors.Processor;
import org.cloudml.core.visitors.Visitor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;

/**
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JMock.class)
public class TestVisitor extends TestCase {

    Mockery context = new JUnit4Mockery();

    @Test
    public void testListenersAreInvoked() {
        final DeploymentModel model = prepareModel();
        final Processor processor = context.mock(Processor.class);
        Visitor visitor = new ContainmentVisitor();
        visitor.addListeners(processor);

        context.checking(new Expectations() {
            {
                oneOf(processor).processDeployment(with(any(DeploymentModel.class)));
                oneOf(processor).processProvider(with(any(Provider.class)));
                oneOf(processor).processNode(with(any(Node.class)));
                oneOf(processor).processNodeInstance(with(any(NodeInstance.class)));
                oneOf(processor).processArtefact(with(any(Artefact.class)));
                oneOf(processor).processArtefactInstance(with(any(ArtefactInstance.class)));
            }
        });

        model.accept(visitor);
    }

    private DeploymentModel prepareModel() {
        final DeploymentModel deploymentModel = new DeploymentModel();
        final Provider provider = new Provider("My provider");
        deploymentModel.getProviders().add(provider);

        final String nodeTypeName = "Node Type #1";
        Node nodeType = new Node(nodeTypeName, provider);
        deploymentModel.getNodeTypes().put(nodeTypeName, nodeType);

        final String nodeInstanceName = "Node Instance #1";
        NodeInstance nodeInstance = new NodeInstance(nodeInstanceName, nodeType);
        deploymentModel.getNodeInstances().add(nodeInstance);

        final String artefactTypeName = "Artefact Type #1";
        Artefact artefactType = new Artefact(artefactTypeName);
        deploymentModel.getArtefactTypes().put(artefactTypeName, artefactType);

        final String artefactInstanceName = "Artefact Instance #1";
        ArtefactInstance artefactInstance = new ArtefactInstance(artefactInstanceName, artefactType);
        deploymentModel.getArtefactInstances().add(artefactInstance);


        return deploymentModel;
    }
}
