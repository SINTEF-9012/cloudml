package org.cloudml.core;

import java.util.List;


public class ServerPortInstance extends ArtefactPortInstance<ServerPort>{
	
	
	public ServerPortInstance(String name, ServerPort type, ArtefactInstance owner, boolean isRemote) {
		super(name, type, owner, isRemote);
	}
	
	public ServerPortInstance(String name, ServerPort type, List<Property> properties, ArtefactInstance owner) {
        super(name, type, properties, owner);
    }
	
	public ServerPortInstance(String name, ServerPort type, List<Property> properties, ArtefactInstance owner, boolean isRemote){
		super(name, type, properties, owner, isRemote);
	}
	
	@Override
    public String toString() {
        return "ServerPortInstance " + name + " owner:" + owner.getName() + " remote:"+ isRemote;
    }
	
	
}
