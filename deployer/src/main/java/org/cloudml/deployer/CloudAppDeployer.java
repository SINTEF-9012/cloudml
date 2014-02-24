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


import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cloudml.connectors.Connector;
import org.cloudml.connectors.ConnectorFactory;
import org.cloudml.connectors.JCloudsConnector;
import org.cloudml.core.*;
import org.cloudml.core.InternalComponentInstance.State;

/*
 * The deployment Engine
 * author: Nicolas Ferry
 */

public class CloudAppDeployer {

    private static final Logger journal = Logger.getLogger(JCloudsConnector.class.getName());

    ArrayList<ComponentInstance> alreadyDeployed=new ArrayList<ComponentInstance>();
    ArrayList<ComponentInstance> alreadyStarted=new ArrayList<ComponentInstance>();

    private CloudMLModel currentModel;

    public CloudAppDeployer(){
        System.setProperty ("jsse.enableSNIExtension", "false");
    }


    /**
     * Deploy from a deployment model
     * @param targetModel a deployment model
     */
    public void deploy(CloudMLModel targetModel){
        //alreadyDeployed=new ArrayList<InternalComponentInstance>();
        //alreadyStarted=new ArrayList<InternalComponentInstance>();
        if(currentModel == null){
            journal.log(Level.INFO, ">> First deployment...");
            this.currentModel=targetModel;

            // Provisioning vms
            setExternalServices(targetModel.getExternalComponentInstances());

            // Deploying on vms
            // TODO: need to be recursive
            prepareComponents(targetModel.getComponentInstances(), targetModel.getRelationshipInstances());

            //Configure the components with the relationships
            configureWithRelationships(targetModel.getRelationshipInstances());

            //configuration process at SaaS level
            configureSaas(targetModel.getComponentInstances());

        }else{
            journal.log(Level.INFO, ">> Updating a deployment...");
            CloudMLModelComparator diff=new CloudMLModelComparator(currentModel, targetModel);
            diff.compareCloudMLModel();

            //Added stuff
            setExternalServices(diff.getAddedVMs());
            prepareComponents(diff.getAddedComponents(), targetModel.getRelationshipInstances());
            configureWithRelationships(diff.getAddedRelationships());
            configureSaas(diff.getAddedComponents());

            //removed stuff
            unconfigureRelationships(diff.getRemovedRelationships());
            stopInternalComponents(diff.getRemovedComponents());
            terminateExternalServices(diff.getRemovedVMs());
            updateCurrentModel(diff);
        }
    }

    /**
     * Update the currentModel with the targetModel and preserve all the CPSM metadata
     * @param diff a model comparator
     */
    public void updateCurrentModel(CloudMLModelComparator diff){
        currentModel.getComponentInstances().removeAll(diff.getRemovedComponents());
        currentModel.getRelationshipInstances().removeAll(diff.getRemovedRelationships());
        currentModel.getExternalComponentInstances().removeAll(diff.getRemovedVMs());
        alreadyDeployed.removeAll(diff.getRemovedComponents());
        alreadyStarted.removeAll(diff.getRemovedComponents());

        currentModel.getComponentInstances().addAll(diff.getAddedComponents());
        currentModel.getRelationshipInstances().addAll(diff.getAddedRelationships());
        currentModel.getExternalComponentInstances().addAll(diff.getAddedVMs());
    }

    /**
     * Prepare the components before their start. Retrieves their resources, builds their PaaS and installs them
     * @param components a list of components
     * @throws MalformedURLException
     */
    private void prepareComponents(List<ComponentInstance> components, List<RelationshipInstance> relationships) {
        for(ComponentInstance x : components){
            if(x instanceof InternalComponentInstance){
                prepareAnInternalComponent((InternalComponentInstance) x, components, relationships);
            }
        }
    }

