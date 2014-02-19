package org.cloudml.core;

/**
 * Created by Nicolas Ferry on 19.02.14.
 */
public class ExternalComponentInstance extends ComponentInstance{

    public ExternalComponentInstance(String name, ExternalComponent type){
        super(name,type);
    }

    @Override
    public String toString() {
        return "Instance " + name + " : " + getType().getName();
    }

}
