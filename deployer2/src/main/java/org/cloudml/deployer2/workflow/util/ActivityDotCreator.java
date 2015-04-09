package org.cloudml.deployer2.workflow.util;

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
    private String root = System.getProperty("user.dir");

    public ActivityDotCreator(Activity activity){
        this.activity = activity;
        printDot();
        saveToFile(root + defaultPath);
    }

    public  ActivityDotCreator(Activity activity, String pathToFile){
        this.activity = activity;
        printDot();
        saveToFile(pathToFile);
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
                String instance = ((VMInstance)((Action) node).getInputs().get(0)).getName();
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
}
