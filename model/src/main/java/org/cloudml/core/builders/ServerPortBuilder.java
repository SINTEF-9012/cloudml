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

import org.cloudml.core.Artefact;
import org.cloudml.core.ServerPort;
import static org.cloudml.core.builders.Commons.*;
import static org.cloudml.core.ServerPort.*;


public class ServerPortBuilder {

    private String name;
    private boolean remote;
    
    public ServerPortBuilder() {
        this.name = DEFAULT_NAME;
        this.remote = DEFAULT_IS_REMOTE;
    }
        
    public ServerPortBuilder named(String name) {
        this.name = name;
        return this;
    }
    
    public ServerPortBuilder remote() {
        this.remote = REMOTE;
        return this;
    }
    
    public ServerPortBuilder local() {
        this.remote = LOCAL;
        return this;
    }
    
    public ServerPort build() {
        return new ServerPort(name, anArtefact().build(), remote);
    }

    public void integrateIn(Artefact container) {
        ServerPort result = new ServerPort(name, container, remote);
        container.getProvided().add(result);
    }
    
}
