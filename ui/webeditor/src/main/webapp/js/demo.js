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
	//jsplumb instance
	var instance;
	var deploymentModel;
	
	jsPlumb.ready(function() {

		instance = jsPlumb.getInstance({
			// default drag options
			DragOptions : { cursor: 'pointer', zIndex:2000 },
			// the overlays to decorate each connection with.  note that the label overlay uses a function to generate the label text; in this
			// case it returns the 'labelText' member that we set on each connection in the 'init' method below.
			ConnectionOverlays : [
				[ "Arrow", { location:1 , id:"myarrow"} ],
				[ "Label", { 
					location:0.5,
					id:"label",
					cssClass:"aLabel"
				}]
			],
			Container:"flowchart-demo"
		});		

		// this is the paint style for destination binding
		connectorDestPaintStyle = {
			lineWidth:4,
			strokeStyle:"#61B7CF",
			joinstyle:"round",
			outlineColor:"white",
			outlineWidth:2
		},
		// this is the connecting style for mandatory communication
		connectorPaintStyle = {
			lineWidth:4,
			strokeStyle:"#aa1111",
			joinstyle:"round",
			outlineColor:"white",
			outlineWidth:2
		},
		// this is the connecting style for optional communication
		connectorOptionalPaintStyle = {
			lineWidth:4,
			strokeStyle:"#aa1111",
			joinstyle:"round",
			outlineColor:"white",
			dashstyle:"4 2",
			outlineWidth:2
		},
		// this is the connecting style for local communication
		connectorLocalPaintStyle = {
			lineWidth:4,
			strokeStyle:"#aa1111",
			outlineColor:"white",
			dashstyle:"4 2",
			outlineWidth:2,
		},
		// .. and this is the hover style. 
		connectorHoverStyle = {
			lineWidth:4,
			strokeStyle:"#216477",
			outlineWidth:2,
			outlineColor:"white"
		},
		endpointHoverStyle = {
			fillStyle:"#216477",
			strokeStyle:"#216477"
		},
		// the definition of source endpoints (the small blue ones)
		sourceOptionalEndpoint = {
			endpoint:"Dot",
			paintStyle:{ 
				strokeStyle:"#7AB02C",
				fillStyle:"transparent",
				radius:7,
				lineWidth:3 
			},				
			isSource:true,
			connector:[ "Flowchart", { stub:[40, 10], gap:5, cornerRadius:5, alwaysRespectStubs:true } ],								                
			connectorStyle:connectorOptionalPaintStyle,
			hoverPaintStyle:endpointHoverStyle,
			connectorHoverStyle:connectorHoverStyle,
            dragOptions:{},
            overlays:[
            	[ "Label", { 
                	location:[0.5, 1.5], 
                	label:"Drag",
                	cssClass:"endpointSourceLabel" 
                }],
            ]
		},
		// the definition of source endpoints (the small blue ones)
		sourceDestEndpoint = {
			endpoint:"Dot",
			paintStyle:{ 
				strokeStyle:"#7AB02C",
				fillStyle:"transparent",
				radius:7,
				lineWidth:3 
			},				
			isSource:true,
			connector:[ "Flowchart", { stub:[40, 10], gap:5, cornerRadius:5, alwaysRespectStubs:true } ],								                
			connectorStyle:connectorDestPaintStyle,
			hoverPaintStyle:endpointHoverStyle,
			connectorHoverStyle:connectorHoverStyle,
            dragOptions:{},
            overlays:[
            	[ "Label", { 
                	location:[0.5, 1.5], 
                	label:"Drag",
                	cssClass:"endpointSourceLabel" 
                } ]
            ]
		},
		// the definition of source endpoints (the small red ones)
		sourceEndpoint = {
			endpoint:"Dot",
			paintStyle:{ 
				strokeStyle:"#7AB02C",
				fillStyle:"transparent",
				radius:7,
				lineWidth:3 
			},				
			isSource:true,
			connector:[ "Flowchart", { stub:[40, 60], gap:5, cornerRadius:5, alwaysRespectStubs:true } ],								                
			connectorStyle:connectorPaintStyle,
			hoverPaintStyle:endpointHoverStyle,
			connectorHoverStyle:connectorHoverStyle,
            dragOptions:{},
            overlays:[
            	[ "Label", { 
                	location:[0.5, 1.5], 
                	label:"Drag",
                	cssClass:"endpointSourceLabel" 
                } ]
            ]
		},	
		// the definition of target endpoints (will appear when the user drags a connection) 
		targetEndpoint = {
			endpoint:"Dot",					
			paintStyle:{ fillStyle:"#7AB02C",radius:11 },
			hoverPaintStyle:endpointHoverStyle,
			maxConnections:-1,
			dropOptions:{ hoverClass:"hover", activeClass:"active" },
			isTarget:true,			
            overlays:[
            	[ "Label", { location:[0.5, -0.5], label:"Drop", cssClass:"endpointTargetLabel" } ]
            ]
		},		
		init = function(connection) {			
			connection.getOverlay("label").setLabel(connection.sourceId.substring(15) + "-" + connection.targetId.substring(15));
			connection.bind("editCompleted", function(o) {
				if (typeof console != "undefined")
					console.log("connection edited. path is now ", o.path);
			});
		};			

		var _addEndpoints = function(toId, sourceAnchors, targetAnchors) {
				for (var i = 0; i < sourceAnchors.length; i++) {
					var sourceUUID = toId + sourceAnchors[i];
					instance.addEndpoint(toId, sourceEndpoint, { anchor:sourceAnchors[i], uuid:sourceUUID });						
				}
				for (var j = 0; j < targetAnchors.length; j++) {
					var targetUUID = toId + targetAnchors[j];
					instance.addEndpoint(toId, targetEndpoint, { anchor:targetAnchors[j], uuid:targetUUID });						
				}
			};

		instance.doWhileSuspended(function() {
			addVMInstances();
			addInternalComponentInstances();
			addExternalComponentInstances();
			instance.draggable(jsPlumb.getSelector(".flowchart-demo .window"), { grid: [20, 20] });
			addRelationshipInstances();
			addExecuteInstances();
		});
		
		
		instance.bind("click", function(conn, originalEvent) {
			if (confirm("Delete connection from " + conn.sourceId + " to " + conn.targetId + "?"))
				jsPlumb.detach(conn); 
		});	
	});
	
	

