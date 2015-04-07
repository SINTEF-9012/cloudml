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
 * Created by Maksym on 13.03.2015.
 */
public class ActivityFinalNode extends ControlNode {

    public ActivityFinalNode() {
        super();
        this.outgoing = null;

    }

    @Override
    public void addEdge(ActivityEdge edge, Direction direction) throws Exception {
        if (direction.equals(Direction.OUT)) {
            throw new Exception("Final node can not have outgoing edges");
        } else if (edge.isObjectFlow()) {
            throw new Exception("No object flow to final node is allowed");
        } else {
            edge.setTarget(this);
            getIncoming().add(edge);
        }
    }

    public void finish() {
        System.exit(0);
    }

}
