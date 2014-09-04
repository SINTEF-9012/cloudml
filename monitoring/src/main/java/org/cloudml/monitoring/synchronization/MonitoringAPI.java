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
import it.polimi.modaclouds.qos_models.monitoring_ontology.VM;
import org.cloudml.core.ExternalComponent;


import java.util.List;
import java.util.logging.Level;

/**
 * @author Lorenzo Cianciaruso
 */
public class MonitoringAPI {

    private HTTPConnection http;
    private String address;
    private final String version = "v1";
    private static final java.util.logging.Logger journal = java.util.logging.Logger.getLogger(MonitoringAPI.class.getName());

    public MonitoringAPI(String address) {
        this.http = new HTTPConnection();
        this.address = address;
    }

    /**
     * This method sends a request to upload a monitoring rule to the monitoring manager.
     @param rule is the monitoring rule to be uploaded

     @return the response of the monitoring manager
     */
    public String addMonitoringRule(String rule){

        String url = address + "/" + version + "/monitoring-rules";

        StringBuffer response = null;
        try {
            response = http.postRequest(url, rule);
        } catch (Exception e) {
            journal.log(Level.INFO, "Connection to the monitoring manager refused");
            return null;
        }

        return response.toString();
    }

    /**
     * This methods sends a request to get all the metrics available
     *
     * @return a list of metrics
     */
    public List<String> getMetrics(){

        String url = address + "/" + version + "/metrics";

        try {
            http.getRequest(url);
        } catch (Exception e) {
            journal.log(Level.INFO, "Connection to the monitoring manager refused");
            return null;
        }

        //TODO parse results

        return null;
    }

    /**
     * This method sends a request to attach an observer to a specific metric
     * @param callback the address on which the observer is running
     *
     * @param metric the requested metric
     */
    public void attachObserver(String callback, String metric) {

        String url = address + "/" + version + "/metrics/" + metric + "/observers";

        try {
            http.postRequest(url, callback);
            journal.log(Level.INFO, "Observer attached");
        } catch (Exception e) {
            journal.log(Level.INFO, "Connection to the monitoring manager refused");
        }

    }

    /**
     * This method sends an update to the monitoring manager about the state of the deployment
     *
     * @param update the state of the deployment
     */
    public void addInstances(Model update){

        String url = address + "/" + version + "/resources";

        Gson gson = new GsonBuilder().setExclusionStrategies(new ModelUpdatesExclusionStrategy()).serializeNulls().create();

        String json = gson.toJson(update);

        try {
            journal.log(Level.INFO, ">> Connecting to the monitoring platform at "+address+"...");
            http.postRequest(url, json);
            printComponentname(update);
        } catch (Exception e) {
            journal.log(Level.INFO, "Connection to the monitoring manager refused");
        }

    }

    /**
     * This method upload the deployment model in the monitoring manager
     *
     * @param model the state of the deployment
     */
    public void uploadDeployment(Model model){

        String url = address + "/" + version + "/resources";

        Gson gson = new GsonBuilder().setExclusionStrategies(new ModelUpdatesExclusionStrategy()).serializeNulls().create();

        String json = gson.toJson(model);
        try {
            journal.log(Level.INFO, ">> Connecting to the monitoring platform at "+address+"...");
            http.putRequest(url, json);
            printComponentname(model);
        } catch (Exception e) {
            journal.log(Level.INFO, "Connection to the monitoring manager refused");
        }

    }

    /**
     * This method tells to the monitoring manager which instances must be removed
     * from the deployment model
     *
     * @param id are the IDs of the instances to be deleted
     */
    public void deleteInstances(String id){
        String url = address + "/" + version + "/resources/" + id;
        try {
            journal.log(Level.INFO, ">> Connecting to the monitoring platform at "+address+"...");
            http.deleteRequest(url, id);
        } catch (Exception e) {
            journal.log(Level.INFO, "Connection to the monitoring manager refused");
        }
    }

    private void printComponentname(Model model){
        /* there is no getComponent in Model class
        for(Component i : model.getComponent()){
            journal.log(Level.INFO, "Component name: "+model.getComponents().get(i).getId());
        }
        for(ExternalComponent e : model.getExternalComponent()){
            journal.log(Level.INFO, "ExternalComponent name: "+model.getExternalComponents().get(i).getId());
        }*/
        for(VM vm : model.getvMs()){
            journal.log(Level.INFO, "VM name: "+vm.getId());
        }
    }

}