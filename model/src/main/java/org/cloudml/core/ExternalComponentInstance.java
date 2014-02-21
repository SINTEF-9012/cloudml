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

/**
 * Created by Nicolas Ferry on 19.02.14.
 */
public class ExternalComponentInstance extends ComponentInstance{

    public ExternalComponentInstance(String name, ExternalComponent type){
        super(name,type);
    }

    @Override
    public String toString() {
        return "Instance " + name + " : " + getType().getName();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof InternalComponentInstance) {
            ExternalComponentInstance otherCompInst = (ExternalComponentInstance) other;
            Boolean match= name.equals(otherCompInst.getName()) && type.equals(otherCompInst.getType());
            return match;
        } else {
            return false;
        }
    }

}
