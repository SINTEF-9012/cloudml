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

import org.cloudml.core.util.OwnedBy;
import static org.cloudml.core.util.OwnedBy.CONTAINED;

public abstract class ExecutionPlatformInstance<T extends ExecutionPlatform> extends WithResources implements DeploymentElement, OwnedBy<ComponentInstance<? extends Component>> {

    private final OptionalOwner<ComponentInstance<? extends Component>> owner;
    private T type;

    public ExecutionPlatformInstance(String name, T type) {
        this(name, type, null);
    }
    
    public ExecutionPlatformInstance(String name, T type, ComponentInstance<? extends Component> owner) {
        super(name);
        this.type = onlyIfValid(type);
        this.owner = new OptionalOwner<ComponentInstance<? extends Component>>();
        if (owner != null) {
            this.owner.set(owner);
        }
    }
    
     private T onlyIfValid(T type) {
        if (type == null) {
            final String error = String.format("'null' is not a valid platform execution type for platform '%s'", getQualifiedName());
            throw new IllegalArgumentException(error);
        }
        return type;
    }

    @Override
    public OptionalOwner<ComponentInstance<? extends Component>> getOwner() {
        return owner;
    }

    @Override
    public Deployment getDeployment() {
        return getOwner().get().getDeployment();
    }
        

    public T getType() {
        return this.type;
    }

    public void setType(T type) {
        this.type = onlyIfValid(type);
    }
    
    public boolean isInstanceOf(T type) {
        return this.type.equals(type);
    }

    @Override
    public String getQualifiedName() {
        return String.format("%s%s%s", getOwner().getName(), CONTAINED, getName());
    }
}
