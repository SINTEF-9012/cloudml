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
package org.cloudml.core.actions;


import org.cloudml.core.Artefact;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.ClientPortInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.builders.ArtefactInstanceBuilder;


import static org.cloudml.core.builders.Commons.*;

public class Install extends AbstractAction<ArtefactInstance> {

    private final Artefact artefactType;

    public Install(StandardLibrary library, Artefact artefactType) {
        super(library);
        this.artefactType = artefactType;
    }

    @Override
    public ArtefactInstance applyTo(DeploymentModel target) {
        final String instanceName = getLibrary().createUniqueArtefactInstanceName(target, artefactType);
        ArtefactInstanceBuilder builder = anArtefactInstance()
                .named(instanceName)
                .ofType(artefactType.getName())
                .hostedBy(getLibrary().findDestinationFor(target, artefactType).getName());
        builder.integrateIn(target);
        ArtefactInstance instance = target.getArtefactInstances().named(instanceName);
        for (ClientPortInstance clientPort : instance.getRequired()) {
            getLibrary().bind(target, clientPort);
        }
        return instance;
    }
}
