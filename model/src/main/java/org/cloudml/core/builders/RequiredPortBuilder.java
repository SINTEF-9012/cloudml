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
/*
 */
package org.cloudml.core.builders;

import org.cloudml.core.InternalComponent;
import org.cloudml.core.RequiredPort;

public class RequiredPortBuilder extends PortBuilder<RequiredPort, RequiredPortBuilder> {

    private boolean optional;

    public RequiredPortBuilder() {
        super();
        this.optional = RequiredPort.MANDATORY;
    }

    public RequiredPortBuilder optional() {
        this.optional = RequiredPort.OPTIONAL;
        return next();
    }

    public RequiredPortBuilder mandatory() {
        this.optional = RequiredPort.MANDATORY;
        return next();
    }

    @Override
    protected RequiredPortBuilder next() {
        return this;
    }

    @Override
    public RequiredPort build() {
        RequiredPort result = new RequiredPort(getName(), isLocal(), optional);
        prepare(result);
        return result;
    }

    public void integrateIn(InternalComponent container) {
        RequiredPort result = new RequiredPort(getName(), isLocal(), optional);
        prepare(result);
        result.getOwner().set(container);
        container.getRequiredPorts().add(result);
    }
}