/***********************************************
* Functions to add CloudML elements in the graph
************************************************/

function addVMInstances(){
	if(deploymentModel.vmInstances != null){
		for(a=0;a<deploymentModel.vmInstances.length;a++){
			var typeInfo = getJSON(deploymentModel,"/"+deploymentModel.vmInstances[a].type);
			var nodeInfo="name:"+deploymentModel.vmInstances[a].name+"&lt;br&gt; type:"+deploymentModel.vmInstances[a].type+"&lt;br&gt; OS:"+typeInfo.os+"&lt;br&gt; OS:"+typeInfo.provider;
			
			$( "#flowchart-demo").append( "<div class='window _vm' data-content='"
											+nodeInfo+"' data-original-title='VM Info:' id='flowchartWindow_"+deploymentModel.vmInstances[a].name+"'><strong>"
											+ deploymentModel.vmInstances[a].name +"</strong><br/><br></div>" );
			placeRandomly("flowchartWindow_"+deploymentModel.vmInstances[a].name);

			$('#flowchartWindow_'+deploymentModel.vmInstances[a].name).popover({html:true});
			addExecutionPort(deploymentModel.vmInstances[a]);
			addPortInstances(deploymentModel.vmInstances[a]);
		}
	}
}

function addInternalComponentInstances(){
	if(deploymentModel.internalComponentInstances != null){
		for(a=0;a<deploymentModel.internalComponentInstances.length;a++){
			var nodeInfo="name:"+deploymentModel.internalComponentInstances[a].name+"&lt;br&gt; Type:"+deploymentModel.internalComponentInstances[a].type;
		
			$( "#flowchart-demo").append( "<div class='window _art' data-content='"
											+nodeInfo+"' data-original-title='Component Info:' id='flowchartWindow_"+deploymentModel.internalComponentInstances[a].name+"'><strong>"+ deploymentModel.internalComponentInstances[a].name +"</strong><br/><br></div>" );
			placeRandomly("flowchartWindow_"+deploymentModel.internalComponentInstances[a].name);
			$('#flowchartWindow_'+deploymentModel.internalComponentInstances[a].name).popover({html:true});
			addExecutionPort(deploymentModel.internalComponentInstances[a]);
			addPortInstances(deploymentModel.internalComponentInstances[a]);
		}
	}
}

