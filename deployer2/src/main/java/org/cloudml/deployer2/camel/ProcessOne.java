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
package org.cloudml.deployer2.camel;

import org.cloudml.connectors.Connector;
import org.cloudml.connectors.ConnectorFactory;
import org.cloudml.core.Provider;
import org.cloudml.core.VMInstance;

import java.util.HashMap;

/**
 * Created by Maksym on 06.03.2015.
 */
public class ProcessOne {

    private VMInstance n;

    public ProcessOne(VMInstance n) {
        this.n = n;
    }

    public void provisionAVM() {

        Provider p = n.getType().getProvider();
        Connector jc = ConnectorFactory.createIaaSConnector(p);

        HashMap<String, String> runtimeInformation = jc.createInstance(n);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("ProcessONE: VM " + n.getName() + "is provisioned with IP " + runtimeInformation.get("publicAddress"));
        jc.closeConnection();
    }
//    public void hi(){
//        System.out.println("hi 1");
//    }
}
