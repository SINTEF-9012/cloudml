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
package org.cloudml.core.builders;

import org.cloudml.core.Deployment;
import org.cloudml.core.VM;

public class VMBuilder extends ExternalComponentBuilder<VM, VMBuilder> {

    private int minRam;
    private int minCores;
    private int minStorage;
    private String location;
    private String os;
    private String groupName;
    private String sshKey;
    private String securityGroup;
    private boolean os64bit;
    private String imageId;

    public VMBuilder() {
        minRam = VM.DEFAULT_MIN_RAM;
        minCores = VM.DEFAULT_MIN_CORES;
        minStorage = VM.DEFAULT_MIN_STORAGE;
        location = VM.DEFAULT_LOCATION;
        os = VM.DEFAULT_OS;
        groupName = VM.DEFAULT_GROUP_NAME;
        sshKey = VM.DEFAULT_SSH_KEY;
        securityGroup = VM.DEFAULT_SECURITY_GROUP;
        os64bit = VM.DEFAULT_64_OS; 
        imageId = VM.DEFAULT_IMAGE_ID;
    }

    @Override
    public VM build() {
        final VM result = new VM(getName(), createStubProvider());
        prepare(result);
        return result;
    }

    public VMBuilder withMinRam(int minRam) {
        this.minRam = minRam;
        return next();
    }

    public VMBuilder withMinCores(int minCores) {
        this.minCores = minCores;
        return next();
    }

    public VMBuilder withMinStorage(int storage) {
        this.minStorage = storage;
        return next();
    }

    public VMBuilder withLocation(String location) {
        this.location = location;
        return next();
    }
    
    public VMBuilder withGroupName(String groupName) {
        this.groupName = groupName;
        return next();
    }
        
    public VMBuilder withOS(String os) {
        this.os = os;
        return next();
    }
    
    public VMBuilder withSshKey(String sshKey) {
        this.sshKey = sshKey;
        return next();
    }
    
    public VMBuilder withSecurityGroup(String secruityGroup) {
        this.securityGroup = secruityGroup;
        return next();
    }
    
    public VMBuilder with64OS() {
        this.os64bit = true;
        return next();
    }
    
    public VMBuilder withImageId(String imageId) {
        this.imageId = imageId;
        return next();
    }

    @Override
    protected VMBuilder next() {
        return this;
    }

    @Override
    public void integrateIn(Deployment container) {
        final VM result = new VM(getName(), findProvider(container));
        prepare(result);
        container.getComponents().add(result);
    }

    protected void prepare(VM underConstruction) {
        super.prepare(underConstruction);
        underConstruction.setMinRam(minRam);
        underConstruction.setMinCores(minCores);
        underConstruction.setMinStorage(minStorage);
        underConstruction.setLocation(location);
        underConstruction.setOs(os);
        underConstruction.setGroupName(groupName);
        underConstruction.setSshKey(sshKey);
        underConstruction.setSecurityGroup(securityGroup);
        underConstruction.setIs64os(os64bit);
        underConstruction.setImageId(imageId);
    }
}
