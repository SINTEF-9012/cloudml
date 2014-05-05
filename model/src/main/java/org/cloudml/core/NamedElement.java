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

import org.cloudml.core.validation.CanBeValidated;
import org.cloudml.core.validation.Report;

public abstract class NamedElement implements CanBeValidated {

    public static final String DEFAULT_NAME = "no name";
    
    private String name;

    public NamedElement() {
        this.name = DEFAULT_NAME;
    }

    public NamedElement(String name) {
        rejectInvalidName(name);
        this.name = name; 
    }

    private void rejectInvalidName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("'null' is not a valid name");
        }
        if (name.equals("")) {
            throw new IllegalArgumentException("The empty String ('') is not a valid name");
        }
    }

    public String getName() {
        return this.name;
    }
    
    public boolean isNamed(String name) {
        return this.name.equals(name);
    }

    public void setName(String name) {
        rejectInvalidName(name);
        this.name = name;
    }

    @Override
    public final Report validate() {
        final Report report = new Report();
        validate(report);
        return report;
    }

    @Override
    public void validate(Report report) {
        if (name.equals(DEFAULT_NAME)) {
            final String message = String.format("%s has no name (found '%s)", this.getClass().getName(), getName());
            report.addWarning(message);
        }
    }
    
    
    
}
