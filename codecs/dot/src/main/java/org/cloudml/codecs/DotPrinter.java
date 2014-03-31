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


package org.cloudml.codecs;

import java.util.Collection;
import java.util.HashMap;
import org.cloudml.core.*;


public class DotPrinter {
    
    private final SymbolTable symbols;
    private final StringBuilder builder;

    public DotPrinter() {
        this.builder = new StringBuilder();
        this.symbols = new SymbolTable();
    }

    public String print(DeploymentModel model) {
        builder.append("digraph ");
        builder.append("G");
        builder.append(" {");
        printClusters(model);
        printEdges(model);
        builder.append("}");
        return builder.toString();
    }

    private void printClusters(DeploymentModel model) {
        int clusterIndex = 0;
        for (NodeInstance nodeInstance : model.getNodeInstances()) {
            clusterIndex += 1;
            builder.append("subgraph cluster_").append(clusterIndex).append(" {");
            builder.append("style=filled;\n");
            builder.append("color=lightgrey;\n");
            builder.append("label=\"").append(escape(nodeInstance.getName())).append("\";");
            printDotNodes(clusterIndex, model.getArtefactInstances().hostedBy(nodeInstance));
            builder.append("}");
        }
    }

    private void printDotNodes(int clusterIndex, Collection<ArtefactInstance> artefactInstances) {
        int artefactIndex = 0;
        for (ArtefactInstance artefact : artefactInstances) {
            artefactIndex += 1;
            final String dotNodeName = "node_" + clusterIndex + "_" + artefactIndex;
            symbols.put(artefact, dotNodeName);
            builder.append("\t\t")
                    .append(dotNodeName)
                    .append(" [")
                    .append("label=\"")
                    .append(escape(artefact.getName()))
                    .append("\",color=black,style=filled,fillcolor=white];\n");
        }
    }

    private void printEdges(DeploymentModel model) {
        for (BindingInstance binding : model.getBindingInstances()) {
            printEdge(binding);
        }
    }

    private void printEdge(BindingInstance binding) {
        builder.append("\t");
        builder.append(symbols.get(binding.getClient().getOwner()));
        builder.append(" -> ");
        builder.append(symbols.get(binding.getServer().getOwner()));
        builder.append("[label=\"").append(escape(binding.getType().getName())).append("\"]");
        builder.append(";");
    }

    private String escape(String name) {
        return name.trim().replace("[^\\]\"", "\\\"");
    }

    private static class SymbolTable {

        private final HashMap<Object, String> table;

        public SymbolTable() {
            this.table = new HashMap<Object, String>();
        }

        private <T extends NamedElement> String get(T namedElement) {
            return this.table.get(namedElement);
        }

        private <T extends NamedElement> void put(T namedElement, String dotName) {
            this.table.put(namedElement, dotName);
        }
    }
}

