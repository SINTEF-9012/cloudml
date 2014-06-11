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
package org.cloudml.mrt.cmd.abstracts;


import java.lang.reflect.Method;

public class Property {
	public String name;
	public Property(String name){
		this.name = name;
	}
	
	public void updateValue(Object context, String value) {
		Method[] methods = context.getClass().getMethods();
		Method setter = null;
		String setterName = "set" + name.substring(0,1).toUpperCase() + name.substring(1);
		for(Method m : methods){
			if(setterName.equals(m.getName()))
				setter = m;
		}
		if(setter == null)
			throw new RuntimeException("property not found");
		try {
			Class type = setter.getParameterTypes()[0];
			if("int".equals(type.getName())){
				setter.invoke(context, Integer.parseInt(value));
			}
			else if("double".equals(type.getName())){
				setter.invoke(context, Double.parseDouble(value));
			}
			else if(String.class.equals(type))
				setter.invoke(context, value);
			else
				throw new RuntimeException("property type not supported");
		} catch (Exception e) {
			throw new RuntimeException("property setter invalid", e);
		}
	}
}
