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

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cloudml.connectors.*;
import org.cloudml.connectors.util.ConfigValet;
import org.cloudml.core.*;
import org.cloudml.core.InternalComponentInstance.State;
import org.cloudml.core.actions.StandardLibrary;
import org.cloudml.core.collections.ComponentInstanceGroup;
import org.cloudml.core.collections.ExternalComponentInstanceGroup;
import org.cloudml.core.collections.InternalComponentInstanceGroup;
import org.cloudml.core.collections.RelationshipInstanceGroup;
import org.jclouds.compute.domain.Image;

import static org.cloudml.core.builders.Commons.anExternalComponentInstance;

/*
 * The deployment Engine 
 * author: Nicolas Ferry 
 * author: Hui Song
 */
public class CloudAppDeployer {

    private static final Logger journal = Logger.getLogger(CloudAppDeployer.class.getName());
    ComponentInstanceGroup<ComponentInstance<? extends Component>> alreadyDeployed = new ComponentInstanceGroup<ComponentInstance<? extends Component>>();
    ComponentInstanceGroup<ComponentInstance<? extends Component>> alreadyStarted = new ComponentInstanceGroup<ComponentInstance<? extends Component>>();
    private Deployment currentModel;
    private Deployment targetModel;

    public CloudAppDeployer() {
        System.setProperty("jsse.enableSNIExtension", "false");
    }

    /**
     * Deploy from a deployment model
     *
     * @param targetModel a deployment model
     */
    public void deploy(Deployment targetModel) {
        unlessNotNull("Cannot deploy null!", targetModel);
        this.targetModel = targetModel;
        if (currentModel == null) {
            journal.log(Level.INFO, ">> First deployment...");
            this.currentModel = targetModel;

            // Provisioning vms
            setExternalServices(targetModel.getComponentInstances().onlyExternals());

            // Deploying on vms
            // TODO: need to be recursive
            prepareComponents(targetModel.getComponentInstances(), targetModel.getRelationshipInstances());

            //Configure the components with the relationships
            configureWithRelationships(targetModel.getRelationshipInstances());

            //configuration process at SaaS level
            configureSaas(targetModel.getComponentInstances().onlyInternals());

        }
        else {
            journal.log(Level.INFO, ">> Updating a deployment...");
            CloudMLModelComparator diff = new CloudMLModelComparator(currentModel, targetModel);
            diff.compareCloudMLModel();

            //Added stuff
            setExternalServices(new ExternalComponentInstanceGroup(diff.getAddedECs()).onlyExternals());
            prepareComponents(new ComponentInstanceGroup(diff.getAddedComponents()), targetModel.getRelationshipInstances());
            configureWithRelationships(new RelationshipInstanceGroup(diff.getAddedRelationships()));
            configureSaas(new ComponentInstanceGroup<InternalComponentInstance>(diff.getAddedComponents()));

            //removed stuff
            unconfigureRelationships(diff.getRemovedRelationships());
            stopInternalComponents(diff.getRemovedComponents());
            terminateExternalServices(diff.getRemovedECs());
            updateCurrentModel(diff);
        }
    }

