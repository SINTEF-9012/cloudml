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
package org.cloudml.ui.graph;

import java.io.Serializable;

import org.cloudml.core.WithProperties;

public class Vertex implements Serializable {
	private String name;
	private String type;
	private WithProperties instance;

    private Vertex(){}
        
	public Vertex(String name,String type){
		this.name=name;
		this.type=type;
	}
	
	public Vertex(String name,String type, WithProperties instance){
		this.name=name;
		this.type=type;
		this.instance=instance;
	}

	public String getName(){
		return name;
	}

	public String getType(){
		return type;
	}
	
	public void setInstance(WithProperties instance){
		this.instance=instance;
	}
	
	public WithProperties getInstance(){
		return instance;
	}
}
