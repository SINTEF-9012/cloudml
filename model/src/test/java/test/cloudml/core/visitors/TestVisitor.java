/**
 * This file is part of CloudML [ http://cloudml.org ]
 *
 * Copyright (C) 2012 - SINTEF ICT Contact: Franck Chauvel
 * <franck.chauvel@sintef.no>
 *
 * Module: root
 *
 * CloudML is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * CloudML is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CloudML. If not, see
 * <http://www.gnu.org/licenses/>.
 */
/*
 */
package test.cloudml.core.visitors;

import junit.framework.TestCase;
import org.cloudml.core.Artefact;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.ArtefactPort;
import org.cloudml.core.Binding;
import org.cloudml.core.BindingInstance;
import org.cloudml.core.ClientPort;
import org.cloudml.core.ClientPortInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.Node;
import org.cloudml.core.NodeInstance;
import org.cloudml.core.Provider;
import org.cloudml.core.ServerPort;
import org.cloudml.core.ServerPortInstance;
import org.cloudml.core.visitors.ContainmentVisitor;
import org.cloudml.core.visitors.VisitListener;
import org.cloudml.core.visitors.Visitor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jmock.Expectations;
import static org.jmock.Expectations.any;
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

    public static final boolean REMOTE = true;
    Mockery context = new JUnit4Mockery();

    @Test
    public void testListenersAreInvoked() {
        final DeploymentModel model = prepareModel();
        final VisitListener processor = context.mock(VisitListener.class);
        Visitor visitor = new ContainmentVisitor();
        visitor.addListeners(processor);

        context.checking(new Expectations() {
            {
                oneOf(processor).onDeployment(with(any(DeploymentModel.class))); 
                oneOf(processor).onProvider(with(any(Provider.class)));
                exactly(2).of(processor).onNode(with(any(Node.class)));
                exactly(2).of(processor).onArtefact(with(any(Artefact.class)));
                oneOf(processor).onClientPort(with(any(ClientPort.class)));
                oneOf(processor).onServerPort(with(any(ServerPort.class)));
                oneOf(processor).onBinding(with(any(Binding.class)));
                exactly(2).of(processor).onNodeInstance(with(any(NodeInstance.class)));
                exactly(2).of(processor).onArtefactInstance(with(any(ArtefactInstance.class)));
                oneOf(processor).onClientPortInstance(with(any(ClientPortInstance.class)));
                oneOf(processor).onServerPortInstance(with(any(ServerPortInstance.class)));
                oneOf(processor).onBindingInstance(with(any(BindingInstance.class)));
            }

        });

        model.accept(visitor);
    }

    private DeploymentModel prepareModel() {
        final DeploymentModel deploymentModel = new DeploymentModel();
        final Provider provider = new Provider("My provider");
        deploymentModel.getProviders().add(provider);

        final String linuxTypeName = "Linux Ubuntu";
        Node linux = new Node(linuxTypeName, provider);
        deploymentModel.getNodeTypes().put(linuxTypeName, linux);

        final String windowsTypeName = "Windows";
        Node windowsType = new Node(windowsTypeName, provider);
        deploymentModel.getNodeTypes().put(windowsTypeName, windowsType);
        
        final String clientAppTypeName = "Client App Type";
        Artefact clientType = new Artefact(clientAppTypeName);
        deploymentModel.getArtefactTypes().put(clientAppTypeName, clientType);

        ClientPort clientEndPoint = new ClientPort("SSH Client", clientType, ArtefactPort.REMOTE, ClientPort.MANDATORY);
        clientType.getRequired().add(clientEndPoint);

        final String serverAppTypeName = "Server App. Type";
        Artefact serverType = new Artefact(serverAppTypeName);
        deploymentModel.getArtefactTypes().put(serverAppTypeName, serverType);

        ServerPort serverEndPoint = new ServerPort("SSH Server", serverType, ArtefactPort.REMOTE);
        serverType.getProvided().add(serverEndPoint);

        final Binding sshConnection = new Binding(clientEndPoint, serverEndPoint);
        deploymentModel.getBindingTypes().put("SSH Connection", sshConnection);

        NodeInstance windowsServer = windowsType.instanciates("Windows Server");
        deploymentModel.getNodeInstances().add(windowsServer);

        ArtefactInstance client = new ArtefactInstance("client", clientType, windowsServer);
        client.getRequired().add(new ClientPortInstance("ssh#1", clientEndPoint, client));
        deploymentModel.getArtefactInstances().add(client);
        
        NodeInstance linuxServer = linux.instanciates("Linux Server");
        deploymentModel.getNodeInstances().add(linuxServer);
        
        ArtefactInstance server = serverType.instanciates("the server", linuxServer);
        server.getProvided().add(new ServerPortInstance("ssh#1", serverEndPoint, server));
        deploymentModel.getArtefactInstances().add(server);

        BindingInstance bindingInstance = sshConnection.instanciates(client.getRequired().get(0), server.getProvided().get(0));
        deploymentModel.getBindingInstances().add(bindingInstance);
        
        return deploymentModel;
    }
}
