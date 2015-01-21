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

    private Map<InternalComponentInstance, InternalComponentInstance> mapping;
    private List<Map> result=new ArrayList<Map>();
    private VMInstance host;
    private VMInstance source;
    private static final Logger journal = Logger.getLogger(ReplicateSubGraph.class.getName());
    private Deployment currentModel;

    public ReplicateSubGraph(StandardLibrary library, Deployment currentModel, VMInstance source, VMInstance host) {
        super(library);
        this.currentModel=rejectIfInvalid(currentModel);
        this.host=host;
        this.source=source;
        mapping=new HashMap<InternalComponentInstance, InternalComponentInstance>();
    }

    private Deployment rejectIfInvalid(Deployment graph) {
        if (graph == null) {
            throw new IllegalArgumentException("'null' is not a valid component instance for duplication");
        }
        return graph;
    }

    private InternalComponentInstanceGroup hostedGroup=new InternalComponentInstanceGroup();

    private void allHosted(ComponentInstance ci, Deployment target){
        InternalComponentInstanceGroup icig= currentModel.getComponentInstances().onlyInternals().hostedOn(ci);
        if(icig !=null){
            hostedGroup.addAll(icig);
            for(ComponentInstance c : icig){
                InternalComponentInstance ici;
                if(ci.isExternal())
                    ici=getLibrary().replicateComponentInstance(target, c, host).asInternal();
                else ici=getLibrary().replicateComponentInstance(target, c, mapping.get(ci)).asInternal();
                mapping.put(c.asInternal(), ici);
                allHosted(c, target);
            }
        }
    }

    @Override
    public Map<InternalComponentInstance, InternalComponentInstance> applyTo(Deployment target) {
        allHosted(source, target);
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
                    journal.log(Level.INFO,"11&11");
                    rib.put(name,aRelationshipInstance()
                            .named(name)
                            .from(mapping.get(ri.getClientComponent()).getName(),
                                    mapping.get(ri.getClientComponent()).asInternal().getRequiredPorts().ofType(ri.getRequiredEnd().getType()).getType().getName())
                            .to(mapping.get(ri.getServerComponent()).getName(),
                                    mapping.get(ri.getServerComponent()).getProvidedPorts().ofType(ri.getProvidedEnd().getType()).getType().getName())
                            .ofType(ri.getType().getName()));
                }else{
                    journal.log(Level.INFO,"2222222");
                    rib.put(name, aRelationshipInstance()
                            .named(name)
                            .from(mapping.get(ri.getClientComponent()).getName(),
                                    mapping.get(ri.getClientComponent()).asInternal().getRequiredPorts().ofType(ri.getRequiredEnd().getType()).getType().getName())
                            .to(ri.getServerComponent().getName(),
                                    ri.getProvidedEnd().getType().getName())
                            .ofType(ri.getType().getName()));
                }
            }else if(mapping.containsKey(ri.getServerComponent())){
                journal.log(Level.INFO,"33333");
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