function addExternalComponentInstances(){
	if(deploymentModel.externalComponentInstances != null){
		for(a=0;a<deploymentModel.externalComponentInstances.length;a++){
			var nodeInfo="name:"+deploymentModel.externalComponentInstances[a].name+"&lt;br&gt; Type:"+deploymentModel.externalComponentInstances[a].type;
			$( "#flowchart-demo").append( "<div class='window _vm' data-content='"
											+nodeInfo+"' data-original-title='Component Info:' id='flowchartWindow_"+deploymentModel.externalComponentInstances[a].name+"'><strong>"+ deploymentModel.externalComponentInstances[a].name +"</strong><br/><br></div>" );
			placeRandomly("flowchartWindow_"+deploymentModel.externalComponentInstances[a].name);
			$('#flowchartWindow_'+deploymentModel.externalComponentInstances[a].name).popover({html:true});
			addExecutionPort(deploymentModel.externalComponentInstances[a]);
			addPortInstances(deploymentModel.externalComponentInstances[a]);
		}
	}
}

function placeRandomly(id){
	var x=Math.floor((Math.random()*50)+1);
	var y=Math.floor((Math.random()*50)+1);
	$("#"+id).attr("style","top:"+y+"em;left:"+x+"em;");
}

function addPortInstances(obj){
	if(obj == null)
		throw("obj cannot be null");
	var tab = obj['requiredPortInstances'];
	if(tab !== null && (typeof tab !== "undefined")){
		var length = tab.length;
		for(j = 0 ; j < length ; j++){
			sourceEndpoint.overlays[0][1].label = tab[j].name;
			var port=getJSON(deploymentModel, "/"+tab[j].type);
			var stringType = (obj['eClass'].split(":")[1]+"s").uncapitalizes(2);
			if(port.isMandatory){
				instance.addEndpoint("flowchartWindow_"+obj.name, sourceEndpoint, { anchor:"Continuous", uuid:stringType+"["+obj.name+"]/requiredPortInstances["+tab[j].name +"]"});
			}else{
				instance.addEndpoint("flowchartWindow_"+obj.name, sourceOptionalEndpoint, { anchor:"Continuous", uuid:stringType+"["+obj.name+"]/requiredPortInstances["+tab[j].name +"]"});
			}
		}
	}
	var tab = obj['providedPortInstances'];
	if(tab !== null && (typeof tab !== "undefined")){
		var length = tab.length;
		for(j = 0 ; j < length ; j++){
			targetEndpoint.overlays[0][1].label = tab[j].name;
			var stringType = (obj['eClass'].split(":")[1]+"s").uncapitalizes(2); // the 's' trick is very crappy ...
			instance.addEndpoint("flowchartWindow_"+obj.name, targetEndpoint, { anchor:"Continuous", uuid:stringType+"["+obj.name+"]/providedPortInstances["+tab[j].name +"]"});
		}
	}
}


