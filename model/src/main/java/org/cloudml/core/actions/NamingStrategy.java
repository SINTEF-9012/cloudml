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
package org.cloudml.core.actions;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.cloudml.core.Component;
import org.cloudml.core.Deployment;
import org.cloudml.core.NamedElement;
import org.cloudml.core.Relationship;

public class NamingStrategy {

    public static final String TYPE_NAME_MARKER = "</type-name>";
    public static final String INDEX_MARKER = "no_%d";
    public static final String FORMAT = String.format("%s (%s)", TYPE_NAME_MARKER, INDEX_MARKER);
    public static final String DEFAULT_PATTERN = "(\\d+)";
    public static final int ID_GROUP_INDEX = 1;
    public static final String COMPONENT_KIND = "component";
    public static final String RELATIONSHIP_KIND = "relationship";
    private final int minimumId = 1;
    private final Pattern pattern;

    public NamingStrategy() {
        this.pattern = Pattern.compile(DEFAULT_PATTERN);
    }

    private String createUniqueName(Collection<? extends NamedElement> existings, String format) {
        return String.format(format, minimumUnusedId(existings));
    }

    private int minimumUnusedId(Collection<? extends NamedElement> names) {
        final List<Integer> usedIds = extractUsedIds(names);
        int id = minimumId;
        while (usedIds.contains(id)) {
            id++;
        }
        return id;
    }

    private List<Integer> extractUsedIds(Collection<? extends NamedElement> names) throws NumberFormatException {
        final ArrayList<Integer> usedIds = new ArrayList<Integer>();
        for (NamedElement element : names) {
            Matcher matcher = pattern.matcher(element.getName());
            if (matcher.find()) {
                usedIds.add(Integer.parseInt(matcher.group(ID_GROUP_INDEX)));
            }
        }
        return usedIds;
    }

    public String createUniqueComponentInstanceName(Deployment deployment, Component type) {
        String format = prepareFormat(type.getName());
        return createUniqueName(deployment.getComponentInstances(), format);
    }

    
    public String createUniqueRelationshipInstanceName(Deployment deployment, Relationship type) {
        String format = prepareFormat(type.getName());
        return createUniqueName(deployment.getRelationshipInstances(), format);
    }

    private String prepareFormat(String typeName) {
        String format = FORMAT;
        format = format.replace(TYPE_NAME_MARKER, typeName);
        return format;
    }
}
