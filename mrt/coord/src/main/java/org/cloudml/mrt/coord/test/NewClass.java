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
