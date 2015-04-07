/**
 * This file is part of CloudML [ http://cloudml.org ]
 *
 * Copyright (C) 2012 - SINTEF ICT
 * Contact: Franck Chauvel <franck.chauvel@sintef.no>
 *
 * Module: root
 *
 * CloudML is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * CloudML is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with CloudML. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.cloudml.deployer2.dsl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Maksym on 16.03.2015.
 */
public class ObjectNode extends ActivityNode {
    //TODO add isControlNode parameter which means that object node may act as control node if needed.
    //TODO Also object ordering mode may be added: FIFO, LIFO, unordered, ordered. And object state.

    protected ArrayList<Object> objects = new ArrayList<Object>();
    private long upperBound = Long.MAX_VALUE;

    public ObjectNode(String name) {
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

    public void addObject(Object object){
        objects.add(object);
    }

    public void removeObject(Object object){
        if (objects.contains(object)){
            objects.remove(object);
        }
    }

    // setters and getters
    public ArrayList<? extends Object> getObjects() {
        return objects;
    }

    public void setObjects(ArrayList<Object> objects) throws Exception {
        this.objects = objects;
    }

    public long getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(long upperBound) {
        this.upperBound = upperBound;
    }
}
