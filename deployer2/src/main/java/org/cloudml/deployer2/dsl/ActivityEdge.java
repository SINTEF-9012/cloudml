package org.cloudml.deployer2.dsl;

import org.cloudml.core.NamedElement;

/**
 * Created by Maksym on 13.03.2015.
 */
public class ActivityEdge extends Element {
    //TODO maybe add edge guard and edge weight. Also object flow has multicast and multireceive parameters, may be useful later

    private ActivityNode source;
    private ActivityNode target;
    // by default all edges are control edges
    private boolean objectFlow = false;


    // constructors
    public ActivityEdge() {
    }

    public ActivityEdge(boolean objectFlow) {
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

    // three methods to describe edge
    @Override
    public String toString(){
        String target = "";
        String source = "";
        if (getTarget() != null){
            target = " to " + getEdgeDescription(getTarget());
        }
        if (getSource() != null){
            source =  " from " + getEdgeDescription(getSource());
        }
        if (isObjectFlow())
                return "Data flow" + source + target;
        return "Control flow" + source + target;

    }

    private String getEdgeDescription(ActivityNode node) {
        String targetString;
        if (node instanceof Action) {
            targetString = "(" + node.getClass().getSimpleName()
                    + ":" + node.getName() + actionInputName(node) + ")";
        } else {
            targetString = "(" + node.getClass().getSimpleName()
                    + ":" + node.getName() + ")";
        }
        return targetString;
    }

    private String actionInputName(ActivityNode node){
        return "_" + ((NamedElement)((Action) node).getInputs().get(0)).getName();
    }

}
