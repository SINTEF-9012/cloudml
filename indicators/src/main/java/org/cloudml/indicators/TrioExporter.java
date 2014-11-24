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
import java.util.*;
import org.cloudml.core.ComponentInstance;
import org.cloudml.core.Property;

import static org.cloudml.indicators.Selection.*;

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
 
        final Map<String, List<String>> tagged = prepareTagMap();

        final ArrayList<Component> trioComponents = new ArrayList<Component>();
        for (ComponentInstance each: model.getComponentInstances()) {
            extractTags(each, tagged);
            trioComponents.add(new Component(each.getName(), extractRequirement.from(each)));
        }

        return new System(trioComponents, buildTags(tagged));
    }

    /**
     * Abort if the given deployment model is not valid
     */
    private void requireValidDeployment(Deployment model) throws IllegalArgumentException {
        if (model == null) {
            throw new IllegalArgumentException("Unable to build a TRIO system from 'null'");
        }
    }

    /**
     * Extract the tag that can be identified on the given component instance.
     *
     * @param componentInstance the component whose tags are needed
     * @param tagged the already initialised maps of tags, to be filled in
     */
    private void extractTags(ComponentInstance componentInstance, Map<String, List<String>> tagged) {
        assert componentInstance != null: "Unable to extract tags from 'null'";
        assert !tagged.isEmpty(): "The map of tag must have been initialized";

        if (componentInstance.isExternal()) {
            tagged.get(EXTERNAL.getLabel()).add(componentInstance.getName());
            if (componentInstance.asExternal().isVM()) {
                tagged.get(VM.getLabel()).add(componentInstance.getName());
            }
        }
        if (componentInstance.isInternal()) {
            tagged.get(INTERNAL.getLabel()).add(componentInstance.getName());
        }
        if (isService(componentInstance)) {
            tagged.get(SERVICE.getLabel()).add(componentInstance.getName());
        } else {
            tagged.get(NOT_SERVICE.getLabel()).add(componentInstance.getName());
        }

    }

    /**
     * Check whether the given component is a marked as being a "service".
     *
     * @param instance the component instance of interest
     * @return true if the instance is marked as being a service, false
     * otherwise
     */
    private boolean isService(ComponentInstance instance) {
        assert instance != null: "Cannot check if 'null' is a service ";
        assert instance.getType() != null: "The given component has no type!";

        if (instance.getType().hasProperty(IS_SERVICE)) {
            final Property isService = instance.getType().getProperties().get(IS_SERVICE);
            final String escapedValue = isService.getValue().trim().toUpperCase();
            if (YES_MARKERS.contains(escapedValue)) {
                return true;
            }
        }

        return false;
    }

    private static final List<String> YES_MARKERS
            = Arrays.asList(new String[]{
                "YES", 
                "TRUE", 
                "OK"}
            );

    public static final String IS_SERVICE = "is_service";

    /**
     * @return the map where each tag is associated with an empty collection
     */
    private Map<String, List<String>> prepareTagMap() {
        final Map<String, List<String>> tagged = new HashMap<String, List<String>>();
        tagged.put(INTERNAL.getLabel(), new ArrayList<String>());
        tagged.put(EXTERNAL.getLabel(), new ArrayList<String>());
        tagged.put(VM.getLabel(), new ArrayList<String>());
        tagged.put(SERVICE.getLabel(), new ArrayList<String>());
        tagged.put(NOT_SERVICE.getLabel(), new ArrayList<String>());

        assert tagged.size() == Selection.values().length - 1:
                "Error some tags are properly initialized!";

        return tagged;
    }

    /**
     * @return  a list of Trio::Tags, computed from the given maps of tags
     * @param tagged the map of tags
     */
    private List<Tag> buildTags(final Map<String, List<String>> tagged) {
        assert tagged.size() == Selection.values().length - 1: 
                "tags were not properly collected/initialized.";
        
        final List<Tag> tags = new ArrayList<Tag>();
        for (String eachTag: tagged.keySet()) {
            final List<String> taggedComponents = tagged.get(eachTag);
            if (!taggedComponents.isEmpty()) {
                tags.add(new Tag(eachTag, taggedComponents));
            }
        }
        
        return tags;
    }

}
