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
package org.cloudml.core.builders;

import java.util.HashMap;
import org.cloudml.core.Resource;

public class ResourceBuilder extends WithPropertyBuilder<Resource, ResourceBuilder> {

    private String install;
    private String configure;
    private String start;
    private String stop;
    private String retrieve;
    private HashMap<String, String> uploads;

    public ResourceBuilder() {
        install = "";
        configure = "";
        start = "";
        stop = "";
        retrieve = "";
        uploads = new HashMap<String, String>();
    }

    public ResourceBuilder installedBy(String command) {
        this.install = command;
        return next();
    }

    public ResourceBuilder startedBy(String command) {
        start = command;
        return next();
    }

    public ResourceBuilder stoppedBy(String command) {
        stop = command;
        return next();
    }

    public ResourceBuilder retrievedBy(String command) {
        retrieve = command;
        return next();
    }

    public ResourceBuilder configuredBy(String command) {
        configure = command;
        return next();
    }

    public ResourceBuilder withUpload(String from, String to) {
        return next();
    }

    @Override
    public Resource build() {
        final Resource resource = new Resource();
        super.prepare(resource);
        resource.setStartCommand(start);
        resource.setStopCommand(stop);
        resource.setConfigureCommand(configure);
        resource.setInstallCommand(install);
        resource.setRetrieveCommand(retrieve);
        resource.setUploadCommand(uploads);
        return resource;
    }

    @Override
    public ResourceBuilder next() {
        return this;
    }
}
