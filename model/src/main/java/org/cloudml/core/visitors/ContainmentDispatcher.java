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
 *
 */
public class ContainmentDispatcher implements Dispatcher {

    @Override
    public void dispatchTo(Visitor visitor, DeploymentModel model) {
        for (Provider provider : model.getProviders()) {
            provider.accept(visitor);
        }
        for (Node node : model.getNodeTypes()) {
            node.accept(visitor);
        }
        for (Binding binding : model.getBindingTypes()) {
            binding.accept(visitor);
        }
        for (NodeInstance nodeInstance : model.getNodeInstances()) {
            nodeInstance.accept(visitor);
        }
        for (Artefact artefact : model.getArtefactTypes()) {
            artefact.accept(visitor);
        }
        for (ArtefactInstance artefactInstance : model.getArtefactInstances()) {
            artefactInstance.accept(visitor);
        }
        for (BindingInstance bindingInstance : model.getBindingInstances()) {
            bindingInstance.accept(visitor);
        }
    }

    @Override
    public void dispatchTo(Visitor visitor, Artefact artefact) {
        for (ClientPort client : artefact.getRequired()) {
            client.accept(visitor);
        }
        for (ServerPort server : artefact.getProvided()) {
            server.accept(visitor);
        }
    }

    @Override
    public void dispatchTo(Visitor visitor, ArtefactInstance artefactInstance) {
        for (ClientPortInstance clientInstance : artefactInstance.getRequired()) {
            clientInstance.accept(visitor);
        }
        for (ServerPortInstance serverInstance : artefactInstance.getProvided()) {
            serverInstance.accept(visitor);
        }
    }

    @Override
    public void dispatchTo(Visitor visitor, Binding binding) {
        // Nothing to do as bindings do not contain anything
    }

    @Override
    public void dispatchTo(Visitor visitor, BindingInstance binding) {
        // Nothing to do as binding instances do not contain anything
    }
    
    
    
}
