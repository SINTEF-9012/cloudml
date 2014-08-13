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
package org.cloudml.deployer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cloudml.core.*;

/**
 * A class to compare two deployment models
 *
 * @author Nicolas Ferry
 * @author Brice Morin
 */
public class CloudMLModelComparator {

    private static final Logger journal = Logger.getLogger(CloudMLModelComparator.class.getName());

    //Attributes related to model comparison
    private Map<ExternalComponentInstance<? extends ExternalComponent>, ExternalComponentInstance<? extends ExternalComponent>> matchingECs = new Hashtable<ExternalComponentInstance<? extends ExternalComponent>, ExternalComponentInstance<? extends ExternalComponent>>();
    private List<ExternalComponentInstance<? extends ExternalComponent>> removedECs = new ArrayList<ExternalComponentInstance<? extends ExternalComponent>>();
    private List<ExternalComponentInstance<? extends ExternalComponent>> addedECs = new ArrayList<ExternalComponentInstance<? extends ExternalComponent>>();
    
    private Map<InternalComponentInstance, InternalComponentInstance> matchingComponents = new Hashtable<InternalComponentInstance, InternalComponentInstance>();
    private List<InternalComponentInstance> removedComponents = new ArrayList<InternalComponentInstance>();
    private List<InternalComponentInstance> addedComponents = new ArrayList<InternalComponentInstance>();

    private List<RelationshipInstance> addedRelationships = new ArrayList<RelationshipInstance>();
    private Map<RelationshipInstance, RelationshipInstance> matchingRelationships = new Hashtable<RelationshipInstance, RelationshipInstance>();
    private List<RelationshipInstance> removedRelationships = new ArrayList<RelationshipInstance>();

    private List<ExecuteInstance> addedExecutes = new ArrayList<ExecuteInstance>();
    private Map<ExecuteInstance, ExecuteInstance> matchingExecutes = new Hashtable<ExecuteInstance, ExecuteInstance>();
    private List<ExecuteInstance> removedExecutes = new ArrayList<ExecuteInstance>();

    private List<CloudMLElementComparator> ecComparator = null;
    private List<CloudMLElementComparator> icComparator = null;
    private List<CloudMLElementComparator> rlComparator = null;
    private List<CloudMLElementComparator> exComparator = null;

    // The commands resulting from the diff
    private Set<String> commands = new HashSet<String>();

    //The models
    private Deployment currentDM;
    private Deployment targetDM;

    public CloudMLModelComparator(Deployment currentDM, Deployment targetDM) {
        this.currentDM = currentDM;
        this.targetDM = targetDM;
    }

    public void setTargetDeploymentModel(Deployment targetDM) {
        this.targetDM = targetDM;
        clean();
    }

    public void setCurrentDeploymentModel(Deployment currentDM) {
        this.currentDM = currentDM;
        clean();
    }

    public Set<String> getCommands() {
        return this.commands;
    }

    //TODO diff on attributes
    /**
     * Compare the targeted deployment model to the deployment model of the
     * current system
     */
    public void compareCloudMLModel() {
        compareECs();
        journal.log(Level.INFO, ">> Removed VMs :" + removedECs.toString());
        journal.log(Level.INFO, ">> Added VMs  :" + addedECs.toString());

        

        compareComponents();
        journal.log(Level.INFO, ">> Removed components: " + removedComponents.toString());
        journal.log(Level.INFO, ">> Added components: " + addedComponents.toString());

        compareRelationships();
        journal.log(Level.INFO, ">> Removed relationships :" + removedRelationships.toString());
        journal.log(Level.INFO, ">> Added relationships :" + addedRelationships.toString());
        
        compareExecutes();
        journal.log(Level.INFO, ">> Removed executes: " + removedExecutes.toString());
        journal.log(Level.INFO, ">> Added executes: " + addedExecutes.toString());
    }

    /**
     * Compares the vms between the targeted and the current deployment model
     */
    public void compareECs() {
        journal.log(Level.INFO, ">> Comparing ExternalComponents ...");
        Boolean match = false;
        for (ExternalComponentInstance ni : currentDM.getComponentInstances().onlyExternals()) {
            secondloop:
            {
                for (ExternalComponentInstance ni2 : targetDM.getComponentInstances().onlyExternals()) {
                    match = ni.equals(ni2);
                    if (match) {
                        matchingECs.put(ni, ni2);
                        break secondloop;
                    }
                }
            }
            if (!match) {
                removeEC(ni);
            }
        }
        //add the rest
        addECs();
    }

    private void removeEC(ExternalComponentInstance<? extends ExternalComponent> ni) {
        removedECs.add(ni);
        //create action

    }

    private void addECs() {
        addedECs = new ArrayList<ExternalComponentInstance<? extends ExternalComponent>>(targetDM.getComponentInstances().onlyExternals());
        addedECs.removeAll(matchingECs.values());
    }

