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

import org.cloudml.core.visitors.Visitor;

public class RequiredPort extends Port {

    public static final boolean OPTIONAL = true;
    public static final boolean MANDATORY = false;
    
    private boolean optional;

    public RequiredPort(String name) {
        this(name, REMOTE, MANDATORY);
    }
    
    public RequiredPort(String name, boolean isRemote) {
        this(name, isRemote, MANDATORY);
    }

    public RequiredPort(String name, boolean isRemote, boolean isOptional) {
        super(name, isRemote);
        this.optional = isOptional;
    }

    @Override
    public RequiredPortInstance instantiate() {
        return new RequiredPortInstance(getName(), this); 
    }    
    

    @Override
    public void accept(Visitor visitor) {
        visitor.visitRequiredPort(this);
    }

    public final void setOptional(boolean isOptional) {
        this.optional = isOptional;
    }

    public final boolean isOptional() {
        return optional;
    }

    public final boolean isMandatory() {
        return !this.optional;
    }

    @Override
    public String toString() {
        return "ClientTypePort " + getQualifiedName();
    }
}
