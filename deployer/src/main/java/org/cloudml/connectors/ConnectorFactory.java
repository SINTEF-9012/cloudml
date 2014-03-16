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

import org.cloudml.core.Provider;

public class ConnectorFactory {

	public static Connector createIaaSConnector(Provider p){
		try {
			if(p.getName().equals("aws-ec2"))
				return new JCloudsConnector(p.getName(), p.getLogin(), p.getPasswd());
			if(p.getName().equals("flexiant"))
				return new FlexiantConnector(p.getProperty("endPoint"), p.getLogin(), p.getPasswd());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		throw new IllegalArgumentException("No such connector");
	}

    public static PaaSConnector createPaaSConnector(Provider p){
        if(p.getName().equals("beanstalk"))
            return new BeanstalkConnector(p.getLogin(), p.getPasswd(), "");
        if(p.getName().equals("cloudbees"))
            return new Cloud4soaConnector(p);

        throw new IllegalArgumentException("No such connector");
    }
}
