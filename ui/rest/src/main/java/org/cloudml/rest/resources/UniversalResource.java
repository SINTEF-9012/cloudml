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
package org.cloudml.rest.resources;


import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.cloudml.mrt.Coordinator;
import org.cloudml.rest.RestDaemon;

/**
 *
 * @author Hui Song
 */
@Path("")
@Produces(MediaType.TEXT_PLAIN)
public class UniversalResource {
    
    private Coordinator coord = Coordinator.SINGLE_INSTANCE;
    
    @POST
    @Path("")
    public String executeInstructions(String instText){
        try{
            String result = coord.process(instText, RestDaemon.commonStub);
            
            if(result == null || result.length()==0){
                return "Command succeeded, without return values";
            }
            else
                return result;
        }
        catch(Exception e){
            e.printStackTrace();
            return "Sorry I failed! " + e;
        }
    }
    
}
