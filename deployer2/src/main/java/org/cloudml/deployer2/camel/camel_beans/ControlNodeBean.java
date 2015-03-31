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

        node.getProperties().put("Status", String.valueOf(Element.Status.ACTIVE));

        String message;
        int index = 1;
        if (node instanceof Fork){
            message = "Inside fork with next targets:\n";
            for (ActivityEdge o:node.getOutgoing()){
                message += "- " + o.getTarget().getName() + " " + index + "\n";
                index++;
            }
            journal.log(Level.INFO, message);
        } else if (node instanceof Join){
            message = "Inside join which synchronizes:\n";
            for (ActivityEdge o:node.getIncoming()){
                message += "- " + o.getSource().getName() + " " + index + "\n";
                index++;
            }
            journal.log(Level.INFO, message);
        } else if (node instanceof ActivityInitialNode){
            journal.log(Level.INFO, "Starting deployment");
        } else {
            journal.log(Level.INFO, "Deployment has finished successfully");
            ((ActivityFinalNode)node).finish();
        }

        node.getProperties().put("Status", String.valueOf(Element.Status.DONE));
    }
}
