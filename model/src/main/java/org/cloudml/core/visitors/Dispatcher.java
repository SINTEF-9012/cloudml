/*
 */
package org.cloudml.core.visitors;

import org.cloudml.core.Artefact;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.Binding;
import org.cloudml.core.BindingInstance;
import org.cloudml.core.DeploymentModel;

/**
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public interface Dispatcher {

    void dispatchTo(Visitor visitor, DeploymentModel model);

    void dispatchTo(Visitor visitor, Artefact artefact);

    void dispatchTo(Visitor visitor, ArtefactInstance artefactInstance);

    void dispatchTo(Visitor visitor, Binding binding);

    void dispatchTo(Visitor visitor, BindingInstance binding);
}
