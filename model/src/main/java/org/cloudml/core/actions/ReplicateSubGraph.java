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
package org.cloudml.core.actions;

import org.cloudml.core.*;
import org.cloudml.core.collections.InternalComponentInstanceGroup;
import org.cloudml.core.collections.RelationshipInstanceGroup;

import java.util.List;
import java.util.Map;

import static org.cloudml.core.builders.Commons.aRelationshipInstance;

/**
 * Created by nicolasf on 16.07.14.
 */
public class ReplicateSubGraph extends AbstractAction<Map<InternalComponentInstance, InternalComponentInstance>> {

    private final InternalComponentInstanceGroup graph;
    private Map<InternalComponentInstance, InternalComponentInstance> mapping;
    private VMInstance host;

    public ReplicateSubGraph(StandardLibrary library, InternalComponentInstanceGroup graph, VMInstance host) {
        super(library);
        this.graph=rejectIfInvalid(graph);
        this.host=host;
    }

    private InternalComponentInstanceGroup rejectIfInvalid(InternalComponentInstanceGroup graph) {
        if (graph == null) {
            throw new IllegalArgumentException("'null' is not a valid component instance for duplication");
        }
        return graph;
    }

    @Override
    public Map<InternalComponentInstance, InternalComponentInstance> applyTo(Deployment target) {
        for(InternalComponentInstance ci: graph){
            mapping.put(ci,getLibrary().replicateComponentInstance(target, ci, host).asInternal());
        }
        manageDependencies(target);

        return mapping;
    }

    private void manageDependencies(Deployment target){
        for(RelationshipInstance ri: target.getRelationshipInstances()){
            if(mapping.containsKey(ri.getClientComponent())){
                final String name = getLibrary().createUniqueRelationshipInstanceName(target, ri.getType());
                if(mapping.containsKey(ri.getServerComponent())){ //TODO: move this in a method
                    aRelationshipInstance()
                            .named(name)
                            .from(mapping.get(ri.getClientComponent()).getType().getName(),
                                    mapping.get(ri.getClientComponent()).asInternal().getRequiredPorts().ofType(ri.getRequiredEnd().getType()).getType().getName())
                            .to(mapping.get(ri.getServerComponent()).getType().getName(),
                                    mapping.get(ri.getServerComponent()).getProvidedPorts().ofType(ri.getProvidedEnd().getType()).getType().getName())
                            .ofType(ri.getType().getName())
                            .integrateIn(target);
                }else{
                    aRelationshipInstance()
                            .named(name)
                            .from(mapping.get(ri.getClientComponent()).getType().getName(),
                                    mapping.get(ri.getClientComponent()).asInternal().getRequiredPorts().ofType(ri.getRequiredEnd().getType()).getType().getName())
                            .to(ri.getServerComponent().getType().getName(),
                                    ri.getProvidedEnd().getType().getName())
                            .ofType(ri.getType().getName())
                            .integrateIn(target);
                }
            }
            if(mapping.containsKey(ri.getServerComponent())){
                final String name = getLibrary().createUniqueRelationshipInstanceName(target, ri.getType());
                aRelationshipInstance()
                        .named(name)
                        .from(ri.getClientComponent().getType().getName(),
                                ri.getRequiredEnd().getType().getName())
                        .to(mapping.get(ri.getServerComponent()).getType().getName(),
                                mapping.get(ri.getServerComponent()).getProvidedPorts().ofType(ri.getProvidedEnd().getType()).getType().getName())
                        .ofType(ri.getType().getName())
                        .integrateIn(target);
            }
        }
    }
}