function addExecutionPort(obj){
	if(obj == null){
		throw("obj cannot be null");
	}
	var tab = obj['providedExecutionPlatformInstances'];
	if(tab !== null && (typeof tab !== "undefined")){
		var length = tab.length;
		for(j = 0 ; j < length ; j++){
			sourceDestEndpoint.overlays[0][1].label = tab[j].name;
			var stringType = (obj['eClass'].split(":")[1]+"s").uncapitalizes(2);
			console.log(stringType+" plop"+tab[j].name);
			instance.addEndpoint("flowchartWindow_"+obj.name, sourceDestEndpoint, { anchor:"Continuous", uuid:stringType+"["+obj.name+"]/providedExecutionPlatformInstances["+tab[j].name +"]"});
		}
	}
	var tab = obj['requiredExecutionPlatformInstance'];
	if(tab !== null && (typeof tab !== "undefined")){
		targetEndpoint.overlays[0][1].label = tab.name;
		var stringType = (obj['eClass'].split(":")[1]+"s").uncapitalizes(2);
		console.log(stringType+" plop");
		instance.addEndpoint("flowchartWindow_"+obj.name, targetEndpoint, { anchor:"Continuous", uuid:stringType+"["+obj.name+"]/requiredExecutionPlatformInstance["+tab.name +"]"});
	}
}


function addRelationshipInstances(){
	for(a=0; a< deploymentModel.relationshipInstances.length;a++){
		var connection=instance.connect({uuids:[deploymentModel.relationshipInstances[a].providedPortInstance, deploymentModel.relationshipInstances[a].requiredPortInstance], editable:true});
		if(!connection){
			alertMessage("error","Relationship instance:"+deploymentModel.relationshipInstances[a].name+" not well defined!",5000);
		}
		connection.getOverlay("label").setLabel(deploymentModel.relationshipInstances[a].name);
		
		var relationshipType = getJSON(deploymentModel, "/"+deploymentModel.relationshipInstances[a].type);
		var portType = getJSON(deploymentModel, "/"+relationshipType.requiredPort);

		if((portType != null) && portType.isLocal){
			connection.removeOverlay("myarrow");
			connection.addOverlay([ "PlainArrow", { location:1 , id:"myarrow"} ]);
		}
	}
}

function addExecuteInstances(){
	var tab = deploymentModel.executesInstances;
	var length = tab.length;
	for(k=0; k < length;k++){
		var connection=instance.connect({uuids:[tab[k].providedExecutionPlatformInstance, tab[k].requiredExecutionPlatformInstance], editable:true});
	}
}

function setModelName(name){
	$('#title').text(name);
}


/***************************************
/***************************************
* General management of deployment model
****************************************/

function saveFile(inputDiv){
	if(deploymentModel)
		window.open("data:application/octet-stream,"+JSON.stringify(deploymentModel));
}

function loadFile(inputDiv) {
    var input, file, fr;
	var progress = $('#'+inputDiv).find('#progressBar');
	progress.css("width","0%");
	progress.parent().show();
    if (typeof window.FileReader !== 'function') {
        showAlert("p", "The file API isn't supported on this browser yet.");
        return;
    }

	//Error Catching
    input = $('#'+inputDiv).find('input').get(0);
	
    if (!input) {
        console.log("error","Hum, couldn't find the fileinput element.",5000);
    }
    else if (!input.files) {
        console.log("error","This browser doesn't seem to support the `files` property of file inputs",5000);
    }
    else if (!input.files[0]) {
        console.log("error","Please select a file before clicking 'Load'",5000);
    }
	//its ok
    else {
        file = input.files[0];
        fr = new FileReader();
		fr.onprogress = updateProgress;
        fr.onload = receivedText;
        fr.readAsText(file);
    }

    function receivedText() {
	    progress.css("width",'100%');
		progress.parent().hide();
		loadDeploymentModel(fr.result);
    }
}


function reset(){
	instance.reset();
	instance.repaintEverything();
	$( "#flowchart-demo").empty();
	$( "#bindingTable>tbody" ).empty();
	$( "#nodeTable>tbody" ).empty();
	$( "#artefactTable>tbody" ).empty();
	$( "#providerTable>tbody" ).empty();
 }
  
