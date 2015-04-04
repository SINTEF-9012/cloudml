package org.cloudml.deployer2.workflow.util;

import org.cloudml.deployer2.workflow.executables.ActionExecutable;
import org.cloudml.deployer2.workflow.executables.EdgeExecutable;
import org.cloudml.deployer2.workflow.executables.ControlExecutable;
import org.cloudml.deployer2.workflow.executables.ObjectExecutable;
import org.cloudml.deployer2.dsl.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Maksym on 30.03.2015.
 */
public class Parallel {
    private ForkJoinPool forkJoinPool = null;
    private Join dataJoin;
    private Join controlJoin;
    private boolean debugMode;

    public Parallel(Activity activity, boolean debugMode){
        this.debugMode = debugMode;
        try {
            traverse(activity);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void traverse(Activity activity) throws InterruptedException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        // start execution from the initial node
        ActivityInitialNode initialNode = null;
        for (ActivityNode initial:activity.getNodes()){
            if (initial instanceof ActivityInitialNode){
                initialNode = (ActivityInitialNode) initial;
                new ControlExecutable(initialNode).execute();
            }
        }

        // move down the tree
        executeNext(initialNode.getOutgoing().get(0));

    }

    private void executeNext(Element element) throws InterruptedException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (element instanceof ActivityEdge){
            processEdge((ActivityEdge) element);
        } else if (element instanceof ObjectNode){
            processObjectNode((ObjectNode) element);
        } else if (element instanceof Action){
            processAction((Action) element);
        } else if (element instanceof Fork){
            processFork((Fork) element);
        } else if (element instanceof Join){
            executeNext(((Join) element).getOutgoing().get(0));
        } else if (element instanceof ActivityFinalNode){
            new ControlExecutable((ControlNode) element).execute();
        }
    }

    private void processObjectNode(ObjectNode element) throws InterruptedException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ObjectNode object = element;
        new ObjectExecutable(object).execute();
        if (object.getOutgoing().size() > 0) {
            executeNext(object.getOutgoing().get(0));
        }
    }

    // each fork ends with control and optionally data join,
    // so when all tasks inside fork are done, we run control and data joins concurrently
    //TODO add handling of multiple data joins
    private void processFork(Fork element) {
        ArrayList<ActivityEdge> edges = element.getOutgoing();
        forkJoinPool = new ForkJoinPool(edges.size());
        forkJoinPool.invoke(new Concurrent(edges));
        if (dataJoin != null){
            forkJoinPool.invoke(new NewThread(dataJoin));
        }
        if (controlJoin != null){
            forkJoinPool.invoke(new NewThread(controlJoin));
        }
    }

    //TODO add handling of input data edges
    private void processAction(Action element) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InterruptedException {
        ArrayList<ActivityEdge> edges = element.getOutgoing();
        new ActionExecutable(element, debugMode).execute();
        if (edges.size() > 1){
            ForkJoinPool actionEdgesPool = new ForkJoinPool(1);
            actionEdgesPool.invoke(new NewThread(edges.get(1)));
        }
        executeNext(edges.get(0));

    }

    // process data or control edge
    private void processEdge(ActivityEdge edge) throws InterruptedException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Element target = edge.getTarget();
        new EdgeExecutable(edge).execute();
        if (target instanceof Join){
            if (edge.isObjectFlow()){
                dataJoin = (Join) target;
            } else {
                controlJoin = (Join) target;
            }
        } else {
            if (target != null) {
                executeNext(target);
            }
        }
    }

    // used to process a list of outgoing fork edges in parallel
    private class Concurrent extends RecursiveAction {
        private ArrayList<ActivityEdge> outgoing = null;

        public Concurrent(ArrayList<ActivityEdge> outgoing){
            super();
            this.outgoing = outgoing;
        }

        @Override
        protected void compute() {
            if (outgoing.size() == 1){
                NewThread thread = new NewThread(outgoing.get(0));
                thread.compute();
            } else {
                List<RecursiveAction> forks = new LinkedList<RecursiveAction>();
                for (ActivityEdge edge : outgoing) {
                    NewThread thread = new NewThread(edge);
                    forks.add(thread);
                    thread.fork();
                }
                for (RecursiveAction action : forks) {
                    action.join();
                }
            }
        }
    }

    // single asynchronous task
    private class NewThread extends RecursiveAction{
        private Element element;

        public NewThread(Element element){
            this.element = element;
        }

        @Override
        protected void compute() {
            try {
                executeNext(element);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
