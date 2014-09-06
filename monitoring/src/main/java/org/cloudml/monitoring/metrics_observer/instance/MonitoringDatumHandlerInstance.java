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
