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
package test.cloudml.core;

import junit.framework.TestCase;
import org.cloudml.core.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


/**
 * Unit test for simple App.
 */
@RunWith(JUnit4.class)
public class ModelTest extends TestCase {
    //TODO: test propagation of demands and offers for internal components
    
    @Test(expected=IllegalArgumentException.class)
    public void testExecuteInstanceOnProvidedNull(){
        ExecuteInstance ei=new ExecuteInstance("foo", null, new RequiredExecutionPlatformInstance());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testExecuteInstanceOnRequiredNull(){
        ExecuteInstance ei=new ExecuteInstance("foo", new ProvidedExecutionPlatformInstance(), null);
    }

    @Test
    public void testLegalButEmptyExecuteOn() {
        InternalComponent ic = new InternalComponent("type C");
        InternalComponentInstance component = ic.instantiates("internal instance");

        RequiredExecutionPlatform requiredExecutionPlatform = new RequiredExecutionPlatform("War Container");
        RequiredExecutionPlatformInstance requiredExecutionPlatformInstance = requiredExecutionPlatform.instantiates("repi");

        ExternalComponent c = new ExternalComponent("External Component");
        ExternalComponentInstance<ExternalComponent> instance2 = c.instantiates("external instance");
        ProvidedExecutionPlatform providedExecutionPlatform = new ProvidedExecutionPlatform("Linux");
        ProvidedExecutionPlatformInstance providedExecutionPlatformInstance = providedExecutionPlatform.instantiates("pepi");

        ExecuteInstance executeInstance = new ExecuteInstance("illegal", providedExecutionPlatformInstance, requiredExecutionPlatformInstance);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testIllegalExecuteOn() {
        InternalComponent ic = new InternalComponent("type C");
        InternalComponentInstance component = ic.instantiates("internal instance");

        RequiredExecutionPlatform requiredExecutionPlatform = new RequiredExecutionPlatform("War Container");
        requiredExecutionPlatform.addDemand("type", "War Container");
        RequiredExecutionPlatformInstance requiredExecutionPlatformInstance = requiredExecutionPlatform.instantiates("repi");

        ExternalComponent c = new ExternalComponent("External Component");
        ExternalComponentInstance<ExternalComponent> instance2 = c.instantiates("external instance");
        ProvidedExecutionPlatform providedExecutionPlatform = new ProvidedExecutionPlatform("Linux");
        providedExecutionPlatform.addOffer("type", "Linux");
        ProvidedExecutionPlatformInstance providedExecutionPlatformInstance = providedExecutionPlatform.instantiates("pepi");

        ExecuteInstance executeInstance = new ExecuteInstance("illegal", providedExecutionPlatformInstance, requiredExecutionPlatformInstance);
    }

}
