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

import static org.jclouds.compute.predicates.NodePredicates.runningInGroup;
import static org.jclouds.compute.predicates.NodePredicates.withIds;
import static org.jclouds.compute.options.RunScriptOptions.Builder.overrideLoginCredentials;
import static org.jclouds.scriptbuilder.domain.Statements.exec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.cloudml.core.*;
import org.jclouds.Constants;
import org.jclouds.ContextBuilder;
import org.jclouds.aws.ec2.reference.AWSEC2Constants;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.RunScriptOnNodesException;
import org.jclouds.compute.domain.*;
import org.jclouds.domain.Location;
import org.jclouds.domain.LocationBuilder;
import org.jclouds.domain.LoginCredentials;
import org.jclouds.ec2.EC2Api;
import org.jclouds.ec2.compute.options.EC2TemplateOptions;
import org.jclouds.ec2.domain.Snapshot;
import org.jclouds.io.Payloads;
import org.jclouds.logging.config.NullLoggingModule;
import org.jclouds.ssh.SshClient;
import org.jclouds.sshj.config.SshjSshClientModule;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;
import static org.jclouds.Constants.*;
import org.jclouds.ec2.features.*;

/**
 * A jclouds connector 
 * @author Nicolas Ferry
 *
 */
public class JCloudsConnector implements Connector{

    private static final Logger journal = Logger.getLogger(JCloudsConnector.class.getName());

    private ComputeService compute;
    private String provider;
    private ComputeServiceContext computeContext;
    private EC2Api ec2api;
    private HashMap<String,String> runtimeInformation;

    public JCloudsConnector(String provider,String login,String secretKey){
        journal.log(Level.INFO, ">> Connecting to "+provider+" ...");
        Properties overrides = new Properties();
        if(provider.equals("aws-ec2")){
            // choose only amazon images that are ebs-backed
            //overrides.setProperty(AWSEC2Constants.PROPERTY_EC2_AMI_OWNERS,"107378836295");
            overrides.setProperty(AWSEC2Constants.PROPERTY_EC2_AMI_QUERY,
                    "owner-id=137112412989,107378836295,099720109477;state=available;image-type=machine;root-device-type=ebs");
        }
        overrides.setProperty(PROPERTY_CONNECTION_TIMEOUT, 0 + "");
        overrides.setProperty(PROPERTY_SO_TIMEOUT, 0 + "");
        overrides.setProperty(PROPERTY_REQUEST_TIMEOUT, 0 + "");
        overrides.setProperty(PROPERTY_RETRY_DELAY_START, 0 + "");
        Iterable<Module> modules = ImmutableSet.<Module> of(
                new SshjSshClientModule(),
                new NullLoggingModule());
        ContextBuilder builder = ContextBuilder.newBuilder(provider).credentials(login, secretKey).modules(modules).overrides(overrides);
        journal.log(Level.INFO, ">> Authenticating ...");
        computeContext=builder.buildView(ComputeServiceContext.class);
        ec2api=builder.buildApi(EC2Api.class);
        //loadBalancerCtx=builder.buildView(LoadBalancerServiceContext.class);
        compute=computeContext.getComputeService();
        this.provider = provider;
    }

