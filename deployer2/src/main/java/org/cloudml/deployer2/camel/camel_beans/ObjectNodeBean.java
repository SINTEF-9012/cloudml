package org.cloudml.deployer2.camel.camel_beans;

import org.cloudml.deployer2.dsl.ActivityParameterNode;
import org.cloudml.deployer2.dsl.DatastoreNode;
import org.cloudml.deployer2.dsl.ExpansionNode;
import org.cloudml.deployer2.dsl.ObjectNode;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Maksym on 25.03.2015.
 */
public class ObjectNodeBean {
    private static final Logger journal = Logger.getLogger(ObjectNodeBean.class.getName());
    private ObjectNode node;

    public ObjectNodeBean(ObjectNode node){
        this.node = node;
    }

    public void execute(){
        if (node instanceof DatastoreNode){
            journal.log(Level.INFO, "Inside datastore " + node.getName() + " which contains:");
            for (Object o:node.getObjects()){
                journal.log(Level.INFO, "- " + o.getClass().getSimpleName());
            }
        } else if (node instanceof ActivityParameterNode){
            journal.log(Level.INFO, "Inside activity parameter node " + node.getName() + " which holds " +
                    ((ActivityParameterNode) node).getParameter().getClass().getSimpleName());
        } else if (node instanceof ExpansionNode){
            journal.log(Level.INFO, "Inside expansion node " + node.getName() + ", collection size is: " + node.getObjects().size());
        } else {
            journal.log(Level.INFO, "Inside object node " + node.getName() + " which contains " + node.getObjects().size() + " objects");
        }
    }
}
