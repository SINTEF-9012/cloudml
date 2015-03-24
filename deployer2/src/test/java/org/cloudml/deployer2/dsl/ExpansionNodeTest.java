package org.cloudml.deployer2.dsl;

import org.junit.Test;

import java.util.ArrayList;

public class ExpansionNodeTest {

    @Test(expected = Exception.class)
    public void minimumTwoObjects() throws Exception{
        ArrayList<Object> collection = new ArrayList<Object>();
        collection.add("single object");

        ExpansionNode node = new ExpansionNode("", collection); //exception here because arraylist has only one object
    }

}