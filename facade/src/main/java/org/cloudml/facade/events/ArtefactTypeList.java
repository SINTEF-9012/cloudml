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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.cloudml.core.Artefact;
import org.cloudml.facade.commands.CloudMlCommand;

/**
 * Convey a list of artefact types
 *
 * @author Franck Chauvel
 * @since 1.0
 */
public class ArtefactTypeList extends Data {

    private final ArrayList<Artefact> artefacts;

    /**
     * Create a new event containing list of artefacts
     *
     * @param artefacts the list of artefacts
     */
    public ArtefactTypeList(CloudMlCommand command, final Collection<Artefact> artefacts) {
        super(command);
        this.artefacts = new ArrayList<Artefact>(artefacts.size());
        this.artefacts.addAll(artefacts);
    }

    /**
     * @return the list of artefacts
     */
    public List<Artefact> getArtefactTypes() {
        return Collections.unmodifiableList(this.artefacts);
    }
    
    @Override
    public void accept(EventHandler handler) {
        handler.handle(this);
    }
    
}
