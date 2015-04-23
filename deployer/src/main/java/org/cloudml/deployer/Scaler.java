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

import com.amazonaws.util.StringInputStream;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.connectors.Connector;
import org.cloudml.connectors.ConnectorFactory;
import org.cloudml.core.*;
import org.cloudml.core.actions.StandardLibrary;
import org.cloudml.core.collections.ProvidedExecutionPlatformGroup;
import org.cloudml.core.collections.RelationshipInstanceGroup;
import org.cloudml.mrt.Coordinator;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ferrynico on 02/12/2014.
 */
public class Scaler {

    private static final Logger journal = Logger.getLogger(Scaler.class.getName());

    protected Deployment currentModel;
    protected Coordinator coordinator;
    StandardLibrary lib = new StandardLibrary();
    VMInstance ci;
    protected CloudAppDeployer dep;

    public Scaler(Deployment currentModel, Coordinator coordinator, CloudAppDeployer dep){
        this.currentModel=currentModel;
        this.coordinator=coordinator;
        this.dep=dep;
    }


    protected VM findVMGenerated(String fromName, String extension){
        for(VM v: currentModel.getComponents().onlyVMs()){
            if(v.getName().contains(fromName) && v.getName().contains(extension)){
                return v;
            }
        }
        return null;
    }

    private VM createNewInstanceOfVMFromImage(VMInstance vmi){
        VM existingVM=vmi.asExternal().asVM().getType();
        //VM v=currentModel.getComponents().onlyVMs().firstNamed(existingVM.getName()+"-fromImage");
        VM v = findVMGenerated(existingVM.getName(),"fromImage");
        if(v == null){//in case a type for the snapshot has already been created
            String name=lib.createUniqueComponentInstanceName(currentModel,existingVM);
            v=new VM(name+"-fromImage",existingVM.getProvider());
            v.setGroupName(existingVM.getGroupName());
            v.setRegion(existingVM.getRegion());
            v.setImageId("tempID");
            v.setLocation(existingVM.getLocation());
            v.setMinRam(existingVM.getMinRam());
            v.setMinCores(existingVM.getMinCores());
            v.setMinStorage(existingVM.getMinStorage());
            v.setSecurityGroup(existingVM.getSecurityGroup());
            v.setSshKey(existingVM.getSshKey());
            v.setPrivateKey(existingVM.getPrivateKey());
            v.setProvider(existingVM.getProvider());
            ProvidedExecutionPlatformGroup pepg=new ProvidedExecutionPlatformGroup();
            for(ProvidedExecutionPlatform pep: existingVM.getProvidedExecutionPlatforms()){
                ArrayList<Property> pg=new ArrayList<Property>();
                for(Property property: pep.getOffers()){
                    Property prop=new Property(property.getName());
                    prop.setValue(property.getValue());
                    pg.add(prop);
                }
                ProvidedExecutionPlatform p =new ProvidedExecutionPlatform(name+"-"+pep.getName(),pg);
                pepg.add(p);
            }
            v.setProvidedExecutionPlatforms(pepg.toList());
            currentModel.getComponents().add(v);
        }
        ci=lib.provision(currentModel,v).asExternal().asVM();
        return v;
    }

    private Map<InternalComponentInstance, InternalComponentInstance> duplicateHostedGraph(Deployment d, VMInstance vmiSource,VMInstance vmiDestination){
        //InternalComponentInstanceGroup icig= currentModel.getComponentInstances().onlyInternals().hostedOn(vmiSource);
        StandardLibrary lib=new StandardLibrary();
        return lib.replicateSubGraph(d, vmiSource, vmiDestination);
    }

    protected void manageDuplicatedRelationships(RelationshipInstanceGroup rig, Set<ComponentInstance> listOfAllComponentImpacted){
        if(rig != null){
            dep.configureWithRelationships(rig);
            for(RelationshipInstance ri: rig){
                listOfAllComponentImpacted.add(ri.getClientComponent());
                listOfAllComponentImpacted.add(ri.getServerComponent());
            }
        }
    }


