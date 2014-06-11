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
package org.cloudml.monitoring.metrics_observer;

import org.cloudml.monitoring.metrics_observer.model.Variable;

import java.io.StringReader;
import java.util.List;
import java.util.Map;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public abstract class ResultsHandler extends ServerResource {

	private Logger logger = LoggerFactory.getLogger(ResultsHandler.class
			.getName());
	
	public abstract void getData(List<String> varNames, List<Map<String, Variable>> bindings);

	@Post
	public void getData(Representation entity) {
		String results = null;
		JsonReader reader = null;
		Gson gson = new Gson();
		try {
			results = entity.getText();
			reader = new JsonReader(new StringReader(results));
			org.cloudml.monitoring.metrics_observer.model.Sparql_json_results s = gson.fromJson(reader,
					org.cloudml.monitoring.metrics_observer.model.Sparql_json_results.class);
			getData(s.getHead().getVars(), s.getResults().getBindings());
			this.getResponse().setStatus(Status.SUCCESS_OK,
					"Result succesfully received");
			this.getResponse().setEntity(
					gson.toJson("Result succesfully received"),
					MediaType.APPLICATION_JSON);

		} catch (Exception e) {
			logger.error("Error while receiving results: " + results, e);
			this.getResponse().setStatus(Status.SERVER_ERROR_INTERNAL,
					"Error while receiving data");
			this.getResponse().setEntity(
					gson.toJson("Error while receiving data"),
					MediaType.APPLICATION_JSON);
		} finally {
			this.getResponse().commit();
			this.commit();
			this.release();
		}
	}
}
