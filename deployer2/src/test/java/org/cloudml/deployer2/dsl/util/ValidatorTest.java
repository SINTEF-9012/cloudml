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
import org.cloudml.deployer2.dsl.util.Validator;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.fail;

/**
 * Created by Maksym on 18.03.2015.
 */
public class ValidatorTest {
    ExpansionRegion region = new ExpansionRegion("", ExpansionRegion.ExpansionMode.PARALLEL);
    Object a = new Object();
    ArrayList<Object> objects = new ArrayList<Object>();
    ArrayList<Object> objects2 = new ArrayList<Object>();
    ExpansionNode node;
    ExpansionNode node2;

    @Test
    public void testAllEdgesAreControlOrObjectFlows() {
        ActivityEdge control = new ActivityEdge();
        ActivityEdge object = new ActivityEdge(true);

        ArrayList<ActivityEdge> list = new ArrayList<ActivityEdge>();

        list.add(control);
        list.add(object);

        try {
            Validator.allEdgesAreControlOrObjectFlows(list, control);
            fail("Exception is expected because forkJoin list has both control and object flow edges");
        } catch (Exception e) {}

        list.remove(object);
        list.add(control);

        try {
            Validator.allEdgesAreControlOrObjectFlows(list, object);
            fail("Exception is expected because forkJoin list consists of control flow edges and inOut edge is an object flow");
        } catch (Exception e) {}
    }

    @Test
    public void testValidateOutputSize(){
        objects.add(a);
        objects.add(a);
        objects.add(a);

        try {
            node = new ExpansionNode("", objects);
            // add input node for size comparison
            region.addOutput(node);
            fail("Exception is expected as we need to declare inputs before adding any outputs");
        } catch (Exception e) {}

        objects2.add(a);
        objects2.add(a);

        try {
            region.addInput(node);
            node2 = new ExpansionNode("", objects2);
            // it's fine to add output node smaller than input
            region.addOutput(node2);
            objects2.add(a);
            objects2.add(a);
            ExpansionNode node3 = new ExpansionNode("", objects2);
            region.addOutput(node3);
            fail("Exception is expected as output collection can not be bigger than input collections");
        } catch (Exception e) {}

    }

    @Test(expected = Exception.class)
    public void testValidateInputSize() throws Exception {
        objects.add(a);
        objects.add(a);
        node = new ExpansionNode("", objects);
        // no nodes yet, so size doesn't matter
        region.addInput(node);

        objects2.add(a);
        objects2.add(a);
        objects2.add(a);
        node2 = new ExpansionNode("", objects2);
        region.addInput(node2); //exception here because new node differs in size from existing ones
    }


}
