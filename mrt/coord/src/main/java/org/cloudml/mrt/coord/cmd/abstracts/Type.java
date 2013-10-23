package org.cloudml.mrt.coord.cmd.abstracts;

public class Type {
	public static final String pkg = "org.cloudml.core";
	public String name;
	public Type(String name){
		this.name = name;
	}
	
	public Class<?> obtainClass(){
		try{
			return Class.forName(pkg+"."+name);
		}
		catch(ClassNotFoundException e){
			throw new RuntimeException(e);
		}
	}
	
}
