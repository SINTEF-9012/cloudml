var width = 1024,
    height = 700;

var force = d3.layout.force()
    .charge(-800)
    .linkDistance(150)
    .size([width, height]);

var svg = d3.select("body").append("svg:svg")
    .attr("width", width)
    .attr("height", height);

var root = getData();
var nodes = flatten(root);
var nodes2 = f2(root);
var links = generateLinks(root);
var links2 = generateExecutes(root);

nodes.concat(nodes2).forEach(function (d, i) {
    d.x = width / 2 + i;
    d.y = height / 2 + 100 * d.depth;
});


root.fixed = true;
root.x = width / 2;
root.y = height / 2;

force.nodes(nodes.concat(nodes2))
    .links(links.concat(links2))
    .start();


// define arrow markers for graph links
svg.append('svg:defs').append('svg:marker')
    .attr('id', 'end-arrow')
    .attr('viewBox', '0 -5 10 10')
    .attr('refX', 6)
    .attr('markerWidth', 4)
    .attr('markerHeight', 4)
    .attr('orient', 'auto')
  .append('svg:path')
    .attr('d', 'M0,-5L10,0L0,5')
    .attr('fill', '#000');

svg.append('svg:defs').append('svg:marker')
    .attr('id', 'start-arrow')
    .attr('viewBox', '0 -5 10 10')
    .attr('refX', 4)
    .attr('markerWidth', 4)
    .attr('markerHeight', 4)
    .attr('orient', 'auto')
  .append('svg:path')
    .attr('d', 'M10,-5L0,0L10,5')
    .attr('fill', '#000');

	
var circle = svg.append('svg:g').selectAll('g');
circle = circle.data(nodes.concat(nodes2));
circle.selectAll('circle');

var path = svg.append('svg:g').selectAll('path');
path = path.data(links.concat(links2));

path.enter().append('svg:path')
    .attr('class', function (d, i) {
    return i < links.length ?  "executionBinding" : "link";
})
	.style('marker-start', '')
    .style('marker-end','url(#end-arrow)');

var g = circle.enter()
    .append('svg:g');

var node = g.append("svg:circle")
    .attr("r", 25.5)
    .attr("class", function (d, i) {
    return i < nodes.length ? "node" : "nodeVM";
})
    .call(force.drag);

// show node IDs
g.append('svg:text')
	.attr('text-anchor', 'middle')
    .attr('dominant-baseline', 'central')
    .attr('font-family', 'FontAwesome')
    .attr('font-size', '20px' )
.text(function(d,i){
    return i < nodes.length ? '\uf013' : '\uf0c2';
});

g.append('svg:text')
    .attr('x', '0')
    .attr('y', '30')
	.attr('text-anchor', 'middle')
    .attr('dominant-baseline', 'central')
    .attr('font-family', 'sans-serif')
    .attr('font-size', '10px' )
.text(function(d,i){
    return d.name;
});



