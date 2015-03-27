package org.cloudml.deployer2.camel.camel_beans;

import org.cloudml.core.VMInstance;
import org.cloudml.deployer2.camel.util.ActivityDiagram;
import org.cloudml.deployer2.dsl.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Maksym on 25.03.2015.
 */
public class ActionNodeBean {
    private static final Logger journal = Logger.getLogger(ObjectNodeBean.class.getName());
    private Action action;
    private boolean debugMode;


    public ActionNodeBean(Action action, boolean debugMode){
        this.action = action;
        this.debugMode = debugMode;
    }

    public void execute() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class cls = ActivityDiagram.class;
        Method method;

        if (action instanceof ExpansionRegion){
            //TODO stuff for expansion region
        } else {
            String methodName = action.getName();
            // common objects fo provisionAVM and provisionAPlatform cases
            //TODO make more generic - in this case I know that I save IP to ObjectNode which goes after Fork node
            Join dataFork = (Join) action.getControlOrObjectFlowEdges(false, ActivityNode.Direction.OUT).get(0).getTarget();
            ObjectNode container = (ObjectNode) dataFork.getOutgoing().get(0).getTarget();
            String externalComponent = ((VMInstance)action.getInputs().get(0)).getName();

            switch (methodName){
                case "provisionAVM": journal.log(Level.INFO, "Action: Provisioning " + externalComponent);
                                     method = cls.getDeclaredMethod(methodName, new Class[]{Action.class, boolean.class});
                                     method.invoke(cls, action, debugMode); //provision a vm and save its IP inside action
                                     container.addObject(externalComponent + " address is:" + action.getOutputs().get(0)); //save IP in ObjectNode
                                     break;
                case "provisionAPlatform": journal.log(Level.INFO, "Action: Provisioning " + externalComponent);
                                           method = cls.getDeclaredMethod(methodName, new Class[]{Action.class, boolean.class});
                                           method.invoke(cls, action, debugMode); //provision a platform and save its IP inside action
                                           container.addObject(externalComponent + " address is:" + action.getOutputs().get(0)); //save IP in ObjectNode
                                           break;
            }
        }
    }

//    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        System.out.println(boolean.class);
//        Action a = new Action("provisionAVM");
//        a.addOutput("output");
//        Class cls = ActivityDiagram.class;
//        Method method = cls.getDeclaredMethod(a.getName(), new Class[]{Action.class, boolean.class});
//        method.invoke(cls, a, true);
//    }
}
