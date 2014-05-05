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
package test.cloudml.core.builders;

import org.cloudml.core.Port;
import org.cloudml.core.WithResources;
import org.cloudml.core.builders.PortBuilder;
import org.cloudml.core.builders.WithResourcesBuilder;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public abstract class PortBuilderTest extends WithResourcesBuilderTest {

    @Override
    public final WithResourcesBuilder<? extends WithResources, ? extends WithResourcesBuilder<?, ?>> aSampleWithResourceBuilder() {
        return aSamplePortBuilder();
    }

    public abstract PortBuilder<? extends Port, ? extends PortBuilder<?, ?>> aSamplePortBuilder();

    @Test
    public void buildAsLocalPort() {
        final Port sut = aSamplePortBuilder()
                .local()
                .build();

        assertThat("is local", sut.isLocal());
    }

    @Test
    public void buildAsRemotePort() {
        final Port sut = aSamplePortBuilder()
                .remote()
                .build();

        assertThat("is remote", sut.isRemote());
    }
    
    @Test
    public void setDefaultPortNumber() {
        final Port sut = aSamplePortBuilder().build();
        
        assertThat("default port number", sut.getPortNumber(), is(equalTo(Port.DEFAULT_PORT_NUMBER)));
    }
    
    @Test
    public void setSpecificPortNumber() {
        final int portNumber = 8080;
        final Port sut = aSamplePortBuilder()
                .withPortNumber(portNumber) 
                .build();        
        
        assertThat("port number", sut.getPortNumber(), is(equalTo(portNumber)));
    }
}