    private void unlessNotNull(String message, Object... obj) {
        if (obj != null) {
            for (Object o : obj) {
                if (o == null) {
                    throw new IllegalArgumentException(message);
                }
            }
        }
        else {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Update the currentModel with the targetModel and preserve all the CPSM
     * metadata
     *
     * @param diff a model comparator
     */
    public void updateCurrentModel(CloudMLModelComparator diff) {
        if (diff != null) {
            currentModel.getComponentInstances().removeAll(diff.getRemovedComponents());
            currentModel.getRelationshipInstances().removeAll(diff.getRemovedRelationships());
            currentModel.getComponentInstances().removeAll(diff.getRemovedECs());
            alreadyDeployed.removeAll(diff.getRemovedComponents());
            alreadyStarted.removeAll(diff.getRemovedComponents());

            currentModel.getComponentInstances().addAll(diff.getAddedComponents());
            currentModel.getRelationshipInstances().addAll(diff.getAddedRelationships());
            currentModel.getComponentInstances().addAll(diff.getAddedECs());
        }
        else {
            throw new IllegalArgumentException("Cannot update current model without comparator!");
        }
    }

    /**
     * Prepare the components before their start. Retrieves their resources,
     * builds their PaaS and installs them
     *
     * @param components a list of components
     * @throws MalformedURLException
     */
    private void prepareComponents(ComponentInstanceGroup<ComponentInstance<? extends Component>> components, RelationshipInstanceGroup relationships) {
        unlessNotNull("Cannot prepare for deployment null!", components);
        for (ComponentInstance<? extends Component> x : components) {
            if (x instanceof InternalComponentInstance) {
                prepareAnInternalComponent((InternalComponentInstance) x, components, relationships);
            }
        }
    }

    /**
     * Prepare a component before it starts. Retrieves its resources, builds
     * its PaaS and installs it
     *
     * @param instance an InternalComponentInstance
     * @param components a list of components
     * @throws MalformedURLException
     */
    private void prepareAnInternalComponent(InternalComponentInstance instance, ComponentInstanceGroup<ComponentInstance<? extends Component>> components, RelationshipInstanceGroup relationships) {
        unlessNotNull("Cannot deploy null!", instance);
        Connector jc;
        if (!alreadyDeployed.contains(instance) && (instance.getRequiredExecutionPlatform() != null)) {
            ExternalComponentInstance host = instance.externalHost();
            if (host.isVM()) {
                VMInstance ownerVM = host.asVM();
                VM n = ownerVM.getType();

                jc = ConnectorFactory.createIaaSConnector(n.getProvider());

                executeUploadCommands(instance, ownerVM, jc);

                executeRetrieveCommand(instance, ownerVM, jc);

                alreadyDeployed.add(instance);

                buildPaas(instance, relationships.toList());

                executeInstallCommand(instance, ownerVM, jc);

                instance.setStatus(State.INSTALLED);
                jc.closeConnection();
            }
            else{ // If the destination is a PaaS platform
                ExternalComponent ownerType = (ExternalComponent) host.getType();
                Provider p = ownerType.getProvider();
                PaaSConnector connector = ConnectorFactory.createPaaSConnector(p);
                connector.createEnvironmentWithWar(
                        instance.getName(),
                        instance.getName(),
                        host.getName(),
                        "",
                        instance.getType().getProperties().valueOf("warfile"),
                        instance.getType().hasProperty("version") ? instance.getType().getProperties().valueOf("version") : "default-cloudml"
                );
            }
        }
    }

    private void executeCommand(VMInstance owner, Connector jc, String command){
        if(!command.equals("")){
            if(!owner.getType().getOs().toLowerCase().contains("windows")){
                jc.execCommand(owner.getId(), command, "ubuntu", owner.getType().getPrivateKey());
            }else{
                if(command != null && !command.isEmpty()){
                    PowerShellConnector run = null;
                    try {
                        Thread.sleep(90000); // crappy stuff: wati for windows .... TODO
                        String cmd="powershell  \""+command+" "+owner.getType().getPrivateKey()+" "+owner.getPublicAddress()+"\"";
                        journal.log(Level.INFO, ">> Executing command: "+cmd);
                        run = new PowerShellConnector(cmd);
                        journal.log(Level.INFO, ">> STDOUT: "+run.getStandardOutput());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void executeInstallCommand(InternalComponentInstance x, VMInstance owner, Connector jc) {
        unlessNotNull("Cannot install with an argument at null", x, owner, jc);
        for (Resource r : x.getType().getResources()) {
            if(!r.getInstallCommand().equals("")){
                if(r.getRequireCredentials()){
                    jc.execCommand(owner.getId(), r.getInstallCommand()+" "+owner.getType().getProvider().getCredentials().getLogin()+" "+owner.getType().getProvider().getCredentials().getPassword(), "ubuntu", owner.getType().getPrivateKey());
                }else{
                    executeCommand(owner, jc, r.getInstallCommand());
                }
            }
        }
    }

    /**
     * Upload resources associated to an internal component on a specified
     * external component
     *
     * @param x the internal component with upload commands
     * @param owner the external component on which the resources are about to
     * be uploaded
     * @param jc the connector used to upload
     */
    private void executeUploadCommands(InternalComponentInstance x, VMInstance owner, Connector jc) {
        unlessNotNull("Cannot upload with an argument at null", x, owner, jc);
        for (Resource r : x.getType().getResources()) {
            for (String path : r.getUploadCommand().keySet()) {
                jc.uploadFile(path, r.getUploadCommand().get(path), owner.getId(), "ubuntu", owner.getType().getPrivateKey());
            }
        }
    }

    /**
     * Retrieve the resources associated to an InternalComponent
     *
     * @param x the internalComponent we want to retrieve the resource
     * @param owner the externalComponent on which the resources will be
     * downloaded
     * @param jc the connector used to trigger the commands
     */
    private void executeRetrieveCommand(InternalComponentInstance x, VMInstance owner, Connector jc) {
        unlessNotNull("Cannot retrieve resources of null!", x, owner, jc);
        for (Resource r : x.getType().getResources()) {
            if(!r.getRetrieveCommand().equals("")){
                if(r.getRequireCredentials())
                    jc.execCommand(owner.getId(), r.getRetrieveCommand()+" "+owner.getType().getProvider().getCredentials().getLogin()+""+owner.getType().getProvider().getCredentials().getPassword(), "ubuntu", owner.getType().getPrivateKey());
                else executeCommand(owner, jc, r.getRetrieveCommand());
            }
        }
    }

    /**
     * Retrieve the external component on which an component should be deployed
     *
     * @param component the component who want to retrieve the destination
     * @return
     */
    public ExternalComponentInstance getDestination(ComponentInstance component) {
        unlessNotNull("Cannot find destination of null!", component);
        if (component instanceof InternalComponentInstance) {
            InternalComponentInstance internalComponent = (InternalComponentInstance) component;
            return internalComponent.externalHost();
        }
        else {
            return (ExternalComponentInstance) component;
        }
    }


    private void buildExecutes(InternalComponentInstance x){
        VMInstance ownerVM = x.externalHost().asVM(); //need some tests but if you need to build PaaS then it means that you want to deploy on IaaS
        VM n = ownerVM.getType();

        Connector jc;
        jc = ConnectorFactory.createIaaSConnector(n.getProvider());

        ComponentInstance host=x.getHost();

        if (!alreadyDeployed.contains(host)){
            if(host.isInternal()){
                buildExecutes(host.asInternal());
                journal.log(Level.INFO, ">> Installing host: "+host.getName());
                executeRetrieveCommand(host.asInternal(), ownerVM, jc);
                executeInstallCommand(host.asInternal(), ownerVM, jc);
                host.asInternal().setStatus(State.INSTALLED);

                for (Resource r :  host.getType().getResources()) {
                    String configurationCommand = r.getConfigureCommand();
                    configure(jc, n, ownerVM, configurationCommand, r.getRequireCredentials());
                }
                host.asInternal().setStatus(State.CONFIGURED);

                for (Resource r : host.getType().getResources()) {
                    String startCommand = r.getStartCommand();
                    start(jc, n, ownerVM, startCommand);
                }
                host.asInternal().setStatus(State.RUNNING);

                alreadyStarted.add(host);
                alreadyDeployed.add(host);
            }
        }
        jc.closeConnection();
    }


    /**
     * Build the paas of an component instance
     *
     * @param x An component instance
     * @throws MalformedURLException
     */
    private void buildPaas(InternalComponentInstance x, List<RelationshipInstance> relationships) {
        unlessNotNull("Cannot deploy null", x, relationships);
        VMInstance ownerVM = x.externalHost().asVM(); //need some tests but if you need to build PaaS then it means that you want to deploy on IaaS
        VM n = ownerVM.getType();

        Connector jc;
        jc = ConnectorFactory.createIaaSConnector(n.getProvider());

        buildExecutes(x);

        for (RelationshipInstance bi : relationships) {
            if (bi.getRequiredEnd().getType().isMandatory() && x.getRequiredPorts().contains(bi.getRequiredEnd())) {
                final ComponentInstance<? extends Component> serverComponent = bi.getServerComponent();
                VMInstance owner = (VMInstance) getDestination(serverComponent);
                if (owner == null) {
                    owner = ownerVM;
                }
                if (!alreadyDeployed.contains(serverComponent)) {
                    for (Resource r : serverComponent.getType().getResources()) {
                        executeUploadCommands(x,owner,jc);
                    }
                    for (Resource r : serverComponent.getType().getResources()) {
                        jc.execCommand(owner.getId(), r.getRetrieveCommand(), "ubuntu", n.getPrivateKey());
                    }
                    for (Resource r : serverComponent.getType().getResources()) {
                        jc.execCommand(owner.getId(), r.getInstallCommand(), "ubuntu", n.getPrivateKey());
                    }

                    if ( serverComponent.isInternal()) {
                        serverComponent.asInternal().setStatus(State.INSTALLED);
                    }

                    for (Resource r :  serverComponent.getType().getResources()) {
                        String configurationCommand = r.getConfigureCommand();
                        configure(jc, n, owner, configurationCommand, r.getRequireCredentials());
                    }
                    if (serverComponent.isInternal()) {
                        serverComponent.asInternal().setStatus(State.CONFIGURED);
                    }

                    for (Resource r : serverComponent.getType().getResources()) {
                        String startCommand = r.getStartCommand();
                        start(jc, n, owner, startCommand);
                    }
                    if (serverComponent.isInternal()) {
                        serverComponent.asInternal().setStatus(State.RUNNING);
                    }

                    alreadyStarted.add(serverComponent);
                    alreadyDeployed.add(serverComponent);
                }
            }
        }
        jc.closeConnection();

    }

    /**
     * Configure and start SaaS components
     *
     * @param components a list of components
     * @throws MalformedURLException
     */
    private void configureSaas(ComponentInstanceGroup<InternalComponentInstance> components) {
        unlessNotNull("Cannot configure null!", components);
        Connector jc;
        for (InternalComponentInstance x : components) {
            if (!alreadyStarted.contains(x)) {
                ExternalComponentInstance owner = x.externalHost();
                if (owner instanceof VMInstance) { //TODO: refactor and be more generic for external component in general
                    VMInstance ownerVM = (VMInstance) owner;
                    VM n = ownerVM.getType();
                    jc = ConnectorFactory.createIaaSConnector(n.getProvider());
                    //jc=new JCloudsConnector(n.getProvider().getName(), n.getProvider().getLogin(), n.getProvider().getPasswd());

                    for (Resource r : x.getType().getResources()) {
                        String configurationCommand = r.getConfigureCommand();
                        configure(jc, n, ownerVM, configurationCommand, r.getRequireCredentials());
                    }
                    x.setStatus(State.CONFIGURED);

                    for (Resource r : x.getType().getResources()) {
                        String startCommand = r.getStartCommand();
                        start(jc, n, ownerVM, startCommand);
                    }
                    x.setStatus(State.RUNNING);

                    alreadyStarted.add(x);
                    jc.closeConnection();
                }
            }//TODO if not InternalComponent
        }
    }

    /**
     * Configure a component
     *
     * @param jc a connector
     * @param n A VM type
     * @param ni a VM instance
     * @param configurationCommand the command to configure the component,
     * parameters are: IP IPDest portDest
     */
    private void configure(Connector jc, VM n, VMInstance ni, String configurationCommand, Boolean keyRequired) {
        if (!configurationCommand.equals("")) {
            if(keyRequired)
                jc.execCommand(ni.getId(), configurationCommand+" "+ni.getType().getProvider().getCredentials().getLogin()+" "+ni.getType().getProvider().getCredentials().getPassword(), "ubuntu", n.getPrivateKey());
            else executeCommand(ni, jc, configurationCommand);
        }
    }

    /**
     * start a component
     *
     * @param jc a connector
     * @param n A VM type
     * @param ni a VM instance
     * @param startCommand the command to start the component
     */
    private void start(Connector jc, VM n, VMInstance ni, String startCommand) {
        unlessNotNull("Cannot start without connector", jc, n, ni, startCommand);
        if (!startCommand.equals("")) {
            executeCommand(ni, jc, startCommand);
        }
    }

    /**
     * Provision the VMs and upload the model with informations about the VM
     *
     * Added: Also deal with PaaS platforms
     * @param ems A list of vms
     */
    private void setExternalServices(ExternalComponentInstanceGroup ems) {
        for (ExternalComponentInstance n : ems) {
            if(n instanceof VMInstance)
                provisionAVM((VMInstance)n);
            else
                provisionAPlatform(n);
        }
    }

    /**
     * Provision a VM
     *
     * @param n a VMInstance
     */
    private void provisionAVM(VMInstance n) {
        Provider p = n.getType().getProvider();
        Connector jc = ConnectorFactory.createIaaSConnector(p);
        jc.createInstance(n);
        jc.closeConnection();
    }

    /**
     * Provision a platform.
     * So far (with only two examples of BeansTalk and CloudBees), the main PaaS
     * platforms are not necessary to be provisioned before deployment, so this 
     * method is basically used to launch a DB
     *
     * @param n: an external component instance for the platform
     */
    private void provisionAPlatform(ExternalComponentInstance n) {
        ExternalComponentInstance<? extends ExternalComponent> eci = (ExternalComponentInstance<? extends ExternalComponent>) n;
        ExternalComponent ec = eci.getType();
        Provider p = eci.getType().getProvider();

        if(ec.getServiceType() == null)
            return;
        if(ec.getServiceType().toLowerCase().equals("database")){//For now we use string but this will evolve to an enum
            PaaSConnector connector = (PaaSConnector) ConnectorFactory.createPaaSConnector(p);
            connector.createDBInstance(
                    ec.hasProperty("DB-Engine")?ec.getProperties().valueOf("DB-Engine"):null,
                    ec.hasProperty("DB-Version")?ec.getProperties().valueOf("DB-Version"):null,
                    eci.getName(),
                    ec.hasProperty("DB-Name")?ec.getProperties().valueOf("DB-Name"):null,
                    ec.getLogin(),
                    ec.getPasswd(),
                    ec.hasProperty("allocatedSize")?Integer.parseInt(ec.getProperties().valueOf("allocatedSize")):0,
                    null,
                    ec.hasProperty("securityGroup")?ec.getProperties().valueOf("securityGroup"):"");
            eci.setPublicAddress(connector.getDBEndPoint(eci.getName(), 600));

            //execute the configure command
            if(!n.getType().getResources().isEmpty()){
                for(Resource r : n.getType().getResources()){
                    if(r.getConfigureCommand() != null){
                        connector.restoreDB(eci.getPublicAddress(),"3306",ec.getLogin(), ec.getPasswd(),
                                ec.hasProperty("DB-Name")?ec.getProperties().valueOf("DB-Name"):null,r.getConfigureCommand());
                    }
                }
            }

        }
        if(ec.getServiceType().toLowerCase().equals("messagequeue")){
            PaaSConnector connector = (PaaSConnector) ConnectorFactory.createPaaSConnector(p);
            String url=connector.createQueue(n.getName());
            eci.setPublicAddress(url);
        }
    }



    /**
     * Configure components according to the relationships
     *
     * @param relationships a list of relationships
     * @throws MalformedURLException
     */
    private void configureWithRelationships(RelationshipInstanceGroup relationships) {
        //Configure on the basis of the relationships
        //parameters transmitted to the configuration scripts are "ip ipDestination portDestination"
        for (RelationshipInstance bi : relationships) {
            if(bi.getProvidedEnd().getOwner().get().isExternal()){  //For DB
                for(Resource res : bi.getType().getResources()){
                    ConfigValet valet = ConfigValet.createValet(bi, res);
                    if(valet != null)
                        valet.config();
                }
                ComponentInstance clienti = bi.getRequiredEnd().getOwner().get();
                Component client = clienti.getType();
                ComponentInstance pltfi = getDestination(clienti);
                ExternalComponent pltf = (ExternalComponent)pltfi.getType();

                PaaSConnector connector = (PaaSConnector) ConnectorFactory.createPaaSConnector(pltf.getProvider());
                connector.uploadWar(client.getProperties().valueOf("temp-warfile"), "db-reconfig", clienti.getName(), pltfi.getName(), 600);

            }
            else if (bi.getRequiredEnd().getType().isRemote()) {
                RequiredPortInstance client = bi.getRequiredEnd();
                ProvidedPortInstance server = bi.getProvidedEnd();

                Resource clientResource = bi.getType().getClientResource();
                Resource serverResource = bi.getType().getServerResource();

                String destinationIpAddress = getDestination(server.getOwner().get()).getPublicAddress();
                int destinationPortNumber = server.getType().getPortNumber();
                String ipAddress = getDestination(client.getOwner().get()).getPublicAddress();

                configureWithIP(serverResource, clientResource, server, client, destinationIpAddress, ipAddress, destinationPortNumber);
            }
        }
    }

    private void configureWithIP(Resource server, Resource client, PortInstance<? extends Port> pserver, PortInstance<? extends Port> pclient, String destinationIpAddress, String ipAddress, int destinationPortNumber){
        Connector jcServer;
        Connector jcClient;
        VMInstance ownerVMServer = (VMInstance) getDestination(pserver.getOwner().get());//TODO:generalization for PaaS
        VM VMserver = ownerVMServer.getType();
        VMInstance ownerVMClient = (VMInstance) getDestination(pclient.getOwner().get());//TODO:generalization for PaaS
        VM VMClient = ownerVMServer.getType();
        jcServer = ConnectorFactory.createIaaSConnector(VMserver.getProvider());
        jcClient = ConnectorFactory.createIaaSConnector(VMClient.getProvider());
        if(server != null){
            if(server.getRetrieveCommand() != null && !server.getRetrieveCommand().equals(""))
                jcServer.execCommand(ownerVMServer.getId(), server.getRetrieveCommand() + " \"" + ipAddress + "\" \"" + destinationIpAddress + "\" " + destinationPortNumber, "ubuntu", VMserver.getPrivateKey());
        }
        if(client !=null){
            if(client.getRetrieveCommand() != null && !client.getRetrieveCommand().equals(""))
                jcClient.execCommand(ownerVMClient.getId(), client.getRetrieveCommand() + " \"" + ipAddress + "\" \"" + destinationIpAddress + "\" " + destinationPortNumber, "ubuntu", VMClient.getPrivateKey());
        }
        if(server != null){
            if(server.getConfigureCommand() != null && !server.getConfigureCommand().equals("")){
                String configurationCommand = server.getConfigureCommand() + " \"" + ipAddress + "\" \"" + destinationIpAddress + "\" " + destinationPortNumber;
                configure(jcServer, VMserver, ownerVMServer, configurationCommand, server.getRequireCredentials());
            }
        }
        if(client != null){
            if(client.getConfigureCommand() != null && !client.getConfigureCommand().equals("")){
                String configurationCommand = client.getConfigureCommand() + " \"" + ipAddress + "\" \"" + destinationIpAddress + "\" " + destinationPortNumber;
                configure(jcClient, VMClient, ownerVMClient, configurationCommand, client.getRequireCredentials());
            }
        }
        if(server != null){
            if(server.getInstallCommand() != null && !server.getInstallCommand().equals("")){
                String installationCommand = server.getInstallCommand() + " \"" + ipAddress + "\" \"" + destinationIpAddress + "\" " + destinationPortNumber;
                configure(jcServer, VMserver, ownerVMServer, installationCommand, server.getRequireCredentials());
            }
        }
        if(client != null){
            if(client.getInstallCommand() != null && !client.getInstallCommand().equals("")){
                String installationCommand = client.getInstallCommand() + " \"" + ipAddress + "\" \"" + destinationIpAddress + "\" " + destinationPortNumber;
                configure(jcClient, VMClient, ownerVMClient, installationCommand, client.getRequireCredentials());
            }
        }
        jcServer.closeConnection();
        jcClient.closeConnection();
    }

    /**
     * Configuration with parameters IP, IPDest, PortDest
     *
     * @param r resources for configuration
     * @param i port of the component to be CONFIGURED
     * @param destinationIpAddress IP of the server
     * @param ipAddress IP of the client
     * @param destinationPortNumber port of the server
     * @throws MalformedURLException
     */
    private void configureWithIP(Resource r, PortInstance<? extends Port> i, String destinationIpAddress, String ipAddress, int destinationPortNumber) {
        Connector jc;
        if (r != null) {
            VMInstance ownerVM = (VMInstance) getDestination(i.getOwner().get());//TODO:generalization for PaaS
            VM n = ownerVM.getType();
            jc = ConnectorFactory.createIaaSConnector(n.getProvider());
            //jc=new JCloudsConnector(n.getProvider().getName(), n.getProvider().getLogin(), n.getProvider().getPasswd());
            jc.execCommand(ownerVM.getId(), r.getRetrieveCommand(), "ubuntu", n.getPrivateKey());
            if(r.getConfigureCommand() != null){
                String configurationCommand = r.getConfigureCommand() + " \"" + ipAddress + "\" \"" + destinationIpAddress + "\" " + destinationPortNumber;
                configure(jc, n, ownerVM, configurationCommand, r.getRequireCredentials());
            }
            if(r.getInstallCommand() != null){
                String installationCommand = r.getInstallCommand() + " \"" + ipAddress + "\" \"" + destinationIpAddress + "\" " + destinationPortNumber;
                configure(jc, n, ownerVM, installationCommand, r.getRequireCredentials());
            }
            jc.closeConnection();
        }
    }

    /**
     * Terminates a set of VMs
     *
     * @param vms A list of vmInstances
     * @throws MalformedURLException
     */
    private void terminateExternalServices(List<ExternalComponentInstance<? extends ExternalComponent>> vms) {
        for (ExternalComponentInstance n : vms) {
            if (n instanceof VMInstance) {
                terminateVM((VMInstance) n);
            }
        }
    }

    /**
     * Terminate a VM
     *
     * @param n A VM instance to be terminated
     * @throws MalformedURLException
     */
    private void terminateVM(VMInstance n) {
        Provider p = n.getType().getProvider();
        Connector jc = ConnectorFactory.createIaaSConnector(p);
        jc.destroyVM(n.getId());
        jc.closeConnection();
        n.setStatusAsStopped();
    }

    /**
     * Stop a list of component
     *
     * @param components a list of ComponentInstance
     * @throws MalformedURLException
     */
    private void stopInternalComponents(List<InternalComponentInstance> components) {//TODO: List<InternalComponentInstances>
        for (InternalComponentInstance a : components) {
            stopInternalComponent((InternalComponentInstance) a);
        }
    }

    /**
     * Stop a specific component instance
     *
     * @param a An InternalComponent Instance
     * @throws MalformedURLException
     */
    private void stopInternalComponent(InternalComponentInstance a) {
        VMInstance ownerVM = (VMInstance) findDestinationWhenNoRequiredExecutionPlatformSpecified(a); //TODO: to be generalized
        if (ownerVM != null) {
            VM n = ownerVM.getType();
            Connector jc = ConnectorFactory.createIaaSConnector(n.getProvider());

            for (Resource r : a.getType().getResources()) {
                String stopCommand = r.getStopCommand();
                jc.execCommand(ownerVM.getId(), stopCommand, "ubuntu", n.getPrivateKey());
            }

            jc.closeConnection();
            a.setStatus(State.CONFIGURED);
        }
    }

    /**
     * After the deletion of a relationships the configuration parameters
     * specific to this relationships are removed
     *
     * @param relationships list of relationships removed
     */
    private void unconfigureRelationships(List<RelationshipInstance> relationships) {
        for (RelationshipInstance b : relationships) {
            unconfigureRelationship(b);
        }
    }

    private void unconfigureRelationship(RelationshipInstance b) {
        if (!b.getRequiredEnd().getType().isLocal()) {
            RequiredPortInstance client = b.getRequiredEnd();
            ProvidedPortInstance server = b.getProvidedEnd();

            Resource clientResource = b.getType().getClientResource();
            Resource serverResource = b.getType().getServerResource();

            //client resources
            unconfigureWithIP(clientResource, client);

            //server resources
            unconfigureWithIP(serverResource, server);
        }
    }

    private void unconfigureWithIP(Resource r, PortInstance<? extends Port> i) {
        Connector jc;
        if (r != null) {
            VMInstance ownerVM = (VMInstance) getDestination(i.getOwner().get()); //TODO: generalize to PaaS
            VM n = ownerVM.getType();
            jc = ConnectorFactory.createIaaSConnector(n.getProvider());
            //jc=new JCloudsConnector(n.getProvider().getName(), n.getProvider().getLogin(), n.getProvider().getPasswd());
            jc.execCommand(ownerVM.getId(), r.getStopCommand(), "ubuntu", n.getPrivateKey());;
            jc.closeConnection();
        }
    }

    /**
     * To initialise a deployment Model as the model of the current system if
     * the system is already RUNNING
     *
     * @param current the current Deployment model
     */
    public void setCurrentModel(Deployment current) {
        this.currentModel = current;
        Connector jc;
        for (VMInstance vm : currentModel.getComponentInstances().onlyVMs()) {
            if (vm.getPublicAddress().equals("")) {
                jc = ConnectorFactory.createIaaSConnector(vm.getType().getProvider());
                jc.updateVMMetadata(vm);
            }
        }
    }

    /**
     * Find the destination of an ComponentInstance
     *
     * @param a an instance of component
     * @return a VMInstance
     */
    private ExternalComponentInstance findDestinationWhenNoRequiredExecutionPlatformSpecified(InternalComponentInstance a) {
        if (getDestination(a) != null) {
            return getDestination(a);
        }
        else {
            for (RelationshipInstance b : currentModel.getRelationshipInstances()) {
                if (a.getRequiredPorts().contains(b.getRequiredEnd()) && b.getRequiredEnd().getType().isLocal()) {
                    return getDestination(b.getProvidedEnd().getOwner().get());
                }
                if (a.getProvidedPorts().contains(b.getProvidedEnd()) && b.getProvidedEnd().getType().isLocal()) {
                    return getDestination(b.getRequiredEnd().getOwner().get());
                }
            }
            return null;
        }
    }

    /**
     * Method to scale out a VM within the same provider
     * Create a snapshot of the VM and then configure the bindings
     * @param vmi an instance of VM
     */
    public void scaleOut(VMInstance vmi){
        Connector c=ConnectorFactory.createIaaSConnector(vmi.getType().getProvider());
        StandardLibrary lib=new StandardLibrary();

        //1. create snapshot of an instance
        String ID=c.createImage(vmi); //TODO: should check if the image already exist
        c.closeConnection();
        //2. instantiate the new VM using the newly created snapshot
        VM existingVM=vmi.asExternal().asVM().getType();
        VM v=currentModel.getComponents().onlyVMs().firstNamed(existingVM.getName()+"-fromImage");
        if(v == null){//in case a type for the snapshot has already been created
            String name=lib.createUniqueComponentInstanceName(currentModel,existingVM);
            v=new VM(name+"-fromImage",existingVM.getProvider());
            v.setGroupName(existingVM.getGroupName());
            v.setRegion(existingVM.getRegion());
            v.setImageId(ID);
            v.setLocation(existingVM.getLocation());
            v.setMinRam(existingVM.getMinRam());
            v.setMinCores(existingVM.getMinCores());
            v.setMinStorage(existingVM.getMinStorage());
            v.setSecurityGroup(existingVM.getSecurityGroup());
            v.setSshKey(existingVM.getSshKey());
            v.setPrivateKey(existingVM.getPrivateKey());
            v.setProvider(existingVM.getProvider());
            v.setProvidedExecutionPlatforms(existingVM.getProvidedExecutionPlatforms().toList());
            currentModel.getComponents().add(v);
        }
        VMInstance ci=lib.provision(currentModel,v).asExternal().asVM();
        Connector c2=ConnectorFactory.createIaaSConnector(v.getProvider());
        c2.createInstance(ci);
        c2.closeConnection();

        //3. update the deployment model by cloning the PaaS and SaaS hosted on the replicated VM
        Map<InternalComponentInstance, InternalComponentInstance> duplicatedGraph=duplicateHostedGraph(vmi, ci);

        //4. configure the new VM
        //execute the configuration bindings
        Set<ComponentInstance> listOfAllComponentImpacted= new HashSet<ComponentInstance>();
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

        //execute configure commands on the components
        for(ComponentInstance ici: listOfAllComponentImpacted){
            if(ici.isInternal()){
                c2=ConnectorFactory.createIaaSConnector(v.getProvider());
                for(Resource r: ici.getType().getResources()){
                    configure(c2, ci.getType(), ici.asInternal().externalHost().asVM(), r.getConfigureCommand(),false);
                }
                c2.closeConnection();
            }
        }

        //execute start commands on the components
        for(ComponentInstance ici: listOfAllComponentImpacted){
            if(ici.isInternal()){
                c2=ConnectorFactory.createIaaSConnector(v.getProvider());
                for(Resource r: ici.getType().getResources()){
                    start(c2,ci.getType(),ici.asInternal().externalHost().asVM(),r.getStartCommand());
                }
                c2.closeConnection();
            }
        }
    }


    private void manageDuplicatedRelationships(RelationshipInstanceGroup rig, Set<ComponentInstance> listOfAllComponentImpacted){
        if(rig != null){
            configureWithRelationships(rig);
            for(RelationshipInstance ri: rig){
                listOfAllComponentImpacted.add(ri.getClientComponent());
                listOfAllComponentImpacted.add(ri.getServerComponent());
            }
        }
    }

    private Map<InternalComponentInstance, InternalComponentInstance> duplicateHostedGraph(VMInstance vmiSource,VMInstance vmiDestination){
        InternalComponentInstanceGroup icig= currentModel.getComponentInstances().onlyInternals().hostedOn(vmiSource);
        StandardLibrary lib=new StandardLibrary();
        return lib.replicateSubGraph(currentModel, icig, vmiDestination);
    }

    /**
     * To scale our a VM on another provider (kind of bursting)
     * @param vmi the vm instance to scale out
     * @param provider the provider where we want to burst
     */
    public void scaleOut(VMInstance vmi, Provider provider){
        Connector c=ConnectorFactory.createIaaSConnector(provider);
        StandardLibrary lib=new StandardLibrary();

        VMInstance ci= lib.replicateComponentInstance(targetModel,vmi,null).asExternal().asVM();
        VM existingVM=ci.asExternal().asVM().getType();
        VM v=new VM(existingVM.getName()+"-scaled",existingVM.getProvider());
        v.setGroupName(existingVM.getGroupName());
        v.setImageId(existingVM.getImageId());
        v.setLocation(existingVM.getLocation());
        v.setMinRam(existingVM.getMinRam());
        v.setMinCores(existingVM.getMinCores());
        v.setMinStorage(existingVM.getMinStorage());
        v.setSecurityGroup(existingVM.getSecurityGroup());
        v.setSshKey(existingVM.getSshKey());
        v.setPrivateKey(existingVM.getPrivateKey());
        v.setProvider(provider);
        targetModel.getComponents().add(v);
        ci.setType(v);
        c.createInstance(ci.asExternal().asVM());

        duplicateHostedGraph(vmi,ci);

        deploy(targetModel);
    }

}
