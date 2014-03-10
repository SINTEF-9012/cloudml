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

import org.cloudml.core.Artefact;
import org.cloudml.core.ClientPort;
import static org.cloudml.core.builders.Commons.*;
import static org.cloudml.core.ClientPort.*;

/**
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class ClientPortBuilder {

    private String name;
    private boolean remote;
    private boolean optional;

    public ClientPortBuilder() {
        this.name = DEFAULT_NAME;
        this.remote = DEFAULT_IS_REMOTE;
        this.optional = DEFAULT_IS_OPTIONAL;
    }

    public ClientPortBuilder named(String name) {
        this.name = name;
        return this;
    }

    public ClientPortBuilder remote() {
        this.remote = REMOTE;
        return this;
    }

    public ClientPortBuilder local() {
        this.remote = LOCAL;
        return this;
    }

    public ClientPortBuilder optional() {
        this.optional = OPTIONAL;
        return this;
    }

    public ClientPortBuilder mandatory() {
        this.optional = MANDATORY;
        return this;
    }

    public ClientPort build() {
        ClientPort result = new ClientPort(name, anArtefact().build(), remote, optional);
        return result;
    }

    public void integrateIn(Artefact container) {
        ClientPort result = new ClientPort(name, container, remote, optional);
        container.getRequired().add(result);
    }
    
}
