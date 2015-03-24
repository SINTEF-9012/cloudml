package org.cloudml.deployer2.camel;

import org.cloudml.deployer2.dsl.*;

import java.util.ArrayList;

import static org.cloudml.deployer2.camel.ActivityBuilder.Edges.IN;

/**
 * Created by Maksym on 24.03.2015.
 */
public class ActivityBuilder {

    public enum Edges {IN, OUT, INOUT, NOEDGES};
    private static Activity activity = new Activity(new ArrayList<ActivityEdge>(), new ArrayList<ActivityNode>());

    public static Activity getActivity() {
        return activity;
    }

    public static void setActivity(Activity activity) {
        ActivityBuilder.activity = activity;
    }

    private static void updateActivity(ActivityNode node){
        getActivity().addNode(node);
        if ((node.getIncoming() != null) && (node.getIncoming().size() > 0)) {
            getActivity().getEdges().addAll(node.getIncoming());
        }
        if ((node.getOutgoing() != null) && (node.getOutgoing().size() > 0)) {
            getActivity().getEdges().addAll(node.getOutgoing());
        }
    }


    // *  initial node
    // *! final node
    // ===> control flow
    // ---> data flow
    // | fork
    // |& join
    // |_| object node
    // () action

    // returns initial node with an outgoing control edge *===>
    public static ActivityInitialNode controlStart() throws Exception {
        ActivityInitialNode initial = new ActivityInitialNode();
        ActivityEdge to = new ActivityEdge();
        initial.addEdge(to, ActivityNode.Direction.OUT);
        updateActivity(initial);
        return initial;
    }

    // returns final node with incoming control edge ===>*!
    public static ActivityFinalNode controlStop() throws Exception {
        ActivityFinalNode finalNode = new ActivityFinalNode();
        ActivityEdge toFinalNode = new ActivityEdge();
        finalNode.addEdge(toFinalNode, ActivityNode.Direction.IN);
        updateActivity(finalNode);
        return finalNode;
    }

    // returns object node with dataflow edges: either --->|_| or |_|---> or --->|_|--->
    public static ObjectNode objectNode(String kindOfObjects, Edges flows) throws Exception {
        ActivityEdge dataFlow = new ActivityEdge(true);
        ObjectNode node = new ObjectNode(kindOfObjects);
        switch (flows){
            case IN:      node.addEdge(dataFlow, ActivityNode.Direction.IN);
                          break;
            case OUT:     node.addEdge(dataFlow, ActivityNode.Direction.OUT);
                          break;
            case INOUT:   node.addEdge(dataFlow, ActivityNode.Direction.IN);
                          node.addEdge(dataFlow, ActivityNode.Direction.OUT);
                          break;
            case NOEDGES: break;
        }
        updateActivity(node);
        return node;
    }

    // returns fork with a given number of outgoing eges (data or control): ===>|===> or --->|--->
    //                                                                          |===>        |--->
    // or returns join with the same semantics ===>|===> or --->|--->
    //                                         ===>|        --->|
    public static ControlNode forkOrJoin(int parallelEdges, boolean isDataFlow, boolean returnFork) throws Exception {
        ActivityEdge edge;
        ArrayList<ActivityEdge> parallel = new ArrayList<ActivityEdge>(parallelEdges);
        if (isDataFlow){
            edge = new ActivityEdge(true);
            while (parallel.size() < parallelEdges){
                parallel.add(new ActivityEdge(true));
            }
        } else {
            edge = new ActivityEdge();
            while (parallel.size() < parallelEdges){
                parallel.add(new ActivityEdge());
            }
        }
        System.out.println(parallel.size());
        ControlNode node = (returnFork) ? new Fork(edge, parallel) : new Join(parallel, edge);
        System.out.println(node.getOutgoing().size());
        updateActivity(node);
        return node;
    }

    public static Action action(ActivityEdge control, ActivityEdge data, Object input, String methodToCall) throws Exception {
        Action action = new Action(methodToCall);
        action.addEdge(control, ActivityNode.Direction.IN);
        action.addEdge(data, ActivityNode.Direction.IN);
        if (input != null) {
            action.addInput(input);
        }
        // add outgoing control flow. outgoing dataflow and actual objects shall be decided upon inside calling method. //TODO Issue is how to update activity with outgoing data edges
        action.addEdge(new ActivityEdge(), ActivityNode.Direction.OUT);
        updateActivity(action);
        return action;
    }

    // sets outgoing edge of intial node as incoming of fork
    public static void joinInitialAndFork(ActivityInitialNode initial, Fork fork) throws Exception {
        ActivityEdge forkIncoming = fork.getIncoming().get(0);
        if (forkIncoming.isObjectFlow()){
            throw new Exception("Initial node may be connected only to Fork with control flow edges, this fork has data flow edges");
        } else {
            // when we join initial node and fork we lose one edge, so remove it from activity
            getActivity().removeEdge(forkIncoming);
            // actually connect them via edges
            fork.setIncoming(initial.getOutgoing());
        }
    }


    public static void main (String[] args){
        try {
            ActivityInitialNode initial = ActivityBuilder.controlStart();
            System.out.println(ActivityBuilder.getActivity().toString());
            Fork fork = (Fork) ActivityBuilder.forkOrJoin(3,false,true);
            System.out.println(ActivityBuilder.getActivity().toString());
            ActivityBuilder.joinInitialAndFork(initial, fork);
            System.out.println(ActivityBuilder.getActivity().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
