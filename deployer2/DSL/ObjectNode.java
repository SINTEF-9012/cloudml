package dsl;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Maksym on 16.03.2015.
 */
public class ObjectNode extends ActivityNode {
    //TODO add isControlNode parameter which means that object node may act as control node if needed.
    //TODO Also object ordering mode may be added: FIFO, LIFO, unordered, ordered. And object state.

    protected ArrayList<? extends Object> objects = new ArrayList<Object>();
    private long upperBound = Long.MAX_VALUE;

    public ObjectNode(String name){
        super(name);
    }

    public ObjectNode(String name, long upperBound) {
        super(name);
        setUpperBound(upperBound);
    }

    @Override
    public void addEdge(ActivityEdge dataFlow, Direction direction) throws Exception {
        if (!dataFlow.isObjectFlow()) {
            throw new Exception("Only object flow to/from object node is allowed: you are trying to add control flow");
        } else if (direction.equals(Direction.IN)) {
            dataFlow.setTarget(this);
            getIncoming().add(dataFlow);
        } else {
            dataFlow.setSource(this);
            getOutgoing().add(dataFlow);
        }
    }

    // setters and getters
    public ArrayList<? extends Object> getObjects() {
        return objects;
    }

    public void setObjects(ArrayList<? extends Object> objects) {
        this.objects = objects;
    }

    public long getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(long upperBound) {
        this.upperBound = upperBound;
    }
}
