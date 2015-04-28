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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cloudml.connectors.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.cloudml.connectors.PyHrapiConnector;


/**
 *
 * @author Hui Song
 */
 
public class TestPyHrapi {
    

    
    
    public void testUse(){
        
        System.out.println("enter testing");
        PyHrapiConnector connector = new PyHrapiConnector("http://127.0.0.1:5000", PyHrapiConnector.Version.V1);
        
        
        for(String gateway : connector.getGateways()){
            connector.deleteGateway(gateway);
        }
        
        
        
        Map<String, Object> gateway = new HashMap<String, Object>();
            String GATEWAY = "gateHTTP";
            gateway.put("gateway", GATEWAY);
            gateway.put("protocol", "http");
            gateway.put("defaultBack", "defaultBack");
            Map<String, String> endpoints = new HashMap<String, String>();
                endpoints.put("endOne", "0.0.0.0:8080");
        gateway.put("endpoints", endpoints);
        gateway.put("enable", "True");
        
        System.out.println(connector.addGateway(gateway));
        
        //assertEquals(1, connector.getEndpoints(GATEWAY).size());
        
       
        for(String pool : connector.getPools()){
            connector.deletePool(pool);
        }
        
        //assertEquals(0, connector.getPools().size());
        
        Map<String, Object> testPool = new HashMap<String, Object>();
            String TESTPOOL = "defaultBack";
            testPool.put("enabled", Boolean.TRUE);
            Map<String, String> targets = new HashMap<String, String>();
                targets.put("targetOne","109.105.109.218:80");
            testPool.put("targets", targets);
        

        System.out.println("Add pool:" + connector.addPool(TESTPOOL, testPool));
        
        
        Map targetm = new HashMap<String, Object>();
        //targetm.put("address","109.105.109.218:80");
        targetm.put("address", "127.0.0.1:1900");
        targetm.put("enabled", "true");
        targetm.put("weight", "100");
        
        Map defaultback = connector.getBackEnd(TESTPOOL);
        System.out.println(defaultback);
        ((Map)defaultback.get("targets")).remove("targetOne");
        ((Map)defaultback.get("targets")).put("targetTwo", "127.0.0.1:980");
        System.out.println("Modify pool:" + connector.addPool(TESTPOOL, defaultback));
        System.out.println("DeleteTaget" + connector.deleteTarget(TESTPOOL, "targetOne"));
        System.out.println("DeleteTaget" + connector.deleteTarget(TESTPOOL, "targetOne"));
        //System.out.println("Add target:"+ connector.addTarget(TESTPOOL, "targetOne", targetm));
        
        //System.out.println("Add gateway pool: "+connector._generalAdd("http://127.0.0.1:5000/v1/gateways/gateHTTP/pools", targets, TESTPOOL));
        
        //assertEquals(TESTPOOL, connector.getPools().get(0));
        //assertEquals(1, connector.getTargets(TESTPOOL).size());
        
        //Map m = connector.getPoolTarget(TESTPOOL, "targetOne");
        //assertEquals("10.0.0.1:8080", m.get("address"));
        
        //m.put("address", "127.0.0.1:8888");
        //System.out.println(connector.addTarget(TESTPOOL, "targetOne", m));
        //assertEquals("127.0.0.1:8888", connector.getPoolTarget(TESTPOOL, "targetOne").get("address"));
        
        
        System.out.println(connector.start());
        
        
    }
    
    public static void main(String[] args){
        new TestPyHrapi().testUse();
    }
    
}
