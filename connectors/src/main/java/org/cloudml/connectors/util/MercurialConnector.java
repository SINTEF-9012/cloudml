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
package org.cloudml.connectors.util;

import com.aragost.javahg.RepositoryConfiguration;
import com.aragost.javahg.commands.AddCommand;
import com.aragost.javahg.commands.CommitCommand;
import com.aragost.javahg.commands.PushCommand;
import org.apache.commons.io.FileUtils;
import org.cloudml.connectors.FlexiantConnector;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.aragost.javahg.BaseRepository;


/**
 * On Linux just need your ssh key as .ssh/id_rsa
 * On windows you need a configuration file where you specify a link to: a ssh tool(such as putty),
 * the mercurial binary, the mercurial configuration file (Mercurial.ini) which contains the path to the ssh key
 * Created by nicolasf on 03.09.14.
 */
public class MercurialConnector {

    private static final Logger journal = Logger.getLogger(MercurialConnector.class.getName());

    public static String endPoint;
    public static String sshKey;
    private String directory="";
    private String filename="";
    private BaseRepository br=null;
    private RepositoryConfiguration rc=null;

    public MercurialConnector(String endPoint, String sshKey){
        this.endPoint=endPoint;
        this.sshKey=sshKey;
        if(!System.getProperty("os.name").toLowerCase().contains("windows")){
            directory=System.getProperty("user.dir")+"/nodes";
        }else{
            directory=System.getProperty("user.dir")+"\\nodes";
        }
    }

    public void clone(String pathDest){
        checkIfExist(directory);
        rc=new RepositoryConfiguration();
        if(System.getProperty("os.name").toLowerCase().contains("windows")){
            configureMercurial();
        }
        br= BaseRepository.clone(rc,new File(pathDest),endPoint);
    }

    private void configureMercurial(){
        MercurialProperties mp= MercurialProperties.load();
        if(mp!=null){
            rc.setHgBin(mp.getHgBin());
            rc.setSshBin(mp.getSshBin());
            rc.setHgrcPath(mp.getHgConf());
        }
    }

    private void checkIfExist(String pathDest){
        File directory = new File(pathDest);
        try {
            FileUtils.deleteDirectory(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void commit(String username, String message){
        CommitCommand cc=new CommitCommand(br);
        cc.message("\"" + message + "\"");
        cc.user(username);
        cc.execute();
    }

    public void add(){
        AddCommand ac=new AddCommand(br);
        ac.execute();
    }

    public void push(String username){
        PushCommand pc=new PushCommand(br);
        try {
            pc.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addFile(String file, String username) {
        clone("./nodes");
        journal.log(Level.INFO, ">> Repository cloned");
        File f =new File(file);
        filename=f.getName();
        copyFile(f);
        add();
        journal.log(Level.INFO, ">> File added");
        rc.setSshBin(null);
        commit(username, "updated by CloudML");
        journal.log(Level.INFO, ">> Commit");
        push(username);
        journal.log(Level.INFO, ">> Push");
    }

    private void copyFile(File f){
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;
        try {
            sourceChannel = new FileInputStream(f).getChannel();
            String destPath;
            if(!System.getProperty("os.name").toLowerCase().contains("windows")){
                destPath=directory+"/"+filename;
            }else{
                destPath=directory+"\\"+filename;
            }
            File destFile=new File(destPath);
            if(!destFile.exists()) {
                destFile.createNewFile();
            }
            destChannel = new FileOutputStream(destFile).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class MercurialProperties {
        public String getHgBin() {
            return hgBin;
        }

        public String getSshBin() {
            return sshBin;
        }

        public String getHgConf() {
            return hgConf;
        }

        private String hgBin;
        private String sshBin;
        private String hgConf;


        public MercurialProperties(String hgBin, String sshBin, String hgConf) {
            this.hgBin=hgBin;
            this.hgConf=hgConf;
            this.sshBin=sshBin;
        }


        public static MercurialProperties load() {

            Properties prop = new Properties();
            InputStream input = null;
            MercurialProperties properties=null;
            try {

                input = new FileInputStream("mercurial.properties");

                // load a properties file
                prop.load(input);

                // get the property
                String hgBin= prop.getProperty("hgBin");
                String hgConf = prop.getProperty("hgConf");
                String sshBin = prop.getProperty("sshBin");

                properties = new MercurialProperties(hgBin, sshBin,hgConf);

            } catch (IOException ex) {
                journal.log(Level.INFO, ">> mercurial.properties not found!!");
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return properties;
        }

    }



}
