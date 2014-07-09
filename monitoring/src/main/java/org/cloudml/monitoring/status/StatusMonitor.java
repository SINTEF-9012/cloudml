package org.cloudml.monitoring.status;
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
import org.cloudml.monitoring.status.modules.FlexiantModule;
import org.cloudml.monitoring.status.modules.JCloudsModule;
import org.cloudml.monitoring.status.modules.Module;
import org.cloudml.monitoring.status.modules.OpenStackModule;
import org.cloudml.mrt.Coordinator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A monitor to get the status of deployed VMs
 * A push notification is send when the status change
 *
 * @author Francesco di Forenza
 */

public class StatusMonitor {
    private static final Logger journal = Logger.getLogger(StatusMonitor.class.getName());
    private Collection<Module> modules;
    private int refreshRate;
    private boolean active;
    private Thread thread;
    private Coordinator coord;

    /**
     * Create a new monitor
     *
     * @param refreshRate the rate at which the monitor will collect information
     * @param active      if true the monitor will start immediately
     * @param coord       the coordinator that will be used to update the model
     */
    public StatusMonitor(int refreshRate, boolean active, Coordinator coord) {
        this.refreshRate = refreshRate;
        this.active = active;
        this.coord = coord;
        this.modules = Collections.synchronizedCollection(new ArrayList<Module>());
        if (active) {
            start();
        }
    }

    private void backgroundAgent() {
        while (active) {
            journal.log(Level.INFO, "Looking for status changes..");
            //TODO put each module in a thread to deal with connection delay
            synchronized (modules) {
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
            module = new FlexiantModule((FlexiantConnector) connector, coord);
        } else if (connector instanceof OpenStackConnector) {
            module = new OpenStackModule((OpenStackConnector) connector, coord);
        } else if (connector instanceof JCloudsConnector) {
            module = new JCloudsModule((JCloudsConnector) connector, coord);
        } else {
            //TODO exception
            System.out.println("error");

        }
        if (module != null) {
            synchronized (modules) {
                if (!modules.contains(module)) {
                    modules.add(module);
                }
            }
            journal.log(Level.INFO, "Module attached: " + module.getType());
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
                    journal.log(Level.INFO, "Module detached: " + i.getType());
                }
            }
        }
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
                    journal.log(Level.INFO, "Module detached: " + i.getType());
                }
            }
        }
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
        journal.log(Level.INFO, "Monitoring paused");
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
            journal.log(Level.INFO, "Monitoring already started. Check your code");
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
        journal.log(Level.INFO, "Monitoring stopped and history deleted");
    }
}
