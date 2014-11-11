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
package test.cloudml.indicators;

import org.cloudml.core.Deployment;
import org.cloudml.core.samples.SshClientServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import eu.diversify.trio.core.System;
import org.cloudml.core.ComponentInstance;
import org.cloudml.indicators.Robustness;
import org.cloudml.indicators.OnlyExplicitDependencies;
import org.cloudml.indicators.TrioExporter;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

/**
 * Test the computation of robustness, especially the conversion from CloudML to
 * Trio models.
 *
 * We assume here that TRIO's robustness calculations are correct and we
 * therefore only test for a correct generation of a Trio model out of a CloudML
 * deployment.
 */
@RunWith(JUnit4.class)
public class RobustnessTest {
    
    
    @Test
    public void robustnessOfOneClientOneServer() {

        final Deployment cloudml = SshClientServer.getOneClientConnectedToOneServer().build();

        final Robustness robustness = new Robustness(cloudml);

        assertThat(
                robustness.value(),
                is(both(greaterThan(0D)).and(lessThan(1D))));
        

    }

    @Test
    public void convertOneClientOneServer() {
        final Deployment cloudml = SshClientServer.getOneClientConnectedToOneServer().build();

        final TrioExporter export = new TrioExporter(new OnlyExplicitDependencies());
        final System trioModel = export.asTrioSystem(cloudml);

        assertThat(trioModel, is(not(nullValue())));

        assertEquivalence(cloudml, trioModel);
        assertTags(cloudml, trioModel);
    }

    /**
     * Check that the given cloudML model and the given Trio System are related.
     * Raise an assertion error as soon as a discrepancy is detected.
     */
    private void assertEquivalence(Deployment cloudml, System trioModel) {
        assertThat("Wrong number of TRIO components",
                   trioModel.getComponentNames().size(),
                   is(equalTo(cloudml.getComponentInstances().size())));

        for (ComponentInstance each: cloudml.getComponentInstances()) {
            assertThat("missing instance '" + each.getName() + "'",
                       trioModel.hasComponentNamed(each.getName()));
        }
    }

    /**
     * Check that the proper tags were added on the TRIO model (i.e., internal,
     * versus external).
     */
    private void assertTags(Deployment cloudml, System trioModel) {
        int tagCount
                = trioModel.taggedAs("internal").size()
                + trioModel.taggedAs("external").size();

        assertThat("some components were not tagged",
                   tagCount,
                   is(equalTo(cloudml.getComponentInstances().size())));
    }

}
