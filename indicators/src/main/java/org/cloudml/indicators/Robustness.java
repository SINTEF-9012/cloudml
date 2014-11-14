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
package org.cloudml.indicators;

import eu.diversify.trio.Trio;
import eu.diversify.trio.analysis.Analysis;
import org.cloudml.core.Deployment;

import eu.diversify.trio.core.System;
import eu.diversify.trio.data.DataSet;
import eu.diversify.trio.simulation.RandomFailureSequence;
import eu.diversify.trio.simulation.Scenario;

/**
 * Compute the robustness of a given deployment model.
 *
 * We generate here a TRIO topology, on which the robustness calculation is
 * based.
 */
public class Robustness {

    private final Analysis results;

    /**
     * Build a robustness analysis accounting only for the dependency explicitly
     * set up in the given model. Failures simulated during the analysis, will
     * only propagate along these dependencies.
     *
     * @param deployment the deployment to analyse
     * @param toObserve the subset of CloudML components whose failure are
     * observed
     * @param toControl the subset of CloudML components where failures are
     * injected
     * @return the robustness analysis
     */
    public static Robustness of(Deployment deployment, Selection toObserve, Selection toControl) {
        return new Robustness(deployment, toObserve, toControl, new OnlyExplicitDependencies());
    }

    /**
     * Build a robustness analysis accounting for self-repair. Failure will not
     * propagate along dependencies, if an existing alternative exist in the
     * model.
     *
     * @param deployment the deployment to analyse
     * @param toObserve the subset of CloudML components whose failure are
     * observed
     * @param toControl the subset of CloudML components where failures are
     * injected
     * @return the robustness analysis
     */
    public static Robustness ofSelfRepairing(Deployment deployment, Selection toObserve, Selection toControl) {
        return new Robustness(deployment, toObserve, toControl, new AllPossibleDependencies());
    }

   
    /**
     * Create a new Robustness analysis of a given CloudML model
     *
     * @param deployment the deployment model to analyse
     * @param dependencyStrategy the strategy to calculate the dependencies of
     * components
     * @param toObserve the subset of CloudML components whose failure are
     * observed
     * @param toControl the subset of CloudML components where failures are
     * injected
     */
    private Robustness(Deployment deployment, Selection toObserve, Selection toControl, DependencyExtractor dependencyStrategy) {
        requireValid(deployment);
        requireValid(toObserve, toControl);

        final System trioSystem = new TrioExporter(dependencyStrategy).asTrioSystem(deployment);
        final Trio trio = new Trio();
        final Scenario scenario = new RandomFailureSequence(trioSystem, toObserve.asTrioFilter(), toControl.asTrioFilter());
        final DataSet data = trio.run(scenario, DEFAULT_RUNCOUNT);
        results = trio.analyse(data);

        assert results != null: "robustness analysis did not produce any results!";
    }

    private void requireValid(Selection toObserve, Selection toControl) throws IllegalArgumentException {
        if (toObserve == null || toControl == null) {
            throw new IllegalArgumentException("'null' is not a valid selection");
        }
    }

    private void requireValid(Deployment deployment) throws IllegalArgumentException {
        if (deployment == null) {
            throw new IllegalArgumentException("Unable to evaluate the robustness of 'null'");
        }
    }

    private static final int DEFAULT_RUNCOUNT = 10000;

    public double value() {
        return results.metric(NORMALIZED_ROBUSTNESS).distribution().mean();
    }

    private static final String NORMALIZED_ROBUSTNESS = "norm. robustness";

}
