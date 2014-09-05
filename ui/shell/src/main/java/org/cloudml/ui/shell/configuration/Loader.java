/**
 * This file is part of CloudML [ http://cloudml.org ]
 *
 * Copyright (C) 2012 - SINTEF ICT Contact: Franck Chauvel
 * <franck.chauvel@sintef.no>
 *
 * Module: root
 *
 * CloudML is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * CloudML is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CloudML. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.cloudml.ui.shell.configuration;

import java.io.InputStream;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

/**
 * Load the configuration of the CloudML Shell. This is a singleton, as the
 * configuration of the shell is shared among all its component.
 */
public class Loader {

    private static final String CONFIGURATION_FILE = "configuration.yaml";

    private static Loader instance;

    public static Loader getInstance() {
        if (instance == null) {
            instance = new Loader();
        }
        return instance;
    }

    private final Configuration configuration;

    /**
     * Create the instance of the Loader and pre-fetch the configuration.
     */
    private Loader() {
        Yaml yaml = new Yaml(new Constructor(Configuration.class));
        InputStream input = Loader.class.getClassLoader().getResourceAsStream(CONFIGURATION_FILE);
        configuration = (Configuration) yaml.load(input);
    }

    /**
     * @return the configuration of the CloudML Shell
     */
    public Configuration getConfiguration() {
        return configuration;
    }

}
