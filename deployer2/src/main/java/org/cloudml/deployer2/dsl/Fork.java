package org.cloudml.deployer2.dsl;

import org.cloudml.deployer2.dsl.util.Validator;

import java.util.ArrayList;

/**
 * Created by Maksym on 13.03.2015.
 */
public class Fork extends ControlNode {

    public Fork(ActivityEdge incoming, ArrayList<ActivityEdge> outgoing) throws Exception {
        super();
        //validate that all edges are of the same nature - UML semantics
        Validator.allEdgesAreControlOrObjectFlows(outgoing, incoming);
        this.setOutgoing(outgoing);
        // set incoming edge and protect incoming edges list from being expanded
        this.addEdge(incoming, Direction.IN);
    }

    @Override
    public void addEdge(ActivityEdge edge, Direction direction) throws Exception {
        if (direction.equals(Direction.IN)) {
            if (getIncoming().size() == 1){
                throw new Exception("Fork node can not have more than one incoming edge");
            }
            edge.setTarget(this);
            getIncoming().add(edge);
        } else {
            edge.setSource(this);
            getOutgoing().add(edge);
        }
    }
}
