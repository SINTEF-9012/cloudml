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
package org.cloudml.facade.commands;

/**
 * A request for the instantiation of a given type
 */
public class Instantiate extends CloudMlCommand {

    private final String typeId;

    private final String instanceId;

    /**
     * Create a new instantiation request from the type ID and the name to give
     * to the resulting instance.
     *
     * @param typeId the ID of the type to instantiate
     *
     * @param instanceId the name to give to the resulting instance
     */
    public Instantiate(final String typeId, final String instanceId) {
        this.typeId = typeId;
        this.instanceId = instanceId;
    }

    @Override
    public void execute(CommandHandler handler) {
        handler.handle(this);

    }

    /**
     * @return the typeId
     */
    public String getTypeId() {
        return typeId;
    }

    /**
     * @return the instanceId
     */
    public String getInstanceId() {
        return instanceId;
    }

    @Override
    public String toString() {
        return String.format("instantiate %s as %s", typeId, instanceId);
    }
    
    

}
