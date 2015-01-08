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

import org.cloudml.core.collections.VMInstanceGroup;
import org.cloudml.core.validation.CanBeValidated;
import org.cloudml.core.visitors.Visitable;
import org.cloudml.core.visitors.Visitor;

public class VM extends ExternalComponent implements Visitable, CanBeValidated {
    public static final int DEFAULT_MIN_RAM = 1000;
    public static final int DEFAULT_MIN_CORES = 1;
    public static final int DEFAULT_MIN_STORAGE = 25;
    public static final String DEFAULT_LOCATION = "";
    public static final String DEFAULT_OS = "";
    public static final String DEFAULT_GROUP_NAME = "";
    public static final String DEFAULT_SSH_KEY = "";
    public static final String DEFAULT_SECURITY_GROUP = "";
    public static final boolean DEFAULT_64_OS = true;
    public static final String DEFAULT_IMAGE_ID = "";

    public VM(String name, Provider provider) {
        super(name, provider);
        minRam = DEFAULT_MIN_RAM;
        minCores = DEFAULT_MIN_CORES;
        minStorage = DEFAULT_MIN_STORAGE;
        location = DEFAULT_LOCATION;
        os = DEFAULT_OS;
        groupName = DEFAULT_GROUP_NAME;
        sshKey = DEFAULT_SSH_KEY;
        securityGroup = DEFAULT_SECURITY_GROUP;
        is64os = DEFAULT_64_OS;
        imageId = DEFAULT_IMAGE_ID;
    }

    @Override
    public final boolean isVM() {
        return true;
    }
       

    @Override
    public void accept(Visitor visitor) {
        visitor.visitVM(this);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other instanceof VM) {
            VM otherNode = (VM) other;
            return super.equals(otherNode);
        }
        return false;
    }

    @Override
    public String toString() {
        return "VM " + getName() + "(minRam: " + minRam + ", minCores: " + minCores + ", minStorage: " + minStorage + ")";
    }
    /*
     * NODE Configuration
     */
    private int minRam;
    private int maxRam = 0;
    private int minCores;
    private int maxCores = 0;
    private int minStorage;
    private int maxStorage = 0;
    private String location;
    private String os;
    private String sshKey;
    private String securityGroup;
    private String groupName;
    private String privateKey = "";
    private Boolean is64os;
    private String imageId;
    private String providerSpecificTypeName;

    /*
     * getters
     */
    public int getMinRam() {
        return minRam;
    }

    public int getMaxRam() {
        return maxRam;
    }

    public int getMinCores() {
        return minCores;
    }

    public int getMaxCores() {
        return maxCores;
    }

    public int getMinStorage() {
        return minStorage;
    }

    public int getMaxStorage() {
        return maxStorage;
    }

    public String getLocation() {
        return location;
    }

    public String getOs() {
        return os;
    }

    public String getSshKey() {
        return sshKey;
    }

    public String getSecurityGroup() {
        return securityGroup;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public Boolean getIs64os() {
        return is64os;
    }

    public String getImageId() {
        return imageId;
    }

    public String getProviderSpecificTypeName(){
        return providerSpecificTypeName;
    }

    /*
     * Setters
     */
    public void setMinRam(int minRam) {
        this.minRam = minRam;
    }

    public void setMaxRam(int maxRam) {
        this.minRam = maxRam;
    }

    public void setMinCores(int minCores) {
        this.minCores = minCores;
    }

    public void setMaxCores(int maxCores) {
        this.maxCores = maxCores;
    }

    public void setMinStorage(int minStorage) {
        this.minStorage = minStorage;
    }

    public void setMaxStorage(int maxStorage) {
        this.maxStorage = maxStorage;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public void setSshKey(String sshKey) {
        this.sshKey = sshKey;//if we cannot access the file, we assume the key is directly passed
    }

    public void setSecurityGroup(String securityGroup) {
        this.securityGroup = securityGroup;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public void setIs64os(Boolean is64os) {
        this.is64os = is64os;
    }

    public void setImageId(String id) {
        imageId = id;
    }

    public void setProviderSpecificTypeName(String providerSpecificTypeName){
        this.providerSpecificTypeName = providerSpecificTypeName;
    }

    @Override
    public VMInstance instantiates(String name) {
        return new VMInstance(name, this);
    }

    public VMInstance instantiates() {
        return new VMInstance(this);
    }

    @Override
    public boolean isProvidedBy(Provider provider) {
        return getProvider().equals(provider);
    }

    public boolean hasAnyInstance() {
        return !getInstances().isEmpty();
    }

    public VMInstanceGroup getInstances() {
        if (getOwner().isDefined()) {
            return getDeployment().getComponentInstances().onlyVMs().ofType(this);
        }
        else {
            return new VMInstanceGroup();
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }
}
