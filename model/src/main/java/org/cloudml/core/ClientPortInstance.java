package org.cloudml.core;

import java.util.List;

public class ClientPortInstance extends ArtefactPortInstance<ClientPort>{

	private boolean isOptional=true;
	
	
	public ClientPortInstance(String name, ClientPort type, ArtefactInstance owner, boolean isRemote, boolean isOptional) {
		super(name, type, owner, isRemote);
		this.isOptional=isOptional;
	}
	
	public ClientPortInstance(String name, ClientPort type, List<Property> properties, ArtefactInstance owner, boolean isOptional) {
        super(name, type, properties, owner);
        this.isOptional=isOptional;
    }
	
	public ClientPortInstance(String name, ClientPort type, List<Property> properties, ArtefactInstance owner, boolean isRemote, boolean isOptional){
		super(name, type, properties, owner, isRemote);
		this.isOptional=isOptional;
	}
	
	public void setIsOptional(boolean isOptional){
		this.isOptional=isOptional;
	}
	
	public boolean getIsOptional(){
		return this.isOptional;
	}

	@Override
    public String toString() {
        return "ClientPortInstance " + name + " owner:" + owner.getName() + " optional:"+ isOptional;
    }
	
}
