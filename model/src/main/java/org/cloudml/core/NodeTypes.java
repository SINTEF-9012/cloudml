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
/*
 */
package org.cloudml.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class NodeTypes implements Iterable<Node> {

    private final DeploymentModel context;
    private final HashMap<String, Node> nodes;

    public NodeTypes(DeploymentModel context) {
        this.context = context;
        this.nodes = new HashMap<String, Node>();
    }
    
    private NodeTypes(DeploymentModel context, Collection<Node> nodes) {
        this.context = context;
        this.nodes = new HashMap<String, Node>();
        for(Node node: nodes) {
            this.nodes.put(node.getName(), node);
        }
    }

    public void add(Node node) {
        if (!context.getProviders().contains(node.getProvider())) {
            String message = String.format("The provider '%s' (used by node '%s') is not part of the model", node.getProvider().getName(), node.getName());
            throw new IllegalStateException(message);
        }
        this.nodes.put(node.getName(), node);
    }

    public Node remove(Node node) {
        if (context.isUsed(node)) {
            final String message = String.format("Unable to remove node type '%s' as there are still some related instances", node.getName());
            throw new IllegalStateException(message);
        }
        return this.nodes.remove(node.getName());
    }
 
    public Node named(String nodeName) {
        return this.nodes.get(nodeName);
    }

    public NodeTypes providedBy(Provider provider) {
        ArrayList<Node> selectedNodes = new ArrayList<Node>();
        for (Node node : this.nodes.values()) {
            if (node.isProvidedBy(provider)) {
                selectedNodes.add(node);
            }
        }
        return new NodeTypes(context, selectedNodes);
    }
    
    public boolean contains(Node node) {
        return this.nodes.containsKey(node.getName());
    }
    
    public boolean isEmpty() {
        return this.nodes.isEmpty();
    }

    @Override
    public Iterator<Node> iterator() {
        return nodes.values().iterator();
    }
}
