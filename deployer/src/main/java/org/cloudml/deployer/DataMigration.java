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
package org.cloudml.deployer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This is a MODAClouds specific class
 * Created by nicolasf on 18.05.15.
 */
public class DataMigration {

    private URL endPoint;

    public DataMigration(){
        try {
            endPoint=new URL("");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * online data migration
     * @param source The source database identifier
     * @param destination The destination database identifier
     * @param threadNumber The number of TWT (i.e., the number of parallel threads writing towards the target database).
     * @param vdpSize The exponent for the base number 10 which, together, define the VDP size (e.g., 2 means that each VDP will contain 100 entities).
     * @return
     */
    public String switchOverPartitioned(String source, String destination, int threadNumber, int vdpSize){
        return doPOST("");
    }


    /**
     * Offline data migration
     * @param source The source database identifier
     * @param destination The destination database identifier
     * @param numberThreads The number of TWT (i.e., the number of parallel threads writing towards the target database).
     * @return
     */
    public String switchOver(String source, String destination, int numberThreads){
        return doPOST("");
    }


    private String doPOST(String content){
        HttpURLConnection httpCon = null;
        try {
            httpCon = (HttpURLConnection) endPoint.openConnection();
            httpCon.setRequestMethod("POST");
            httpCon.setRequestProperty("Content-Type", "application/json");

            // Send post request
            httpCon.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(httpCon.getOutputStream());
            wr.writeBytes(content);
            wr.flush();
            wr.close();

            int responseCode = httpCon.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpCon.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
