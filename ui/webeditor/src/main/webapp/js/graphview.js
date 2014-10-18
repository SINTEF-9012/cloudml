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


/// NOTE: duplicate variable with the one in demo.js; used in order to enable for reuse of websocket.js
var currentJSON;

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
var contextMenu;
var cloudMLServerHost;
var connectedToCloudMLServer = false;

var stateColorMap = {};
stateColorMap['PENDING'] = "#fee08b";
stateColorMap['TERMINATED'] = "#a50026";
stateColorMap['STOPPED'] = "#BAC2C3";
stateColorMap['RUNNING'] = "#74AF7A";
stateColorMap['ERROR'] = "#a50026";
stateColorMap['UNCATEGORIZED'] = "#BAC2C3";
stateColorMap['RECOVERY'] = "#4B9D9B";


/***********************************************
JS-YAML parser definitions
***********************************************/

// define all of the unrecognizable yaml tags and types contained in the response from the CloudML server (e.g. the tag !!org.cloudml.core.VMInstance, the type !snapshot, etc.)
// the definitions are then used by the js-yaml parser to form the javascript objects out of the yaml using the 'load' method

var noCredentialsTag = new jsyaml.Type('tag:yaml.org,2002:org.cloudml.core.credentials.NoCredentials', {
    kind : 'mapping',
    construct : function () {
        return {};
    },
});
var localRequiredPortInstanceGroupTag = new jsyaml.Type('tag:yaml.org,2002:org.cloudml.core.InternalComponentInstance$LocalRequiredPortInstanceGroup', {
    kind : 'mapping',
    construct : function () {
        return {};
    },
});
var yamlVMInstanceTag = new jsyaml.Type('tag:yaml.org,2002:org.cloudml.core.VMInstance', {
    kind : 'mapping',
    construct : function (data) {
        data["__type"]="VMInstance"; 
        return data;
    },
});
var yamlICInstanceTag = new jsyaml.Type('tag:yaml.org,2002:org.cloudml.core.InternalComponentInstance', {
    kind : 'mapping',
    construct : function (data) {
        data["__type"]="InternalComponentInstance"; 
        return data;
    },
});
var localProvPortInstanceGroupTag = new jsyaml.Type('tag:yaml.org,2002:org.cloudml.core.ComponentInstance$LocalProvidedPortInstanceGroup', {
    kind : 'mapping',
    construct : function () {
        return {};
    },
});
var provPortInstanceGroupTag = new jsyaml.Type('tag:yaml.org,2002:org.cloudml.core.ComponentInstance$LocalProvidedExecutionPlatformInstanceGroup', {
    kind : 'mapping',
    construct : function () {
        return {};
    },
});
var fileCredentialsFullTag = new jsyaml.Type('tag:yaml.org,2002:org.cloudml.core.credentials.FileCredentials', {
    kind : 'mapping',
    construct : function () {
        return {};
    },
});
var fileCredentialsTag = new jsyaml.Type('!FileCredential', {
    kind : 'mapping',
    construct : function () {
        return {};
    },
});

var updatedType = new jsyaml.Type('!updated', {
    kind : 'mapping',
    construct : function (data) {
        data = data || {}; // null safety first
        return data;
    },
});
var snapshotType = new jsyaml.Type('!snapshot', {
    kind : 'mapping',
    construct : function (data) {
        data = data || {}; // null safety first
        return data;
    },
});

// create a jsyaml schema containing the defined tags and types which is then used as a second argument to the 'load' method
var jsyamlSchema = jsyaml.Schema.create([ provPortInstanceGroupTag, fileCredentialsTag, yamlVMInstanceTag, yamlICInstanceTag, updatedType, snapshotType, localProvPortInstanceGroupTag, fileCredentialsFullTag, localRequiredPortInstanceGroupTag, noCredentialsTag ]);

function pushModelToServer(){
    if(currentJSON == null){
        alertMessage("error",'Error pushing model to server - the model is empty.', 20000);
        return;
    }
    if(currentJSON.trim() == ""){
        alertMessage("error",'Error pushing model to server - the model is empty.', 20000);
        return;
    }

    send("!extended { name : LoadDeployment }");
    send("!additional json-string:"+stringifyRoot()); 

    // TODO fix this dirty hack - the model might (and probably should) take more than two seconds
    // to load for the general case (e.g. - remote server for CloudML)
    setTimeout(function(){send("!getSnapshot {path : /}");}, 2000);
    alertMessage("success","Sent model to the CloudML server.", 3000);
}

/***********************************************
* Functions to add CloudML elements in the graph
************************************************/

function loadElementsInArray(array, elements) {
    elements.forEach(
        function (element) {
            array.push(element);
        }
    );
}

function getInternalComponentInstances(depModel) {
    var instancesArray = [];
    if (depModel.internalComponentInstances != null) {
        loadElementsInArray(instancesArray, depModel.internalComponentInstances);
    }
    return instancesArray;
}

function getExternalComponentInstances(depModel) {
    var instancesArray = [];
    if (depModel.vmInstances != null) {
        loadElementsInArray(instancesArray, depModel.vmInstances);
    }
    if (depModel.externalComponentInstances != null) {
        loadElementsInArray(instancesArray, depModel.externalComponentInstances);
    }
    return instancesArray;
}

function generateExecutesLinks(depModel) {
    var links = [];
    depModel.executesInstances.forEach(

        function (d, i) {
            var tempLink = {
                source : null,
                target : null
            };
            tempLink.id = d.name;
            tempLink.source = findSource(d.providedExecutionPlatformInstance, depModel);
            tempLink.left = false;
            tempLink.target = findTarget(d.requiredExecutionPlatformInstance, depModel);
            tempLink.right = true;
            if (tempLink.source != null && tempLink.target != null) {
                links.push(tempLink);
            }
        });
    return links;
}

