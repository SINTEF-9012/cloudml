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
        for (VisitListener listener : listeners) {
            add(listener);
        }
    }

    private void rejectIfInvalid(VisitListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("'null' is not a valid visit listener");
        }
    }

    public void publish(Deployment deployment) {
        for (VisitListener listener : listeners) {
            listener.onDeployment(deployment);
        }
    }

    public void publish(Provider provider) {
        for (VisitListener listener : listeners) {
            listener.onProvider(provider);
        }
    }

    public void publish(VM vm) {
        for (VisitListener listener : listeners) {
            listener.onVM(vm);
        }
    }

    public void publish(InternalComponent component) {
        for (VisitListener listener : listeners) {
            listener.onInternalComponent(component);
        }
    }

    public void publish(ExternalComponent component) {
        for (VisitListener listener : listeners) {
            listener.onExternalComponent(component);
        }
    }

    public void publish(Relationship relationship) {
        for (VisitListener listener : listeners) {
            listener.onRelationship(relationship);
        }
    }

    public void publish(RequiredExecutionPlatform subject) {
        for (VisitListener listener : listeners) {
            listener.onRequiredExecutionPlatform(subject);
        }
    }

    public void publish(ProvidedExecutionPlatform subject) {
        for (VisitListener listener : listeners) {
            listener.onProvidedExecutionPlatform(subject);
        }
    }

    public void publish(ProvidedPort subject) {
        for (VisitListener listener : listeners) {
            listener.onProvidedPort(subject);
        }
    }

    public void publish(RequiredPort subject) {
        for (VisitListener listener : listeners) {
            listener.onRequiredPort(subject);
        }
    }

    public void publish(VMInstance vmInstance) {
        for (VisitListener listener : listeners) {
            listener.onVMInstance(vmInstance);
        }
    }

    public void publish(ExternalComponentInstance subject) {
        for (VisitListener listener : listeners) {
            listener.onExternalComponentInstance(subject);
        }
    }

    public void publish(InternalComponentInstance subject) {
        for (VisitListener listener : listeners) {
            listener.onInternalComponentInstance(subject);
        }
    }

    public void publish(ProvidedPortInstance subject) {
        for (VisitListener listener : listeners) {
            listener.onProvidedPortInstance(subject);
        }
    }

    public void publish(RequiredPortInstance subject) {
        for (VisitListener listener : listeners) {
            listener.onRequiredPortInstance(subject);
        }
    }

    public void publish(RequiredExecutionPlatformInstance subject) {
        for (VisitListener listener : listeners) {
            listener.onRequiredExecutionPlatformInstance(subject);
        }
    }

    public void publish(ProvidedExecutionPlatformInstance subject) {
        for (VisitListener listener : listeners) {
            listener.onProvidedExecutionPlatformInstance(subject);
        }
    }

    public void publish(RelationshipInstance relationship) {
        for (VisitListener listener : listeners) {
            listener.onRelationshipInstance(relationship);
        }
    }

    public void publish(ExecuteInstance execute) {
        for (VisitListener listener : listeners) {
            listener.onExecuteInstance(execute);
        }
    }
    
    public void publish(Cloud cloud) {
        for(VisitListener listener: listeners) {
            listener.onCloud(cloud);
        }
    }
}
