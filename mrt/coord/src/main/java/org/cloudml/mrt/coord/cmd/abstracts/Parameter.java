package org.cloudml.mrt.coord.cmd.abstracts;

public class Parameter {
	public Parameter(){}
	public Parameter(String type, Object value){
		this.type = type;
		this.value = value;
	}
	public String type;
	public Object value;
}
