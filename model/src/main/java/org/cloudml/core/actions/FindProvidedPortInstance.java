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


import java.util.ArrayList;
import java.util.List;
import org.cloudml.core.Component;
import org.cloudml.core.ComponentInstance;
import org.cloudml.core.InternalComponentInstance;
import org.cloudml.core.Relationship;
import org.cloudml.core.Deployment;
import org.cloudml.core.ProvidedPortInstance;

public class FindProvidedPortInstance extends AbstractFind<ProvidedPortInstance> {

    private final Relationship bindingType;

    public FindProvidedPortInstance(StandardLibrary library, Relationship bindingType) {
        super(library);
        this.bindingType = bindingType;
    }

    @Override
    protected List<ProvidedPortInstance> collectCandidates(Deployment deployment) {
        ArrayList<ProvidedPortInstance> candidates = new ArrayList<ProvidedPortInstance>();
        for (InternalComponentInstance artefactInstance : deployment.getComponentInstances().onlyInternals()) {
            for (ProvidedPortInstance serverPort : artefactInstance.getProvidedPorts()) {
                if (isCandidate(bindingType, serverPort)) {
                    candidates.add(serverPort);
                }
            }
        }
        return candidates;
    }

    private boolean isCandidate(Relationship bindingType, ProvidedPortInstance serverPort) {
        return bindingType.getProvidedEnd().equals(serverPort.getType());
    }

    @Override
    protected void handleLackOfCandidate(Deployment deployment, List<ProvidedPortInstance> candidates) {
        final Component component = getLibrary().findComponentProviding(deployment, bindingType);
        final ComponentInstance<? extends Component> instance = getLibrary().provision(deployment, component);
        final ProvidedPortInstance serverPort = instance.getProvidedPorts().firstNamed(bindingType.getProvidedEnd().getName());
        candidates.add(serverPort);
    }

}