function generateRelationshipLinks(depModel) {
    var links = [];
    depModel.relationshipInstances.forEach(

        function (d, i) {
            var tempLink = {
                source : null,
                target : null
            };
            tempLink.id = d.name;
            tempLink.source = findSource(d.providedPortInstance, depModel);
            tempLink.target = findTarget(d.requiredPortInstance, depModel);
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

function findSource(refSource, depModel) {
    var temp = refSource.split("/");
    return getJSON(depModel, "/" + temp[0]);
}

function findTarget(refTarget, depModel) {
    var temp = refTarget.split("/");
    return getJSON(depModel, "/" + temp[0]);
}

function addContextMenuItems(contextMenuElement){
    addExecuteContextMenuItems(contextMenuElement);
    addRelationshipContextMenuItems(contextMenuElement);
    addExtCompContextMenuItems(contextMenuElement);
    addIntCompContextMenuItems(contextMenuElement);
}

function addAnyNodeContextMenuItems(contextMenuElement){
    contextMenuElement.append('li')
    .attr('class', 'executeInstanceMI')
    .attr('id', 'executeInstanceMI1')
    .text('Fold')
    ;
}

function getInstanceType(instanceName){
    if(typeof instanceName == 'undefined' || instanceName == null)
        return null;
    for(i=0;i<intCompInstances.length;++i){
        if(intCompInstances[i].name == instanceName){
            return "internalComponentInstances";
        }
    }

    for(i=0;i<extCompInstances.length;++i){
        if(extCompInstances[i].name == instanceName){
            if(extCompInstances[i].name.indexOf("vms") >=0)
                return "vmInstances";
            else
                return "externalComponentInstances"; // should not ever happen since we don't differentiate right now
        }
    }
}

/***********************************************
* Initialisation and updating of the graph
************************************************/

// set a value in an input JSON object according to a property path (calls traverseAndSet after initial validation as in jsoninpointer+path.js)
function setValueInJSON(jsonObject, propertyPath, newValue) {
    propertyPath = validate_input(jsonObject, propertyPath);
    if (propertyPath.length === 0) {
        throw("Invalid JSON pointer for set.")
    }
    return traverseAndSet(jsonObject, propertyPath, newValue);
}

// traverse an input JSON object according to a property path and change its value (slightly modified copy of the traverse2 function in jsoninpointer+path.js)
function traverseAndSet(jsonObject, propertyPath, newValue) {
    var propertySubPath = propertyPath.shift();
    var subPathSplit = propertySubPath.split("[");

    //filtering
    if (propertySubPath.indexOf("[") >= 0) {
        // we have an XPath expression with a predicate for an attribute (i.e. expression similar to: "/containmentCollection[attribute='attributeValue']"; there could be additional levels of depth)

        var containmentCollection = subPathSplit[0];
        if (!jsonObject.hasOwnProperty(containmentCollection)) {
            console.log("Containment collection not found: ", jsonObject, propertyPath, newValue);
            return null;
        }
        var predicateAttributeValueSplit = subPathSplit[1].split("=");

        // the attribute from the predicate that we check (usually would be the 'name' of the node)
        var attribute = predicateAttributeValueSplit[0];
        var attrValueExpressionSplit = predicateAttributeValueSplit[1].split("]");
        var attributeValueSplit = attrValueExpressionSplit[0].split("'");

        // the attribute value from the predicate we check against to find the node
        var attributeValue = attributeValueSplit[1];

        for (var i = 0; i < jsonObject[containmentCollection].length; i++) {
            // check if the current element of the node containment group even has the attribute
            if (jsonObject[containmentCollection][i].hasOwnProperty(attribute)) {
                // check if the current element of the node containment group's attribute value is the same as in the predicate expression ([attribute='attributeValue'])
                if (jsonObject[containmentCollection][i][attribute] == attributeValue) {
                    // we have found the exact node we need to set the new value to
                    return traverseAndSet(jsonObject[containmentCollection][i], propertyPath, newValue);
                }
            }
        }
        // if we don't find a matching node, we return null
        return null;

    }else{
        // we have a simple XPath expression without a predicate (i.e. expression of the kind: "attribute")

        // check if the JSON object contains this attribute at all
        if (!jsonObject.hasOwnProperty(propertySubPath)) {
            return null;
        }
    }

    if (propertyPath.length !== 0) { // keep traversin!
        return traverseAndSet(jsonObject[propertySubPath], propertyPath, newValue);
    }

    // in case we call this method with 2 attributes
    if (typeof newValue === "undefined") {
        // just reading
        return jsonObject[propertySubPath];
    }

    // set new value, return old value
    var old_value = jsonObject[propertySubPath];

    // when called with a 'null' as the newValue - we want to delete the attribute
    if (newValue === null) {
        // we delete the attribute
        delete jsonObject[propertySubPath];
    } else {
        // we change the attribute value
        jsonObject[propertySubPath] = newValue;
    }
    // ... and return the old value
    return old_value;
}

// update the JSON associated with the current graph view
function graphViewUpdateJSON(parent, propertyId, newValue){

    /* 
Since the json serialization of the metamodel differs from the internal POJO serialization the path to a component instance is not directly resolvable in the json (it could be e.g. /componentInstances[name='sensapp-sl1'] which can be found with our current implementation of json traversal by the expression '/vmInstances[name='sensapp-sl1'])'. But there is no way to differentiate the particular component instance type (vm-, internal-, or external component instance). Therefore we need to determine that by sending a request to the CloudML server for the full information for the internal component instance (which also contains its type). The following code does just that.
    */
    var updateSocket = new WebSocket(socket.url);
    var message = "!getSnapshot"
    + '\n' 
    + "  path : " + parent;
    sendMessageFromSocket(updateSocket,message);
    updateSocket.onerror = function(error){
        console.log(error);
        alertMessage("error",'Error connecting to CloudML server: ' + updateSocket.readyState, 5000);  
    }

    // 
    updateSocket.onmessage = function(msg){
        if(msg.data.indexOf("GetSnapshot") >= 0){
            var array=msg.data.split("###");
            var jObj;
            try{
                var jObj = jsyaml.load(array[2], { schema: jsyamlSchema });
            } catch (error) {
                console.log(msg);
                console.log(error);
                alertMessage("error",'Error parsing the YAML response from the CloudML server: ', 5000);  
            }
            if(jObj.content != 'undefined'){
                // in the js-yaml parser definitions (the global variables), we define a temporary variable '__type' 
                if(jObj.content.__type != 'undefined'){
                    switch (jObj.content.__type){
                            // for now, only state changes in VM instances and internal component instances will be supported (no external component instances like e.g. PaaS services)
                        case 'VMInstance' : {
                            if(parent.indexOf("componentInstances") >=0){
                                parent = parent.replace("componentInstances", "vmInstances");
                                var xpath="";
                                if(parent.length <= 1){
                                    xpath = parent + propertyId;
                                }else{
                                    xpath = parent + "/" + propertyId;

                                }
                                // set the according value to the JSON
                                setValueInJSON(root, xpath, newValue);
                                // update the global object holding the current JSON
                                currentJSON = stringifyRoot();
                            }
                            update();
                        };
                            break;
                        case 'InternalComponentInstance' : {
                            if(parent.indexOf("componentInstances") >=0){
                                parent = parent.replace("componentInstances", "internalComponentInstances");
                                var xpath="";
                                if(parent.length <= 1){
                                    xpath = parent + propertyId;
                                }else{
                                    xpath = parent + "/" + propertyId;

                                }
                                // set the according value to the JSON
                                setValueInJSON(root, xpath, newValue);
                                // update the global object holding the current JSON
                                currentJSON = stringifyRoot();
                            }
                            update();
                        };
                            break;
                        default: // throw error 
                            console.log(jObj);
                            console.log('ERROR', 'Update received from an unrecognized type of component:' + jObj.content.__type);
                            break;
                    }
                    updateSocket.close();
                    updateSocket = null;
                }
            }
        }
    }
}

// set the value of a property object of a CloudML element; returns true if successful
function setOrCreatePropValOfCloudMLElement(element, propertyName, newValue){
    try {
        for(i=0;i<element.properties.length;++i){
            if(element.properties[i].name == propertyName){
                element.properties[i].value = newValue;
                return true;
            }
        }
        // if we cannot find it we create it
        element.properties.push(
            {
                "eClass" :  "net.cloudml.core:Property",
                'name'   :  propertyName,
                'value'  :  newValue
            }
        );
        return true;
    } catch (error){
        console.log("Error:", "Could not set property", propertyName ,"for element", element, "!");
        return false;
    }

}

// get the value of a property object of a CloudML element; returns null if not found
function getPropValFromCloudMLElement(element, propertyName){
    try {
        for(i=0;i<element.properties.length;++i){
            if(element.properties[i].name == propertyName){
                return element.properties[i].value;
            }
        }
    } catch (error){
        console.log("Error:", "Could not get property value of property", propertyName ,"for element", element, "!");
        return null;
    }
    return null;
}

// create the JSON string for the root object while removing circular references and unnecessary data 
function stringifyRoot(){
    var cache=[];

    var result = JSON.stringify(root, function(key, value){
        if (typeof value === 'object' && value !== null) {
            if (cache.indexOf(value) !== -1) {
                return;
            }
            cache.push(value);
        }
        // the added attributes used by internal logic or d3.js
        if(key == "x" || key ==  "y" || key ==  "dx" || key ==  "dy" || key == "fixed" 
           || key == "px" || key == "py" || key == "cx" || key == "cy" || key == "index" 
           || key == "weight" || key == "_type" || key == "_source" || key == "_target"
           || key == "foldedSubNodes" || key == "foldedSubEdges" || key == "outgoingLinks"
           || key == "incomingLinks" || key == "isFolded" || key == "socket" || key == "__type" 
           || key == "id" || key == "status" /* status and id-s are assigned by graphview on demand */){
            return;
        }
        return value;
    });

    return result;
}

// initialise the graph
function getData(inputJSONString) {
    // remove the svg and the selection area (brush)
    d3.selectAll("svg").remove();
    d3.selectAll(".popover").remove();
    brush = null;
    root = eval('(' + inputJSONString + ')');
    currentJSON = inputJSONString;

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
    .on('click', function(){
        d3.select('#context_menu').remove();
        if($(d3.event.target)[0] == $(this)[0]){
            var nodes= $('.singleNode');
            for(i=0;i<nodes.length;++i){
                if($(nodes[i]).data("bs.popover").tip().hasClass("in")){
                    $(nodes[i]).popover('toggle');
                }
            }
        }
        d3.event.stopPropagation();
    })
    .on('contextmenu', function(){
        d3.event.preventDefault();
    })
    ;

    intCompInstances.concat(extCompInstances).forEach(function (d, i) {
        d.x = width / 2 + i;
        d.y = height / 2 + 100 * d.depth;
    });

    root.fixed = true;
    root.x = width / 2;
    root.y = height / 2;

    // create the arrow markers and gradients for the graph circles and arrows
    createSVGDefs();

    var layoutIntCompInstances = intCompInstances;
    for(var i=0;i<layoutIntCompInstances.length;i++){
        layoutIntCompInstances[i].status = 'UNCATEGORIZED';
        layoutIntCompInstances[i].isFolded = false;
        layoutIntCompInstances[i].foldedSubNodes = [];
        layoutIntCompInstances[i].foldedSubEdges = [];
        layoutIntCompInstances[i]._type = "InternalComponent";
    }

    var layoutExtCompInstances = extCompInstances;

    for(i=0;i<layoutExtCompInstances.length;i++){
        layoutExtCompInstances[i].status = 'UNCATEGORIZED';
        // We are assuming that each external component instance has these properties.
        // We use it afterwards to form the content of the popovers.
        layoutExtCompInstances[i].properties = [
            {
                "eClass" :  "net.cloudml.core:Property",
                'name'   :  'cpu',
                'value'  :  0
            }
        ];
        layoutExtCompInstances[i].isFolded = false;
        layoutExtCompInstances[i].foldedSubNodes = [];
        layoutExtCompInstances[i].foldedSubEdges = [];
        layoutExtCompInstances[i]._type = "ExternalComponent";
        layoutExtCompInstances[i].publicAddress = "no address given.";
    }

    /*
     For each of the graph node we define an observer for the attributes
     'properties' and 'state' so that any changes to their values will affect the associated
     popover content
    */
    graphNodes = layoutIntCompInstances.concat(layoutExtCompInstances);

    graphNodes.forEach(function(d){
        // POPOVERS!!: Watch the values for the following properties for change, when one occurs - trigger a refresh of the popover/tooltip
        watch(d, ['properties', 'status', 'publicAddress'], function(){
            // select the according svg 'g' element for the node
            var svgNodeElement = svg.selectAll(".singleNode").filter(
                function() {
                    return this.id == d.name;
                }
            );
            // change the data content (input for the popover)
            svgNodeElement.attr("data-content", function(d){
                return getNodePopover(d);
            });
            // update the status circle coloring
            decorateNodeCircle(svgNodeElement.select(".internalComponent, .externalComponent")[0][0]);
            for(i=0; i<graphEdges.length;++i){
                if(graphEdges[i].source == d){
                    var svgEdgeElement = svg.selectAll(".executionBinding, .relationshipBinding").filter(
                        function() {
                            return this.id == graphEdges[i].id;
                        }
                    );
                    decorateEdgePath(svgEdgeElement[0][0]);
                }
            }
        });
    });

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

// create the svg 'def' entries used to decorate arrows and nodes
function createSVGDefs(){

    var svgDefs = svg.append('svg:defs');

    // define arrow markers for graph links
    svgDefs.append('svg:marker')
    .attr('id', 'pendingExecuteArrow')
    .attr('viewBox', '0 -5 10 10')
    .attr('refX', 6)
    .attr('markerWidth', 4)
    .attr('markerHeight', 4)
    .attr('orient', 'auto')
    .append('svg:path')
    .attr('d', 'M0,-5L10,0L0,5')
    .attr('class','executionArrow')
    .attr('fill', stateColorMap['PENDING']);

    svgDefs.append('svg:marker')
    .attr('id', 'terminatedExecuteArrow')
    .attr('viewBox', '0 -5 10 10')
    .attr('refX', 6)
    .attr('markerWidth', 4)
    .attr('markerHeight', 4)
    .attr('orient', 'auto')
    .append('svg:path')
    .attr('d', 'M0,-5L10,0L0,5')
    .attr('class','executionArrow')
    .attr('fill', stateColorMap['TERMINATED']);

    svgDefs.append('svg:marker')
    .attr('id', 'stoppedExecuteArrow')
    .attr('viewBox', '0 -5 10 10')
    .attr('refX', 6)
    .attr('markerWidth', 4)
    .attr('markerHeight', 4)
    .attr('orient', 'auto')
    .append('svg:path')
    .attr('d', 'M0,-5L10,0L0,5')
    .attr('class','executionArrow')
    .attr('fill', stateColorMap['STOPPED']);

    svgDefs.append('svg:marker')
    .attr('id', 'runningExecuteArrow')
    .attr('viewBox', '0 -5 10 10')
    .attr('refX', 6)
    .attr('markerWidth', 4)
    .attr('markerHeight', 4)
    .attr('orient', 'auto')
    .append('svg:path')
    .attr('d', 'M0,-5L10,0L0,5')
    .attr('class','executionArrow')
    .attr('fill', stateColorMap['RUNNING']);

    svgDefs.append('svg:marker')
    .attr('id', 'errorExecuteArrow')
    .attr('viewBox', '0 -5 10 10')
    .attr('refX', 6)
    .attr('markerWidth', 4)
    .attr('markerHeight', 4)
    .attr('orient', 'auto')
    .append('svg:path')
    .attr('d', 'M0,-5L10,0L0,5')
    .attr('class','executionArrow')
    .attr('fill', stateColorMap['ERROR']);

    svgDefs.append('svg:marker')
    .attr('id', 'uncategorizedExecuteArrow')
    .attr('viewBox', '0 -5 10 10')
    .attr('refX', 6)
    .attr('markerWidth', 4)
    .attr('markerHeight', 4)
    .attr('orient', 'auto')
    .append('svg:path')
    .attr('d', 'M0,-5L10,0L0,5')
    .attr('class','executionArrow')
    .attr('fill', stateColorMap['UNCATEGORIZED']);

    svgDefs.append('svg:marker')
    .attr('id', 'recoveryExecuteArrow')
    .attr('viewBox', '0 -5 10 10')
    .attr('refX', 6)
    .attr('markerWidth', 4)
    .attr('markerHeight', 4)
    .attr('orient', 'auto')
    .append('svg:path')
    .attr('d', 'M0,-5L10,0L0,5')
    .attr('class','executionArrow')
    .attr('fill', stateColorMap['RECOVERY']);

    svgDefs.append('svg:marker')
    .attr('id', 'terminatedRelationshipArrow')
    .attr('viewBox', '0 -5 10 10')
    .attr('refX', 6)
    .attr('markerWidth', 4)
    .attr('markerHeight', 4)
    .attr('orient', 'auto')
    .append('svg:path')
    .attr('d', 'M0,-5L10,0L0,5')
    .attr('class','relationshipArrow')
    .attr('fill', stateColorMap['TERMINATED']);

    svgDefs.append('svg:marker')
    .attr('id', 'stoppedRelationshipArrow')
    .attr('viewBox', '0 -5 10 10')
    .attr('refX', 6)
    .attr('markerWidth', 4)
    .attr('markerHeight', 4)
    .attr('orient', 'auto')
    .append('svg:path')
    .attr('d', 'M0,-5L10,0L0,5')
    .attr('class','relationshipArrow')
    .attr('fill', stateColorMap['STOPPED']);

    svgDefs.append('svg:marker')
    .attr('id', 'runningRelationshipArrow')
    .attr('viewBox', '0 -5 10 10')
    .attr('refX', 6)
    .attr('markerWidth', 4)
    .attr('markerHeight', 4)
    .attr('orient', 'auto')
    .append('svg:path')
    .attr('d', 'M0,-5L10,0L0,5')
    .attr('class','relationshipArrow')
    .attr('fill', stateColorMap['RUNNING']);

    svgDefs.append('svg:marker')
    .attr('id', 'errorRelationshipArrow')
    .attr('viewBox', '0 -5 10 10')
    .attr('refX', 6)
    .attr('markerWidth', 4)
    .attr('markerHeight', 4)
    .attr('orient', 'auto')
    .append('svg:path')
    .attr('d', 'M0,-5L10,0L0,5')
    .attr('class','relationshipArrow')
    .attr('fill', stateColorMap['ERROR']);

    svgDefs.append('svg:marker')
    .attr('id', 'uncategorizedRelationshipArrow')
    .attr('viewBox', '0 -5 10 10')
    .attr('refX', 6)
    .attr('markerWidth', 4)
    .attr('markerHeight', 4)
    .attr('orient', 'auto')
    .append('svg:path')
    .attr('d', 'M0,-5L10,0L0,5')
    .attr('class','relationshipArrow')
    .attr('fill', stateColorMap['UNCATEGORIZED']);

    svgDefs.append('svg:marker')
    .attr('id', 'recoveryRelationshipArrow')
    .attr('viewBox', '0 -5 10 10')
    .attr('refX', 6)
    .attr('markerWidth', 4)
    .attr('markerHeight', 4)
    .attr('orient', 'auto')
    .append('svg:path')
    .attr('d', 'M0,-5L10,0L0,5')
    .attr('class','relationshipArrow')
    .attr('fill', stateColorMap['RECOVERY']);

    svgDefs.append('svg:marker')
    .attr('id', 'pendingRelationshipArrow')
    .attr('viewBox', '0 -5 10 10')
    .attr('refX', 6)
    .attr('markerWidth', 4)
    .attr('markerHeight', 4)
    .attr('orient', 'auto')
    .append('svg:path')
    .attr('d', 'M0,-5L10,0L0,5')
    .attr('class','relationshipArrow')
    .attr('fill', stateColorMap['PENDING']);

    // define gradients
    var pendingNodeGradient = svgDefs
    .append("svg:linearGradient")
    .attr("id", "pendingNodeGradient")
    .attr("x1", "0%")
    .attr("y1", "100%")
    .attr("x2", "95%")
    .attr("y2", "5%")
    .attr("spreadMethod", "pad");

    pendingNodeGradient.append("svg:stop")
    .attr("offset", "0%")
    .attr("stop-color", stateColorMap['PENDING'])
    .attr("stop-opacity", 1);

    pendingNodeGradient.append("svg:stop")
    .attr("offset", "100%")
    .attr("stop-color", stateColorMap['PENDING'])
    .attr("stop-opacity", 0.2);

    var terminatedNodeGradient = svgDefs
    .append("svg:linearGradient")
    .attr("id", "terminatedNodeGradient")
    .attr("x1", "0%")
    .attr("y1", "100%")
    .attr("x2", "95%")
    .attr("y2", "5%")
    .attr("spreadMethod", "pad");

    terminatedNodeGradient.append("svg:stop")
    .attr("offset", "0%")
    .attr("stop-color", stateColorMap['TERMINATED'])
    .attr("stop-opacity", 1);

    terminatedNodeGradient.append("svg:stop")
    .attr("offset", "100%")
    .attr("stop-color", stateColorMap['TERMINATED'])
    .attr("stop-opacity", 0.2);

    var stoppedNodeGradient = svgDefs
    .append("svg:linearGradient")
    .attr("id", "stoppedNodeGradient")
    .attr("x1", "0%")
    .attr("y1", "100%")
    .attr("x2", "95%")
    .attr("y2", "5%")
    .attr("spreadMethod", "pad");

    stoppedNodeGradient.append("svg:stop")
    .attr("offset", "0%")
    .attr("stop-color", stateColorMap['STOPPED'])
    .attr("stop-opacity", 1);

    stoppedNodeGradient.append("svg:stop")
    .attr("offset", "100%")
    .attr("stop-color", stateColorMap['STOPPED'])
    .attr("stop-opacity", 0.2);

    var runningNodeGradient = svgDefs
    .append("svg:linearGradient")
    .attr("id", "runningNodeGradient")
    .attr("x1", "0%")
    .attr("y1", "100%")
    .attr("x2", "95%")
    .attr("y2", "5%")
    .attr("spreadMethod", "pad");

    runningNodeGradient.append("svg:stop")
    .attr("offset", "0%")
    .attr("stop-color", stateColorMap['RUNNING'])
    .attr("stop-opacity", 1);

    runningNodeGradient.append("svg:stop")
    .attr("offset", "100%")
    .attr("stop-color", stateColorMap['RUNNING'])
    .attr("stop-opacity", 0.2);

    var uncategorizedNodeGradient = svgDefs
    .append("svg:linearGradient")
    .attr("id", "uncategorizedNodeGradient")
    .attr("x1", "0%")
    .attr("y1", "100%")
    .attr("x2", "95%")
    .attr("y2", "5%")
    .attr("spreadMethod", "pad");

    uncategorizedNodeGradient.append("svg:stop")
    .attr("offset", "0%")
    .attr("stop-color", stateColorMap['UNCATEGORIZED'])
    .attr("stop-opacity", 1);

    uncategorizedNodeGradient.append("svg:stop")
    .attr("offset", "100%")
    .attr("stop-color", stateColorMap['UNCATEGORIZED'])
    .attr("stop-opacity", 0.2);

    var recoveryNodeGradient = svgDefs
    .append("svg:linearGradient")
    .attr("id", "recoveryNodeGradient")
    .attr("x1", "0%")
    .attr("y1", "100%")
    .attr("x2", "95%")
    .attr("y2", "5%")
    .attr("spreadMethod", "pad");

    recoveryNodeGradient.append("svg:stop")
    .attr("offset", "0%")
    .attr("stop-color", stateColorMap['RECOVERY'])
    .attr("stop-opacity", 1);

    recoveryNodeGradient.append("svg:stop")
    .attr("offset", "100%")
    .attr("stop-color", stateColorMap['RECOVERY'])
    .attr("stop-opacity", 0.2);

    var errorNodeGradient = svgDefs
    .append("svg:linearGradient")
    .attr("id", "errorNodeGradient")
    .attr("x1", "0%")
    .attr("y1", "100%")
    .attr("x2", "95%")
    .attr("y2", "5%")
    .attr("spreadMethod", "pad");

    errorNodeGradient.append("svg:stop")
    .attr("offset", "0%")
    .attr("stop-color", stateColorMap['ERROR'])
    .attr("stop-opacity", 1);

    errorNodeGradient.append("svg:stop")
    .attr("offset", "100%")
    .attr("stop-color", stateColorMap['ERROR'])
    .attr("stop-opacity", 0.2);

}

// send an input text message from an input socket
function sendMessageFromSocket(aSocket, text){

    try{ 
        if(aSocket.readyState !=1 ){
            setTimeout(function(){sendMessageFromSocket(aSocket,text)},1000);
        }else{
            aSocket.send(text);
        }
    } catch(exception){  
        alertMessage("error",'Unable to send message: ' + exception , 10000);  
    }  
}

// get the popover content for a node
function getNodePopover(node){
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

    if(typeof node.status != "undefined"){
        result += '<br/>';
        result += 'status: ';
        result += node.status;
    }
    if(typeof node.publicAddress != "undefined"){
        result += '<br/>';
        result += 'public address: ';
        result += node.publicAddress;
    }
    if(typeof node.properties != "undefined"){
        var cpuLoad = getPropValFromCloudMLElement(node,"cpu");
        // TODO add this back when necessary
        //        if(cpuLoad != null){
        //            result += '<br/>';
        //            result += 'cpu load (%): ';
        //            result += cpuLoad;
        //        }
    }
    return result;
}

// update the graph layout
function update(){
    allNodesGroup = svg.select('.nodes');
    
    // we define a socket connection for each of the graph nodes that connects to the server to retrieve state information
    // need to have this check on each update in case there are new nodes or we just connected to the CloudML server
    if(connectedToCloudMLServer){
        graphNodes.forEach(function (d){
            // if node already has a socket, skip
            if(!d.socket){
                // we only create a socket in case we are using the socket interface (i.e. the graph is connected to a CloudML server)

                d.socket = new WebSocket(cloudMLServerHost);
                d.socket.onopen = function(){
                    var message = 
                        "!getSnapshot"
                    + '\n' 
                    + "  path : /componentInstances[name='" + d.name + "']";
                    sendMessageFromSocket(d.socket, message);
                }
                d.socket.onmessage = function(msg){
                    if(msg.data.indexOf("GetSnapshot") >= 0){
                        try{
                            var json=jsyaml.load(msg.data, {schema : jsyamlSchema});
                        }catch (error) {
                            console.log(error);
                        }
                        if(typeof json.content.status != 'undefined'){
                            if(json.content.status != null)
                                d.status = json.content.status;
                        }

                        if(typeof json.content.properties != 'undefined'){
                            var cpuLoad = getPropValFromCloudMLElement(json.content, "cpu");
                            if(cpuLoad != null) {
                                setOrCreatePropValOfCloudMLElement(d, "cpu", cpuLoad);
                            }
                        }
                        if(typeof json.content.id != 'undefined'){
                            if(json.content.id != null)
                                d.id = json.content.id;
                        }
                        if(typeof json.content.publicAddress != 'undefined'){
                            if(json.content.publicAddress != null)
                                d.publicAddress = json.content.publicAddress;
                        }
                    }
                }
                d.socket.onclose = function(){
                    console.log("socket closed for " + d.name);
                }
                d.socket.onerror = function(error){
                    console.log("error for node " + d.name + d.socket.readyState);
                }

            }
        });
    };
    
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
        'data-content'    :   function(d){ return getNodePopover(d) }
    })
    .each(function (d){
        // define a popover for each of the elements
        $(this).popover(
            {
                'container' : 'body',
                'placement' : 'auto right',
                'title'     : d.name,
                // template for the popover includes the identifier of each of the elements
                'template'  : '<div class="popover" role="tooltip"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content" id="popover_content_' + d.name + '"></div></div>',
                'html'      : true,
                'trigger'   : 'manual'
            }
        )
        // listeners to allow to change styles of the elements for some basic animation
        .on("mouseover", function () {
            d3.select(this).selectAll('circle').classed("hover", true);
        })
        .on("mouseout", function () {
            d3.select(this).selectAll('circle').classed("hover", false);
        })
        .popover();
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

    .on('contextmenu', function(node) {

        generateContextMenuForNode(this, node);
        d3.event.preventDefault();
    })
    ;

    // definition of the node selection area
    if(!brush){
        // put brush area in the background so that the nodes and edges are on top of (and not inside) it
        brush = svg.insert("g", "g.nodes")
        .datum(function() { 
            return {selected: false, previouslySelected: false}; 
        })
        .attr("class", "brush")
        .call(d3.svg.brush()
              .x(d3.scale.identity().domain([0, width]))
              .y(d3.scale.identity().domain([0, height]))
              .on("brushstart", function(){

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
                    if(d3.event.sourceEvent.shiftKey){
                        return this.classList.contains("selected") || 
                            (extent[0][0] <= d.x && d.x < extent[1][0]
                             && extent[0][1] <= d.y && d.y < extent[1][1]);
                    }else{
                        return (extent[0][0] <= d.x && d.x < extent[1][0]
                                && extent[0][1] <= d.y && d.y < extent[1][1]);
                    }
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
        'r'     : 25.5
    })
    .each(function(d){decorateNodeCircle(this)})
    .call(force.drag);

    // outer circle used to depict a selected node
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
    // add listener for mouseclick for either adding to a selection or toggling the popovers (tooltips) for each element
    nodeGroup.on("click", function(d){
        var e = d3.event;
        // toggle selection of a node if shift/ctrl key is pressed
        if(e.shiftKey || e.ctrlKey){
            d3.select(this).selectAll('.selectionCircle')
            .classed("selected", function(d){
                return this.classList.contains('selected') ? false : true ;
            });
        }else{
            $(this).popover("toggle");
            clearCurrentSelection();
        }
    });
    // add listener for double clicks that folds a node
    nodeGroup.on("dblclick", function(d){
        toggleFoldNode(d);
    });

    var path = svg.select(".edges").selectAll('path');
    path = path.data(graphEdges);

    path.exit().remove();

    path.enter().append('svg:path')
    .attr('class', function (d, i) {
        return d._type == "ExecuteLink" ? "executionBinding" : "relationshipBinding";
    })
    .attr("id", function(d) {
        return d.id;
    })
    .each(function(d){decorateEdgePath(this)})
    .style('marker-start', '')
    /*.style('marker-end', function(d) {
        return d._type == "ExecuteLink" ? 'url(#pendingExecuteArrow)' : 'url(#pendingRelationshipArrow)';
    })*/;

    path.on('click', function(){
        d3.select('#context_menu').remove();
        d3.event.stopPropagation();
    })
    .on('contextmenu', function(){
        d3.event.preventDefault();
    });


    d3.selectAll("text")
    .on("mouseover", function () {
        d3.event.preventDefault();
    });

    force.on("tick", tick);
}

// update colors of a node circle svg element corresponding to a node
function decorateNodeCircle(svgNodeElement){
    var node = d3.select(svgNodeElement).datum();
    // modify the stroke and fill
    d3.select(svgNodeElement).attr(
        {
            'stroke'    : function(node) {
                switch(node.status){
                    case 'PENDING':
                        return stateColorMap['PENDING'];
                    case 'TERMINATED':
                        return stateColorMap['TERMINATED'];
                    case 'STOPPED':
                        return stateColorMap['STOPPED'];
                    case 'RUNNING':
                        return stateColorMap['RUNNING'];
                    case 'ERROR':
                        return stateColorMap['ERROR'];
                    case 'UNCATEGORIZED':
                        return stateColorMap['UNCATEGORIZED'];
                    case 'RECOVERY':
                        return stateColorMap['RECOVERY'];
                    default: 
                        return stateColorMap['UNCATEGORIZED'];
                }
            },
            'fill'  : function(node) {
                switch(node.status){
                    case 'PENDING':
                        return 'url(#pendingNodeGradient)';
                    case 'TERMINATED':
                        return 'url(#terminatedNodeGradient)';
                    case 'STOPPED':
                        return 'url(#stoppedNodeGradient)';
                    case 'RUNNING':
                        return 'url(#runningNodeGradient)';
                    case 'ERROR':
                        return 'url(#errorNodeGradient)';
                    case 'UNCATEGORIZED':
                        return 'url(#uncategorizedNodeGradient)';
                    case 'RECOVERY':
                        return 'url(#recoveryNodeGradient)';
                    default: 
                        node.status = 'UNCATEGORIZED';
                        return 'url(#uncategorizedNodeGradient)';
                }
            }
        });
}

// update colors of an edge path svg element corresponding to an edge
function decorateEdgePath(svgEdgeElement){
    var edge = d3.select(svgEdgeElement).datum();
    if(typeof edge == 'undefined'){
        return;
    }
    // find and modify all children edge elements' color
    d3.select(svgEdgeElement).attr({
        'stroke' : function() {
            switch(edge.source.status){
                case 'PENDING':
                    return stateColorMap['PENDING'];
                case 'TERMINATED':
                    return stateColorMap['TERMINATED'];
                case 'STOPPED':
                    return stateColorMap['STOPPED'];
                case 'RUNNING':
                    return stateColorMap['RUNNING'];
                case 'ERROR':
                    return stateColorMap['ERROR'];
                case 'UNCATEGORIZED':
                    return stateColorMap['UNCATEGORIZED'];
                case 'RECOVERY':
                    return stateColorMap['RECOVERY'];
                default: 
                    return stateColorMap['UNCATEGORIZED'];
            }
        },
        'marker-end': function() {
            switch(edge.source.status){
                case 'PENDING':
                    return edge._type == 'ExecuteLink' ? 'url(#pendingExecuteArrow)' : 'url(#pendingRelationshipArrow)';
                case 'TERMINATED':
                    return edge._type == 'ExecuteLink' ? 'url(#terminatedExecuteArrow)' : 'url(#terminatedRelationshipArrow)';
                case 'STOPPED':
                    return edge._type == 'ExecuteLink' ? 'url(#stoppedExecuteArrow)' : 'url(#stoppedRelationshipArrow)';
                case 'RUNNING':
                    return edge._type == 'ExecuteLink' ? 'url(#runningExecuteArrow)' : 'url(#runningRelationshipArrow)';
                case 'ERROR':
                    return edge._type == 'ExecuteLink' ? 'url(#errorExecuteArrow)' : 'url(#errorRelationshipArrow)';
                case 'UNCATEGORIZED':
                    return edge._type == 'ExecuteLink' ? 'url(#uncategorizedExecuteArrow)' : 'url(#uncategorizedRelationshipArrow)';
                case 'RECOVERY':
                    return edge._type == 'ExecuteLink' ? 'url(#recoveryExecuteArrow)' : 'url(#recoveryRelationshipArrow)';
                default: 
                    return edge._type == 'ExecuteLink' ? 'url(#uncategorizedExecuteArrow)' : 'url(#uncategorizedRelationshipArrow)';
            }
        }});
}

// de-select the currently selected set of nodes
function clearCurrentSelection(){
    d3.selectAll('.selected').classed('selected', false);
}

// recalculate svg element positions on every tick
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

// fold or unfold the according svg elements based on an input node 
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
    // update the popover for the SVG element
    svgNodeElement.attr("data-content", function(d){
        return getNodePopover(nodeToToggle)
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

// computes an object containing arrays with the nodes and edges to fold additionally if the input node is the one being folded
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

// find the host VM (or other external component) for an input node
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

// hide an input set of nodes from the graph (used when folding)
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

// hide an input set of edges from the graph (used when folding)
function hideEdges(edgesToFold){
    var currentEdgeIndex;
    var currentEdge;
    for(i=0;i<edgesToFold.length;++i){
        currentEdgeIndex=graphEdges.indexOf(edgesToFold[i]);
        currentEdge = graphEdges[currentEdgeIndex];

        // remove edge from the graph data
        var svgEdgeElement = svg.selectAll(".executionBinding, .relationshipBinding").filter(
            function() {
                return this.id == currentEdge.id;
            });

        svgEdgeElement.remove();
        graphEdges.splice(currentEdgeIndex,1);
    }
}

// reset the entire graph
function reset(){
    svg.remove();
    brush = null;
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

/***********************************************
* Context menu related functions
************************************************/

// generate the context menu and specific items associated with an input node and its according svg element
function generateContextMenuForNode(svgNodeElement, node){
    // if any popover is open for the current element - hide it
    $(svgNodeElement).popover('hide');

    // if there is another context menu open - remove it too
    d3.select('#context_menu').remove();

    var mousePosition = d3.mouse(svgNodeElement.parentNode);

    var contextMenu = d3.select('body').append('ul')
    .attr('id', 'context_menu')
    .attr('class', 'contextMenu')
    .style('left', mousePosition[0] + "px")
    .style('top', mousePosition[1] + "px")
    .on('mouseleave', function() {
        d3.select('#context_menu').remove();
    })
    ;

    addContextOptionsAllNodes(contextMenu, svgNodeElement, node);
    addContextOptionsExternalComponents(contextMenu, svgNodeElement, node);



}

// generate the list items (<li>) for the context menu options of all of the nodes
function addContextOptionsAllNodes(contextMenu, element, node){

    // context menu item for Fold/Unfold node
    contextMenu.append('li')
    .attr('class', 'contextOption')
    .attr('id', function(){
        node.isFolded ? 'unfoldNodeCMI' : 'foldNodeCMI';
    })
    .style('cursor', 'pointer')
    .on('click', function(d){
        toggleFoldNode(node);
        clearCurrentSelection();
        // if the click of the context menu occurs over a node this will prevent it from bein folded/unfolded
        d3.event.stopPropagation();
        d3.select('#context_menu').remove();
    })
    .text(function(){
        return node.isFolded ? 'Unfold node' : 'Fold node';
    });


    contextMenu.append('hr');


    contextMenu.append('li')
    .attr('class', 'contextOption')
    .attr('id', 'foldSelectionCMI')
    .style('cursor', 'pointer')
    .on('click', function(d){
        foldSelectionOfNodes(d3.selectAll('.selected'));
        clearCurrentSelection();
        // if the click of the context menu occurs over a node this will prevent it from bein folded/unfolded
        d3.event.stopPropagation();
        d3.select('#context_menu').remove();
    })
    .text('Fold selection');



    contextMenu.append('li')
    .attr('class', 'contextOption')
    .attr('id', 'unfoldSelectionCMI')
    .style('cursor', 'pointer')
    .on('click', function(d){
        unfoldSelectionOfNodes(d3.selectAll('.selected'));
        clearCurrentSelection();
        // if the click of the context menu occurs over a node this will prevent it from bein folded/unfolded
        d3.event.stopPropagation();
        d3.select('#context_menu').remove();
    })
    .text('Unfold selection');
}

// generate the list items (<li>) for the context menu options of the external components
function addContextOptionsExternalComponents(contextMenu, element, node){
    if(node._type == 'ExternalComponent'){
        contextMenu.append('hr');
        contextMenu.append('li')
        .attr('class', 'contextOption')
        .attr('id', 'scaleOutNode')
        .style('cursor', 'pointer')
        .on('click', function(d){
            scaleOutNode(node);
            // if the click of the context menu occurs over a node this will prevent it from bein folded/unfolded
            d3.event.stopPropagation();
            d3.select('#context_menu').remove();
        })
        .text('Scale out');
    }
}

// unfold the current selection of nodes
function unfoldSelectionOfNodes(nodesSelection){
    if(nodesSelection[0].length == 0){
        return;
    }
    var selectedNodesDatums = [];
    for(i=0;i < nodesSelection[0].length;i++){
        // select the data associated with the 'g' node of the selection
        var nodeDatum = d3.select(nodesSelection[0][i].parentNode).datum();
        if(nodeDatum.isFolded){
            // unfold the node
            unfoldNode(nodeDatum);
            // find the SVG element that corresponds to the toggled node
            var svgNodeElement = svg.selectAll(".singleNode").filter(
                function() {
                    return this.id == nodeDatum.name;
                }
            );
            // update the popover for the SVG element
            svgNodeElement.attr("data-content", function(d){
                return getNodePopover(nodeDatum)
            });
        }


    }

    // refresh the force layout
    force.start();
    update();
}

// folds the current selection of nodes
function foldSelectionOfNodes(nodesSelection){
    if(nodesSelection[0].length ==0){
        return;
    }
    var selectedNodesDatums = [];
    for(i=0;i<nodesSelection[0].length;++i){
        var nodeDatum = d3.select(nodesSelection[0][i].parentNode).datum();
        selectedNodesDatums.push(nodeDatum);
    }

    while(selectedNodesDatums.length>0){
        var currentDatum = selectedNodesDatums.pop();

        if(currentDatum.isFolded)
            continue;

        if(getNodeDatumHostFromSelection(currentDatum, selectedNodesDatums).length > 0){
            // if the host of the node of the datum is in the selection, we will check it anyways
            continue;
        }

        toggleFoldNode(currentDatum);

        var index;
        // remove the already folded sub-nodes as they are no longer bound
        for(i=0;i<currentDatum.foldedSubNodes.length;++i){
            index = selectedNodesDatums.indexOf(currentDatum.foldedSubNodes[i]);
            if(index > -1){
                selectedNodesDatums.splice(index,1);
            }
        }
    }
}

// returns the top level host (if any) of a node from a selection of nodes
function getNodeDatumHostFromSelection(datum, selection){
    // no parent - datum represents a host
    if(datum._type == 'ExternalComponent')
        return [];

    var result = [];
    var edge;
    var edgeSourceHost = findNodeHost(datum);
    // check if the host of the node is in the selection
    if(selection.indexOf(edgeSourceHost) > -1){
        result.push(edgeSourceHost);
    }
    return result;
}

// call the 'scale out' command for an input node
function scaleOutNode(node){
    // get the identifier of the node
    var id = getNodeId(node);
    // value null is returned in case of an error; we don't perform an action in this case
    if(id == null)
        return;
    // form the message to initiate the scale out action
    var message = "!extended { name: ScaleOut, params: [" + id + "] }";
    sendMessageFromSocket(node.socket, message);
    alertMessage("success","Initiated scaling out action for node " + node.name + "!", 5000); 
}

// get the identifier of an input node
function getNodeId(node){
    if(node.socket){
        // send a message that retrieves the current node state (the listener updates the 'id' field)
        // this is in case the ID for the node has been reassigned and the listener did not get notified
        var message = 
            "!getSnapshot"
        + '\n' 
        + "  path : /componentInstances[name='" + node.name + "']";
        sendMessageFromSocket(node.socket, message);
        if(typeof node.id == 'undefined'){
            alertMessage("error",'The selected node does not have an identifier.', 5000);
            return null;
        }
        if(node.id == null){
            alertMessage("error",'The selected node has a null identifier.', 5000);
            return null;
        }
        if(node.id == ''){
            alertMessage("error",'The selected node has an empty identifier.', 5000);
            return null;
        }
        return node.id;
    }
    else{
        alertMessage("error",'Something went wrong during scaling action: No socket associated with the current node.');
        return null;
    }




}

