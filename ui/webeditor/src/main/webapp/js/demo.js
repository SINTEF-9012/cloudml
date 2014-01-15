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
	var instance;
	
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
		// this is the connecting style for optional communication
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
            dragOptions:{}
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
			addNodeInstances();
			addArtefactsInstances();
			instance.draggable(jsPlumb.getSelector(".flowchart-demo .window"), { grid: [20, 20] });
			addPortInstances();
			addBindinInstances();
		});
		
		
		instance.bind("click", function(conn, originalEvent) {
			if (confirm("Delete connection from " + conn.sourceId + " to " + conn.targetId + "?"))
				jsPlumb.detach(conn); 
		});	
	});
	
	
	function addNodeInstances(){
			for(a=0;a<deploymentModel.nodeInstances.length;a++){
				var nodeType=findNodeTypeByName(deploymentModel.nodeInstances[a].type);
				var nodeInfo="minRam:"+nodeType.minRam+
							"&lt;br&gt; minCore:"+nodeType.minCore+
							"&lt;br&gt; minDisk:"+nodeType.minDisk+
							"&lt;br&gt; sshKey:"+nodeType.sshKey+
							"&lt;br&gt; OS:"+nodeType.OS+
							"&lt;br&gt; location:"+nodeType.location;
				
				$( "#flowchart-demo").append( "<div class='window _vm' data-content='"+nodeInfo+"' data-original-title='Type Info:' id='flowchartWindow_"+deploymentModel.nodeInstances[a].name+"'><strong>"+ deploymentModel.nodeInstances[a].name +"</strong><br/><br></div>" );
				placeRandomly("flowchartWindow_"+deploymentModel.nodeInstances[a].name);
				targetEndpoint.overlays[0][1].label = "destination";
				instance.addEndpoint("flowchartWindow_"+deploymentModel.nodeInstances[a].name, targetEndpoint, { anchor:"Continuous", uuid:"nodeInstances["+deploymentModel.nodeInstances[a].name+"]" });
				
				$('#flowchartWindow_'+deploymentModel.nodeInstances[a].name).popover({html:true});
			}

		}
		
		function addArtefactsInstances(){
			for(a=0;a<deploymentModel.artefactInstances.length;a++){
				$( "#flowchart-demo").append( "<div class='window _art' id='flowchartWindow_"+deploymentModel.artefactInstances[a].name+"'><strong>"+ deploymentModel.artefactInstances[a].name +"</strong><br/><br></div>" );
				placeRandomly("flowchartWindow_"+deploymentModel.artefactInstances[a].name);
				//$('#flowchartWindow_'+deploymentModel.artefactInstances[a].name).popover();
			}
		}
		
		function placeRandomly(id){
			var x=Math.floor((Math.random()*50)+1);
			var y=Math.floor((Math.random()*50)+1);
			$("#"+id).attr("style","top:"+y+"em;left:"+x+"em;");
		}
		
		function addPortInstances(){
			for(a=0;a<deploymentModel.artefactInstances.length;a++){
				var i=0;
				instance.addEndpoint("flowchartWindow_"+deploymentModel.artefactInstances[a].name, sourceDestEndpoint, { anchor:"Continuous", uuid:deploymentModel.artefactInstances[a].name+"_destination"});
				if(deploymentModel.artefactInstances[a].required != null){
					for(i=0; i<deploymentModel.artefactInstances[a].required.length;i++){
						sourceEndpoint.overlays[0][1].label = deploymentModel.artefactInstances[a].required[i].name;
						var port=findPortType(deploymentModel.artefactInstances[a].type,deploymentModel.artefactInstances[a].required[i].type);
						if(!port.isOptional){
							instance.addEndpoint("flowchartWindow_"+deploymentModel.artefactInstances[a].name, sourceEndpoint, { anchor:"Continuous", uuid:"artefactInstances["+deploymentModel.artefactInstances[a].name+"]/required["+deploymentModel.artefactInstances[a].required[i].name +"]"});
						}else{
							instance.addEndpoint("flowchartWindow_"+deploymentModel.artefactInstances[a].name, sourceOptionalEndpoint, { anchor:"Continuous", uuid:"artefactInstances["+deploymentModel.artefactInstances[a].name+"]/required["+deploymentModel.artefactInstances[a].required[i].name +"]"});
						}
						
					}
				}
				if(deploymentModel.artefactInstances[a].provided != null){
					for(i=0; i<deploymentModel.artefactInstances[a].provided.length;i++){
						targetEndpoint.overlays[0][1].label = deploymentModel.artefactInstances[a].provided[i].name;
						instance.addEndpoint("flowchartWindow_"+deploymentModel.artefactInstances[a].name, targetEndpoint, { anchor:"Continuous", uuid:"artefactInstances["+deploymentModel.artefactInstances[a].name+"]/provided["+deploymentModel.artefactInstances[a].provided[i].name +"]"});
					}
				}
			}
		}
		
		function findPortType(artefactTypeName,portTypeName){
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
			for(var a=0;a<deploymentModel.artefactTypes.length;a++){
				if(name.indexOf(deploymentModel.artefactTypes[a].name) >= 0){
					return deploymentModel.artefactTypes[a];
				}
			}
			return null;
		}
		
		function findNodeTypeByName(name){
			for(var a=0;a<deploymentModel.nodeTypes.length;a++){
				if(name.indexOf(deploymentModel.nodeTypes[a].name) >= 0){
					return deploymentModel.nodeTypes[a];
				}
			}
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
			return null;
		}
		
		function addBindinInstances(){
			for(a=0; a< deploymentModel.bindingInstances.length;a++){
				var connection=instance.connect({uuids:[deploymentModel.bindingInstances[a].server, deploymentModel.bindingInstances[a].client], editable:true});
				connection.getOverlay("label").setLabel(deploymentModel.bindingInstances[a].name);
				
				var artefactinstance=findArtefactInstanceByName(deploymentModel.bindingInstances[a].client);
				var portinstance=findPortInstanceByName(artefactinstance,deploymentModel.bindingInstances[a].client);
				var port=findPortType(artefactinstance.type, portinstance.type);
				
				if((port != null) && !port.isRemote){
					connection.removeOverlay("myarrow");
					connection.addOverlay([ "PlainArrow", { location:1 , id:"myarrow"} ]);
				}
			}
			for(a=0;a<deploymentModel.artefactInstances.length;a++){
				if(deploymentModel.artefactInstances[a].destination != null){
					instance.connect({uuids:[ deploymentModel.artefactInstances[a].name+"_destination", deploymentModel.artefactInstances[a].destination ], editable:true});
				}
			}
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
        console.log("error","Um, couldn't find the fileinput element.",5000);
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

function updateProgress (evt) {
    // evt is an ProgressEvent.
    if (evt.lengthComputable) {
      var percentLoaded = Math.round((evt.loaded / evt.total) * 100);
      // Increase the progress bar length.
      if (percentLoaded < 100) {
        progress.css("width",percentLoaded + '%');
      }
    }
  }

 function reset(){
	instance.reset();
	instance.repaintEverything();
	$( "#flowchart-demo").empty();
	$( "#selectableBindings" ).empty();
	$( "#selectableNodes" ).empty();
	$( "#selectableArtefacts" ).empty();
 }
  
 //Load the deployment model
function loadDeploymentModel(jsonString) {
	deploymentModel=eval("(" + jsonString + ")");
	reset();
	addArtefactType();
	addNodeTypes();
	addBindingType();
	instance.doWhileSuspended(function() {
		addNodeInstances();
		addArtefactsInstances();
		instance.draggable(jsPlumb.getSelector(".flowchart-demo .window"), { grid: [20, 20] });
		addPortInstances();
		addBindinInstances();
	});

}

//Alert Messages
function alertMessage(type,message,timeout) {
	alertDiv = $(document.createElement('div'));

	switch (type) {
		case "success":
			alertDiv.attr("class","alert alert-success fade in")
					.html("<b>Success.</b> "+message);
			break;
		case "error":
			alertDiv.attr("class","alert alert-error fade in")
					.html("<b>Error.</b> "+message);
			break;
		case "warning":
			alertDiv.attr("class","alert fade in")
					.html("<b>Warning.</b> "+message);
			break;
		default:
			break;
	}

	alertDiv.append(
			$(document.createElement('a'))
				.attr("class","close")
				.attr("data-dismiss","alert")
				.html("&times;")
		);

	$('#alert-div').append(alertDiv);
	if(typeof timeout!='undefined')
		window.setTimeout(function() { $('#alert-div').find(':contains('+message+')').remove(); }, timeout);
}