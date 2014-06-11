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

import org.cloudml.monitoring.metrics_observer.MetricsObServer;

public class MetricObServerInstance extends MetricsObServer {

	public MetricObServerInstance(int listeningPort) {
		super(listeningPort, MyResultHandler.class);
	}
	
	public static void main(String[] args) {
		MetricObServerInstance observer = new MetricObServerInstance(8123);
		try {
			observer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
     * This method instantiates an observer in order to receive the data
     * @param port specify the port on which the observer wil run
     * @return true if the it is instanciated correctly, 0 otherwise
     */
    public boolean instanciateObserver(int port){

        //TEST
        port=8123;

        MetricObServerInstance observer = new MetricObServerInstance(port);
        try {
            observer.start();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
