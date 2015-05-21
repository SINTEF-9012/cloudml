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
package org.cloudml.deployer2.workflow.frontend;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;

/**
 * Created by Maksym on 15.05.2015.
 */
public class Browser {



    public Browser(){
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/plan", new HTMLHandler());
            server.createContext("/dot", new DotHandler());
//            server.createContext("/refresh", new UpdateHandler());
            server.setExecutor(null); // creates a default executor
            server.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class HTMLHandler implements HttpHandler {
        private String root = System.getProperty("user.dir").split("cloudml")[0] + "cloudml" +
                "\\deployer2\\src\\main\\java\\org\\cloudml\\deployer2\\workflow\\frontend";

        public void handle(HttpExchange t) throws IOException {

            URI uri = t.getRequestURI();

            File file = new File(root + uri.getPath().replaceAll("/", "\\\\")).getCanonicalFile();
            if (!file.isFile()) {
                String response = "404 (Not Found)\n";
                t.sendResponseHeaders(404, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                // add the required response header for an html file
                Headers h = t.getResponseHeaders();
                h.add("Content-Type", "text/html");

                byte[] bytearray = new byte[(int) file.length()];
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                bis.read(bytearray, 0, bytearray.length);

                // ok, we are ready to send the response.
                t.sendResponseHeaders(200, 0);
                OutputStream os = t.getResponseBody();
                os.write(bytearray, 0, bytearray.length);
                fis.close();
                os.close();
            }
        }
    }

    static class DotHandler implements HttpHandler {
        private String root = System.getProperty("user.dir").split("cloudml")[0] + "cloudml" +
                "\\deployer2\\src\\main\\java\\org\\cloudml\\deployer2\\workflow\\frontend";
        private String dot = System.getProperty("user.dir").split("cloudml")[0] + "cloudml" +
                "\\deployer2\\src\\main\\resources\\DagreD3.dot";

        public void handle(HttpExchange t) throws IOException {

            URI uri = t.getRequestURI();

            File file = new File(dot).getCanonicalFile();
            if (!uri.getPath().equals("/dot")) {
                String response = "404 (Not Found)\n";
                t.sendResponseHeaders(404, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {

                // add the required response header for a dot file
                Headers h = t.getResponseHeaders();
                h.add("Content-Type", "text/plain");

                byte[] bytearray = new byte[(int) file.length()];
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                bis.read(bytearray, 0, bytearray.length);

                // ok, we are ready to send the response.
                t.sendResponseHeaders(200, 0);
                OutputStream os = t.getResponseBody();
                os.write(bytearray, 0, bytearray.length);
                fis.close();
                os.close();
            }
        }
    }

//    public static void main(String[] args){
//        try {
//            Desktop.getDesktop().browse(URI.create("http://localhost:8000/plan.html"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Browser br = new Browser();
//    }

}
