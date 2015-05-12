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

import org.cloudml.deployer2.dsl.*;
import org.cloudml.deployer2.workflow.executables.ActionExecutable;
import org.cloudml.deployer2.workflow.executables.ControlExecutable;
import org.cloudml.deployer2.workflow.executables.EdgeExecutable;
import org.cloudml.deployer2.workflow.executables.ObjectExecutable;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Created by Maksym on 30.03.2015.
 */
public class Parallel {
    private ForkJoinPool forkJoinPool = new ForkJoinPool();
    private List<Join> joinNodes = new CopyOnWriteArrayList<Join>();
    private List<Join> alreadyExecuted = new CopyOnWriteArrayList<Join>();
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
        // check for new threads because of Join nodes
        if (!(element instanceof Join || element instanceof ActivityEdge))
            if (!joinNodes.isEmpty())
                checkJoinNodes();

        //process next element
        if (element instanceof ActivityEdge){
            processEdge((ActivityEdge) element);
        } else if (element instanceof ObjectNode){
            processObjectNode((ObjectNode) element);
        } else if (element instanceof Action){
            processAction((Action) element);
        } else if (element instanceof Fork){
            processFork((Fork) element);
        } else if (element instanceof Join ){
            processJoin((Join) element);
        } else if (element instanceof ActivityFinalNode){
            new ControlExecutable((ControlNode) element).execute();
        }
    }

    private void checkJoinNodes() throws InterruptedException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<Join> readyJoins = new CopyOnWriteArrayList<Join>();
        if (!joinNodes.isEmpty()) {
            for (Join join:joinNodes){
                synchronizeJoin(readyJoins, join);
            }
        }
        if (!readyJoins.isEmpty()) {
            ArrayList<ActivityEdge> joinEdges = new ArrayList<ActivityEdge>();
            for (Join j:readyJoins){
                processJoin(j);
                joinEdges.addAll(j.getOutgoing());
            }
            forkJoinPool.invoke(new Concurrent(joinEdges));
        }
    }

    /**
     *  Let's say this join must be executed in parallel with 3 other actions.
     *  Before every action starts, it will check if there are any joins that must be executed, so all three actions will find that there is 1 Join that must be executed.
     *  We want this join to be executed only once, so whichever action will fire first, it will add Join to the list of "alreadyExecuted" joins
     *  and other actions will skip it.
     * @param readyJoins
     * @param join
     */
    private void synchronizeJoin(List<Join> readyJoins, Join join) {
        if (!alreadyExecuted.contains(join)) {
            boolean joinIsReadToExecute = true;
            for (ActivityEdge edge:join.getIncoming()){
                if (!edge.getProperties().get("Status").equals("DONE")) {
                    joinIsReadToExecute = false;
                    break;
                }
            }
            if (joinIsReadToExecute) {
                synchronized (readyJoins) {
                    readyJoins.add(join);
                }
                synchronized (alreadyExecuted) {
                    alreadyExecuted.add(join);
                }
            }
        }
    }

    private void processJoin(Join join) throws InterruptedException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        // if you try to modify dataJoin or controlJoin within checkJoinNodes and consequently synchronizeJoin methods, you will get concurrency exceptions
        synchronized (joinNodes) {
            if (joinNodes.contains(join))
                joinNodes.remove(join);
        }

        new ControlExecutable(join).execute();
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
    private void processFork(Fork element) throws InterruptedException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        new ControlExecutable(element).execute();
        ArrayList<ActivityEdge> edges = element.getOutgoing();
        forkJoinPool.invoke(new Concurrent(edges));
        checkJoinNodes();
    }

    private void processAction(Action element) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InterruptedException {
        ArrayList<ActivityEdge> edges = element.getOutgoing();

        boolean processAction = true;
        // if we have a few incoming edges we have to wait before all of them execute
        if (element.getIncoming().size() > 1) {

            ArrayList<ActivityEdge> firedEdges = new ArrayList<ActivityEdge>();
            for (ActivityEdge edge : element.getIncoming()) {
                if (edge.getProperties().get("Status").equals("DONE"))
                    firedEdges.add(edge);
            }
            if (firedEdges.size() != element.getIncoming().size()) {
                processAction = false;
            }

        }

        // process action only if all incoming edges fired or we have only one incoming edge
        if (processAction) {
            new ActionExecutable(element, debugMode).execute();

            //handle outgoing edges
            if (edges.size() > 1) {
                ForkJoinPool actionEdgesPool = new ForkJoinPool(edges.size());
                actionEdgesPool.invoke(new Concurrent(edges));
            } else if (!edges.isEmpty()) {
                executeNext(edges.get(0));
            }
            checkJoinNodes();
        }
    }

    // process data or control edge
    private void processEdge(ActivityEdge edge) throws InterruptedException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Element target = edge.getTarget();  //System.out.println(edge.toString() + " active threads now: " + forkJoinPool.getActiveThreadCount());
        new EdgeExecutable(edge).execute();
        if (target instanceof Join){
            synchronized (joinNodes) {
                if (!joinNodes.contains((Join) target))
                    joinNodes.add((Join) target);
            }

            // handle case when edge goes from one join to another
//            if (edge.getSource() instanceof Join)
//                checkJoinNodes();
//        } else if ((target instanceof Action) && edge.isObjectFlow()){
//            // do nothing, stop traversing the graph
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
