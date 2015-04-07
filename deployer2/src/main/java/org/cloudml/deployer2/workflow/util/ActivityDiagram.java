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
package org.cloudml.deployer2.workflow.util;

import org.cloudml.connectors.*;
import org.cloudml.connectors.util.CloudMLQueryUtil;
import org.cloudml.connectors.util.ConfigValet;
import org.cloudml.connectors.util.MercurialConnector;
import org.cloudml.core.*;
import org.cloudml.core.InternalComponentInstance.State;
import org.cloudml.core.collections.ComponentInstanceGroup;
import org.cloudml.core.collections.ExternalComponentInstanceGroup;
import org.cloudml.core.collections.RelationshipInstanceGroup;
import org.cloudml.deployer.CloudAppDeployer;
import org.cloudml.deployer.CloudMLModelComparator;
import org.cloudml.deployer.PuppetManifestGenerator;
import org.cloudml.deployer.Scaler;
import org.cloudml.deployer2.dsl.*;
import org.cloudml.monitoring.status.StatusConfiguration;
import org.cloudml.monitoring.status.StatusMonitor;
import org.cloudml.monitoring.synchronization.MonitoringPlatformConfiguration;
import org.cloudml.monitoring.synchronization.MonitoringSynch;
import org.cloudml.mrt.Coordinator;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * The deployment Engine
 * author: Nicolas Ferry
 * author: Hui Song
 */
public class ActivityDiagram  {

    private ArrayList<ActivityNode> nodes = new ArrayList<ActivityNode>();
    private ArrayList<ActivityEdge> edges = new ArrayList<ActivityEdge>();
    private HashMap<String, ArrayList<? extends Element>> collector = new HashMap<String, ArrayList<? extends Element>>();
    private Activity activity = new Activity();



    private static final Logger journal = Logger.getLogger(ActivityDiagram.class.getName());
    private static boolean DEBUG=false;

    static ComponentInstanceGroup<ComponentInstance<? extends Component>> alreadyDeployed = new ComponentInstanceGroup<ComponentInstance<? extends Component>>();
    ComponentInstanceGroup<ComponentInstance<? extends Component>> alreadyStarted = new ComponentInstanceGroup<ComponentInstance<? extends Component>>();
    private Deployment currentModel;
    private Deployment targetModel;
    private Coordinator coordinator;
    private boolean statusMonitorActive;
    private StatusMonitor statusMonitor; //always check if active

    public ActivityDiagram() {
        System.setProperty("jsse.enableSNIExtension", "false");
    }

    public Deployment getCurrentModel(){
        return currentModel;
    }

    public StatusMonitor getStatusMonitor(){
        return this.statusMonitor;
    }

    public Coordinator getCoordinator(){
        return coordinator;
    }

    public void setCoordinator(Coordinator coordinator){
        this.coordinator=coordinator;
    }

    /**
     * Deploy from a deployment model
     *
     * @param targetModel a deployment model
     */
    public void createActivityDiagram(Deployment targetModel) throws Exception {

        unlessNotNull("Cannot deploy null!", targetModel);
        this.targetModel = targetModel;
        //set up the monitoring
//        StatusConfiguration.StatusMonitorProperties statusMonitorProperties = StatusConfiguration.load();
//        MonitoringPlatformConfiguration.MonitoringPlatformProperties monitoringPlatformProperties = MonitoringPlatformConfiguration.load();
        if (currentModel == null) {
            journal.log(Level.INFO, ">> First deployment...");
            this.currentModel = targetModel;

//            if (statusMonitorProperties.getActivated() && statusMonitor == null) {
//                statusMonitorActive = true;
//                statusMonitor = new StatusMonitor(statusMonitorProperties.getFrequency(), false, coordinator);
//            }

            // Provisioning vms and external services
            setExternalServices(targetModel.getComponentInstances().onlyExternals());

            // Deploying on vms
            prepareComponents(targetModel.getComponentInstances(), targetModel.getRelationshipInstances());

            //Configure the components with the relationships
            configureWithRelationships(targetModel.getRelationshipInstances());

            //configuration process at SaaS level
            configureSaas(targetModel.getComponentInstances().onlyInternals());

            //Run puppet
            configureWithPuppet(targetModel.getComponentInstances().onlyInternals());
            generatePuppetManifestAndConfigure();

            //send the current deployment to the monitoring platform
//            if (monitoringPlatformProperties.isMonitoringPlatformGiven()) {
//                MonitoringSynch.sendCurrentDeployment(monitoringPlatformProperties.getIpAddress(), currentModel);
//            }
        } else {
            journal.log(Level.INFO, ">> Updating a deployment...");
            CloudMLModelComparator diff = new CloudMLModelComparator(currentModel, targetModel);
            diff.compareCloudMLModel();

            updateCurrentModel(diff);

            //Added stuff
            setExternalServices(new ExternalComponentInstanceGroup(diff.getAddedECs()).onlyExternals());
            prepareComponents(new ComponentInstanceGroup(diff.getAddedComponents()), targetModel.getRelationshipInstances());
            configureWithRelationships(new RelationshipInstanceGroup(diff.getAddedRelationships()));
            configureSaas(new ComponentInstanceGroup<InternalComponentInstance>(diff.getAddedComponents()));
            configureWithPuppet(targetModel.getComponentInstances().onlyInternals());

            //removed stuff
            unconfigureRelationships(diff.getRemovedRelationships());
            stopInternalComponents(diff.getRemovedComponents());
            terminateExternalServices(diff.getRemovedECs());


            //send the changes to the monitoring platform
//            if (monitoringPlatformProperties.isMonitoringPlatformGiven()) {
//                MonitoringSynch.sendAddedComponents(monitoringPlatformProperties.getIpAddress(), diff.getAddedECs(), diff.getAddedComponents());
//                boolean result = MonitoringSynch.sendRemovedComponents(monitoringPlatformProperties.getIpAddress(), diff.getRemovedECs(), diff.getRemovedComponents());
//                if (!result && monitoringPlatformProperties.isMonitoringPlatformGiven()){
//                    MonitoringSynch.sendCurrentDeployment(monitoringPlatformProperties.getIpAddress(), currentModel);
//                }
//            }
        }

        activity.setEdges(edges);
        activity.setNodes(nodes);

        //start the monitoring of VMs
//        if (statusMonitorActive) {
//            statusMonitor.start();
//        }

        //MODAClouds specific code
//        if(targetModel.getProperties().get("sla_url") != null && targetModel.getProperties().get("agreement_id") != null){
//            Boolean status=startSLA(targetModel.getProperties().get("sla_url").getValue(),targetModel.getProperties().get("agreement_id").getValue());
//            if(status){
//                journal.log(Level.INFO, ">> SLA management started");
//            }else{
//                journal.log(Level.INFO, ">> SLA management not started");
//            }
//        }
    }

    private Boolean startSLA(String url, String agreementId){
        URL slaUrl = null;
        try {
            slaUrl = new URL(url+"/modaclouds/"+agreementId+"/start");
            HttpURLConnection httpCon = (HttpURLConnection) slaUrl.openConnection();
            httpCon.setRequestMethod("PUT");
            httpCon.setRequestProperty("Content-Type", "application/json");
            httpCon.connect();
            if(httpCon.getResponseCode() == 202){
                return true;
            }
        } catch (MalformedURLException e) {
            journal.log(Level.SEVERE, e.getMessage());
        } catch (ProtocolException e) {
            journal.log(Level.SEVERE, e.getMessage());
        } catch (IOException e) {
            journal.log(Level.SEVERE, e.getMessage());
        }
        return false;
    }


