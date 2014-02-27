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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * Change XML configurations inside a ZIP/JAR/WAR file.
 * 
 * A sample use is as follows:
 * 
 * ZipModifier modifier = new ZipModifier("C:\\temp\\granny-test.war","C:\\temp\\granny-out.war");
 * Map<String, String> kv = new HashMap<String,String>();
 *
 * kv.put("//bean[@id=\"dataSource\"]/property[@name=\"url\"]/@value", "mysql://newurl");
 * modifier.updateXMLAttribute("WEB-INF/classes/META-INF/spring/app-context.xml", kv);
 * 
 * @author Hui Song
 */
public class ZipModifier {
    

    
    File input = null;
    File output = null;
    public ZipModifier(String input, String output){
        this.input = new File(input);
        this.output = new File(output);
    }


    
    public void updateXMLElement(String entryName, Map<String,String> keyValues) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException, TransformerConfigurationException, TransformerException{
        ZipFile inZip = new ZipFile(input);
        ZipEntry entry = inZip.getEntry(entryName);
        ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(output));
        try{
            if(entry == null)
                throw new RuntimeException(String.format("Entry %s not found", entryName));
            InputStream entryIS = inZip.getInputStream(entry);
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = docBuilder.parse(entryIS);

            XPath xpath = XPathFactory.newInstance().newXPath();
            for(Map.Entry<String,String> kv : keyValues.entrySet()){
                XPathExpression xpe = xpath.compile(kv.getKey());
                Node node = (Node) xpe.evaluate(doc,XPathConstants.NODE);
                if(node instanceof Attr){
                    ((Attr)node).setValue(kv.getValue());
                }
                else
                    throw new UnsupportedOperationException("Only support the change of attributes at this moment");
            }

            Enumeration<? extends ZipEntry> entries = inZip.entries();
            while(entries.hasMoreElements()){
                ZipEntry en = entries.nextElement();
                
                if(en.getName().equals(entryName)){ 
                     ZipEntry newen = new ZipEntry(entryName);
                     outZip.putNextEntry(newen);
                     Transformer transformer = TransformerFactory.newInstance().newTransformer();
                     Result output = new StreamResult(outZip);
                     transformer.transform(new DOMSource(doc), output);
                }
                else{
                    outZip.putNextEntry(en);
                    copy(inZip.getInputStream(en),outZip);
                }
                outZip.closeEntry();
            }
        }
        finally{
            inZip.close();
            outZip.close();
        }
    }
    
    private static final byte[] BUFFER = new byte[4096 * 1024];
    private void copy(InputStream input, OutputStream output) throws IOException {
        int bytesRead;
        while ((bytesRead = input.read(BUFFER))!= -1) {
            output.write(BUFFER, 0, bytesRead);
        }
    }
    
}
