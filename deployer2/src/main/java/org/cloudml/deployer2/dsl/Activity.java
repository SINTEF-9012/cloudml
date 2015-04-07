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
package org.cloudml.deployer2.dsl;

import org.cloudml.core.NamedElement;

import java.util.ArrayList;

/**
 * Created by Maksym on 16.03.2015.
 */
public class Activity extends Element {
    private ArrayList<ActivityEdge> edges = new ArrayList<ActivityEdge>();
    private ArrayList<ActivityNode> nodes = new ArrayList<ActivityNode>();

    public Activity(){}

    public Activity(ArrayList<ActivityEdge> edges, ArrayList<ActivityNode> nodes) {
        setEdges(edges);
        setNodes(nodes);
    }

    public void addNode(ActivityNode node){
        getNodes().add(node);
    }

    public void removeNode (ActivityNode node){
        if (getNodes().contains(node)){
            getNodes().remove(node);
        }
    }

    public void addEdge(ActivityEdge edge){
        getEdges().add(edge);
    }

    public void removeEdge(ActivityEdge edge){
        if (getEdges().contains(edge)){
            getEdges().remove(edge);
        }
    }

    public ArrayList<ActivityEdge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<ActivityEdge> edges) {
        this.edges = edges;
    }

    public ArrayList<ActivityNode> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<ActivityNode> nodes) {
        this.nodes = nodes;
    }

    @Override
    public String toString(){
        int control = 0;
        int data = 0;
        for (ActivityEdge e:getEdges()){
            if (e.isObjectFlow()){
                data++;
            } else {
                control ++;
            }
        }
        String activitySummary = "\nThis activity contains " +
                getNodes().size() + " nodes and " +
                getEdges().size() + " edges (" +
                control + " control and " + data  +
                " data flow edges)";
        String nodes = "\nNodes are:";
        for (ActivityNode node: getNodes()){
            nodes += "\n- " + node.getClass().getSimpleName() +
                    ":" + node.getName();
            if (node instanceof Action){
                nodes += "_" + ((NamedElement)((Action) node).getInputs().get(0)).getName();
            }
        }
        String edges = "\nEdge are:";
        for (ActivityEdge edge:getEdges()){
            edges += "\n- " + edge.toString();
        }
        return activitySummary + nodes + edges;
    }
}
