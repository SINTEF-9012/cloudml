package org.cloudml.deployer2.dsl;

import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by Maksym on 18.03.2015.
 */
public class JoinTest {

    ActivityEdge control = new ActivityEdge();

    ArrayList<ActivityEdge> list = new ArrayList<ActivityEdge>();


    @Test(expected = Exception.class)
    public void hasOneOutputEdge() throws Exception {
        list.add(control);
        Join join = new Join(list, control);
        join.addEdge(control, ActivityNode.Direction.OUT); //exception is thrown here
    }

}
