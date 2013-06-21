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
public class DeploymentModelComparator {
	private static final Logger journal = Logger.getLogger(DeploymentModelComparator.class.getName());

	//Attributes related to model comparison
	private Map<NodeInstance,NodeInstance> matchingNodes = new Hashtable<NodeInstance,NodeInstance>();
	private List<NodeInstance> removedNodes = new ArrayList<NodeInstance>();
	private List<NodeInstance> addedNodes = new ArrayList<NodeInstance>();


	private Map<ArtefactInstance,ArtefactInstance> matchingArtefacts = new Hashtable<ArtefactInstance,ArtefactInstance>();
	private List<ArtefactInstance> removedArtefacts = new ArrayList<ArtefactInstance>();
	private List<ArtefactInstance> addedArtefacts = new ArrayList<ArtefactInstance>();


	private Map<BindingInstance,BindingInstance> matchingBindings = new Hashtable<BindingInstance,BindingInstance>();
	private List<BindingInstance> removedBindings = new ArrayList<BindingInstance>();
	private List<BindingInstance> addedBindings = new ArrayList<BindingInstance>();
	
	// The commands resulting from the diff
	private Set<String> commands = new HashSet<String>();

	private DeploymentModel currentDM;
	private DeploymentModel targetDM;

	public DeploymentModelComparator(DeploymentModel currentDM, DeploymentModel targetDM){
		this.currentDM=currentDM;
		this.targetDM=targetDM;
	}

	public void setTargetDeploymentModel(DeploymentModel targetDM){
		this.targetDM=targetDM;
		clean();
	}

	public void setCurrentDeploymentModel(DeploymentModel currentDM){
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
	public void compareDeploymentModel(){
		compareNodes();
		journal.log(Level.INFO, ">> Removed nodes :" + removedNodes.toString());
		journal.log(Level.INFO, ">> Added nodes  :" + addedNodes.toString());
		
		compareBindings();
		journal.log(Level.INFO, ">> Removed artefacts: " + removedArtefacts.toString());
		journal.log(Level.INFO, ">> Added artefacts: " + addedArtefacts.toString());
		
		compareArtefacts();
		journal.log(Level.INFO, ">> Removed bindings :" + removedBindings.toString());
		journal.log(Level.INFO, ">> Added bindings :" + addedBindings.toString());
	}
	
	/**
	 * Compares the nodes between the targeted and the current deployment model
	 */
	public void compareNodes(){
		journal.log(Level.INFO, ">> Comparing nodes ...");
		Boolean match=false;
		for(NodeInstance ni : currentDM.getNodeInstances()){
			secondloop:{ for(NodeInstance ni2 : targetDM.getNodeInstances()){
				match=ni.equals(ni2);
				if(ni.equals(ni2)){
					matchingNodes.put(ni, ni2);
					break secondloop;
				}
			}}
			if(!match){
				removeNode(ni);
			}
		}
		//add the rest
		addNodes();
	}
	
	private void removeNode(NodeInstance ni){
		removedNodes.add(ni);
		//create action
		
	}
	
	private void addNodes(){
		addedNodes =  new ArrayList<NodeInstance>(targetDM.getNodeInstances()); 
		addedNodes.removeAll(matchingNodes.values());
		for(NodeInstance n : addedNodes){
			//create action
		}
	}
	
	
	/**
	 * Compares the bindings between the targeted and the current deployment model
	 */
	public void compareBindings(){
		journal.log(Level.INFO, ">> Comparing bindings ...");
		Boolean match=false;
		for(BindingInstance ni : currentDM.getBindingInstances()){
			secondloop:{ for(BindingInstance ni2 : targetDM.getBindingInstances()){
				match=ni.equals(ni2);
				if(ni.equals(ni2)){
					matchingBindings.put(ni, ni2);
					break secondloop;
				}
			}}
			if(!match){
				removedBindings.add(ni);
			}
		}
		//add the rest
		addBindings();
	}
	
	private void removedBinding(BindingInstance ni){
		removedBindings.add(ni);
		//create action
	}

	private void addBindings(){
		addedBindings =  new ArrayList<BindingInstance>(targetDM.getBindingInstances()); 
		addedBindings.removeAll(matchingBindings.values());
		for(BindingInstance b : addedBindings){
			//create action
		}
	}
	
	/**
	 * Compares the artefacts between the targeted and the current deployment model
	 */
	public void compareArtefacts(){
		journal.log(Level.INFO, ">> Comparing artefacts ...");
		Boolean match=false;
		for(ArtefactInstance ni : currentDM.getArtefactInstances()){
			secondloop:{ for(ArtefactInstance ni2 : targetDM.getArtefactInstances()){
				match=ni.equals(ni2);
				if(ni.equals(ni2)){
					matchingArtefacts.put(ni, ni2);
					break secondloop;
				}
			}}
			if(!match){
				removedArtefacts.add(ni);
			}
		}
		//add the rest
		addArtefacts();
	}
	
	private void removedArtefact(ArtefactInstance ni){
		removedArtefacts.add(ni);
		//create action
	}
	
	private void addArtefacts(){
		addedArtefacts =  new ArrayList<ArtefactInstance>(targetDM.getArtefactInstances()); 
		addedArtefacts.removeAll(matchingArtefacts.values());
		for(ArtefactInstance a : addedArtefacts){
			//create action
		}
	}

	/**
	 * Clean all the lists resulting from the previous comparison
	 */
	private void clean() {
		matchingNodes.clear();
		removedNodes.clear();
		addedNodes.clear();

		matchingArtefacts.clear();
		removedArtefacts.clear();
		addedArtefacts.clear();

		matchingBindings.clear();
		removedBindings.clear();
		addedBindings.clear();
		
		commands.clear();
	}
	
	public List<ArtefactInstance> getRemovedArtefacts(){
		return this.removedArtefacts;
	}
	
	public List<ArtefactInstance> getAddedArtefacts(){
		return this.addedArtefacts;
	}
	
	public List<NodeInstance> getRemovedNodes(){
		return this.removedNodes;
	}
	
	public List<NodeInstance> getAddedNodes(){
		return this.addedNodes;
	}
	
	public List<BindingInstance> getRemovedBindings(){
		return this.removedBindings;
	}
	
	public List<BindingInstance> getAddedBindings(){
		return this.addedBindings;
	}
	
	public Map<BindingInstance,BindingInstance> getMatchingBindings(){
		return this.matchingBindings;
	}
	
	public Map<ArtefactInstance,ArtefactInstance> getMatchingArtefacts(){
		return this.matchingArtefacts;
	}
	
	public Map<NodeInstance,NodeInstance> getMatchingNodes(){
		return this.matchingNodes;
	}
}
