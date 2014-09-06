package org.cloudml.monitoring.metrics_observer.instance;

import it.polimi.modaclouds.monitoring.metrics_observer.examples.CVSResultHandler;

import it.polimi.modaclouds.monitoring.metrics_observer.MetricsObServer;

/**
 * Created by Lorenzo Cianciaruso on 06.09.14.
 * This class extends the Metric Observer, just declares the constructor.
 * The constructor must be called when a new observer must be created.
 *
 */
public class MetricObserverInstance extends MetricsObServer {

    public MetricObserverInstance(int listeningPort) {
        super(listeningPort, "/v1/results", CVSResultHandler.class);
    }

}
