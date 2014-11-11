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
package test.cloudml.indicators;

import eu.diversify.trio.core.requirements.Requirement;
import eu.diversify.trio.core.requirements.stats.VariableCount;
import org.cloudml.core.ComponentInstance;
import org.cloudml.core.Deployment;
import org.cloudml.core.samples.SshClientServer;
import org.cloudml.indicators.DependencyExtractor;
import org.cloudml.indicators.OnlyExplicitDependencies;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.*;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static eu.diversify.trio.core.Evaluation.evaluate;
import static org.hamcrest.Matchers.*;

/**
 * Test the computation of the logical expression that reflects the conditions
 * under which a given component fail, when failure occurs in its direct
 * environment.
 */
@RunWith(JUnit4.class)
public class OnlyExplicitDependenciesTest {

    @Test
    public void sshClientShouldHaveOnlyTwoDependencies() {
        final Deployment deployment = SshClientServer
                .getOneClientConnectedToOneServer()
                .build();

        final ComponentInstance<?> subject = deployment
                .getComponentInstances()
                .firstNamed(SshClientServer.CLIENT_1);

        final DependencyExtractor extractRequirement = new OnlyExplicitDependencies();
        final Requirement requirement = extractRequirement.from(subject);

        final VariableCount dependencyCount = new VariableCount();
        evaluate(dependencyCount).on(requirement);

        assertThat("SSH client should have only two dependencies",
                   dependencyCount.get(),
                   is(equalTo(2)));

    }

}
