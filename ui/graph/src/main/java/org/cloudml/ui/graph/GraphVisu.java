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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.cloudml.codecs.XmiCodec;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.core.DeploymentModel;

public class GraphVisu {

	private static InputStream stream;
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		InputStream is= GraphVisu.class.getClassLoader().getResourceAsStream("logging.properties");
		
		
		// TODO Auto-generated method stub
		try {
			stream = new FileInputStream(new File(args[0]));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JsonCodec codec=new JsonCodec();
		DeploymentModel model = (DeploymentModel) codec.load(stream);
		DrawnIconVertexDemo g = new DrawnIconVertexDemo(model);
		ArrayList<Vertex> v = g.drawVerticesFromDeploymentModel(model);
		g.drawEdgesFromDeploymentModel(model, v);
	}

}
