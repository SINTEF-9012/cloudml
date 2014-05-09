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
package org.cloudml.core.samples;

import org.cloudml.core.Deployment;
import org.cloudml.core.ExternalComponent;
import org.cloudml.core.builders.DeploymentBuilder;
import org.cloudml.core.builders.ExternalComponentBuilder;
import org.cloudml.core.builders.InternalComponentBuilder;
import org.cloudml.core.builders.RelationshipBuilder;
import org.cloudml.core.credentials.FileCredentials;

import static org.cloudml.core.builders.Commons.*;

/**
 * Created by Nicolas Ferry on 08.05.14.
 */
public class PaasCloudBees {

    public static DeploymentBuilder completeCloudBeesPaaS() {

        ExternalComponentBuilder dbb = anExternalComponent()
                .named("cbdb")
                .with(
                        aProvidedPort().named("db")
                )
                .providedBy("CloudBees");
        
        ExternalComponentBuilder wserverb = anExternalComponent()
                .named("granny-cloudml")
                .with(
                        aProvidedExecutionPlatform()
                            .named("TomCat")
                )
                .providedBy("CloudBees");
        
        InternalComponentBuilder grannyb = anInternalComponent()
                .named("granny-war")
                .with(aRequiredExecutionPlatform().named("TomCat"))
                .withProperty("warfile", "C:\\temp\\granny-common.war")
                .withProperty("temp-warfile", "C:\\temp\\granny-common-temp.war")
                .with(aRequiredPort().remote().named("db").mandatory());
        
        RelationshipBuilder relb = aRelationship()
                .from("granny-war", "db")
                .to("cbdb", "db")
                .named("dbrel")
                .withResource(
                        aResource()
                            .named("dbconfig")
                            .withProperty("valet","war-xml")
                            .withProperty("entry_spring", "WEB-INF/classes/META-INF/spring/app-context.xml")
                            .withProperty("path_dburl", "@self{properties/entry_spring}:://bean[@id=\"dataSource\"]/property[@name=\"url\"]/@value")
                            .withProperty("value_dburl", "jdbc:mysql://@instance{providedEnd/owner/value/publicAddress}")
                            .withProperty("path_dbuser", "@self{properties/entry_spring}:://bean[@id=\"dataSource\"]/property[@name=\"username\"]/@value")
                            .withProperty("value_dbuser", "@instance{providedEnd/owner/value/type/login}")
                            .withProperty("path_dbpassword", "@self{properties/entry_spring}:://bean[@id=\"dataSource\"]/property[@name=\"username\"]/@value")
                            .withProperty("value_dbpassword", "@instance{providedEnd/owner/value/type/passwd}")
//                            .withProperty("valet", "war-xml")
//                            .withProperty("path", "WEB-INF/classes/META-INF/spring/app-context.xml")
//                            .withProperty("#getPublicAddress", "//bean[@id=\"dataSource\"]/property[@name=\"url\"]/@value")
//                            .withProperty("@#getPublicAddress-prefix", "jdbc:mysql://")
//                            .withProperty("##getLogin","//bean[@id=\"dataSource\"]/property[@name=\"username\"]/@value")
//                            .withProperty("##getPasswd","//bean[@id=\"dataSource\"]/property[@name=\"password\"]/@value")
                );
                
                
        
        DeploymentBuilder dmb = new DeploymentBuilder()
                .named("cloudbees-deployment")
                .with(aProvider()
                        .named("CloudBees")
                        .withProperty("account", "mod4cloud")
                )
                .with(dbb)
                .with(grannyb)
                .with(wserverb)
                .with(relb);
        dmb.with(anExternalComponentInstance()
                .named("cbdb1")
                .ofType("cbdb")
            ).with(anExternalComponentInstance()
                .named("granny-cloudml")
                .ofType("granny-cloudml")
            ).with(anInternalComponentInstance()
                .named("granny-war-i")
                .ofType("granny-war")
                .hostedBy("granny-cloudml")
            ).with(aRelationshipInstance()
                .named("dbreli")
                .ofType("dbrel")
                .from("granny-war-i", "db")
                .to("cbdb1", "db")
            )
            ;
        return dmb;
    }

}
