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
package org.cloudml.connectors;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cloudml.core.ComponentInstance;
import org.cloudml.core.Property;
import org.cloudml.core.VM;
import org.cloudml.core.VMInstance;
import org.jclouds.ContextBuilder;


import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.RunScriptOnNodesException;
import org.jclouds.compute.domain.*;
import org.jclouds.domain.LoginCredentials;
import org.jclouds.io.Payloads;
import org.jclouds.logging.config.NullLoggingModule;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.compute.options.NovaTemplateOptions;
import org.jclouds.openstack.nova.v2_0.features.ImageApi;
import org.jclouds.openstack.nova.v2_0.features.ServerApi;
import org.jclouds.ssh.SshClient;
import org.jclouds.sshj.config.SshjSshClientModule;

import static org.jclouds.compute.options.RunScriptOptions.Builder.blockOnComplete;
import static org.jclouds.compute.options.RunScriptOptions.Builder.overrideLoginCredentials;

import static org.jclouds.compute.predicates.NodePredicates.runningInGroup;
import static org.jclouds.scriptbuilder.domain.Statements.exec;

/**
 * Created by Nicolas Ferry on 08.05.14.
 */
public class OpenStackConnector implements Connector{

    private static final Logger journal = Logger.getLogger(JCloudsConnector.class.getName());
    private ComputeServiceContext computeContext;
    private ComputeService novaComputeService;
    private final String endpoint;
    private NovaApi serverApi;
    private HashMap<String,Object> runtimeInformation=new HashMap<String, Object>();

    public OpenStackConnector(String endPoint,String provider,String login,String secretKey){
        this.endpoint=endPoint;
        journal.log(Level.INFO, ">> Connecting to "+provider+" ...");
        Iterable<Module> modules = ImmutableSet.<Module> of(
                new SshjSshClientModule(),
                new NullLoggingModule());
        ContextBuilder builder = ContextBuilder.newBuilder(provider)
                .endpoint(endPoint)
                .credentials(login, secretKey)
                .modules(modules);
        journal.log(Level.INFO, ">> Authenticating ...");
        computeContext=builder.buildView(ComputeServiceContext.class);
        novaComputeService= computeContext.getComputeService();
        serverApi=builder.buildApi(NovaApi.class);
    }

    /**
     * Retrieve information about a VM
     * @param name name of a VM
     * @return data about a VM
     */
    public ComputeMetadata getVMByName(String name){
        for(ComputeMetadata n : novaComputeService.listNodes()){
            if(n.getName() != null &&  n.getName().equals(name))
                return n;
        }
        return null;
    }

    /**
     * retrieve the list of VMs
     * @return a list of information about each VM
     */
    public Set<? extends ComputeMetadata> listOfVMs(){
        return novaComputeService.listNodes();
    }

    /**
     * Retrieve data about a VM
     * @param id id of a VM
     * @return Information about a VM
     */
    public NodeMetadata getVMById(String id){
        return novaComputeService.getNodeMetadata(id);
    }

    /**
     * Close the connection
     */
    public void closeConnection(){
        novaComputeService.getContext().close();
        journal.log(Level.INFO, ">> Closing connection ...");
    }

    /**
     * Create an image of the VM
     * @param vmi a VMInstance
     * @return id of the image
     */
    public String createImage(VMInstance vmi){
        String id="";
        Image i = checkIfImageExist(vmi.getName()+"-image");
        if(i == null){
            NodeMetadata nm=getVMById(vmi.getId());
            ServerApi serverApi1=serverApi.getServerApiForZone(vmi.getType().getRegion());
            journal.log(Level.INFO, ">> Creating an image of VM: "+vmi.getName()+" "+nm.getId()+" :: "+vmi.getType().getRegion());
            id=serverApi1.createImageFromServer(vmi.getName()+"-image",nm.getId().split("/")[1]);
            String status="";
            while (!status.toLowerCase().equals("available")){
                Image im=novaComputeService.getImage(vmi.getType().getRegion()+"/"+id);
                status=im.getStatus().name();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    journal.log(Level.SEVERE, e.getMessage());
                }
            }
            journal.log(Level.INFO, ">> Image created with ID: "+id);
        }else{
            id=i.getId().split("/")[1];
        }

