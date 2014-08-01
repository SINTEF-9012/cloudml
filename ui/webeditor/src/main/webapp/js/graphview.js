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


var root;
var allNodesGroup;
var force;
var intCompInstances;
var extCompInstances;
var executesLinks;
var relationshipLinks;
var svg;

var graphNodes;
var graphEdges;

//var RdYlGn = {
//3: ["#fc8d59","#ffffbf","#91cf60"],
//4: ["#d7191c","#fdae61","#a6d96a","#1a9641"],
//5: ["#d7191c","#fdae61","#ffffbf","#a6d96a","#1a9641"],
//6: ["#d73027","#fc8d59","#fee08b","#d9ef8b","#91cf60","#1a9850"],
//7: ["#d73027","#fc8d59","#fee08b","#ffffbf","#d9ef8b","#91cf60","#1a9850"],
//8: ["#d73027","#f46d43","#fdae61","#fee08b","#d9ef8b","#a6d96a","#66bd63","#1a9850"],
//9: ["#d73027","#f46d43","#fdae61","#fee08b","#ffffbf","#d9ef8b","#a6d96a","#66bd63","#1a9850"],
//10: ["#a50026","#d73027","#f46d43","#fdae61","#fee08b","#d9ef8b","#a6d96a","#66bd63","#1a9850","#006837"],
//11: ["#a50026","#d73027","#f46d43","#fdae61","#fee08b","#ffffbf","#d9ef8b","#a6d96a","#66bd63","#1a9850","#006837"]
//};

function loadInNodesArray(nodesArray, componentInstances) {
    componentInstances.forEach(

        function (componentInstance) {
            nodesArray.push(componentInstance);
        });
}

function getInternalComponentInstances(deploymentModel) {
    var instancesArray = [];
    if (deploymentModel.internalComponentInstances != null) {
        loadInNodesArray(instancesArray, deploymentModel.internalComponentInstances);
    }
    return instancesArray;
}

function getExternalComponentInstances(deploymentModel) {
    var instancesArray = [];
    if (deploymentModel.vmInstances != null) {
        loadInNodesArray(instancesArray, deploymentModel.vmInstances);
    }
    if (deploymentModel.externalComponentInstances != null) {
        loadInNodesArray(instancesArray, deploymentModel.externalComponentInstances);
    }
    return instancesArray;
}

function generateExecutesLinks(deploymentModel) {
    var links = [];
    deploymentModel.executesInstances.forEach(

        function (d, i) {
            var tempLink = {
                source : null,
                target : null
            };
            tempLink.id = d.name;
            tempLink.source = findSource(d.providedExecutionPlatformInstance, deploymentModel);
            tempLink.left = false;
            tempLink.target = findTarget(d.requiredExecutionPlatformInstance, deploymentModel);
            tempLink.right = true;
            if (tempLink.source !== null && tempLink.target !== null) {
                links.push(tempLink);
            }
        });
    return links;
}

function generateRelationshipLinks(deploymentModel) {
    var links = [];
    deploymentModel.relationshipInstances.forEach(

        function (d, i) {
            var tempLink = {
                source : null,
                target : null
            };
            tempLink.id = d.name;
            tempLink.source = findSource(d.providedPortInstance, deploymentModel);
            tempLink.target = findTarget(d.requiredPortInstance, deploymentModel);
            if (tempLink.source !== null && tempLink.target !== null) {
                tempLink.source.outgoingLinks == null ? tempLink.source.outgoingLinks = [tempLink] : tempLink.source.outgoingLinks.push(tempLink);
                tempLink.target.incomingLinks == null ? tempLink.target.incomingLinks = [tempLink] : tempLink.target.incomingLinks.push(tempLink);
                links.push(tempLink);
            } else {
                window.alert("bouhaaa");
            }
        });
    return links;
}


function findSource(refSource, deploymentModel) {
    var temp = refSource.split("/");
    return getJSON(deploymentModel, "/" + temp[0]);
}

function findTarget(refTarget, deploymentModel) {
    var temp = refTarget.split("/");
    return getJSON(deploymentModel, "/" + temp[0]);
}

