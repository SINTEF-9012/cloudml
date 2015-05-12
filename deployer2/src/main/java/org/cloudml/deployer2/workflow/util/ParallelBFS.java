package org.cloudml.deployer2.workflow.util;

import org.cloudml.deployer2.dsl.*;
import org.cloudml.deployer2.workflow.executables.ActionExecutable;
import org.cloudml.deployer2.workflow.executables.ControlExecutable;
import org.cloudml.deployer2.workflow.executables.EdgeExecutable;
import org.cloudml.deployer2.workflow.executables.ObjectExecutable;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Created by Maksym on 08.05.2015.
 */
public class ParallelBFS {

    private final boolean debugMode;
    private final String LEVEL = "Level";
    private final String VISITED = "Visited";
    private int markingLevel = 1;
    private List<Integer> levels = new ArrayList<>();
    private List<Element> delayedNodes = new ArrayList<>();
//    ConcurrentLinkedQueue<Element> leveledTasks = null;

    public ParallelBFS(Activity activity, boolean debugMode) {
        this.debugMode = debugMode;

        List<Element> activityElements = new ArrayList<Element>();
        activityElements.addAll(activity.getNodes());
        activityElements.addAll(activity.getEdges());

        //assign levels to elements
        markElements(activityElements);

        // check levels
        for (int level:levels){
            List<Element> levelTasks = getLevelTasks(level, activityElements);
            System.out.println("------------------LEVEL " + level + "-------------------");
            for (Element el:levelTasks){
                System.out.println(el.toString());
            }
            System.out.println("\n");
        }

        boolean parallelMode = false;

//        leveledTasks = new ConcurrentLinkedQueue<Element>(activityElements);
        ForkJoinPool levelProcessor = new ForkJoinPool();
        for (int level:levels) {
            List<Element> levelTasks = getLevelTasks(level, activityElements);
            parallelProcessing(levelProcessor, levelTasks);
            activityElements.removeAll(levelTasks);
        }
    }

    private void parallelProcessing(ForkJoinPool levelProcessor, List<Element> levelTasks) {
        if (levelTasks.size() > 1)
            levelProcessor.invoke(new LevelProcessor(levelTasks));
        else
            processElement(levelTasks.get(0));
    }

    private List<Element> getLevelTasks(int currentLevel, List<Element> activityElements) {
        List<Element> elements = new ArrayList<>();
        for (Element el:activityElements){
            if (Integer.parseInt(el.getProperties().get(LEVEL)) == currentLevel){
                elements.add(el);
            }
        }
        return elements;
    }

    private void markElements(List<Element> activity) {
        Queue<Element> nodesAndEdges = new LinkedList<Element>();

        for (Element el:activity){
            el.getProperties().put(VISITED, String.valueOf(false));
            el.getProperties().put(LEVEL, String.valueOf(Integer.MAX_VALUE));
        }

        Element root = activity.get(0);
        nodesAndEdges.add(root);
        root.getProperties().put(LEVEL, String.valueOf(markingLevel));
        root.getProperties().put(VISITED, String.valueOf(true));

        while (!nodesAndEdges.isEmpty()){
            Element parent = nodesAndEdges.peek();
            int parentLevel = Integer.parseInt(parent.getProperties().get(LEVEL));

            List<Element> children = getUnvisitedChildren(parent);
            for (Element child:children){
                child.getProperties().put(LEVEL,String.valueOf(parentLevel + 1));
                nodesAndEdges.add(child);
            }

            if (!levels.contains(parentLevel))
                levels.add(parentLevel);
            nodesAndEdges.remove();
        }

    }

    private List<Element> getUnvisitedChildren(Element parent) {
        List<Element> elements = new ArrayList<Element>();

        //before we process children, we check if any delayed nodes are ready to be processed on this level
        if (!delayedNodes.isEmpty()){
            for (Element node:delayedNodes){
                boolean ready = checkNodeReadiness((ActivityNode) node);
                if (ready) {
                    elements.add(node);
                }
            }
            delayedNodes.removeAll(elements);
        }

        // process children
        if (parent instanceof ActivityEdge) {
            // nodes we add only if they have not been visited
            ActivityNode target = ((ActivityEdge) parent).getTarget();
            if (!Boolean.parseBoolean(target.getProperties().get(VISITED))) {
                //check node, do not add it if all incoming edges haven't been visited
                boolean addSynchronizationNode = checkNodeReadiness(target);
                if (addSynchronizationNode) {
                    elements.add(target);
                    target.getProperties().put(VISITED, String.valueOf(true));
                } else
                    delayedNodes.add(target); //if we do not add node on this level, we record it and will check if it is ready on the next level
            }
        } else {
            ActivityNode node = (ActivityNode) parent;
            // edges can not be VISITED twice, so we add all of them
            if (node.getOutgoing() != null) {
                elements.addAll(node.getOutgoing());
                for (Element el:node.getOutgoing())
                    el.getProperties().put(VISITED, String.valueOf(true));
            }
        }

        return elements;
    }

    // checks if all incoming edges of the node have been visited
    private boolean checkNodeReadiness(ActivityNode target) {
        boolean addSynchronizationNode = true;
        if (target.getIncoming().size() > 1 ){
            for (ActivityEdge in:target.getIncoming()){
                if (!Boolean.parseBoolean(in.getProperties().get(VISITED))) {
                    addSynchronizationNode = false;
                    break;
                }
            }
        }
        return addSynchronizationNode;
    }

    private class LevelProcessor extends RecursiveAction {
        private List<Element> elements = null;

        public LevelProcessor(List<Element> elements){
            super();
            this.elements = elements;
        }

        @Override
        protected void compute() {
            List<RecursiveAction> actions = new LinkedList<RecursiveAction>();
            for (Element el : elements) {
                NewThread thread = new NewThread(el);
//                if (elements.indexOf(el) + 1 == elements.size()) {
//                    thread.compute();
//                } else {
//                    actions.add(thread);
//                    thread.fork();
//                }
                actions.add(thread);
                thread.fork();
            }
            for (RecursiveAction action : actions) {
                action.join();
            }
        }
    }

    private class NewThread extends RecursiveAction {
        private Element element;

        public NewThread(Element element) {
            this.element = element;
        }

        @Override
        protected void compute() {
            processElement(element);
        }
    }

    private void processElement(Element element) {
        //process next element
        if (element instanceof ActivityEdge){
            new EdgeExecutable((ActivityEdge) element).execute();
        } else if (element instanceof ObjectNode){
            new ObjectExecutable((ObjectNode) element).execute();
        } else if (element instanceof Action){
            try {
                new ActionExecutable((Action) element, debugMode).execute();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else if (element instanceof ControlNode){
            new ControlExecutable((ControlNode) element).execute();
        }
    }


}
