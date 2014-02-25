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

import java.util.List;

public class VM extends ExternalComponent {

	public VM() {
	}

	public VM(String name) {
		super(name);
	}

	public VM(String name, Provider provider) {
		super(name,provider);
	}

	public VM(String name, List<Property> properties) {
		super(name, properties);
	}

	public VM(String name, List<Property> properties, Provider provider) {
		super(name, properties,provider);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof VM) {
			VM otherVM = (VM) other;
			return name.equals(otherVM.getName()) && this.provider.equals(otherVM.getProvider());
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "VM " + name + "(minRam: " + minRam + ", minCores: " + minCores + ", minStorage: " + minStorage + ")";
	}
	/*
	 * NODE Configuration
	 */
	private int minRam = 0;
    private int maxRam = 0;
	private int minCores = 0;
    private int maxCores = 0;
	private int minStorage = 0;
    private int maxStorage = 0;
	private String location = "";
	private String os = "";
	private String sshKey = "";
	private String securityGroup = "";
	private String groupName = "";
	private String privateKey = "";
	private Boolean is64os = true;
	private String imageId = "";

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

	/*
	 * Setters
	 */
	public void setMinRam(int minRam) { this.minRam = minRam;	}

    public void setMaxRam(int maxRam) { this.minRam = maxRam;	}

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

	public VMInstance instantiates(String name) {
		return new VMInstance(name, this);
	}

	public VMInstance instantiates() {
		return new VMInstance("", this);
	}
}
