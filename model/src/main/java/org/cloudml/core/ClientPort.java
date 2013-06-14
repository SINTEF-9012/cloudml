package org.cloudml.core;

import java.util.List;

public class ClientPort extends ArtefactPort{

	private boolean isOptional=true;
	
	public ClientPort(String name, Artefact owner, boolean isRemote) {
        super(name, owner, isRemote);
    }
	
	public ClientPort(String name, Artefact owner, boolean isRemote, boolean isOptional) {
        super(name, owner, isRemote);
        this.isOptional=isOptional;
    }
	
	public ClientPort(String name, List<Property> properties, Artefact owner, boolean isRemote, boolean isOptional){
		super(name,properties,owner,isRemote);
		this.isOptional=isOptional;
	}

	public void setIsOptional(boolean isOptional){
		this.isOptional=isOptional;
	}

	public boolean getIsOptional(){
		return isOptional;
	}

	@Override
	public String toString() {
		return "ClientTypePort " + name + " ownerType" + owner.getName();
	}
}
