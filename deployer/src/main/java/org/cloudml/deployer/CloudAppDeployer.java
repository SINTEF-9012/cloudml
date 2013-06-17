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

import org.cloudml.core.*;
import org.jclouds.compute.RunScriptOnNodesException;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.NodeMetadata;

/*
 * The deployment Engine
 * author: Nicolas Ferry
 */

public class CloudAppDeployer {

	ArrayList<ArtefactInstance> alreadyDeployed=new ArrayList<ArtefactInstance>();
	ArrayList<ArtefactInstance> alreadyStarted=new ArrayList<ArtefactInstance>();

	/**
	 * The deployment process
	 * @param dm a deployment model
	 */
	public void deploy(DeploymentModel dm){
		JCloudsConnector jc;
		alreadyDeployed=new ArrayList<ArtefactInstance>();
		alreadyStarted=new ArrayList<ArtefactInstance>();

		// Provisioning nodes
		provisioning(dm);

		// Deploying on nodes
		// need to be recursive
		for(ArtefactInstance x : dm.getArtefactInstances()){
			if(!alreadyDeployed.contains(x) && (x.getDestination() != null)){
				NodeInstance ownerNode = x.getDestination();
				Node n=ownerNode.getType();
				jc=new JCloudsConnector(n.getProvider().getName(), n.getProvider().getLogin(), n.getProvider().getPasswd());
				jc.execCommand(ownerNode.getId(), x.getType().getResource().getRetrievingResourceCommand(),"ubuntu",n.getPrivateKey());
				alreadyDeployed.add(x);

				buildPaas(x,dm);

				jc.execCommand(ownerNode.getId(), x.getType().getResource().getDeployingResourceCommand(),"ubuntu",n.getPrivateKey());
				jc.closeConnection();
			}
		}

		//Configure the artefacts with the bindings
		configureWithBindings(dm);

		//configuration process at SaaS level
		configureSaas(dm);
	}

	/**
	 * Build the paas of an artefact instance
	 * @param x An artefactInstance
	 */
	private void buildPaas(ArtefactInstance x, DeploymentModel dm){
		NodeInstance ownerNode = x.getDestination();
		Node n=ownerNode.getType();

		JCloudsConnector jc;
		jc=new JCloudsConnector(n.getProvider().getName(), n.getProvider().getLogin(), n.getProvider().getPasswd());


		for(BindingInstance bi : dm.getBindingInstances()){
			if(!bi.getClient().getType().getIsRemote() && x.getRequired().contains(bi.getClient())){
				ServerPortInstance p=bi.getServer();
				if(!alreadyDeployed.contains(p.getOwner())){
					jc.execCommand(ownerNode.getId(), p.getOwner().getType().getResource().getRetrievingResourceCommand() ,"ubuntu",n.getPrivateKey());
					jc.execCommand(ownerNode.getId(), p.getOwner().getType().getResource().getDeployingResourceCommand(),"ubuntu",n.getPrivateKey());

					String configurationCommand=p.getOwner().getType().getResource().getConfigurationResourceCommand();
					String startCommand=p.getOwner().getType().getResource().getStartResourceCommand();
					configureAndStart(jc, n, ownerNode, configurationCommand, startCommand);
					alreadyDeployed.add(p.getOwner());
					alreadyStarted.add(p.getOwner());
				}
			}
		}

	}


	/**
	 * Configure and start SaaS artefacts
	 * @param dm a deployment model
	 */
	private void configureSaas(DeploymentModel dm){
		JCloudsConnector jc;
		for(ArtefactInstance x : dm.getArtefactInstances()){
			if(!alreadyStarted.contains(x)){
				NodeInstance ownerNode = x.getDestination();
				Node n=ownerNode.getType();
				jc=new JCloudsConnector(n.getProvider().getName(), n.getProvider().getLogin(), n.getProvider().getPasswd());

				String configurationCommand=x.getType().getResource().getConfigurationResourceCommand();
				String startCommand= x.getType().getResource().getStartResourceCommand();
				configureAndStart(jc, n, ownerNode, configurationCommand, startCommand);
				alreadyStarted.add(x);
			}
		}
	}


	/**
	 * Configure and start an artefact
	 * @param jc a connector
	 * @param n A node type
	 * @param ni a node instance
	 * @param configurationCommand the command to configure the artefact, parameters are: IP IPDest portDest
	 * @param startCommand the command to start the artefact
	 */
	private void configureAndStart(JCloudsConnector jc, Node n, NodeInstance ni, String configurationCommand, String startCommand){
		if(!configurationCommand.equals(""))
			jc.execCommand(ni.getId(), configurationCommand,"ubuntu",n.getPrivateKey());
		if(!startCommand.equals(""))
			jc.execCommand(ni.getId(), startCommand,"ubuntu",n.getPrivateKey());
	}

	/**
	 * Provision a node and upload the model with informations about the node
	 * @param dm 
	 * 			A deployment model
	 */
	private void provisioning(DeploymentModel dm){
		JCloudsConnector jc;
		for(NodeInstance n : dm.getNodeInstances()){
			Provider p=n.getType().getProvider();
			jc=new JCloudsConnector(p.getName(), p.getLogin(), p.getPasswd());
			ComputeMetadata cm= jc.getNodeByName(n.getName());
			/* UPDATE THE MODEL */
			if(cm == null){
				NodeMetadata testNode=jc.createInstance(n);
			}else{
				n.setPublicAddress(jc.getNodeById(cm.getId()).getPublicAddresses().iterator().next());
				n.setId(cm.getId());
			}
			jc.closeConnection();
		}
	}

	/**
	 * Configure Artefacts according to the bindings
	 * @param dm a deployment model
	 */
	private void configureWithBindings(DeploymentModel dm){
		//Configure on the basis of the bindings
		//parameters transmitted to the configuration scripts are "ip ipDestination portDestination"
		for(BindingInstance bi : dm.getBindingInstances()){
			if(bi.getClient().getType().getIsRemote()){
				ClientPortInstance client=bi.getClient();
				ServerPortInstance server=bi.getServer();

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
	 * @param r resource for configuration
	 * @param i port of the component to be configured
	 * @param destinationIpAddress IP of the server
	 * @param ipAddress IP of the client
	 * @param destinationPortNumber port of the server
	 */
	private void configureWithIP(Resource r, ArtefactPortInstance i, String destinationIpAddress, String ipAddress, int destinationPortNumber){
		JCloudsConnector jc;
		if(r != null){
			NodeInstance ownerNode = i.getOwner().getDestination();
			Node n=ownerNode.getType();
			jc=new JCloudsConnector(n.getProvider().getName(), n.getProvider().getLogin(), n.getProvider().getPasswd());
			jc.execCommand(ownerNode.getId(), r.getRetrievingResourceCommand(),"ubuntu",n.getPrivateKey());
			String configurationCommand=r.getConfigurationResourceCommand()+" \""+ipAddress+"\" \""+destinationIpAddress+"\" "+destinationPortNumber;
			configureAndStart(jc, n, ownerNode, configurationCommand, "");
			jc.closeConnection();
		}
	}

}
