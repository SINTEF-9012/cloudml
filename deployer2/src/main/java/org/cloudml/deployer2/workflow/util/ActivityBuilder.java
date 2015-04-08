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
package org.cloudml.deployer2.workflow.util;

import org.cloudml.deployer2.dsl.*;

import java.util.ArrayList;

/**
 * Created by Maksym on 24.03.2015.
 */
public class ActivityBuilder {

    public enum Edges {IN, OUT, INOUT, NOEDGES};
    public enum ObjectNodeType {PARAMETER, DATASTORE, EXPANSION, OBJECT};
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

    // returns object node of specific kind with dataflow edges: either --->|_| or |_|---> or --->|_|---> or |_|
    public static ObjectNode objectNode(String kindOfObjects, Edges flows, ObjectNodeType returnType) throws Exception {
        ActivityEdge dataFlow = new ActivityEdge(true);
        ObjectNode node = null;
        switch (returnType){
            case PARAMETER: node = new ActivityParameterNode(kindOfObjects);
                            break;
            case DATASTORE: node = new DatastoreNode(kindOfObjects);
                            break;
            case EXPANSION: node = new ExpansionNode(kindOfObjects);
                            break;
            case OBJECT:    node = new ObjectNode(kindOfObjects);
                            break;
        }
        switch (flows){
            case IN:      node.addEdge(dataFlow, ActivityNode.Direction.IN);
                          break;
            case OUT:     node.addEdge(dataFlow, ActivityNode.Direction.OUT);
                          break;
            case INOUT:   if(node.getClass().equals(ActivityParameterNode.class)){
                            throw new Exception("Proper edges fro ActivitiParameterNode are IN, OUT or NOEDGES");
                          } else {
                            node.addEdge(dataFlow, ActivityNode.Direction.IN);
                            node.addEdge(dataFlow, ActivityNode.Direction.OUT);
                          }
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
        if (isDataFlow) {
            edge = new ActivityEdge(true);
            while (parallel.size() < parallelEdges) {
                parallel.add(new ActivityEdge(true));
            }
        } else {
            edge = new ActivityEdge();
            while (parallel.size() < parallelEdges) {
                parallel.add(new ActivityEdge());
            }
        }
        ControlNode node = (returnFork) ? new Fork(edge, parallel) : new Join(parallel, edge);
        updateActivity(node);
        return node;
    }

    // returns action                                                                          ===>()
    // this action is already connected to data (unless null) and control source               --->()
    public static Action action(ActivityEdge control, ActivityEdge data, Object input, String methodToCall) throws Exception {
        Action action = new Action(methodToCall);
        action.addEdge(control, ActivityNode.Direction.IN);
        getActivity().removeEdge(control); // to avoid duplication of edges
        if (data != null) {
            action.addEdge(data, ActivityNode.Direction.IN);
            getActivity().removeEdge(data); // to avoid duplication of edges
        }
        if (input != null) {
            action.addInput(input);
        }
        // add outgoing control flow. outgoing dataflow and actual objects shall be decided upon inside calling method. //TODO Issue is how to update activity with outgoing data edges
        //action.addEdge(new ActivityEdge(), ActivityNode.Direction.OUT);
        updateActivity(action);
        return action;
    }

    // sets outgoing edge of initial node as incoming of fork
    public static void connectInitialToFork(ActivityInitialNode initial, Fork fork) throws Exception {
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

    // connect Join to Final node
    public static void connectJoinToFinal(Join join, ActivityFinalNode node) throws Exception {
        ActivityEdge joinOutgoing = join.getOutgoing().get(0);
        if (joinOutgoing.isObjectFlow()){
            throw new Exception("Final node may be connected only with Join with control flow edges, this join node has data flow edges");
        } else {
            // when we join initial node and fork we lose one edge, so remove it from activity
            getActivity().removeEdge(joinOutgoing);
            // actually connect them via edges
            join.setOutgoing(node.getIncoming());
        }
    }

    // connect object node to fork node with data edges
    public static void connectObjectToFork(ObjectNode node, Fork fork) throws Exception {
        ActivityEdge forkIncoming = fork.getIncoming().get(0);
        if (!forkIncoming.isObjectFlow()){
            throw new Exception("Object node may be connected only to Fork with data flow edges, this fork has control flow edges");
        } else if (node.getOutgoing().size() > 0){
            // when we join object node and fork we lose one edge, so remove it from activity
            getActivity().removeEdge(forkIncoming);
            // actually connect them via edges
            fork.setIncoming(node.getOutgoing());
        } else {
            node.setOutgoing(fork.getIncoming());
        }
    }


    public static void connectJoinToObject(Join join, ObjectNode node) throws Exception {
        ActivityEdge joinOutgoing = join.getOutgoing().get(0);
        if (!joinOutgoing.isObjectFlow()){
            throw new Exception("Object node may be connected only to Join with data flow edges, this join node has control flow edges");
        } else if (node.getIncoming().size() > 0){
            // when we connect object node and join node we lose one edge, so remove it from activity
            getActivity().removeEdge(joinOutgoing);
            // actually connect them via edges
            join.setOutgoing(node.getIncoming());
        } else {
            node.setIncoming(join.getOutgoing());
        }
    }

    // connects a set of actions without outgoing edges to Join nodes (control and maybe data join)
    public static void connectActionsWithJoinNodes(ArrayList<Action> actions, Join control, Join data) throws Exception {
        for (Action a:actions){
            int index = actions.indexOf(a);
            if (control != null) {
                ActivityEdge controlEdge = control.getIncoming().get(index);
                a.addEdge(controlEdge, ActivityNode.Direction.OUT);
            }
            if (data != null) {
                ActivityEdge dataEdge = data.getIncoming().get(index);
                a.addEdge(dataEdge, ActivityNode.Direction.OUT);
            }
        }
    }

    // connects data Fork with actions
    //@param fork - fork with outgoing edges
    //@param actions - list of actions where each action has
    public static void connectDataForkWithActions(Fork fork, ArrayList<Action> actions) throws Exception {
        if (fork.getOutgoing().size() != actions.size()){
            throw new Exception("Number of outgoing fork edges and number of actions doesn't match, check your code.");
        }
        ArrayList<ActivityEdge> forkOutgoing = new ArrayList<ActivityEdge>();
        for (Action action:actions){
            ActivityEdge data = action.getControlOrObjectFlowEdges(false, ActivityNode.Direction.IN).get(0);
            forkOutgoing.add(data);
        }
        for (ActivityEdge edge:fork.getOutgoing()){
            getActivity().removeEdge(edge); //avoid duplication
        }
        fork.setOutgoing(forkOutgoing);
    }

    // get node which holds public addresses of VMs and platforms
    public static ObjectNode getAddressesRegistry(){
        ObjectNode registry = null;
        for (ActivityNode node:getActivity().getNodes()){
            if (node.getName().equals("Public Addresses")){
                registry = (ObjectNode) node;
            }
        }
        return registry;
    }

    // return only actions
    public static ArrayList<Action> getActions(){
        ArrayList<Action> actions = new ArrayList<Action>();
        for (ActivityNode node:getActivity().getNodes()){
            if (node instanceof Action){
                actions.add((Action) node);
            }
        }
        return actions;
    }


//    public static void main (String[] args){
//        try {
//            ActivityInitialNode initial = ActivityBuilder.controlStart();
//            System.out.println(ActivityBuilder.getActivity().toString());
//            Fork fork = (Fork) ActivityBuilder.forkOrJoin(3,false,true);
//            System.out.println(ActivityBuilder.getActivity().toString());
//            ActivityBuilder.connectInitialToFork(initial, fork);
//            System.out.println(ActivityBuilder.getActivity().toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
