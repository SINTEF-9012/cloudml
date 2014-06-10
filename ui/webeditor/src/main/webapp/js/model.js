/*
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
var deploymentModel;

function updateProperty(id,propertyId,value){
	if(id.indexOf("/") >= 0){
		var xpath="";
		if(id.length <= 1){
			xpath=id+propertyId;
		}else{
			xpath=id+"/"+propertyId;
		}
		setJSON(deploymentModel,xpath, value);
	}
}

function addInModel(object){
	if(object.type.indexOf("nodeTypes") >=0){
		deploymentModel.nodeInstances.push(object);
	}
	if(object.type.indexOf("artefactTypes") >=0){
		deploymentModel.artefactInstances.push(object);
	}
	if(object.type.indexOf("bindingTypes") >=0){
		deploymentModel.bindingInstances.push(object);
	}
}

function removeInModel(path){
	var array=path.split("'"); //To be updated
	if(path.indexOf("nodeInstances") >=0){
		removeNode(array[1]);
	}
	if(path.indexOf("artefactInstances") >=0){
		removeArtefact(array[1]);
	}
	if(path.indexOf("bindingInstances") >=0){
		removeBinding(array[1]);
	}
}

/*******************************************
* Methods to instantiate
********************************************/

function instanciateVM(id, typeOfNode, ip){
	var node=new Object();
	node.eClass="net.cloudml.core:NodeInstance";
	node.name=id;
	node.type=typeOfNode;
	if(ip != ""){
		node.publicAddress=ip;
	}
	deploymentModel.nodeInstances.push(node);
}

function instanciateInternalComponent(id, typeOfArtefact, dest, required,provided){
	var artefact=new Object();
	artefact.eClass="net.cloudml.core:ArtefactInstance";
	artefact.name=id;
	artefact.type=typeOfArtefact;
	artefact.destination=dest;
	artefact.required=req;
	artefact.provided=prov;
	deploymentModel.artefactInstances.push(artefact);
}

function instanciateExternalComponent(){

}

/*******************************************
* Methods to remove instances
********************************************/

function removeVMInstance(name){
	for(var a=0; a<deploymentModel.vms.length;a++){
		var instance=deploymentModel.vms[a];
		if(instance.name.indexOf(name) >= 0){
			deploymentModel.vms.splice(a,1);
			return instance;
		}
	}
	return null;
}

function removeInternalComponentInstance(name){
	for(var a=0; a<deploymentModel.internalComponentInstances.length;a++){
		var instance=deploymentModel.internalComponentInstances[a];
		if(instance.name.indexOf("name") >= 0){
			deploymentModel.internalComponentInstances.splice(a,1);
			return instance;
		}
	}
	return null;
}

function removeRelationshipInstance(name){
	for(var a=0; a<deploymentModel.relationshipInstances.length;a++){
		var instance=deploymentModel.relationshipInstances[a];
		if(instance.name.indexOf("name") >= 0){
			deploymentModel.relationshipInstances.splice(a,1);
			return instance;
		}
	}
	return null;
}
