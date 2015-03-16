package dsl;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Maksym on 13.03.2015.
 */
public class Main {
    public static void main(String[] args){

        ActivityEdge controlFlow = new ActivityEdge();
        ActivityEdge objectFlow = new ActivityEdge(true);

        ArrayList<ActivityEdge> list = new ArrayList<ActivityEdge>();
        list.add(controlFlow);
        list.add(controlFlow);

        // a few different objects to pass as data
        int[] integers = {2,3,5};
        String data = "Pass me in and then throw me out";

        HashMap<String, Object> actionInput = new HashMap<String, Object>();
        actionInput.put("Int", integers);
        actionInput.put("Str", data);
        ObjectNode objectInput = new ObjectNode("input");
        objectInput.setInputs(actionInput);

        HashMap<String, Object> actionOutput = new HashMap<String, Object>();
        actionOutput.put("Int", integers);
        ObjectNode objectOutput = new ObjectNode("output");
        objectOutput.setOutputs(actionOutput);

        ArrayList<ExpansionNode> expNodes = new ArrayList<ExpansionNode>();
        ExpansionNode exp1 = new ExpansionNode("exp1", actionInput);
        ExpansionNode exp2 = new ExpansionNode("exp2", actionOutput);
        expNodes.add(exp1);
        expNodes.add(exp1);


        try {

            // test action node if we can add incoming and outgoing edges + passing data
            Action ac = new Action("test", list, list);
            ac.addEdge(controlFlow, "in");
            ac.addEdge(controlFlow, "out");
            ac.setInput(objectInput);

            // test fork node to check that we can not add more than one incoming edge
            Fork fork = new Fork(controlFlow, list);
            fork.addEdge(controlFlow, "out"); // change to in to catch exception

            // test join node to check that we can not add more than one outgoing edge
            Join join = new Join(list, controlFlow);
            join.addEdge(controlFlow, "in"); // change to out to catch exception

            //test initial node, it must not have any incoming edges and no data edges
            ActivityInitialNode initial = new ActivityInitialNode();
            initial.getIncoming(); // add ".size()" to catch null pointer exception
            initial.addEdge(controlFlow, "out"); // change to in to catch exception
            initial.addEdge(controlFlow, "out"); // change first parameter to "objectFlow" to catch exception, no object flow is allowed

            //test ActivityParameter
            ActivityParameterNode parameterNode = new ActivityParameterNode("Model",integers);
            parameterNode.addEdge(objectFlow,"in"); // change first parameter to "controlFlow" to catch exception, no control flow is allowed
            // only one edge is allowed: parameterNode.addEdge(objectFlow,"in");
            // only in or out flows are allowed: parameterNode.addEdge(objectFlow,"out");

            //create activity with all node and edges
            ArrayList<ActivityNode> allNodes = new ArrayList<ActivityNode>();
            allNodes.add(ac);
            allNodes.add(fork);
            allNodes.add(join);
            allNodes.add(initial);
            allNodes.add(parameterNode);
            Activity container = new Activity(list,allNodes);

            //test datastore node
            DatastoreNode global = new DatastoreNode("Global");
            global.addEdge(objectFlow, "in"); // change first parameter to "controlFlow" to catch exception, no control flow is allowed
            global.setInputs(actionInput);
            // input and output keys are different: global.setOutputs(actionOutput);
            actionOutput.put("Str", null);
            // input and output values are different: global.setOutputs(actionOutput);
            actionOutput.put("Str", data);
            System.out.println(actionInput.values().toString() + "   " + actionOutput.values().toString());
            global.setOutputs(actionOutput);

            //test ExpansionRegion
            ExpansionRegion region = new ExpansionRegion("Parallel", ExpansionRegion.ExpansionMode.PARALLEL);
            region.setInputCollections(expNodes);


            //test final node, it must not have outgoing edges and exit with some code
            ActivityFinalNode finalActivity = new ActivityFinalNode();
            finalActivity.getOutgoing(); // add ".size()" to catch null pointer exception
            finalActivity.addEdge(controlFlow, "in"); // change to out to catch exception
            finalActivity.addEdge(controlFlow, "in"); // change first parameter to "objectFlow" to catch exception, no object flow is allowed
            finalActivity.finish(); //must exit with some code without exceptions

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
