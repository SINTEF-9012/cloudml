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
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cloudml.connectors.util;

import org.cloudml.core.RelationshipInstance;
import org.cloudml.core.Resource;



/**
 * As there is no default configuration methods for PaaS (in contrary, we can
 * execute shell scripts via JCloud on IaaS), we provide a set of pre-defined 
 * configuration agents, each of which works on a given {@link RelationshipInstance}
 * and one of its {@link Resource}s. 
 * 
 * The Valets (acting as a kid hired by a restaurant to help customers park their cars}
 * accepts some properties from a relationship, and performs the configurations
 * to enable the two components to communicate.
 * @author Hui Song
 */
public abstract class ConfigValet {
    
    
    public abstract void config();
    
    public static ConfigValet createValet(RelationshipInstance relation, Resource resource){

        String type = resource.getProperties().valueOf("valet");
        if("war-xml".equals(type))
            return new WarXmlConfigValet(relation, resource);
        
        return null;
    }

}
