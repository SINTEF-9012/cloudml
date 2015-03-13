package dsl;

/**
 * Created by Maksym on 13.03.2015.
 */
public class ActivityFinalNode extends ActivityNode{

    public ActivityFinalNode(){
        super();
        this.outgoing = null;

    }

    @Override
    public void addEdge(ActivityEdge edge, String direction) throws Exception{
        if(direction.equals(OUT)){
            throw new Exception("Final node can not have outgoing edges");
        } else if (edge.isObjectFlow()){
            throw new Exception("No object flow to final node is allowed");
        } else {
            edge.setTarget(this);
            getIncoming().add(edge);
        }
    }

    public void finish(){
        //TODO change code to zero
        System.exit(1);
    }

}