function getData(jsonString) {
	root = eval('(' + jsonString + ')');
	
	intCompInstances = getInternalComponentInstances(root);
    extCompInstances = getExternalComponentInstances(root);
    executesLinks = generateExecutesLinks(root);
    relationshipLinks = generateRelationshipLinks(root);

    var width = (window.innerWidth),
        height = (window.innerHeight);

    force = d3.layout.force()
    .charge(-800)
    .linkDistance(150)
    .size([width, height]);

    svg = d3.select("body").append("svg:svg")
    .attr("width", width)
    .attr("height", height);

    intCompInstances.concat(extCompInstances).forEach(function (d, i) {
        d.x = width / 2 + i;
        d.y = height / 2 + 100 * d.depth;
    });

    root.fixed = true;
    root.x = width / 2;
    root.y = height / 2;
    
    // define arrow markers for graph links
    svg.append('svg:defs').append('svg:marker')
    .attr('id', 'execute-arrow')
    .attr('viewBox', '0 -5 10 10')
    .attr('refX', 6)
    .attr('markerWidth', 4)
    .attr('markerHeight', 4)
    .attr('orient', 'auto')
    .append('svg:path')
    .attr('d', 'M0,-5L10,0L0,5')
    .attr('class','executionArrow');
    
    svg.append('svg:defs').append('svg:marker')
    .attr('id', 'relationship-arrow')
    .attr('viewBox', '0 -5 10 10')
    .attr('refX', 6)
    .attr('markerWidth', 4)
    .attr('markerHeight', 4)
    .attr('orient', 'auto')
    .append('svg:path')
    .attr('d', 'M0,-5L10,0L0,5')
    .attr('class','relationshipArrow');

    var layoutIntCompInstances = intCompInstances;
    for(i=0;i<layoutIntCompInstances.length;i++){
        layoutIntCompInstances[i].isFolded = false;
        layoutIntCompInstances[i].foldedSubNodes = [];
        layoutIntCompInstances[i].foldedSubEdges = [];
        layoutIntCompInstances[i].type = "InternalComponent";
    }
    
    var layoutExtCompInstances = extCompInstances;
    
    for(i=0;i<layoutExtCompInstances.length;i++){
        layoutExtCompInstances[i].isFolded = false;
        layoutExtCompInstances[i].foldedSubNodes = [];
        layoutExtCompInstances[i].foldedSubEdges = [];
        layoutExtCompInstances[i].type = "ExternalComponent";
    }
    
    graphNodes = layoutIntCompInstances.concat(layoutExtCompInstances);
    
    var layoutExecuteLinks = executesLinks;
    for(i=0;i<layoutExecuteLinks.length;i++){
        layoutExecuteLinks[i].isFolded = false;
        layoutExecuteLinks[i].type = "ExecuteLink";
        layoutExecuteLinks[i]._target = [];
    }
    
    var layoutRelationshipLinks = relationshipLinks;
    for(i=0;i<layoutRelationshipLinks.length;i++){
        layoutRelationshipLinks[i].isFolded = false;
        layoutRelationshipLinks[i].type = "RelationshipLink";
        layoutRelationshipLinks[i]._target = [];
    }
    
    graphEdges = layoutExecuteLinks.concat(layoutRelationshipLinks);
    
    force
    .nodes(graphNodes)
    .links(graphEdges)
    .start();
    
    svg.append('svg:g').attr("class", "nodes");
    svg.append('svg:g').attr("class", "edges");
    
//    var scale = d3.scale.log().domain([0, 100]);
//    scale.domain([0, 0.5, 1].map(scale.invert));
//    scale.range(["green", "yellow", "red"]);
//    
//    for(int i=0;i<100;i++){
//        
//    }
    
    update();
}

