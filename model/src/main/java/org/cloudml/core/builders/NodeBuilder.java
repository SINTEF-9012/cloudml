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
package org.cloudml.core.builders;

import org.cloudml.core.DeploymentModel;
import static org.cloudml.core.builders.Commons.*;
import org.cloudml.core.Node;
import static org.cloudml.core.Node.*;

public class NodeBuilder {

    private String name;
    private String providerName;

    public NodeBuilder() {
        this.name = DEFAULT_NAME;
        this.providerName = DEFAULT_NAME;
    }
    
    public NodeBuilder named(String name) {
        this.name = name;
        return this;
    }

    public NodeBuilder providedBy(String providerName) {
        this.providerName = providerName;
        return this;
    }

    public Node build() {
        Node result = new Node();
        result.setName(name);
        result.setProvider(aProvider().named(providerName).build());
        return result;
    }

    public void integrateIn(DeploymentModel context) {
        Node result = new Node();
        result.setName(name);
        result.setProvider(context.findProviderByName(providerName));
        context.getNodeTypes().add(result);
    }
}
