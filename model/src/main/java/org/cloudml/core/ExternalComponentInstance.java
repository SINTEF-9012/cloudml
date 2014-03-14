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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicolas Ferry on 19.02.14.
 */
public class ExternalComponentInstance<T extends ExternalComponent> extends ComponentInstance<T>{


    private State status;
    private List<String> ips=new ArrayList<String>();
    protected String publicAddress="";

    private enum State{
        stopped,
        running,
        error,
    }

    public ExternalComponentInstance(){}

    public ExternalComponentInstance(String name, T type){
        super(name,type);
        this.status=State.stopped;
    }

    public ExternalComponentInstance(String name, T type, List<Property> properties){
        super(name, properties, type);
        this.status=State.stopped;
    }



    public void setPublicAddress(String publicAddress){
        this.publicAddress=publicAddress;
    }

    public String getPublicAddress(){
        return this.publicAddress;
    }

    public List<String> getIps(){
        return this.ips;
    }

    public void setIps(List<String> ips){
        this.ips=ips;
    }

    public State getStatus(){
        return this.status;
    }

    public void setStatusAsStopped(){
        this.status=State.stopped;
    }

    public void setStatusAsError(){
        this.status=State.error;
    }

    public void setStatusAsRunning(){
        this.status=State.running;
    }

    @Override
    public String toString() {
        return "Instance " + name + " : " + getType().getName();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof InternalComponentInstance) {
            ExternalComponentInstance otherCompInst = (ExternalComponentInstance) other;
            Boolean match= name.equals(otherCompInst.getName()) && type.equals(otherCompInst.getType());
            return match;
        } else {
            return false;
        }
    }

}
