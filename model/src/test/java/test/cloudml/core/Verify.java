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
package test.cloudml.core;

import org.cloudml.core.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * Verification Helper
 */
public class Verify {

    public static void correctComponentInstantiation(Component type, ComponentInstance<? extends Component> instance) {
        assertThat("null type", type, is(not(nullValue())));
        assertThat("null instance", instance, is(not(nullValue())));
        assertThat("instance's type", instance.getType(), is(sameInstance(type)));
        verifyProvidedPorts(type, instance);
        verifyProvidedExecutionPlatforms(type, instance);
    }

    private static void verifyProvidedPorts(Component type, ComponentInstance<? extends Component> instance) {
        assertThat("instance provided port count", instance.getProvidedPorts(), hasSize(type.getProvidedPorts().size()));
        verifyProvidedPortsOwner(instance);
        for (ProvidedPort portType : type.getProvidedPorts()) {
            hasOneInstanceOf(instance, portType);
        }
    }
    
    private static void verifyProvidedPortsOwner(ComponentInstance<? extends Component> instance) {
        for(ProvidedPortInstance port: instance.getProvidedPorts()) {
            assertThat("provided port owner", port.getOwner().get() == instance);
        }
    }

    public static void hasOneInstanceOf(ComponentInstance<? extends Component> instance, ProvidedPort type) {
        ProvidedPortInstance match = null;
        for (ProvidedPortInstance port : instance.getProvidedPorts()) {
            if (port.isInstanceOf(type)) {
                match = port;
            }
        }
        assertThat("missing port instance of type " + type.getQualifiedName(),
                   match, is(not(nullValue())));
    }
    
    private static void verifyProvidedExecutionPlatforms(Component type, ComponentInstance<? extends Component> instance) {
        assertThat("provided execution platforms count", instance.getProvidedExecutionPlatforms(), hasSize(type.getProvidedExecutionPlatforms().size()));
        verifyProvidedExecutionPlatformOwner(instance); 
        for(ProvidedExecutionPlatform platformType: type.getProvidedExecutionPlatforms()) {
            hasOneInstanceOf(instance, platformType);
        }
    }
    
    public static void verifyProvidedExecutionPlatformOwner(ComponentInstance<? extends Component> instance) {
        for(ProvidedExecutionPlatformInstance platform: instance.getProvidedExecutionPlatforms()) {
            assertThat("wrong provided execution platform owner", platform.getOwner().get() == instance);
        }
    }
    
    private static void hasOneInstanceOf(ComponentInstance<? extends Component> instance, ProvidedExecutionPlatform platformType) {
        ProvidedExecutionPlatformInstance match = null;
        for (ProvidedExecutionPlatformInstance platform: instance.getProvidedExecutionPlatforms()) {
            if (platform.isInstanceOf(platformType)) { 
                match = platform;
            }
        }
        assertThat("missing execution platform instance of " + platformType.getQualifiedName(),
                   match, is(not(nullValue())));
    }

    public static void correctExternalComponentInstantiation(ExternalComponent type, ExternalComponentInstance<? extends ExternalComponent> instance) {
        correctComponentInstantiation(type, instance);
    }

    public static void correctVMInstantiation(VM type, VMInstance instance) {
        correctExternalComponentInstantiation(type, instance);
    }
    
    public static void correctInternalComponentInstance(InternalComponent type, InternalComponentInstance instance) {
        correctComponentInstantiation(type, instance);
        verifyRequiredPorts(type, instance);
        verifyRequiredExecutionPlatform(type, instance);
    }

    private static void verifyRequiredPorts(InternalComponent type, InternalComponentInstance instance) {
        assertThat("required port count", instance.getRequiredPorts(), hasSize(type.getRequiredPorts().size()));
        verifyRequiredPortOwner(instance); 
        for(RequiredPort portType: type.getRequiredPorts()) {
            hasOneInstanceOf(instance, portType);
        }
    }
    
    
    public static void verifyRequiredPortOwner(InternalComponentInstance instance) {
        for(RequiredPortInstance port: instance.getRequiredPorts()) {
            assertThat("wrong required port owner", port.getOwner().get() == instance);
        }
    }

    public static void hasOneInstanceOf(InternalComponentInstance instance, RequiredPort type) {
        RequiredPortInstance match = null;
        for (RequiredPortInstance port : instance.getRequiredPorts()) { 
            if (port.isInstanceOf(type)) {
                match = port;
            }
        }
        assertThat("missing required port instance of type " + type.getQualifiedName(),
                   match, is(not(nullValue())));
    }
    
    private static void verifyRequiredExecutionPlatform(InternalComponent type, InternalComponentInstance instance) {
        assertThat("required execution platform type", instance.getRequiredExecutionPlatform().isInstanceOf(type.getRequiredExecutionPlatform()));
        assertThat("required execution platform owner", instance == instance.getRequiredExecutionPlatform().getOwner().get());
    }
}
