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
import static junit.framework.TestCase.assertTrue;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.Node;
import org.cloudml.core.Provider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.cloudml.core.builders.Commons.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;


@RunWith(JUnit4.class)
public class ProviderTest extends TestCase {

    public static final boolean WITHOUT_WARNING = false;

    @Test
    public void passValidationWithoutWarningsWhenValid() {
        Provider provider = new Provider("Amazon EC2", "src/test/resources/credentials.txt");
        assertTrue(provider.validate().pass(WITHOUT_WARNING));
    }

    @Test
    public void detectMissingCredentials() {
        Provider provider = new Provider("Amazon EC2", "a_file_which_is_not_on_disk.properties");
        assertTrue(provider.validate().hasWarningAbout("credentials"));
    }
    
    @Test
    public void testBidirectionalAssocationWithDeployment() {
        final DeploymentModel deployment = aDeployment()
                .withProvider(aProvider().named("EC2"))
                .withNodeType(aNode()
                    .named("Linux")
                    .providedBy("EC2"))
                .build();
        
        final Provider ec2 = deployment.getProviders().named("EC2");
        assertThat("contained in model", ec2, is(not(nullValue()))); 
        assertThat("provided node types", ec2.providedNodeTypes().size(), is(equalTo(1)));
        
        final Node linux = deployment.getNodeTypes().named("Linux");
        deployment.getNodeTypes().remove(linux);
        deployment.getProviders().remove(ec2);
        assertThat("attached to model", !ec2.isAttachedToADeployment()); 
        assertThat("contained in model", deployment.getProviders().named("EC2"), is(nullValue()));
    }

}
