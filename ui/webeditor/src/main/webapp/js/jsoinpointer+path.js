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

//extended from https://github.com/alexeykuzmin/jsonpointer.js/


function traverse(obj, pointer) {
	var part = pointer.shift();
	var tmp = part.split("[");
	//filtering
	if (part.indexOf("[") >= 0) {
		if (!obj.hasOwnProperty(tmp[0])) {
			return null;
		}
		//var temp2=tmp[1].split("=");
		//var attribute=temp2[0];
		var tmp2 = tmp[1].split("]");
		var val = tmp2[0];
		//var temp4=temp3[0].split("'");
		//var val=temp4[1];
		for (var a = 0; a < obj[tmp[0]].length; a++) {
			if (obj[tmp[0]][a].hasOwnProperty('name')) {
				if (obj[tmp[0]][a]['name'] == val) {
					if (pointer.length !== 0)
						return traverse(obj[tmp[0]][a], pointer);
					else
						return obj[tmp[0]][a];
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

function traverse2(obj, pointer, value) {
	var part = pointer.shift();
	var tmp = part.split("[");

	//filtering
	if (part.indexOf("[") >= 0) {
		if (!obj.hasOwnProperty(tmp[0])) {
			return null;
		}
		var temp2 = tmp[1].split("=");
		var attribute = temp2[0];
		var temp3 = temp2[1].split("]");
		var temp4 = temp3[0].split("'");
		var val = temp4[1];
		for (var a = 0; a < obj[tmp[0]].length; a++) {
			if (obj[tmp[0]][a].hasOwnProperty(attribute)) {
				if (obj[tmp[0]][a][attribute] == val) {
					return traverse(obj[tmp[0]][a], pointer, value);
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
		return traverse(obj[part], pointer, value);
	}
	// we're done
	if (typeof value === "undefined") {
		// just reading
		return obj[part];
	}
	// set new value, return old value
	var old_value = obj[part];
	if (value === null) {
		delete obj[part];
	} else {
		obj[part] = value;
	}
	return old_value;
}

var validate_input = function (obj, pointer) {
	if (typeof obj !== "object") {
		throw("Invalid input object.");
	}

	if (pointer === "") {
		return [];
	}

	if (!pointer) {
		throw("Invalid JSON pointer.");
	}

	pointer = pointer.split("/");
	var first = pointer.shift();
	if (first !== "") {
		throw("Invalid JSON pointer:" + first);
	}

	return pointer;
}

function getJSON(obj, pointer) {
	pointer = validate_input(obj, pointer);
	if (pointer.length === 0) {
		return obj;
	}
	return traverse(obj, pointer);
}

function setJSON(obj, pointer, value) {
	pointer = validate_input(obj, pointer);
	if (pointer.length === 0) {
		throw("Invalid JSON pointer for set.")
	}
	return traverse2(obj, pointer, value);
}
