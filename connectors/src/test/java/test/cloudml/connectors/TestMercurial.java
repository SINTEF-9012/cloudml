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
package test.cloudml.connectors;

/**
 * Created by nicolasf on 01.09.14.
 */


import junit.framework.TestCase;
import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.domain.*;
import org.cloudml.connectors.CloudFoundryConnector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(JUnit4.class)
public class TestMercurial extends TestCase {

    @Test
    public void testApp(){
        //WindowsMercurialConnector mc=new WindowsMercurialConnector("ssh://cloudml@109.231.121.6//etc/puppet/manifests//cloudml-nodes");
        //mc.clone("C:\\Users\\nicolasf\\Desktop\\cloudml2.0\\nodes");


        /*CloudFoundryConnector cfc=new CloudFoundryConnector("http://api.run.pivotal.io","mod4cloud@gmail.com","modus9012", "mod4cloud", "development");
        System.out.printf("%nSpaces:%n");
        for (CloudSpace space : cfc.getConnectedClient().getSpaces()) {
            System.out.printf("  %s\t(%s)%n", space.getName(), space.getOrganization().getName());
        }
        cfc.createEnvironmentWithWar("moduscpowa","","","","C:\\Users\\nicolasf\\Dropbox\\spring-music.war","");

        Map<String, String> env1 = new HashMap<String, String>();
        env1.put("foo", "bar");
        env1.put("bar", "baz");
        cfc.getConnectedClient().updateApplicationEnv("moduscpowa", env1);

        if(cfc.findCloudServiceOffering("p-mysql") != null){
            for(CloudServicePlan csp : cfc.findCloudServiceOffering("p-mysql").getCloudServicePlans())
                System.out.println(csp.getName()+ " :: " + csp.isFree());
        } else {
            System.out.printf("mmmmm");
        }

        cfc.createDBInstance("cleardb","","testDB","","","",0,"","");
        cfc.bindService("moduscpowa","testDB");
        cfc.logOut();*/
    }


}