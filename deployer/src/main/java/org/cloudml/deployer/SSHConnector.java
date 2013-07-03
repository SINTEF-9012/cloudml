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
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jcraft.jsch.*;

public class SSHConnector {
	
	private static final Logger journal = Logger.getLogger(JCloudsConnector.class.getName());
	
	String keyPath="";
	String user="";
	String host="";

	public SSHConnector(String keyPath, String user, String host){
		this.keyPath=keyPath;
		this.user=user;
		this.host=host;
	}

	public void execCommandSsh(String command){
		journal.log(Level.INFO, ">> executing command...");
		journal.log(Level.INFO, ">> "+ command);
		JSch jsch = new JSch();
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		try {
			jsch.addIdentity(keyPath);

			Session session = jsch.getSession(user, host, 22);
			session.setConfig(config);
			session.connect(0);

			Channel channel = session.openChannel("exec");
			ChannelExec channelExec=((ChannelExec)channel);
			channelExec.setCommand(command);
			channelExec.setErrStream(System.err);

			InputStream in;
			in = channel.getInputStream();
			channel.connect();
			byte[] tmp=new byte[1024];
			while(true){
				while(in.available()>0){
					int i=in.read(tmp, 0, 1024);
					if(i<0)break;
					journal.log(Level.INFO, ">> "+ new String(tmp, 0, i));
				}
				if(channel.isClosed()){
					journal.log(Level.INFO, ">> exit-status: "+channel.getExitStatus());
					break;
				}
				try{Thread.sleep(1000);}catch(Exception ee){}
			}

			channel.disconnect();
			session.disconnect();

		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			journal.log(Level.SEVERE,"File access error");
		}
	}

}
