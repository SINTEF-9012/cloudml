package org.cloudml.deployer2.dsl;

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
        return "This activity contains " + getNodes().size() + " nodes and " + getEdges().size() + " edges";
    }
}