    /**
     * Retrieve information about a VM
     * @param name name of a VM
     * @return data about a VM
     */
    public ComputeMetadata getVMByName(String name){
        for(ComputeMetadata n : compute.listNodes()){
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
        return compute.listNodes();
    }

    /**
     * Retrieve data about a VM
     * @param id id of a VM
     * @return Information about a VM
     */
    public NodeMetadata getVMById(String id){
        return compute.getNodeMetadata(id);
    }

    /**
     * Close the connection
     */
    public void closeConnection(){
        compute.getContext().close();
        journal.log(Level.INFO, ">> Closing connection ...");
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
        try {
            contentKey = FileUtils.readFileToString(new File(key));
            b.user(login);
            b.noPassword();
            b.privateKey(contentKey);
        } catch (IOException e) {
            journal.log(Level.SEVERE, e.getMessage());
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
        journal.log(Level.INFO, ">> Uploading "+sourcePath);
        org.jclouds.domain.LoginCredentials.Builder b=initCredentials(login, key);
        SshClient ssh = compute.getContext().utils().sshForNode().apply(NodeMetadataBuilder.fromNodeMetadata(getVMById(VMId)).credentials(b.build()).build());
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
     * Execute a command on a group of vms
     * @param group name of the group
     * @param command the command to be executed
     * @param login username
     * @param key sshkey
     * @throws RunScriptOnNodesException
     */
    public void execCommandInGroup(String group, String command, String login, String key) throws RunScriptOnNodesException{
        journal.log(Level.INFO, ">> executing command...");
        journal.log(Level.INFO, ">> "+ command);

        org.jclouds.domain.LoginCredentials.Builder b=initCredentials(login, key);
        Map<? extends NodeMetadata, ExecResponse> responses = compute.runScriptOnNodesMatching(
                runningInGroup(group),
                exec(command),
                overrideLoginCredentials(b.build())
                        .runAsRoot(false)
                        .wrapInInitScript(false));// run command directly

        for(Entry<? extends NodeMetadata, ExecResponse> r : responses.entrySet())
            journal.log(Level.INFO, ">> "+r.getValue());
    }

    /**
     * Execute a command on a specified vm
     * @param id id of the VM
     * @param command the command to be executed
     * @param login username
     * @param key sshkey for connection
     */
    public void execCommand(String id, String command, String login, String key){
        journal.log(Level.INFO, ">> executing command...");
        journal.log(Level.INFO, ">> "+ command);

        org.jclouds.domain.LoginCredentials.Builder b=initCredentials(login, key);
        ExecResponse response = compute.runScriptOnNode(
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
            a.setPublicAddress(getVMById(cm.getId()).getPublicAddresses().iterator().next());
            a.setId(cm.getId());
        }
    }

    /**
     * Provision a VM
     * @param a description of the VM to be created
     * @return
     */
    public HashMap<String,String> createInstance(VMInstance a){
        runtimeInformation=new HashMap<String, String>();
        VM vm = a.getType();
        ComponentInstance.State state = ComponentInstance.State.UNRECOGNIZED;
        ComputeMetadata cm= getVMByName(a.getName());
		/* UPDATE THE MODEL */
        if(cm != null){
            updateVMMetadata(a);
        }else{
            Template template=null;
            NodeMetadata nodeInstance = null;
            String groupName="cloudml-instance";
            if(!vm.getGroupName().equals(""))
                groupName= vm.getGroupName();

            TemplateBuilder templateBuilder = compute.templateBuilder();

            if(!vm.getImageId().equals("")){
                templateBuilder.imageId(vm.getImageId());
            }

            journal.log(Level.INFO, ">> Provisioning a vm ...");

            if(vm.getProviderSpecificTypeName().equals("")){
                if (vm.getMinRam() > 0)
                    templateBuilder.minRam(vm.getMinRam());
                if (vm.getMinCores() > 0)
                    templateBuilder.minCores(vm.getMinCores());
            }else{
                templateBuilder.hardwareId(vm.getProviderSpecificTypeName());
            }
            if (!vm.getLocation().equals(""))
                templateBuilder.locationId(vm.getLocation());
            if (!vm.getOs().equals(""))
                templateBuilder.imageDescriptionMatches(vm.getOs());
            else templateBuilder.osFamily(OsFamily.UBUNTU);
            templateBuilder.os64Bit(vm.getIs64os());

            template = templateBuilder.build();
            journal.log(Level.INFO, ">> vm type: "+template.getHardware().getId()+" on location: "+template.getLocation().getId());
            a.getProperties().add(new Property("ProviderInstanceType", template.getHardware().getId()));
            a.getProperties().add(new Property("location", template.getLocation().getId()));

            if(provider.equals("aws-ec2")){
                template.getOptions().as(EC2TemplateOptions.class).mapNewVolumeToDeviceName("/dev/sdm", vm.getMinStorage(), true);
                template.getOptions().as(EC2TemplateOptions.class).securityGroups(vm.getSecurityGroup());
                template.getOptions().as(EC2TemplateOptions.class).keyPair(vm.getSshKey());
                template.getOptions().as(EC2TemplateOptions.class).userMetadata("Name", a.getName());
                template.getOptions().as(EC2TemplateOptions.class).overrideLoginUser(a.getName());
            }

            template.getOptions().blockUntilRunning(true);

            try {
                Set<? extends NodeMetadata> nodes = compute.createNodesInGroup(groupName, 1, template);
                nodeInstance = nodes.iterator().next();

                journal.log(Level.INFO, ">> Running vm: "+nodeInstance.getName()+" Id: "+ nodeInstance.getId() +" with public address: "+nodeInstance.getPublicAddresses() +
                        " on OS:"+nodeInstance.getOperatingSystem()+ " " + nodeInstance.getCredentials().identity+":"+nodeInstance.getCredentials().getUser()+":"+nodeInstance.getCredentials().getPrivateKey());

            } catch (RunNodesException e) {
                journal.log(Level.SEVERE, e.getMessage());
                //a.setStatusAsError();
                state = ComponentInstance.State.ERROR;

            }
            runtimeInformation.put("publicAddress", nodeInstance.getPublicAddresses().iterator().next());
            //a.setPublicAddress(nodeInstance.getPublicAddresses().iterator().next());
            a.setId(nodeInstance.getId());
            a.setCore((int) nodeInstance.getHardware().getProcessors().iterator().next().getCores());
            state = ComponentInstance.State.RUNNING;

        }
        runtimeInformation.put("status", state.toString());
        return runtimeInformation;
    }

    /**
     * Terminate a specified VM
     * @param id id of the VM
     */
    public void destroyVM(String id){
        compute.destroyNode(id);
    }


    /**
     * Create a snapshot of the volume attached to the VM
     * @param vmi a VMInstance
     */
    public String createSnapshot(VMInstance vmi){
        NodeMetadata nm=getVMById(vmi.getId());
        ElasticBlockStoreApi ebsClient = ec2api.getElasticBlockStoreApi().get();
        journal.log(Level.INFO, ">> Creating snapshot of VM: "+vmi.getName());
        Snapshot snapshot = ebsClient.createSnapshotInRegion("eu-west-1", nm.getHardware().getVolumes().get(0).getId());
        journal.log(Level.INFO, ">> Snapshot created with ID: "+snapshot.getId());
        return snapshot.getId();
    }

    /**
     * Create an image of a VM
     * @param vmi the VMInstance to use
     * @return  id of the image
     */
    public String createImage(VMInstance vmi){
        AMIApi ami=ec2api.getAMIApi().get();
        journal.log(Level.INFO, ">> Creating an image of VM: "+vmi.getName());
        String id=ami.createImageInRegion("eu-west-1",vmi.getName()+"-image",vmi.getId().split("/")[1]);//TODO: check the region
        String status="";
        while (!status.toLowerCase().equals("available")){
            Image i=compute.getImage("eu-west-1/"+id);
            status=i.getStatus().name();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                journal.log(Level.SEVERE, e.getMessage());
            }
        }
        journal.log(Level.INFO, ">> Image created with ID: "+id);
        return "eu-west-1/"+id;
    }

    /**
     * Transform a snapshot into an image
     * @param vmi the VMInstance to use
     * @param snapshotID identifier of the snapshot
     * @return id of the image
     */
    public String snapshotToImage(VMInstance vmi, String snapshotID){
        AMIApi ami=ec2api.getAMIApi().get();
        journal.log(Level.INFO, ">> Creating an image from snapshot: "+snapshotID);
        String id=ami.registerUnixImageBackedByEbsInRegion("eu-west-1",vmi.getName()+"-image",snapshotID);
        journal.log(Level.INFO, ">> Image created with ID: "+id);
        return id;
    }

    /**
     * Find Hardware from a provider on the basis of the disk space
     * @param minDisk minimum disk space available
     * @return
     */
    public Hardware findHardwareByDisk(int minDisk) {
        Set<? extends Hardware> listHardware=compute.listHardwareProfiles();
        Hardware min=null;
        int minSum=2000; // baaah
        for (Hardware h : listHardware) {
            List<? extends Volume> listVolumes=h.getVolumes();
            int sum=0;
            for (Volume volume : listVolumes) {
                sum+=volume.getSize().intValue();
            }
            if(sum >= minDisk && minSum > sum){
                minSum=sum;
                min=h;
            }
        }
        return min;
    }

	/*public void createLoadBalancer(String location, String group, String protocol, int loadBalancerPort, int instancePort, List<NodeMetadata> nodes){
		LocationBuilder lBuilder= new LocationBuilder();
		Location l= lBuilder.id(location).build();
		LoadBalancerMetadata lb=loadBalancerCtx.getLoadBalancerService().createLoadBalancerInLocation(l, group, protocol, loadBalancerPort, instancePort, nodes);
	}*/
}