    protected void configureBindingOfImpactedComponents(Set<ComponentInstance> listOfAllComponentImpacted, Map<InternalComponentInstance, InternalComponentInstance> duplicatedGraph){
        for(InternalComponentInstance ici: duplicatedGraph.values()){
            for(ProvidedPortInstance ppi: ici.getProvidedPorts()){
                RelationshipInstanceGroup rig=currentModel.getRelationshipInstances().whereEitherEndIs(ppi);
                manageDuplicatedRelationships(rig, listOfAllComponentImpacted);
            }
            for(RequiredPortInstance rpi: ici.getRequiredPorts()){
                RelationshipInstanceGroup rig=currentModel.getRelationshipInstances().whereEitherEndIs(rpi);
                manageDuplicatedRelationships(rig, listOfAllComponentImpacted);
            }
        }
    }

    protected void configureImpactedComponents(Set<ComponentInstance> listOfAllComponentImpacted, Map<InternalComponentInstance, InternalComponentInstance> duplicatedGraph){
        for(ComponentInstance ici: listOfAllComponentImpacted){
            coordinator.updateStatusInternalComponent(ici.getName(), InternalComponentInstance.State.INSTALLED.toString(), CloudAppDeployer.class.getName());
            if(ici.isInternal()){
                Provider p=ici.asInternal().externalHost().asVM().getType().getProvider();
                Connector c2=ConnectorFactory.createIaaSConnector(p);
                for(Resource r: ici.getType().getResources()){
                    dep.configure(c2, ci.getType(), ici.asInternal().externalHost().asVM(), r.getConfigureCommand(),false);
                }
                c2.closeConnection();
            }
            coordinator.updateStatusInternalComponent(ici.getName(), InternalComponentInstance.State.CONFIGURED.toString(), CloudAppDeployer.class.getName());
        }
    }

    protected void startImpactedComponents(Set<ComponentInstance> listOfAllComponentImpacted, Map<InternalComponentInstance, InternalComponentInstance> duplicatedGraph){
        for(ComponentInstance ici: listOfAllComponentImpacted){
            if(ici.isInternal()){
                Provider p=ici.asInternal().externalHost().asVM().getType().getProvider();
                Connector c2=ConnectorFactory.createIaaSConnector(p);
                for(Resource r: ici.getType().getResources()){
                    dep.start(c2,ci.getType(),ici.asInternal().externalHost().asVM(),r.getStartCommand());
                }
                c2.closeConnection();
            }
            coordinator.updateStatusInternalComponent(ici.getName(), InternalComponentInstance.State.RUNNING.toString(), CloudAppDeployer.class.getName());
        }

        for(InternalComponentInstance ici: duplicatedGraph.values()){
            coordinator.updateStatusInternalComponent(ici.getName(), InternalComponentInstance.State.RUNNING.toString(), CloudAppDeployer.class.getName());
        }
    }


