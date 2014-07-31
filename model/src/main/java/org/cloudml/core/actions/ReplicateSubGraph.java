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
import org.cloudml.core.builders.RelationshipInstanceBuilder;
import org.cloudml.core.collections.InternalComponentInstanceGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.cloudml.core.builders.Commons.aRelationshipInstance;

/**
 * Created by nicolasf on 16.07.14.
 */
public class ReplicateSubGraph extends AbstractAction<Map<InternalComponentInstance, InternalComponentInstance>> {

    private final InternalComponentInstanceGroup graph;
    private Map<InternalComponentInstance, InternalComponentInstance> mapping;
    private List<Map> result=new ArrayList<Map>();
    private VMInstance host;
    private static final Logger journal = Logger.getLogger(ReplicateSubGraph.class.getName());

    public ReplicateSubGraph(StandardLibrary library, InternalComponentInstanceGroup graph, VMInstance host) {
        super(library);
        this.graph=rejectIfInvalid(graph);
        this.host=host;
        mapping=new HashMap<InternalComponentInstance, InternalComponentInstance>();
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
            InternalComponentInstance ici=getLibrary().replicateComponentInstance(target, ci, host).asInternal();
            mapping.put(ci,ici);
        }
        manageDependencies(target);

        return mapping;
    }

    private HashMap<String,RelationshipInstance> manageDependencies(Deployment target){
        HashMap<String,RelationshipInstance> createdInstances=new HashMap<String,RelationshipInstance>();
        HashMap<String,RelationshipInstanceBuilder> rib=new HashMap<String,RelationshipInstanceBuilder>();
        for(RelationshipInstance ri: target.getRelationshipInstances()){
            final String name = getLibrary().createUniqueRelationshipInstanceName(target, ri.getType());
            RelationshipInstance relationship=null;
            if(mapping.containsKey(ri.getClientComponent())){
                if(mapping.containsKey(ri.getServerComponent())){ //TODO: move this in a method
                    rib.put(name,aRelationshipInstance()
                            .named(name)
                            .from(mapping.get(ri.getClientComponent()).getName(),
                                    mapping.get(ri.getClientComponent()).asInternal().getRequiredPorts().ofType(ri.getRequiredEnd().getType()).getType().getName())
                            .to(mapping.get(ri.getServerComponent()).getName(),
                                    mapping.get(ri.getServerComponent()).getProvidedPorts().ofType(ri.getProvidedEnd().getType()).getType().getName())
                            .ofType(ri.getType().getName()));
                }else{
                    rib.put(name, aRelationshipInstance()
                            .named(name)
                            .from(mapping.get(ri.getClientComponent()).getName(),
                                    mapping.get(ri.getClientComponent()).asInternal().getRequiredPorts().ofType(ri.getRequiredEnd().getType()).getType().getName())
                            .to(ri.getServerComponent().getName(),
                                    ri.getProvidedEnd().getType().getName())
                            .ofType(ri.getType().getName()));
                }
            }else if(mapping.containsKey(ri.getServerComponent())){
                rib.put(name, aRelationshipInstance()
                        .named(name)
                        .from(ri.getClientComponent().getName(),
                                ri.getRequiredEnd().getName())
                        .to(mapping.get(ri.getServerComponent()).getName(),
                                mapping.get(ri.getServerComponent()).getProvidedPorts().ofType(ri.getProvidedEnd().getType()).getType().getName())
                        .ofType(ri.getType().getName()));
            }
        }
        for(String name: rib.keySet()){
            rib.get(name).integrateIn(target);
            createdInstances.put(name, target.getRelationshipInstances().firstNamed(name));
        }
        return createdInstances;
    }
}
