package org.cloudml.deployer2.workflow.executables;

import org.cloudml.connectors.Connector;
import org.cloudml.core.InternalComponentInstance;
import org.cloudml.core.VM;
import org.cloudml.core.VMInstance;
import org.cloudml.deployer2.workflow.util.ActivityDiagram;
import org.cloudml.deployer2.dsl.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Maksym on 25.03.2015.
 */
public class ActionExecutable {
    private static final Logger journal = Logger.getLogger(ObjectExecutable.class.getName());
    private Action action;
    private boolean debugMode;


    public ActionExecutable(Action action, boolean debugMode){
        this.action = action;
        this.debugMode = debugMode;
    }

    public void execute() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        action.getProperties().put("Status", String.valueOf(Element.Status.ACTIVE));

        Class cls = ActivityDiagram.class;
        Method method;

        if (action instanceof ExpansionRegion){
            //TODO stuff for expansion region
        } else {
            String methodName = action.getName();

            // common objects for provisionAVM and provisionAPlatform cases
            String externalComponent = null;
            ObjectNode container = null;
            if (methodName.contains("provision")) {
                //TODO make more generic - in this case I know that I save IP to ObjectNode which goes after Join node
                Join dataJoin = (Join) action.getControlOrObjectFlowEdges(false, ActivityNode.Direction.OUT).get(0).getTarget();
                container = (ObjectNode) dataJoin.getOutgoing().get(0).getTarget();
                externalComponent = ((VMInstance) action.getInputs().get(0)).getName();
            }

            //common objects for resource's commands
            InternalComponentInstance instance = null;
            VMInstance vm = null;
            Connector jc = null;
            if (methodName.contains("execute")) {
                instance = (InternalComponentInstance) action.getInputs().get(0);
                vm = (VMInstance) action.getInputs().get(1);
                jc = (Connector) action.getInputs().get(2);
            }

            // common objects for configure and start commands
            VM type = null;
            String command = null;
            if (methodName.equals("configure") || methodName.equals("start")){
                type = (VM) action.getInputs().get(1);
                vm = (VMInstance) action.getInputs().get(0);
                jc = (Connector) action.getInputs().get(2);
                command = (String) action.getInputs().get(3);
            }

            // specific to configure
            Boolean requireCredentials = null;
            if (methodName.equals("configure")){
                requireCredentials = (Boolean) action.getInputs().get(4);
            }

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
                case "executeUploadCommands": journal.log(Level.INFO, "Action: Uploading " + instance.getName());
                                              method = cls.getDeclaredMethod(methodName, new Class[]{InternalComponentInstance.class, VMInstance.class, Connector.class, boolean.class});
                                              method.invoke(cls, instance, vm, jc, debugMode);
                                              break;
                case "executeRetrieveCommand": journal.log(Level.INFO, "Action: Retrieving " + instance.getName());
                                               method = cls.getDeclaredMethod(methodName, new Class[]{InternalComponentInstance.class, VMInstance.class, Connector.class, boolean.class});
                                               method.invoke(cls, instance, vm, jc, debugMode);
                                               break;
                case "executeInstallCommand": journal.log(Level.INFO, "Action: Installing " + instance.getName());
                                              method = cls.getDeclaredMethod(methodName, new Class[]{InternalComponentInstance.class, VMInstance.class, Connector.class, boolean.class});
                                              method.invoke(cls, instance, vm, jc, debugMode);
                                              break;
                case "configure": journal.log(Level.INFO, "Action: Configuring resource on " + vm.getName());
                                  method = cls.getDeclaredMethod(methodName, new Class[]{Connector.class, VM.class, VMInstance.class, String.class, Boolean.class, boolean.class});
                                  method.invoke(cls, jc, type, vm, command, requireCredentials, debugMode);
                                  break;
                case "start": journal.log(Level.INFO, "Action: Starting " + vm.getName());
                              method = cls.getDeclaredMethod(methodName, new Class[]{Connector.class, VM.class, VMInstance.class, String.class, boolean.class});
                              method.invoke(cls, jc, type, vm, command, debugMode);
                              break;
            }
        }

        action.getProperties().put("Status", String.valueOf(Element.Status.DONE));
    }

}
