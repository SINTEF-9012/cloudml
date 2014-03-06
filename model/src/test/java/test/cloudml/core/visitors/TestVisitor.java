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
import org.cloudml.core.visitors.ContainmentDispatcher;
import org.cloudml.core.visitors.VisitListener;
import org.cloudml.core.visitors.Visitor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jmock.Expectations;
import static org.jmock.Expectations.any;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import test.cloudml.core.builder.ArtefactBuilder;
import test.cloudml.core.builder.Builder;

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
        Visitor visitor = new Visitor(new ContainmentDispatcher());
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
        final Builder builder = new Builder();
        
        Provider amazon = builder.createProvider("Amazon EC2");
        
        Node linux = builder.createNodeType("Linux Ubuntu", amazon);
        Node windows = builder.createNodeType("Windows 7", amazon);
        
        ArtefactBuilder clientBuilder = builder.createArtefactType("Client Application");
        ClientPort client = clientBuilder.createClientPort("SSH client", ArtefactPort.REMOTE, ClientPort.MANDATORY);

        ArtefactBuilder serverBuilder = builder.createArtefactType("Server Application");
        ServerPort server = serverBuilder.createServerPort("SSH server", ArtefactPort.REMOTE);

        Binding sshConnection = builder.createBindingType("SSH connection", client, server);
        
        NodeInstance windowsHost = builder.provision(windows, "Windows Server");
        ArtefactInstance clientApp = builder.install(clientBuilder.getResult(), "client", windowsHost);
        
        NodeInstance linuxHost = builder.provision(linux, "Windows Server");   
        ArtefactInstance serverApp = builder.install(serverBuilder.getResult(), "server", linuxHost);
      
        builder.connect("ssh connection", sshConnection, clientApp.getRequired().get(0), serverApp.getProvided().get(0));        
        
        return builder.getResult();
    }
}
