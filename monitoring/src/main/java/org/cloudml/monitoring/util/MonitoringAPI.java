package org.cloudml.monitoring.util;

import it.polimi.modaclouds.monitoring.metrics_observer.examples.ExampleObServer;
import org.cloudml.monitoring.util.HTTPConnection;

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
     * This method instantiates an observer in order to receive the data
     * @param port specify the port on which the observer wil run
     * @return the port
     */
    public int instantiateObserver(int port){

        //TEST
        port=8123;

        ExampleObServer observer = new ExampleObServer(port);

        try {
            observer.start();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

        return port;
    }

    /**
     * This method sends a request to attach an observer to a specific metric
     * @param callback the address on which the observer is running
     * @param address the address on which the monitoring manager is running
     * @param metric the requested metric
     */
    public void attachObserver(String callback, String address, String metric){

        String url = address + "/v1/metrics/" + metric;

        http.postRequest(url, callback);

    }

    /**
     * This method sends an update to the monitoring manager about the state of the deployment
     * @param address the address on which the monitoring manager is running
     * @param deployment the state of the deployment
     */
    public void updateDeployment(String address, String deployment){

       String url = address + "/v1/update";

       http.postRequest(url, deployment);

    }

}