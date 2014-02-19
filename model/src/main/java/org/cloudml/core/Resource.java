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

import java.util.HashMap;
import java.util.Map;

public class Resource extends CloudMLElementWithProperties {
	private String installCommand ="";
	private String retrieveCommand ="";
	private String configureCommand ="";
	private String startCommand ="";
	private String stopCommand ="";
	private Map<String,String> uploadCommand=new HashMap<String, String>();
	
	
	public Resource(){}
	
	public Resource(String name){
		super(name);
	}
	
	public Resource(String name, String deployingCommand,String retrievingCommand){
		super(name);
		this.installCommand =deployingCommand;
		this.retrieveCommand =retrievingCommand;
	}
	
	public Resource(String name, String deployingCommand,String retrievingCommand, String configurationCommand, String startCommand, String stopCommand){
		super(name);
		this.installCommand =deployingCommand;
		this.retrieveCommand =retrievingCommand;
		this.configureCommand =configurationCommand;
		this.startCommand =startCommand;
		this.stopCommand =stopCommand;
	}
	
	public String getInstallCommand(){
		return installCommand;
	}
	
	public String getRetrieveCommand(){
		return retrieveCommand;
	}
	
	public String getConfigureCommand(){
		return configureCommand;
	}
	
	public String getStartCommand(){
		return startCommand;
	}
	
	public String getStopCommand(){
		return stopCommand;
	}
	
	public Map<String,String> getUploadCommand(){
		return uploadCommand;
	}

	public void setInstallCommand(String deployingCommand){
		this.installCommand =deployingCommand;
	}
	
	public void setRetrieveCommand(String retrievingCommand){
		this.retrieveCommand =retrievingCommand;
	}
	
	public void setConfigureCommand(String configurationCommand){
		this.configureCommand =configurationCommand;
	}
	
	public void setStartCommand(String startCommand){
		this.startCommand =startCommand;
	}
	
	public void setStopCommand(String stopCommand){
		this.stopCommand =stopCommand;
	}
	
	public void setUploadCommand(Map<String,String> paths){
		this.uploadCommand=paths;
	}
	
	
	@Override
	public String toString(){
		return "Resource: "+name;
	}
	
}
