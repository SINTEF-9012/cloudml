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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cloudml.facade.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.cloudml.core.Artefact;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.facade.commands.CloudMlCommand;

/**
 * Data object to convey a list of artefact instances
 * 
 * @author Franck Chauvel
 * @since 1.0
 */
public class ArtefactInstanceList extends Data {

    private final ArrayList<ArtefactInstance> artefacts;

    /**
     * Create a new event containing list of artefacts
     *
     * @param artefacts the list of artefacts
     */
    public ArtefactInstanceList(CloudMlCommand command, final Collection<ArtefactInstance> artefacts) {
        super(command);
        this.artefacts = new ArrayList<ArtefactInstance>(artefacts.size());
        this.artefacts.addAll(artefacts);
    }

    /**
     * @return the list of artefact instances
     */
    public List<ArtefactInstance> getArtefactInstances() {
        return Collections.unmodifiableList(this.artefacts);
    }

    @Override
    public void accept(EventHandler handler) {
        handler.handle(this);
    }
}
