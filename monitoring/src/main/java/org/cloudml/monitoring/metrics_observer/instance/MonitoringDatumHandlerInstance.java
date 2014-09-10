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

import it.polimi.modaclouds.monitoring.metrics_observer.MonitoringDatumHandler;

import java.util.*;

/**
 * Sample class that handles the data coming from the observer
 * In the getData() method there should be the code to store the
 * data in the model
 */
public class MonitoringDatumHandlerInstance extends MonitoringDatumHandler {

    @Override
    public void getData(
            Map<String, Map<String, List<Map<String, String>>>> monitoringData) {
        for (String datum : monitoringData.keySet()) {
            String metric = nullable(
                    monitoringData
                            .get(datum)
                            .get("http://www.modaclouds.eu/rdfs/1.0/monitoringdata#metric"))
                    .get(0).get("value");
            String timestamp = nullable(
                    monitoringData
                            .get(datum)
                            .get("http://www.modaclouds.eu/rdfs/1.0/monitoringdata#timestamp"))
                    .get(0).get("value");
            String value = nullable(
                    monitoringData
                            .get(datum)
                            .get("http://www.modaclouds.eu/rdfs/1.0/monitoringdata#value"))
                    .get(0).get("value");
            String resourceId = nullable(
                    monitoringData
                            .get(datum)
                            .get("http://www.modaclouds.eu/rdfs/1.0/monitoringdata#resourceId"))
                    .get(0).get("value");
            System.out.println(new Date().getTime()+"," + resourceId + "," + metric + "," + value + ","
                    + timestamp);
        }
    }

    private List<Map<String, String>> nullable(List<Map<String, String>> list) {
        if (list != null)
            return list;
        else {
            List<Map<String, String>> emptyValueList = new ArrayList<Map<String, String>>();
            Map<String, String> emptyValueMap = new HashMap<String, String>();
            emptyValueMap.put("value", "");
            emptyValueList.add(emptyValueMap);
            return emptyValueList;
        }
    }

}
