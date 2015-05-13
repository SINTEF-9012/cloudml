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

/**
 * Created by Maksym on 20.04.2015.
 */
public class ActivityValidator {

    public static void checkActivity(Activity activity) throws Exception {
        checkEdges(activity.getEdges());
        checkNodes(activity.getNodes());
        checkCycles(activity.getNodes());
        checkStartStop(activity.getNodes());
    }

    private static void checkControlEdge(ActivityEdge edge) throws Exception {
        if (edge.getTarget() instanceof ObjectNode){
            throw new Exception(edge.toString() + " target is ObjectNode, control flow target must be instance of Control or Action nodes");
        } else if (edge.getSource() instanceof  ObjectNode){
            throw new Exception(edge.toString() + " source is ObjectNode, control flow source must be instance of Control or Action nodes");
        }
    }

    private static void checkStartStop(ArrayList<ActivityNode> nodes) throws Exception {
        ArrayList<ActivityInitialNode> startNodes = new ArrayList<ActivityInitialNode>();
        ArrayList<ActivityFinalNode> finalNodes = new ArrayList<ActivityFinalNode>();
        for (ActivityNode node:nodes){
            if (node instanceof ActivityInitialNode)
                startNodes.add((ActivityInitialNode) node);
            if (node instanceof ActivityFinalNode)
                finalNodes.add((ActivityFinalNode) node);
        }
        if (startNodes.isEmpty())
            throw new Exception("This activity does not have initial node");
        if (startNodes.size() > 1)
            throw new Exception("This activity has more than one initial node");
        if (finalNodes.isEmpty())
            throw new Exception("This activity does not have final node");
        if (finalNodes.size() > 1)
            throw new Exception("This activity has more than one final node");
    }

    private static void checkCycles(ArrayList<ActivityNode> nodes) throws Exception {
        for (ActivityNode node:nodes){
            if (node.getOutgoing() != null && !node.getOutgoing().isEmpty() && node.getIncoming() != null && !node.getIncoming().isEmpty()){
                // get source nodes
                ArrayList<ActivityNode> sources = new ArrayList<ActivityNode>();
                for (ActivityEdge edge:node.getIncoming()){
                    sources.add(edge.getSource());
                }
                //get target nodes
                ArrayList<ActivityNode> targets = new ArrayList<ActivityNode>();
                for (ActivityEdge edge:node.getOutgoing()){
                    targets.add(edge.getTarget());
                }
                // check if targets contain any element from source, if yes - throw exception
                for (ActivityNode source:sources){
                    if (targets.contains(source))
                        throw new Exception("Cycle detected between " + source.toString() + " and " + node.toString());
                }
            }
        }
    }

    private static void checkNodes(ArrayList<ActivityNode> nodes) throws Exception {
        for (ActivityNode node:nodes){
            if (!(node instanceof ActivityInitialNode) && !(node instanceof ActivityParameterNode) && node.getIncoming().isEmpty())
                throw new Exception(node.toString() + " does not have incoming edges");
            if (!(node instanceof ActivityFinalNode) && !(node instanceof ActivityParameterNode) && node.getOutgoing().isEmpty())
                throw new Exception(node.toString() + " does not have outgoing edges");
            if (node instanceof Action)
                checkAction((Action)node);
        }
    }

    private static void checkAction(Action node) throws Exception {
        ArrayList<ActivityEdge> incomingControl = node.getControlOrObjectFlowEdges(true, ActivityNode.Direction.IN);
        ArrayList<ActivityEdge> outgoingControl = node.getControlOrObjectFlowEdges(true, ActivityNode.Direction.OUT);
        if (incomingControl.isEmpty() || incomingControl == null)
            throw new Exception(node.toString() + " does not have incoming control flow");
        if (outgoingControl.isEmpty() || outgoingControl == null)
            throw new Exception(node.toString() + " does not have outgoing control flow");
    }

    private static void checkEdges(ArrayList<ActivityEdge> edges) throws Exception {
        for (ActivityEdge edge:edges){
            if (edge.getSource() == null)
                throw new Exception(edge.toString() + " does not have a source node");
            if (edge.getTarget() == null)
                throw new Exception(edge.toString() + " does not have a target node");
            if (!edge.isObjectFlow())
                checkControlEdge(edge);

        }
    }
}
