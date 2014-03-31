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
import org.cloudml.core.NamedElement;
import org.cloudml.core.Provider;

public class ProviderBuilder {

    private String name;
    private String credentials;

    public ProviderBuilder() {
        this.name = NamedElement.DEFAULT_NAME;
    }

    public ProviderBuilder named(String name) {
        this.name = name;
        return this;
    }

    public ProviderBuilder withCredentials(String credentials) {
        this.credentials = Provider.DEFAULT_CREDENTIALS;
        return this;
    }

    public Provider build() {
        final Provider result = new Provider();
        result.setName(name);
        //result.setCredentials(credentials);
        return result;
    }

    public void integrateIn(DeploymentModel model) {
        final Provider result = new Provider();
        result.setName(name);
        model.getProviders().add(result);   
    }
}