//Load the deployment model
function loadDeploymentModel(jsonString) {
	deploymentModel = eval('(' + jsonString + ')');
	
	reset();
	setModelName(deploymentModel.name);
	addInternalComponentTypes();
	addVMTypes();
	addExternalComponentTypes();
	//addRelationshipType();
	//addProviders();
	instance.doWhileSuspended(function() {
		addVMInstances();
		addInternalComponentInstances();
		addExternalComponentInstances();
		instance.draggable(jsPlumb.getSelector(".flowchart-demo .window"), { grid: [20, 20] });
		addRelationshipInstances();
		addExecuteInstances();
	});

}

/********************************************
* Build the list of buttons to manage types
*********************************************/

function addInternalComponentTypes(){
	if(deploymentModel.internalComponents != null){
		for(var a=0;a<deploymentModel.internalComponents.length;a++){
			//$( "#artefactTable>tbody" ).append("<tr><td>"+deploymentModel.internalComponents[a].name+" </td><td><button type='button' class='btn btn-xs btn-primary'>Instantiate</button>&nbsp;<button type='button' data-toggle='modal' data-target='#editArtefactModal' class='btn btn-xs btn-warning'>Edit</button>&nbsp;<button type='button' class='btn btn-xs btn-danger'><b>x</b></button></td></tr>");
			$("#ICtype").append("<option value='"+deploymentModel.internalComponents[a].name+"'>"+deploymentModel.internalComponents[a].name+"</option>");
		}
	}
}

function addRelationshipType(){
	if(deploymentModel.relationships != null){
		for(var a=0;a<deploymentModel.relationships.length;a++){
			$( "#bindingTable>tbody" ).append("<tr><td>"+deploymentModel.relationships[a].name+" </td><td><button type='button' class='btn btn-xs btn-primary'>Instantiate</button>&nbsp;<button type='button' data-toggle='modal' data-target='#editBindingModal' class='btn btn-xs btn-warning'>Edit</button>&nbsp;<button type='button' class='btn btn-xs btn-danger'><b>x</b></button></td></tr>");
		}
	}
}

function addVMTypes(){
	if(deploymentModel.vms != null){
		for(var a=0;a<deploymentModel.vms.length;a++){
			//$( "#nodeTable>tbody" ).append("<tr><td>"+deploymentModel.vms[a].name+" </td><td><button type='button' class='btn btn-xs btn-primary'>Instantiate</button>&nbsp;<button type='button'   data-toggle='modal' data-target='#editNodeModal' class='btn btn-xs btn-warning'>Edit</button>&nbsp;<button type='button' class='btn btn-xs btn-danger'><b>x</b></button></td></tr>");
			$("#VMtype").append("<option value='"+deploymentModel.vms[a].name+"'>"+deploymentModel.vms[a].name+"</option>");
		}
	}
}

function addExternalComponentTypes(){
	if(deploymentModel.externalComponents != null){
		for(var a=0;a<deploymentModel.externalComponents.length;a++){
			//$( "#nodeTable>tbody" ).append("<tr><td>"+deploymentModel.vms[a].name+" </td><td><button type='button' class='btn btn-xs btn-primary'>Instantiate</button>&nbsp;<button type='button'   data-toggle='modal' data-target='#editNodeModal' class='btn btn-xs btn-warning'>Edit</button>&nbsp;<button type='button' class='btn btn-xs btn-danger'><b>x</b></button></td></tr>");
			$("#ECtype").append("<option value='"+deploymentModel.externalComponents[a].name+"'>"+deploymentModel.externalComponents[a].name+"</option>");
		}
	}
}


function addProviders(){
	if(deploymentModel.providers != null){
		for(var a=0;a<deploymentModel.providers.length;a++){
			$( "#providerTable>tbody" ).append("<tr><td>"+deploymentModel.providers[a].name+" </td><td><button type='button' class='btn btn-xs btn-warning'>Edit</button>&nbsp;<button type='button' class='btn btn-xs btn-danger'><b>x</b></button></td></tr>");
		}
	}
}
