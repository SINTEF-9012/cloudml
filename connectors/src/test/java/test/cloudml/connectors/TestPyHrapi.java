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

package test.cloudml.connectors;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.cloudml.connectors.PyHrapiConnector;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 *
 * @author Hui Song
 */
@RunWith(JUnit4.class)
public class TestPyHrapi extends TestCase{
    
    @Test
    @Ignore
    public void testGatewayList(){
        PyHrapiConnector connector = new PyHrapiConnector("http://127.0.0.1:5000", PyHrapiConnector.Version.V1);
        
        
        for(String gateway : connector.listGateways()){
            connector.deleteGateway(gateway);
        }
        
        assertTrue(connector.listGateways().isEmpty());
        
        Map<String, Object> gateway = new HashMap<String, Object>();
        String GATEWAY = "gateHTTP";
        gateway.put("gateway", GATEWAY);
        gateway.put("protocol", "http");
        Map<String, String> endpoints = new HashMap<String, String>();
            endpoints.put("endOne", "173.13.23.11:80");
            endpoints.put("endTwo", "123.123.123.124:80");
        gateway.put("endpoints", endpoints);
        gateway.put("enable", "True");
        
        assertEquals(GATEWAY, connector.createGateway(gateway));
        
        assertEquals(2, connector.getEndpoints(GATEWAY).size());
        assertEquals("173.13.23.11:80", connector.getEndpointAddress(GATEWAY, "endOne"));
        System.out.println(connector.addEndpoint(GATEWAY, "EndThree", "192.168.1.10:8080"));
        assertEquals("192.168.1.10:8080", connector.getEndpointAddress(GATEWAY, "EndThree"));
        connector.deleteEndpoint(GATEWAY, "EndThree");
        assertEquals(1, connector.getEndpointAddress(GATEWAY, "EndThree"));
        
        System.out.println(connector.start());
       
        
    }
    
}
