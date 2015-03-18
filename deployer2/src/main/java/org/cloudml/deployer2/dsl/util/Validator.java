package org.cloudml.deployer2.dsl.util;

import org.cloudml.deployer2.dsl.ActivityEdge;
import org.cloudml.deployer2.dsl.ExpansionNode;
import org.cloudml.deployer2.dsl.ExpansionRegion;

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

    public static void validateOutputSize(ExpansionNode node, ExpansionRegion region) throws Exception {
        if (region.getInputCollections().size() == 0){
            throw new Exception("First declare inputs as outputs size depends on inputs size");
        } else {
            int maxSize = region.getInputCollections().get(0).getObjects().size();
            if (node.getObjects().size() > maxSize) {
                throw new Exception("Output collection can not be bigger than input collections");
            }
        }
    }

    public static void validateInputSize(ExpansionNode node, ExpansionRegion region) throws Exception{
        if (region.getInputCollections().size() == 0){
        } else {
            int size = region.getInputCollections().get(0).getObjects().size();
            if (node.getObjects().size() != size) {
                throw new Exception("New collection must be of the same size as existing collections");
            }
        }
    }

}
