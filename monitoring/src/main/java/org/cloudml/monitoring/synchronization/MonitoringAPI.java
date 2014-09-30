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

import com.github.kevinsawicki.http.HttpRequest;
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


    private String address;
    private final String version = "v1";
    private static final java.util.logging.Logger journal = java.util.logging.Logger.getLogger(MonitoringAPI.class.getName());
    public static final int SUCCESS_NO_CONTENT = 204;
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int SUCCESS = 200;
    public static final int CLIENT_ERROR_NOT_FOUND = 404;
    public static final int NO_RESPONSE = 0;

    public MonitoringAPI(String address) {

        this.address = address;
    }

    /**
     * This method sends a request to upload a monitoring rule to the monitoring manager.
     @param rule is the monitoring rule to be uploaded

     @return the response of the monitoring manager
     */
    public String addMonitoringRule(String rule){

        String url = address + "/" + version + "/monitoring-rules";
        String response = null;

        try {
        //    response = http.postRequest(url, rule);
            response = HttpRequest.post(url).send(rule).body();
        } catch (Exception e) {
            journal.log(Level.INFO, "Connection to the monitoring manager refused");
        }
        return response;
    }

    /**
     * This methods sends a request to get all the metrics available
     *
     * @return a list of metrics
     */
    public List<String> getMetrics(){

        String url = address + "/" + version + "/metrics";

        try {
           // http.getRequest(url);
            String response = HttpRequest.get(url).body();
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
         //   http.postRequest(url, callback);
        HttpRequest.post(url).send(callback).code();
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

        String url = address + "/" + version + "/model/resources";
        int result;
        Gson gson = new GsonBuilder().setExclusionStrategies(new ModelUpdatesExclusionStrategy()).serializeNulls().create();
        String json = gson.toJson(update);

        try {
            journal.log(Level.INFO, ">> Connecting to the monitoring platform at "+address+"...");
            printComponentname(update);
            result = HttpRequest.post(url).send(json).code();
        } catch (Exception e) {
        result = 0;
        }
        printResult(result);

    }

    /**
     * This method upload the deployment model in the monitoring manager
     *
     * @param model the state of the deployment
     */
    public void uploadDeployment(Model model){

        String url = address + "/" + version + "/model/resources";
        int result;
        Gson gson = new GsonBuilder().setExclusionStrategies(new ModelUpdatesExclusionStrategy()).serializeNulls().create();
        String json = gson.toJson(model);
        try {
            journal.log(Level.INFO, ">> Connecting to the monitoring platform at "+address+"...");
        result = HttpRequest.put(url).send(json).code();
            printComponentname(model);
        } catch (Exception e) {
           result = 0;
        }

        printResult(result);

    }

    /**
     * This method tells to the monitoring manager which instances must be removed
     * from the deployment model
     *
     * @param id are the IDs of the instances to be deleted
     * @return int code status of the connection
     */
    public int deleteInstances(String id){
        String url = address + "/" + version + "/model/resources/" + id;
        int result;
        try {
            journal.log(Level.INFO, ">> Connecting to the monitoring platform at "+address+"...");
            result = HttpRequest.delete(url).code();
        } catch (Exception e) {
            result = NO_RESPONSE;
        }
        printResult(result);

        return result;
    }

    private void printComponentname(Model model){
        for(VM vm : model.getvMs()){
            journal.log(Level.INFO, "VM name: "+vm.getId());
        }
    }

    private void printResult(int result){
        String message = "";
        switch(result) {
            case SUCCESS:
                message = " Connection successful";
                break;
            case SUCCESS_NO_CONTENT:
                message = " Connection successful no content";
                break;
            case INTERNAL_SERVER_ERROR:
                message = " Error during connection, internal server error";
                break;
            case CLIENT_ERROR_NOT_FOUND:
                message = " Connection error, resource not found";
                break;
            case NO_RESPONSE:
                message = " Connection to the monitoring manager refused";
                break;

        }
        journal.log(Level.INFO,  String.valueOf(result) + message);
    }



}