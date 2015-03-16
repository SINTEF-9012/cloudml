package dsl;

/**
 * Created by Maksym on 13.03.2015.
 */
public class ActivityInitialNode extends ControlNode{

    public ActivityInitialNode(){
        super();
        this.incoming = null;
    }


    @Override
    public void addEdge(ActivityEdge edge, String direction) throws Exception{
        if(direction.matches(IN)){
            throw new Exception("Initial node can not have incoming edges");
        } else if (edge.isObjectFlow()){
            throw new Exception("No object flow from initial node is allowed");
        } else {
            edge.setSource(this);
            getOutgoing().add(edge);
        }
    }
}