force.on("tick", function (e) {

	path.attr('d', function(d) {
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

    circle.attr('transform', function (d) {
        return 'translate(' + d.x + ',' + d.y + ')';
    });

});

function cs(n, deploymentModel) {
    deploymentModel.forEach(

    function (ici) {
        n.push(ici);
    });
}


function flatten(deploymentModel) {
    var nodes = [];
    if (deploymentModel.internalComponentInstances != null) {
        cs(nodes, deploymentModel.internalComponentInstances);
    }
    return nodes;
}

function f2(deploymentModel) {
    var nodes2 = [];
    if (deploymentModel.vmInstances != null) {
        cs(nodes2, deploymentModel.vmInstances);
    }
    if (deploymentModel.externalComponentInstances != null) {
        cs(nodes2, deploymentModel.externalComponentInstances);
    }
    return nodes2;
}

function generateLinks(deploymentModel) {
    var links = [];
    deploymentModel.executesInstances.forEach(

    function (d, i) {
        var tempLink = {
            source: null,
            target: null
        };
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

function generateExecutes(deploymentModel){
	var links = [];
    deploymentModel.relationshipInstances.forEach(

    function (d, i) {
        var tempLink = {
            source: null,
            target: null
        };
        tempLink.source = findSource(d.providedPortInstance, deploymentModel);
        tempLink.target = findTarget(d.requiredPortInstance, deploymentModel);
        if (tempLink.source !== null && tempLink.target !== null) {
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

function getData() {
    return {
        "eClass": "net.cloudml.core:CloudMLModel",
            "name": "SensApp",
            "providers": [{
            "eClass": "net.cloudml.core:Provider",
                "name": "openstack-nova",
                "credentials": "./credentialsOpenstack",
                "properties": [{
                "eClass": "net.cloudml.core:Property",
                    "name": "endPoint",
                    "value": "http://192.168.1.10:5000/v2.0"
            }]
        }],
            "internalComponents": [{
            "eClass": "net.cloudml.core:InternalComponent",
                "name": "zookeeper",
                "resources": [{
                "eClass": "net.cloudml.core:Resource",
                    "name": "no name",
                    "uploadCommand": "./cloudml.pem /home/ubuntu/.ssh/id_rsa",
                    "downloadCommand": "sudo apt-get update; sudo apt-get install -y openjdk-7-jre",
                    "installCommand": "sudo apt-get install -y zookeeper",
                    "startCommand": "nohup sudo /usr/share/zookeeper/bin/zkServer.sh start"
            }],
                "providedPorts": [{
                "eClass": "net.cloudml.core:ProvidedPort",
                    "name": "zookeeper",
                    "isLocal": false,
                    "portNumber": "0",
                    "component": "internalComponents[zookeeper]"
            }],
                "requiredPorts": [{
                "eClass": "net.cloudml.core:ProvidedPort",
                    "name": "requiredZKMaster",
                    "isLocal": false,
                    "isMandatory": false,
                    "portNumber": "0",
                    "component": "internalComponents[zookeeper]"
            }],
                "requiredExecutionPlatform": {
                "eClass": "net.cloudml.core:RequiredExecutionPlatform",
                    "name": "vmRequired",
                    "owner": "internalComponents[zookeeper]"
            }
        }, {
            "eClass": "net.cloudml.core:InternalComponent",
                "name": "storm-supervisor",
                "resources": [{
                "eClass": "net.cloudml.core:Resource",
                    "name": "no name",
                    "downloadCommand": "wget -P ~ http://apache.uib.no/incubator/storm/apache-storm-0.9.2-incubating/apache-storm-0.9.2-incubating.tar.gz;tar xvf apach*;mkdir storm",
                    "configureCommand": "echo 'storm.local.dir: \"/mnt/storm\"' >> ./apache*/conf/storm.yaml",
                    "startCommand": "wget -P ~ http://ferrynico.com/configure_relationships_storm.sh;sudo bash configure_relationships_storm.sh"
            }],
                "providedPorts": [],
                "requiredPorts": [{
                "eClass": "net.cloudml.core:ProvidedPort",
                    "name": "requiredZK",
                    "isLocal": false,
                    "isMandatory": false,
                    "portNumber": "0",
                    "component": "internalComponents[storm-supervisor]"
            }],
                "requiredExecutionPlatform": {
                "eClass": "net.cloudml.core:RequiredExecutionPlatform",
                    "name": "vmRequired",
                    "owner": "internalComponents[storm-supervisor]"
            }
        }, {
            "eClass": "net.cloudml.core:InternalComponent",
                "name": "storm-nimbus",
                "resources": [{
                "eClass": "net.cloudml.core:Resource",
                    "name": "no name",
                    "downloadCommand": "wget -P ~ http://apache.uib.no/incubator/storm/apache-storm-0.9.2-incubating/apache-storm-0.9.2-incubating.tar.gz;tar xvf apach*;mkdir storm",
                    "configureCommand": "echo 'storm.local.dir: \"/mnt/storm\"' >> ./apache*/conf/storm.yaml",
                    "startCommand": "wget -P ~ http://ferrynico.com/configure_relationships_storm_nimbus.sh;sudo bash configure_relationships_storm_nimbus.sh"
            }],
                "providedPorts": [],
                "requiredPorts": [{
                "eClass": "net.cloudml.core:ProvidedPort",
                    "name": "requiredZK",
                    "isLocal": true,
                    "isMandatory": false,
                    "portNumber": "0",
                    "component": "internalComponents[storm-nimbus]"
            }],
                "requiredExecutionPlatform": {
                "eClass": "net.cloudml.core:RequiredExecutionPlatform",
                    "name": "vmRequired",
                    "owner": "internalComponents[storm-nimbus]"
            }
        }],
            "internalComponentInstances": [{
            "eClass": "net.cloudml.core:InternalComponentInstance",
                "name": "zookeeper-worker1",
                "type": "internalComponents[zookeeper]",
                "providedPortInstances": [{
                "eClass": "net.cloudml.core:ProvidedPortInstance",
                    "name": "zookeeper1",
                    "type": "internalComponents[zookeeper]/providedPorts[zookeeper]"
            }],
                "requiredPortInstances": [{
                "eClass": "net.cloudml.core:ProvidedPortInstance",
                    "name": "requireZKMaster1",
                    "type": "internalComponents[zookeeper]/requiredPorts[requiredZKMaster]"
            }],
                "requiredExecutionPlatformInstance": {
                "eClass": "net.cloudml.core:RequiredExecutionPlatformInstance",
                    "name": "vmRequired1",
                    "owner": "internalComponentInstances[zookeeper-worker1]",
                    "type": "internalComponents[zookeeper]/requiredExecutionPlatform[vmRequired]"
            }
        }, {
            "eClass": "net.cloudml.core:InternalComponentInstance",
                "name": "zookeeper-worker2",
                "type": "internalComponents[zookeeper]",
                "providedPortInstances": [{
                "eClass": "net.cloudml.core:ProvidedPortInstance",
                    "name": "zookeeper2",
                    "type": "internalComponents[zookeeper]/providedPorts[zookeeper]"
            }],
                "requiredPortInstances": [{
                "eClass": "net.cloudml.core:ProvidedPortInstance",
                    "name": "requireZKMaster2",
                    "type": "internalComponents[zookeeper]/requiredPorts[requiredZKMaster]"
            }],
                "requiredExecutionPlatformInstance": {
                "eClass": "net.cloudml.core:RequiredExecutionPlatformInstance",
                    "name": "vmRequired2",
                    "owner": "internalComponentInstances[zookeeper-worker2]",
                    "type": "internalComponents[zookeeper]/requiredExecutionPlatform[vmRequired]"
            }
        }, {
            "eClass": "net.cloudml.core:InternalComponentInstance",
                "name": "zookeeper-master",
                "type": "internalComponents[zookeeper]",
                "providedPortInstances": [{
                "eClass": "net.cloudml.core:ProvidedPortInstance",
                    "name": "zookeeper3",
                    "type": "internalComponents[zookeeper]/providedPorts[zookeeper]"
            }],
                "requiredExecutionPlatformInstance": {
                "eClass": "net.cloudml.core:RequiredExecutionPlatformInstance",
                    "name": "vmRequired3",
                    "owner": "internalComponentInstances[zookeeper-master]",
                    "type": "internalComponents[zookeeper]/requiredExecutionPlatform[vmRequired]"
            }
        }, {
            "eClass": "net.cloudml.core:InternalComponentInstance",
                "name": "storm-nimbus1",
                "type": "internalComponents[storm-nimbus]",
                "requiredPortInstances": [{
                "eClass": "net.cloudml.core:ProvidedPortInstance",
                    "name": "zookeeperMaster",
                    "type": "internalComponents[storm-nimbus]/requiredPorts[requiredZK]"
            }],
                "requiredExecutionPlatformInstance": {
                "eClass": "net.cloudml.core:RequiredExecutionPlatformInstance",
                    "name": "vmRequired4",
                    "owner": "internalComponentInstances[storm-nimbus1]",
                    "type": "internalComponents[storm-nimbus]/requiredExecutionPlatform[vmRequired]"
            }
        }, {
            "eClass": "net.cloudml.core:InternalComponentInstance",
                "name": "storm-supervisor1",
                "type": "internalComponents[storm-supervisor]",
                "requiredPortInstances": [{
                "eClass": "net.cloudml.core:requiredPortInstance",
                    "name": "zookeeperWorker1",
                    "type": "internalComponents[storm-supervisor]/requiredPorts[requiredZK]"
            }],
                "requiredExecutionPlatformInstance": {
                "eClass": "net.cloudml.core:RequiredExecutionPlatformInstance",
                    "name": "vmRequired5",
                    "owner": "internalComponentInstances[storm-supervisor1]",
                    "type": "internalComponents[storm-supervisor]/requiredExecutionPlatform[vmRequired]"
            }
        }, {
            "eClass": "net.cloudml.core:InternalComponentInstance",
                "name": "storm-supervisor2",
                "type": "internalComponents[storm-supervisor]",
                "requiredPortInstances": [{
                "eClass": "net.cloudml.core:requiredPortInstance",
                    "name": "zookeeperWorker2",
                    "type": "internalComponents[storm-supervisor]/requiredPorts[requiredZK]"
            }],
                "requiredExecutionPlatformInstance": {
                "eClass": "net.cloudml.core:RequiredExecutionPlatformInstance",
                    "name": "vmRequired6",
                    "owner": "internalComponentInstances[storm-supervisor2]",
                    "type": "internalComponents[storm-supervisor]/requiredExecutionPlatform[vmRequired]"
            }
        }],
            "vms": [{
            "eClass": "net.cloudml.core:VM",
                "name": "ML",
                "minRam": "2048",
                "maxRam": "0",
                "minCores": "1",
                "maxCores": "0",
                "minStorage": "10",
                "maxStorage": "0",
                "os": "ubuntu",
                "is64os": true,
                "securityGroup": "SensApp",
                "sshKey": "cloudml",
                "groupName": "storm",
                "privateKey": "cloudml.pem",
                "provider": "providers[openstack-nova]",
                "imageId": "RegionOne/9e2877b8-799e-4c87-a9f7-48140b021ba4",
                "properties": [{
                "eClass": "net.cloudml.core:Property",
                    "name": "KeyPath",
                    "value": "./cloudml.pem"
            }],
                "providedExecutionPlatforms": [{
                "eClass": "net.cloudml.core:ProvidedExecutionPlatform",
                    "name": "m1Provided",
                    "owner": "vms[ML]"
            }]
        }],
            "vmInstances": [{
            "eClass": "net.cloudml.core:VMInstance",
                "name": "storm_worker1",
                "type": "vms[ML]",
                "providedExecutionPlatformInstances": [{
                "eClass": "net.cloudml.core:ProvidedExecutionPlatformInstance",
                    "name": "mlprovided1",
                    "owner": "vmInstances[storm_worker1]",
                    "type": "vms[ML]/providedExecutionPlatforms[m1Provided]"
            }]
        }, {
            "eClass": "net.cloudml.core:VMInstance",
                "name": "storm_worker2",
                "type": "vms[ML]",
                "providedExecutionPlatformInstances": [{
                "eClass": "net.cloudml.core:ProvidedExecutionPlatformInstance",
                    "name": "mlprovided2",
                    "owner": "vmInstances[storm_worker2]",
                    "type": "vms[ML]/providedExecutionPlatforms[m1Provided]"
            }]
        }, {
            "eClass": "net.cloudml.core:VMInstance",
                "name": "storm_master",
                "type": "vms[ML]",
                "providedExecutionPlatformInstances": [{
                "eClass": "net.cloudml.core:ProvidedExecutionPlatformInstance",
                    "name": "mlprovided3",
                    "owner": "vmInstances[storm_master]",
                    "type": "vms[ML]/providedExecutionPlatforms[m1Provided]"
            }]
        }],
            "relationships": [{
            "eClass": "net.cloudml.core:Relationship",
                "name": "zkMastertozk",
                "requiredPort": "internalComponents[zookeeper]/requiredPorts[requiredZKMaster]",
                "providedPort": "internalComponents[zookeeper]/providedPorts[zookeeper]",
                "requiredPortResource": {
                "eClass": "net.cloudml.core:Resource",
                    "name": "no name",
                    "downloadCommand": "wget -P ~ http://ferrynico.com/configure_relationships_zookeeper_workers.sh",
                    "configureCommand": "chmod 400 ~/.ssh/id_rsa; sudo bash configure_relationships_zookeeper_workers.sh"
            },
                "providedPortResource": {
                "eClass": "net.cloudml.core:Resource",
                    "name": "no name",
                    "downloadCommand": "wget -P ~ http://ferrynico.com/configure_relationships_zookeeper_master.sh; sudo bash configure_relationships_zookeeper_master.sh",
                    "configureCommand": ""
            }
        }, {
            "eClass": "net.cloudml.core:Relationship",
                "name": "nimbusTozk",
                "requiredPort": "internalComponents[storm-nimbus]/requiredPorts[requiredZK]",
                "providedPort": "internalComponents[zookeeper]/providedPorts[zookeeper]",
                "requiredPortResource": {
                "eClass": "net.cloudml.core:Resource",
                    "name": "no name",
                    "downloadCommand": "",
                    "configureCommand": ""
            },
                "providedPortResource": {
                "eClass": "net.cloudml.core:Resource",
                    "name": "no name",
                    "downloadCommand": "",
                    "configureCommand": ""
            }
        }],
            "relationshipInstances": [{
            "eClass": "net.cloudml.core:RelationshipInstance",
                "name": "skMasterToWorker1",
                "type": "relationships[zkMastertozk]",
                "requiredPortInstance": "internalComponentInstances[zookeeper-worker1]/requiredPortInstances[requireZKMaster1]",
                "providedPortInstance": "internalComponentInstances[zookeeper-master]/providedPortInstances[zookeeper3]"
        }, {
            "eClass": "net.cloudml.core:RelationshipInstance",
                "name": "skMasterToWorker2",
                "type": "relationships[zkMastertozk]",
                "requiredPortInstance": "internalComponentInstances[zookeeper-worker2]/requiredPortInstances[requireZKMaster2]",
                "providedPortInstance": "internalComponentInstances[zookeeper-master]/providedPortInstances[zookeeper3]"
        }, {
            "eClass": "net.cloudml.core:RelationshipInstance",
                "name": "stormZk1",
                "type": "relationships[nimbusTozk]",
                "requiredPortInstance": "internalComponentInstances[storm-nimbus1]/requiredPortInstances[zookeeperMaster]",
                "providedPortInstance": "internalComponentInstances[zookeeper-master]/providedPortInstances[zookeeper3]"
        }, {
            "eClass": "net.cloudml.core:RelationshipInstance",
                "name": "stormZk2",
                "type": "relationships[nimbusTozk]",
                "requiredPortInstance": "internalComponentInstances[storm-supervisor1]/requiredPortInstances[zookeeperWorker1]",
                "providedPortInstance": "internalComponentInstances[zookeeper-worker1]/providedPortInstances[zookeeper1]"
        }, {
            "eClass": "net.cloudml.core:RelationshipInstance",
                "name": "stormZk3",
                "type": "relationships[nimbusTozk]",
                "requiredPortInstance": "internalComponentInstances[storm-supervisor2]/requiredPortInstances[zookeeperWorker2]",
                "providedPortInstance": "internalComponentInstances[zookeeper-worker2]/providedPortInstances[zookeeper2]"
        }],
            "executesInstances": [{
            "eClass": "net.cloudml.core:ExecuteInstance",
                "name": "runOn1",
                "providedExecutionPlatformInstance": "vmInstances[storm_worker1]/providedExecutionPlatformInstances[mlprovided1]",
                "requiredExecutionPlatformInstance": "internalComponentInstances[zookeeper-worker1]/requiredExecutionPlatformInstance[vmRequired1]"
        }, {
            "eClass": "net.cloudml.core:ExecuteInstance",
                "name": "runOn2",
                "providedExecutionPlatformInstance": "vmInstances[storm_worker2]/providedExecutionPlatformInstances[mlprovided2]",
                "requiredExecutionPlatformInstance": "internalComponentInstances[zookeeper-worker2]/requiredExecutionPlatformInstance[vmRequired2]"
        }, {
            "eClass": "net.cloudml.core:ExecuteInstance",
                "name": "runOn3",
                "providedExecutionPlatformInstance": "vmInstances[storm_master]/providedExecutionPlatformInstances[mlprovided3]",
                "requiredExecutionPlatformInstance": "internalComponentInstances[zookeeper-master]/requiredExecutionPlatformInstance[vmRequired3]"
        }, {
            "eClass": "net.cloudml.core:ExecuteInstance",
                "name": "runOn4",
                "providedExecutionPlatformInstance": "vmInstances[storm_master]/providedExecutionPlatformInstances[mlprovided3]",
                "requiredExecutionPlatformInstance": "internalComponentInstances[storm-nimbus1]/requiredExecutionPlatformInstance[vmRequired4]"
        }, {
            "eClass": "net.cloudml.core:ExecuteInstance",
                "name": "runOn5",
                "providedExecutionPlatformInstance": "vmInstances[storm_worker1]/providedExecutionPlatformInstances[mlprovided1]",
                "requiredExecutionPlatformInstance": "internalComponentInstances[storm-supervisor1]/requiredExecutionPlatformInstance[vmRequired5]"
        }, {
            "eClass": "net.cloudml.core:ExecuteInstance",
                "name": "runOn6",
                "providedExecutionPlatformInstance": "vmInstances[storm_worker2]/providedExecutionPlatformInstances[mlprovided2]",
                "requiredExecutionPlatformInstance": "internalComponentInstances[storm-supervisor2]/requiredExecutionPlatformInstance[vmRequired6]"
        }]
    };
}

function validate_input(obj, pointer) {
    if (typeof obj !== "object") {
        throw ("Invalid input object.");
    }
    if (pointer === "") {
        return [];
    }
    if (!pointer) {
        throw ("Invalid JSON pointer.");
    }
    pointer = pointer.split("/");
    var first = pointer.shift();
    if (first !== "") {
        throw ("Invalid JSON pointer:" + first);
    }
    return pointer;
};

function getJSON(obj, pointer) {
    pointer = validate_input(obj, pointer);
    if (pointer.length === 0) {
        return obj;
    }
    return traverse(obj, pointer);
}

function traverse(obj, pointer) {
    var part = pointer.shift();
    var tmp = part.split("[");
    //filtering
    if (part.indexOf("[") >= 0) {
        if (!obj.hasOwnProperty(tmp[0])) {
            return null;
        }
        var tmp2 = tmp[1].split("]");
        var val = tmp2[0];
        if (!obj[tmp[0]].length) {
            if (obj[tmp[0]].name == val) {
                return obj[tmp[0]];
            }
        }
        for (var a = 0; a < obj[tmp[0]].length; a++) {
            if (obj[tmp[0]][a].hasOwnProperty('name')) {
                if (obj[tmp[0]][a]['name'] == val) {
                    if (pointer.length !== 0) return traverse(obj[tmp[0]][a], pointer);
                    else return obj[tmp[0]][a];
                }
            }
        }
        return null;
    } else {
        if (!obj.hasOwnProperty(part)) {
            return null;
        }
    }
    if (pointer.length !== 0) { // keep traversin!
        return traverse(obj[part], pointer);
    }
    return obj[part];
}