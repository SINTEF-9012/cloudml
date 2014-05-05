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
package org.cloudml.core.actions;

import org.cloudml.core.Component;
import org.cloudml.core.ComponentInstance;
import org.cloudml.core.Deployment;




public class Stop extends AbstractAction<Void> {

    private ComponentInstance<? extends Component> instance;

    public Stop(StandardLibrary library, ComponentInstance<? extends Component> instance) {
        super(library);
        this.instance = instance;
    }
      
    @Override
    public Void applyTo(Deployment target) {
        // TODO: this.instance.setStatus(ComponentInstance.State.STOPPED);
        return NOTHING; 
    }      

}
