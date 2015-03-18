package org.cloudml.deployer2.dsl;

import org.cloudml.deployer2.dsl.util.Validator;

import java.util.ArrayList;

/**
 * Created by Maksym on 13.03.2015.
 */
public class Join extends ControlNode {

    public Join(ArrayList<ActivityEdge> incoming, ActivityEdge outgoing) throws Exception {
        super();
        //validate that all edges are of the same nature - UML semantics
        Validator.allEdgesAreControlOrObjectFlows(incoming, outgoing);
        this.setIncoming(incoming);
        // set outgoing edge and protect outgoing edges list from being expanded
        this.addEdge(outgoing, Direction.OUT);
    }

    @Override
    public void addEdge(ActivityEdge edge, Direction direction) throws Exception {
        if (direction.equals(Direction.IN)) {
            edge.setTarget(this);
            getIncoming().add(edge);
        } else {
            if (getOutgoing().size() == 1){
                throw new Exception("Join node can not have more than one outgoing edge");
            }
            edge.setSource(this);
            getOutgoing().add(edge);
        }
    }


}
