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

import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cloudml.core.Provider;

public class ConnectorFactory {

    private static final Logger journal = Logger.getLogger(ConnectorFactory.class.getName());

    public static Connector createIaaSConnector(Provider p){
        try {
            if(p.getName().toLowerCase().equals("aws-ec2") || p.getName().toLowerCase().equals("ec2"))
                return new JCloudsConnector(p.getName(), p.getCredentials().getLogin(), p.getCredentials().getPassword());
            if(p.getName().toLowerCase().equals("cloudsigma2-zrh"))
                return new CloudSigmaConnector(p.getName(), p.getCredentials().getLogin(), p.getCredentials().getPassword());
            if(p.getName().toLowerCase().equals("flexiant"))
                return new FlexiantConnector(p.getProperties().valueOf("endPoint"), p.getCredentials().getLogin(), p.getCredentials().getPassword());
            if(p.getName().toLowerCase().equals("openstack-nova"))
                return new OpenStackConnector(p.getProperties().valueOf("endPoint"), p.getName(), p.getCredentials().getLogin(), p.getCredentials().getPassword());
        } catch (MalformedURLException e) {
            journal.log(Level.SEVERE, e.getMessage());
        }

        throw new IllegalArgumentException("No such connector");
    }

    /**
     * I had a hard-coded region of eu-west-1. Need to fix this some time
     * @param p
     * @return 
     */
    public static PaaSConnector createPaaSConnector(Provider p){
        if("beanstalk".equals(p.getName().toLowerCase()) || "ebs".equals(p.getName().toLowerCase())
                || "rds".equals(p.getName().toLowerCase()) || "sqs".equals(p.getName().toLowerCase()))
            return new BeanstalkConnector(p.getCredentials().getLogin(), p.getCredentials().getPassword(), "eu-west-1");
        if("cloudbees".equals(p.getName().toLowerCase()))
            return new Cloud4soaConnector(p);
        if("cf".equals(p.getName().toLowerCase()) || p.getName().toLowerCase().contains("cloudfoundry"))
            return new CloudFoundryConnector(p.getProperties().valueOf("endPoint"),p.getCredentials().getLogin(), p.getCredentials().getPassword(),
                    p.getProperties().valueOf("org"), p.getProperties().valueOf("space"));
        throw new IllegalArgumentException("No such connector");
    }
}
