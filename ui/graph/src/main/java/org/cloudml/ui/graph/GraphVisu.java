/**
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
package org.cloudml.ui.graph;

import java.io.InputStream;
import java.util.ArrayList;

import org.cloudml.codecs.DrawnIconVertexDemo;
import org.cloudml.codecs.Vertex;
import org.cloudml.core.Deployment;

public class GraphVisu {

	private static InputStream stream;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Deployment model=new Deployment();
		DrawnIconVertexDemo g = new DrawnIconVertexDemo(model);
		Visu gui= new Visu(model,g);
		gui.createFrame();
		ArrayList<Vertex> v = g.drawVerticesFromDeploymentModel(model);
		g.drawEdgesFromDeploymentModel(model, v);
	}
	
	
}