    public void deploy(Deployment targetModel, CloudMLModelComparator diff){
        unlessNotNull("Cannot deploy null!", targetModel);
        this.targetModel = targetModel;
        //set up the monitoring
        StatusConfiguration.StatusMonitorProperties statusMonitorProperties = StatusConfiguration.load();
        MonitoringPlatformConfiguration.MonitoringPlatformProperties monitoringPlatformProperties = MonitoringPlatformConfiguration.load();

        journal.log(Level.INFO, ">> Updating a deployment...");

        //Added stuff
        try {
            setExternalServices(new ExternalComponentInstanceGroup(diff.getAddedECs()).onlyExternals());
        } catch (Exception e) {
            e.printStackTrace();
        }
        prepareComponents(new ComponentInstanceGroup(diff.getAddedComponents()), targetModel.getRelationshipInstances());
        configureWithRelationships(new RelationshipInstanceGroup(diff.getAddedRelationships()));
        configureSaas(new ComponentInstanceGroup<InternalComponentInstance>(diff.getAddedComponents()));
        configureWithPuppet(targetModel.getComponentInstances().onlyInternals());

        //removed stuff
        unconfigureRelationships(diff.getRemovedRelationships());
        stopInternalComponents(diff.getRemovedComponents());
        terminateExternalServices(diff.getRemovedECs());


        //send the changes to the monitoring platform
        if (monitoringPlatformProperties.isMonitoringPlatformGiven()) {
            MonitoringSynch.sendAddedComponents(monitoringPlatformProperties.getIpAddress(), diff.getAddedECs(), diff.getAddedComponents());
            boolean result = MonitoringSynch.sendRemovedComponents(monitoringPlatformProperties.getIpAddress(), diff.getRemovedECs(), diff.getRemovedComponents());
            if (!result && monitoringPlatformProperties.isMonitoringPlatformGiven()){
                MonitoringSynch.sendCurrentDeployment(monitoringPlatformProperties.getIpAddress(), currentModel);
            }
        }
    }

    private static void unlessNotNull(String message, Object... obj) {
        if (obj != null) {
            for (Object o : obj) {
                if (o == null) {
                    throw new IllegalArgumentException(message);
                }
            }
        } else {
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
            currentModel.getExecuteInstances().removeAll(diff.getRemovedExecutes());
            alreadyDeployed.removeAll(diff.getRemovedComponents());
            alreadyStarted.removeAll(diff.getRemovedComponents());

            currentModel.getComponentInstances().addAll(diff.getAddedComponents());
            currentModel.getRelationshipInstances().addAll(diff.getAddedRelationships());
            currentModel.getComponentInstances().addAll(diff.getAddedECs());
            currentModel.getExecuteInstances().addAll(diff.getAddedExecutes());
        } else {
            throw new IllegalArgumentException("Cannot update current model without comparator!");
        }
    }

