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
package org.cloudml.core.util;

import org.cloudml.core.NamedElement;

public interface OwnedBy<O extends NamedElement> {

    public static final String NO_OWNER_MARK = "??";
    public static final String CONTAINED = "::";

    public String getQualifiedName();

    public OptionalOwner<O> getOwner();
    
    public static class OptionalOwner<O extends NamedElement> extends Optional<O> {
        
        public String getName() {
            return isDefined() ? this.get().getName() : NO_OWNER_MARK;
        }
    }
}