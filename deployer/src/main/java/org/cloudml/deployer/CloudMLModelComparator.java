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
 * @author Nicolas Ferry
 * @author Brice Morin
 */
public class CloudMLModelComparator {
	private static final Logger journal = Logger.getLogger(CloudMLModelComparator.class.getName());

	//Attributes related to model comparison
	private Map<ExternalComponentInstance,ExternalComponentInstance> matchingVMs = new Hashtable<ExternalComponentInstance,ExternalComponentInstance>();
	private List<ExternalComponentInstance> removedVMs = new ArrayList<ExternalComponentInstance>();
	private List<ExternalComponentInstance> addedVMs = new ArrayList<ExternalComponentInstance>();


	private Map<ComponentInstance,ComponentInstance> matchingComponents = new Hashtable<ComponentInstance,ComponentInstance>();
	private List<ComponentInstance> removedComponents = new ArrayList<ComponentInstance>();
	private List<ComponentInstance> addedComponents = new ArrayList<ComponentInstance>();


	private Map<RelationshipInstance,RelationshipInstance> matchingRelationships = new Hashtable<RelationshipInstance,RelationshipInstance>();
	private List<RelationshipInstance> removedRelationships = new ArrayList<RelationshipInstance>();
	private List<RelationshipInstance> addedRelationships = new ArrayList<RelationshipInstance>();

	// The commands resulting from the diff
	private Set<String> commands = new HashSet<String>();

	private CloudMLModel currentDM;
	private CloudMLModel targetDM;

	public CloudMLModelComparator(CloudMLModel currentDM, CloudMLModel targetDM){
		this.currentDM=currentDM;
		this.targetDM=targetDM;
	}

	public void setTargetDeploymentModel(CloudMLModel targetDM){
		this.targetDM=targetDM;
		clean();
	}

	public void setCurrentDeploymentModel(CloudMLModel currentDM){
		this.currentDM=currentDM;
		clean();
	}

	public Set<String> getCommands(){
		return this.commands;
	}

	//TODO diff on attributes
	/**
	 * Compare the targeted deployment model to the deployment model of the current system
	 */
	public void compareCloudMLModel(){
		compareVMs();
		journal.log(Level.INFO, ">> Removed VMs :" + removedVMs.toString());
		journal.log(Level.INFO, ">> Added VMs  :" + addedVMs.toString());

		compareRelationships();
		//journal.log(Level.INFO, ">> current bindings :" + currentDM.getRelationshipInstances());
		//journal.log(Level.INFO, ">> Matching bindings :" + matchingRelationships.toString());
		journal.log(Level.INFO, ">> Removed relationships :" + removedRelationships.toString());
		journal.log(Level.INFO, ">> Added relationships :" + addedRelationships.toString());


		compareComponents();
		journal.log(Level.INFO, ">> Removed components: " + removedComponents.toString());
		journal.log(Level.INFO, ">> Added components: " + addedComponents.toString());
	}

	/**
	 * Compares the vms between the targeted and the current deployment model
	 */
	public void compareVMs(){
		journal.log(Level.INFO, ">> Comparing vms ...");
		Boolean match=false;
		for(ExternalComponentInstance ni : currentDM.getExternalComponentInstances()){
			secondloop:{ for(ExternalComponentInstance ni2 : targetDM.getExternalComponentInstances()){
				match=ni.equals(ni2);
				if(ni.equals(ni2)){
					matchingVMs.put(ni, ni2);
					break secondloop;
				}
			}}
		if(!match){
			removeVM(ni);
		}
		}
		//add the rest
		addVMs();
	}

	private void removeVM(ExternalComponentInstance ni){
		removedVMs.add(ni);
		//create action

	}

	private void addVMs(){
		addedVMs =  new ArrayList<ExternalComponentInstance>(targetDM.getExternalComponentInstances());
		addedVMs.removeAll(matchingVMs.values());
	}


