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

import org.cloudml.core.ExternalComponent;
import org.cloudml.core.VM;
import org.cloudml.core.builders.ExternalComponentBuilder;
import org.cloudml.core.builders.VMBuilder;
import org.junit.Test;

import static org.cloudml.core.builders.Commons.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class VMBuilderTest extends ExternalComponentBuilderTest {

    @Override
    public ExternalComponentBuilder<? extends ExternalComponent, ? extends ExternalComponentBuilder<?, ?>> aSampleExternalComponentBuilder() {
        return aSampleVMBuilder();
    }

    public VMBuilder aSampleVMBuilder() {
        return aVM();
    }

    @Test
    public void defaultMinRam() {
        final VM sut = aSampleVMBuilder().build();

        assertThat("default min ram", sut.getMinRam(), is(equalTo(VM.DEFAULT_MIN_RAM)));
    }

    @Test
    public void setASpecificMinRam() {
        final int minRam = 25;
        final VM sut = aSampleVMBuilder()
                .withMinRam(minRam)
                .build();

        assertThat("min ram", sut.getMinRam(), is(equalTo(minRam)));
    }

    @Test
    public void defaultMinCores() {
        final VM sut = aSampleVMBuilder().build();

        assertThat("default minimun number of cores", sut.getMinCores(), is(equalTo(VM.DEFAULT_MIN_CORES)));
    }

    @Test
    public void setASpecificMinCores() {
        final int minCores = 4;
        final VM sut = aSampleVMBuilder()
                .withMinCores(minCores)
                .build();

        assertThat("minimun number of cores", sut.getMinCores(), is(equalTo(minCores)));
    }

    @Test
    public void defaultMinStorage() {
        final VM sut = aSampleVMBuilder().build();

        assertThat("default minimun storage", sut.getMinStorage(), is(equalTo(VM.DEFAULT_MIN_STORAGE)));
    }

    @Test
    public void setASpecificMinStorage() {
        final int minStorage = 50;
        final VM sut = aSampleVMBuilder()
                .withMinStorage(minStorage)
                .build();

        assertThat("minimun storage", sut.getMinStorage(), is(equalTo(minStorage)));
    }

    @Test
    public void defaultLocation() {
        final VM sut = aSampleVMBuilder().build();

        assertThat("default location", sut.getLocation(), is(equalTo(VM.DEFAULT_LOCATION)));
    }

    @Test
    public void setASpecificLocation() {
        final String location = "my location";
        final VM sut = aSampleVMBuilder()
                .withLocation(location)
                .build();

        assertThat("location", sut.getLocation(), is(equalTo(location)));
    }

    @Test
    public void defaultOS() {
        final VM sut = aSampleVMBuilder().build();

        assertThat("default OS", sut.getOs(), is(equalTo(VM.DEFAULT_OS)));
    }

    @Test
    public void setASpecificOS() {
        final String operatingSystem = "foo os";
        final VM sut = aSampleVMBuilder()
                .withOS(operatingSystem)
                .build();

        assertThat("operating system", sut.getOs(), is(equalTo(operatingSystem)));
    }

    @Test
    public void defaultGroupName() {
        final VM sut = aSampleVMBuilder().build();

        assertThat("default Group Name", sut.getGroupName(), is(equalTo(VM.DEFAULT_GROUP_NAME)));
    }

    @Test
    public void setASpecificGroupName() {
        final String groupName = "foo";
        final VM sut = aSampleVMBuilder()
                .withGroupName(groupName)
                .build();

        assertThat("group name", sut.getGroupName(), is(equalTo(groupName)));
    }

    @Test
    public void defaultSshKey() {
        final VM sut = aSampleVMBuilder().build();

        assertThat("default ssh key", sut.getSshKey(), is(equalTo(VM.DEFAULT_SSH_KEY)));
    }

    @Test
    public void setASpecificSshKey() {
        final String sshKey = "foo";
        final VM sut = aSampleVMBuilder()
                .withSshKey(sshKey)
                .build();

        assertThat("ssh key", sut.getSshKey(), is(equalTo(sshKey)));
    }
    
   @Test
    public void defaultSecurityGroup() {
        final VM sut = aSampleVMBuilder().build();

        assertThat("default security group", sut.getSecurityGroup(), is(equalTo(VM.DEFAULT_SECURITY_GROUP))); 
    }

    @Test
    public void setASpecificSecurityGroup() {
        final String securityGroup = "foo";
        final VM sut = aSampleVMBuilder()
                .withSecurityGroup(securityGroup)
                .build();

        assertThat("secruity group", sut.getSecurityGroup(), is(equalTo(securityGroup)));
    }
    
    
    @Test
    public void default64Os() {
        final VM sut = aSampleVMBuilder().build();

        assertThat("default security group", sut.getIs64os(), is(equalTo(VM.DEFAULT_64_OS))); 
    }

    @Test
    public void setASpecific64Os() {
        final VM sut = aSampleVMBuilder()
                .with64OS() 
                .build();

        assertThat("secruity group", sut.getIs64os());
    }
    
    
    @Test
    public void defaultImageId() {
        final VM sut = aSampleVMBuilder().build();

        assertThat("default image id", sut.getImageId(), is(equalTo(VM.DEFAULT_IMAGE_ID)));  
    }

    @Test
    public void setASpecificImageId() {
        final String imageId = "foo";
        final VM sut = aSampleVMBuilder()
                .withImageId(imageId) 
                .build();

        assertThat("secruity group", sut.getImageId(), is(equalTo(imageId)));
    }
}
