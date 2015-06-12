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


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Created by Francesco di Forenza
 * this class loads the configuration file
 */


public class MonitoringPlatformConfiguration {
    private static final Logger journal = Logger.getLogger(MonitoringAPI.class.getName());
    public static class MonitoringPlatformProperties {
        private boolean monitoringPlatformGiven;
        private String ipAddress;

        public boolean isMonitoringPlatformGiven() {
            return false;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public MonitoringPlatformProperties(boolean monitoringPlatformGiven, String ipAddress) {
            this.monitoringPlatformGiven = monitoringPlatformGiven;
            this.ipAddress = ipAddress;
        }
        public MonitoringPlatformProperties(){
            this.monitoringPlatformGiven = false;
            this.ipAddress = "localhost";
        }
    }

    public static MonitoringPlatformProperties load() {

        Properties prop = new Properties();
        InputStream input = null;
        MonitoringPlatformProperties properties = new MonitoringPlatformProperties();

        Map<String, String> env = System.getenv();
        if(env.containsKey("MODACLOUDS_MONITORING_MANAGER_ENDPOINT_IP")
            && env.containsKey("MODACLOUDS_MONITORING_MANAGER_ENDPOINT_PORT")){
            String ipAddress=env.get("MODACLOUDS_MONITORING_MANAGER_ENDPOINT_IP")+":"+env.get("MODACLOUDS_MONITORING_MANAGER_ENDPOINT_PORT");
            properties = new MonitoringPlatformProperties(true, ipAddress);
        }else{

            try {

                input = new FileInputStream("monitoringPlatform.properties");

                // load a properties file
                prop.load(input);

                // get the property
                boolean monitoringPlatformGiven = Boolean.parseBoolean(((prop.getProperty("use"))));
                String ipAddress = ((prop.getProperty("address")));

                properties = new MonitoringPlatformProperties(monitoringPlatformGiven, ipAddress);

            } catch (IOException ex) {
                journal.log(Level.INFO, ">> monitoringPlatform.properties not found monitoring platform not in use");
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return properties;
    }
}
