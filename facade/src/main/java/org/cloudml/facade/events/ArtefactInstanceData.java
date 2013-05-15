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
package org.cloudml.facade.events;

import org.cloudml.core.Artefact;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.facade.commands.CloudMlCommand;

/**
 * A Data that contains a artefact instance
 *
 * @author Franck Chauvel
 * @since 1.0
 */
public class ArtefactInstanceData extends Data {

    private final ArtefactInstance artefact;

    /**
     * Create a new data which contains a given artefact
     * 
     * @param artefact the artefact to convey
     */
    public ArtefactInstanceData(final CloudMlCommand command, final ArtefactInstance artefact) {
        super(command);
        this.artefact = artefact;
    }

    /**
     * @return the artefact contained in this data
     */
    public ArtefactInstance getArtefactInstance() {
        return this.artefact;
    }

    @Override
    public void accept(EventHandler handler) {
        handler.handle(this);
    }
    
}
