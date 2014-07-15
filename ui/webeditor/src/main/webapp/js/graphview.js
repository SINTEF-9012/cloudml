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
var nodes;
var nodes2;
var links;
var links2;
var svg;

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
			source : null,
			target : null
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

function generateExecutes(deploymentModel) {
	var links = [];
	deploymentModel.relationshipInstances.forEach(

		function (d, i) {
		var tempLink = {
			source : null,
			target : null
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

function getData(jsonString) {
	root = eval('(' + jsonString + ')');
	nodes = flatten(root);
	nodes2 = f2(root);
	links = generateLinks(root);
	links2 = generateExecutes(root);
	
	var width = (window.innerWidth),
	height = (window.innerHeight);

	var force = d3.layout.force()
		.charge(-800)
		.linkDistance(150)
		.size([width, height]);

	svg = d3.select("body").append("svg:svg")
		.attr("width", width)
		.attr("height", height);

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
		return i < links.length ? "executionBinding" : "link";
	})
	.style('marker-start', '')
	.style('marker-end', 'url(#end-arrow)');

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
	.attr('font-size', '20px')
	.text(function (d, i) {
		return i < nodes.length ? '\uf013' : '\uf0c2';
	});

	g.append('svg:text')
	.attr('x', '0')
	.attr('y', '30')
	.attr('text-anchor', 'middle')
	.attr('dominant-baseline', 'central')
	.attr('font-family', 'sans-serif')
	.attr('font-size', '10px')
	.text(function (d, i) {
		return d.name;
	});

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

		circle.attr('transform', function (d) {
			return 'translate(' + d.x + ',' + d.y + ')';
		});

	});
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
