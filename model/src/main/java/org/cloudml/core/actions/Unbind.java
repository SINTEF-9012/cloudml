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
import org.cloudml.core.ArtefactPort;
import org.cloudml.core.ArtefactPortInstance;
import org.cloudml.core.BindingInstance;
import org.cloudml.core.DeploymentModel;


public class Unbind extends AbstractAction<Void> {

    private final ArtefactPortInstance<? extends ArtefactPort> port;

    public Unbind(StandardLibrary library, ArtefactPortInstance<? extends ArtefactPort> port) {
        super(library);
        this.port = port;
    }

    @Override
    public Void applyTo(DeploymentModel deployment) {
        List<BindingInstance> bindings = findBindingInstancesByPort(deployment, port);
        for(BindingInstance binding: bindings) {
            deployment.getBindingInstances().remove(binding);
        }
        return NOTHING;
    }  

    // FIXME: Should be moved in CloudML
    private List<BindingInstance> findBindingInstancesByPort(DeploymentModel deployment, ArtefactPortInstance<? extends ArtefactPort> port) {
        final ArrayList<BindingInstance> selection = new ArrayList<BindingInstance>();
        for (BindingInstance binding: deployment.getBindingInstances()) {
            if (binding.getServer().equals(port)
                    || binding.getClient().equals(port)) {
                selection.add(binding);
            }
        }
        return selection;
    }
    
}
