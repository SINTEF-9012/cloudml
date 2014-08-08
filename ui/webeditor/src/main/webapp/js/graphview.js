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
var randomLoadChangerButton;
var graphNodes;
var graphEdges;
var width = (window.innerWidth),
    height = (window.innerHeight);
var brush;

var colorScale = d3.scale.linear()
.domain([1, 0.9, 0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2, 0.1, 0.0])
.range(["#a50026","#d73027","#f46d43","#fdae61","#fee08b","#ffffbf","#d9ef8b","#a6d96a","#66bd63","#1a9850","#006837"]);

var loadDispatcher;
var shiftKey;
/*
var RdYlGn = {
    3: ["#fc8d59","#ffffbf","#91cf60"],
    4: ["#d7191c","#fdae61","#a6d96a","#1a9641"],
    5: ["#d7191c","#fdae61","#ffffbf","#a6d96a","#1a9641"],
    6: ["#d73027","#fc8d59","#fee08b","#d9ef8b","#91cf60","#1a9850"],
    7: ["#d73027","#fc8d59","#fee08b","#ffffbf","#d9ef8b","#91cf60","#1a9850"],
    8: ["#d73027","#f46d43","#fdae61","#fee08b","#d9ef8b","#a6d96a","#66bd63","#1a9850"],
    9: ["#d73027","#f46d43","#fdae61","#fee08b","#ffffbf","#d9ef8b","#a6d96a","#66bd63","#1a9850"],
    10: ["#a50026","#d73027","#f46d43","#fdae61","#fee08b","#d9ef8b","#a6d96a","#66bd63","#1a9850","#006837"],
    11: ["#a50026","#d73027","#f46d43","#fdae61","#fee08b","#ffffbf","#d9ef8b","#a6d96a","#66bd63","#1a9850","#006837"]
};
*/

