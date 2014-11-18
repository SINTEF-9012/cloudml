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
import junit.framework.TestCase;
import org.cloudml.core.Deployment;
import org.cloudml.core.builders.DeploymentBuilder;
import org.cloudml.monitoring.synchronization.Filter;
import org.cloudml.monitoring.synchronization.Model;
import org.cloudml.monitoring.synchronization.MonitoringAPI;
import org.cloudml.monitoring.synchronization.MonitoringSynch;

/**
 * Created by user on 08.07.14.
 */
public class Test extends TestCase {
    public void test() {
        DeploymentBuilder dmb = org.cloudml.core.samples.SensApp.completeSensApp();
        Deployment dm = dmb.build();

        Model updates = Filter.fromCloudmlToModaMP(dm);

        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(updates);
        System.out.println(json);

        //MonitoringSynch.sendCurrentDeployment("http://109.231.122.205:8170", dm);

    }


}
