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

import java.util.List;
import org.cloudml.core.validation.CanBeValidated;
import org.cloudml.core.validation.Report;
import org.cloudml.core.visitors.Visitable;

public abstract class ArtefactPort extends WithProperties implements Visitable, CanBeValidated {

    public static final boolean LOCAL = false;
    public static final boolean REMOTE = true;
    
    protected boolean remote = false;
    protected Artefact owner;
    protected int portNumber = 0;

    public ArtefactPort() {
        super();
    }

    public ArtefactPort(String name, Artefact owner, boolean isRemote) {
        super(name);
        this.owner = owner;
        this.remote = isRemote;
    }

    public ArtefactPort(String name, List<Property> properties, Artefact owner, boolean isRemote) {
        super(name, properties);
        this.owner = owner;
        this.remote = isRemote;
    }

    @Override
    public Report validate() {
        final Report report = new Report();
        if (owner == null) {
            final String message = String.format("The owner of port '%s' is null", getName());
            report.addError(message);
        }
        return report;
    }
   

    public Artefact getOwner() {
        return owner;
    }

    public void setOwner(Artefact owner) {
        this.owner = owner;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int n) {
        this.portNumber = n;
    }

    @Deprecated
    public void setIsRemote(boolean isRemote) {
        this.remote = isRemote;
    }
    
    public void setRemote(boolean isRemote) {
        this.remote = isRemote;
    }

    @Deprecated
    public boolean getIsRemote() {
        return this.remote;
    }
    
    public boolean isRemote() {
        return this.remote;
    }
    
    public boolean isLocal() {
        return !isRemote();
    }  

    @Override
    public String toString() {
        return "ArtefactPortType " + getName() + " ownerType" + owner.getName();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ArtefactPort) {
            ArtefactPort otherArt = (ArtefactPort) other;
            return getName().equals(otherArt.getName()) && owner.equals(otherArt.getOwner());
        }
        else {
            return false;
        }
    }
}
