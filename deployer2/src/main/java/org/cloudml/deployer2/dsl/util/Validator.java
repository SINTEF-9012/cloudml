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
