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
package org.cloudml.deployer2.workflow;

import org.cloudml.codecs.JsonCodec;
import org.cloudml.core.Deployment;
import org.cloudml.deployer2.dsl.util.ActivityBuilder;
import org.cloudml.deployer2.dsl.util.ActivityValidator;
import org.cloudml.deployer2.workflow.util.ActivityDiagram;
import org.cloudml.deployer2.workflow.util.ActivityDotCreator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Logger;

public class ConcurrentDeployment {

    private static final Logger journal = Logger.getLogger(ConcurrentDeployment.class.getName());

    private Deployment targetModel;

    public ConcurrentDeployment(String pathToOldModel, String pathToNewModel) {
        setTargetModel(getDeployment(pathToNewModel));

        Deployment oldModel = null;
        if (pathToOldModel != null)
            oldModel = getDeployment(pathToOldModel);

        ActivityDiagram diagram = new ActivityDiagram();
        try {
            //create deployment plan
            diagram.createActivityDiagram(oldModel, getTargetModel());

            //validate activity diagram (deployment plan)
            ActivityValidator.checkActivity(ActivityBuilder.getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // create graph in a dot file, it is save in the resources folder
        // tool to view diagram: http://stamm-wilbrandt.de/GraphvizFiddle/
        new ActivityDotCreator(ActivityBuilder.getActivity());

        // traverse graph (execute deployment plan)
//        Parallel parallel = new Parallel(ActivityBuilder.getActivity(), false);
//        System.out.println(ActivityBuilder.getActivity().toString());
//        ParallelBFS bfs = new ParallelBFS(ActivityBuilder.getActivity(), false);
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


    public Deployment getTargetModel() {
        return targetModel;
    }

    public void setTargetModel(Deployment targetModel) {
        this.targetModel = targetModel;
    }

    public static void main(String[] args) throws Exception {

//        ConcurrentDeployment sensApp = new ConcurrentDeployment("C:\\Users\\Maksym\\Dropbox\\Documents\\Master thesis papers\\sensappAdmin-v2.json");
//        ConcurrentDeployment deployment = new ConcurrentDeployment(null, "c:\\Users\\Maksym\\Dropbox\\Documents\\Master thesis papers\\ec2.json");
        ConcurrentDeployment deployment = new ConcurrentDeployment("c:\\Users\\Maksym\\Dropbox\\Documents\\Master thesis papers\\ec2.json", "c:\\Users\\Maksym\\Dropbox\\Documents\\Master thesis papers\\ec2 - Copy.json");


    }


}
