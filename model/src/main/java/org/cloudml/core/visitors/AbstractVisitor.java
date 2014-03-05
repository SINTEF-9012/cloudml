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

import java.util.ArrayList;
import java.util.Arrays;
import org.cloudml.core.Artefact;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.Node;
import org.cloudml.core.NodeInstance;
import org.cloudml.core.Provider;

/**
 * TODO: Extract navigation code, which decide of the order in which children
 * are traversed
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public abstract class AbstractVisitor implements Visitor {

    private final ArrayList<Processor> listeners;

    public AbstractVisitor() {
        this.listeners = new ArrayList<Processor>();
    }

    @Override
    public void addListeners(Processor... listeners) {
        if (listeners == null) {
            throw new IllegalArgumentException("Unable to add 'null' listeners");
        }
        this.listeners.addAll(Arrays.asList(listeners));
    }

    @Override
    public void visitDeploymentModel(DeploymentModel model) {
        for (Processor processor : listeners) {
            processor.processDeployment(model);
        }
        for (Provider provider : model.getProviders()) {
            provider.accept(this);
        }
        for (Node node : model.getNodeTypes().values()) {
            node.accept(this);
        }
        for (NodeInstance nodeInstance : model.getNodeInstances()) {
            nodeInstance.accept(this);
        }
        for(Artefact artefact: model.getArtefactTypes().values()) {
            artefact.accept(this);
        }
        for(ArtefactInstance artefactInstance: model.getArtefactInstances()) {
            artefactInstance.accept(this);
        }
    }

    @Override
    public void visitProvider(Provider provider) {
        for (Processor processor : listeners) {
            processor.processProvider(provider);
        }
        // TODO: dispatch to children
    }

    @Override
    public void visitorNode(Node node) {
        for (Processor processor : listeners) {
            processor.processNode(node);
        }
        // TODO: dispatch to children
    }

    @Override
    public void visitNodeInstance(NodeInstance nodeInstance) {
        for (Processor processor : listeners) {
            processor.processNodeInstance(nodeInstance);
        }
        // TODO: dispatch to children
    }

    @Override
    public void visitArtefact(Artefact artefact) {
        for (Processor listener: listeners) {
            listener.processArtefact(artefact);
        }
        // TODO: dispatch to children
    }

    @Override
    public void visitArtefactInstance(ArtefactInstance artefactInstance) {
        for (Processor listener: listeners) {
            listener.processArtefactInstance(artefactInstance);
        }
        // TODO: dispatch to children
    }
    
}