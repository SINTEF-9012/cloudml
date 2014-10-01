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

import org.cloudml.core.VMInstance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by nicolasf on 29.08.14.
 */
public class PuppetMarionnetteConnector {

    private static final Logger journal = Logger.getLogger(PuppetMarionnetteConnector.class.getName());
    public static String endpoint;
    private Boolean isWindows =false;


    public PuppetMarionnetteConnector(String endpoint, VMInstance vmi){
        this.endpoint=endpoint;
        this.isWindows=(vmi.getType().getOs().toLowerCase().contains("windows"));
    }

    public void install(VMInstance vmi){
        try {
            startPuppetAgent(vmi.getType().getPrivateKey(),vmi.getType().getLogin(), vmi.getType().getPasswd(), vmi.getName(), vmi.getPublicAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startPuppetAgent(String key, String user, String passwd, String host, String ip){
        if(isWindows){
            journal.log(Level.INFO, ">> STDOUT: Not yet implemented");
        }else{
            SSHConnector sc=new SSHConnector(key,user,ip, passwd);
            sc.execCommandSsh("puppet agent -t");
        }
    }

    private void configureHostnameWindows(String passwd, String ip, String masterEndpoint,String hostname){
        PowerShellConnector run = null;
        try {
            String command = "powershell  \".\\connect.ps1 " + passwd + " " + ip +" "+ hostname+" "+masterEndpoint+ "\"";
            run = new PowerShellConnector(command);
            journal.log(Level.INFO, ">> STDOUT: " + run.getStandardOutput());
            journal.log(Level.INFO, ">> STDERR: " + run.getStandardError());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void configureHostnameLinux(String key, String user, String passwd, String ip, String masterEndpoint,String hostname, String cmd){
        SSHConnector sc=new SSHConnector(key,user,ip, passwd);
        sc.execCommandSsh(cmd+" "+ip+" "+hostname);
        //need to restart, after updating hostname, crappy fix
        try {
            Thread.sleep(90000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sc.execCommandSsh("echo \"server = puppet-master-01\" >> /etc/puppet/puppet.conf; echo \""+masterEndpoint+"    puppet-master-01\" >>  /etc/hosts");
    }

    public void configureHostname(String key, String user, String passwd, String ip, String masterEndpoint,String hostname, String cmd){
        if(isWindows)
            configureHostnameWindows(passwd,ip,masterEndpoint,hostname);
        else configureHostnameLinux(key,user,passwd,ip,masterEndpoint,hostname,cmd);
    }

    public void manageCertificates(String id){
        StringBuffer sb=listCertificates();
        if(sb.indexOf(id) >= 0)
            removeCertificate(id);
        Boolean notDone=true;
        while(notDone){
            StringBuffer temp=listCertificates();
            if(temp.indexOf(id) >= 0){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{
                notDone=false;
            }
        }
    }

    private StringBuffer removeCertificate(String id){
        StringBuffer sb=new StringBuffer();
        try {
            sb= getRequest("http://"+endpoint+"/cgi-bin/revoke-cert?node="+id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb;
    }

    private StringBuffer listCertificates(){
        StringBuffer sb=new StringBuffer();
        try {
            sb= getRequest("http://"+endpoint+"/cgi-bin/list-certs");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb;
    }


    public StringBuffer getRequest(String targetUrl) throws Exception{
        URL url;
        HttpURLConnection connection = null;

        //Create connection
        url = new URL(targetUrl);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        //Get Response
        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuffer response = new StringBuffer();
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();
        return response;
    }

}
