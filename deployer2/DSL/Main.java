package dsl;

import java.util.ArrayList;

/**
 * Created by Maksym on 13.03.2015.
 */
public class Main {
    public static void main(String[] args){

        ActivityEdge controlFlow = new ActivityEdge();
        ActivityEdge objectFlow = new ActivityEdge();
        objectFlow.setObjectFlow(true);

        ArrayList<ActivityEdge> list = new ArrayList<ActivityEdge>();
        list.add(controlFlow);
        list.add(controlFlow);

        try {

            // test action node if we can add incoming and outgoing edges
            Action ac = new Action("test", list, list);
            ac.addEdge(controlFlow, "in");
            ac.addEdge(controlFlow, "out");

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
