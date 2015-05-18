package org.cloudml.deployer2.workflow.frontend;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.awt.*;
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

//    https://today.java.net/article/2010/03/31/html5-server-push-technologies-part-1
//    static class UpdateHandler implements HttpHandler {
//        private String root = System.getProperty("user.dir") +
//                "\\deployer2\\src\\main\\java\\org\\cloudml\\deployer2\\workflow\\frontend";
//
//        public void handle(HttpExchange t) throws IOException {
//
//            URI uri = t.getRequestURI();
////            System.out.println(root + "\n");
//            System.out.println(root + uri.getPath().replaceAll("/", "\\\\") + "\n\n");
//
//            if (!uri.getPath().equals("/refresh")) {
//                String response = "404 (Not Found)\n";
//                t.sendResponseHeaders(404, response.length());
//                OutputStream os = t.getResponseBody();
//                os.write(response.getBytes());
//                os.close();
//            } else {
//
//
//                // add the required response header for an html file
//                Headers h = t.getResponseHeaders();
//                h.add("Content-Type", "text/html");
//
//                String response = "Page updated from server";
//
//                t.sendResponseHeaders(200, 0);
//                t.getResponseBody().write(response.getBytes());
//            }
//        }
//    }

    static class HTMLHandler implements HttpHandler {
        private String root = System.getProperty("user.dir") +
                "\\deployer2\\src\\main\\java\\org\\cloudml\\deployer2\\workflow\\frontend";

        public void handle(HttpExchange t) throws IOException {

            URI uri = t.getRequestURI();
//            System.out.println(root + "\n");
//            System.out.println(root + uri.getPath().replaceAll("/", "\\\\") + "\n\n");
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
        private String root = System.getProperty("user.dir") +
                "\\deployer2\\src\\main\\java\\org\\cloudml\\deployer2\\workflow\\frontend";
        private String dot = System.getProperty("user.dir") +
                "\\deployer2\\src\\main\\resources\\DagreD3.dot";

        public void handle(HttpExchange t) throws IOException {

            URI uri = t.getRequestURI();
//            System.out.println(uri.getPath());
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




    public static void main(String[] args){
        try {
            Desktop.getDesktop().browse(URI.create("http://localhost:8000/plan.html"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Browser br = new Browser();
    }
}
