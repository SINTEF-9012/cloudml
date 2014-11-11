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

import eu.diversify.trio.core.Component;
import org.cloudml.core.Deployment;

import eu.diversify.trio.core.System;
import eu.diversify.trio.core.Tag;
import java.util.ArrayList;
import java.util.List;
import org.cloudml.core.ComponentInstance;

/**
 * Convert a CloudML model into a Trio System.
 *
 * The conversion is configured with the strategy to extract the conditions
 * under which a component fails when failures occurs in its environment. Two
 * strategies are possible:
 *
 * (i) only existing dependencies, which reflect the actual relationships
 * explicitly defined in the given model,
 *
 * (ii) all possible dependencies, which reflects the possibility for a
 * component to use alternative service/execution platform provider to overcome
 * failures.
 */
public class TrioExporter {

    private final DependencyExtractor extractRequirement;

    /**
     * Create a new TrioExporter, with a specific strategy for extracting the
     * requirements that characterise each CloudML component.
     *
     * @param strategy the requirement extraction strategy to use.
     */
    public TrioExporter(DependencyExtractor strategy) {
        requireValidExtractionStrategy(strategy);

        this.extractRequirement = strategy;
    }

    private void requireValidExtractionStrategy(DependencyExtractor strategy) throws IllegalArgumentException {
        if (strategy == null) {
            throw new IllegalArgumentException("'null' is not a valid requirement extraction strategy.");
        }
    }

    /**
     * Convert the given CloudML deployment model into a TRIO system
     *
     * @param model the CloudML model to be converted
     *
     * @return an equivalent Trio system
     */
    public System asTrioSystem(Deployment model) {
        requireValidDeployment(model);

        final List<String> taggedAsInternal = new ArrayList<String>();
        final List<String> taggedAsExternal = new ArrayList<String>();

        final ArrayList<Component> trioComponents = new ArrayList<Component>();
        for (ComponentInstance each: model.getComponentInstances()) {
            if (each.isExternal()) {
                taggedAsExternal.add(each.getName());
            }
            if (each.isInternal()) {
                taggedAsInternal.add(each.getName());
            }
            trioComponents.add(new Component(each.getName(), extractRequirement.from(each)));
        }

        final List<Tag> tags = new ArrayList<Tag>();
        tags.add(new Tag(INTERNAL_TAG, taggedAsInternal));
        tags.add(new Tag(EXTERNAL_TAG, taggedAsExternal));

        return new System(trioComponents, tags);
    }

    private void requireValidDeployment(Deployment model) throws IllegalArgumentException {
        if (model == null) {
            throw new IllegalArgumentException("Unable to build a TRIO system from 'null'");
        }
    }

    private static final String EXTERNAL_TAG = "external";
    private static final String INTERNAL_TAG = "internal";

}