function update(){
    allNodesGroup = svg.select('.nodes');
    
    var nodeDataReference = allNodesGroup.selectAll('g').data(graphNodes);
    
    // remove deleted nodes from graph
    nodeDataReference.exit().remove();
    
    var nodeGroup = nodeDataReference
    .enter()
    .append('svg:g')
    .attr("class", "singleNode")
    .attr("id", function(d) {
        return d.name;
    });
    
//    console.log("exit selection of nodeDataReference");
//    nodeGroup.selectAll('g').exit();
    
    nodeGroup.append("svg:circle")
    .attr("r", 25.5)
    .attr("class", function (d) {
        return d.type=="InternalComponent" ? "internalComponent" : "externalComponent";
    })
    .call(force.drag);
    
    // show symbols (for now - later - TODO: icons)
    nodeGroup.append('svg:text')
    .attr('class', 'tmpNodeSymbol')
    .text(function (d) {
        return d.type=="InternalComponent" ? '\uf013' : '\uf0c2';
    })
    .call(force.drag);
    
    var foldedText = nodeGroup.append('svg:text')
    .attr('class', 'tmpToggleFoldSymbol')
    .attr('dx','5')
    .attr('dy','-5')
    .call(force.drag);
    
    // show node IDs
    nodeGroup.append('svg:text')
    .attr('x', '0')
    .attr('y', '30')
    .attr('text-anchor', 'middle')
    .attr('dominant-baseline', 'central')
    .attr('font-family', 'sans-serif')
    .attr('font-size', '10px')
    .text(function (d) {
        return d.name;
    });
    
    
    svg.selectAll('.tmpToggleFoldSymbol')
    .text(function (d) {
        return d.isFolded ? '\uf150' : ''; //'\uf0aa';
    });
    
    svg.selectAll('.tmpNodeSymbol')
    .attr('dx', function(d) {
        return d.isFolded ? '-5' : '0';
    });
    
    // add listener for mouseclick for folding
    nodeGroup.on("click", toggleFoldNode);
    
    
    var path = svg.select(".edges").selectAll('path');
    path = path.data(graphEdges);
    
    path.exit().remove();

    path.enter().append('svg:path')
    .attr('class', function (d, i) {
        return d.type == "ExecuteLink" ? "executionBinding" : "link";
    })
    .attr("id", function(d) {
        return d.id;
    })
    .style('marker-start', '')
    .style('marker-end', function(d) {
        return d.type == "ExecuteLink" ? 'url(#execute-arrow)' : 'url(#relationship-arrow)';
    });

    

    d3.select("text")
    .on("mouseover", function () {
        d3.event.preventDefault();

    });
    
    var allSingleNodes = svg.selectAll(".singleNode");
    
    force.on("tick", function (e) {

        path.attr('d', function (d) {
            var deltaX = d.target.x - d.source.x,
                deltaY = d.target.y - d.source.y,
                dist = Math.sqrt(deltaX * deltaX + deltaY * deltaY),
                normX = deltaX / dist,
                normY = deltaY / dist,
                sourcePadding = 25,
                targetPadding = 28,
                sourceX = d.source.x + (sourcePadding * normX),
                sourceY = d.source.y + (sourcePadding * normY),
                targetX = d.target.x - (targetPadding * normX),
                targetY = d.target.y - (targetPadding * normY);

            return 'M' + sourceX + ',' + sourceY + 'L' + targetX + ',' + targetY;
        });

        allSingleNodes.attr('transform', function (d) {
            return 'translate(' + d.x + ',' + d.y + ')';
        });

    });
}

function toggleFoldNode(nodeToToggle){
    
    // so that we won't fold while dragging
    if (d3.event.defaultPrevented) return;
    
    if(nodeToToggle.isFolded){
        // we should unfold the node
        unfoldNode(nodeToToggle);
    }else{
        // we should fold the node
        foldNode(nodeToToggle);
    }
    
    // refresh the force layout
    force.start();
    console.log("updating");
    update();
}

function foldNode(nodeToFold){
    nodeToFold.isFolded=true;
        
    var edgesNodesToFold = findEdgesAndNodesToFold(nodeToFold);
    if(edgesNodesToFold.edgesToFold.length==0 && edgesNodesToFold.nodesToFold.length == 0){
        nodeToFold.isFolded = false;
        return;
    }
    hideEdges(edgesNodesToFold.edgesToFold);

    for(i=0;i<edgesNodesToFold.edgesToFold.length;++i){
        nodeToFold.foldedSubEdges.push(edgesNodesToFold.edgesToFold[i]);
    }

    hideNodes(edgesNodesToFold.nodesToFold);
    for(i=0;i<edgesNodesToFold.nodesToFold.length;++i){
        nodeToFold.foldedSubNodes.push(edgesNodesToFold.nodesToFold[i]);
    }

    recalculateEdges(nodeToFold);
}

function unfoldNode(nodeToUnfold){
    nodeToUnfold.isFolded=false;
    var foldedNodesToCheck = [];
    var currentlyEvaluatedFoldedNode;
    
    // unfold the folded sub-nodes of the currently unfolded node
    while(nodeToUnfold.foldedSubNodes.length >0){
        currentlyEvaluatedFoldedNode = nodeToUnfold.foldedSubNodes.pop();
        graphNodes.push(currentlyEvaluatedFoldedNode);
        foldedNodesToCheck.push(currentlyEvaluatedFoldedNode);
    }
    
    // update the force layout
    force.nodes(graphNodes);

    // unfold the folded sub-edges of the currently unfolded node
    while(nodeToUnfold.foldedSubEdges.length >0){
        graphEdges.push(nodeToUnfold.foldedSubEdges.pop());
    }
    
    foldedNodesToCheck.push(nodeToUnfold);
    
    // undo the redirecting of the edges due to folding of nodes
    while(foldedNodesToCheck.length>0){
        var currentlyCheckedNode = foldedNodesToCheck.pop();
        for(i=0;i<graphEdges.length;++i){
            if(graphEdges[i]._target.length > 0){
                if(graphEdges[i]._target[graphEdges[i]._target.length-1] == currentlyCheckedNode){
                    graphEdges[i].target = graphEdges[i]._target.pop();
                }
            }

        }
    }
    // update the edges of the force layout
    force.links(graphEdges);
}

