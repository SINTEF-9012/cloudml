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
package test.cloudml.core.actions;

import junit.framework.TestCase;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.Node;
import org.cloudml.core.actions.NamingStrategy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import static org.cloudml.core.builders.Commons.*;
import org.cloudml.core.builders.DeploymentModelBuilder;

@RunWith(JUnit4.class)
public class NamingStrategyTest extends TestCase {

    public static final String NODE_KIND = "node";
    public static final String NODE_TYPE_NAME = "Linux";
    private final NamingStrategy naming;

    public NamingStrategyTest() {
        this.naming = new NamingStrategy();
    }

    @Test
    public void testMaxId() {
        DeploymentModel model = prepareTypes(NODE_TYPE_NAME)
                .withNodeInstance(aNodeInstance()
                .named("node instance #1")
                .ofType(NODE_TYPE_NAME))
                .withNodeInstance(aNodeInstance()
                .named("node instance #2")
                .ofType(NODE_TYPE_NAME))
                .build();

        Node type = model.getNodeTypes().named(NODE_TYPE_NAME);
        String name = naming.createUniqueNodeInstanceName(model, type);

        verifyInstanceName(name, NODE_KIND, 3);
    }

    private DeploymentModelBuilder prepareTypes(final String nodeTypeName) {
        return aDeployment()
                .withProvider(aProvider()
                .named("EC2"))
                .withNodeType(aNode()
                .named(nodeTypeName)
                .providedBy("EC2"));
    }

    private void verifyInstanceName(String instanceName, String kind, int index) {
        final String description = String.format("%s instance name", kind);;
        assertThat(description, instanceName, containsString(String.valueOf(index)));
        assertThat(description, instanceName, containsString("instance"));
        assertThat(description, instanceName, containsString(kind));
    }

    @Test
    public void testMinId() {
        DeploymentModel model = prepareTypes(NODE_TYPE_NAME)
                .build();

        Node type = model.getNodeTypes().named(NODE_TYPE_NAME);
        String name = naming.createUniqueNodeInstanceName(model, type);

        verifyInstanceName(name, NODE_KIND, 1);
    }

    @Test
    public void testReuseUnusedId() {
        DeploymentModel model = prepareTypes(NODE_TYPE_NAME)
                .withNodeInstance(aNodeInstance()
                .named("node instance #1")
                .ofType(NODE_TYPE_NAME))
                .withNodeInstance(aNodeInstance()
                .named("node instance #3")
                .ofType(NODE_TYPE_NAME))
                .build();

        Node type = model.getNodeTypes().named(NODE_TYPE_NAME);
        String name = naming.createUniqueNodeInstanceName(model, type);

        verifyInstanceName(name, NODE_KIND, 2);
    }
}