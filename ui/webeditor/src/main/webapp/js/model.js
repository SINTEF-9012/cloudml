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
	var xpath="";
	if(id.length <= 1){
		xpath=id+propertyId;
	}else{
		xpath=id+"/"+propertyId;
	}
	setJSON(deploymentModel,xpath, value);

	if(propertyId.indexOf('name') >= 0){
		loadDeploymentModel(JSON.stringify(deploymentModel));
	}
}

function instanciateNode(){

}

function instanciateArtefact(){

}

function instanciateBinding(){

}

function removeNode(){

}

function removeArtefact(){

}

function removeBinding(){

}

function setDestination(artefactId, nodeId){
	var artefact = findArtefactInstanceByName(artefactId);
	artefact.destination="nodeInstances["+nodeId+"]";
}


/*********************************************
* Looking for something in the model ?
* Should not be used anymore => jspointer
**********************************************/


function findPortType(artefactTypeName,portTypeName){
	if(deploymentModel.artefactTypes != null){
		for(var a=0;a<deploymentModel.artefactTypes.length;a++){
			//very crappy but I am lazy
			var aname="artefactTypes["+deploymentModel.artefactTypes[a].name+"]";
			if(aname == artefactTypeName){
				for(var i=0; i<deploymentModel.artefactTypes[a].required.length;i++){
					//very crappy but I am lazy
					if(aname+"/required["+deploymentModel.artefactTypes[a].required[i].name+"]" == portTypeName){
						return deploymentModel.artefactTypes[a].required[i];
					}
				}
			}
		}
	}
	alertMessage("error","Unknown port type!",5000);
	return null;
}

function findArtefactInstanceByName(name){
	for(var a=0;a<deploymentModel.artefactInstances.length;a++){
		if(name.indexOf(deploymentModel.artefactInstances[a].name) >= 0){
			return deploymentModel.artefactInstances[a];
		}
	}
	return null;
}

function findArtefactTypeByName(name){
	if(deploymentModel.artefactTypes != null){
		for(var a=0;a<deploymentModel.artefactTypes.length;a++){
			if(name.indexOf(deploymentModel.artefactTypes[a].name) >= 0){
				return deploymentModel.artefactTypes[a];
			}
		}
	}
	alertMessage("error","Unknown artefact type!",5000);
	return null;
}

function findNodeTypeByName(name){
	if(deploymentModel.nodeTypes != null){
		for(var a=0;a<deploymentModel.nodeTypes.length;a++){
			if(name.indexOf(deploymentModel.nodeTypes[a].name) >= 0){
				return deploymentModel.nodeTypes[a];
			}
		}
	}
	alertMessage("error","Unknown node type!",5000);
	return null;
}

function findPortInstanceByName(artefactInstance, name){
	for(var a=0; a < artefactInstance.required.length; a++){
		if(name.indexOf(artefactInstance.required[a].name) >= 0){
			return artefactInstance.required[a];
		}
	}
	for(var a=0; a < artefactInstance.provided.length; a++){
		if(name.indexOf(artefactInstance.provided[a].name) >= 0){
			return artefactInstance.provided[a];
		}
	}
	alertMessage("error","Unknown port type!",5000);
	return null;
}