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
