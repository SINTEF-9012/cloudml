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
package org.cloudml.deployer2.workflow.executables;

import org.cloudml.connectors.Connector;
import org.cloudml.core.InternalComponentInstance;
import org.cloudml.core.VM;
import org.cloudml.core.VMInstance;
import org.cloudml.deployer2.dsl.*;
import org.cloudml.deployer2.dsl.util.ActivityBuilder;
import org.cloudml.deployer2.workflow.util.ActivityDiagram;
import org.cloudml.deployer2.workflow.util.ActivityDotCreator;

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

        //update DOT file
        new ActivityDotCreator(ActivityBuilder.getActivity());

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
                //TODO make more generic - in this case I know that I save IP to ObjectNode which goes after Join node or after
                ActivityNode afterAction = action.getControlOrObjectFlowEdges(false, ActivityNode.Direction.OUT).get(0).getTarget();
//                Join dataJoin = (Join) action.getControlOrObjectFlowEdges(false, ActivityNode.Direction.OUT).get(0).getTarget();
                if (afterAction instanceof Join) {
                    container = (ObjectNode) afterAction.getOutgoing().get(0).getTarget();
                } else if (afterAction instanceof ObjectNode){
                    container = (ObjectNode) afterAction;
                }
                externalComponent = ((VMInstance) action.getInputs().get(0)).getName(); //TODO fix name if get(0) returns platform instead of vm
            }

            //common objects for resource's commands
            InternalComponentInstance instance = null;
            VMInstance vm = null;
            Connector jc = null;
            if (methodName.contains("execute")) {
                instance = (InternalComponentInstance) action.getInputs().get(1);
                vm = (VMInstance) action.getInputs().get(0);
                jc = (Connector) action.getInputs().get(2);
            }

            // common objects for configure and start commands
            VM type = null;
            String command = null;
            String componentInstanceName = null;
            if (methodName.contains("configure") || methodName.equals("start")){
                type = (VM) action.getInputs().get(1);
                vm = (VMInstance) action.getInputs().get(0);
                jc = (Connector) action.getInputs().get(2);
                command = (String) action.getInputs().get(3);
                componentInstanceName = (String) action.getInputs().get(4);
                // handle connections when we pass ip destinationIP and destinationPort to connection resource' commands
                if (command.contains("::")){
                    String actualCommand = command.split("::")[0];
                    String port = command.split("::")[1];
                    String destinationVM = command.split("::")[2];
                    String destinationIP = getProvisionedVMaddress(destinationVM);
                    String ip = getProvisionedVMaddress(vm.getName());
                    command = actualCommand + " \"" + ip + "\" \"" + destinationIP + "\" " + port;
                }
            }

            // specific to configure
            Boolean requireCredentials = null;
            if (methodName.contains("configure")){
                // connection configuration method names follow pattern configure:connectionRetrieve
                if (methodName.contains(":")){
                    methodName = methodName.split(":")[0];
                }
                requireCredentials = (Boolean) action.getInputs().get(5);
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
                case "configure": journal.log(Level.INFO, "Action: Configuring resource on " + componentInstanceName);
                                  method = cls.getDeclaredMethod(methodName, new Class[]{Connector.class, VM.class, VMInstance.class, String.class, Boolean.class, boolean.class});
                                  method.invoke(cls, jc, type, vm, command, requireCredentials, debugMode);
                                  break;
                case "start": journal.log(Level.INFO, "Action: Starting " + componentInstanceName);
                              method = cls.getDeclaredMethod(methodName, new Class[]{Connector.class, VM.class, VMInstance.class, String.class, boolean.class});
                              method.invoke(cls, jc, type, vm, command, debugMode);
                              break;
            }
        }

        action.getProperties().put("Status", String.valueOf(Element.Status.DONE));

        //update DOT file
        new ActivityDotCreator(ActivityBuilder.getActivity());
    }

    private String getProvisionedVMaddress(String vmName) {
        String ip = null;
        for (Object provisionedIP: ActivityBuilder.getIPregistry().getObjects()){
            if (((String)provisionedIP).contains(vmName)){
                // objects inside public address look like 'Nimbus ip address is:1.1.1.1'
                ip = ((String) provisionedIP).split(":")[1];
            }
        }
        return ip;
    }

}
