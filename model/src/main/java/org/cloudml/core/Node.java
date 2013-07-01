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

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

public class Node extends WithProperties {

	private Provider cloudProvider;

	public Node() {
	}

	public Node(String name) {
		super(name);
	}

	public Node(String name, Provider provider) {
		super(name);
		this.cloudProvider = provider;
	}

	public Node(String name, List<Property> properties) {
		super(name, properties);
	}

	public Node(String name, List<Property> properties, Provider provider) {
		super(name, properties);
		this.cloudProvider = provider;
	}

	public Provider getProvider() {
		return cloudProvider;
	}

	public void setProvider(Provider p) {
		cloudProvider = p;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Node) {
			Node otherNode = (Node) other;
			return name.equals(otherNode.getName()) && cloudProvider.equals(otherNode.getProvider());
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "Node " + name + "(minRam: " + minRam + ", minCore: " + minCore + ", minDisk: " + minDisk + ")";
	}
	/*
	 * NODE Configuration
	 */
	private int minRam = 0;
	private int minCore = 0;
	private int minDisk = 0;
	private String location = "";
	private String OS = "";
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

	public int getMinCore() {
		return minCore;
	}

	public int getMinDisk() {
		return minDisk;
	}

	public String getLocation() {
		return location;
	}

	public String getOS() {
		return OS;
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
	public void setMinRam(int minRam) {
		this.minRam = minRam;
	}

	public void setMinCore(int minCore) {
		this.minCore = minCore;
	}

	public void setMinDisk(int minDisk) {
		this.minDisk = minDisk;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setOS(String OS) {
		this.OS = OS;
	}

	public void setSshKey(String sshKey) {
		this.sshKey = sshKey;
	}

	public void setSecurityGroup(String securityGroup) {
		this.securityGroup = securityGroup;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setPrivateKey(String privateKey) {
		try {
			String contentKey=FileUtils.readFileToString(new File(privateKey));
		} catch (IOException e) {
			this.privateKey = privateKey;
		}
	}

	public void setIs64os(Boolean is64os) {
		this.is64os = is64os;
	}

	public void setImageId(String id) {
		imageId = id;
	}

	public NodeInstance instanciates(String name) {
		return new NodeInstance(name, this);
	}

	public NodeInstance instanciates() {
		return new NodeInstance("", this);
	}
}
