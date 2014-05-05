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

import org.cloudml.core.Deployment;
import org.cloudml.core.ExternalComponent;
import org.cloudml.core.NamedElement;
import org.cloudml.core.Provider;

import static org.cloudml.core.builders.Commons.*;

public class ExternalComponentBuilder<T extends ExternalComponent, N extends ExternalComponentBuilder<?, ?>> extends ComponentBuilder<T, N> {

    private String providerName;

    public ExternalComponentBuilder() {
        this.providerName = NamedElement.DEFAULT_NAME;
    }

    public N providedBy(String providerName) {
        this.providerName = providerName;
        return next();
    }

    @Override
    public T build() {
        final ExternalComponent result = new ExternalComponent(getName(), createStubProvider());
        prepare(result);
        return (T) result;
    }

    protected final Provider createStubProvider() {
        return aProvider()
                .named(providerName)
                .build();
    }

    @Override
    protected N next() {
        return (N) this;
    }

    @Override
    public void integrateIn(Deployment container) {
        final ExternalComponent result = new ExternalComponent(getName(), findProvider(container));
        prepare(result);
        container.getComponents().add(result);
    }

    protected Provider findProvider(Deployment container) {
        final Provider provider = container.getProviders().firstNamed(providerName);
        if (provider == null) {
            final String error = String.format("Unable to find the provider named '%s'", providerName);
            throw new IllegalStateException(error);
        }
        return provider;
    }

}
