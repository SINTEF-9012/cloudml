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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by nicolasf on 21.05.14.
 */
public class PowerShellConnector {

    private static final Logger journal = Logger.getLogger(PowerShellConnector.class.getName());
    private final OutputCollector standardOutput;
    private final OutputCollector standardError;
    private final Process p;

    public PowerShellConnector(String commandLine) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder(commandLine.split("\\s+"));
        p = builder.start();
        p.getOutputStream().close();
        standardOutput = new OutputCollector(p.getInputStream());
        standardError = new OutputCollector(p.getErrorStream());
        standardOutput.join();
        standardError.join();
        p.waitFor();
    }

    public PowerShellConnector(String... cmd) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder(cmd);
        p = builder.start();
        p.getOutputStream().close();
        standardOutput = new OutputCollector(p.getInputStream());
        standardError = new OutputCollector(p.getErrorStream());
        standardOutput.join();
        standardError.join();
        p.waitFor();
    }

    public PowerShellConnector(File directory, String... cmd) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder(cmd);
        builder.directory(directory);
        p = builder.start();
        p.getOutputStream().close();
        standardOutput = new OutputCollector(p.getInputStream());
        standardError = new OutputCollector(p.getErrorStream());
        System.out.println(standardOutput.toString());
        standardOutput.join();
        standardError.join();
        p.waitFor();
    }

    public static boolean checkConnectivity(String host) {
        return checkConnectivity(host, 5985);
    }

    private static boolean checkConnectivity(String host, int port) {
        try {
            Socket sock = new Socket(host, port);
            sock.close();
            return true;
        } catch (IOException ex) {
            journal.log(Level.SEVERE, "Connection not yet available or refused");
            return false;
        }
    }

    public void destroy(){
        p.destroy();
    }

    public String getStandardOutput() {
        return standardOutput.getOutput();
    }

    public String getStandardError() {
        return standardError.getOutput();
    }

    public static class OutputCollector extends Thread {

        private final BufferedReader reader;
        private final StringBuilder buffer;

        public OutputCollector(InputStream inputStream) {
            this.reader = new BufferedReader(new InputStreamReader(inputStream));
            this.buffer = new StringBuilder();
            this.start();
        }

        public void run() {
            String line = "";
            try {
                while (null != (line = this.reader.readLine())) {
                    buffer.append(line);
                }
                this.reader.close();
            } catch (IOException ex) {
                throw new RuntimeException("Error while reading", ex);
            }
        }

        public String getOutput() {
            return this.buffer.toString();
        }
    }
}
