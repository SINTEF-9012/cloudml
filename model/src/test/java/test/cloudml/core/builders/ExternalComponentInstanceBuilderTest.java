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

package test.cloudml.core.builders;

import org.cloudml.core.Component;
import org.cloudml.core.ComponentInstance;
import org.cloudml.core.ExternalComponent;
import org.cloudml.core.ExternalComponentInstance;
import org.cloudml.core.builders.ComponentInstanceBuilder;
import org.cloudml.core.builders.ExternalComponentInstanceBuilder;


public abstract class ExternalComponentInstanceBuilderTest extends ComponentInstanceBuilderTest {

    @Override
    public final ComponentInstanceBuilder<? extends ComponentInstance<? extends Component>, ? extends ComponentInstanceBuilder<?, ?>> aSampleComponentInstanceBuilder() {
        return aSampleExternalComponentInstanceBuilder();  
    } 

    public abstract ExternalComponentInstanceBuilder<? extends ExternalComponentInstance<? extends ExternalComponent>, ? extends ExternalComponentInstanceBuilder<?, ?>> aSampleExternalComponentInstanceBuilder();
    
}
