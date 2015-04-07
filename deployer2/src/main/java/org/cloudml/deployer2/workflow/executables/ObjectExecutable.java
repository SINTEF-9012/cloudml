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
public class ObjectExecutable {
    private static final Logger journal = Logger.getLogger(ObjectExecutable.class.getName());
    private ObjectNode node;

    public ObjectExecutable(ObjectNode node){
        this.node = node;
    }

    public void execute(){

        node.getProperties().put("Status", String.valueOf(Element.Status.ACTIVE));

        if (node instanceof DatastoreNode){
            journal.log(Level.INFO, "Inside datastore " + node.getName() + " which contains:");
            for (Object o:node.getObjects()){
                journal.log(Level.INFO, "- " + o.getClass().getSimpleName());
            }
        } else if (node instanceof ActivityParameterNode){
            journal.log(Level.INFO, "Inside activity parameter node " + node.getName() + " which holds " +
                    ((ActivityParameterNode) node).getParameter().getClass().getSimpleName());
        } else if (node instanceof ExpansionNode){
            journal.log(Level.INFO, "Inside expansion node " + node.getName() + ", collection size is: " + node.getObjects().size());
        } else {
            String message = "Inside object node " + node.getName() + " which contains " + node.getObjects().size() + " objects:\n";
            for (Object obj:node.getObjects()){
                if (obj instanceof String){
                    message += (String)obj + "\n";
                } else {
                    //TODO if you can think of other objects which are not String, add output logic here
                }
            }
            journal.log(Level.INFO, message);
        }

        node.getProperties().put("Status", String.valueOf(Element.Status.DONE));
    }
}
