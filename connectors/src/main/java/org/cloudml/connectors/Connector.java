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
package org.cloudml.connectors;

import org.cloudml.core.ComponentInstance;
import org.cloudml.core.VMInstance;
import org.cloudml.mrt.Coordinator;

import java.util.HashMap;

public interface Connector {
	
	public void execCommand(String id, String command, String login, String key);
	
	public HashMap<String,String> createInstance(VMInstance a);
	
	public void destroyVM(String id);
	
	public void closeConnection();

	public void updateVMMetadata(VMInstance a);
	
	public void uploadFile(String sourcePath, String destinationPath, String nodeId, String login, String key);

    public String createSnapshot(VMInstance a);

    public String createImage(VMInstance a);
	
}
