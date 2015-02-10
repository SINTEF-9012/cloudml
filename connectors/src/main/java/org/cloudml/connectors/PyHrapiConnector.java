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

package org.cloudml.connectors;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Connector to operate a HAProxy load balancer through the REST API
 * pyHrapi @link{https://github.com/igabriel85/modaclouds-loadbalancer-controller}
 * which is also developed for MODAClouds
 * 
 * @author Hui Song
 * @date 12/02/2015
 */
public class PyHrapiConnector {
    
    private static final String NO_SUCH_GATEWAY = "No such gateway";
    private static final String ADDED_GATEWAY = "Added gateway";
    
    private String endpoint = "";
    private Version version = null;
    private String prefix = "";
    private final DefaultHttpClient httpclient = new DefaultHttpClient();
    
    private ObjectMapper mapper = new ObjectMapper();







    
    /**
     * Currently only "v1" is supported
     */
    public enum Version{
        V1("v1");
        
        private final String text;
        
        private Version(final String text) {
            this.text = text;
        }
        
        public String toString(){
            return text;
        }
    }
    
    public PyHrapiConnector(String endpoint, Version version){
        if(!endpoint.startsWith("http://")){
            endpoint = "http://"+endpoint;
        }
        this.endpoint = endpoint;
        this.version = version;
        this.prefix = String.format("%s/%s", endpoint, version);
        
        
    }
    
    private String invoke(URI uri, String method, String body) throws UnsupportedEncodingException, IOException{
        HttpUriRequest request = null;
        
        if("GET".equals(method)){
            request = new HttpGet(uri);
        }
        else if("POST".equals(method)){
            HttpPost post  = new HttpPost(uri);
            if(body != null && !body.isEmpty()){
                post.setHeader("Content-type", "application/json");
                post.setEntity(new StringEntity(body));
            }
            request = post;
        }
        else if("PUT".equals(method)){
            HttpPut put = new HttpPut(uri);
            if(body != null && !body.isEmpty()){
                put.setHeader("Content-type", "application/json");
                put.setEntity(new StringEntity(body));
            }
            request = put;
        }
        else if("DELETE".equals(method)){
            request = new HttpDelete(uri);
        }
        HttpResponse response = httpclient.execute(request);
        String s = IOUtils.toString(response.getEntity().getContent());
        //TODO: will be removed after debug
        System.out.println(s);
        return s;
    }
    
    private String invoke(String uri, String method, String body) throws UnsupportedEncodingException, IOException, URISyntaxException{
        URI request = new URIBuilder(uri).build();
        return invoke(request, method, body);
    }
    
    private Map invokeToMap(URI uri, String method, String body) throws IOException{
        String s = invoke(uri, method, body);
        return mapper.readValue(s, Map.class);
    }
    
    private Map invokeToMap(String uri, String method, String body) throws IOException, UnsupportedEncodingException, URISyntaxException{
        String s = invoke(uri, method, body);
        return mapper.readValue(s, Map.class);
    }
    
    public List<String> listGateways() {
        try{
            URI request = new URIBuilder(prefix+"/gateways").build();
            Map result = invokeToMap(request, "GET", null);
            return (List<String>)result.get("Gateways");
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    public Map getGateway(String gateway){
        try{
            URI request = new URIBuilder(prefix+"/gateways/"+gateway).build();
            Map result = invokeToMap(request, "GET", null);
            if(result.containsKey(NO_SUCH_GATEWAY)){
                return null;
            }
            return result;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    public String createGateway(Map gateway){
        try{
            String name = (String)gateway.get("gateway");
            String body = mapper.writeValueAsString(gateway);
            System.out.println(body);
            Map result = invokeToMap(prefix + "/gateways/"+name, "PUT", body);
            
            return (String)result.get(ADDED_GATEWAY);
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    public String deleteGateway(String gateway){
        try{
            return invoke(prefix+"/gateways/"+gateway, "DELETE", null);
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    public List<String> getEndpoints(String gateway) {
        try{
            URI request = new URIBuilder(String.format("%s/gateways/%s/endpoints", prefix, gateway)).build();
            Map result = invokeToMap(request, "GET", null);
            return (List<String>) result.get("Endpoints");
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    public String getEndpointAddress(String gateway, String endpoint){
        try{
            URI request = new URIBuilder(String.format("%s/gateways/%s/endpoints/%s", 
                    prefix, gateway, endpoint)).build();
            Map result = invokeToMap(request, "GET", null);
            return (String) result.get("address");
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    public Object addEndpoint(String gateway, String endpoint, String address) {
        try{
            Map result = invokeToMap(
                    String.format("%s/gateways/%s/endpoints/%s", prefix, gateway, endpoint),
                    "PUT",
                    "{ \"address\": \""+address+"\" }");
            
            return result;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String deleteEndpoint(String gateway, String endpoint) {
        try{
            return invoke(
                    String.format("%s/gateways/%s/endpoints/%s", prefix, gateway, endpoint),
                    "DELETE", 
                    null);
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    public String start() {

        try {
            return invoke(prefix+"/controller/commit", "POST", null);
        } catch (Exception ex) {
           ex.printStackTrace();
           return null;
        }
    }
}