//returns an object containing arrays with the nodes and edges to fold
function findEdgesAndNodesToFold(aNode){

    var edgesToFold=[];
    var nodesToFold=[];
    var nodesToCheck=[aNode];

    // evaluate (at least) the input node for edges and sub-nodes to fold (hide)
    // in case we find any sub-nodes to fold - we also check their sub-sub-nodes
    while(nodesToCheck.length>0){
        var currentlyCheckedNode = nodesToCheck.pop();
        
        // we check each of the edges in the graph
        for(i=0;i<graphEdges.length;++i){
            
            // if the node being evaluated is a source of a certain edge in the graph
            if(graphEdges[i].source == currentlyCheckedNode){
                
                // in case it is not in the list of edges to fold (hide) - add it
                if(edgesToFold.indexOf(graphEdges[i])==-1){
                    edgesToFold.push(graphEdges[i]);
                }
                
                // in case the edge's target is not in the nodes to fold (hide) - add it
                if(nodesToFold.indexOf(graphEdges[i].target)==-1)
                    nodesToFold.push(graphEdges[i].target);
                
                // in case the edge's target is not in the list of nodes to evaluate - add it
                if(nodesToCheck.indexOf(graphEdges[i].target)==-1)
                    nodesToCheck.push(graphEdges[i].target);
            }
        }
    }
    
    // result contains both the nodes and the edges that need to be folded (hidden) from the graph
    var result={};
    result.nodesToFold = nodesToFold;
    result.edgesToFold = edgesToFold;
    
    return result;
}

function findNodeHost(aNode){
    var currentlyCheckedNode = aNode;
    while(currentlyCheckedNode){
        graphEdges.forEach(function(edge){
//            if(edge.target == currentlyCheckedNode && edge.source.type == "ExternalComponent"){
//                return currentlyCheckedNode;
//            }else{
//                currentlyCheckedNode = edge.source;
//            }
        });
    }
    
}

//recalculate the edges in the graph when a node is folded
function recalculateEdges(aNode){
    var currentEdge;
    for(i=0;i<graphEdges.length;++i){
        currentEdge = graphEdges[i];
        
        var index = aNode.foldedSubNodes.indexOf(currentEdge.target)
        // if the target of the currently evaluated edge is one of the folded sub-nodes of the evaluated node
        if(index != -1){
            // redirect it to the currently evaluated node
            // but keeping the reference to the folded sub-node
            currentEdge._target.push(currentEdge.target);
            currentEdge.target = aNode;
        }
    }
}

function hideEdges(edgesToFold){
    var currentEdgeIndex;
    var currentEdge;
    for(i=0;i<edgesToFold.length;++i){
        currentEdgeIndex=graphEdges.indexOf(edgesToFold[i]);
        currentEdge = graphEdges[currentEdgeIndex];
        
        // remove edge from the graph data
        var svgEdgeElement = svg.selectAll(".executionBinding, .link").filter(
            function() {
                return this.id == currentEdge.id;
        });
        console.log("removing...");
        console.log(svgEdgeElement);
        
        svgEdgeElement.remove();
        graphEdges.splice(currentEdgeIndex,1);
    }
}

function hideNodes(nodesToFold){
    var currentNodeIndex;
    var currentNode;
    for(i=0;i<nodesToFold.length;++i){
        currentNodeIndex = graphNodes.indexOf(nodesToFold[i]);
        currentNode = graphNodes[currentNodeIndex];
        
        // remove node from the graph data
        var svgNodeElement = svg.selectAll(".singleNode").filter(
            function() {
                console.log(this.id + "==" + currentNode.name);
                return this.id == currentNode.name;
        });
//        var svgNodeElement = svg.select("#" + currentNode.name);
        console.log("removing node...");
        console.log(svgNodeElement);
        svgNodeElement.remove();
        graphNodes.splice(currentNodeIndex,1);
    }
}

function reset(){
    svg.remove();
}

function loadFile(inputDiv) {
	var input,
	file,
	fr;
	if (typeof window.FileReader !== 'function') {
		showAlert("p", "The file API isn't supported on this browser yet.");
		return;
	}

	//Error Catching
	input = $('#' + inputDiv).find('input').get(0);

	if (!input) {
		console.log("error", "Hum, couldn't find the fileinput element.", 5000);
	} else if (!input.files) {
		console.log("error", "This browser doesn't seem to support the `files` property of file inputs", 5000);
	} else if (!input.files[0]) {
		console.log("error", "Please select a file before clicking 'Load'", 5000);
	}
	//its ok
	else {
		file = input.files[0];
		fr = new FileReader();
		fr.onload = receivedText;
		fr.readAsText(file);
	}

	function receivedText() {
		getData(fr.result);
	}
}


