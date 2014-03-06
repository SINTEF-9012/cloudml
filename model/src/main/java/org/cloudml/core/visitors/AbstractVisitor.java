/**
 * This file is part of CloudML [ http://cloudml.org ]
 *
 * Copyright (C) 2012 - SINTEF ICT Contact: Franck Chauvel
 * <franck.chauvel@sintef.no>
 *
 * Module: root
 *
 * CloudML is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * CloudML is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CloudML. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package org.cloudml.core.visitors;

import java.util.ArrayList;
import java.util.Arrays;
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

public abstract class AbstractVisitor implements Visitor {

    private final Dispatcher dispatcher;
    private final ArrayList<VisitListener> listeners;

    public AbstractVisitor(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
        this.listeners = new ArrayList<VisitListener>();
    }

    @Override
    public void addListeners(VisitListener... listeners) {
        if (listeners == null) {
            throw new IllegalArgumentException("Unable to add 'null' listeners");
        }
        this.listeners.addAll(Arrays.asList(listeners));
    }

    @Override
    public void visitDeploymentModel(DeploymentModel model) {
        for (VisitListener processor : listeners) {
            processor.onDeployment(model);
        }
        dispatcher.dispatchTo(this, model);
    }

    @Override
    public void visitProvider(Provider subject) {
        for (VisitListener processor : listeners) {
            processor.onProvider(subject);
        }
    }

    @Override
    public void visitorNode(Node node) {
        for (VisitListener processor : listeners) {
            processor.onNode(node);
        }
        // No dispatch needed as nodes have no relationship
    }

    @Override
    public void visitNodeInstance(NodeInstance nodeInstance) {
        for (VisitListener processor : listeners) {
            processor.onNodeInstance(nodeInstance);
        }
        // No dispatch needed as node instances have no relationship
    }

    @Override
    public void visitArtefact(Artefact artefact) {
        for (VisitListener listener : listeners) {
            listener.onArtefact(artefact);
        }
        dispatcher.dispatchTo(this, artefact);
    }

    @Override
    public void visitArtefactInstance(ArtefactInstance artefactInstance) {
        for (VisitListener listener : listeners) {
            listener.onArtefactInstance(artefactInstance);
        }
        dispatcher.dispatchTo(this, artefactInstance);
    }

    @Override
    public void visitBinding(Binding binding) {
        for (VisitListener listener : listeners) {
            listener.onBinding(binding);
        }
        dispatcher.dispatchTo(this, binding);
    }

    @Override
    public void visitBindingInstance(BindingInstance subject) {
        for (VisitListener listener : listeners) {
            listener.onBindingInstance(subject);
        }
        dispatcher.dispatchTo(this, subject);
    }

    @Override
    public void visitClientPort(ClientPort subject) {
        for (VisitListener listener : listeners) {
            listener.onClientPort(subject);
        }
        // No dispatch needed as ClientPorts have no relationship
    }

    @Override
    public void visitServerPort(ServerPort subject) {
        for (VisitListener listener : listeners) {
            listener.onServerPort(subject);
        }
        // No dispatch needed as ServerPorts have no relationship
    }

    @Override
    public void visitClientPortInstance(ClientPortInstance subject) {
        for (VisitListener listener : listeners) {
            listener.onClientPortInstance(subject);
        }
        // No dispatch needed as ClientPorts have no relationship
    }

    @Override
    public void visitServerPortInstance(ServerPortInstance subject) {
        for (VisitListener listener : listeners) {
            listener.onServerPortInstance(subject);
        }
        // No dispatch needed as ClientPorts have no relationship
    }
}