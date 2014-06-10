package org.cloudml.monitoring;
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


import org.cloudml.connectors.Connector;
import org.cloudml.connectors.FlexiantConnector;
import org.cloudml.connectors.JCloudsConnector;
import org.cloudml.connectors.OpenStackConnector;
import org.cloudml.monitoring.modules.FlexiantModule;
import org.cloudml.monitoring.modules.JCloudsModule;
import org.cloudml.monitoring.modules.Module;
import org.cloudml.monitoring.modules.OpenStackModule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * A monitor to get the status of deployed VMs
 * A push notification is send when the status change
 *
 * @author Francesco di Forenza
 */

public class StatusMonitor {

    private Collection<Module> modules;
    private int refreshRate;
    private boolean active;

    Thread thread;

    /**
     * Create a new monitor
     *
     * @param refreshRate the rate at which the monitor will collect information
     * @param active      if true the monitor will start immediately
     */
    public StatusMonitor(int refreshRate, boolean active) {
        this.refreshRate = refreshRate;
        this.active = active;
        this.modules = Collections.synchronizedCollection(new ArrayList<Module>());
        thread = new Thread(new Runnable() {
            public void run() {
                backgroundAgent();
            }
        });
        thread.start();
    }

    private void backgroundAgent() {
        while (active) {
            System.out.println("EXECUTING");
            //TODO put each module in a thread to deal with connection delay
            synchronized  (modules) {
                for (Module i : modules) {
                    i.exec();
                }
            }
            try {
                Thread.sleep(refreshRate * 1000);
            } catch (InterruptedException e) {
                break;
            }
        }

    }

    /**
     * Add a module to the monitor
     *
     * @param connector the connector
     */
    public void attachModule(Connector connector) {
        Module module = null;
        if (connector instanceof FlexiantConnector) {
            module = new FlexiantModule((FlexiantConnector) connector);
        } else if (connector instanceof OpenStackConnector) {
            module = new OpenStackModule((OpenStackConnector) connector);
        } else if (connector instanceof JCloudsConnector) {
            module = new JCloudsModule((JCloudsConnector) connector);
        } else {
            //TODO exception
            System.out.println("error");

        }
        if (module!=null)
        {
            synchronized (modules) {
                if (!modules.contains(module)) {
                    modules.add(module);
                }
            }
            System.out.println("MODULE ATTACHED");
        }
    }

    /**
     * Remove a module from the monitor
     *
     * @param module pick one from the enum
     */
    public void detachModule(FlexiantModule.Type module) {
        synchronized (modules) {
            for (Module i : modules) {
                if (i.getType() == module) {
                    modules.remove(i);
                }
            }
        }
        System.out.println("MODULE DETACHED");
    }

    /**
     * Remove a module from the monitor
     *
     * @param connector pick one from the enum
     */
    public void detachModule(Connector connector) {
        synchronized (modules) {

            for (Module i : modules) {
                if (i.getConnector() == connector) {
                    modules.remove(i);
                }
            }
        }
        System.out.println("MODULE DETACHED");
    }

    /**
     * Change the monitor frequency (in seconds)
     *
     * @param refreshRateInSeconds as the name said
     */
    public void changeRate(int refreshRateInSeconds) {
        refreshRate = refreshRateInSeconds;
    }

    /**
     * Pause the monitoring
     */
    public void pause() {
        this.active = false;
        thread.interrupt();
    }

    /**
     * Start the monitoring
     */
    public void start() {
        if (!this.active) {
            this.active = true;
            thread = new Thread(new Runnable() {
                public void run() {
                    backgroundAgent();
                }
            });
            thread.start();
        } else {
            System.out.println("Already started");
        }
    }

    /**
     * Stop the monitor and delete monitoring history
     */
    public void stop() {
        this.active = false;
        thread.interrupt();
        synchronized (modules) {
            modules = new ArrayList<Module>();
        }
    }
}
