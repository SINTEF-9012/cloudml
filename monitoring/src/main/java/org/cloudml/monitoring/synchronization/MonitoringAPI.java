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
package org.cloudml.monitoring.synchronization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
/**
 * Created by user on 10.06.14.
 * @author Lorenzo Cianciaruso
 */
public class MonitoringAPI {

    private HTTPConnection http;

    public MonitoringAPI () {
        http = new HTTPConnection();
    }

    /**
     * This method sends a request to upload a monitoring rule to the monitoring manager.
     @param rule is the monitoring rule to be uploaded
     @param address is the address where the monitoring manager is running
     @return the response of the monitoring manager
     */
    public String addMonitoringRule(String rule, String address){

        String url = address + "/v1/monitoring-rules";

        StringBuffer response = http.postRequest(url, rule);

        return response.toString();

    }

    /**
     * This methods sends a request to get all the metrics available
     * @param address is the address where the monitoring manager is running
     * @return a list of metrics
     */
    public List<String> getMetrics(String address){

        String url = address + "/v1/metrics";

        http.getRequest(url);

        //TODO parse results

        return null;
    }

    /**
     * This method sends a request to attach an observer to a specific metric
     * @param callback the address on which the observer is running
     * @param address the address on which the monitoring manager is running
     * @param metric the requested metric
     */
    public void attachObserver(String callback, String address, String metric) {

        String url = address + "/v1/metrics/" + metric;

        http.postRequest(url, callback);
    }



    /**
     * This method sends an update to the monitoring manager about the state of the deployment
     * @param address the address where the monitoring manager is running
     * @param update the state of the deployment
     */
    public void addInstances(String address, ModelUpdates update){

        String url = address + "/v1/update";

        Gson gson = new GsonBuilder().setExclusionStrategies(new ModelUpdatesExclusionStrategy()).serializeNulls().create();

        String json = gson.toJson(update);

        http.postRequest(url, json);

    }


    /**
     * This method upload the deployment model in the monitoring manager
     * @param address is the address where the monitoring manager is running
     * @param model the state of the deployment
     */
    public void uploadDeployment(String address, ModelUpdates model){

        String url = address + "/v1/upload";

        Gson gson = new GsonBuilder().setExclusionStrategies(new ModelUpdatesExclusionStrategy()).serializeNulls().create();

        String json = gson.toJson(model);

        http.postRequest(url, json);
    }

    /**
     * This method deletes tells to the monitoring manager which instances must be removed from the deployment model
     * @param address is the address where the monitoring manager is running
     * @param id are the IDs of the instances to be deleted
     */
    public void deleteInstances(String address, String id){

        String url = address + "/v1/update";

        http.deleteRequest(url, id);
    }

}