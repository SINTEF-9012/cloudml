package org.cloudml.deployer2.dsl;

import java.util.ArrayList;

/**
 * Created by Maksym on 17.03.2015.
 */
public class Validator {

    public static void allEdgesAreControlOrObjectFlows(ArrayList<ActivityEdge> forkJoin, ActivityEdge inOut) throws Exception {
        boolean isObjectFlow = inOut.isObjectFlow();
        for (ActivityEdge edge:forkJoin){
            if(edge.isObjectFlow() != isObjectFlow){
                throw new Exception("The edges coming into and out of a fork/join node must be either all object flows or all control flows");
            }
        }
    }
}
