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
package org.cloudml.core.builders;

import java.util.ArrayList;
import org.cloudml.core.Component;
import org.cloudml.core.Deployment;

public abstract class ComponentBuilder<T extends Component, N extends ComponentBuilder<?, ?>> extends WithResourcesBuilder<T, N> implements SubPartBuilder<Deployment> {

    private final ArrayList<ProvidedPortBuilder> providedPorts;
    private final ArrayList<ProvidedExecutionPlatformBuilder> providedExecutionPlatforms;

    public ComponentBuilder() {
        this.providedPorts = new ArrayList<ProvidedPortBuilder>();
        this.providedExecutionPlatforms = new ArrayList<ProvidedExecutionPlatformBuilder>();
    }
        
    protected void prepare(Component underConstruction) {
        super.prepare(underConstruction);
        setupProvidedPorts(underConstruction);
        setupProvidedExecutionPlatforms(underConstruction);
    }

    private void setupProvidedPorts(Component underConstruction) {
        for (ProvidedPortBuilder providedPort : providedPorts) {
            providedPort.integrateIn(underConstruction);
        }
    }

    private void setupProvidedExecutionPlatforms(Component underConstruction) {
        for (ProvidedExecutionPlatformBuilder platform : providedExecutionPlatforms) {
            platform.integrateIn(underConstruction);
        }
    }

    public N with(ProvidedPortBuilder requiredPort) {
        this.providedPorts.add(requiredPort);
        return next();
    }

    public N with(ProvidedExecutionPlatformBuilder executionPlatform) {
        this.providedExecutionPlatforms.add(executionPlatform);
        return next();
    }

  
}
