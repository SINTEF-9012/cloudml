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
package org.cloudml.deployer2.dsl;

import java.util.HashMap;

/**
 * Created by Maksym on 16.03.2015.
 */
abstract public class Element {
    public static final String DEFAULT_NAME = "empty";
    public enum Status {INACTIVE, ACTIVE, DONE}

    private String name;
    private String elementID;
    private HashMap<String, String> properties = new HashMap<String, String>();

    public Element() {
        setName(DEFAULT_NAME);
        getProperties().put("Status", String.valueOf(Status.INACTIVE));
    }

    public Element(String name) {
        setName(name);
        getProperties().put("Status", String.valueOf(Status.INACTIVE));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getElementID() {
        return elementID;
    }

    public void setElementID(String elementID) {
        this.elementID = elementID;
    }

    public HashMap<String, String> getProperties() {
        return properties;
    }

    public void setProperties(HashMap<String, String> properties) {
        this.properties = properties;
    }
}