        return id;
    }

    @Override
    public void startVM(VMInstance a) {
        journal.log(Level.INFO, ">> Starting VM: "+a.getName());
        try{
            novaComputeService.resumeNode(a.getId());
        }catch(Exception exception){
            journal.log(Level.INFO, ">> Check VM status!");
        }
    }

    @Override
    public void stopVM(VMInstance a) {
        journal.log(Level.INFO, ">> Stopping VM: "+a.getName());
        try{
            novaComputeService.suspendNode(a.getId());
        }catch(Exception exception){
            journal.log(Level.INFO, ">> Check VM status!");
        }
    }

    /**
     * Create a snapshot of the volume attached to the VM
     * @param vmi
     * @return id of the snapshot
     */
    public String createSnapshot(VMInstance vmi){
        return createImage(vmi);
    }

    /**
     * Check if an image exist
     * @param imageID the id of the image
     * @return
     */
    public Boolean imageIsAvailable(String imageID){
        if(novaComputeService.getImage(imageID) != null)
            return true;
        return false;
    }

    /**
     * Prepare the credential builder
     * @param login
     * @param key
     * @return
     */
    private org.jclouds.domain.LoginCredentials.Builder initCredentials(String login, String key){
        String contentKey;
        org.jclouds.domain.LoginCredentials.Builder b= LoginCredentials.builder();
        File f = new File(key);
        if(f.exists() && !f.isDirectory()) {
            try {
                contentKey = FileUtils.readFileToString(new File(key));
                b.user(login);
                b.noPassword();
                b.privateKey(contentKey);
            } catch (IOException e) {
                journal.log(Level.SEVERE, e.getMessage());
            }
        }else{
            b.user(login);
            b.noPassword();
            b.privateKey(key);
        }
        return b;
    }

    /**
     * Upload a file on a selected VM
     * @param sourcePath path to the file to be uploaded
     * @param destinationPath path to the file to be created
     * @param VMId Id of a VM
     * @param login user login
     * @param key key to connect
     */
    public void uploadFile(String sourcePath, String destinationPath, String VMId, String login, String key){
        org.jclouds.domain.LoginCredentials.Builder b=initCredentials(login, key);
        SshClient ssh = novaComputeService.getContext().utils().sshForNode().apply(NodeMetadataBuilder.fromNodeMetadata(getVMById(VMId)).credentials(b.build()).build());
        try {
            ssh.connect();
            ssh.put(destinationPath, Payloads.newPayload(new File(sourcePath)));
        } finally {
            if (ssh != null)
                ssh.disconnect();
            journal.log(Level.INFO, ">> File uploaded!");
        }

    }


    /**
     * Execute a command on a group of VMs
     * @param group name of the group
     * @param command the command to be executed
     * @param login username
     * @param key sshkey
     * @throws RunScriptOnNodesException
     */
    public void execCommandInGroup(String group, String command, String login, String key) throws RunScriptOnNodesException {
        journal.log(Level.INFO, ">> executing command...");
        journal.log(Level.INFO, ">> "+ command);

        org.jclouds.domain.LoginCredentials.Builder b=initCredentials(login, key);
        Map<? extends NodeMetadata, ExecResponse> responses = novaComputeService.runScriptOnNodesMatching(
                runningInGroup(group),
                exec(command),
                overrideLoginCredentials(b.build())
                        .runAsRoot(false)
                        .wrapInInitScript(false));// run command directly

        for(Map.Entry<? extends NodeMetadata, ExecResponse> r : responses.entrySet())
            journal.log(Level.INFO, ">> "+r.getValue());
    }

    /**
     * Execute a command on a specified node
     * @param id id of the node
     * @param command the command to be executed
     * @param login username
     * @param key sshkey for connection
     */
    public void execCommand(String id, String command, String login, String key){
        journal.log(Level.INFO, ">> executing command...");
        journal.log(Level.INFO, ">> "+ command);

        org.jclouds.domain.LoginCredentials.Builder b=initCredentials(login, key);
        ExecResponse response = novaComputeService.runScriptOnNode(
                id,
                exec(command),
                overrideLoginCredentials(b.build())
                        .runAsRoot(false)
                        .wrapInInitScript(false));// run command directly

        journal.log(Level.INFO, ">> "+response.getOutput());
    }

    /**
     * Update the runtime metadata of a VM if already deployed
     * @param a description of a VM
     */
    public void updateVMMetadata(VMInstance a){
        ComputeMetadata cm= getVMByName(a.getName());
        if(cm != null){
            if(((NodeMetadata)cm).getPublicAddresses().size() > 0)
                a.setPublicAddress(((NodeMetadata)cm).getPublicAddresses().iterator().next());
            a.setId(cm.getId());
            a.setStatus(ComponentInstance.State.RUNNING);
        }
    }


    /**
     * retrieve the list of images avaialbels
     * @return the list of available images
     */
    public Set<? extends Image> listOfImages(){
        return novaComputeService.listImages();
    }

    /**
     * Search for an image with that name
     * @param name name if the image
     * @return an Image
     */
    public Image checkIfImageExist(String name){
        for(Image i : listOfImages()){
            if(i.getName().equals(name))
                return i;
        }
        return null;
    }


    /**
     * Provision a VM
     * @param a description of the VM to be created
     * @return
     */
    public HashMap<String,Object> createInstance(VMInstance a){
        ComponentInstance.State state = ComponentInstance.State.UNRECOGNIZED;
        VM vm = a.getType();
        ComputeMetadata cm= getVMByName(a.getName());
		/* UPDATE THE MODEL */
        if(cm != null){
            updateVMMetadata(a);
            state=ComponentInstance.State.RUNNING;
        }else{
            Template template=null;
            NodeMetadata nodeInstance = null;
            String groupName="cloudml-instance";
            if(!vm.getGroupName().equals(""))
                groupName= vm.getGroupName();

            TemplateBuilder templateBuilder = novaComputeService.templateBuilder();

            if(vm.getRegion() == null)
                journal.log(Level.INFO, ">> No Region " );

            if(!vm.getImageId().equals("")){
                String fullId="";
                if((vm.getRegion() != null) && (!vm.getRegion().equals("")) && (!vm.getImageId().contains("/"))){
                    journal.log(Level.INFO, ">> Region: " +vm.getRegion());
                    fullId=vm.getRegion()+"/"+vm.getImageId();
                }else{
                    fullId=vm.getImageId();
                }
                if(imageIsAvailable(fullId)){
                    templateBuilder.imageId(fullId);
                }else{
                    journal.log(Level.INFO, ">> There is no image with the following ID: " +fullId);
                }
            }

            journal.log(Level.INFO, ">> Provisioning a VM ...");

            if (vm.getMinRam() > 0)
                templateBuilder.minRam(vm.getMinRam());
            if (vm.getMinCores() > 0)
                templateBuilder.minCores(vm.getMinCores());

            String region="";
            if(vm.getRegion() != null){
                if(!vm.getRegion().equals("")){
                    region=vm.getRegion()+"/";
                }
            }

            if (!vm.getLocation().equals(""))
                templateBuilder.locationId(region+vm.getLocation());
            if (!vm.getOs().equals(""))
                templateBuilder.imageDescriptionMatches(vm.getOs());
            else templateBuilder.osFamily(OsFamily.UBUNTU);
            templateBuilder.os64Bit(vm.getIs64os());

            template = templateBuilder.build();
            journal.log(Level.INFO, ">> VM type: " + template.getHardware().getId() + " on location: " + template.getLocation().getId());
            a.getProperties().add(new Property("ProviderInstanceType", template.getHardware().getId()));
            a.getProperties().add(new Property("location", template.getLocation().getId()));


            template.getOptions().as(NovaTemplateOptions.class).keyPairName(vm.getSshKey());
            template.getOptions().as(NovaTemplateOptions.class).securityGroups(vm.getSecurityGroup());
            template.getOptions().as(NovaTemplateOptions.class).overrideLoginUser(a.getName());
            ArrayList<String> names=new ArrayList<String>();
            names.add(a.getName());
            template.getOptions().as(NovaTemplateOptions.class).nodeNames(names);
            ArrayList<String> sgNames=new ArrayList<String>();
            sgNames.add(vm.getSecurityGroup());
            template.getOptions().as(NovaTemplateOptions.class).securityGroupNames(sgNames);

            template.getOptions().blockUntilRunning(true);

            try {
                Set<? extends NodeMetadata> nodes = novaComputeService.createNodesInGroup(groupName, 1, template);
                nodeInstance = nodes.iterator().next();
                journal.log(Level.INFO, ">> Running VM: "+nodeInstance.getName()+" Id: "+ nodeInstance.getId() +" with public address: "+nodeInstance.getPublicAddresses() +
                        " on OS:"+nodeInstance.getOperatingSystem()+ " " + nodeInstance.getCredentials().identity+":"+nodeInstance.getCredentials().getUser()+":"+nodeInstance.getCredentials().getPrivateKey());

            } catch (RunNodesException e) {
                journal.log(Level.SEVERE, e.getMessage());
                //a.setStatusAsError();
                state = ComponentInstance.State.ERROR;
            }

            if(nodeInstance.getPublicAddresses().iterator().hasNext()){
                //a.setPublicAddress(nodeInstance.getPublicAddresses().iterator().next());
                runtimeInformation.put("publicAddress",nodeInstance.getPublicAddresses().iterator().next());
            }else{
                //a.setPublicAddress(nodeInstance.getPrivateAddresses().iterator().next());
                runtimeInformation.put("publicAddress",nodeInstance.getPrivateAddresses().iterator().next());
            }

            if(nodeInstance.getHardware().getProcessors().iterator().hasNext())
                a.setCore((int)nodeInstance.getHardware().getProcessors().iterator().next().getCores());

            a.setId(nodeInstance.getId());

            state = ComponentInstance.State.RUNNING;
        }
        runtimeInformation.put("status",state);
        return runtimeInformation;
    }

    /**
     * Terminate a specified VM
     * @param id id of the VM
     */
    public void destroyVM(String id){
        novaComputeService.destroyNode(id);
    }


}
