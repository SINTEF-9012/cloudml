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

import static org.jclouds.compute.predicates.NodePredicates.runningInGroup;
import static org.jclouds.compute.predicates.NodePredicates.withIds;
import static org.jclouds.compute.options.RunScriptOptions.Builder.overrideLoginCredentials;
import static org.jclouds.scriptbuilder.domain.Statements.exec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.cloudml.core.*;
import org.jclouds.ContextBuilder;
import org.jclouds.aws.ec2.reference.AWSEC2Constants;
import org.jclouds.byon.suppliers.NodesParsedFromSupplier;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.RunScriptOnNodesException;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.ExecResponse;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.NodeMetadataBuilder;
import org.jclouds.compute.domain.OsFamily;
import org.jclouds.compute.domain.Template;
import org.jclouds.compute.domain.TemplateBuilder;
import org.jclouds.compute.domain.Volume;
import org.jclouds.domain.Location;
import org.jclouds.domain.LocationBuilder;
import org.jclouds.domain.LoginCredentials;
import org.jclouds.ec2.compute.options.EC2TemplateOptions;
import org.jclouds.gogrid.domain.LoadBalancer;
import org.jclouds.gogrid.domain.LoadBalancer.Builder;
import org.jclouds.io.Payloads;
import org.jclouds.loadbalancer.LoadBalancerServiceContext;
import org.jclouds.loadbalancer.domain.LoadBalancerMetadata;
import org.jclouds.logging.config.NullLoggingModule;
import org.jclouds.ssh.SshClient;
import org.jclouds.sshj.config.SshjSshClientModule;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;
import static org.jclouds.Constants.*;
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
	private LoadBalancerServiceContext loadBalancerCtx;

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
		//loadBalancerCtx=builder.buildView(LoadBalancerServiceContext.class);
		compute=computeContext.getComputeService();
		this.provider = provider; 
	}

	/**
	 * Retrieve information about a node
	 * @param name name of a node
	 * @return data about a node
	 */
	public ComputeMetadata getNodeByName(String name){
		for(ComputeMetadata n : compute.listNodes()){
			if(n.getName() != null &&  n.getName().equals(name))
				return n;
		}
		return null;
	}

	/**
	 * retrieve the list of nodes
	 * @return a list of information about each node
	 */
	public Set<? extends ComputeMetadata> listOfNodes(){
		return compute.listNodes();
	}

	/**
	 * Retrieve data about a node
	 * @param id id of a node
	 * @return Information about a node
	 */
	public NodeMetadata getNodeById(String id){
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
	 * Upload a file on a selected node
	 * @param sourcePath path to the file to be uploaded
	 * @param destinationPath path to the file to be created
	 * @param nodeId Id of a node
	 * @param login user login
	 * @param key key to connect
	 */
	public void uploadFile(String sourcePath, String destinationPath, String nodeId, String login, String key){
		SshClient ssh = compute.getContext().getUtils().sshForNode().apply(NodeMetadataBuilder.fromNodeMetadata(getNodeById(nodeId)).credentials(new LoginCredentials(login, null, key, true)).build());
		try {
			ssh.connect();
			ssh.put(destinationPath, Payloads.newPayload(new File(sourcePath)));
		} finally {
			if (ssh != null)
				ssh.disconnect();
		}
	}


	/**
	 * Execute a command on a group of nodes
	 * @param group name of the group
	 * @param command the command to be executed
	 * @param login username
	 * @param key sshkey
	 * @throws RunScriptOnNodesException
	 */
	public void execCommandInGroup(String group, String command, String login, String key) throws RunScriptOnNodesException{
		journal.log(Level.INFO, ">> executing command...");
		journal.log(Level.INFO, ">> "+ command);

		Map<? extends NodeMetadata, ExecResponse> responses = compute.runScriptOnNodesMatching(
				runningInGroup(group), 
				exec(command),
				overrideLoginCredentials(new LoginCredentials(login, null, key, false)) 
				.runAsRoot(false) 
				.wrapInInitScript(false));// run command directly

		for(Entry<? extends NodeMetadata, ExecResponse> r : responses.entrySet())
			journal.log(Level.INFO, ">> "+r.getValue());
	}

	/**
	 * Execute a command on a specified node
	 * @param id id of the node
	 * @param command the command to be executed
	 * @param login username
	 * @param keyPath sshkey for connection
	 */
	public void execCommand(String id, String command, String login, String key){
		journal.log(Level.INFO, ">> executing command...");
		journal.log(Level.INFO, ">> "+ command);

		ExecResponse response = compute.runScriptOnNode(
				id, 
				exec(command),
				overrideLoginCredentials(new LoginCredentials(login, null, key, false)) 
				.runAsRoot(false) 
				.wrapInInitScript(false));// run command directly

		journal.log(Level.INFO, ">> "+response.getOutput());

	}

	/**
	 * Provision a node
	 * @param a description of the node to be created
	 * @return
	 */
	public void createInstance(NodeInstance a){
		Template template=null;
		NodeMetadata nodeInstance = null;
		Node node= a.getType();
		String groupName="cloudml-instance";
		if(!node.getGroupName().equals(""))
			groupName=node.getGroupName();

		TemplateBuilder templateBuilder = compute.templateBuilder();

		if(!node.getImageId().equals("")){
			templateBuilder.imageId(node.getImageId());
		}

		journal.log(Level.INFO, ">> Provisioning a node ...");

		if (node.getMinRam() > 0)
			templateBuilder.minRam(node.getMinRam());
		if (node.getMinCore() > 0)
			templateBuilder.minCores(node.getMinCore());
		if (!node.getLocation().equals(""))
			templateBuilder.locationId(node.getLocation());
		if (!node.getOS().equals(""))
			templateBuilder.imageDescriptionMatches(node.getOS());
		else templateBuilder.osFamily(OsFamily.UBUNTU);
		templateBuilder.os64Bit(node.getIs64os());

		/*if(node.getMinDisk() > 0 && provider.equals("aws-ec2")){
    		Hardware hw=findHardwareByDisk(node.getMinDisk());
    		templateBuilder.hardwareId(hw.getId());
    	}*/

		template = templateBuilder.build();
		journal.log(Level.INFO, ">> node type: "+template.getHardware().getId()+" on location: "+template.getLocation().getId());
		a.getProperties().add(new Property("ProviderInstanceType", template.getHardware().getId()));
		a.getProperties().add(new Property("location", template.getLocation().getId()));

		if(provider.equals("aws-ec2")){
			template.getOptions().as(EC2TemplateOptions.class).mapNewVolumeToDeviceName("/dev/sdm", node.getMinDisk(), true);
			template.getOptions().as(EC2TemplateOptions.class).securityGroups(node.getSecurityGroup());
			template.getOptions().as(EC2TemplateOptions.class).keyPair(node.getSshKey());
			template.getOptions().as(EC2TemplateOptions.class).userMetadata("Name", a.getName());
			template.getOptions().as(EC2TemplateOptions.class).overrideLoginUser(a.getName());
		}

		template.getOptions().blockUntilRunning(true);

		try {
			Set<? extends NodeMetadata> nodes = compute.createNodesInGroup(groupName, 1, template);
			nodeInstance = nodes.iterator().next();

			journal.log(Level.INFO, ">> Running node: "+nodeInstance.getName()+" Id: "+ nodeInstance.getId() +" with public address: "+nodeInstance.getPublicAddresses() + 
					" on OS:"+nodeInstance.getOperatingSystem()+ " " + nodeInstance.getCredentials().identity+":"+nodeInstance.getCredentials().getUser()+":"+nodeInstance.getCredentials().getPrivateKey());

		} catch (RunNodesException e) {
			e.printStackTrace();
		}	

		a.setPublicAddress(nodeInstance.getPublicAddresses().iterator().next());
		a.setId(nodeInstance.getId());

	}

	/**
	 * Terminate a specified node
	 * @param id id of the node
	 */
	public void destroyNode(String id){
		compute.destroyNode(id);
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
