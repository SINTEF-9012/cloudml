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


package org.cloudml.mrt.coord;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.core.Artefact;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.Node;
import org.cloudml.core.NodeInstance;
import org.cloudml.core.Property;
import org.cloudml.core.Provider;

/**
 *
 * @author Hui Song
 */
public class ModelRepo {
    
    public DeploymentModel root;
    
    public ModelRepo(){
        root = new DeploymentModel();
        root.setName("root");
        //initWithSenseApp();
        //root.getNodeTypes().put("a", new Node("a"));
    }
    
    public DeploymentModel getRoot(){
        return root;
    }
    
    public void initWithFaked(){
        Provider provider = new Provider("provider","");
        root.getProviders().add(provider);
        Node node1 = new Node("node1");
        node1.setProvider(provider);
        root.getNodeTypes().put("node1",node1);
        root.getNodeInstances().add(node1.instanciates("ni11"));
        root.getNodeInstances().add(node1.instanciates("ni12"));
        
        Node node2 = new Node("node2");
        node2.setProvider(provider);
        root.getNodeTypes().put("node2", node2);
        
        root.getNodeInstances().add(node2.instanciates("ni21"));
        
        
//        Artefact artefact1 = new Artefact("artefact1");
//        root.getArtefactTypes().put("artefact1",artefact1);
//        
//        root.getArtefactInstances().add(artefact1.instanciates("ai11"));
//        root.getArtefactInstances().add(artefact1.instanciates("ai12"));
        
       
        for(NodeInstance ni : root.getNodeInstances()){
            ni.getProperties().add(new Property("state","onn"));
        }
        for(ArtefactInstance ai : root.getArtefactInstances()){
            ai.getProperties().add(new Property("state","onn"));
        }
    }
    
 
    
}
