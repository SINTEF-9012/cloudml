package org.cloudml.monitoring.util;

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

import org.cloudml.monitoring.status.StatusMonitor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Francesco di Forenza
 */

public class ConfigurationLoader {
    private static final Logger journal = Logger.getLogger(StatusMonitor.class.getName());
    public static class MonitoringProperties{
        private boolean activated;
        private int frequency;
        private boolean monitoringPlatformGiven;
        private String ipAddress;

        public boolean getActivated() {
            return activated;
        }

        public int getFrequency() {
            return frequency;
        }

        public boolean isMonitoringPlatformGiven() {
            return monitoringPlatformGiven;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public MonitoringProperties(boolean activated, int frequency, boolean monitoringPlatformGiven, String ipAddress) {
            this.activated = activated;
            this.frequency = frequency;
            this.monitoringPlatformGiven = monitoringPlatformGiven;
            this.ipAddress = ipAddress;
        }
        public MonitoringProperties(){
            this.activated = false;
            this.frequency = 60;
            this.monitoringPlatformGiven = false;
            this.ipAddress = "localhost";
        }
    }
    public static MonitoringProperties load() {

        Properties prop = new Properties();
        InputStream input = null;
        MonitoringProperties properties = new MonitoringProperties();
        try {

            input = new FileInputStream("monitoring.properties");

            // load a properties file
            prop.load(input);

            // get the property
            boolean activated= Boolean.parseBoolean((prop.getProperty("activated")));
            int frequency = Integer.parseInt((prop.getProperty("frequency")));
            boolean monitoringPlatformGiven = Boolean.parseBoolean(((prop.getProperty("use"))));
            String ipAddress = ((prop.getProperty("address")));

            properties = new MonitoringProperties(activated, frequency, monitoringPlatformGiven, ipAddress);

        } catch (IOException ex) {
            journal.log(Level.INFO, ">> Monitoring.properties not found using default values");
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return properties;
    }
}
