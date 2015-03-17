package dsl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maksym on 13.03.2015.
 */
public abstract class ActivityNode extends Element{

    public enum Direction {IN, OUT};

    protected List<ActivityEdge> incoming = new ArrayList<ActivityEdge>();
    protected List<ActivityEdge> outgoing = new ArrayList<ActivityEdge>();

    // constructors
    public ActivityNode (){}

    public ActivityNode (String name){
        super(name);
    }

    public ActivityNode (String name, ArrayList<ActivityEdge> incoming, ArrayList<ActivityEdge> outgoing){
        super(name);
        this.setIncoming(incoming);
        this.setOutgoing(outgoing);
    }

    // methods
    public void addEdge (ActivityEdge edge, Direction direction) throws Exception {
        if (direction.equals(Direction.IN)){
            edge.setTarget(this);
            getIncoming().add(edge);
        } else {
            edge.setSource(this);
            getOutgoing().add(edge);
        }
    }

    public void removeEdge (ActivityEdge edge, Direction direction){
        if (direction.equals(Direction.IN)){
            getIncoming().remove(edge);
            edge.setTarget(null);
        } else {
            getOutgoing().remove(edge);
            edge.setSource(null);
        }
    }


    // setters and getters

    public ArrayList<ActivityEdge> getIncoming() {
        return (ArrayList<ActivityEdge>) incoming;
    }

    public void setIncoming(ArrayList<ActivityEdge> incoming) {
        for(ActivityEdge edge:incoming){
            edge.setTarget(this);
        }
        this.incoming = incoming;
    }

    public ArrayList<ActivityEdge> getOutgoing() {
        return (ArrayList<ActivityEdge>) outgoing;
    }

    public void setOutgoing(ArrayList<ActivityEdge> outgoing) {
        for(ActivityEdge edge:outgoing){
            edge.setSource(this);
        }
        this.outgoing = outgoing;
    }
}
