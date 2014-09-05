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

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.core.Deployment;
import org.cloudml.mrt.Coordinator;
import org.cloudml.mrt.cmd.abstracts.XPath;
import org.cloudml.mrt.cmd.gen.CloudMLCmds;
import org.cloudml.mrt.cmd.gen.GetSnapshot;
import org.cloudml.mrt.cmd.gen.Snapshot;
import org.cloudml.rest.RestDaemon;

/**
 *
 * @author Hui Song
 */
@Path("snapshot")
@Produces(MediaType.TEXT_PLAIN)
public class QueryResource {
    private Coordinator coord = Coordinator.SINGLE_INSTANCE;
    
    @GET
    public String getSnapshot(@QueryParam("path") String path, @QueryParam("codec") String codec){
        
        try{
            System.out.println("GetSnapshot: " + path);
            GetSnapshot cmd = new GetSnapshot();
            if(path == null || path.length()==0)
                path = "/";

            cmd.path = new XPath(path);

            return this.codec(coord.process(cmd, RestDaemon.commonStub), codec);
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    public String codec(Object object, String codec) {
        if(codec!=null && "text".equals(codec.toLowerCase())){
            return object.toString();
        }
        if(object instanceof Deployment){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            new JsonCodec().save((Deployment) object, baos);
            try {
                return baos.toString("UTF-8");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(QueryResource.class.getName()).log(Level.SEVERE, null, ex);
                return ex.getLocalizedMessage();
            }
            
        }
        else if(object instanceof Collection){
            List list = new ArrayList();
            list.addAll((Collection)object);
            return CloudMLCmds.INSTANCE.getYaml().dump(list);
        }
        else{            
            return CloudMLCmds.INSTANCE.getYaml().dump(object);
        }
    }
    
}
