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
package org.cloudml.deployer2.dsl.util;

import org.cloudml.deployer2.dsl.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maksym on 24.03.2015.
 */
public class ActivityBuilder {

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

    /*
    ------------------------------------------ Methods to create different kinds of activity nodes----------------------------------------------
     */

    // creates initial node
    public static ActivityInitialNode controlStart() throws Exception {
        ActivityInitialNode initial = new ActivityInitialNode();
//        ActivityEdge to = new ActivityEdge();
//        initial.addEdge(to, ActivityNode.Direction.OUT);
        getActivity().addNode(initial);
        return initial;
    }

    // creates final node
    public static ActivityFinalNode controlStop() throws Exception {
        ActivityFinalNode finalNode = new ActivityFinalNode();
//        ActivityEdge toFinalNode = new ActivityEdge();
//        finalNode.addEdge(toFinalNode, ActivityNode.Direction.IN);
        getActivity().addNode(finalNode);
        return finalNode;
    }

    // creates fork node that can have only edges of the specified type
    public static Fork forkNode(boolean isDataFork){
        Fork fork =  new Fork(isDataFork);
        updateActivity(fork);
        return fork;
    }

    // creates join node that can have only edges of the specified type
    public static Join joinNode(boolean isDataJoin){
        Join join = new Join(isDataJoin);
        updateActivity(join);
        return join;
    }

    // creates action node with an optional set of input data
    public static Action actionNode(String methodToCall, Object... inputs){
        Action action = new Action(methodToCall);
        if (inputs != null){
            for (Object data:inputs)
                action.addInput(data);
        }
        updateActivity(action);
        return action;
    }

    // creates object node of the specified type
    public static ObjectNode objectNode(String name, ObjectNodeType type) throws Exception {
        ObjectNode node = null;
        switch (type){
            case PARAMETER: node = new ActivityParameterNode(name);
                break;
            case DATASTORE: node = new DatastoreNode(name);
                break;
            case EXPANSION: node = new ExpansionNode(name);
                break;
            case OBJECT:    node = new ObjectNode(name);
                break;
        }
        updateActivity(node);
        return node;
    }

    // creates Object node with specific name to save IPs of VMs
    public static ObjectNode createIPregistry() throws Exception {
        return objectNode("Public Addresses", ObjectNodeType.OBJECT);
    }

    /*
    ------------------------------------------Activity manipulation: connect, add, remove...----------------------------------------
     */

    // connect two nodes with data or control flow
    public static void connect(ActivityNode from, ActivityNode to, boolean isControlFlow) throws Exception {
        boolean exists = connectionExists(from, to, isControlFlow);
        if (!exists){
            ActivityEdge edge = new ActivityEdge(!isControlFlow);
            from.addEdge(edge, ActivityNode.Direction.OUT);
            to.addEdge(edge, ActivityNode.Direction.IN);
            getActivity().getEdges().add(edge);
        } else
            throw new Exception("Such connection between the given nodes already exists");
    }

    // remove data or control edge between two nodes
    public static void disconnect(ActivityNode from, ActivityNode to, boolean isControlFlow) throws Exception {
        boolean exists = connectionExists(from, to, isControlFlow);
        if (exists){
            ActivityEdge remove = null;
            for (ActivityEdge edge:from.getOutgoing()){
                if ((!edge.isObjectFlow() == isControlFlow) && edge.getTarget().equals(to)){
                    remove = edge;
                }
            }
            if (remove != null) {
                from.removeEdge(remove, ActivityNode.Direction.OUT);
                to.removeEdge(remove, ActivityNode.Direction.IN);
                deleteEdge(remove);
            }
        } else
            throw new Exception("There is no connection between the given nodes");
    }

    // check if control (controlFlow=true) or data (controlFlow=false) connection between specified nodes exists already
    private static boolean connectionExists(ActivityNode from, ActivityNode to, boolean controlFlow) {
        boolean result = false;
        for (ActivityEdge out:from.getOutgoing()){
            if ((!out.isObjectFlow() == controlFlow) && out.getTarget().equals(to)) {
                result = true;
                break;
            }
        }
        return result;
    }

    // adds edge to the activity
    public static void saveEdge(ActivityEdge edge){
        getActivity().getEdges().add(edge);
    }

    // removes edge from the activity
    public static void deleteEdge(ActivityEdge edge){
        if (getActivity().getEdges().contains(edge))
            getActivity().getEdges().remove(edge);
    }

    // removes node and its incoming/outgoing edges from the activity
    public static void deleteNode(ActivityNode node) throws Exception {
        List<ActivityEdge> sourceEdges = new ArrayList<>();
        List<ActivityEdge> targetEdges = new ArrayList<>();

        // splitting into two for loops is necessary to avoid ConcurrentModificationException
        if (node.getIncoming() != null && !node.getIncoming().isEmpty()){
            for (ActivityEdge in:node.getIncoming())
                sourceEdges.add(in);
        }
        for (ActivityEdge e:sourceEdges){
            boolean isControlFlow = !e.isObjectFlow();
            ActivityBuilder.disconnect(e.getSource(), node, isControlFlow);
        }


        if (node.getOutgoing() != null && !node.getOutgoing().isEmpty()){
            for (ActivityEdge out:node.getOutgoing())
                targetEdges.add(out);
        }
        for (ActivityEdge e:targetEdges){
            boolean isControlFlow = !e.isObjectFlow();
            ActivityBuilder.disconnect(node, e.getTarget(), isControlFlow);
        }

        getActivity().removeNode(node);
    }

    /*
    ---------------------------------------------------------------GETTERS----------------------------------------------
     */

    // return only actions of the activity diagram
    public static ArrayList<Action> getActions(){
        ArrayList<Action> actions = new ArrayList<Action>();
        for (ActivityNode node:getActivity().getNodes()){
            if (node instanceof Action){
                actions.add((Action) node);
            }
        }
        return actions;
    }

    // return only object nodes of the activity diagram
    public static ArrayList<ObjectNode> getObjectNodes(){
        ArrayList<ObjectNode> dataNodes = new ArrayList<>();
        for (ActivityNode node:getActivity().getNodes()){
            if (node instanceof ObjectNode){
                dataNodes.add((ObjectNode) node);
            }
        }
        return dataNodes;
    }

    //return only control nodes of the activity diagram
    public static ArrayList<ControlNode> getControlNodes(){
        ArrayList<ControlNode> controls = new ArrayList<ControlNode>();
        for (ActivityNode node:getActivity().getNodes()){
            if (node instanceof ControlNode){
                controls.add((ControlNode) node);
            }
        }
        return controls;
    }

    public static ActivityInitialNode getStartNode(){
        ActivityInitialNode initialNode = null;
        for (ActivityNode initial:activity.getNodes()){
            if (initial instanceof ActivityInitialNode){
                initialNode = (ActivityInitialNode) initial;
            }
        }
        return  initialNode;
    }

    public static ActivityFinalNode getFinalNode(){
        ActivityFinalNode finalNode = null;
        for (ActivityNode node:activity.getNodes()){
            if (node instanceof ActivityFinalNode){
                finalNode = (ActivityFinalNode) node;
            }
        }
        return  finalNode;
    }

    // get node which holds public addresses of VMs and platforms
    public static ObjectNode getIPregistry(){
        ObjectNode registry = null;
        for (ActivityNode node:getActivity().getNodes()){
            if (node.getName().equals("Public Addresses")){
                registry = (ObjectNode) node;
            }
        }
        return registry;
    }

}