    /**
     * Compares the relationships between the targeted and the current
     * deployment model
     */
    public void compareRelationships() {
        journal.log(Level.INFO, ">> Comparing relationships ...");
        Boolean match = false;
        for (RelationshipInstance ni : currentDM.getRelationshipInstances()) {
            secondloop:
            {
                for (RelationshipInstance ni2 : targetDM.getRelationshipInstances()) {
                    match = ni.equals(ni2);
                    if (ni.equals(ni2)) {
                        matchingRelationships.put(ni, ni2);
                        break secondloop;
                    }
                }
            }
            if (!match) {
                removedRelationships.add(ni);
            }
        }
        //add the rest
        addRelationships();
    }

    private void addRelationships() {
        addedRelationships = new ArrayList<RelationshipInstance>(targetDM.getRelationshipInstances());
        addedRelationships.removeAll(matchingRelationships.values());
        for (RelationshipInstance ni : addedRelationships) {
            int i = currentDM.getComponentInstances().toList().indexOf(ni.getRequiredEnd().getOwner().get());
            if (i >= 0) {
                ComponentInstance a = currentDM.getComponentInstances().toList().get(i);
                if (a.isInternal()) {
                    int j = ((InternalComponentInstance) a).getRequiredPorts().toList().indexOf(ni.getRequiredEnd());
                    ni.setRequiredEnd(((InternalComponentInstance) a).getRequiredPorts().toList().get(j));
                }
            }
            i = currentDM.getComponentInstances().toList().indexOf(ni.getProvidedEnd().getOwner().get());
            if (i >= 0) {
                ComponentInstance a = currentDM.getComponentInstances().toList().get(i);
                int j = a.getProvidedPorts().toList().indexOf(ni.getProvidedEnd());
                List<ProvidedPortInstance> l = a.getProvidedPorts().toList();
                ni.setProvidedEnd(l.get(j));
            }
        }
    }

    /**
     * Compares the components between the targeted and the current deployment
     * model
     */
    public void compareComponents() {
        journal.log(Level.INFO, ">> Comparing Internal Components ...");
        Boolean match = false;
        for (InternalComponentInstance ni : currentDM.getComponentInstances().onlyInternals()) {
            secondloop:
            {
                for (InternalComponentInstance ni2 : targetDM.getComponentInstances().onlyInternals()) {
                    match = ni.equals(ni2);
                    if (ni.equals(ni2)) {
                        matchingComponents.put(ni, ni2);
                        break secondloop;
                    }
                }
            }
            if (!match) {
                removedComponents.add(ni);
            }
        }
        //add the rest
        addComponents();
    }

    private void removedComponent(InternalComponentInstance ni) {
        removedComponents.add(ni);
        //create action
    }

    private void addComponents() {
        addedComponents = new ArrayList<InternalComponentInstance>(targetDM.getComponentInstances().onlyInternals());
        addedComponents.removeAll(matchingComponents.values());
    }

    /**
     * Compares the executeInstances between the current and target model
     */
    public void compareExecutes() {
        journal.log(Level.INFO, ">> Comparing Executes Instances ...");
        Boolean match = false;
        for (ExecuteInstance ei : currentDM.getExecuteInstances()) {
            secondloop:
            {
                for (ExecuteInstance ei2 : targetDM.getExecuteInstances()) {
                    match = ei.equals(ei2);
                    if (ei.equals(ei2)) {
                        matchingExecutes.put(ei, ei2);
                        break secondloop;
                    }
                }
            }
            if (!match) {
                removedExecutes.add(ei);
            }
        }
        //add the rest
        addExecutes();
    }

    private void addExecutes(){
        addedExecutes = new ArrayList<ExecuteInstance>(targetDM.getExecuteInstances());
        addedExecutes.removeAll(matchingExecutes.values());
        for (ExecuteInstance ei : addedExecutes) {
            int i = currentDM.getComponentInstances().toList().indexOf(ei.getRequiredEnd().getOwner().get());
            if (i >= 0) {
                ComponentInstance a = currentDM.getComponentInstances().toList().get(i);
                ei.setRequiredEnd(((InternalComponentInstance) a).getRequiredExecutionPlatform());
            }
            i = currentDM.getComponentInstances().toList().indexOf(ei.getProvidedEnd().getOwner().get());
            if (i >= 0) {
                ComponentInstance a = currentDM.getComponentInstances().toList().get(i);
                int j = a.getProvidedExecutionPlatforms().toList().indexOf(ei.getProvidedEnd());
                List<ProvidedExecutionPlatformInstance> l = a.getProvidedExecutionPlatforms().toList();
                ei.setProvidedEnd(l.get(j));
            }
        }
    }


    /**
     * Clean all the lists resulting from the previous comparison
     */
    private void clean() {
        matchingECs.clear();
        removedECs.clear();
        addedECs.clear();

        matchingComponents.clear();
        removedComponents.clear();
        addedComponents.clear();

        matchingRelationships.clear();
        removedRelationships.clear();
        addedRelationships.clear();

        matchingExecutes.clear();
        removedExecutes.clear();
        addedExecutes.clear();

        commands.clear();
        
        this.ecComparator = null;
        this.icComparator = null;
        this.exComparator = null;
        this.rlComparator = null;
    }

    public List<InternalComponentInstance> getRemovedComponents() {
        return this.removedComponents;
    }

