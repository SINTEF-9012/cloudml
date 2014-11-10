/*
 */
package org.cloudml.indicators;

import eu.diversify.trio.core.Component;
import org.cloudml.core.Deployment;

import eu.diversify.trio.core.System;
import eu.diversify.trio.core.Tag;
import eu.diversify.trio.core.requirements.Requirement;
import java.util.ArrayList;
import java.util.List;
import org.cloudml.core.ComponentInstance;
import org.cloudml.core.InternalComponentInstance;
import org.cloudml.core.RequiredPortInstance;

import static eu.diversify.trio.core.requirements.Factory.nothing;
import static eu.diversify.trio.core.requirements.Factory.require;

/**
 * Convert a CloudML model into a Trio System
 */
public class TrioExporter {

    /**
     * @return a TrioExporter, which will not include any self-repair
     * capabilities in the TRIO model it produces.
     */
    public static TrioExporter withoutSelfRepair() {
        return new TrioExporter();
    }

    /**
     * Convert the given CloudML deployment model into a TRIO system
     *
     * @param model the CloudML model to be converted
     *
     * @return an equivalent Trio system
     */
    public System asTrioSystem(Deployment model) {
        if (model == null) {
            throw new IllegalArgumentException("Unable to build a TRIO system from 'null'");
        }
        
        final List<String> taggedAsInternal = new ArrayList<String>();
        final List<String> taggedAsExternal = new ArrayList<String>();
        
        final ArrayList<Component> trioComponents = new ArrayList<Component>();
        for (ComponentInstance each: model.getComponentInstances()) {
            if (each.isExternal()) {
                taggedAsExternal.add(each.getName());
            }
            if (each.isInternal()) {
                taggedAsInternal.add(each.getName());
            }
            trioComponents.add(new Component(each.getName(), extractRequirement(each))); 
        }
        
        
        final List<Tag> tags = new ArrayList<Tag>();
        tags.add(new Tag(INTERNAL_TAG, taggedAsInternal));
        tags.add(new Tag(EXTERNAL_TAG, taggedAsExternal));
        
        return new System(trioComponents, tags);
    }
    
    private static final String EXTERNAL_TAG = "external";
    private static final String INTERNAL_TAG = "internal";
    

    private Requirement extractRequirement(ComponentInstance instance) {
        assert instance != null: "Cannot extract any requirement from 'null'";

        Requirement requirements = nothing();
        
        if (instance.isInternal()) {
            final InternalComponentInstance internalInstance = instance.asInternal();
            for (RequiredPortInstance eachDependency: internalInstance.getRequiredPorts()) {
                if (eachDependency.isBound()) {
                    requirements = requirements.and(require(eachDependency.findProvider().getName()));
                }
            }
            
           requirements = requirements.and(require(internalInstance.getHost().getName()));
        }
        
        
        return requirements;
    } 

}