/***********************************************
* Functions to add CloudML elements in the graph
************************************************/

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
            if (tempLink.source != null && tempLink.target != null) {
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
            if (tempLink.source != null && tempLink.target != null) {
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


/***********************************************
* Initialisation of the graph
************************************************/

function getData(jsonString) {
    d3.selectAll("svg").remove();
    root = eval('(' + jsonString + ')');

    intCompInstances = getInternalComponentInstances(root);
    extCompInstances = getExternalComponentInstances(root);
    executesLinks = generateExecutesLinks(root);
    relationshipLinks = generateRelationshipLinks(root);

    force = d3.layout.force()
    .charge(-800)
    .linkDistance(150)
    .size([width, height]);

    svg = d3.select("body").append("svg:svg")
    .attr("width", width)
    .attr("height", height)
    .on("keydown.brush", function (){shiftKey = d3.event.shiftKey;})
    .on("keyup.brush", function (){shiftKey = d3.event.shiftKey;})
    ;

    ////////////////////////  experimental code
    //    loadDispatcher = d3.dispatch("loadChanged");
    //    loadDispatcher.on("loadChanged", randomlyChangeLoad);
    //
    //    randomLoadChangerButton = svg.append("rect")
    //    .attr("width", 100)
    //    .attr("height", 50)
    //    .style("cursor", "pointer")
    //    .on("click", function (){loadDispatcher.loadChanged();});

    ////////////////////////

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
    for(var i=0;i<layoutIntCompInstances.length;i++){
        layoutIntCompInstances[i].isFolded = false;
        layoutIntCompInstances[i].foldedSubNodes = [];
        layoutIntCompInstances[i].foldedSubEdges = [];
        layoutIntCompInstances[i]._type = "InternalComponent";
    }

    var layoutExtCompInstances = extCompInstances;

    for(i=0;i<layoutExtCompInstances.length;i++){
        layoutExtCompInstances[i].isFolded = false;
        layoutExtCompInstances[i].foldedSubNodes = [];
        layoutExtCompInstances[i].foldedSubEdges = [];
        layoutExtCompInstances[i]._type = "ExternalComponent";
    }

    graphNodes = layoutIntCompInstances.concat(layoutExtCompInstances);

    var layoutExecuteLinks = executesLinks;
    for(i=0;i<layoutExecuteLinks.length;i++){
        layoutExecuteLinks[i].isFolded = false;
        layoutExecuteLinks[i]._type = "ExecuteLink";
        layoutExecuteLinks[i]._source = [];
        layoutExecuteLinks[i]._target = [];
    }

    var layoutRelationshipLinks = relationshipLinks;
    for(i=0;i<layoutRelationshipLinks.length;i++){
        layoutRelationshipLinks[i].isFolded = false;
        layoutRelationshipLinks[i]._type = "RelationshipLink";
        layoutRelationshipLinks[i]._source = [];
        layoutRelationshipLinks[i]._target = [];
    }

    graphEdges = layoutExecuteLinks.concat(layoutRelationshipLinks);

    force
    .nodes(graphNodes)
    .links(graphEdges)
    .start();

    svg.append('svg:g').attr("class", "nodes");
    svg.append('svg:g').attr("class", "edges");

    update();
}

function getNodeState(node){
    var result = "";
    result += 'type: ';
    result += node.type;
    result += '<br/>';
    result += "sub-nodes: ";
    if(node.foldedSubNodes.length>0){
        for(i=0;i<node.foldedSubNodes.length;++i){

            // folded subnodes
            result += node.foldedSubNodes[i].name;
            if(!(node.foldedSubNodes.length == i+1)){
                result+= ', ';
            }

        }
    }
    return result;
}


function update(){
    allNodesGroup = svg.select('.nodes');

    var nodeDataReference = allNodesGroup.selectAll('g').data(graphNodes);

    // remove deleted nodes from graph
    nodeDataReference.exit().remove();
    var counter;
    // create a group element for each data entry, combining the according individual nodes' elements
    var nodeGroup = nodeDataReference
    .enter()
    .append('svg:g')
    .attr({
        'class'           :   'singleNode',
        'id'              :   function(d) { return d.name; },
        'data-content'    :   function(d){ return getNodeState(d) }
    })
    .each(function (d){
        // define a popover for each of the elements
        $(this).popover(
            {
                'container' : 'body',
                'placement' : 'auto right',
                'title'     : d.name,
                'template'  : '<div class="popover" role="tooltip"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content" id="popover_content_' + d.name + '"></div></div>',
                //                'content'   : getNodeState(d),
                //                'delay'     : {show: 500, hide: 100},
                'html'      : true,
                'trigger'   : 'manual'
            }
        )
        // listeners for the popover enabling it to stay shown when hovered over
        .on("mouseenter", function () {
            var _this = this;
            $(this).popover("show");
            $(".popover").on("mouseleave", function () {
                $(_this).popover('hide');
            });
        })
        .on("mouseleave", function () {
            var _this = this;
            setTimeout(function () {
                if (!$(".popover:hover").length) {
                    $(_this).popover("hide")
                }
            }, 100);
        })
        // listeners to allow to change styles of the elements for some basic animation
        .on("mouseover", function () {
            d3.select(this).selectAll('circle').classed("hover", true);
        })
        .on("mouseout", function () {
            d3.select(this).selectAll('circle').classed("hover", false);
        })
        .popover()
    })
    // we define for each of the nodes and observer used that listens to changes 
    // of the attribute 'data-content' to enable dynamic popovers content
    .each(function(d){
        window.MutationObserver = window.MutationObserver
        || window.WebKitMutationObserver
        || window.MozMutationObserver;

        var target = this,
            // use the observer to dynamically edit the content of the popover
            observer = new MutationObserver(function(mutations) {
                mutations.forEach(function (mutation){
                    var targetPopoverContentId = 'popover_content_' + mutation.target.id;
                    var affectedPopover = $('div.popover-content').filter(
                        function() {
                            return this.id == targetPopoverContentId;
                        }
                    );
                    affectedPopover.html($(mutation.target).attr('data-content'));
                });

            }),
            config = {
                attributes: true,
                attributeFilter: ['data-content']
            };
        observer.observe(target, config);
    })

    ;

//    var drag = d3.behavior.drag()
//    .on("drag", function(d, i) {
//        console.log("drag");
//        d.px += d3.event.dx;
//        d.py += d3.event.dy;
//        d.x += d3.event.dx;
//        d.y += d3.event.dy;
//        tick();
//    })
//    .on("dragend", function(d, i){
//        console.log("dragend");
//        svg.selectAll('.selectionCircle .internalComponent .externalComponent .tmpNodeSymbol .tmpToggleFoldSymbol').call(force.drag);
//    });

    if(!brush){
        brush = svg.insert("g", "g.nodes")
        .datum(function() { 
            return {selected: false, previouslySelected: false}; 
        })
        .attr("class", "brush")
        .call(d3.svg.brush()
              .x(d3.scale.identity().domain([0, width]))
              .y(d3.scale.identity().domain([0, height]))
              .on("brushstart", function(d) {
                  nodeGroup.each(function(d) { d.previouslySelected = shiftKey && d.selected; });
              })
              .on("brush", function() {
                  var extent = d3.event.target.extent();
                  nodeGroup.each(function(d){
                      svg.selectAll('g.singleNode').filter(
                          function(){
                              return this.id == d.name;
                          }
                      )
                      .selectAll('.selectionCircle')
                      .classed("selected", function() {
                          return (extent[0][0] <= d.x && d.x < extent[1][0]
                                  && extent[0][1] <= d.y && d.y < extent[1][1]);
                      })
                  })

              })
              .on("brushend", function() {
                  d3.event.target.clear();
                  d3.select(this).call(d3.event.target);
              })
             );

    }

    // create a circle SVG element to represent each node
    nodeGroup.append("svg:circle")
    .attr({
        'class' :   function (d){
            return d._type=="InternalComponent" ? "internalComponent" : "externalComponent";
        },
        'r'     : 25.5,
    })
    .call(force.drag);

    nodeGroup.append("svg:circle")
    .attr({
        'class' : 'selectionCircle',

        'r'     : 28,
    })
    .call(force.drag);

    // show symbols (for now - later - TODO: icons)
    nodeGroup.append('svg:text')
    .attr('class', 'tmpNodeSymbol')
    .text(function (d) {
        return d._type=="InternalComponent" ? '\uf013' : '\uf0c2';
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

    // temporary implementation using a fold symbol
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
        return d._type == "ExecuteLink" ? "executionBinding" : "link";
    })
    .attr("id", function(d) {
        return d.id;
    })
    .style('marker-start', '')
    .style('marker-end', function(d) {
        return d._type == "ExecuteLink" ? 'url(#execute-arrow)' : 'url(#relationship-arrow)';
    });



    d3.selectAll("text")
    .on("mouseover", function () {
        d3.event.preventDefault();

    });

    force.on("tick", tick);
}

function tick (e) {
    var path = svg.select(".edges").selectAll('path');
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
    var allSingleNodes = svg.selectAll(".singleNode");
    allSingleNodes.attr('transform', function (d) {
        return 'translate(' + d.x + ',' + d.y + ')';
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

    // find the SVG element that corresponds to the toggled node
    var svgNodeElement = svg.selectAll(".singleNode").filter(
        function() {
            return this.id == nodeToToggle.name;
        }
    );

    svgNodeElement.attr("data-content", function(d){
        return getNodeState(nodeToToggle)
    });

    // refresh the force layout
    force.start();
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

    for(var i=0;i<edgesNodesToFold.edgesToFold.length;++i){
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
        for(var i=0;i<graphEdges.length;++i){

            if(graphEdges[i]._source.length > 0){
                if(graphEdges[i]._source[graphEdges[i]._source.length-1] == currentlyCheckedNode){
                    graphEdges[i].source = graphEdges[i]._source.pop();
                }
            }

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
    var nodeHost = findNodeHost(aNode);
    // evaluate (at least) the input node for edges and sub-nodes to fold (hide)
    // in case we find any sub-nodes to fold - we also check their sub-sub-nodes
    while(nodesToCheck.length>0){
        var currentlyCheckedNode = nodesToCheck.pop();

        // we check each of the edges in the graph
        graphEdges.forEach(
            function(edge){
                if(edge.source == currentlyCheckedNode){

                    // in case it is not in the list of edges to fold (hide) - add it
                    if(edgesToFold.indexOf(edge)==-1){
                        var edgeTargetNodeHost = findNodeHost(edge.target);
                        if(nodeHost == edgeTargetNodeHost){
                            edgesToFold.push(edge);
                            // in case the edge's target is not in the nodes to fold (hide) - add it
                            if(nodesToFold.indexOf(edge.target)==-1){
                                nodesToFold.push(edge.target);
                            }
                            // in case the edge's target is not in the list of nodes to evaluate - add it
                            if(nodesToCheck.indexOf(edge.target)==-1){
                                nodesToCheck.push(edge.target);
                            }
                        }
                    }


                }
            }
        );
    }

    // result contains both the nodes and the edges that need to be folded (hidden) from the graph
    var result={};
    result.nodesToFold = nodesToFold;
    result.edgesToFold = edgesToFold;

    return result;
}

function findNodeHost(aNode){
    var result = [];
    var currentlyCheckedNode = aNode;
    var firstCycleFlag = true;
    if(aNode._type == "ExternalComponent"){
        return aNode;
    }else{
        for(i=0;i<graphEdges.length;i++){
            var edge = graphEdges[i];
            if(edge.target == aNode && edge._type == "ExecuteLink"){
                // we have found the host but it could be an internal component
                if(edge.source._type == "InternalComponent"){
                    return findNodeHost(edge.source);
                }
                return edge.source;
            }
        }
        console.log("error", "No execution binding found for the clicked node. Check model for errors. " + aNode.name);
    }
}

//recalculate the edges in the graph when a node is folded
function recalculateEdges(aNode){
    var currentEdge;
    for(i=0;i<graphEdges.length;++i){
        currentEdge = graphEdges[i];

        var currentEdgeTargetIndex = aNode.foldedSubNodes.indexOf(currentEdge.target);
        // if the target of the currently evaluated edge is one of the folded sub-nodes of the evaluated node
        if(currentEdgeTargetIndex != -1){
            // redirect it to the currently evaluated node
            // but keeping the reference to the folded sub-node
            currentEdge._target.push(currentEdge.target);
            currentEdge.target = aNode;
        }

        var currentEdgeSourceIndex = aNode.foldedSubNodes.indexOf(currentEdge.source);

        if(currentEdgeSourceIndex != -1){
            // redirect it to the currently evaluated node
            // but keeping the reference to the folded sub-node
            currentEdge._source.push(currentEdge.source);
            currentEdge.source = aNode;
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
                return this.id == currentNode.name;
            });
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

// test function only
//function randomlyChangeLoad(){
//    var colorCode = colorScale(Math.random());
//    var tmp = svg.selectAll("circle.externalComponent");
////    console.log(tmp + " - length: " + tmp.length);
//    //    var chosenCircleIndex = Math.floor((Math.random() * allCircles.length) + 0);
//    //    chosenCircle.style('fill', colorCode);
//    var allCircles = svg.selectAll('circle.externalComponent')/*.find(':eq(' + 1 + ')')*/.style("stroke",colorCode);
//    //    console.log(allCircles);
//    //    var chosenCircleIndex = Math.floor((Math.random() * allCircles.length) + 0);
//    //    console.log(chosenCircleIndex+ "/" + allCircles.length);
//    //    var chosenCircle = allCircles[0][chosenCircleIndex];
//    //    console.log(chosenCircle);
//    //    var colorCode = colorScale(Math.random());
//    //    chosenCircle.style('fill', colorCode);
//}


