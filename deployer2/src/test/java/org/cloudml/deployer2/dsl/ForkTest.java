package org.cloudml.deployer2.dsl;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import static org.junit.Assert.fail;


public class ForkTest {
    ActivityEdge control = new ActivityEdge();
    ActivityEdge object = new ActivityEdge();

    ArrayList<ActivityEdge> list = new ArrayList<ActivityEdge>();


    @Test(expected = Exception.class)
    public void hasOneInputEdge() throws Exception {
        list.add(control);
        Fork fork = new Fork(control, list);
        //fork.removeEdge(inEdge, ActivityNode.Direction.IN);
        fork.addEdge(control, ActivityNode.Direction.IN);
    }

    @Test
    public void allEdgesAreOfTheSameNature() {
        //TODO wy it doesn't throw exception upon creation of fork?
        list.add(control);
        list.add(control);
        try {
            Fork fork2 = new Fork(control, list);
            fail("Exception is expected");
        } catch (Exception e) {
        }


    }
}