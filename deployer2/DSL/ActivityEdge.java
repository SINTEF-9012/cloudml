package dsl;

/**
 * Created by Maksym on 13.03.2015.
 */
public class ActivityEdge extends Element{

    private ActivityNode source;
    private ActivityNode target;
    private boolean objectFlow = false;


    // constructors

    // by default all edges are control edges
    public ActivityEdge(){
    }

    public ActivityEdge(boolean objectFlow){
        this.setObjectFlow(objectFlow);
    }


    // setters and getters
    public ActivityNode getSource() {
        return source;
    }

    public void setSource(ActivityNode source) {
        this.source = source;
    }

    public ActivityNode getTarget() {
        return target;
    }

    public void setTarget(ActivityNode target) {
        this.target = target;
    }

    public boolean isObjectFlow() {
        return objectFlow;
    }

    public void setObjectFlow(boolean objectFlow) {
        this.objectFlow = objectFlow;
    }
}
