package org.cloudml.deployer2.dsl;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import static org.junit.Assert.fail;


public class ForkTest {

    ActivityEdge control = new ActivityEdge();
    ArrayList<ActivityEdge> list = new ArrayList<ActivityEdge>();


    @Test(expected = Exception.class)
    public void hasOneInputEdge() throws Exception {
        list.add(control);
        Fork fork = new Fork(control, list);
        fork.addEdge(control, ActivityNode.Direction.IN); //exception is thrown here
    }


}