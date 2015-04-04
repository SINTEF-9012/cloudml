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
/*
 */
package org.cloudml.core.visitors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.cloudml.core.*;

final class ListenerGroup {

    private final ArrayList<VisitListener> listeners;

    public ListenerGroup(VisitListener... listeners) {
        this(Arrays.asList(listeners));
    }

    public ListenerGroup(Collection<VisitListener> listeners) {
        this.listeners = new ArrayList<VisitListener>();
        addAll(listeners);
    }

    public void add(VisitListener listener) {
        rejectIfInvalid(listener);
        this.listeners.add(listener);
    }

    public void addAll(Collection<VisitListener> listeners) {
        for (VisitListener listener: listeners) {
            add(listener);
        }
    }

    private void rejectIfInvalid(VisitListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("'null' is not a valid visit listener");
        }
    }

    public void enter(Deployment deployment) {
        for (VisitListener listener: listeners) {
            listener.onDeploymentEntry(deployment);
        }
    }

    public void enter(Provider provider) {
        for (VisitListener listener: listeners) {
            listener.onProviderEntry(provider);
        }
    }

    public void enter(ResourcePoolInstance resourcePoolInstance) {
        for (VisitListener listener: listeners) {
            listener.onResourcePoolInstanceEntry(resourcePoolInstance);
        }
    }

    public void enter(VM vm) {
        for (VisitListener listener: listeners) {
            listener.onVMEntry(vm);
        }
    }

    public void enter(InternalComponent component) {
        for (VisitListener listener: listeners) {
            listener.onInternalComponentEntry(component);
        }
    }

    public void enter(ExternalComponent component) {
        for (VisitListener listener: listeners) {
            listener.onExternalComponentEntry(component);
        }
    }

    public void enter(Relationship relationship) {
        for (VisitListener listener: listeners) {
            listener.onRelationshipEntry(relationship);
        }
    }

    public void enter(RequiredExecutionPlatform subject) {
        for (VisitListener listener: listeners) {
            listener.onRequiredExecutionPlatformEntry(subject);
        }
    }

    public void enter(ProvidedExecutionPlatform subject) {
        for (VisitListener listener: listeners) {
            listener.onProvidedExecutionPlatformEntry(subject);
        }
    }

    public void enter(ProvidedPort subject) {
        for (VisitListener listener: listeners) {
            listener.onProvidedPortEntry(subject);
        }
    }

    public void enter(RequiredPort subject) {
        for (VisitListener listener: listeners) {
            listener.onRequiredPortEntry(subject);
        }
    }

    public void enter(VMInstance vmInstance) {
        for (VisitListener listener: listeners) {
            listener.onVMInstanceEntry(vmInstance);
        }
    }

    public void enter(ExternalComponentInstance subject) {
        for (VisitListener listener: listeners) {
            listener.onExternalComponentInstanceEntry(subject);
        }
    }

    public void enter(InternalComponentInstance subject) {
        for (VisitListener listener: listeners) {
            listener.onInternalComponentInstanceEntry(subject);
        }
    }

    public void enter(ProvidedPortInstance subject) {
        for (VisitListener listener: listeners) {
            listener.onProvidedPortInstanceEntry(subject);
        }
    }

    public void enter(RequiredPortInstance subject) {
        for (VisitListener listener: listeners) {
            listener.onRequiredPortInstanceEntry(subject);
        }
    }

    public void enter(RequiredExecutionPlatformInstance subject) {
        for (VisitListener listener: listeners) {
            listener.onRequiredExecutionPlatformInstanceEntry(subject);
        }
    }

    public void enter(ProvidedExecutionPlatformInstance subject) {
        for (VisitListener listener: listeners) {
            listener.onProvidedExecutionPlatformInstanceEntry(subject);
        }
    }

    public void enter(RelationshipInstance relationship) {
        for (VisitListener listener: listeners) {
            listener.onRelationshipInstanceEntry(relationship);
        }
    }

    public void enter(ExecuteInstance execute) {
        for (VisitListener listener: listeners) {
            listener.onExecuteInstanceEntry(execute);
        }
    }

    public void enter(Cloud cloud) {
        for (VisitListener listener: listeners) {
            listener.onCloudEntry(cloud);
        }
    }

    public void exit(Deployment deployment) {
        for (VisitListener listener: listeners) {
            listener.onDeploymentExit(deployment);
        }
    }

    public void exit(Provider subject) {
        for (VisitListener listener: listeners) {
            listener.onProviderExit(subject);
        }
    }

    public void exit(ResourcePoolInstance subject) {
        for (VisitListener listener: listeners) {
            listener.onResourcePoolInstanceExit(subject);
        }
    }

    public void exit(InternalComponent subject) {
        for (VisitListener listener: listeners) {
            listener.onInternalComponentExit(subject);
        }
    }

    public void exit(ExternalComponent subject) {
        for (VisitListener listener: listeners) {
            listener.onExternalComponentExit(subject);
        }
    }

    public void exit(VM subject) {
        for (VisitListener listener: listeners) {
            listener.onVMExit(subject);
        }
    }

    public void exit(Relationship subject) {
        for (VisitListener listener: listeners) {
            listener.onRelationshipExit(subject);
        }
    }

    public void exit(RequiredPort subject) {
        for (VisitListener listener: listeners) {
            listener.onRequiredPortExit(subject);
        }
    }

    public void exit(ProvidedPort subject) {
        for (VisitListener listener: listeners) {
            listener.onProvidedPortExit(subject);
        }
    }

    public void exit(RequiredExecutionPlatform subject) {
        for (VisitListener listener: listeners) {
            listener.onRequiredExecutionPlatformExit(subject);
        }
    }

    public void exit(ProvidedExecutionPlatform subject) {
        for (VisitListener listener: listeners) {
            listener.onProvidedExecutionPlatformExit(subject);
        }
    }

    public void exit(VMInstance subject) {
        for (VisitListener listener: listeners) {
            listener.onVMInstanceExit(subject);
        }
    }

    public void exit(ExternalComponentInstance subject) {
        for (VisitListener listener: listeners) {
            listener.onExternalComponentInstanceExit(subject);
        }
    }

    public void exit(InternalComponentInstance subject) {
        for (VisitListener listener: listeners) {
            listener.onInternalComponentInstanceExit(subject);
        }
    }

    public void exit(RelationshipInstance subject) {
        for (VisitListener listener: listeners) {
            listener.onRelationshipInstanceExit(subject);
        }
    }

    public void exit(RequiredPortInstance subject) {
        for (VisitListener listener: listeners) {
            listener.onRequiredPortInstanceExit(subject);
        }
    }

    public void exit(ProvidedPortInstance subject) {
        for (VisitListener listener: listeners) {
            listener.onProvidedPortInstanceExit(subject);
        }
    }

    public void exit(RequiredExecutionPlatformInstance subject) {
        for (VisitListener listener: listeners) {
            listener.onRequiredExecutionPlatformInstanceExit(subject);
        }
    }

    public void exit(ProvidedExecutionPlatformInstance subject) {
        for (VisitListener listener: listeners) {
            listener.onProvidedExecutionPlatformInstanceExit(subject);
        }
    }

    public void exit(ExecuteInstance subject) {
        for (VisitListener listener: listeners) {
            listener.onExecuteInstanceExit(subject);
        }
    }

    public void exit(Cloud subject) {
        for (VisitListener listener: listeners) {
            listener.onCloudExit(subject);
        }
    }
}
