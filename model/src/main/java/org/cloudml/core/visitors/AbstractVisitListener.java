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

package org.cloudml.core.visitors;

import org.cloudml.core.Artefact;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.Binding;
import org.cloudml.core.BindingInstance;
import org.cloudml.core.ClientPort;
import org.cloudml.core.ClientPortInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.Node;
import org.cloudml.core.NodeInstance;
import org.cloudml.core.Provider;
import org.cloudml.core.ServerPort;
import org.cloudml.core.ServerPortInstance;

/**
 * Provides default empty implementation for each event handler
 */
public abstract class AbstractVisitListener implements VisitListener {

    @Override
    public void onDeployment(DeploymentModel subject) {
    }

    @Override
    public void onProvider(Provider subject) {
    }

    @Override
    public void onNode(Node subject) {
    }

    @Override
    public void onArtefact(Artefact subject) {
    }

    @Override
    public void onClientPort(ClientPort subject) {
    }

    @Override
    public void onServerPort(ServerPort subject) {
    }

    @Override
    public void onBinding(Binding subject) {
    }

    @Override
    public void onNodeInstance(NodeInstance subject) {
    }

    @Override
    public void onArtefactInstance(ArtefactInstance subject) {
    }

    @Override
    public void onClientPortInstance(ClientPortInstance subject) {
    }

    @Override
    public void onServerPortInstance(ServerPortInstance subject) {
    }

    @Override
    public void onBindingInstance(BindingInstance subject) {
    }
   
    
}
