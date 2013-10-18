package org.cloudml.mrt.coord.cmd.abstracts;


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
