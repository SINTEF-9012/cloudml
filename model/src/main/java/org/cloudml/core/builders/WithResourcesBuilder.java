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

import java.util.ArrayList;
import org.cloudml.core.WithResources;

public abstract class WithResourcesBuilder<T extends WithResources, N extends WithResourcesBuilder<?, ?>> extends WithPropertyBuilder<T, N> {

    private final ArrayList<ResourceBuilder> resources;

    protected void prepare(WithResources underConstruction) {
        super.prepare(underConstruction);
        setupResources(underConstruction);
    }

    public WithResourcesBuilder() {
        this.resources = new ArrayList<ResourceBuilder>();
    }

    public N withResource(ResourceBuilder builder) {
        this.resources.add(builder);
        return next();
    }

    protected void setupResources(WithResources underConstruction) {
        for (ResourceBuilder resource : resources) {
            underConstruction.getResources().add(resource.build());
        }
    }
}
