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
        for (Node node : model.getNodeTypes().values()) {
            node.accept(visitor);
        }
        for (Binding binding : model.getBindingTypes().values()) {
            binding.accept(visitor);
        }
        for (NodeInstance nodeInstance : model.getNodeInstances()) {
            nodeInstance.accept(visitor);
        }
        for (Artefact artefact : model.getArtefactTypes().values()) {
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
