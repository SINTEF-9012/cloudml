package org.cloudml.deployer2.camel.camel_beans;

import org.cloudml.deployer2.dsl.ActivityEdge;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Maksym on 25.03.2015.
 */
public class ActivityEdgeBean {
    private static final Logger journal = Logger.getLogger(ObjectNodeBean.class.getName());
    ActivityEdge edge;

    public ActivityEdgeBean(ActivityEdge edge){
        this.edge = edge;
    }

    public void execute(){
        if (edge.isObjectFlow()){
            journal.log(Level.INFO, "Passing data from " + edge.getSource().getName() + " to " + edge.getTarget().getName());
        } else {
            journal.log(Level.INFO, "Moving from " + edge.getSource().getName() + " to " + edge.getTarget().getName());
        }
    }
}
