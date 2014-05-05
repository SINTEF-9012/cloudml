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
package org.cloudml.core;

import java.util.*;
import org.cloudml.core.collections.PropertyGroup;
import org.cloudml.core.validation.Report;
import org.cloudml.core.visitors.Visitor;

public class ProvidedExecutionPlatform extends ExecutionPlatform {

    public static final Collection<Property> NO_OFFER = new LinkedList<Property>();
    private final PropertyGroup offers;

    public ProvidedExecutionPlatform(String name) {
        this(name, NO_OFFER);
    }

    public ProvidedExecutionPlatform(String name, Collection<Property> offers) {
        super(name);
        this.offers = new PropertyGroup(offers);
    }    
    
    
    public boolean canHost(InternalComponent component) {
        return offers.containsAll(component.getRequiredExecutionPlatform().getDemands());
    }

    @Override
    public void validate(Report report) {
        super.validate(report);
        if (offers.isEmpty()) {
            report.addWarning("Provided execution platform with no offer (will always match any required execution platform)");
        }
    }

    @Override
    public ProvidedExecutionPlatformInstance instantiate() {
        return new ProvidedExecutionPlatformInstance(getName(), this);
    }

    public PropertyGroup getOffers() { 
        return this.offers;
    }

    public boolean match(RequiredExecutionPlatform platform) {
        return getOffers().containsAll(platform.getDemands());
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitProvidedExecutionPlatform(this);
    }
}
