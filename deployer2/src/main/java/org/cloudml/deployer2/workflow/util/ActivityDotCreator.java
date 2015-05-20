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
package org.cloudml.deployer2.workflow.util;

import org.cloudml.core.InternalComponentInstance;
import org.cloudml.core.PortInstance;
import org.cloudml.core.VMInstance;
import org.cloudml.deployer2.dsl.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/**
 * Created by Maksym on 07.04.2015.
 */
public class ActivityDotCreator {
    private Activity activity;
    private StringBuilder dotText = new StringBuilder();
    private String defaultPath = "\\deployer2\\src\\main\\resources\\Deployment.dot";
    private String dagrePath = "\\deployer2\\src\\main\\resources\\DagreD3.dot";
    private String root = System.getProperty("user.dir");

    public ActivityDotCreator(Activity activity){
        this.activity = activity;

        printDot();
        String path = root.split("cloudml")[0] + "cloudml" + defaultPath;
        saveToFile(path);

        //clear string builder
        getDotText().replace(0, getDotText().length(), "");

        printDagreD3();
        String dagreD3Path = root.split("cloudml")[0] + "cloudml" + dagrePath;
        saveToFile(dagreD3Path);
    }

    private void printDagreD3() {
        getDotText().append("digraph ConcurrentDeployment {\n");
        addDagreNodes();
        addDagreEdges();
    }

    private String setElementColor(Element element) {
        String property = element.getProperties().get("Status");
        if (property.equals("ACTIVE")){
            return "orange";
        } else if (property.equals("DONE")){
            return "#58D658";
        } else
            return null;
    }

    private void addDagreEdges() {
        for (ActivityEdge edge:activity.getEdges()){
            String color = setElementColor(edge);
            getDotText().append("\t");
            String source = "";
            if (edge.getSource() != null){
                source = edge.getSource().getElementID();
            }
            String target = "";
            if (edge.getTarget() != null){
                target = edge.getTarget().getElementID();
            }
            getDotText().append(source).append(" -> ").append(target);

            if (edge.isObjectFlow()) {
                if (color == null) {
                    getDotText().append(" [style=\"");
                    getDotText().append(" stroke-dasharray: 5,5;");
                } else {
                    getDotText().append(" [style=\"stroke: " + color + ";");
                    getDotText().append(" stroke-dasharray: 5,5;");
                }
                getDotText().append("\"");
            } else {
                if (color == null)
                    getDotText().append(" [");
                else
                    getDotText().append(" [style=\"stroke: " + color + ";\"");
            }
            getDotText().append(" lineInterpolate=basis];\n");
            if (activity.getEdges().indexOf(edge) == (activity.getEdges().size() - 1))
                getDotText().append("}");
        }
    }


    private void addDagreNodes() {
        int nodeIndex = 0;
        for (ActivityNode node:activity.getNodes()){
            String color = setElementColor(node);
            String nodeID = "node_" + nodeIndex;
            nodeIndex++;
            node.setElementID(nodeID);
            getDotText().append("\t").append(nodeID);
            if (node instanceof ActivityInitialNode || node instanceof ActivityFinalNode){
                getDotText().append(" [shape=circle];\n");
            } else if (node instanceof Action){
                String action = node.getName();
                String instance = null;
                if (action.contains("execute")){
                    instance = ((InternalComponentInstance)((Action) node).getInputs().get(1)).getName();
                } else if (action.startsWith("configure") || action.contains("start")){
                    instance = (String)((Action) node).getInputs().get(4);
                } else if (action.equals("unconfigureWithIP")){
                    instance = ((PortInstance)((Action) node).getInputs().get(1)).getType().getName();
                } else if (node.getName().equals("stopInternalComponent")){
                    instance = ((InternalComponentInstance)((Action) node).getInputs().get(0)).getName();
                } else {
                    instance = ((VMInstance) ((Action) node).getInputs().get(0)).getName();
                }
                getDotText().append(" [shape=rect rx=10 label=\"").
                        append(action).
                        append("\\n").
                        append(instance + "\"");
                if (color != null)
                    getDotText().append(" style=\"fill: " + color + ";\"");
                getDotText().append("];\n");
            } else if (node instanceof ObjectNode){
                getDotText().append(" [shape=rect, label=\"").
                        append(node.getName() + "\"");
                if (color != null)
                    getDotText().append(" style=\"fill: " + color + ";\"");
                getDotText().append("];\n");
            } else if (node instanceof ControlNode){
                getDotText().append(" [shape=rect width=60 label=\"\" style=\"fill: black\"];\n");
            }
        }
    }

    private void saveToFile(String path){
        try {
            BufferedWriter bwr = new BufferedWriter(new FileWriter(new File(path)));
            bwr.write(getDotText().toString());
            bwr.flush();
            bwr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printDot() {
        getDotText().append("digraph ConcurrentDeployment {\n");
        addNodes();
        addEdges();
    }

    private void addNodes(){
        int nodeIndex = 0;
        for (ActivityNode node:activity.getNodes()){
            String nodeID = "node_" + nodeIndex;
            nodeIndex++;
            node.setElementID(nodeID);
            getDotText().append("\t").append(nodeID);
            if (node instanceof ActivityInitialNode || node instanceof ActivityFinalNode){
                getDotText().append(" [shape=point];\n");
            } else if (node instanceof Action){
                String action = node.getName();
                String instance = null;
                if (action.contains("execute")){
                    instance = ((InternalComponentInstance)((Action) node).getInputs().get(1)).getName();
                } else if (action.startsWith("configure") || action.contains("start")){
                    instance = (String)((Action) node).getInputs().get(4);
                } else if (action.equals("unconfigureWithIP")){
                    instance = ((PortInstance)((Action) node).getInputs().get(1)).getType().getName();
                } else if (node.getName().equals("stopInternalComponent")){
                    instance = ((InternalComponentInstance)((Action) node).getInputs().get(0)).getName();
                } else {
                    instance = ((VMInstance) ((Action) node).getInputs().get(0)).getName();
                }
                getDotText().append(" [shape=Mrecord label=\"").
                        append(action).
                        append("\\n").
                        append(instance).
                        append("\"];\n");
            } else if (node instanceof ObjectNode){
                getDotText().append(" [shape=box, label=\"").
                        append(node.getName()).
                        append("\"];\n");
            } else if (node instanceof ControlNode){
                getDotText().append(" [shape=box label=\"\" style=filled color=black height=0];\n");
            }
        }
    }

    private void addEdges(){
        for (ActivityEdge edge:activity.getEdges()){
            getDotText().append("\t");
            String source = "";
            if (edge.getSource() != null){
                source = edge.getSource().getElementID();
            }
            String target = "";
            if (edge.getTarget() != null){
                target = edge.getTarget().getElementID();
            }
            getDotText().append(source).append(" -> ").append(target);
            if (edge.isObjectFlow())
                getDotText().append(" [style=dashed]");
            getDotText().append(";\n");
            if (activity.getEdges().indexOf(edge) == (activity.getEdges().size() - 1))
                getDotText().append("}");
        }
    }


    public StringBuilder getDotText() {
        return dotText;
    }

//    public static void main(String[] args){
//        ActivityDotCreator dot = new ActivityDotCreator(ActivityBuilder.getActivity());
//        System.out.println(dot.defaultPath);
//        System.out.println(dot.root);
//        System.out.println(dot.root.split("cloudml")[0] + "cloudml" + dot.defaultPath);
//        System.out.println(dot.root.split("cloudml")[0] + "cloudml" + dot.dagrePath);
//    }

}
