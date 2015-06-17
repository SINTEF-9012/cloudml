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

import org.cloudml.deployer2.dsl.util.Validator;

import java.util.ArrayList;

/**
 * Created by Maksym on 13.03.2015.
 */
public class Join extends ControlNode {
    boolean isDataJoin = false;

    public Join(ArrayList<ActivityEdge> incoming, ActivityEdge outgoing) throws Exception {
        super();
        //validate that all edges are of the same nature - UML semantics
        Validator.allEdgesAreControlOrObjectFlows(incoming, outgoing);
        this.setIncoming(incoming);
        this.addEdge(outgoing, Direction.OUT);
    }

    public Join(boolean isDataJoin){
        this.isDataJoin = isDataJoin;
    }

    @Override
    public void addEdge(ActivityEdge edge, Direction direction) throws Exception {
        if (edge.isObjectFlow() != isDataJoin) {
            String joinType = (isDataJoin == true) ? "data Join" : "control Join";
            throw new Exception("This join is " + joinType + ", but edge is not. Connection not possible");
        }
        if (direction.equals(Direction.IN)) {
            edge.setTarget(this);
            getIncoming().add(edge);
        } else {
            if (getOutgoing().size() == 1){
                throw new Exception("Join node can not have more than one outgoing edge");
            }
            edge.setSource(this);
            getOutgoing().add(edge);
        }
    }

    public boolean isDataJoin() {
        return isDataJoin;
    }

    public void setDataJoin(boolean isDataJoin) {
        this.isDataJoin = isDataJoin;
    }


}
