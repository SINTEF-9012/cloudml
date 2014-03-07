/*
 */
package org.cloudml.core.validation;

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
import org.cloudml.core.visitors.VisitListener;

/**
 * On any deployment model object, append the result of the local validation.
 */
public class ValidationCollector implements VisitListener {

    private final Report validation;

    public ValidationCollector() {
        this.validation = new Report();
    }

    public void collect(CanBeValidated toValidate) {
        validation.append(toValidate.validate());
    }

    public Report getOverallReport() {
        return this.validation;
    }

    @Override
    public void onDeployment(DeploymentModel subject) {
        collect(subject);
    }

    @Override
    public void onProvider(Provider subject) {
        collect(subject);
    }

    @Override
    public void onNode(Node subject) {
        collect(subject);
    }

    @Override
    public void onArtefact(Artefact subject) {
        collect(subject);
    }

    @Override
    public void onClientPort(ClientPort subject) {
        collect(subject);
    }

    @Override
    public void onServerPort(ServerPort subject) {
        collect(subject);
    }

    @Override
    public void onBinding(Binding subject) {
        collect(subject);
    }

    @Override
    public void onNodeInstance(NodeInstance subject) {
        collect(subject);
    }

    @Override
    public void onArtefactInstance(ArtefactInstance subject) {
        collect(subject);
    }

    @Override
    public void onClientPortInstance(ClientPortInstance subject) {
        validation.append(subject.validate());
    }

    @Override
    public void onServerPortInstance(ServerPortInstance subject) {
        validation.append(subject.validate());
    }

    @Override
    public void onBindingInstance(BindingInstance subject) {
        validation.append(subject.validate());
    }
}
