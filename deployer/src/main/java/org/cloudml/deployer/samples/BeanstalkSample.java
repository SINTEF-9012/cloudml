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
package org.cloudml.deployer.samples;


import javax.xml.xpath.XPath;
import org.cloudml.connectors.util.JXPath;
import org.cloudml.core.Deployment;
import org.cloudml.core.ExecuteInstance;
import org.cloudml.core.ExternalComponent;
import org.cloudml.core.ExternalComponentInstance;
import org.cloudml.core.InternalComponent;
import org.cloudml.core.InternalComponentInstance;
import org.cloudml.core.Property;
import org.cloudml.core.ProvidedExecutionPlatform;
import org.cloudml.core.ProvidedExecutionPlatformInstance;
import org.cloudml.core.ProvidedPort;
import org.cloudml.core.ProvidedPortInstance;
import org.cloudml.core.Provider;
import org.cloudml.core.Relationship;
import org.cloudml.core.RelationshipInstance;
import org.cloudml.core.RequiredExecutionPlatform;
import org.cloudml.core.RequiredPort;
import org.cloudml.core.RequiredPortInstance;
import org.cloudml.core.Resource;
import static org.cloudml.core.builders.Commons.aProvidedExecutionPlatform;
import static org.cloudml.core.builders.Commons.aProvidedPort;
import static org.cloudml.core.builders.Commons.aProvider;
import static org.cloudml.core.builders.Commons.aRelationship;
import static org.cloudml.core.builders.Commons.aRelationshipInstance;
import static org.cloudml.core.builders.Commons.aRequiredExecutionPlatform;
import static org.cloudml.core.builders.Commons.aRequiredPort;
import static org.cloudml.core.builders.Commons.aResource;
import static org.cloudml.core.builders.Commons.anExternalComponent;
import static org.cloudml.core.builders.Commons.anExternalComponentInstance;
import static org.cloudml.core.builders.Commons.anInternalComponent;
import static org.cloudml.core.builders.Commons.anInternalComponentInstance;
import org.cloudml.core.builders.DeploymentBuilder;
import org.cloudml.core.builders.ExternalComponentBuilder;
import org.cloudml.core.builders.InternalComponentBuilder;
import org.cloudml.core.builders.ProviderBuilder;
import org.cloudml.core.builders.RelationshipBuilder;
import org.cloudml.core.credentials.FileCredentials;
import org.cloudml.deployer.CloudAppDeployer;

/**
 * Sample deployment model for {@link http://osintegrators.com/PaasInstallNotes} on 
 * CloudBees. The sample use mysql database, and therefore requires a slightly 
 * adapted war-file than the one downloaded directly from the website (which
 * utilises HSQL).
 * 
 * Required files before deployment
 *  - war-file: "C:\\temp\\granny-common.war", which can be downloaded from 
 *    {@link https://www.dropbox.com/s/xu0dg4p9dwijght/granny-common.war}
 *  - credential: "c:\\temp\\cloudbees.credential", which you can obtain by 
 *    registering a free CloudBees account, or by asking me for a copy of ours
 *    {@link hui.song@sintef.no}
 * 
 * The deployment model is rather straightforward:
 * 
 * Type-level
 * 
 *     granny-war : InternalComponent  ------------> cbdb : ExternalComponent
 *                |                dbrel : Relationship
 *                | hostedby
 *                V
 *    granny-cloudml : ExternalComponent 
 * 
 * Instance-level
 * 
 *    granny-war-1 : grannywar  -----------> cbdb1 : cbdb
 *                 |
 *                 |  dbreli : dbrel
 *                 V
 *   granny-cloudml : granny-cloudml
 *
 */
public class BeanstalkSample 
{
    
    public static void main(String[] args){
        
        Deployment dm = createCloudBeesDeployment();
        
        CloudAppDeployer deployer = new CloudAppDeployer();
        deployer.deploy(dm);
        
    }
    
    public static Deployment createCloudBeesDeployment(){         
 
        DeploymentBuilder dmb = org.cloudml.core.samples.PaasCloudBees.completeCloudBeesPaaS();
                
        Deployment dm = dmb.build();
        dm.getProviders().firstNamed("CloudBees").setName("beanstalk");
        dm.getProviders().firstNamed("beanstalk").setCredentials(new FileCredentials("c:\\temp\\aws.credential"));
        ExternalComponent c = dm.getComponents().onlyExternals().firstNamed("cbdb");
        c.setServiceType("database");
        c.setLogin("sintef");
        c.setPasswd("password123");
        c.getProperties().add(new Property("DB-Engine","MySQL"));
        c.getProperties().add(new Property("DB-Version","5.6.17"));
        c.getProperties().add(new Property("DB-Name","cbdb"));
        System.out.println(dm);
                
        return dm;

    }

}
