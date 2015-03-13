package dsl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maksym on 13.03.2015.
 */
public class ActivityNode {

    protected static final String IN = "in";
    protected static final String OUT = "out";

    private String name;
    protected List<ActivityEdge> incoming = new ArrayList<ActivityEdge>();
    protected List<ActivityEdge> outgoing = new ArrayList<ActivityEdge>();

    // constructors
    public ActivityNode (){}

    public ActivityNode (String name){
        this.setName(name);
    }

    public ActivityNode (String name, ArrayList<ActivityEdge> incoming, ArrayList<ActivityEdge> outgoing){
        this.setName(name);
        this.setIncoming(incoming);
        this.setOutgoing(outgoing);
    }

    // methods
    public void addEdge (ActivityEdge edge, String direction) throws Exception {
        if (direction.equals(IN)){
            edge.setTarget(this);
            getIncoming().add(edge);
        } else if (direction.equals(OUT)){
            edge.setSource(this);
            getOutgoing().add(edge);
        }
    }

    public void removeEdge (ActivityEdge edge, String direction){
        if (direction.equals(IN)){
            getIncoming().remove(edge);
            edge.setTarget(null);
        } else if (direction.equals(OUT)){
            getOutgoing().remove(edge);
            edge.setSource(null);
        }
    }


    public ArrayList<ActivityEdge> getControlOrObjectFlowEdges(boolean control, String direction){
        ArrayList<ActivityEdge> result = new ArrayList<ActivityEdge>();
        if (direction.equals(IN)) {
            if (control){
                // retrieve all incoming control edges
                for(ActivityEdge edge:getIncoming()){
                    if (!edge.isObjectFlow()){
                        result.add(edge);
                    }
                }
            } else {
                // retrieve all incoming object edges
                for(ActivityEdge edge:getIncoming()){
                    if (edge.isObjectFlow()){
                        result.add(edge);
                    }
                }
            }
        } else if (direction.equals(OUT)){
            if (control){
                // retrieve all outgoing control edges
                for(ActivityEdge edge:getOutgoing()){
                    if (!edge.isObjectFlow()){
                        result.add(edge);
                    }
                }
            } else {
                // retrieve all outgoing object edges
                for(ActivityEdge edge:getOutgoing()){
                    if (edge.isObjectFlow()){
                        result.add(edge);
                    }
                }
            }
        }
        return result;
    }

    // setters and getters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
