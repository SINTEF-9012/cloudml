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
import static junit.framework.TestCase.assertEquals;
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

    public static final String SAMPLE_ARTEFACT_NAME = "SensApp";
    public static final String SAMPLE_CLIENT_ARTEFACT_NAME = "client";
    public static final String SAMPLE_SERVER_ARTEFACT_NAME = "server";
    public static final String SAMPLE_BINDING_NAME = "binding";
    public static final String SAMPLE_PROVIDER_NAME = "ec2";
    public static final String SAMPLE_REQUIRED_PORT_NAME = "required port";
    public static final String SAMPLE_PROVIDED_PORT_NAME = "provided port";
    public static final String SAMPLE_NODE_TYPE_NAME = "sample node type";
    public static final String SAMPLE_NODE_INSTANCE_NAME = "a sample node instance";
    public static final String SAMPLE_ARTEFACT_INSTANCE_NAME = "a sample artefact instance";
    public static final String SAMPLE_BINDING_INSTANCE_NAME = "a binding instance";
    public static final String SAMPLE_CLIENT_PORT_INSTANCE_NAME = "a sample client port instance";
    public static final String SAMPLE_SERVER_PORT_INSTANCE_NAME = "a sample server port instance";

    // FIXME: credentials raise an exceptions
    @Test
    public void providerAreProperlyBuilt() {
        Provider provider = aProvider()
                .named(SAMPLE_PROVIDER_NAME) //.withCredentials(credentials) 
                .build();

        assertThat("provider name", provider.getName(), is(equalTo(SAMPLE_PROVIDER_NAME)));
    }

    @Test
    public void providersAreProperlyIntegrated() {
        final String providerName = "ec2";
        DeploymentModel model = aDeployment()
                .withProvider(aProvider()
                .named(providerName))
                .build();


        Provider provider = model.findProviderByName(providerName);

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
                .named(SAMPLE_PROVIDER_NAME))
                .withNodeType(aNode()
                .named(SAMPLE_NODE_TYPE_NAME)
                .providedBy(SAMPLE_PROVIDER_NAME))
                .build();

        Provider provider = model.findProviderByName(SAMPLE_PROVIDER_NAME);
        Node node = model.findNodeByName(SAMPLE_NODE_TYPE_NAME);
        assertThat("no node produced", node, is(not(nullValue())));
        assertThat("node's name", node.getName(), is(equalTo(SAMPLE_NODE_TYPE_NAME)));
        assertThat("node's provider", node.getProvider(), is(sameInstance(provider)));
    }

    @Test
    public void artefactAreProperlyBuilt() {
        Artefact artefact = anArtefact()
                .named(SAMPLE_ARTEFACT_NAME)
                .withClientPort(aClientPort()
                .named(SAMPLE_REQUIRED_PORT_NAME)
                .remote()
                .mandatory())
                .withServerPort(aServerPort()
                .named(SAMPLE_PROVIDED_PORT_NAME)
                .remote())
                .build();

        assertThat("no artefact", artefact, is(not(nullValue())));
        assertThat("artefact name",
                   artefact.getName(),
                   is(equalTo(SAMPLE_ARTEFACT_NAME)));
        final ClientPort client = artefact.findRequiredPortByName(SAMPLE_REQUIRED_PORT_NAME);
        assertThat("no client port", client,
                   is(not(nullValue())));
        assertThat("client reflexive association",
                   client.getOwner(),
                   is(sameInstance(artefact)));
        final ServerPort server = artefact.findProvidedPortByName(SAMPLE_PROVIDED_PORT_NAME);
        assertThat("no server port", server, is(not(nullValue())));
        assertThat("server port containment",
                   server.getOwner(),
                   is(sameInstance(artefact)));
    }

    @Test
    public void clientPortsAreBuiltProperly() {
        ClientPort client = aClientPort()
                .named(SAMPLE_REQUIRED_PORT_NAME)
                .local()
                .optional()
                .build();

        assertNotNull("no client port", client);
        assertEquals("client port name", SAMPLE_REQUIRED_PORT_NAME,
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
                .named(SAMPLE_CLIENT_ARTEFACT_NAME)
                .withClientPort(aClientPort()
                .named(SAMPLE_REQUIRED_PORT_NAME))
                .withServerPort(aServerPort()
                .named(SAMPLE_PROVIDED_PORT_NAME)))
                .build();


        Artefact artefact = model.
                findArtefactByName(SAMPLE_CLIENT_ARTEFACT_NAME);
        assertThat("no artefact",
                   artefact,
                   is(not(nullValue())));
        assertThat("artefact name",
                   artefact.getName(),
                   is(equalTo(SAMPLE_CLIENT_ARTEFACT_NAME)));

        ClientPort client = artefact.findRequiredPortByName(SAMPLE_REQUIRED_PORT_NAME);
        assertThat("no client port",
                   client, is(not(nullValue())));
        assertThat("client reflexive association",
                   client.getOwner(),
                   is(sameInstance(artefact)));

        ServerPort server = artefact.findProvidedPortByName(SAMPLE_PROVIDED_PORT_NAME);
        assertThat("no server port",
                   server, is(not(nullValue())));
        assertThat("server port containment",
                   server.getOwner(),
                   is(sameInstance(artefact)));

    }

    @Test
    public void bindingsAreProperlyBuilt() {
        Binding binding = aBinding()
                .named(SAMPLE_BINDING_NAME)
                .from(SAMPLE_CLIENT_ARTEFACT_NAME, SAMPLE_REQUIRED_PORT_NAME)
                .to(SAMPLE_SERVER_ARTEFACT_NAME, SAMPLE_PROVIDED_PORT_NAME)
                .build();

        assertNotNull("no binding", binding);
        assertEquals("binding name", SAMPLE_BINDING_NAME, binding.getName());
        assertEquals("client end name", SAMPLE_REQUIRED_PORT_NAME,
                     binding.getClient()
                .getName());
        assertEquals("server end name", SAMPLE_PROVIDED_PORT_NAME,
                     binding.getServer()
                .getName());

    }

    @Test
    public void bindingsAreProperlyIntegrated() {
        DeploymentModel model = aDeployment()
                .withArtefact(anArtefact()
                    .named(SAMPLE_CLIENT_ARTEFACT_NAME)
                    .withClientPort(aClientPort()
                        .named(SAMPLE_REQUIRED_PORT_NAME)))
                .withArtefact(anArtefact()
                    .named(SAMPLE_SERVER_ARTEFACT_NAME)
                    .withServerPort(aServerPort()
                        .named(SAMPLE_PROVIDED_PORT_NAME)))
                .withBinding(aBinding()
                    .named(SAMPLE_BINDING_NAME)
                    .from(SAMPLE_CLIENT_ARTEFACT_NAME, SAMPLE_REQUIRED_PORT_NAME)
                    .to(SAMPLE_SERVER_ARTEFACT_NAME, SAMPLE_PROVIDED_PORT_NAME))
                .build();

        Binding binding = model.findBindingByName(SAMPLE_BINDING_NAME);

        assertThat("no binding", binding, is(not(nullValue())));
        assertThat("binding name", binding.getName(), is(equalTo(SAMPLE_BINDING_NAME)));
        
        ClientPort client = model
                .findArtefactByName(SAMPLE_CLIENT_ARTEFACT_NAME)
                .findRequiredPortByName(SAMPLE_REQUIRED_PORT_NAME);
        assertThat("no client", client, is(not(nullValue())));
        assertThat("client end", binding.getClient(), is(sameInstance(client)));

        ServerPort server = model
                .findArtefactByName(SAMPLE_SERVER_ARTEFACT_NAME)
                .findProvidedPortByName(SAMPLE_PROVIDED_PORT_NAME);
        assertThat("no server", server, is(not(nullValue())));
        assertThat("server end", binding.getServer(), is(sameInstance(server)));
    }

    @Test
    public void nodeInstancesAreProperlyBuilt() {
        NodeInstance instance = aNodeInstance()
                .ofType(SAMPLE_NODE_TYPE_NAME)
                .named(SAMPLE_NODE_INSTANCE_NAME)
                .build();

        assertNotNull("no node instance", instance);
        assertEquals("node instance name", SAMPLE_NODE_INSTANCE_NAME, instance
                .getName());
        assertEquals("node intstance type name", SAMPLE_NODE_TYPE_NAME, instance
                .getType()
                .getName());
    }

    @Test
    public void nodeInstancesAreProperlyBuiltWithinAModel() {
        DeploymentModel model = aDeployment()
                .withProvider(aProvider()
                .named(SAMPLE_PROVIDER_NAME))
                .withNodeType(aNode()
                .named(SAMPLE_NODE_TYPE_NAME)
                .providedBy(SAMPLE_PROVIDER_NAME))
                .withNodeInstance(aNodeInstance()
                .ofType(SAMPLE_NODE_TYPE_NAME)
                .named(SAMPLE_NODE_INSTANCE_NAME))
                .build();

        NodeInstance instance = model
                .findNodeInstanceByName(SAMPLE_NODE_INSTANCE_NAME);

        assertNotNull("no node instance", instance);
        assertEquals("node instance name", SAMPLE_NODE_INSTANCE_NAME, instance
                .getName());
        assertNotNull("node type", model.findNodeByName(SAMPLE_NODE_TYPE_NAME));
        assertEquals("node intstance type name", SAMPLE_NODE_TYPE_NAME, instance
                .getType()
                .getName());
    }

    @Test
    public void artefactInstancesAreProperlyBuilt() {
        ArtefactInstance instance = anArtefactInstance()
                .ofType(SAMPLE_CLIENT_ARTEFACT_NAME)
                .named(SAMPLE_ARTEFACT_INSTANCE_NAME)
                .hostedBy(SAMPLE_NODE_INSTANCE_NAME)
                .build();

        assertNotNull("artefact instance", instance);
        assertEquals("name of artefact instance", SAMPLE_ARTEFACT_INSTANCE_NAME, instance
                .getName());
        assertNotNull("artefact type", instance.getType());
        assertEquals("name of artefact instance type", SAMPLE_CLIENT_ARTEFACT_NAME, instance
                .getType()
                .getName());
        assertNotNull("host", instance.getDestination());
        assertEquals("host name", SAMPLE_NODE_INSTANCE_NAME, instance
                .getDestination()
                .getName());
    }

    @Test
    public void artefactInstancesAreProperlyIntegrated() {
        DeploymentModel model = aDeployment()
                .withProvider(aProvider().named(SAMPLE_PROVIDER_NAME))
                .withNodeType(aNode()
                .named(SAMPLE_NODE_TYPE_NAME)
                .providedBy(SAMPLE_PROVIDER_NAME))
                .withArtefact(anArtefact()
                .named(SAMPLE_ARTEFACT_NAME)
                .withClientPort(aClientPort()
                .named(SAMPLE_REQUIRED_PORT_NAME)
                .remote())
                .withServerPort(aServerPort()
                .named(SAMPLE_PROVIDED_PORT_NAME)
                .remote()))
                .withNodeInstance(aNodeInstance()
                .named(SAMPLE_NODE_INSTANCE_NAME)
                .ofType(SAMPLE_NODE_TYPE_NAME))
                .withArtefactInstance(anArtefactInstance()
                .named(SAMPLE_ARTEFACT_INSTANCE_NAME)
                .ofType(SAMPLE_ARTEFACT_NAME)
                .hostedBy(SAMPLE_NODE_INSTANCE_NAME))
                .build();

        ArtefactInstance instance = model.findArtefactInstanceByName(SAMPLE_ARTEFACT_INSTANCE_NAME);

        assertThat("artefact instance", instance, is(not(nullValue())));

        Artefact type = model.findArtefactByName(SAMPLE_ARTEFACT_NAME);
        assertThat("artefact instance type", instance.getType(), is(sameInstance(type)));

        NodeInstance destination = model.findNodeInstanceByName(SAMPLE_NODE_INSTANCE_NAME);
        assertThat("artefact destination", instance.getDestination(), is(sameInstance(destination)));
    }

    @Test
    public void testClientPortInstanceAreProperlyBuilt() {
        ClientPortInstance instance = aClientPortInstance()
                .withName(SAMPLE_CLIENT_PORT_INSTANCE_NAME)
                .ownedBy(SAMPLE_ARTEFACT_INSTANCE_NAME)
                .ofType(SAMPLE_REQUIRED_PORT_NAME)
                .build();

        assertThat("port instance", instance, is(not(nullValue())));
        assertThat("port instance name", instance.getName(), is(equalTo(SAMPLE_CLIENT_PORT_INSTANCE_NAME)));
        assertThat("port type", instance.getType(), is(not(nullValue())));
        assertThat("port type name", instance.getType().getName(), is(equalTo(SAMPLE_REQUIRED_PORT_NAME)));
        assertThat("port owner", instance.getOwner(), is(not(nullValue())));
        assertThat("port owner name", instance.getOwner().getName(), is(equalTo(SAMPLE_ARTEFACT_INSTANCE_NAME)));
        assertThat("port owner containment", instance.getOwner().getRequired(), hasItem(equalTo(instance)));
    }

    @Test
    public void testServerPortInstanceAreProperlyBuilt() {
        ServerPortInstance instance = aServerPortInstance()
                .withName(SAMPLE_SERVER_PORT_INSTANCE_NAME)
                .ownedBy(SAMPLE_ARTEFACT_INSTANCE_NAME)
                .ofType(SAMPLE_PROVIDED_PORT_NAME)
                .build();

        assertThat("port instance", instance, is(not(nullValue())));
        assertThat("port instance name", instance.getName(), is(equalTo(SAMPLE_SERVER_PORT_INSTANCE_NAME)));
        assertThat("port type", instance.getType(), is(not(nullValue())));
        assertThat("port type name", instance.getType().getName(), is(equalTo(SAMPLE_PROVIDED_PORT_NAME)));
        assertThat("port owner", instance.getOwner(), is(not(nullValue())));
        assertThat("port owner name", instance.getOwner().getName(), is(equalTo(SAMPLE_ARTEFACT_INSTANCE_NAME)));
        assertThat("port owner containment", instance.getOwner().getProvided(), hasItem(equalTo(instance)));
    }

    @Test
    public void bindingInstanceAreProperlyBuilt() {
        BindingInstance instance = aBindingInstance()
                .named(SAMPLE_BINDING_INSTANCE_NAME)
                .ofType(SAMPLE_BINDING_NAME)
                .from("my instance of client", "client port")
                .to("my instance of server", "server port")
                .build();

        assertThat("no binding instance", instance, is(not(nullValue())));
        assertThat("binding's name", instance.getName(), is(equalTo(SAMPLE_BINDING_INSTANCE_NAME)));
        assertThat("binding's type", instance.getType(), is(not(nullValue())));
        assertThat("binding type's name", instance.getType().getName(), is(equalTo(SAMPLE_BINDING_NAME)));
        assertThat("client end point", instance.getClient(), is(not(nullValue())));
        assertThat("server end point", instance.getServer(), is(not(nullValue())));
    }

    @Test
    public void testReferenceCreation() {
        DeploymentModel model = aDeployment()
                .withProvider(aProvider()
                .named("ec2"))
                .withNodeType(aNode()
                .named("Linux")
                .providedBy("ec2"))
                .build();

        assertNotNull(model.findProviderByName("ec2"));
        assertNotNull(model.findNodeByName("Linux"));
    }
}
