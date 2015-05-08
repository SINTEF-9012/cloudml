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
package org.cloudml.deployer2.workflow.executables;

import org.cloudml.deployer2.dsl.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Maksym on 25.03.2015.
 */
public class ControlExecutable {
    private static final Logger journal = Logger.getLogger(ObjectExecutable.class.getName());
    ControlNode node;

    public ControlExecutable(ControlNode node){
        this.node = node;
    }

    public void execute(){

        node.getProperties().put("Status", String.valueOf(Element.Status.ACTIVE));

        String message;
        if (node instanceof Fork){
            message = "\n\nInside fork with next parallel flows:\n";
            for (ActivityEdge o:node.getOutgoing()){
                message += "- " + o.toString() + "\n";
            }
            journal.log(Level.INFO, message);
        } else if (node instanceof Join){
            message = "\n\nInside join which synchronizes:\n";
            for (ActivityEdge o:node.getIncoming()){
                message += "- " + o.toString() + "\n";
            }
            journal.log(Level.INFO, message);
        } else if (node instanceof ActivityInitialNode){
            journal.log(Level.INFO, "\n\nStarting deployment\n");
        } else {
            journal.log(Level.INFO, "\n\nDeployment has finished successfully\n");
            ((ActivityFinalNode)node).finish();
        }

        node.getProperties().put("Status", String.valueOf(Element.Status.DONE));
    }
}
