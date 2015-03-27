package org.cloudml.deployer2.camel.util;

import org.apache.camel.util.jndi.JndiContext;
import org.cloudml.deployer2.camel.camel_beans.ActionNodeBean;
import org.cloudml.deployer2.camel.camel_beans.ActivityEdgeBean;
import org.cloudml.deployer2.camel.camel_beans.ControlNodeBean;
import org.cloudml.deployer2.camel.camel_beans.ObjectNodeBean;
import org.cloudml.deployer2.dsl.*;

import javax.naming.NamingException;
import java.util.ArrayList;

/**
 * Created by Maksym on 26.03.2015.
 */
public class BeansRegistrator {

    public static void performRegistration(JndiContext jndiContext, ArrayList<ActivityNode> nodes, ArrayList<String> taskNames, boolean debugMode) throws NamingException {

        // indexes are used to distinguish actions with the same name, for instance upload command will be upload 1, upload 2, etc.
        int actionIndex = 1;

        for (ActivityNode node : nodes) {
            if (node instanceof Action) {
                if (node instanceof ExpansionRegion) {
                    //TODO stuff for internal components of expansion region, probably recursion
                }
                Action a = (Action) node;
                ActionNodeBean vm = new ActionNodeBean(a, debugMode);
                String actionName = a.getName() + " " + actionIndex;
                jndiContext.unbind(actionName); // fixes javax.naming.NamingException: Something already bound at
                jndiContext.bind(actionName, vm);
                // update list of names by which we will access every bean
                taskNames.add(actionName);
                taskNames.addAll(registerActionEdges(jndiContext, a, actionName));
                actionIndex++;
            } else if (node instanceof ObjectNode) {
                ObjectNode object = (ObjectNode)node;
                ObjectNodeBean ip = new ObjectNodeBean(object);
                String name = object.getName();
                jndiContext.unbind(name);
                jndiContext.bind(name, ip);
                // update list of names by which we will access every bean
                taskNames.add(name);
                taskNames.addAll(registerObjectEdges(jndiContext, object, name));
            } else if (node instanceof ControlNode) {
                ControlNode controlNode = (ControlNode)node;
                ControlNodeBean control = new ControlNodeBean((ControlNode) node);
                // update list of names by which we will access every bean
                taskNames.addAll(registerControlNodeAndEdges(jndiContext, controlNode));
            }
        }
    }

    private static ArrayList<String> registerControlNodeAndEdges(JndiContext jndiContext, ControlNode controlNode) throws NamingException {
        // rules for registering edges of control node are:
        // - register outgoing edge for ActivityInitial node
        // - register incoming edge for ActivityFinal node
        // - do not register any edges for fork and join nodes

        ArrayList<String> taskNames = new ArrayList<String>();

        String identifier;
        if (controlNode instanceof ActivityInitialNode){
            // register outgoing edge for ActivityInitial node
            identifier = "Start";
            jndiContext.unbind(identifier);
            jndiContext.bind(identifier, new ControlNodeBean(controlNode));
            taskNames.add(identifier);
            ActivityEdge out = controlNode.getOutgoing().get(0);
            jndiContext.unbind(identifier + ":OUT");
            jndiContext.bind(identifier + ":OUT", new ActivityEdgeBean(out));
            taskNames.add(identifier + ":OUT");
        } else if (controlNode instanceof ActivityFinalNode){
            // register incoming edge for ActivityFinal node
            identifier = "Stop";
            jndiContext.unbind(identifier);
            jndiContext.bind(identifier, new ControlNodeBean(controlNode));
            taskNames.add(identifier);
            ActivityEdge in = controlNode.getIncoming().get(0);
            jndiContext.unbind(identifier + ":IN");
            jndiContext.bind(identifier + ":IN", new ActivityEdgeBean(in));
            taskNames.add(identifier + ":IN");
        } else if (controlNode instanceof Fork){
            // do not register any edges for fork node except the node itself
            ActivityNode source = controlNode.getIncoming().get(0).getSource();
            String name = (source.getName().equals("empty")) ? "Start" : source.getName(); //empty name means we are connected to some control node. Only valid possibility is ActivityInitialNode
            jndiContext.unbind("Fork from:" + name);
            jndiContext.bind("Fork from:" + name, new ControlNodeBean(controlNode));
            taskNames.add("Fork from:" + name);
        } else if (controlNode instanceof Join){
            // do not register any edges for join node except the node itself
            ActivityNode target = controlNode.getOutgoing().get(0).getTarget();
            String name = (target.getName().equals("empty")) ? "Stop" : target.getName();  //empty name means we are connected to some control node. Only valid possibility is ActivityFinalNode
            jndiContext.unbind("Join to:" + name);
            jndiContext.bind("Join to:" + name, new ControlNodeBean(controlNode));
            taskNames.add("Join to:" + name);
        }
        return taskNames;
    }

