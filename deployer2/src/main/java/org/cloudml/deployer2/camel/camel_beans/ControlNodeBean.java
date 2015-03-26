package org.cloudml.deployer2.camel.camel_beans;

import org.cloudml.deployer2.dsl.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Maksym on 25.03.2015.
 */
public class ControlNodeBean {
    private static final Logger journal = Logger.getLogger(ObjectNodeBean.class.getName());
    ControlNode node;

    public ControlNodeBean(ControlNode node){
        this.node = node;
    }

    public void execute(){
        if (node instanceof Fork){
            journal.log(Level.INFO, "Inside fork with next targets:");
            for (ActivityEdge o:node.getOutgoing()){
                journal.log(Level.INFO, "- " + o.getTarget().getName());
            }
        } else if (node instanceof Join){
            journal.log(Level.INFO, "Inside join which synchronizes:");
            for (ActivityEdge o:node.getOutgoing()){
                journal.log(Level.INFO, "- " + o.getTarget().getName());
            }
        } else if (node instanceof ActivityInitialNode){
            journal.log(Level.INFO, "Starting deployment");
        } else {
            journal.log(Level.INFO, "Deployment has finished successfully");
        }
    }
}