	/**
	 * Compares the relationships between the targeted and the current deployment model
	 */
	public void compareRelationships(){
		journal.log(Level.INFO, ">> Comparing relationships ...");
		Boolean match=false;
		for(RelationshipInstance ni : currentDM.getRelationshipInstances()){
			secondloop:{ for(RelationshipInstance ni2 : targetDM.getRelationshipInstances()){
				match=ni.equals(ni2);
				if(ni.equals(ni2)){
					matchingRelationships.put(ni, ni2);
					break secondloop;
				}
			}}
		if(!match){
			removedRelationships.add(ni);
		}
		}
		//add the rest
		addRelationships();
	}

	private void removedRelationship(RelationshipInstance ni){
		removedRelationships.add(ni);
		//create action
	}

	private void addRelationships(){
		addedRelationships =  new ArrayList<RelationshipInstance>(targetDM.getRelationshipInstances());
		addedRelationships.removeAll(matchingRelationships.values());
		for(RelationshipInstance ni : addedRelationships){
			int i=currentDM.getComponentInstances().indexOf(ni.getRequiredPortInstance().getOwner());
			if(i >= 0){
				ComponentInstance a=currentDM.getComponentInstances().get(i);
                if(a instanceof InternalComponentInstance){
				    int j=((InternalComponentInstance)a).getRequiredPortInstances().indexOf(ni.getRequiredPortInstance());
				    ni.setRequiredPortInstance(((InternalComponentInstance)a).getRequiredPortInstances().get(j));
                }
			}
			i=currentDM.getComponentInstances().indexOf(ni.getProvidedPortInstance().getOwner());
			if(i >= 0){
				ComponentInstance a=currentDM.getComponentInstances().get(i);
				int j=a.getProvidedPortInstances().indexOf(ni.getProvidedPortInstance());
                List<ProvidedPortInstance> l= a.getProvidedPortInstances();
				ni.setProvidedPortInstance(l.get(j));
			}
		}
	}

	/**
	 * Compares the components between the targeted and the current deployment model
	 */
	public void compareComponents(){
		journal.log(Level.INFO, ">> Comparing components ...");
		Boolean match=false;
		for(ComponentInstance ni : currentDM.getComponentInstances()){
			secondloop:{ for(ComponentInstance ni2 : targetDM.getComponentInstances()){
				match=ni.equals(ni2);
				if(ni.equals(ni2)){
					matchingComponents.put(ni, ni2);
					break secondloop;
				}
			}}
		if(!match){
			removedComponents.add(ni);
		}
		}
		//add the rest
		addComponents();
	}

	private void removedComponent(InternalComponentInstance ni){
		removedComponents.add(ni);
		//create action
	}

	private void addComponents(){
		addedComponents =  new ArrayList<ComponentInstance>(targetDM.getComponentInstances());
		addedComponents.removeAll(matchingComponents.values());
	}

	/**
	 * Clean all the lists resulting from the previous comparison
	 */
	private void clean() {
		matchingVMs.clear();
		removedVMs.clear();
		addedVMs.clear();

		matchingComponents.clear();
		removedComponents.clear();
		addedComponents.clear();

		matchingRelationships.clear();
		removedRelationships.clear();
		addedRelationships.clear();

		commands.clear();
	}

	public List<ComponentInstance> getRemovedComponents(){
		return this.removedComponents;
	}

	public List<ComponentInstance> getAddedComponents(){
		return this.addedComponents;
	}

	public List<ExternalComponentInstance> getRemovedVMs(){
		return this.removedVMs;
	}

	public List<ExternalComponentInstance> getAddedVMs(){
		return this.addedVMs;
	}

	public List<RelationshipInstance> getRemovedRelationships(){
		return this.removedRelationships;
	}

	public List<RelationshipInstance> getAddedRelationships(){
		return this.addedRelationships;
	}

	public Map<RelationshipInstance,RelationshipInstance> getMatchingRelationships(){
		return this.matchingRelationships;
	}

	public Map<ComponentInstance,ComponentInstance> getMatchingComponents(){
		return this.matchingComponents;
	}

	public Map<ExternalComponentInstance,ExternalComponentInstance> getMatchingVMs(){
		return this.matchingVMs;
	}
}