    /**
     * Prepare an component before it starts. Retrieves its resources, builds its PaaS and installs it
     * @param x an InternalComponentInstance
     * @param components a list of components
     * @throws MalformedURLException
     */
    private void prepareAnInternalComponent(InternalComponentInstance x, List<ComponentInstance> components, List<RelationshipInstance> relationships) {
        Connector jc;
        if(!alreadyDeployed.contains(x) && (x.getDestination() != null)){
            VMInstance ownerVM = x.getDestination();
            VM n=ownerVM.getType();

            jc=ConnectorFactory.createConnector(n.getProvider());

            for(Resource r: x.getType().getResources()){
                for(String path : r.getUploadCommand().keySet()){
                    jc.uploadFile(path, r.getUploadCommand().get(path), ownerVM.getId(), "ubuntu", n.getPrivateKey());
                }
            }

            for(Resource r: x.getType().getResources()){
                jc.execCommand(ownerVM.getId(), r.getRetrieveCommand(),"ubuntu",n.getPrivateKey());
            }
            alreadyDeployed.add(x);

            buildPaas(x, relationships);
            for(Resource r: x.getType().getResources()){
                jc.execCommand(ownerVM.getId(), r.getInstallCommand(), "ubuntu", n.getPrivateKey());
            }
            x.setStatus(State.installed);
            jc.closeConnection();
        }
    }


    /**
     * Build the paas of an component instance
     * @param x An component instance
     * @throws MalformedURLException
     */
    private void buildPaas(InternalComponentInstance x, List<RelationshipInstance> relationships) {
        VMInstance ownerVM = x.getDestination();
        VM n=ownerVM.getType();

        Connector jc;
        jc=ConnectorFactory.createConnector(n.getProvider());
        //jc=new JCloudsConnector(n.getProvider().getName(), n.getProvider().getLogin(), n.getProvider().getPasswd());


        for(RelationshipInstance bi : relationships){
            if(bi.getRequiredPortInstance().getType().getIsMandatory() && x.getRequiredPortInstances().contains(bi.getRequiredPortInstance())){
                ProvidedPortInstance p=bi.getProvidedPortInstance();
                VMInstance owner=p.getOwner().getDestination();
                if(owner == null)
                    owner=ownerVM;
                if(!alreadyDeployed.contains(p.getOwner())){
                    for(Resource r:  p.getOwner().getType().getResources()){
                        jc.execCommand(owner.getId(), r.getRetrieveCommand() ,"ubuntu",n.getPrivateKey());
                    }
                    for(Resource r:  p.getOwner().getType().getResources()){
                        jc.execCommand(owner.getId(), r.getInstallCommand(),"ubuntu",n.getPrivateKey());
                    }

                    if(p.getOwner() instanceof InternalComponentInstance){
                        ((InternalComponentInstance)p.getOwner()).setStatus(State.installed);
                    }

                    for(Resource r:  p.getOwner().getType().getResources()){
                        String configurationCommand=r.getConfigureCommand();
                        configure(jc, n, owner, configurationCommand);
                    }
                    if(p.getOwner() instanceof InternalComponentInstance){
                        ((InternalComponentInstance)p.getOwner()).setStatus(State.configured);
                    }

                    for(Resource r:  p.getOwner().getType().getResources()){
                        String startCommand=r.getStartCommand();
                        start(jc, n, owner, startCommand);
                    }
                    if(p.getOwner() instanceof InternalComponentInstance){
                        ((InternalComponentInstance)p.getOwner()).setStatus(State.running);
                    }

                    alreadyDeployed.add(p.getOwner());
                    alreadyStarted.add(p.getOwner());
                }
            }
        }
        jc.closeConnection();

    }



    /**
     * Configure and start SaaS components
     * @param components a list of components
     * @throws MalformedURLException
     */
    private void configureSaas(List<ComponentInstance> components) {
        Connector jc;
        for(ComponentInstance x : components){
            if((x instanceof InternalComponentInstance) && (!alreadyStarted.contains(x))){
                InternalComponentInstance ix=(InternalComponentInstance)x;
                VMInstance ownerVM = ix.getDestination();
                VM n=ownerVM.getType();
                jc=ConnectorFactory.createConnector(n.getProvider());
                //jc=new JCloudsConnector(n.getProvider().getName(), n.getProvider().getLogin(), n.getProvider().getPasswd());

                for(Resource r:ix.getType().getResources()){
                    String configurationCommand=r.getConfigureCommand();
                    configure(jc, n, ownerVM, configurationCommand);
                }
                ix.setStatus(State.configured);

                for(Resource r:ix.getType().getResources()){
                    String startCommand= r.getStartCommand();
                    start(jc, n, ownerVM, startCommand);
                }
                ix.setStatus(State.running);

                alreadyStarted.add(ix);
                jc.closeConnection();
            }//TODO if not InternalComponent
        }
    }


