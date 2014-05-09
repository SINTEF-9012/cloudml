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
 */
package org.cloudml.core.samples;

import org.cloudml.core.builders.*;

import static org.cloudml.core.builders.Commons.*;

/**
 * Build the deployment model of SensApp
 */
public class SensApp {

    public static final String AMAZON_EC2 = "aws-ec2";
    public static final String OPENSTACK = "openstack-nova";
    public static final String FLEXIANT = "flexiant";
    public static final String SENSAPP = "SensApp";
    public static final String JETTY = "JettySC";
    public static final String MONGO_DB = "mongoDB";
    public static final String SENSAPP_ADMIN = "SensAppAdmin";
    public static final String SENSAPP_DB_PORT = "mongoDBRequired";
    public static final String DB_PORT = "mongoDB";
    public static final String SENSAPP_ADMIN_PORT = "restRequired";
    public static final String SENSAPP_USER_PORT = "rest";
    public static final String MONGO_DB_1 = "mongoDB1";
    public static final String JETTY_2 = "jettySC2";
    public static final String JETTY_1 = "jettySC1";
    public static final String SENSAPP_1 = "sensApp1";
    public static final String SENSAPP_ADMIN_1 = "sensAppAdmin1";
    public static final String SENSAPP_ADMIN_TO_SENSAPP = "SensAppAdminSensApp";
    public static final String SENSAPP_TO_MONGO_DB = "SensAppMongoDB";
    public static final String SENSAPP_ADMIN_VM = "sensapp-SL1";
    public static final String SENSAPP_VM = "sensapp-ML1";

    public static DeploymentBuilder completeSensApp() {
        return sensAppTypes()
                .with(aVMInstance()
                    .named(SENSAPP_VM)
                    .ofType("ML"))
                .with(aVMInstance()
                    .named(SENSAPP_ADMIN_VM)
                    .ofType("SL"))
                .with(anInternalComponentInstance()
                    .named(JETTY_1)
                    .ofType(JETTY)
                    .hostedBy(SENSAPP_VM))
                .with(anInternalComponentInstance()
                    .named(SENSAPP_1)
                    .ofType(SENSAPP)
                    .hostedBy(JETTY_1))
                .with(anInternalComponentInstance()
                    .named(MONGO_DB_1)
                    .ofType(MONGO_DB)
                    .hostedBy(SENSAPP_VM))
                .with(anInternalComponentInstance()
                    .named(JETTY_2)
                    .ofType(JETTY)
                    .hostedBy(SENSAPP_ADMIN_VM))
                .with(anInternalComponentInstance()
                    .named(SENSAPP_ADMIN_1)
                    .ofType(SENSAPP_ADMIN)
                    .hostedBy(JETTY_2))
                .with(aRelationshipInstance()
                    .named("sensAppMongoDB1")
                    .ofType(SENSAPP_TO_MONGO_DB) 
                    .from(SENSAPP_1, SENSAPP_DB_PORT)
                    .to(MONGO_DB_1, DB_PORT))
                 .with(aRelationshipInstance()
                    .named("sensAppAdminSensApp1")
                    .ofType(SENSAPP_ADMIN_TO_SENSAPP)
                    .from(SENSAPP_ADMIN_1, SENSAPP_ADMIN_PORT)
                    .to(SENSAPP_1, SENSAPP_USER_PORT))
                ;
    }
    
    private static VMBuilder vmML() {
        return aVM()
                .named("ML")
                .providedBy(FLEXIANT)
                .with(aProvidedExecutionPlatform()
                    .named("m1Provided")
                    .offering("OS", "Ubuntu")) // FIXME offering of the execution platform
                .withMinRam(1000)
                .withMinCores(2)
                .withMinStorage(50)
                .withLocation("eu-west-1b")
                .withOS("ubuntu")
                .withGroupName("sensapp")
                .withSshKey("cloudml")
                .withProperty("KeyPath", "./cloudml.pem")
                .withSecurityGroup("SensApp")
                .with64OS();
    }

    public static VMBuilder vmSL() {
        return aVM()
                .named("SL")
                .providedBy(FLEXIANT)
                .with(aProvidedExecutionPlatform()
                    .named("s1Provided")
                    .offering("OS", "Ubuntu"))
                .withMinRam(1000)
                .withMinCores(1)
                .withMinStorage(50)
                .withOS("ubuntu")
                .withGroupName("SensApp")
                .withSshKey("cloudml")
                .withProperty("KeyPath", "./cloudml.pem")
                .withSecurityGroup("SensApp")
                .withImageId("Ubuntu-SINTEF")
                .with64OS();
    }

    public static ProviderBuilder ec2() {
        return aProvider() // FIXME: Setup the credentials
                .named(AMAZON_EC2);
    }

    public static ProviderBuilder flexiant() {
        return aProvider()
                .named(FLEXIANT)
                .withProperty("endPoint", "https://api.sd1.flexiant.net:4442/userapi");
    }

