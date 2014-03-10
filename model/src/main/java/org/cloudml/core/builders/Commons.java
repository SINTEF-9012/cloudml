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

package org.cloudml.core.builders;

/**
 * Ease importing all static factories used for building deployment models
 */
public class Commons {
    
    public static DeploymentModelBuilder aDeployment() {
        return new DeploymentModelBuilder();
    }

    public static ProviderBuilder aProvider() {
        return new ProviderBuilder();
    }
    
    public static NodeBuilder aNode() {
        return new NodeBuilder();
    }
    
    public static ArtefactBuilder anArtefact() {
        return new ArtefactBuilder(); 
    }
    
    public static ClientPortBuilder aClientPort() {
        return new ClientPortBuilder();
    }
    
    public static ServerPortBuilder aServerPort() {
        return new ServerPortBuilder();
    }
    
    public static BindingBuilder aBinding() {
        return new BindingBuilder();
    }
    
    public static NodeInstanceBuilder aNodeInstance() {
        return new NodeInstanceBuilder();
    }
   
    public static ArtefactInstanceBuilder anArtefactInstance() {
        return new ArtefactInstanceBuilder();
    }
    
    public static BindingInstanceBuilder aBindingInstance() {
        return new BindingInstanceBuilder();
    }
    
    public static ClientPortInstanceBuilder aClientPortInstance() {
        return new ClientPortInstanceBuilder(); 
    }
    
    public static ServerPortInstanceBuilder aServerPortInstance() {
        return new ServerPortInstanceBuilder();
    }
    
}
