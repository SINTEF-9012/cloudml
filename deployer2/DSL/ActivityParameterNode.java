package dsl;

import java.util.HashMap;

/**
 * Created by Maksym on 16.03.2015.
 */
public class ActivityParameterNode extends ObjectNode {
    //TODO add notation of exceptions meaning that some parameters will be filled with data only on exception

    private Object parameter;

    public ActivityParameterNode(String name, Object parameter) {
        super(name);
        setParameter(parameter);
    }

    public ActivityParameterNode(String name, Object parameter, ActivityEdge data, Direction direction) {
        super(name);
        try {
            setParameter(parameter);
            addEdge(data, direction);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void addEdge(ActivityEdge dataFlow, Direction direction) throws Exception {
        if (!dataFlow.isObjectFlow()) {
            throw new Exception("Only object flow to/from object node is allowed: you are trying to add control flow");
        } else if (direction.equals(Direction.IN)){
            if(getOutgoing().size() != 0){
                throw new Exception("ActivityParameters can not have both incoming and outgoing object flows:it already has outgoing flow");
            } else if (getIncoming().size() == 1){
                tooManyEdgesException();
            }
            dataFlow.setTarget(this);
            getIncoming().add(dataFlow);
        } else {
            if(getIncoming().size() != 0){
                throw new Exception("ActivityParameters can not have both incoming and outgoing object flows:it already has incoming flow");
            } else if (getOutgoing().size() == 1){
                tooManyEdgesException();
            }
            dataFlow.setSource(this);
            getOutgoing().add(dataFlow);
        }
    }

    private void tooManyEdgesException() throws Exception {
        throw new Exception("ActivityParameters may have only one edge, if you want to share data with many tasks connect ActivityParameterNode to DatastoreNode");
    }

    public Object getParameter() {
        return parameter;
    }

    public void setParameter(Object parameter) {
        this.parameter = parameter;
    }
}