    private static ArrayList<String> registerObjectEdges(JndiContext jndiContext, ObjectNode object, String name) throws NamingException {
        // rules for registering edges of object node are:
        // - register incoming edges if source is object node or fork
        // - register outgoing edges if target is join

        ArrayList<String> taskNames = new ArrayList<String>();

        ArrayList<ActivityEdge> outgoing = object.getOutgoing();
        ArrayList<ActivityEdge> incoming = object.getIncoming();
        boolean isJoinOrFork = false;
        if (incoming.size() > 0) {
            isJoinOrFork = (incoming.get(0).getSource() instanceof Fork) || (incoming.get(0).getSource() instanceof Join);
        }
        // register incoming
        if (isJoinOrFork){
            jndiContext.unbind(name + ":dataIN");
            jndiContext.bind(name + ":dataIN", new ActivityEdgeBean(incoming.get(0)));
            taskNames.add(name + ":dataIN");
        }
        // register outgoing
        if ((outgoing.size() > 0) && (outgoing.get(0).getSource() instanceof Join)){
            jndiContext.unbind(name + ":dataOUT");
            jndiContext.bind(name + ":dataOUT", new ActivityEdgeBean(outgoing.get(0)));
            taskNames.add(name + ":dataOUT");
        }
        return taskNames;
    }


    private static ArrayList<String> registerActionEdges(JndiContext jndiContext, Action a, String actionName) throws NamingException {
        // rules for registering edges of actions are:
        // - register all incoming edges
        // - register all outgoing data edges
        // - register outgoing control edges unless next target is another action
        // TODO I consider one edge of each kind: control and data in, control and data out. Update to handle multiple in/out control/data edges if neccessary

        ArrayList<String> taskNames = new ArrayList<String>();

        ArrayList<ActivityEdge> objectsIn = a.getControlOrObjectFlowEdges(false, ActivityNode.Direction.IN);
        ArrayList<ActivityEdge> objectsOut = a.getControlOrObjectFlowEdges(false, ActivityNode.Direction.OUT);
        ActivityEdge controlIn = a.getControlOrObjectFlowEdges(true, ActivityNode.Direction.IN).get(0);
        ActivityEdge controlOut = a.getControlOrObjectFlowEdges(true, ActivityNode.Direction.OUT).get(0);
        //register all incoming edges
        jndiContext.unbind(actionName + ":controlIN");
        jndiContext.bind(actionName + ":controlIN", new ActivityEdgeBean(controlIn));
        taskNames.add(actionName + ":controlIN");
        if (objectsIn.size() > 0) {
            ActivityEdge dataIn = objectsIn.get(0);
            jndiContext.unbind(actionName + ":dataIN");
            jndiContext.bind(actionName + ":dataIN", new ActivityEdgeBean(dataIn));
            taskNames.add(actionName + ":dataIN");
        }
        // register all outgoing data edges
        if (objectsOut.size() > 0) {
            ActivityEdge dataOut = objectsOut.get(0);
            jndiContext.unbind(actionName + ":dataOUT");
            jndiContext.bind(actionName + ":dataOUT", new ActivityEdgeBean(dataOut));
            taskNames.add(actionName + ":dataOUT");
        }
        // register outgoing control edges unless next target is another action
        if (!(controlOut.getTarget() instanceof Action)) {
            jndiContext.unbind(actionName + ":controlOUT");
            jndiContext.bind(actionName + ":controlOUT", new ActivityEdgeBean(controlOut));
            taskNames.add(actionName + ":controlOUT");
        }
        return taskNames;
    }

}