    /**
     * Configure a component
     * @param jc a connector
     * @param n A VM type
     * @param ni a VM instance
     * @param configurationCommand the command to configure the component, parameters are: IP IPDest portDest
     */
    private void configure(Connector jc, VM n, VMInstance ni, String configurationCommand){
        if(!configurationCommand.equals(""))
            jc.execCommand(ni.getId(), configurationCommand,"ubuntu",n.getPrivateKey());
    }


    /**
     * start a component
     * @param jc a connector
     * @param n A VM type
     * @param ni a VM instance
     * @param startCommand the command to start the component
     */
    private void start(Connector jc, VM n, VMInstance ni, String startCommand){
        if(!startCommand.equals(""))
            jc.execCommand(ni.getId(), startCommand,"ubuntu",n.getPrivateKey());
    }


    /**
     * Provision the VMs and upload the model with informations about the VM
     * @param vms
     * 			A list of vms
     */
    private void setExternalServices(List<ExternalComponentInstance> vms){
        for(ExternalComponentInstance n : vms){
            if(n instanceof VMInstance)
                provisionAVM((VMInstance)n);
        }
    }

    /**
     * Provision a VM
     * @param n a VMInstance
     */
    private void provisionAVM(VMInstance n){
        Provider p=n.getType().getProvider();
        Connector jc=ConnectorFactory.createConnector(p);
        jc.createInstance(n);
        jc.closeConnection();
    }


    /**
     * Configure components according to the relationships
     * @param relationships a list of relationships
     * @throws MalformedURLException
     */
    private void configureWithRelationships(List<RelationshipInstance> relationships) {
        //Configure on the basis of the relationships
        //parameters transmitted to the configuration scripts are "ip ipDestination portDestination"
        for(RelationshipInstance bi : relationships){
            if(!bi.getRequiredPortInstance().getType().getIsLocal()){
                RequiredPortInstance client=bi.getRequiredPortInstance();
                ProvidedPortInstance server=bi.getProvidedPortInstance();

                Resource clientResource=bi.getType().getClientResource();
                Resource serverResource=bi.getType().getServerResource();

                String destinationIpAddress=server.getOwner().getDestination().getPublicAddress();
                int destinationPortNumber=server.getType().getPortNumber();
                String ipAddress=client.getOwner().getDestination().getPublicAddress();

                //client resources
                configureWithIP(clientResource,client,destinationIpAddress,ipAddress,destinationPortNumber);

                //server resources
                configureWithIP(serverResource,server,destinationIpAddress,ipAddress,destinationPortNumber);
            }
        }
    }

    /**
     * Configuration with parameters IP, IPDest, PortDest
     * @param r resources for configuration
     * @param i port of the component to be configured
     * @param destinationIpAddress IP of the server
     * @param ipAddress IP of the client
     * @param destinationPortNumber port of the server
     * @throws MalformedURLException
     */
    private void configureWithIP(Resource r, PortInstance i, String destinationIpAddress, String ipAddress, int destinationPortNumber) {
        Connector jc;
        if(r != null){
            VMInstance ownerVM = i.getOwner().getDestination();
            VM n=ownerVM.getType();
            jc=ConnectorFactory.createConnector(n.getProvider());
            //jc=new JCloudsConnector(n.getProvider().getName(), n.getProvider().getLogin(), n.getProvider().getPasswd());
            jc.execCommand(ownerVM.getId(), r.getRetrieveCommand(),"ubuntu",n.getPrivateKey());
            String configurationCommand=r.getConfigureCommand()+" \""+ipAddress+"\" \""+destinationIpAddress+"\" "+destinationPortNumber;
            configure(jc, n, ownerVM, configurationCommand);
            jc.closeConnection();
        }
    }

    /**
     * Terminates a set of VMs
     * @param vms A list of vmInstances
     * @throws MalformedURLException
     */
    private void terminateExternalServices(List<ExternalComponentInstance> vms) {
        for(ExternalComponentInstance n: vms){
            if(n instanceof ExternalComponentInstance)
                terminateVM((VMInstance)n);
        }
    }

