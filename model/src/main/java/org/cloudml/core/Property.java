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

import java.sql.Timestamp;

public class Property extends NamedElement {

    public static final String UNDEFINED = null;
    public static final String CONCAT_IDENTIFIER = "_";
    private String value;

    public Property(String name) {
        this(name, UNDEFINED);
    }

    public Property(String name, String value) {
        super(name);
        this.value = value;
    }

    public Property(String name, String value, Timestamp timestamp){
        super(name);
        this.value = value+ CONCAT_IDENTIFIER +timestamp.getTime();
    }

    public String getValue() {
        return value;
    }

    public Timestamp getTimestamp(){
        int index= value.lastIndexOf(CONCAT_IDENTIFIER);
        String timestamp = value.substring(index+1);
        return (new Timestamp(Long.valueOf(timestamp)));
    }

    public String getValueWithTimestamp(){
        int index= value.lastIndexOf(CONCAT_IDENTIFIER);
        return value.substring(0,index);
    }

    public boolean hasValue() {
        return value != UNDEFINED;
    }

    public boolean hasValue(String value) {
        return this.value.equals(value);
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setValue(String value, Timestamp timestamp){
        this.value = value+ CONCAT_IDENTIFIER +timestamp.getTime();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other instanceof Property) {
            Property p = (Property) other;
            return isNamed(p.getName()) && (p.hasValue() && hasValue(p.getValue()));
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("(%s = %s)", getName(), getValue());
    }

  }
