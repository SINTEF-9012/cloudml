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
package org.cloudml.indicators;

import eu.diversify.trio.Trio;
import eu.diversify.trio.analysis.Analysis;
import org.cloudml.core.Deployment;

import eu.diversify.trio.core.System;
import eu.diversify.trio.data.DataSet;
import eu.diversify.trio.filter.TaggedAs;
import eu.diversify.trio.simulation.RandomFailureSequence;
import eu.diversify.trio.simulation.Scenario;

/**
 * Compute the robustness of a given deployment model.
 *
 * We generate here a TRIO topology, on which the robustness calculation is
 * based.
 */
public class Robustness {

    private final Deployment deployment;
    private final Analysis results;

    public Robustness(Deployment deployment) {
        requireValidDeployment(deployment);

        this.deployment = deployment;
        final System trioSystem = new TrioExporter(new OnlyExplicitDependencies()).asTrioSystem(deployment);
        final Trio trio = new Trio();
        final Scenario scenario = new RandomFailureSequence(trioSystem, new TaggedAs("internal"), new TaggedAs("external"));
        final DataSet data = trio.run(scenario, DEFAULT_RUNCOUNT);
        results = trio.analyse(data);
        
        assert results != null;
    }

    private void requireValidDeployment(Deployment deployment1) throws IllegalArgumentException {
        if (deployment1 == null) {
            throw new IllegalArgumentException("Unable to evaluate the robustness of 'null'");
        }
    }
    
    private static final int DEFAULT_RUNCOUNT = 10000;

    public double value() {
        return results.metric("norm. robustness").distribution().mean();
    }

}
