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
import org.cloudml.core.Component;
import org.cloudml.core.ProvidedPort;


public class ProvidedPortBuilder extends PortBuilder<ProvidedPort, ProvidedPortBuilder> {

    public ProvidedPortBuilder() {
        super();
    }

    @Override
    protected ProvidedPortBuilder next() {
        return this;
    }
            
    public ProvidedPort build() {
        final ProvidedPort result = new ProvidedPort(getName(), isRemote());
        prepare(result);
        return result;
    }

    public void integrateIn(Component container) {
        final ProvidedPort port = new ProvidedPort(getName(), isRemote());
        prepare(port);
        port.getOwner().set(container);
        container.getProvidedPorts().add(port);
    }
    
}
