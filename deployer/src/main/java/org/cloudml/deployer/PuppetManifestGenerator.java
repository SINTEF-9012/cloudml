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
package org.cloudml.deployer;

import org.cloudml.core.*;
import org.cloudml.core.collections.InternalComponentInstanceGroup;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by nicolasf on 15.10.14.
 */
public class PuppetManifestGenerator {

    private static final Logger journal = Logger.getLogger(PuppetManifestGenerator.class.getName());
    private VMInstance vmi;
    private Deployment currentModel;
    private String manifest="";
    private PuppetResource skeleton=null;


    public PuppetManifestGenerator(VMInstance vmi, Deployment current){
        this.vmi=vmi;
        this.currentModel =current;
    }

    public PuppetResource getSkeleton(){
        return this.skeleton;
    }

    private PuppetResource findPuppetResource(ComponentInstance ci){
        for(Resource r: ci.getType().getResources()){
            if(r instanceof PuppetResource){
                if(skeleton ==  null)
                    skeleton=(PuppetResource)r;
                return (PuppetResource)r;
            }
        }
        return null;
    }

    public String generate(){
        String name= vmi.getName();
        visitPuppetResources(vmi);
        if(!manifest.equals("")){
            if(!name.equals("")){
                try {
                    File file=new File(name+".pp");
                    BufferedWriter output = new BufferedWriter(new FileWriter(file));
                    output.write("node '" + name + "' {"+System.lineSeparator());
                    output.write(manifest);
                    output.write("}");
                    output.close();
                    journal.log(Level.INFO, ">> Manifest generated");
                    return file.getPath();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private void visitPuppetResources(ComponentInstance ci){
        InternalComponentInstanceGroup icig= currentModel.getComponentInstances().onlyInternals().hostedOn(ci);
        if(icig !=null){
            for(ComponentInstance c : icig){
                InternalComponentInstance ici;
                PuppetResource pr=findPuppetResource(c);
                if(pr != null)
                    manifest+=pr.getManifestEntry()+System.lineSeparator();
                visitPuppetResources(c);
            }
        }
    }

}