    /**
     * Prepare the components before their start. Retrieves their resources,
     * builds their PaaS and installs them
     *
     * @param components a list of components
     * @throws java.net.MalformedURLException
     */
    public void prepareComponents(ComponentInstanceGroup<ComponentInstance<? extends Component>> components, RelationshipInstanceGroup relationships) {
        unlessNotNull("Cannot prepare for deployment null!", components);
        // get VM provisioning tasks
        ArrayList<Action> provisioned = new ArrayList<Action>();
        for (ActivityNode node:ActivityBuilder.getActivity().getNodes()){
            if(node.getName().contains("provision")){
                provisioned.add((Action) node);
            }
        }
        for (ComponentInstance<? extends Component> x : components) {
            if (x instanceof InternalComponentInstance) {
                try {
                    prepareAnInternalComponent((InternalComponentInstance) x, components, relationships, provisioned);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Prepare a component before it starts. Retrieves its resources, builds
     * its PaaS and installs it
     *
     * @param instance   an InternalComponentInstance
     * @param components a list of components
     * @param provisioned a list of provisioning actions to connect them with further actions
     * @throws java.net.MalformedURLException
     */
    private void prepareAnInternalComponent(InternalComponentInstance instance, ComponentInstanceGroup<ComponentInstance<? extends Component>> components, RelationshipInstanceGroup relationships, ArrayList<Action> provisioned) throws Exception {
        unlessNotNull("Cannot deploy null!", instance);
        Connector jc;
        if (!alreadyDeployed.contains(instance) && (instance.getRequiredExecutionPlatform() != null)) {
            ExternalComponentInstance host = instance.externalHost();

            // find corresponding action in activity diagram and add outgoing edge to action
            ActivityEdge controlOut = null;
            for (Action action:provisioned){
                if (action.getInputs().get(0).equals(host)){
                    controlOut = new ActivityEdge();
                    action.addEdge(controlOut, ActivityNode.Direction.OUT);
                }
            }

            if (host.isVM()) {
                VMInstance ownerVM = host.asVM();
                VM n = ownerVM.getType();

                // TODO maybe I have to call this from actionNodeBean, because it's not just object - it is opened connection
                jc = ConnectorFactory.createIaaSConnector(n.getProvider());


                Action upload = ActivityBuilder.action(controlOut, null, ownerVM, "executeUploadCommands");
                upload.addInput(instance);
                upload.addInput(jc);
                upload.addEdge(new ActivityEdge(), ActivityNode.Direction.OUT);

                Action retrieve = ActivityBuilder.action(upload.getOutgoing().get(0), null, ownerVM, "executeRetrieveCommand");
                retrieve.addInput(instance);
                retrieve.addInput(jc);
                retrieve.addEdge(new ActivityEdge(), ActivityNode.Direction.OUT);

                alreadyDeployed.add(instance);

                ActivityEdge outgoingFromPaas = buildPaas(instance, relationships.toList(),retrieve.getOutgoing().get(0));

                Action install = ActivityBuilder.action(outgoingFromPaas, null, ownerVM, "executeInstallCommand");
                install.addInput(instance);
                install.addInput(jc);
                install.addEdge(new ActivityEdge(), ActivityNode.Direction.OUT);
                }

//                coordinator.updateStatusInternalComponent(instance.getName(), State.INSTALLED.toString(), ActivityDiagram.class.getName());
//                //instance.setStatus(State.INSTALLED);
//                jc.closeConnection();
//            } else { // If the destination is a PaaS platform
//                ExternalComponent ownerType = (ExternalComponent) host.getType();
//                Provider p = ownerType.getProvider();
//                PaaSConnector connector = ConnectorFactory.createPaaSConnector(p);
//                String stack = "";
//                if(instance.getType().hasProperty("stack"))
//                    stack = instance.getType().getProperties().valueOf("stack");
//                if(instance.hasProperty("stack"))
//                    stack = instance.getProperties().valueOf("stack");
//                if(instance.getType().hasProperty("buildpack"))
//                    stack = instance.getType().getProperties().valueOf("buildpack");
//                if(instance.hasProperty("buildpack"))
//                    stack = instance.getProperties().valueOf("buildpack");
//                String url=connector.createEnvironmentWithWar(
//                        instance.getName(),
//                        instance.getName(),
//                        host.getName(),
//                        stack,
//                        instance.getType().getProperties().valueOf("warfile"),
//                        instance.getType().hasProperty("version") ? instance.getType().getProperties().valueOf("version") : "default-cloudml"
//                );
//                host.setPublicAddress(url);
//                if(instance.hasProperty("containerSize")){
//                    String size =instance.getProperties().valueOf("containerSize");
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("containerSize", size);
//                    connector.configAppParameters(instance.getName(), params);
//                }
//                for(InternalComponentInstance ici: host.hostedComponents()){
//                    coordinator.updateStatus(ici.getName(), State.RUNNING.toString(), ActivityDiagram.class.getName());
//                }
//                coordinator.updateStatusInternalComponent(host.getName(), ComponentInstance.State.RUNNING.toString(), ActivityDiagram.class.getName());
//            }
        }
    }

    /**
     * Execute a command either on Linux or on Windows depending on the name of the OS set up in the type
     * @param owner the VMInstance on which the command will be executed
     * @param jc a connector
     * @param command the command to be executed
     */
    private static void executeCommand(VMInstance owner, Connector jc, String command) {
        if(DEBUG){
            journal.log(Level.INFO, ">> Executing command: " + command);
            journal.log(Level.INFO, ">> On VM: " + owner.getName());
            return;
        }
        if (!command.equals("")) {
            if (!owner.getType().getOs().toLowerCase().contains("windows")) {
                jc.execCommand(owner.getId(), command, "ubuntu", owner.getType().getPrivateKey());
            } else {
                if (command != null && !command.isEmpty()) {
                    PowerShellConnector run = null;
                    try {
                        Thread.sleep(90000); // crappy stuff: wati for windows .... TODO
                        String cmd = "powershell  \"" + command + " " + owner.getType().getPrivateKey() + " " + owner.getPublicAddress() + "\"";
                        journal.log(Level.INFO, ">> Executing command: " + cmd);
                        run = new PowerShellConnector(cmd);
                        journal.log(Level.INFO, ">> STDOUT: " + run.getStandardOutput());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void executeInstallCommand(InternalComponentInstance x, VMInstance owner, Connector jc, boolean debugMode) {
        if (debugMode){
            journal.log(Level.INFO, ">> Installation of " + x.getType().getName() + " is done.");
        } else {
            unlessNotNull("Cannot install with an argument at null", x, owner, jc);
            for (Resource r : x.getType().getResources()) {
                if (!r.getInstallCommand().equals("")) {
                    if (r.getRequireCredentials()) {
                        jc.execCommand(owner.getId(), CloudMLQueryUtil.cloudmlStringRecover(r.getInstallCommand(), r, x) + " " + owner.getType().getProvider().getCredentials().getLogin() + " " + owner.getType().getProvider().getCredentials().getPassword(), "ubuntu", owner.getType().getPrivateKey());
                    } else {
                        executeCommand(owner, jc, CloudMLQueryUtil.cloudmlStringRecover(r.getInstallCommand(), r, x));
                    }
                }
            }
        }
    }

    /**
     * Upload resources associated to an internal component on a specified
     * external component
     *  @param x     the internal component with upload commands
     * @param owner the external component on which the resources are about to
     *              be uploaded
     * @param jc    the connector used to upload
     * @param debugMode
     */
    public static void executeUploadCommands(InternalComponentInstance x, VMInstance owner, Connector jc, boolean debugMode) {
        if (debugMode){
            journal.log(Level.INFO, ">> Uploading of " + x.getType().getName() + " is done.");
        } else {
            journal.log(Level.INFO, ">> Upload " + x.getType().getName());
            unlessNotNull("Cannot upload with an argument at null", x, owner, jc);
            for (Resource r : x.getType().getResources()) {
                for (String path : r.getUploadCommand().keySet()) {
                    jc.uploadFile(path, r.getUploadCommand().get(path), owner.getId(), "ubuntu", owner.getType().getPrivateKey());
                }
            }
        }
    }

    /**
     * Retrieve the resources associated to an InternalComponent
     *
     * @param x     the internalComponent we want to retrieve the resource
     * @param owner the externalComponent on which the resources will be
     *              downloaded
     * @param jc    the connector used to trigger the commands
     * @param debugMode set debug mode to true or false
     */
    public static void executeRetrieveCommand(InternalComponentInstance x, VMInstance owner, Connector jc, boolean debugMode) {
        if (debugMode){
            journal.log(Level.INFO, ">> Retrieving " + x.getType().getName() + " is done.");
        } else {
            unlessNotNull("Cannot retrieve resources of null!", x, owner, jc);
            for (Resource r : x.getType().getResources()) {
                if (!r.getRetrieveCommand().equals("")) {
                    if (r.getRequireCredentials())
                        jc.execCommand(owner.getId(), CloudMLQueryUtil.cloudmlStringRecover(r.getRetrieveCommand(), r, x) + " " + owner.getType().getProvider().getCredentials().getLogin() + "" + owner.getType().getProvider().getCredentials().getPassword(), "ubuntu", owner.getType().getPrivateKey());
                    else executeCommand(owner, jc, CloudMLQueryUtil.cloudmlStringRecover(r.getRetrieveCommand(), r, x));
                }
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
        } else {
            return (ExternalComponentInstance) component;
        }
    }


    /**
     * Generate the manifest file for each VM from the manifestEntry of each puppet resource and start puppet.
     */
    private void generatePuppetManifestAndConfigure(){
        for(VMInstance vmi : currentModel.getComponentInstances().onlyVMs()){
            PuppetManifestGenerator pmg = new PuppetManifestGenerator(vmi, currentModel);
            String path=pmg.generate();
            if(path != null){
                managePuppet(pmg.getSkeleton(), vmi, vmi.getName(), path);
            }
        }
    }


    /**
     * Install puppet, manage the repo, change the hostname and execute puppet on a VM
     * @param pr the puppet resource
     * @param n the vm instance on which puppet will be installed and executed
     * @param hostname the new hostname of the vm for puppet
     * @param path the path to the puppet manifest
     */
    private void managePuppet(PuppetResource pr, VMInstance n, String hostname, String path){
        PuppetMarionnetteConnector puppet=new PuppetMarionnetteConnector(pr.getMaster(),n);
        //check if the configuration file is in the repo and manage the repo
        MercurialConnector mc=new MercurialConnector(pr.getRepo(),pr.getRepositoryKey());
        mc.addFile(path, pr.getUsername());
        //Touch the site.pp file
        puppet.touchSiteFile();
        //call the update host command
        puppet.configureHostname(n.getType().getPrivateKey(), n.getType().getLogin(),n.getType().getPasswd(),
                n.getPublicAddress(), pr.getMaster(), hostname, pr.getConfigureHostnameCommand());
        //start the puppet run
        puppet.install(n);
    }

    /**
     * For each component, execute the puppet manifest associated
     * @param components
     */
    private void configureWithPuppet(ComponentInstanceGroup<InternalComponentInstance> components){
        unlessNotNull("Cannot configure null!", components);
        Connector jc;
        for (InternalComponentInstance ic : components) {
            if(ic.externalHost().isVM()){
                for(Resource r: ic.getType().getResources()){
                    if(r instanceof PuppetResource){
                        PuppetResource pr=(PuppetResource)r;
                        if(!pr.getConfigurationFile().isEmpty()){
                            journal.log(Level.INFO, ">> Using Puppet to configure the following component: "+ic.getName());
                            VMInstance n= ic.getHost().asExternal().asVM();
                            Provider p = n.getType().getProvider();
                            managePuppet(pr,n, pr.getName(),pr.getConfigurationFile());
                        }
                    }
                }
            }
        }
    }


    private void startExecutes(InternalComponentInstance x){
        VMInstance ownerVM = x.externalHost().asVM(); //need some tests but if you need to build PaaS then it means that you want to deploy on IaaS
        VM n = ownerVM.getType();

        Connector jc = ConnectorFactory.createIaaSConnector(n.getProvider());

        ComponentInstance host = x.getHost();

        if (!alreadyStarted.contains(host)) {
            if (host.isInternal()) {
                startExecutes(host.asInternal());
                for (Resource r : host.getType().getResources()) {
                    String startCommand = CloudMLQueryUtil.cloudmlStringRecover(r.getStartCommand(), r, x);
                    start(jc, n, ownerVM, startCommand, true);
                }
                coordinator.updateStatusInternalComponent(host.getName(), State.RUNNING.toString(), ActivityDiagram.class.getName());

                alreadyStarted.add(host);
            }
        }
        jc.closeConnection();
    }

    private ActivityEdge buildExecutes(InternalComponentInstance x, ActivityEdge incoming) throws Exception {
        ActivityEdge outgoingFromBuildExecutes = incoming;
        VMInstance ownerVM = x.externalHost().asVM(); //need some tests but if you need to build PaaS then it means that you want to deploy on IaaS
        VM n = ownerVM.getType();

        Connector jc;
        jc = ConnectorFactory.createIaaSConnector(n.getProvider());

        ComponentInstance host = x.getHost();

        if (!alreadyDeployed.contains(host)) {
            if (host.isInternal()) {
                ActivityEdge outgoingTwo = buildExecutes(host.asInternal(), incoming);
                Action upload = ActivityBuilder.action(outgoingTwo, null, ownerVM, "executeUploadCommands");
                upload.addInput(host.asInternal());
                upload.addInput(jc);
                upload.addEdge(new ActivityEdge(), ActivityNode.Direction.OUT);

                Action retrieve = ActivityBuilder.action(upload.getOutgoing().get(0), null, ownerVM, "executeRetrieveCommand");
                retrieve.addInput(host.asInternal());
                retrieve.addInput(jc);
                retrieve.addEdge(new ActivityEdge(), ActivityNode.Direction.OUT);

                Action install = ActivityBuilder.action(retrieve.getOutgoing().get(0), null, ownerVM, "executeInstallCommand");
                install.addInput(host.asInternal());
                install.addInput(jc);
                install.addEdge(new ActivityEdge(), ActivityNode.Direction.OUT);

//                coordinator.updateStatusInternalComponent(host.getName(), State.INSTALLED.toString(), ActivityDiagram.class.getName());
                //host.asInternal().setStatus(State.INSTALLED);

                Action configure = null;
                for (Resource r : host.getType().getResources()) {
                    String configurationCommand = CloudMLQueryUtil.cloudmlStringRecover(r.getConfigureCommand(), r, x);
                    if (configurationCommand != null && !configurationCommand.equals("")) {
                        configure = ActivityBuilder.action(install.getOutgoing().get(0), null, ownerVM, "configure");
                        configure.addInput(n);
                        configure.addInput(jc);
                        configure.addInput(configurationCommand);
                        configure.addInput(r.getRequireCredentials());
                        configure.addEdge(new ActivityEdge(), ActivityNode.Direction.OUT);
                    }
                }
//                            if (serverComponent.isInternal()) {
//                                coordinator.updateStatusInternalComponent(serverComponent.getName(), State.CONFIGURED.toString(), ActivityDiagram.class.getName());
//                            }

                Action start = null;
                for (Resource r : host.getType().getResources()) {
                    String startCommand = CloudMLQueryUtil.cloudmlStringRecover(r.getStartCommand(), r, x);
                    if (startCommand != null && !startCommand.equals("")) {
                        start = ActivityBuilder.action(configure.getOutgoing().get(0), null, ownerVM, "start");
                        start.addInput(n);
                        start.addInput(jc);
                        start.addInput(startCommand);
                        start.addEdge(new ActivityEdge(), ActivityNode.Direction.OUT);
                    }
                }

                outgoingFromBuildExecutes =
                        start == null ?
                        (configure == null? install.getOutgoing().get(0): configure.getOutgoing().get(0)) :
                        start.getOutgoing().get(0);

//                coordinator.updateStatusInternalComponent(host.getName(), State.RUNNING.toString(), ActivityDiagram.class.getName());
                //host.asInternal().setStatus(State.RUNNING);

                alreadyStarted.add(host);
                alreadyDeployed.add(host);
            }
        }
        jc.closeConnection();
        return outgoingFromBuildExecutes;
    }


    /**
     * Build the paas of an component instance
     *
     * @param x An component instance
     * @param incoming edge that comes from previous Action
     */
    private ActivityEdge buildPaas(InternalComponentInstance x, List<RelationshipInstance> relationships, ActivityEdge incoming) {
        ActivityEdge outgoingFromPaas = incoming;
        unlessNotNull("Cannot deploy null", x, relationships);
        VMInstance ownerVM = x.externalHost().asVM(); //need some tests but if you need to build PaaS then it means that you want to deploy on IaaS
        VM n = ownerVM.getType();

        Connector jc;
        jc = ConnectorFactory.createIaaSConnector(n.getProvider());

        ActivityEdge outgoingFromBuildExecutes = null;
        try {
            outgoingFromBuildExecutes = buildExecutes(x, incoming);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (RelationshipInstance bi : relationships) {
            if (bi.getRequiredEnd().getType().isMandatory() && x.getRequiredPorts().contains(bi.getRequiredEnd())) {
                final ComponentInstance<? extends Component> serverComponent = bi.getServerComponent();
                if( getDestination(serverComponent).isVM()){
                    VMInstance owner = (VMInstance) getDestination(serverComponent);
                    if (owner == null) {
                        owner = ownerVM;
                    }
                    if (!alreadyDeployed.contains(serverComponent)) {
                        try {
                            Action upload = null;
                            for (Resource r : serverComponent.getType().getResources()) {
                                upload = ActivityBuilder.action(outgoingFromBuildExecutes, null, owner, "executeUploadCommands");
                                upload.addInput(serverComponent.asInternal());
                                upload.addInput(jc);
                                upload.addEdge(new ActivityEdge(), ActivityNode.Direction.OUT);
                            }
                            Action retrieve = null;
                            for (Resource r : serverComponent.getType().getResources()) {
                                retrieve = ActivityBuilder.action(upload.getOutgoing().get(0), null, owner, "executeRetrieveCommand");
                                retrieve.addInput(serverComponent.asInternal());
                                retrieve.addInput(jc);
                                retrieve.addEdge(new ActivityEdge(), ActivityNode.Direction.OUT);
                            }
                            Action install = null;
                            for (Resource r : serverComponent.getType().getResources()) {
                                install = ActivityBuilder.action(retrieve.getOutgoing().get(0), null, owner, "executeInstallCommand");
                                install.addInput(serverComponent.asInternal());
                                install.addInput(jc);
                                install.addEdge(new ActivityEdge(), ActivityNode.Direction.OUT);
                            }

//                        if (serverComponent.isInternal()) {
//                            coordinator.updateStatusInternalComponent(serverComponent.getName(), State.INSTALLED.toString(), ActivityDiagram.class.getName());
//                        }
                            Action configure = null;
                            for (Resource r : serverComponent.getType().getResources()) {
                                String configurationCommand = r.getConfigureCommand();
                                if (configurationCommand != null && !configurationCommand.equals("")) {
                                    configure = ActivityBuilder.action(install.getOutgoing().get(0), null, owner, "configure");
                                    configure.addInput(n);
                                    configure.addInput(jc);
                                    configure.addInput(configurationCommand);
                                    configure.addInput(r.getRequireCredentials());
                                    configure.addEdge(new ActivityEdge(), ActivityNode.Direction.OUT);
                                }
                            }
//                            if (serverComponent.isInternal()) {
//                                coordinator.updateStatusInternalComponent(serverComponent.getName(), State.CONFIGURED.toString(), ActivityDiagram.class.getName());
//                            }

                            Action start = null;
                            for (Resource r : serverComponent.getType().getResources()) {
                                String startCommand = CloudMLQueryUtil.cloudmlStringRecover(r.getStartCommand(), r, x);
                                if (startCommand != null && !startCommand.equals("")) {
                                    start = ActivityBuilder.action(configure.getOutgoing().get(0), null, owner, "start");
                                    start.addInput(n);
                                    start.addInput(jc);
                                    start.addInput(startCommand);
                                    start.addEdge(new ActivityEdge(), ActivityNode.Direction.OUT);
                                }
                            }
//                            if (serverComponent.isInternal()) {
//                                coordinator.updateStatusInternalComponent(serverComponent.getName(), State.RUNNING.toString(), ActivityDiagram.class.getName());
//                                //serverComponent.asInternal().setStatus(State.RUNNING);
//                            }
                            outgoingFromPaas =
                                    start == null ?
                                    (configure == null? install.getOutgoing().get(0): configure.getOutgoing().get(0)) :
                                    start.getOutgoing().get(0);
                            alreadyStarted.add(serverComponent);
                            alreadyDeployed.add(serverComponent);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        jc.closeConnection();
        return outgoingFromPaas;

    }

    /**
     * Configure and start SaaS components
     *
     * @param components a list of components
     * @throws java.net.MalformedURLException
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
                        String configurationCommand = CloudMLQueryUtil.cloudmlStringRecover(r.getConfigureCommand(), r, x);
                        configure(jc, n, ownerVM, configurationCommand, r.getRequireCredentials(), true);
                    }
                    coordinator.updateStatusInternalComponent(x.getName(), State.CONFIGURED.toString(), ActivityDiagram.class.getName());
                    //x.setStatus(State.CONFIGURED);

                    for (Resource r : x.getType().getResources()) {
                        String startCommand = CloudMLQueryUtil.cloudmlStringRecover(r.getStartCommand(), r, x);
                        start(jc, n, ownerVM, startCommand, true);
                    }
                    coordinator.updateStatusInternalComponent(x.getName(), State.RUNNING.toString(), ActivityDiagram.class.getName());
                    //x.setStatus(State.RUNNING);

                    alreadyStarted.add(x);
                    jc.closeConnection();
                }
            }//TODO if not InternalComponent
        }
    }

    /**
     * Configure a component
     *
     * @param jc                   a connector
     * @param n                    A VM type
     * @param ni                   a VM instance
     * @param configurationCommand the command to configure the component,
     *                             parameters are: IP IPDest portDest
     */
    public static void configure(Connector jc, VM n, VMInstance ni, String configurationCommand, Boolean keyRequired, boolean debugMode) {
        if (debugMode){
            journal.log(Level.INFO, "Configuration of " + ni.getName() + " is done");
        } else {
            if (!configurationCommand.equals("")) {
                if (keyRequired)
                    jc.execCommand(ni.getId(), configurationCommand + " " + ni.getType().getProvider().getCredentials().getLogin() + " " + ni.getType().getProvider().getCredentials().getPassword(), "ubuntu", n.getPrivateKey());
                else executeCommand(ni, jc, configurationCommand);
            }
        }
    }

    /**
     * start a component
     *
     * @param jc           a connector
     * @param n            A VM type
     * @param ni           a VM instance
     * @param startCommand the command to start the component
     */
    public static void start(Connector jc, VM n, VMInstance ni, String startCommand, boolean debugMode) {
        if (debugMode){
            journal.log(Level.INFO, ni.getName() + " started");
        } else {
            unlessNotNull("Cannot start without connector", jc, n, ni, startCommand);
            if (!startCommand.equals("")) {
                executeCommand(ni, jc, startCommand);
            }
        }
    }

    /**
     * Provision the VMs and upload the model with informations about the VM
     * <p/>
     * Added: Also deal with PaaS platforms
     *
     * @param ems A list of vms
     */
    //TODO do not delete - this is expanded version of external components provisioning with all data objects and flows
//    public void setExternalServices(ExternalComponentInstanceGroup ems) throws Exception {
//
//        // *  initial node
//        // *! final node
//        // ===> control flow
//        // ---> data flow
//        // | fork
//        // |& join
//        // |_| object node
//        // () action
//        // P parameter
//
//        // *===>|
//        ActivityInitialNode controlStart = ActivityBuilder.controlStart();
////        System.out.println("Initial: " + ActivityBuilder.getActivity().toString());
//        // |===>
//        Fork controlFork = (Fork) ActivityBuilder.forkOrJoin(ems.size(), false, true);
//        ActivityBuilder.connectInitialToFork(controlStart, controlFork);
////        System.out.println("Control fork: " + ActivityBuilder.getActivity().toString());
//        // P--->
//        ActivityParameterNode dataStart = (ActivityParameterNode) ActivityBuilder.objectNode("Model",
//                                                                ActivityBuilder.Edges.OUT,
//                                                                ActivityBuilder.ObjectNodeType.PARAMETER);
//        dataStart.setParameter(getCurrentModel());
////        System.out.println("Input parameter: " + ActivityBuilder.getActivity().toString());
//
//        DatastoreNode datastore = (DatastoreNode) ActivityBuilder.objectNode("Datastore",
//                                                                            ActivityBuilder.Edges.NOEDGES,
//                                                                            ActivityBuilder.ObjectNodeType.DATASTORE);
//        datastore.setIncoming(dataStart.getOutgoing());
//        datastore.addObject(ems);
////        System.out.println("Datastore: " + ActivityBuilder.getActivity().toString());
//
//        Fork dataStoreFork = (Fork) ActivityBuilder.forkOrJoin(1, true, true);
//        ActivityBuilder.connectObjectToFork(datastore, dataStoreFork);
////        System.out.println("Data fork model: " + ActivityBuilder.getActivity().toString());
//        // |_|--->|
//        ObjectNode vmObjects = ActivityBuilder.objectNode("ExternalComponents",
//                                                            ActivityBuilder.Edges.NOEDGES,
//                                                            ActivityBuilder.ObjectNodeType.OBJECT);
//        vmObjects.setIncoming(dataStoreFork.getOutgoing());
////        System.out.println("External components object: " + ActivityBuilder.getActivity().toString());
//        ArrayList<Object> list = new ArrayList<Object>();
//        list.addAll(((ExternalComponentInstanceGroup) datastore.getObjects().get(0)).toList());
//        vmObjects.setObjects(list);
//        // |--->
//        Fork dataFork = (Fork) ActivityBuilder.forkOrJoin(ems.size(), true, true);
//        ActivityBuilder.connectObjectToFork(vmObjects, dataFork);
////        System.out.println("VMs data fork: " + ActivityBuilder.getActivity().toString());
//        // |--->()
//
//
//
//        Action action;
//        ActivityEdge controlEdge;
//        ActivityEdge dataEdge;
//        ArrayList<Action> provisioning = new ArrayList<Action>(ems.size());
////        System.out.println(ems.size());
//
//        for (ExternalComponentInstance n : ems) {
//            controlEdge = controlFork.getOutgoing().get(ems.toList().indexOf(n));
//            dataEdge = dataFork.getOutgoing().get(ems.toList().indexOf(n));
//
//            if (n instanceof VMInstance) {
//                action = ActivityBuilder.action(controlEdge, dataEdge, n, "provisionAVM");
//                provisioning.add(action);
//
////                provisionAVM((VMInstance) n);
//            } else {
//                action = ActivityBuilder.action(controlEdge, dataEdge, n, "provisionAPlatform");
//                provisioning.add(action);
//            }
////            System.out.println("Action n: " + ActivityBuilder.getActivity().toString());
//        }
//
//        ObjectNode IPs = ActivityBuilder.objectNode("Public Addresses",
//                                                    ActivityBuilder.Edges.OUT,
//                                                    ActivityBuilder.ObjectNodeType.OBJECT);
//        Join dataJoin = (Join) ActivityBuilder.forkOrJoin(ems.size(), true, false);
//        ActivityBuilder.connectJoinToObject(dataJoin, IPs);
//
//        ActivityParameterNode dataStop = (ActivityParameterNode) ActivityBuilder.objectNode("End of provisioning",
//                                                                                ActivityBuilder.Edges.NOEDGES,
//                                                                                ActivityBuilder.ObjectNodeType.PARAMETER);
//        dataStop.setParameter(IPs.getObjects());
//        dataStop.setIncoming(IPs.getOutgoing());
//
//        ActivityFinalNode finalNode = ActivityBuilder.controlStop();
//        Join controlJoin = (Join) ActivityBuilder.forkOrJoin(ems.size(), false, false);
//        ActivityBuilder.connectJoinToFinal(controlJoin, finalNode);
//
//        for (Action a:provisioning){
//            int index = provisioning.indexOf(a);
//            controlEdge = controlJoin.getIncoming().get(index);
//            dataEdge = dataJoin.getIncoming().get(index);
//            a.addEdge(controlEdge, ActivityNode.Direction.OUT);
//            a.addEdge(dataEdge, ActivityNode.Direction.OUT);
//        }
//
//    }

    public void setExternalServices(ExternalComponentInstanceGroup ems) throws Exception{

        // start and fork
        ActivityInitialNode controlStart = ActivityBuilder.controlStart();   //System.out.println("Initial: " + ActivityBuilder.getActivity().toString());
        Fork controlFork = (Fork) ActivityBuilder.forkOrJoin(ems.size(), false, true);
        ActivityBuilder.connectInitialToFork(controlStart, controlFork);     //System.out.println("Fork: " + ActivityBuilder.getActivity().toString());

        // list of actions with input data
        Action action;
        ActivityEdge controlEdge;
        ArrayList<Action> provisioning = new ArrayList<Action>(ems.size());

        for (ExternalComponentInstance n : ems) {
            controlEdge = controlFork.getOutgoing().get(ems.toList().indexOf(n));
            if (n instanceof VMInstance) {
                action = ActivityBuilder.action(controlEdge, null, n, "provisionAVM");
                provisioning.add(action);
            } else {
                action = ActivityBuilder.action(controlEdge, null, n, "provisionAPlatform");
                provisioning.add(action);
            }
//            System.out.println("Action n: " + ActivityBuilder.getActivity().toString());
        }

        Join dataJoin = (Join) ActivityBuilder.forkOrJoin(ems.size(), true, false);
        // container of all IPs
        ObjectNode IPs = ActivityBuilder.objectNode("Public Addresses",
                ActivityBuilder.Edges.NOEDGES,
                ActivityBuilder.ObjectNodeType.OBJECT);  // System.out.println("Object: " + ActivityBuilder.getActivity().toString());
        ActivityBuilder.connectJoinToObject(dataJoin, IPs);  //System.out.println("Data join: " + ActivityBuilder.getActivity().toString());

//        // control join and finish
//        ActivityFinalNode finalNode = ActivityBuilder.controlStop();  //System.out.println("Final: " + ActivityBuilder.getActivity().toString());
//        Join controlJoin = (Join) ActivityBuilder.forkOrJoin(ems.size(), false, false);
//        ActivityBuilder.connectJoinToFinal(controlJoin, finalNode);   //System.out.println("Control Join n: " + ActivityBuilder.getActivity().toString());

        // connect actions with control and data join
        ActivityBuilder.connectActionsWithJoinNodes(provisioning, null, dataJoin);  //System.out.println("Update actions: " + ActivityBuilder.getActivity().toString());
    }

    /**
     * Provision a VM
     *
     * @param action action which holds VMInstance as input
     */
    public static void provisionAVM(Action action, boolean debugMode) {
        VMInstance n = (VMInstance) action.getInputs().get(0);

        if(debugMode){
            journal.log(Level.INFO, ">> Provisioning of: " + n.getName() + " is done");
            action.addOutput("debug IP");
        } else {
            Provider p = n.getType().getProvider();
            Connector jc = ConnectorFactory.createIaaSConnector(p);
//        coordinator.updateStatus(n.getName(), ComponentInstance.State.PENDING.toString(), ActivityDaigram.class.getName());
            HashMap<String, String> runtimeInformation = jc.createInstance(n);
//        coordinator.updateStatus(n.getName(), runtimeInformation.get("status"), ActivityDaigram.class.getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // save IP
            action.addOutput(runtimeInformation.get("publicAddress"));

//        coordinator.updateIP(n.getName(),runtimeInformation.get("publicAddress"),ActivityDaigram.class.getName());
            //enable the monitoring of the new machine
//        if (statusMonitorActive) {
//            statusMonitor.attachModule(jc);
//        }
            jc.closeConnection();
        }
    }

    /**
     * Provision a platform.
     * So far (with only two examples of BeansTalk and CloudBees), the main PaaS
     * platforms are not necessary to be provisioned before deployment, so this
     * method is basically used to launch a DB
     *
     * @param action action which holds ExternalComponentInstance as input
     */
    public static void provisionAPlatform(Action action, boolean debugMode) {
        ExternalComponentInstance n = (ExternalComponentInstance) action.getInputs().get(0);

        if(debugMode){
            journal.log(Level.INFO, ">> Provisioning of: " + n.getName() + " is done");
            action.addOutput("debug IP");
        } else {
            ExternalComponentInstance<? extends ExternalComponent> eci = (ExternalComponentInstance<? extends ExternalComponent>) n;
            ExternalComponent ec = eci.getType();
            Provider p = eci.getType().getProvider();

            if (ec.getServiceType() == null)
                return;
            if (ec.getServiceType().toLowerCase().equals("database")) {//For now we use string but this will evolve to an enum
                PaaSConnector connector = (PaaSConnector) ConnectorFactory.createPaaSConnector(p);
                connector.createDBInstance(
                        ec.hasProperty("DB-Engine") ? ec.getProperties().valueOf("DB-Engine") : null,
                        ec.hasProperty("DB-Version") ? ec.getProperties().valueOf("DB-Version") : null,
                        eci.getName(),
                        ec.hasProperty("DB-Name") ? ec.getProperties().valueOf("DB-Name") : null,
                        ec.getLogin(),
                        ec.getPasswd(),
                        ec.hasProperty("allocatedSize") ? Integer.parseInt(ec.getProperties().valueOf("allocatedSize")) : 0,
                        null,
                        ec.hasProperty("securityGroup") ? ec.getProperties().valueOf("securityGroup") : "");
                String pa = connector.getDBEndPoint(eci.getName(), 600);
                eci.setPublicAddress(pa);
                action.addOutput(pa);
//            coordinator.updateIP(n.getName(),pa,ActivityDaigram.class.getName());
//            coordinator.updateStatus(n.getName(), ComponentInstance.State.RUNNING.toString(), ActivityDaigram.class.getName());
                //execute the configure command
            /*if (!n.getType().getResources().isEmpty()) {
                for (Resource r : n.getType().getResources()) {
                    if (r.getConfigureCommand() != null) {
                        connector.restoreDB(eci.getPublicAddress(), "3306", ec.getLogin(), ec.getPasswd(),
                                ec.hasProperty("DB-Name") ? ec.getProperties().valueOf("DB-Name") : null, r.getConfigureCommand());
                    }
                }
            }*/

            }
            if (ec.getServiceType().toLowerCase().equals("messagequeue")) {
                PaaSConnector connector = (PaaSConnector) ConnectorFactory.createPaaSConnector(p);
                String url = connector.createQueue(n.getName());
                eci.setPublicAddress(url);
                action.addOutput(url);
            }
        }
    }


    /**
     * Configure components according to the relationships
     *
     * @param relationships a list of relationships
     * @throws java.net.MalformedURLException
     */
    protected void configureWithRelationships(RelationshipInstanceGroup relationships) {
        //Configure on the basis of the relationships
        //parameters transmitted to the configuration scripts are "ip ipDestination portDestination"
        for (RelationshipInstance bi : relationships) {
            if (bi.getProvidedEnd().getOwner().get().isExternal()) {  //For DB
                for (Resource res : bi.getType().getResources()) {
                    ConfigValet valet = ConfigValet.createValet(bi, res);
                    if (valet != null)
                        valet.config();
                    else if(res.hasProperty("db-binding-alias")){
                        coordinator.updateStatus(bi.getProvidedEnd().getOwner().get().getName(), ComponentInstance.State.PENDING.toString(), ActivityDiagram.class.getName());
                        try{
                            Provider p = ((ExternalComponent) bi.getProvidedEnd().getOwner().get().getType()).getProvider();
                            PaaSConnector connector = ConnectorFactory.createPaaSConnector(p);
                            String alias = res.getProperties().valueOf("db-binding-alias");
                            connector.bindDbToApp(bi.getRequiredEnd().getOwner().getName(), bi.getProvidedEnd().getOwner().getName(), alias);
                            coordinator.updateStatus(bi.getProvidedEnd().getOwner().get().getName(), ComponentInstance.State.RUNNING.toString(), ActivityDiagram.class.getName());
                        }catch(Exception ex){
                            ex.printStackTrace();
                            journal.log(Level.INFO, ">> db-binding only works for PaaS databases" );
                        }
                    }

                }
                ComponentInstance clienti = bi.getRequiredEnd().getOwner().get();
                Component client = clienti.getType();
                ComponentInstance pltfi = getDestination(clienti);
                if(pltfi.isExternal()){
                    ExternalComponent pltf = (ExternalComponent) pltfi.getType();
                    if(!pltf.isVM()){
                        if(client.hasProperty("temp-warfile")) {
                            try {
                                PaaSConnector connector = (PaaSConnector) ConnectorFactory.createPaaSConnector(pltf.getProvider());
                                connector.uploadWar(client.getProperties().valueOf("temp-warfile"), "db-reconfig", clienti.getName(), pltfi.getName(), 600);
                                coordinator.updateStatusInternalComponent(clienti.getName(), State.RUNNING.toString(), CloudAppDeployer.class.getName());
                            } catch (NullPointerException e) {
                                journal.log(Level.INFO, ">> no temp-warfile specified, no re-deploy");
                            }
                        }
                    }else{
                        journal.log(Level.INFO, ">> Connection IaaS to PaaS ...");
                        RequiredPortInstance clientInternal = bi.getRequiredEnd();
                        ProvidedPortInstance server = bi.getProvidedEnd();

                        Resource clientResource = bi.getType().getClientResource();

                        Connector jcClient;
                        VMInstance ownerVMClient = (VMInstance) getDestination(clientInternal.getOwner().get());
                        VM VMClient = ownerVMClient.getType();
                        jcClient = ConnectorFactory.createIaaSConnector(VMClient.getProvider());

                        String destinationIpAddress = getDestination(server.getOwner().get()).getPublicAddress();
                        int destinationPortNumber = server.getType().getPortNumber();
                        String ipAddress = getDestination(clientInternal.getOwner().get()).getPublicAddress();
                        if(clientResource == null)
                            return; // ignore configuration if there is no resource at all

                        if(clientResource.getRetrieveCommand() != null && !clientResource.getRetrieveCommand().equals(""))
                            jcClient.execCommand(ownerVMClient.getId(), clientResource.getRetrieveCommand() + " \"" + ipAddress + "\" \"" + destinationIpAddress + "\" " + destinationPortNumber, "ubuntu", VMClient.getPrivateKey());
                        if(clientResource.getConfigureCommand() != null && !clientResource.getConfigureCommand().equals("")){
                            String configurationCommand = clientResource.getConfigureCommand() + " \"" + ipAddress + "\" \"" + destinationIpAddress + "\" " + destinationPortNumber;
                            configure(jcClient, VMClient, ownerVMClient, configurationCommand, clientResource.getRequireCredentials(), true);
                        }
                        if(clientResource.getInstallCommand() != null && !clientResource.getInstallCommand().equals("")){
                            String installationCommand = clientResource.getInstallCommand() + " \"" + ipAddress + "\" \"" + destinationIpAddress + "\" " + destinationPortNumber;
                            configure(jcClient, VMClient, ownerVMClient, installationCommand, clientResource.getRequireCredentials(), true);
                        }
                        jcClient.closeConnection();
                    }
                }

            } else if (bi.getRequiredEnd().getType().isRemote()) {
                RequiredPortInstance client = bi.getRequiredEnd();
                ProvidedPortInstance server = bi.getProvidedEnd();

                Resource clientResource = bi.getType().getClientResource();
                Resource serverResource = bi.getType().getServerResource();
                this.bi=bi;
                retrieveIPandConfigure(serverResource,clientResource,server,client);
            }
            if(isPaaS2PaaS(bi)) {
                ComponentInstance clienti = bi.getRequiredEnd().getOwner().get();
                ComponentInstance s=bi.getProvidedEnd().getOwner().get().asInternal();
                ExternalComponentInstance serveri = bi.getProvidedEnd().getOwner().get().asInternal().externalHost();
                ExternalComponent pltf = clienti.asInternal().externalHost().getType();
                PaaSConnector connector = (PaaSConnector) ConnectorFactory.createPaaSConnector(pltf.getProvider());
                connector.setEnvVar(clienti.getName(), s.getName(), serveri.getPublicAddress());
            }
        }
    }

    private Boolean isPaaS2PaaS(RelationshipInstance ri){
        if(bi.getRequiredEnd().getOwner().get().isInternal()){
            if(bi.getProvidedEnd().getOwner().get().isInternal()){
                if(!bi.getRequiredEnd().getOwner().get().asInternal().externalHost().isVM()
                        && !bi.getProvidedEnd().getOwner().get().asInternal().externalHost().isVM()){
                    return true;
                }
            }
        }
        return false;
    }

    private RelationshipInstance bi = null;

    public void retrieveIPandConfigure(Resource serverResource, Resource clientResource, PortInstance<? extends Port> server, PortInstance<? extends Port> client){
        String destinationIpAddress = getDestination(server.getOwner().get()).getPublicAddress();
        int destinationPortNumber = server.getType().getPortNumber();
        String ipAddress = getDestination(client.getOwner().get()).getPublicAddress();
        if(clientResource == null && serverResource == null)
            return; // ignore configuration if there is no resource at all
        configureWithIP(serverResource, clientResource, server, client, destinationIpAddress, ipAddress, destinationPortNumber);
    }

    private void configureWithIP(Resource server, Resource client,
                                 PortInstance<? extends Port> pserver, PortInstance<? extends Port> pclient, String destinationIpAddress, String ipAddress, int destinationPortNumber) {
        if(DEBUG){
            journal.log(Level.INFO, ">> Configure with IP ");
            return;
        }
        Connector jcServer;
        Connector jcClient;
        VMInstance ownerVMServer = (VMInstance) getDestination(pserver.getOwner().get());//TODO:generalization for PaaS
        VM VMserver = ownerVMServer.getType();
        VMInstance ownerVMClient = (VMInstance) getDestination(pclient.getOwner().get());//TODO:generalization for PaaS
        VM VMClient = ownerVMClient.getType();
        jcServer = ConnectorFactory.createIaaSConnector(VMserver.getProvider());
        jcClient = ConnectorFactory.createIaaSConnector(VMClient.getProvider());

        if(server != null){
            if(server.getRetrieveCommand() != null && !server.getRetrieveCommand().equals(""))
                jcServer.execCommand(ownerVMServer.getId(), CloudMLQueryUtil.cloudmlStringRecover(server.getRetrieveCommand(), server, bi) + " \"" + ipAddress + "\" \"" + destinationIpAddress + "\" " + destinationPortNumber, "ubuntu", VMserver.getPrivateKey());
        }
        if(client !=null){
            if(client.getRetrieveCommand() != null && !client.getRetrieveCommand().equals(""))
                jcClient.execCommand(ownerVMClient.getId(), CloudMLQueryUtil.cloudmlStringRecover(client.getRetrieveCommand(), client, bi) + " \"" + ipAddress + "\" \"" + destinationIpAddress + "\" " + destinationPortNumber, "ubuntu", VMClient.getPrivateKey());
        }
        if(server != null){
            if(server.getConfigureCommand() != null && !server.getConfigureCommand().equals("")){
                String configurationCommand = CloudMLQueryUtil.cloudmlStringRecover(server.getConfigureCommand(), server, bi) + " \"" + ipAddress + "\" \"" + destinationIpAddress + "\" " + destinationPortNumber;
                configure(jcServer, VMserver, ownerVMServer, configurationCommand, server.getRequireCredentials(), true);
            }
        }

        if(client != null){
            if(client.getConfigureCommand() != null && !client.getConfigureCommand().equals("")){
                String configurationCommand = CloudMLQueryUtil.cloudmlStringRecover(client.getConfigureCommand(), client, bi) + " \"" + ipAddress + "\" \"" + destinationIpAddress + "\" " + destinationPortNumber;
                configure(jcClient, VMClient, ownerVMClient, configurationCommand, client.getRequireCredentials(), true);
            }
        }

        if(server != null){
            if(server.getInstallCommand() != null && !server.getInstallCommand().equals("")){
                String installationCommand = CloudMLQueryUtil.cloudmlStringRecover(server.getInstallCommand(), server, bi) + " \"" + ipAddress + "\" \"" + destinationIpAddress + "\" " + destinationPortNumber;
                configure(jcServer, VMserver, ownerVMServer, installationCommand, server.getRequireCredentials(), true);
            }
        }
        if(client != null){
            if(client.getInstallCommand() != null && !client.getInstallCommand().equals("")){
                String installationCommand = CloudMLQueryUtil.cloudmlStringRecover(client.getInstallCommand(), client, bi) + " \"" + ipAddress + "\" \"" + destinationIpAddress + "\" " + destinationPortNumber;
                configure(jcClient, VMClient, ownerVMClient, installationCommand, client.getRequireCredentials(), true);
            }
        }
        jcServer.closeConnection();
        jcClient.closeConnection();
    }

    /**
     * Configuration with parameters IP, IPDest, PortDest
     *
     * @param r                     resources for configuration
     * @param i                     port of the component to be CONFIGURED
     * @param destinationIpAddress  IP of the server
     * @param ipAddress             IP of the client
     * @param destinationPortNumber port of the server
     * @throws java.net.MalformedURLException
     */
    private void configureWithIP(Resource r, PortInstance<? extends Port> i, String destinationIpAddress, String ipAddress, int destinationPortNumber) {
        if(DEBUG){
            journal.log(Level.INFO, ">> Configure with IP ");
            return;
        }
        Connector jc;
        if (r != null) {
            VMInstance ownerVM = (VMInstance) getDestination(i.getOwner().get());//TODO:generalization for PaaS
            VM n = ownerVM.getType();
            jc = ConnectorFactory.createIaaSConnector(n.getProvider());
            //jc=new JCloudsConnector(n.getProvider().getName(), n.getProvider().getLogin(), n.getProvider().getPasswd());
            jc.execCommand(ownerVM.getId(), r.getRetrieveCommand(), "ubuntu", n.getPrivateKey());
            if (r.getConfigureCommand() != null) {
                String configurationCommand = r.getConfigureCommand() + " \"" + ipAddress + "\" \"" + destinationIpAddress + "\" " + destinationPortNumber;
                configure(jc, n, ownerVM, configurationCommand, r.getRequireCredentials(), true);
            }
            if (r.getInstallCommand() != null) {
                String installationCommand = r.getInstallCommand() + " \"" + ipAddress + "\" \"" + destinationIpAddress + "\" " + destinationPortNumber;
                configure(jc, n, ownerVM, installationCommand, r.getRequireCredentials(), true);
            }
            jc.closeConnection();
        }
    }

    /**
     * Terminates a set of VMs
     *
     * @param vms A list of vmInstances
     * @throws java.net.MalformedURLException
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
     * @throws java.net.MalformedURLException
     */
    private void terminateVM(VMInstance n) {
        Provider p = n.getType().getProvider();
        Connector jc = ConnectorFactory.createIaaSConnector(p);
        jc.destroyVM(n.getId());
        jc.closeConnection();
        coordinator.updateStatus(n.getName(), ComponentInstance.State.STOPPED.toString(), ActivityDiagram.class.getName());
        //old way without using mrt
        //n.setStatusAsStopped();
    }

    /**
     * Stop a list of component
     *
     * @param components a list of ComponentInstance
     * @throws java.net.MalformedURLException
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
     * @throws java.net.MalformedURLException
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
            coordinator.updateStatusInternalComponent(a.getName(), State.UNINSTALLED.toString(), ActivityDiagram.class.getName());
            //a.setStatus(State.CONFIGURED);
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
            jc.execCommand(ownerVM.getId(), r.getStopCommand(), "ubuntu", n.getPrivateKey());
            ;
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
        } else {
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

    public void scaleOut(VMInstance vmi){
        Scaler scaler=new Scaler(currentModel,coordinator,new CloudAppDeployer());
        scaler.scaleOut(vmi);
    }

    public void scaleOut(VMInstance vmi,Provider provider){
        Scaler scaler=new Scaler(currentModel,coordinator,new CloudAppDeployer());
        scaler.scaleOut(vmi,provider);
    }

    public void activeDebug(){
        DEBUG=true;
    }

    public void stopDebug(){
        DEBUG=false;
    }

}
