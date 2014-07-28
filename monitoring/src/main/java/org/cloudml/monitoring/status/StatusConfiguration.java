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


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Francesco di Forenza
 * this class loads the configuration file
 */

public class StatusConfiguration {
    private static final Logger journal = Logger.getLogger(StatusMonitor.class.getName());
    public static class StatusMonitorProperties {
        private boolean activated;
        private int frequency;


        public boolean getActivated() {
            return activated;
        }

        public int getFrequency() {
            return frequency;
        }


        public StatusMonitorProperties(boolean activated, int frequency) {
            this.activated = activated;
            this.frequency = frequency;

        }
        public StatusMonitorProperties(){
            this.activated = false;
            this.frequency = 60;

        }
    }
    public static StatusMonitorProperties load() {

        Properties prop = new Properties();
        InputStream input = null;
        StatusMonitorProperties properties = new StatusMonitorProperties();
        try {

            input = new FileInputStream("monitoring.properties");

            // load a properties file
            prop.load(input);

            // get the property
            boolean activated= Boolean.parseBoolean((prop.getProperty("activated")));
            int frequency = Integer.parseInt((prop.getProperty("frequency")));

            properties = new StatusMonitorProperties(activated, frequency);

        } catch (IOException ex) {
            journal.log(Level.INFO, ">> monitoring.properties not found status monitor not in use");
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
