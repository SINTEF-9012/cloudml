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
 * Request detailed information about a selected artefact type
 * 
 * @author Franck Chauvel
 * @since 1.0
 */
public class ViewArtefactType extends ManageableCommand {

    private final String artefactTypeId;
    
    
    /**
     * Create a new ViewArtefactType from the ID of the artefact type whose 
     * details are needed
     * @param artefactTypeId the ID of the needed artefact type.
     */
    public ViewArtefactType(CommandHandler handler, final String artefactTypeId) {
        super(handler);
        this.artefactTypeId = artefactTypeId;
    }
    
    
    /**
     * @return the ID of the needed artefact type.
     */
    public String getArtefactTypeId() {
        return this.artefactTypeId;
    }
    
    @Override
    public void execute(CommandHandler handler) {
        handler.handle(this);
    }
    
}
