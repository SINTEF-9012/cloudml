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

package org.cloudml.mrt.coord.test;

import org.apache.commons.jxpath.JXPathContext;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.Node;
import org.cloudml.core.Property;

/**
 *
 * @author huis
 */
public class NewClass {
    public static void main(String[] args){
        DeploymentModel root = buildAModel();
        System.out.println(root);
       
        JXPathContext context = JXPathContext.newContext(root);
        Object obj = context.getValue("/nodeTypes/nt1/properties[name='book']/value");
        System.out.println(obj);
    }
   
    public static DeploymentModel buildAModel(){
        DeploymentModel root = new DeploymentModel("root");
        Node n1 = new Node("nt1");
        root.getNodeTypes().put("nt1", n1);
        n1.setOS("windows");
        n1.getProperties().add(new Property("time","now"));
        n1.getProperties().add(new Property("book","no"));
        return root;
    }
}
