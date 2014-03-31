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
package test.cloudml.core.builders;

import junit.framework.TestCase;
import org.cloudml.core.Artefact;
import org.cloudml.core.ArtefactInstance;
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
import static org.cloudml.core.builders.Commons.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class BuildersTest extends TestCase {

    public static final String PROVIDER_NAME = "ec2";
    public static final String ARTEFACT_NAME = "SensApp";
    public static final String CLIENT_ARTEFACT_NAME = "client";
    public static final String SERVER_ARTEFACT_NAME = "server";
    public static final String REQUIRED_PORT_NAME = "required port";
    public static final String PROVIDED_PORT_NAME = "provided port";
    public static final String NODE_TYPE_NAME = "sample node type";
    public static final String NODE_INSTANCE_NAME = "a sample node instance";
    public static final String CLIENT_ARTEFACT_INSTANCE_NAME = "a sample client artefact instance";
    public static final String SERVER_ARTEFACT_INSTANCE_NAME = "a sample server artefact instance";
    public static final String CLIENT_PORT_INSTANCE_NAME = "a sample client port instance";
    public static final String SERVER_PORT_INSTANCE_NAME = "a sample server port instance";
    public static final String BINDING_NAME = "binding";
    public static final String BINDING_INSTANCE_NAME = "a binding instance";

    // FIXME: credentials raise an exceptions
    @Test
    public void providerAreProperlyBuilt() {
        Provider provider = aProvider()
                .named(PROVIDER_NAME) //.withCredentials(credentials) 
                .build();

        assertThat("provider name", provider.getName(), is(equalTo(PROVIDER_NAME)));
    }

    @Test
    public void providersAreProperlyIntegrated() {
        final String providerName = "ec2";
        DeploymentModel model = aDeployment()
                .withProvider(aProvider()
                .named(providerName))
                .build();


        Provider provider = model.getProviders().named(providerName);

        assertThat("no provider", provider, is(not(nullValue())));
        assertThat("provider name", providerName, is(equalTo(provider.getName())));
    }

    @Test
    public void nodesAreProperlyBuilt() {
        final String nodeName = "aNode";
        final String providerName = "ec2";
        Node node = aNode()
                .named(nodeName)
                .providedBy(providerName)
                .build();

        assertThat("node name", node.getName(), is(equalTo(nodeName)));
        assertThat("node provider", node.getProvider(), is(not(nullValue())));
        assertThat("node provider name", node.getProvider().getName(), is(equalTo(providerName)));

    }

    @Test
    public void nodesAreProperlyIntegrated() {
        DeploymentModel model = aDeployment()
                .withProvider(aProvider()
                .named(PROVIDER_NAME))
                .withNodeType(aNode()
                .named(NODE_TYPE_NAME)
                .providedBy(PROVIDER_NAME))
                .build();

        Provider provider = model.getProviders().named(PROVIDER_NAME);
        Node node = model.getNodeTypes().named(NODE_TYPE_NAME);
        assertThat("no node produced", node, is(not(nullValue())));
        assertThat("node's name", node.getName(), is(equalTo(NODE_TYPE_NAME)));
        assertThat("node's provider", node.getProvider(), is(sameInstance(provider)));
    }

    @Test
    public void artefactAreProperlyBuilt() {
        Artefact artefact = anArtefact()
                .named(ARTEFACT_NAME)
                .withClientPort(aClientPort()
                .named(REQUIRED_PORT_NAME)
                .remote()
                .mandatory())
                .withServerPort(aServerPort()
                .named(PROVIDED_PORT_NAME)
                .remote())
                .build();

        assertThat("no artefact", artefact, is(not(nullValue())));
        assertThat("artefact name",
                   artefact.getName(),
                   is(equalTo(ARTEFACT_NAME)));
        final ClientPort client = artefact.findRequiredPortByName(REQUIRED_PORT_NAME);
        assertThat("no client port", client,
                   is(not(nullValue())));
        assertThat("client reflexive association",
                   client.getOwner(),
                   is(sameInstance(artefact)));
        final ServerPort server = artefact.findProvidedPortByName(PROVIDED_PORT_NAME);
        assertThat("no server port", server, is(not(nullValue())));
        assertThat("server port containment",
                   server.getOwner(),
                   is(sameInstance(artefact)));
    }

    @Test
    public void clientPortsAreBuiltProperly() {
        ClientPort client = aClientPort()
                .named(REQUIRED_PORT_NAME)
                .local()
                .optional()
                .build();

        assertNotNull("no client port", client);
        assertEquals("client port name", REQUIRED_PORT_NAME,
                     client.getName());
        assertTrue("is remote", client.isLocal());
        assertTrue("is optinal", client.isOptional());
    }

    @Test
    public void serverPortAreBuiltProperly() {
        ServerPort server = aServerPort()
                .named("server1")
                .remote()
                .build();

        assertNotNull("no server port", server);
        assertEquals("server port name", "server1", server.getName());
        assertTrue("is remote", server.isRemote());
    }

    @Test
    public void artefactsAreProperlyIntegrated() {
        DeploymentModel model = aDeployment()
                .withArtefact(anArtefact()
                .named(CLIENT_ARTEFACT_NAME)
                .withClientPort(aClientPort()
                .named(REQUIRED_PORT_NAME))
                .withServerPort(aServerPort()
                .named(PROVIDED_PORT_NAME)))
                .build();


        Artefact artefact = model.
                getArtefactTypes().named(CLIENT_ARTEFACT_NAME);
        assertThat("no artefact",
                   artefact,
                   is(not(nullValue())));
        assertThat("artefact name",
                   artefact.getName(),
                   is(equalTo(CLIENT_ARTEFACT_NAME)));

        ClientPort client = artefact.findRequiredPortByName(REQUIRED_PORT_NAME);
        assertThat("no client port",
                   client, is(not(nullValue())));
        assertThat("client reflexive association",
                   client.getOwner(),
                   is(sameInstance(artefact)));

        ServerPort server = artefact.findProvidedPortByName(PROVIDED_PORT_NAME);
        assertThat("no server port",
                   server, is(not(nullValue())));
        assertThat("server port containment",
                   server.getOwner(),
                   is(sameInstance(artefact)));

    }

    @Test
    public void bindingsAreProperlyBuilt() {
        Binding binding = aBinding()
                .named(BINDING_NAME)
                .from(CLIENT_ARTEFACT_NAME, REQUIRED_PORT_NAME)
                .to(SERVER_ARTEFACT_NAME, PROVIDED_PORT_NAME)
                .build();

        assertNotNull("no binding", binding);
        assertEquals("binding name", BINDING_NAME, binding.getName());
        assertEquals("client end name", REQUIRED_PORT_NAME,
                     binding.getClient()
                .getName());
        assertEquals("server end name", PROVIDED_PORT_NAME,
                     binding.getServer()
                .getName());

    }

    @Test
    public void bindingsAreProperlyIntegrated() {
        DeploymentModel model = aDeployment()
                .withArtefact(anArtefact()
                .named(CLIENT_ARTEFACT_NAME)
                .withClientPort(aClientPort()
                .named(REQUIRED_PORT_NAME)))
                .withArtefact(anArtefact()
                .named(SERVER_ARTEFACT_NAME)
                .withServerPort(aServerPort()
                .named(PROVIDED_PORT_NAME)))
                .withBinding(aBinding()
                .named(BINDING_NAME)
                .from(CLIENT_ARTEFACT_NAME, REQUIRED_PORT_NAME)
                .to(SERVER_ARTEFACT_NAME, PROVIDED_PORT_NAME))
                .build();

        Binding binding = model.getBindingTypes().named(BINDING_NAME);

        assertThat("no binding", binding, is(not(nullValue())));
        assertThat("binding name", binding.getName(), is(equalTo(BINDING_NAME)));

        ClientPort client = model
                .getArtefactTypes().named(CLIENT_ARTEFACT_NAME)
                .findRequiredPortByName(REQUIRED_PORT_NAME);
        assertThat("no client", client, is(not(nullValue())));
        assertThat("client end", binding.getClient(), is(sameInstance(client)));

        ServerPort server = model
                .getArtefactTypes().named(SERVER_ARTEFACT_NAME)
                .findProvidedPortByName(PROVIDED_PORT_NAME);
        assertThat("no server", server, is(not(nullValue())));
        assertThat("server end", binding.getServer(), is(sameInstance(server)));
    }

    @Test
    public void nodeInstancesAreProperlyBuilt() {
        NodeInstance instance = aNodeInstance()
                .ofType(NODE_TYPE_NAME)
                .named(NODE_INSTANCE_NAME)
                .build();

        assertNotNull("no node instance", instance);
        assertEquals("node instance name", NODE_INSTANCE_NAME, instance
                .getName());
        assertEquals("node intstance type name", NODE_TYPE_NAME, instance
                .getType()
                .getName());
    }

    @Test
    public void nodeInstancesAreProperlyBuiltWithinAModel() {
        DeploymentModel model = aDeployment()
                .withProvider(aProvider()
                .named(PROVIDER_NAME))
                .withNodeType(aNode()
                .named(NODE_TYPE_NAME)
                .providedBy(PROVIDER_NAME))
                .withNodeInstance(aNodeInstance()
                .ofType(NODE_TYPE_NAME)
                .named(NODE_INSTANCE_NAME))
                .build();

        NodeInstance instance = model
                .getNodeInstances().named(NODE_INSTANCE_NAME);

        assertNotNull("no node instance", instance);
        assertEquals("node instance name", NODE_INSTANCE_NAME, instance
                .getName());
        assertNotNull("node type", model.getNodeTypes().named(NODE_TYPE_NAME));
        assertEquals("node intstance type name", NODE_TYPE_NAME, instance
                .getType()
                .getName());
    }

    @Test
    public void artefactInstancesAreProperlyBuilt() {
        ArtefactInstance instance = anArtefactInstance()
                .ofType(CLIENT_ARTEFACT_NAME)
                .named(CLIENT_ARTEFACT_INSTANCE_NAME)
                .hostedBy(NODE_INSTANCE_NAME)
                .build();

        assertNotNull("artefact instance", instance);
        assertEquals("name of artefact instance", CLIENT_ARTEFACT_INSTANCE_NAME, instance
                .getName());
        assertNotNull("artefact type", instance.getType());
        assertEquals("name of artefact instance type", CLIENT_ARTEFACT_NAME, instance
                .getType()
                .getName());
        assertNotNull("host", instance.getDestination());
        assertEquals("host name", NODE_INSTANCE_NAME, instance
                .getDestination()
                .getName());
    }

    @Test
    public void artefactInstancesAreProperlyIntegrated() {
        DeploymentModel model = aDeployment()
                .withProvider(aProvider().named(PROVIDER_NAME))
                .withNodeType(aNode()
                    .named(NODE_TYPE_NAME)
                    .providedBy(PROVIDER_NAME))
                .withArtefact(anArtefact()
                    .named(CLIENT_ARTEFACT_NAME)
                    .withClientPort(aClientPort()
                        .named(REQUIRED_PORT_NAME)
                        .remote())
                    .withServerPort(aServerPort()
                        .named(PROVIDED_PORT_NAME)
                        .remote()))
                .withNodeInstance(aNodeInstance()
                    .named(NODE_INSTANCE_NAME)
                    .ofType(NODE_TYPE_NAME))
                .withArtefactInstance(anArtefactInstance()
                    .named(CLIENT_ARTEFACT_INSTANCE_NAME)
                    .ofType(CLIENT_ARTEFACT_NAME)
                    .hostedBy(NODE_INSTANCE_NAME))
                .build();

        ArtefactInstance instance = model.getArtefactInstances().named(CLIENT_ARTEFACT_INSTANCE_NAME);
        assertThat("artefact instance", instance, is(not(nullValue())));

        Artefact type = model.getArtefactTypes().named(CLIENT_ARTEFACT_NAME);
        assertThat("client's type", instance.getType(), is(sameInstance(type)));
        
        ClientPortInstance clientPort = instance.findRequiredPortByName(REQUIRED_PORT_NAME);
        assertThat("client port", clientPort, is(not(nullValue())));
        
        ClientPort clientPortType = type.findRequiredPortByName(REQUIRED_PORT_NAME);
        assertThat("client port type", clientPort.getType(), is(sameInstance(clientPortType)));
        
        ServerPortInstance serverPort = instance.findProvidedPortByName(PROVIDED_PORT_NAME);
        assertThat("server port", serverPort, is(not(nullValue())));
        
        ServerPort serverPortType = type.findProvidedPortByName(PROVIDED_PORT_NAME);
        assertThat("server port type", serverPort.getType(), is(sameInstance(serverPortType)));
        
        NodeInstance destination = model.getNodeInstances().named(NODE_INSTANCE_NAME);
        assertThat("artefact destination", instance.getDestination(), is(sameInstance(destination)));
    }

    @Test
    public void testClientPortInstanceAreProperlyBuilt() {
        ClientPortInstance instance = aClientPortInstance()
                .withName(CLIENT_PORT_INSTANCE_NAME)
                .ownedBy(CLIENT_ARTEFACT_INSTANCE_NAME)
                .ofType(REQUIRED_PORT_NAME)
                .build();

        assertThat("port instance", instance, is(not(nullValue())));
        assertThat("port instance name", instance.getName(), is(equalTo(CLIENT_PORT_INSTANCE_NAME)));
        assertThat("port type", instance.getType(), is(not(nullValue())));
        assertThat("port type name", instance.getType().getName(), is(equalTo(REQUIRED_PORT_NAME)));
        assertThat("port owner", instance.getOwner(), is(not(nullValue())));
        assertThat("port owner name", instance.getOwner().getName(), is(equalTo(CLIENT_ARTEFACT_INSTANCE_NAME)));
        assertThat("port owner containment", instance.getOwner().getRequired(), hasItem(equalTo(instance)));
    }

    @Test
    public void testServerPortInstanceAreProperlyBuilt() {
        ServerPortInstance instance = aServerPortInstance()
                .withName(SERVER_PORT_INSTANCE_NAME)
                .ownedBy(CLIENT_ARTEFACT_INSTANCE_NAME)
                .ofType(PROVIDED_PORT_NAME)
                .build();

        assertThat("port instance", instance, is(not(nullValue())));
        assertThat("port instance name", instance.getName(), is(equalTo(SERVER_PORT_INSTANCE_NAME)));
        assertThat("port type", instance.getType(), is(not(nullValue())));
        assertThat("port type name", instance.getType().getName(), is(equalTo(PROVIDED_PORT_NAME)));
        assertThat("port owner", instance.getOwner(), is(not(nullValue())));
        assertThat("port owner name", instance.getOwner().getName(), is(equalTo(CLIENT_ARTEFACT_INSTANCE_NAME)));
        assertThat("port owner containment", instance.getOwner().getProvided(), hasItem(equalTo(instance)));
    }

    @Test
    public void bindingInstanceAreProperlyBuilt() {
        BindingInstance instance = aBindingInstance()
                .named(BINDING_INSTANCE_NAME)
                .ofType(BINDING_NAME)
                .from("my instance of client", "client port")
                .to("my instance of server", "server port")
                .build();

        assertThat("binding instance", instance, is(not(nullValue())));
        assertThat("binding's name", instance.getName(), is(equalTo(BINDING_INSTANCE_NAME)));
        assertThat("binding's type", instance.getType(), is(not(nullValue())));
        assertThat("binding type's name", instance.getType().getName(), is(equalTo(BINDING_NAME)));
        assertThat("client end point", instance.getClient(), is(not(nullValue())));
        assertThat("server end point", instance.getServer(), is(not(nullValue())));
    }

    @Test
    public void bindingInstancesAreProperlyIntegrated() {
        DeploymentModel model = aDeployment()
                .withProvider(aProvider().named(PROVIDER_NAME))
                .withNodeType(aNode()
                    .named(NODE_TYPE_NAME)
                    .providedBy(PROVIDER_NAME))
                .withArtefact(anArtefact()
                    .named(CLIENT_ARTEFACT_NAME)
                    .withClientPort(aClientPort()
                        .named(REQUIRED_PORT_NAME)
                        .remote()))
                .withArtefact(anArtefact()
                    .named(SERVER_ARTEFACT_NAME)
                    .withServerPort(aServerPort()
                        .named(PROVIDED_PORT_NAME)
                        .remote()))
                .withBinding(aBinding()
                    .named(BINDING_NAME)
                    .from(CLIENT_ARTEFACT_NAME, REQUIRED_PORT_NAME)
                    .to(SERVER_ARTEFACT_NAME, PROVIDED_PORT_NAME))
                .withNodeInstance(aNodeInstance()
                    .named("host no. 1")
                    .ofType(NODE_TYPE_NAME))
                .withNodeInstance(aNodeInstance()
                    .named("host no. 2")
                    .ofType(NODE_TYPE_NAME))
                .withArtefactInstance(anArtefactInstance()
                    .named(CLIENT_ARTEFACT_INSTANCE_NAME)
                    .ofType(CLIENT_ARTEFACT_NAME)
                    .hostedBy("host no. 1"))
                .withArtefactInstance(anArtefactInstance()
                    .named(SERVER_ARTEFACT_INSTANCE_NAME)
                    .ofType(SERVER_ARTEFACT_NAME)
                    .hostedBy("host no. 2"))
                .withBindingInstance(aBindingInstance()
                    .ofType(BINDING_NAME)
                    .named(BINDING_INSTANCE_NAME)
                    .from(CLIENT_ARTEFACT_INSTANCE_NAME, REQUIRED_PORT_NAME)
                    .to(SERVER_ARTEFACT_INSTANCE_NAME, PROVIDED_PORT_NAME))
                .build();

        BindingInstance instance = model.getBindingInstances().named(BINDING_INSTANCE_NAME);
        assertThat("binding instance", instance, is(not(nullValue())));

        ArtefactInstance client = model.getArtefactInstances().named(CLIENT_ARTEFACT_INSTANCE_NAME);
        ArtefactInstance server = model.getArtefactInstances().named(SERVER_ARTEFACT_INSTANCE_NAME);
                
        Binding bindingType = model.getBindingTypes().named(BINDING_NAME);
        assertThat("binding's type", instance.getType(), is(sameInstance(bindingType)));
        
        ClientPortInstance clientEnd = client.findRequiredPortByName(REQUIRED_PORT_NAME);
        assertThat("client's end", instance.getClient(), is(sameInstance(clientEnd)));
        
        ServerPortInstance serverEnd = server.findProvidedPortByName(PROVIDED_PORT_NAME);
        assertThat("server's end", instance.getServer(), is(sameInstance(serverEnd)));
        
    }
}
