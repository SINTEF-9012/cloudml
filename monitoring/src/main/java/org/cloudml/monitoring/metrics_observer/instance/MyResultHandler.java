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
package org.cloudml.monitoring.metrics_observer.instance;

import java.util.List;
import java.util.Map;

import org.cloudml.monitoring.metrics_observer.ResultsHandler;

public class MyResultHandler extends ResultsHandler {

	@Override
	public void getData(List<String> varNames,
			List<Map<String, org.cloudml.monitoring.metrics_observer.model.Variable>> bindings) {
		String value;
		for (Map<String, org.cloudml.monitoring.metrics_observer.model.Variable> m : bindings) {
			String metricPath = "";
			int last = varNames.size();
			for (int i = 0; i < last; i++) {
				org.cloudml.monitoring.metrics_observer.model.Variable var = m.get(varNames.get(i));
				if (var != null) {
					value = var.getValue();
					metricPath += value.substring(value.indexOf('#') + 1)
							+ (i == last - 1 ? "" : " ");
				}
			}
			System.out.println(metricPath);
		}
	}

}
