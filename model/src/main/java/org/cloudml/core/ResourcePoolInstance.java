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

import org.cloudml.core.actions.StandardLibrary;
import org.cloudml.core.util.OwnedBy;
import org.cloudml.core.visitors.Visitor;

import java.util.List;
import java.util.Map;

/**
 * Created by nicolasf on 24.02.15.
 */
public class ResourcePoolInstance extends WithResources implements DeploymentElement, OwnedBy<Deployment> {

    private int nbOfReplicats;
    private int minReplicats =1;
    private int maxReplicats;
    StandardLibrary lib = new StandardLibrary();
    private final OptionalOwner<Deployment> owner;

    private String type;

    private List<VMInstance> baseInstances;
    private List<VMInstance> replicats;
    private List<ComponentInstance> excluded;

    public ResourcePoolInstance(int nbOfReplicats, int maxReplicats, int minReplicats, List<VMInstance> baseInstances, String type){
        this.maxReplicats = maxReplicats;
        this.minReplicats = minReplicats;
        this.nbOfReplicats =nbOfReplicats;
        this.baseInstances=baseInstances;
        this.type=type;
        this.owner = new OptionalOwner<Deployment>();
    }

    public void remove(VMInstance vmi, Deployment target, int nb){
        for(int i =0; i < nb; i++){
            remove(vmi, target);
        }
    }

    public void remove(VMInstance vmi, Deployment target){
        if(nbOfReplicats > minReplicats){
            if(baseInstances.contains(vmi)){
                target.getComponentInstances().onlyVMs().remove(vmi);
                nbOfReplicats--;
            }else{
                throw new IllegalArgumentException("This VM is not part of the Pool");
            }
        }else{
            throw new IllegalStateException("You already reached the minimum number of replicats");
        }
    }

    public void replicate(VMInstance vmi, Deployment target, int nb){
        for(int i =0; i < nb; i++){
            replicate(vmi, target);
        }
    }

    public void replicate(int n, Deployment target){
        for(VMInstance i:baseInstances){
            replicate(i,target,n);
        }
    }

    public Map<InternalComponentInstance, InternalComponentInstance> replicate(VMInstance vmi, Deployment target){
        if(nbOfReplicats < maxReplicats){
            if(baseInstances.contains(vmi)){
                VMInstance newVM=lib.cloneVM(vmi, target);
                Map<InternalComponentInstance, InternalComponentInstance> duplicatedGraph=lib.replicateSubGraph(target, vmi, newVM);
                nbOfReplicats++;
                return duplicatedGraph;
            }else{
                throw new IllegalArgumentException("This VM is not part of the Pool");
            }
        }else{
            throw new IllegalStateException("You already reached the maximum number of replicats");
        }
    }

    public int getNbOfReplicats() {
        return nbOfReplicats;
    }

    public void setNbOfReplicats(int nbOfReplicats) {
        this.nbOfReplicats = nbOfReplicats;
    }

    public List<VMInstance> getBaseInstances() {
        return baseInstances;
    }

    public void setBaseInstances(List<VMInstance> baseInstances) {
        this.baseInstances = baseInstances;
    }

    public int getMaxReplicats() {
        return maxReplicats;
    }

    public void setMaxReplicats(int maxReplicats) {
        this.maxReplicats = maxReplicats;
    }

    public int getMinReplicats() {
        return minReplicats;
    }

    public void setMinReplicats(int minReplicats) {
        this.minReplicats = minReplicats;
    }

    public String getType() {
        return type;
    }

    public List<VMInstance> getReplicats() {
        return replicats;
    }

    @Override
    public Deployment getDeployment() {
        return getOwner().get();
    }

    @Override
    public String getQualifiedName() {
        return getOwner().getName() + "::" + getName();
    }

    @Override
    public OptionalOwner<Deployment> getOwner() {
        return owner;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitResourcePoolInstance(this);
    }

    public List<ComponentInstance> getExcluded() {
        return excluded;
    }

    public void setExcluded(List<ComponentInstance> excluded) {
        this.excluded = excluded;
    }
}
