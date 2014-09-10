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
package test.cloudml.monitoring;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.modaclouds.qos_models.monitoring_ontology.Component;
import it.polimi.modaclouds.qos_models.monitoring_ontology.InternalComponent;
import it.polimi.modaclouds.qos_models.monitoring_ontology.VM;
import it.polimi.modaclouds.qos_models.monitoring_ontology.ExternalComponent;
import junit.framework.TestCase;
import org.cloudml.core.Deployment;

import org.cloudml.core.builders.DeploymentBuilder;
import org.cloudml.monitoring.synchronization.Filter;
import org.cloudml.monitoring.synchronization.Model;
import org.cloudml.monitoring.synchronization.ModelUpdatesExclusionStrategy;
import org.cloudml.monitoring.synchronization.MonitoringAPI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 08.07.14.
 */
public class Test extends TestCase {
    public void test() {
        DeploymentBuilder dmb = org.cloudml.core.samples.SensApp.completeSensApp();
        Deployment dm = dmb.build();

        Model updates = Filter.fromCloudmlToModaMP(dm);

        Gson gson = new GsonBuilder().setExclusionStrategies(new ModelUpdatesExclusionStrategy()).serializeNulls().create();
        String json = gson.toJson(updates);

    }

    public void testUpload(){

        Model model = new Model();



            VM vm = new VM();
            vm.setId("frontend3");
            //vm.setNumberOfCpus(i);
            //vm.setUrl("NUOVO");
        vm.setCloudProvider("amazon");
            vm.setNumberOfCPUs(2);
            vm.setType(vm.getType());
            model.add(vm);


      /* no more component in Model class
      for(int i = 0; i<2; i++){
            Component component = new Component();
            component.setId(Integer.toString(i)+"compNUOVOP");
            //component.setStarted(true);
            //component.setUrl("abcd-compoNUOVO");
            model.add(component);
        }*/


            InternalComponent internalComponent = new InternalComponent();
            internalComponent.setId("mic3");
            //externalComponent.setStarted(false);
            //externalComponent.setUrl("abcd-extcompoNUOVO");
            //internalComponent.setCloudProvider("meNUOVO");
        internalComponent.addProvidedMethod("mic3-register");
        internalComponent.addProvidedMethod("mic3-answer");
        internalComponent.addProvidedMethod("mic3-save");
        internalComponent.addRequiredComponent("frontend3");
            internalComponent.setType(internalComponent.getType());
            model.add(internalComponent);


        MonitoringAPI monitor = new MonitoringAPI("http://localhost:8170");

        //monitor.addInstances(model);

        monitor.deleteInstances("mic3");

    }
}
