package org.cloudml.deployer2.dsl;

import java.util.ArrayList;

/**
 * Created by Maksym on 16.03.2015.
 */
public class Activity extends Element {
    private ArrayList<ActivityEdge> edges = new ArrayList<ActivityEdge>();
    private ArrayList<ActivityNode> nodes = new ArrayList<ActivityNode>();

    public Activity(ArrayList<ActivityEdge> edges, ArrayList<ActivityNode> nodes) {
        setEdges(edges);
        setNodes(nodes);
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
}
