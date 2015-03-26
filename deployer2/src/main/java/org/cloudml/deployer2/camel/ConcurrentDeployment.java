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
/**
 * Created by Maksym on 05.03.2015.
 */
package org.cloudml.deployer2.camel;

import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.util.jndi.JndiContext;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.core.Deployment;
import org.cloudml.core.VMInstance;
import org.cloudml.deployer2.camel.camel_beans.ActionNodeBean;
import org.cloudml.deployer2.camel.camel_beans.ActivityEdgeBean;
import org.cloudml.deployer2.camel.camel_beans.ControlNodeBean;
import org.cloudml.deployer2.camel.camel_beans.ObjectNodeBean;
import org.cloudml.deployer2.camel.util.ActivityBuilder;
import org.cloudml.deployer2.camel.util.ActivityDiagram;
import org.cloudml.deployer2.camel.util.BeansRegistrator;
import org.cloudml.deployer2.dsl.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

public class ConcurrentDeployment {

    private static final Logger journal = Logger.getLogger(ConcurrentDeployment.class.getName());

    private DefaultCamelContext context;
    private Deployment targetModel;

    public ConcurrentDeployment(String pathToModel) {
        setTargetModel(getDeployment(pathToModel));

        ActivityDiagram diagram = new ActivityDiagram();
        try {
            diagram.setExternalServices(getTargetModel().getComponentInstances().onlyExternals());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // a list of task names which will be used as endpoint names in the Camel routes
        ArrayList<String> tasks = new ArrayList<String>();

        try {

            //register execution tasks(or beans, processes..) in the Camel context and save their names
            JndiContext jndiContext = new JndiContext();
            ArrayList<ActivityNode> nodes = ActivityBuilder.getActivity().getNodes();
            BeansRegistrator.performRegistration(jndiContext, nodes, tasks);
//            while (VMs.hasNext()) {
//                VMInstance vm = VMs.next();
//                String name = vm.getName();
//                if (name.equals("zookeeper (Maksym)")) {
//                    jndiContext.bind(name, new ProcessTwo(vm, true));
//                } else {
//                    jndiContext.bind(name, new ProcessOne(vm, true));
//                }
//                tasks.add(name);
//            }
            System.out.println(ActivityBuilder.getActivity().toString());
            System.out.println(tasks.size() + " registered tasks:");
            for (String name:tasks) {
                System.out.println("- " + name);
            }

            // Create a CamelContext (which is Camel's runtime system)
            context = new DefaultCamelContext(jndiContext);

            // add execution order (flow) to CamelContext
            new Activity_w(tasks).addRoutesToCamelContext(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // read model from json file
    private Deployment getDeployment(String pathToModel) {
        JsonCodec json = new JsonCodec();
        InputStream is = null;
        try {
            is = new FileInputStream(pathToModel);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return (Deployment) json.load(is);
    }

    public void start() throws Exception {
        context.start();
    }

    public void stop() throws Exception {
        context.stop();
    }

    public Deployment getTargetModel() {
        return targetModel;
    }

    public void setTargetModel(Deployment targetModel) {
        this.targetModel = targetModel;
    }

    public static void main(String[] args) throws Exception {

        ConcurrentDeployment deployment = new ConcurrentDeployment("c:\\Users\\Maksym\\Dropbox\\Documents\\Master thesis papers\\ec2.json");
//        ActivityDiagram diagram = new ActivityDiagram();
//        diagram.setExternalServices(deployment.getTargetModel().getComponentInstances().onlyExternals());
//        System.out.println(ActivityBuilder.getActivity().toString());
//        for (ActivityNode node:ActivityBuilder.getActivity().getNodes()){
//            if (node instanceof Action){
//                ActionNodeBean exec = new ActionNodeBean((Action) node);
//                exec.execute();
//            }
//        }

//        deployment.start();
//
//        while (true) {
//        }

//        deployment.stop();

    }


}
