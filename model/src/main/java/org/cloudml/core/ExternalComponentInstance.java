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
import org.cloudml.core.visitors.Visitor;

public class ExternalComponentInstance<T extends ExternalComponent> extends ComponentInstance<T> {
    
    public static final String DEFAULT_PUBLIC_ADDRESS = "no address given";
    private State status;
    private List<String> ips = new ArrayList<String>();
    private String publicAddress;
    
    public ExternalComponentInstance(String name, T type) {
        super(name, type);
        this.status = State.STOPPED;
        this.publicAddress = DEFAULT_PUBLIC_ADDRESS;
    }
    
    public final boolean isVM() {
        return getType().isVM();
    }
    
    public VMInstance asVM() {
        if (!isVM()) {
            throw new IllegalStateException("Unable to cast an plain external component instance to a VM instance");
        }
        return (VMInstance) this;
    }
    
    @Override
    public void accept(Visitor visitor) {
        visitor.visitExternalComponentInstance(this);
    }
    
    public void setPublicAddress(String publicAddress) {
        this.publicAddress = publicAddress;
    }
    
    public String getPublicAddress() {
        return this.publicAddress;
    }
    
    public List<String> getIps() {
        return this.ips;
    }
    
    public void setIps(List<String> ips) {
        this.ips = ips;
    }
    
    public State getStatus() {
        return this.status;
    }

    //TODO these methods should be deleted as soon as nobody use them
    @Deprecated
    //USE MRT METHODS TO SET THE STATUS
    public void setStatusAsStopped() {
        this.status = State.STOPPED;
    }

    @Deprecated
    //USE MRT METHODS TO SET THE STATUS
    public void setStatusAsError() {
        this.status = State.ERROR;
    }

    @Deprecated
    // USE MRT METHODS TO SET THE STATUS
    public void setStatusAsRunning() {
        this.status = State.RUNNING;
    }

    @Deprecated
    //USE setStatus(State)
    public void setStatus(String state){
        this.status = State.valueOf(state);
    }

    public void setStatus (State state){
        this.status = state;
    }
    
    @Override
    public String toString() {
        return "Instance " + getQualifiedName();
    }
    
    @Override
    public boolean equals(Object other) {
        if (other instanceof InternalComponentInstance) {
            ExternalComponentInstance otherCompInst = (ExternalComponentInstance) other;
            Boolean match = getName().equals(otherCompInst.getName()) && getType().equals(otherCompInst.getType());
            return match;
        }
        else {
            return false;
        }
    }
}
