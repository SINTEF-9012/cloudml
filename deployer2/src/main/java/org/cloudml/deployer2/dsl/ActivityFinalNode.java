package org.cloudml.deployer2.dsl;

/**
 * Created by Maksym on 13.03.2015.
 */
public class ActivityFinalNode extends ControlNode {

    public ActivityFinalNode() {
        super();
        this.outgoing = null;

    }

    @Override
    public void addEdge(ActivityEdge edge, Direction direction) throws Exception {
        if (direction.equals(Direction.OUT)) {
            throw new Exception("Final node can not have outgoing edges");
        } else if (edge.isObjectFlow()) {
            throw new Exception("No object flow to final node is allowed");
        } else {
            edge.setTarget(this);
            getIncoming().add(edge);
        }
    }

    public void finish() {
        //TODO change code to zero
        System.exit(1);
    }

}