    /**
     * Terminate a VM
     * @param n A VM instance to be terminated
     * @throws MalformedURLException
     */
    private void terminateVM(VMInstance n){
        Provider p=n.getType().getProvider();
        Connector jc=ConnectorFactory.createConnector(p);
        jc.destroyVM(n.getId());
        jc.closeConnection();
        n.setStatusAsStopped();
    }

    /**
     * Stop a list of component
     * @param components a list of ComponentInstance
     * @throws MalformedURLException
     */
    private void stopInternalComponents(List<ComponentInstance> components) {//TODO: List<InternalComponentInstances>
        for(ComponentInstance a : components){
            if(a instanceof InternalComponentInstance)
                stopInternalComponent((InternalComponentInstance)a);
        }
    }

    /**
     * Stop a specific component instance
     * @param a An InternalComponent Instance
     * @throws MalformedURLException
     */
    private void stopInternalComponent(InternalComponentInstance a) {
        VMInstance ownerVM = findDestination(a);
        if(ownerVM != null){
            VM n=ownerVM.getType();
            Connector jc=ConnectorFactory.createConnector(n.getProvider());

            for(Resource r: a.getType().getResources()){
                String stopCommand=r.getStopCommand();
                jc.execCommand(ownerVM.getId(), stopCommand,"ubuntu",n.getPrivateKey());
            }

            jc.closeConnection();
            a.setStatus(State.configured);
        }
    }

    /**
     * After the deletion of a relationships the configuration parameters specific to this relationships are removed
     * @param relationships list of relationships removed
     */
    private void unconfigureRelationships(List<RelationshipInstance> relationships) {
        for(RelationshipInstance b:relationships){
            unconfigureRelationship(b);
        }
    }

    private void unconfigureRelationship(RelationshipInstance b) {
        if(!b.getRequiredPortInstance().getType().getIsLocal()){
            RequiredPortInstance client=b.getRequiredPortInstance();
            ProvidedPortInstance server=b.getProvidedPortInstance();

            Resource clientResource=b.getType().getClientResource();
            Resource serverResource=b.getType().getServerResource();

            //client resources
            unconfigureWithIP(clientResource,client);

            //server resources
            unconfigureWithIP(serverResource,server);
        }
    }

    private void unconfigureWithIP(Resource r, PortInstance i) {
        Connector jc;
        if(r != null){
            VMInstance ownerVM = i.getOwner().getDestination();
            VM n=ownerVM.getType();
            jc=ConnectorFactory.createConnector(n.getProvider());
            //jc=new JCloudsConnector(n.getProvider().getName(), n.getProvider().getLogin(), n.getProvider().getPasswd());
            jc.execCommand(ownerVM.getId(), r.getStopCommand(),"ubuntu",n.getPrivateKey());;
            jc.closeConnection();
        }
    }

    /**
     * To initialize a deployment Model as the model of the current system if the system is already running
     * @param current the current Deployment model
     */
    public void setCurrentModel(CloudMLModel current){
        this.currentModel=current;
        Connector jc;
        for(ExternalComponentInstance en: currentModel.getExternalComponentInstances()){
            if(en instanceof VMInstance){
                VMInstance n=(VMInstance)en;
                if(n.getPublicAddress().equals("")){
                    jc=ConnectorFactory.createConnector(n.getType().getProvider());
                    jc.updateVMMetadata(n);
                }
            }
        }
    }

    /**
     * Find the destination of an ComponentInstance
     * @param a an instance of component
     * @return a VMInstance
     */
    private VMInstance findDestination(ComponentInstance a){
        if(a.getDestination() != null){
            return a.getDestination();
        }else{
            for(RelationshipInstance b: currentModel.getRelationshipInstances()){
                if(a instanceof InternalComponentInstance){
                    if(((InternalComponentInstance)a).getRequiredPortInstances().contains(b.getRequiredPortInstance()) && b.getRequiredPortInstance().getType().getIsLocal())
                        return b.getProvidedPortInstance().getOwner().getDestination();
                }
                if(a.getProvidedPortInstances().contains(b.getProvidedPortInstance()) && b.getProvidedPortInstance().getType().getIsLocal())
                    return b.getRequiredPortInstance().getOwner().getDestination();
            }
            return null;
        }
    }

}
