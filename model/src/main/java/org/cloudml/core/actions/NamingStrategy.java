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
import org.cloudml.core.Artefact;
import org.cloudml.core.Binding;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.NamedElement;
import org.cloudml.core.Node;

public class NamingStrategy {

    public static final String KIND_MARKER = "</kind>";
    public static final String INSTANCE_MARKER = "</instance>";
    public static final String TYPE_NAME_MARKER = "</type-name>";
    public static final String INDEX_MARKER = "%d";
    public static final String FORMAT = String.format("%s %s %s", KIND_MARKER, INSTANCE_MARKER, INDEX_MARKER);
    public static final String DEFAULT_PATTERN = "(\\d+)";
    public static final int ID_GROUP_INDEX = 1;
    public static final String NODE_KIND = "node";
    public static final String ARTEFACT_KIND = "artefact";
    public static final String BINDING_KIND = "binding";
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

    public String createUniqueNodeInstanceName(DeploymentModel deployment, Node type) {
        String format = prepareFormat(NODE_KIND, type.getName());
        return createUniqueName(deployment.getNodeInstances(), format);
    }

    public String createUniqueArtefactInstanceName(DeploymentModel deployment, Artefact type) {
        String format = prepareFormat(ARTEFACT_KIND, type.getName());
        return createUniqueName(deployment.getArtefactInstances(), format);
    }

    public String createUniqueBindingInstanceName(DeploymentModel deployment, Binding type) {
        String format = prepareFormat(BINDING_KIND, type.getName());
        return createUniqueName(deployment.getBindingInstances(), format);
    }

    private String prepareFormat(String kind, String typeName) {
        String format = FORMAT;
        format = format.replace(KIND_MARKER, kind);
        format = format.replace(INSTANCE_MARKER, "instance");
        format = format.replace(TYPE_NAME_MARKER, typeName);
        return format;
    }
}