    public List<InternalComponentInstance> getAddedComponents() {
        return this.addedComponents;
    }

    public List<ExternalComponentInstance<? extends ExternalComponent>> getRemovedECs() {
        return this.removedECs;
    }

    public List<ExternalComponentInstance<? extends ExternalComponent>> getAddedECs() {
        return this.addedECs;
    }

    public List<RelationshipInstance> getRemovedRelationships() {
        return this.removedRelationships;
    }

    public List<RelationshipInstance> getAddedRelationships() {
        return this.addedRelationships;
    }

    public List<ExecuteInstance> getRemovedExecutes(){
        return this.removedExecutes;
    }

    public List<ExecuteInstance> getAddedExecutes(){
        return this.addedExecutes;
    }

    public Map<ExecuteInstance,ExecuteInstance> getMatchingExecutes(){
        return this.matchingExecutes;
    }

    public Map<RelationshipInstance, RelationshipInstance> getMatchingRelationships() {
        return this.matchingRelationships;
    }

    public Map<InternalComponentInstance, InternalComponentInstance> getMatchingComponents() {
        return this.matchingComponents;
    }

    public Map<ExternalComponentInstance<? extends ExternalComponent>, ExternalComponentInstance<? extends ExternalComponent>> getMatchingECs() {
        return this.matchingECs;
    }
    
    public List<CloudMLElementComparator> getEcComparator(){
        if(this.ecComparator!=null)
            return this.ecComparator;
        this.ecComparator = new ArrayList<CloudMLElementComparator>();
        for(Map.Entry<ExternalComponentInstance<? extends ExternalComponent>, ExternalComponentInstance<? extends ExternalComponent>> entry: getMatchingECs().entrySet()){
            
            CloudMLElementComparator comparator = new CloudMLElementComparator(entry.getKey(), entry.getValue());
            comparator.compare();
            this.ecComparator.add(comparator);
        }
        return this.ecComparator;
    }
    
    public List<CloudMLElementComparator.ElementUpdate> getAllEcUpdates(){
        List<CloudMLElementComparator.ElementUpdate> allUpdates = new ArrayList<CloudMLElementComparator.ElementUpdate>();
        for(CloudMLElementComparator c : this.getEcComparator()){
            allUpdates.addAll(c.getUpdates());
        }
        return allUpdates;
    }
    
    public List<CloudMLElementComparator> getIcComparator(){
        if(this.icComparator!=null)
            return this.icComparator;
        this.icComparator = new ArrayList<CloudMLElementComparator>();
        for(Map.Entry<InternalComponentInstance, InternalComponentInstance> entry: this.getMatchingComponents().entrySet()){
            CloudMLElementComparator comparator = new CloudMLElementComparator(entry.getKey(), entry.getValue());
            comparator.compare();
            this.icComparator.add(comparator);
        }
        return this.icComparator;
    }
    
    public List<CloudMLElementComparator.ElementUpdate> getAllIcUpdates(){
        List<CloudMLElementComparator.ElementUpdate> allUpdates = new ArrayList<CloudMLElementComparator.ElementUpdate>();
        for(CloudMLElementComparator c : this.getIcComparator()){
            allUpdates.addAll(c.getUpdates());
        }
        return allUpdates;
    }
    
    public List<CloudMLElementComparator> getRlComparator(){
        if(this.rlComparator!=null)
            return this.rlComparator;
        this.rlComparator = new ArrayList<CloudMLElementComparator>();
        for(Map.Entry<RelationshipInstance, RelationshipInstance> entry: this.getMatchingRelationships().entrySet()){
            CloudMLElementComparator comparator = new CloudMLElementComparator(entry.getKey(), entry.getValue());
            comparator.compare();
            this.rlComparator.add(comparator);
        }
        return this.rlComparator;
    }
    
    public List<CloudMLElementComparator.ElementUpdate> getAllRlUpdates(){
        List<CloudMLElementComparator.ElementUpdate> allUpdates = new ArrayList<CloudMLElementComparator.ElementUpdate>();
        for(CloudMLElementComparator c : this.getRlComparator()){
            allUpdates.addAll(c.getUpdates());
        }
        return allUpdates;
    }
    
    public List<CloudMLElementComparator> getExComparator(){
        if(this.exComparator!=null)
            return this.exComparator;
        this.exComparator = new ArrayList<CloudMLElementComparator>();
        for(Map.Entry<ExecuteInstance, ExecuteInstance> entry: this.getMatchingExecutes().entrySet()){
            CloudMLElementComparator comparator = new CloudMLElementComparator(entry.getKey(), entry.getValue());
            comparator.compare();
            this.exComparator.add(comparator);
        }
        return this.exComparator;
    }
    
    public List<CloudMLElementComparator.ElementUpdate> getAllExUpdates(){
        List<CloudMLElementComparator.ElementUpdate> allUpdates = new ArrayList<CloudMLElementComparator.ElementUpdate>();
        for(CloudMLElementComparator c : this.getExComparator()){
            allUpdates.addAll(c.getUpdates());
        }
        return allUpdates;
    }
}
