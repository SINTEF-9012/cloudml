package org.cloudml.deployer2.dsl;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;

public class ExpansionRegionTest {
    ExpansionRegion region = new ExpansionRegion("", ExpansionRegion.ExpansionMode.PARALLEL);
    ExpansionNode node;
    Object a = new Object();
    ArrayList<Object> objects = new ArrayList<Object>();

    @Test(expected = Exception.class)
    public void testRemoveInput() throws Exception {
        objects.add(a);
        objects.add(a);
        node = new ExpansionNode("", objects);
        region.addInput(node);
        region.removeInput(node); //exception because region has to have at least one input
    }

}