    public void scaleOut(VMInstance vmi, int n){
        ArrayList<VM> newbies=new ArrayList<VM>();
        ArrayList<Map<InternalComponentInstance, InternalComponentInstance>> duplicatedGraphs=new ArrayList<Map<InternalComponentInstance, InternalComponentInstance>>();
        ArrayList<Thread> ts=new ArrayList<Thread>();
        ArrayList<VMInstance> cis=new ArrayList<VMInstance>();


        for(int i=0;i<n;i++) {
            VM temp = findVMGenerated(vmi.getType().getName(),"fromImage");
            VM v=createNewInstanceOfVMFromImage(vmi);
            newbies.add(v);
            Map<InternalComponentInstance, InternalComponentInstance> duplicatedGraph = duplicateHostedGraph(currentModel, vmi, ci);
            duplicatedGraphs.add(duplicatedGraph);
            cis.add(ci);
            if (temp == null) {
                Connector c = ConnectorFactory.createIaaSConnector(vmi.getType().getProvider());
                String ID = c.createImage(vmi);
                c.closeConnection();
                v.setImageId(ID);
            } else {
                v.setImageId(temp.getImageId());
            }
        }

        for(int i=0;i<n;i++) {
            final Map<InternalComponentInstance, InternalComponentInstance> d=duplicatedGraphs.get(i);
            final VM vm=newbies.get(i);
            final String name=vmi.getName();
            final VMInstance ci=cis.get(i);
            ts.add(new Thread(){
                public void run() {
                    //once this is done we can work in parallel
                    Connector c2 = ConnectorFactory.createIaaSConnector(vm.getProvider());
                    HashMap<String, String> result = c2.createInstance(ci);
                    c2.closeConnection();
                    coordinator.updateStatusInternalComponent(ci.getName(), result.get("status"), CloudAppDeployer.class.getName());
                    coordinator.updateStatus(name, ComponentInstance.State.RUNNING.toString(), CloudAppDeployer.class.getName());
                    coordinator.updateIP(ci.getName(),result.get("publicAddress"),CloudAppDeployer.class.getName());

                    //4. configure the new VM
                    //execute the configuration bindings
                    Set<ComponentInstance> listOfAllComponentImpacted= new HashSet<ComponentInstance>();
                    configureBindingOfImpactedComponents(listOfAllComponentImpacted,d);

                    //execute configure commands on the components
                    configureImpactedComponents(listOfAllComponentImpacted,d);

                    //execute start commands on the components
                    startImpactedComponents(listOfAllComponentImpacted, d);

                    //restart components on the VM scaled
                    restartHostedComponents(ci);
                }
            });
            ts.get(i).start();
        }

        for(Thread t: ts){
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        journal.log(Level.INFO, ">> Multiple scaling completed!");
    }

    /**
     * Method to scale out a VM within the same provider
     * Create a snapshot of the VM and then configure the bindings
     *
     * @param vmi an instance of VM
     */
    public void scaleOut(VMInstance vmi) {
        Deployment tmp=currentModel.clone();
        VM temp = findVMGenerated(vmi.getType().getName(),"fromImage");
        //1. instantiate the new VM using the newly created snapshot
        VM v=createNewInstanceOfVMFromImage(vmi);

        //2. update the deployment model by cloning the PaaS and SaaS hosted on the replicated VM
        Map<InternalComponentInstance, InternalComponentInstance> duplicatedGraph=duplicateHostedGraph(currentModel,vmi, ci);

        //3. For synchronization purpose we provision once the model has been fully updated
        if(temp == null){
            Connector c = ConnectorFactory.createIaaSConnector(vmi.getType().getProvider());
            String ID=c.createImage(vmi);
            c.closeConnection();
            v.setImageId(ID);
        }else{
            v.setImageId(temp.getImageId());
        }

        Connector c2=ConnectorFactory.createIaaSConnector(v.getProvider());
        HashMap<String,String> result=c2.createInstance(ci);

        c2.closeConnection();
        coordinator.updateStatusInternalComponent(ci.getName(), result.get("status"), CloudAppDeployer.class.getName());
        coordinator.updateStatus(vmi.getName(), ComponentInstance.State.RUNNING.toString(), CloudAppDeployer.class.getName());
        coordinator.updateIP(ci.getName(),result.get("publicAddress"),CloudAppDeployer.class.getName());

        //4. configure the new VM
        //execute the configuration bindings
        Set<ComponentInstance> listOfAllComponentImpacted= new HashSet<ComponentInstance>();
        configureBindingOfImpactedComponents(listOfAllComponentImpacted,duplicatedGraph);

        //execute configure commands on the components
        configureImpactedComponents(listOfAllComponentImpacted,duplicatedGraph);

        //execute start commands on the components
        startImpactedComponents(listOfAllComponentImpacted, duplicatedGraph);

        //restart components on the VM scaled 
        restartHostedComponents(ci);

        journal.log(Level.INFO, ">> Scaling completed!");
    }

    protected void restartHostedComponents(VMInstance ci){
        for(InternalComponentInstance ici: ci.hostedComponents().onlyInternals()){
            Provider p=ci.getType().getProvider();
            Connector c2=ConnectorFactory.createIaaSConnector(p);
            for(Resource r: ici.getType().getResources()){
                dep.start(c2,ci.getType(),ici.asInternal().externalHost().asVM(),r.getStartCommand());
            }
            c2.closeConnection();
        }
    }


    private List<VM> vmFromAProvider(Provider p){
        ArrayList<VM> result=new ArrayList<VM>();
        for(VM v : currentModel.getComponents().onlyVMs()){
            if(v.getProvider().getName().equals(p.getName())){
                result.add(v);
            }
        }
        return result;
    }

    private VM findSimilarVMFromProvider(VM sampleVM, Provider p){
        VM selected=null;
        List<VM> availablesVM=vmFromAProvider(p);
        if(availablesVM.size() > 0){
            selected=availablesVM.get(0);
            for(VM v: availablesVM){
                if((v.getMinRam() >= sampleVM.getMinRam()) && (v.getMinRam() < selected.getMinRam())){
                    selected=v;
                }
            }
        }
        return selected;
    }


    public void scaleOut(ExternalComponentInstance eci, Provider provider){
        if(eci.isVM()){
            scaleOut(eci.asVM(),provider);
        }else{
            Deployment targetModel=cloneCurrentModel();
            ExternalComponentInstance eci2=targetModel.getComponentInstances().onlyExternals().firstNamed(eci.getName());
            ExternalComponent ec=eci2.getType().asExternal();
            ec.setProvider(provider);
            ec.setName(eci.getName()+"-scaled");
            dep.deploy(targetModel);
        }
    }


    private Deployment cloneCurrentModel(){
        //need to clone the model
        JsonCodec jsonCodec=new JsonCodec();
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        jsonCodec.save(currentModel,baos);

        Deployment targetModel=new Deployment();
        try {
            String aString = new String(baos.toByteArray(),"UTF-8");
            InputStream is = new StringInputStream(aString);
            targetModel = (Deployment) jsonCodec.load(is);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return targetModel;
    }

    /**
     * To scale our a VM on another provider (kind of bursting)
     *
     * @param vmi      the vm instance to scale out
     * @param provider the provider where we want to burst
     */
    public void scaleOut(VMInstance vmi, Provider provider) {
        Connector c = ConnectorFactory.createIaaSConnector(provider);
        StandardLibrary lib = new StandardLibrary();

        //need to clone the model
        Deployment targetModel=cloneCurrentModel();

        VM existingVM=findSimilarVMFromProvider(vmi.asExternal().asVM().getType(), provider);
        if(existingVM == null){
            journal.log(Level.INFO, ">> No VM available for this provider!");
            return;
        }

        //VM existingVM=vmi.asExternal().asVM().getType();
        //VM v=currentModel.getComponents().onlyVMs().firstNamed(existingVM.getName()+"-scaled");
        VM v = findVMGenerated(existingVM.getName(), "scaled");
        if(v == null){//in case a type for the snapshot has already been created
            String name=lib.createUniqueComponentInstanceName(targetModel,existingVM);
            v=new VM(name+"-scaled",provider);
            v.setGroupName(existingVM.getGroupName());
            v.setRegion(existingVM.getRegion());
            v.setImageId(existingVM.getImageId());
            v.setLocation(existingVM.getLocation());
            v.setMinRam(existingVM.getMinRam());
            v.setMinCores(existingVM.getMinCores());
            v.setMinStorage(existingVM.getMinStorage());
            v.setSecurityGroup(existingVM.getSecurityGroup());
            v.setSshKey(existingVM.getSshKey());
            v.setPrivateKey(existingVM.getPrivateKey());
            v.setProvider(provider);
            ProvidedExecutionPlatformGroup pepg=new ProvidedExecutionPlatformGroup();
            for(ProvidedExecutionPlatform pep: existingVM.getProvidedExecutionPlatforms()){
                ArrayList<Property> pg=new ArrayList<Property>();
                for(Property property: pep.getOffers()){
                    Property prop=new Property(property.getName());
                    prop.setValue(property.getValue());
                    pg.add(prop);
                }
                ProvidedExecutionPlatform p =new ProvidedExecutionPlatform(name+"-"+pep.getName(),pg);
                pepg.add(p);
            }
            v.setProvidedExecutionPlatforms(pepg.toList());
            targetModel.getComponents().add(v);
        }

        ci=lib.provision(targetModel,v).asExternal().asVM();

        //2. update the deployment model by cloning the PaaS and SaaS hosted on the replicated VM
        Map<InternalComponentInstance, InternalComponentInstance> duplicatedGraph=duplicateHostedGraph(targetModel,vmi, ci);

        dep.deploy(targetModel);
    }

}
