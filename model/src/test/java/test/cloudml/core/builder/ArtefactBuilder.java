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
package test.cloudml.core.builder;

import org.cloudml.core.Artefact;
import org.cloudml.core.ClientPort;
import org.cloudml.core.ServerPort;

/**
 * Simplify the construction of complex artefact types
 */
public class ArtefactBuilder {

    private final Artefact inProgress;

    public ArtefactBuilder(String artefactName) {
        this.inProgress = new Artefact(artefactName);
    }

    public ClientPort createClientPort(String portName, boolean isRemote, boolean isMandatory) {
        ClientPort port = new ClientPort(portName, inProgress, isRemote, isMandatory);
        inProgress.getRequired().add(port);
        return port;
    }
    
    public ServerPort createServerPort(String portName, boolean isRemote) {
        ServerPort port = new ServerPort(portName, inProgress, isRemote);
        inProgress.getProvided().add(port);
        return port;
    }

    public Artefact getResult() {
        return this.inProgress;
    }
}
