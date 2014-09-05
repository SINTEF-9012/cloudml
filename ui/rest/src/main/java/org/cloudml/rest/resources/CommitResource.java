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

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.cloudml.deployer.CloudAppDeployer;
import org.cloudml.mrt.Coordinator;
import org.cloudml.mrt.cmd.gen.CloudMLCmds;
import org.cloudml.mrt.cmd.abstracts.Modification;
import org.cloudml.mrt.cmd.gen.Commit;
import org.cloudml.rest.RestDaemon;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author Hui Song
 */
@Path("commit")
@Produces(MediaType.TEXT_PLAIN)
public class CommitResource {
    
    private static final Logger journal = Logger.getLogger(CloudAppDeployer.class.getName());
    
    private Coordinator coord = Coordinator.SINGLE_INSTANCE;
    
    @POST
    public String executeCommit(String commands){
        try{
            Commit commit = new Commit();
            commit.modifications = new ArrayList<Modification>();
            Yaml yaml = CloudMLCmds.INSTANCE.getYaml();

            Object obj =  yaml.load(commands);

            if(obj instanceof Modification){
                commit.modifications.add((Modification) obj);
            }
            else if(obj instanceof Collection){
                for(Object sobj : (Collection) obj){
                    if(sobj instanceof Modification){
                        commit.modifications.add((Modification)sobj);
                    }
                    else
                        return "Invalid command format";
                }
            }
            else
                return "Invalid command format";

            coord.process(commit, RestDaemon.commonStub);
            return "Command succeeded without return values";
        }
        catch(Exception e){
            e.printStackTrace();
            return "Sorry, I failed. "+ e;
        }
        
    }
}
