package org.cloudml.deployer2.dsl;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;

public class ExpansionNodeTest {

    @Test(expected = Exception.class)
    public void minimumTwoObjects() throws Exception{
        ArrayList<String> collection = new ArrayList<String>();
        collection.add("single object");

        ExpansionNode node = new ExpansionNode("", collection); //exception here because arraylist has only one object
    }

}