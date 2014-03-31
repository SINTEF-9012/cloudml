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

package org.cloudml.core;


public class ProviderGroup extends NamedElementGroup<Provider> {
    
    public ProviderGroup(DeploymentModel context) {
        super(context);
    }

    @Override
    protected void abortIfCannotBeAdded(Provider provider) {
       if (named(provider.getName()) != null) {
            final String message = String.format("A provider named '%s' already exists", provider.getName());
            throw new IllegalArgumentException(message);
        }
    }

    @Override
    protected void abortIfCannotBeRemoved(Provider provider) {
         if (getContext().isUsed(provider)) {
            String message = String.format("Unable to remove provider '%s' as it still provides nodes", provider.getName());
            throw new IllegalStateException(message);
        }
   }
    
}
