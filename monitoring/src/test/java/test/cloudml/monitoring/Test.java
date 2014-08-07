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
import it.polimi.modaclouds.qos_models.monitoring_ontology.VM;
import it.polimi.modaclouds.qos_models.monitoring_ontology.ExternalComponent;
import junit.framework.TestCase;
import org.cloudml.core.Deployment;

import org.cloudml.core.Property;
import org.cloudml.core.builders.DeploymentBuilder;
import org.cloudml.core.credentials.FileCredentials;
import org.cloudml.monitoring.synchronization.Filter;
import org.cloudml.monitoring.synchronization.ModelUpdates;
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

        /*
        DeploymentBuilder dmb = org.cloudml.core.samples.PaasCloudBees.completeCloudBeesPaaS();
        Deployment dm = dmb.build();
        dm.getProviders().firstNamed("CloudBees").setName("beanstalk");
        dm.getProviders().firstNamed("beanstalk").setCredentials(new FileCredentials("c:\\temp\\aws.credential"));
        ExternalComponent c = dm.getComponents().onlyExternals().firstNamed("cbdb");
        c.setServiceType("database");
        c.setLogin("sintef");
        c.setPasswd("password123");
        c.getProperties().add(new Property("DB-Engine","MySQL"));
        c.getProperties().add(new Property("DB-Version","5.6.17"));
        c.getProperties().add(new Property("DB-Name","cbdb"));
        System.out.println(dm);*/

        ModelUpdates updates = Filter.fromCloudmlToModaMP(dm);

        Gson gson = new GsonBuilder().setExclusionStrategies(new ModelUpdatesExclusionStrategy()).serializeNulls().create();
        String json = gson.toJson(updates);
        System.out.println(json);
        /*
        MonitoringAPI monitoringAPI = new MonitoringAPI();
        //an instance of the monitring manager should run il localhost
        monitoringAPI.uploadDeployment("http://localhost:8170", updates);
        */
    }

    public void testUpload(){
        List<VM> vms = new ArrayList<VM>();
        List<Component> components = new ArrayList<Component>();
        List<ExternalComponent> externalComponents = new ArrayList<ExternalComponent>();

        for(int i = 0; i<1; i++){
            VM vm = new VM();
            vm.setId(Integer.toString(i));
            vm.setNumberOfCpus(i);
            vm.setUrl("NUOVO");
            vms.add(vm);
        }

        for(int i = 0; i<2; i++){
            Component component = new Component();
            component.setId(Integer.toString(i)+"compNUOVOP");
            component.setStarted(true);
            component.setUrl("abcd-compoNUOVO");
            components.add(component);
        }

        for(int i = 0; i<3; i++){
            ExternalComponent externalComponent = new ExternalComponent();
            externalComponent.setId(Integer.toString(i)+"extcompNUOVO");
            externalComponent.setStarted(false);
            externalComponent.setUrl("abcd-extcompoNUOVO");
            externalComponent.setCloudProvider("meNUOVO");
            externalComponents.add(externalComponent);
        }

        ModelUpdates model = new ModelUpdates(components,externalComponents,vms);

        MonitoringAPI monitor = new MonitoringAPI("http://localhost:8170");

        monitor.uploadDeployment(model);
    }
}
