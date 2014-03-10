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

import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.ServerPort;
import org.cloudml.core.ServerPortInstance;
import static org.cloudml.core.ServerPortInstance.*;
import org.cloudml.core.ServerPortInstance.*;

import static org.cloudml.core.builders.Commons.*;

public class ServerPortInstanceBuilder {

    private String name;
    private String typeName;
    private String ownerName;

    public ServerPortInstanceBuilder() {
        this.name = DEFAULT_NAME;
        this.ownerName = DEFAULT_NAME;
        this.typeName = DEFAULT_NAME;
    }

    public ServerPortInstanceBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ServerPortInstanceBuilder ofType(String typeName) {
        this.typeName = typeName;
        return this;
    }

    public ServerPortInstanceBuilder ownedBy(String ownerName) {
        this.ownerName = ownerName;
        return this;
    }

    public ServerPortInstance build() {
        final ArtefactInstance owner = anArtefactInstance()
                .named(this.ownerName)
                .build();
        final ServerPort type = aServerPort()
                .named(this.typeName)
                .build();
        owner.getType().addProvidedPort(type);
        final ServerPortInstance result = new ServerPortInstance(name, type, owner);
        owner.addProvidedPort(result);
        return result;
    }

    public void integrateIn(ArtefactInstance container) {
        ServerPortInstance result = new ServerPortInstance(
                name, container.getType().findProvidedPortByName(typeName),
                container);
        container.addProvidedPort(result);
    }
}
