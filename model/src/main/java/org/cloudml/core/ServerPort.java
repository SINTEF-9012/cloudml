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
package org.cloudml.core;

import java.util.List;

public class ServerPort extends ArtefactPort {

	public ServerPort() {
    }

    public ServerPort(String name, Artefact owner, boolean isRemote) {
        super(name, owner, isRemote);
    }

    public ServerPort(String name, List<Property> properties, Artefact owner, boolean isRemote) {
        super(name, properties, owner, isRemote);
    }
	
	@Override
	public String toString() {
		return "ServerPortType " + name + " ownerType" + owner.getName();
	}

}
