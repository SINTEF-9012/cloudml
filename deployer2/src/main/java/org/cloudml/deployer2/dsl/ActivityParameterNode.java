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

/**
 * Created by Maksym on 16.03.2015.
 */
public class ActivityParameterNode extends ObjectNode {
    //TODO add notation of exceptions meaning that some parameters will be filled with data only on exception

    private Object parameter;

    public ActivityParameterNode(String name) throws Exception {
        super(name);
        setObjects(null);
    }

    public ActivityParameterNode(String name, Object parameter) throws Exception {
        super(name);
        setParameter(parameter);
        setObjects(null);
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
        } else if (direction.equals(Direction.IN)) {
            if (getOutgoing().size() != 0) {
                throw new Exception("ActivityParameters can not have both incoming and outgoing object flows:it already has outgoing flow");
            } else if (getIncoming().size() == 1) {
                tooManyEdgesException();
            }
            dataFlow.setTarget(this);
            getIncoming().add(dataFlow);
        } else {
            if (getIncoming().size() != 0) {
                throw new Exception("ActivityParameters can not have both incoming and outgoing object flows:it already has incoming flow");
            } else if (getOutgoing().size() == 1) {
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