    public static ProviderBuilder minicloud() {
        return aProvider().named(OPENSTACK).withProperty("endPoint", "http://192.168.1.10:5000/v2.0");
    }

    public static InternalComponentBuilder sensApp() { 
        return anInternalComponent() 
             .named(SENSAPP)
             .with(aRequiredExecutionPlatform()
                 .named("scRequired")
                 .demanding("Servlet Container", "true"))  // FIXME: Should be a War container, right?
             .with(aRequiredPort()
                 .named(SENSAPP_DB_PORT)
                 .local())
             .with(aProvidedPort()
                 .named(SENSAPP_USER_PORT)
                 .remote()
                 .withPortNumber(8080))
             .withResource(aResource()
                 .named("sensAppWar")
                 .retrievedBy("wget -P ~ http://github.com/downloads/SINTEF-9012/sensapp/sensapp.war; wget -P ~ http://cloudml.org/scripts/linux/ubuntu/sensapp/install_start_sensapp.sh")
                 .installedBy("cd ~; sudo bash install_start_sensapp.sh"));
    }

    public static InternalComponentBuilder sensAppAdmin() {
        return anInternalComponent()
             .named(SENSAPP_ADMIN)
             .with(aRequiredExecutionPlatform()
                 .named("scRequired")
                 .demanding("Servlet Container", "true"))
             .with(aRequiredPort()
                 .named(SENSAPP_ADMIN_PORT)
                 .remote()
                 .mandatory()
                 .withPortNumber(8080))
             .withResource(aResource()
                 .named("sensAppAdminWar")
                 .retrievedBy("wget -P ~ http://cloudml.org/resources/sensappAdmin/SensAppAdmin.tar; wget -P ~ http://cloudml.org/scripts/linux/ubuntu/sensappAdmin/start_sensappadmin.sh ; wget -P ~ http://cloudml.org/scripts/linux/ubuntu/sensappAdmin/install_sensappadmin.sh ; wget -P ~ http://cloudml.org/resources/sensappAdmin/localTopology.json")
                 .installedBy("cd ~; sudo bash install_sensappadmin.sh")
                 .startedBy("cd ~; sudo bash start_sensappadmin.sh")
                 .stoppedBy("sudo rm -rf /opt/jetty/webapps/SensAppGUI ; sudo service jetty restart"));
    }

    public static InternalComponentBuilder jetty() {
        return anInternalComponent()
             .named(JETTY)
             .with(aProvidedExecutionPlatform()
                 .named("sc")
                 .offering("Servlet Container", "true"))
             .with(aRequiredExecutionPlatform()
                 .named("ml")
                 .demanding("OS", "Ubuntu"))
             .withResource(aResource()
                 .named("jettyBin")
                 .retrievedBy("wget -P ~ http://cloudml.org/scripts/linux/ubuntu/jetty/install_jetty.sh")
                 .installedBy("cd ~; sudo bash install_jetty.sh")
                 .stoppedBy("sudo service jetty stop"));
    }

    public static InternalComponentBuilder mongoDB() {
        return anInternalComponent()
             .named(MONGO_DB)
             .with(aRequiredExecutionPlatform()
                 .named("sl")
                 .demanding("OS", "Ubuntu"))
             .with(aProvidedPort()
                 .named(DB_PORT)
                 .remote()
                 .withPortNumber(0))
             .withResource(aResource()
                 .named("MongoDBBin")
                 .retrievedBy("wget -P ~ http://cloudml.org/scripts/linux/ubuntu/mongoDB/install_mongoDB.sh")
                 .installedBy("cd ~; sudo bash install_mongoDB.sh"));
    }

    public static RelationshipBuilder sensappToMongoDB() {
        return aRelationship()
             .named(SENSAPP_TO_MONGO_DB)
             .from(SENSAPP, SENSAPP_DB_PORT)
             .to(MONGO_DB, DB_PORT);
    }

    public static RelationshipBuilder sensappAdminToSensapp() {
        return aRelationship()
             .named(SENSAPP_ADMIN_TO_SENSAPP)
             .from(SENSAPP_ADMIN, SENSAPP_ADMIN_PORT)
             .to(SENSAPP, SENSAPP_USER_PORT)
             .withResource(aResource()
                 .named("client")
                 .retrievedBy("get -P ~ http://cloudml.org/scripts/linux/ubuntu/sensappAdmin/configure_sensappadmin.sh")
                 .installedBy("cd ~; sudo bash configure_sensappadmin.sh"));
    }

    public static DeploymentBuilder sensAppTypes() {
        return aDeployment()
                .named("SensApp")
                .with(ec2())
                .with(flexiant())
                .with(minicloud())
                .with(vmML())
                .with(vmSL())
                .with(sensApp())
                .with(sensAppAdmin())
                .with(jetty())
                .with(mongoDB())
                .with(sensappToMongoDB())
                .with(sensappAdminToSensapp());
    }
}
