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
package test.cloudml.codecs.library;

import org.cloudml.core.*;

/**
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class SampleModels {

    public CloudMLModel buildSensApp() {
        Provider amazon = new Provider("aws-ec2", "./credentials");
        Provider flexiant = new Provider("flexiant", "./credentials");
        flexiant.getProperties().add(new Property("endPoint", "https://api.sd1.flexiant.net:4442/userapi"));

        VM ml = new VM("ML");
        ml.setProvider(amazon);
        ml.setMinRam(1000);
        ml.setMinCores(2);
        ml.setMinStorage(50);
        ml.setLocation("eu-west-1b");
        ml.setOs("ubuntu");
        ml.setGroupName("sensapp");
        ml.setSshKey("cloudml");
        ml.setSecurityGroup("SensApp");
        ml.getProperties().add(new Property("KeyPath", "./cloudml.pem"));
        ml.setIs64os(true);

        VM sl = new VM("SL");
        sl.setProvider(flexiant);
        sl.setMinRam(1000);
        sl.setMinCores(1);
        sl.setMinStorage(50);
        sl.setLocation("");
        sl.setOs("ubuntu");
        sl.setGroupName("SensApp");
        sl.setSshKey("cloudml");
        sl.setSecurityGroup("SensApp");
        sl.getProperties().add(new Property("KeyPath", "./cloudml.pem"));
        sl.setIs64os(true);
        sl.setImageId("Ubuntu-SINTEF");

        VMInstance vmi1 = new VMInstance("sensapp-ML1", ml);
        VMInstance vmi2 = new VMInstance("sensapp-SL1", sl);

        InternalComponent ic1 = new InternalComponent("SensApp");
        RequiredPort rp = new RequiredPort("scRequired", ic1, true);
        rp.setPortNumber(0);
        rp.setIsLocal(true);
        ic1.getRequiredPorts().add(rp);
        RequiredPort rp2 = new RequiredPort("mongoDBRequired", ic1, true);
        rp2.setPortNumber(0);
        rp2.setIsLocal(true);
        ic1.getRequiredPorts().add(rp2);
        ProvidedPort pp = new ProvidedPort("rest", ic1, false);
        pp.setPortNumber(8080);
        ic1.getProvidedPorts().add(pp);
        Resource r = new Resource("sensAppWar");
        r.setRetrieveCommand("wget -P ~ http://github.com/downloads/SINTEF-9012/sensapp/sensapp.war; wget -P ~ http://ec2-54-228-116-115.eu-west-1.compute.amazonaws.com/scripts/linux/ubuntu/sensapp/sensapp.sh");
        r.setInstallCommand("cd ~; sudo bash sensapp.sh");
        ic1.getResources().add(r);

        InternalComponent sensappAdmin = new InternalComponent("SensAppAdmin");
        RequiredPort rpAdmin = new RequiredPort("scRequired", sensappAdmin, true);
        rpAdmin.setPortNumber(0);
        rpAdmin.setIsLocal(true);
        sensappAdmin.getRequiredPorts().add(rp);
        RequiredPort rest = new RequiredPort("restRequired", sensappAdmin, false);
        rest.setPortNumber(8080);
        rest.setIsLocal(false);
        sensappAdmin.getRequiredPorts().add(rest);
        Resource war = new Resource("sensAppAdminWar");
        war.setRetrieveCommand("wget -P ~ http://ec2-54-228-116-115.eu-west-1.compute.amazonaws.com/resources/sensappAdmin/SensAppGUI.tar; wget -P ~ http://ec2-54-228-116-115.eu-west-1.compute.amazonaws.com/scripts/linux/ubuntu/sensappAdmin/startsensappgui.sh ; wget -P ~ http://ec2-54-228-116-115.eu-west-1.compute.amazonaws.com/scripts/linux/ubuntu/sensappAdmin/sensappgui.sh ; wget -P ~ http://ec2-54-228-116-115.eu-west-1.compute.amazonaws.com/resources/sensappAdmin/localTopology.json; wget http://ec2-54-228-116-115.eu-west-1.compute.amazonaws.com/resources/sources.list; sudo mv sources.list /etc/apt/sources.list");
        war.setInstallCommand("cd ~; sudo bash sensappgui.sh");
        war.setStartCommand("cd ~; sudo bash startsensappgui.sh");
        sensappAdmin.getResources().add(war);

        InternalComponent jetty1 = new InternalComponent("JettySC");
        Resource binary = new Resource("jettyBin");
        binary.setRetrieveCommand("wget -P ~ http://ec2-54-228-116-115.eu-west-1.compute.amazonaws.com/scripts/linux/ubuntu/jetty/jetty.sh");
        binary.setInstallCommand("cd ~; sudo bash jetty.sh");
        jetty1.getResources().add(binary);
        ProvidedPort jettyProc = new ProvidedPort("sc", jetty1, true);
        jettyProc.setPortNumber(0);
        jetty1.getProvidedPorts().add(jettyProc);

        InternalComponent mongo = new InternalComponent("mongoDB");
        Resource mongoBin = new Resource("MongoDBBin");
        mongoBin.setRetrieveCommand("wget -P ~ http://ec2-54-228-116-115.eu-west-1.compute.amazonaws.com/scripts/linux/ubuntu/mongoDB/mongoDB.sh");
        mongoBin.setInstallCommand("cd ~; sudo bash mongoDB.sh");
        mongo.getResources().add(mongoBin);
        ProvidedPort mongoProv = new ProvidedPort("mongoDB", mongo, false);
        mongoProv.setPortNumber(0);
        mongo.getProvidedPorts().add(mongoProv);

        InternalComponentInstance ici1 = ic1.instantiates("sensApp1");
        ici1.setDestination(vmi1);
        RequiredPortInstance rpi = new RequiredPortInstance("scRequired1", rp, ici1);
        RequiredPortInstance rpi2 = new RequiredPortInstance("mongoDBRequired1", rp2, ici1);
        ici1.getRequiredPortInstances().add(rpi);
        ici1.getRequiredPortInstances().add(rpi2);
        ProvidedPortInstance ppi = new ProvidedPortInstance("rest1", pp, ici1);
        ici1.getProvidedPortInstances().add(ppi);

        InternalComponentInstance sensAppAdmin = sensappAdmin.instantiates("sensAppAdmin1");
        sensAppAdmin.setDestination(vmi2);
        RequiredPortInstance rpAdmini = new RequiredPortInstance("scRequired2", rpAdmin, sensAppAdmin);
        RequiredPortInstance resti = new RequiredPortInstance("restRequired1", rest, sensAppAdmin);
        sensAppAdmin.getRequiredPortInstances().add(rpAdmini);
        sensAppAdmin.getRequiredPortInstances().add(resti);

        InternalComponentInstance jettyi = jetty1.instantiates("jettySC1");
        jettyi.setDestination(vmi1);
        ProvidedPortInstance sc1 = new ProvidedPortInstance("sc1", jettyProc, jettyi);
        jettyi.getProvidedPortInstances().add(sc1);

        InternalComponentInstance jettyi2 = jetty1.instantiates("jettySC2");
        jettyi2.setDestination(vmi2);
        ProvidedPortInstance sc2 = new ProvidedPortInstance("sc2", jettyProc, jettyi2);
        jettyi2.getProvidedPortInstances().add(sc2);

        InternalComponentInstance mongo1 = mongo.instantiates("mongoDB1");
        mongo1.setDestination(vmi1);
        ProvidedPortInstance mon = new ProvidedPortInstance("mongoDB1", mongoProv, mongo1);
        mongo1.getProvidedPortInstances().add(mon);

        Relationship sensappsc = new Relationship("SensAppSC");
        sensappsc.setProvidedPort(jettyProc);
        sensappsc.setRequiredPort(rp);

        Relationship sensappmongo = new Relationship("SensAppMongoDB");
        sensappmongo.setProvidedPort(mongoProv);
        sensappmongo.setRequiredPort(rp2);

        Relationship sensappadminsc = new Relationship("SensAppAdminSC");
        sensappadminsc.setProvidedPort(jettyProc);
        sensappadminsc.setRequiredPort(rpAdmin);

        Relationship sensappadminsensapp = new Relationship("SensAppAdminSensApp");
        sensappadminsensapp.setProvidedPort(pp);
        sensappadminsensapp.setRequiredPort(rest);
        Resource br = new Resource("client");
        br.setRetrieveCommand("wget -P ~ http://ec2-54-228-116-115.eu-west-1.compute.amazonaws.com/scripts/linux/ubuntu/sensappAdmin/configuresensappgui.sh");
        br.setInstallCommand("cd ~; sudo bash configuresensappgui.sh");
        sensappadminsensapp.setClientResource(br);

        RelationshipInstance sensappsc1 = sensappsc.instantiates("sensAppSC1");
        sensappsc1.setProvidedPortInstance(sc1);
        sensappsc1.setRequiredPortInstance(rpi);

        RelationshipInstance sensappmongo1 = sensappmongo.instantiates("sensAppMongoDB1");
        sensappmongo1.setProvidedPortInstance(mon);
        sensappmongo1.setRequiredPortInstance(rpi2);

        RelationshipInstance sensappadminsc1 = sensappadminsc.instantiates("sensAppAdminSC1");
        sensappadminsc1.setProvidedPortInstance(sc2);
        sensappadminsc1.setRequiredPortInstance(rpAdmini);

        RelationshipInstance sensappadminsensapp1 = sensappadminsensapp.instantiates("sensAppAdminSensApp1");
        sensappadminsensapp1.setProvidedPortInstance(ppi);
        sensappadminsensapp1.setRequiredPortInstance(resti);

        CloudMLModel model = new CloudMLModel("SensAppAdmnin");
        model.getProviders().add(amazon);
        model.getProviders().add(flexiant);
        model.getExternalComponents().put("ml", ml);
        model.getExternalComponents().put("sl", sl);
        model.getComponents().put("ic1", ic1);
        model.getComponents().put("sensappAdmin", sensappAdmin);
        model.getComponents().put("mongo", mongo);
        model.getComponents().put("jetty1", jetty1);
        model.getExternalComponentInstances().add(vmi1);
        model.getExternalComponentInstances().add(vmi2);
        model.getComponentInstances().add(sensAppAdmin);
        model.getComponentInstances().add(ici1);
        model.getComponentInstances().add(jettyi);
        model.getComponentInstances().add(jettyi2);
        model.getComponentInstances().add(mongo1);
        model.getRelationships().put("sensappsc", sensappsc);
        model.getRelationships().put("sensappadminsc", sensappadminsc);
        model.getRelationships().put("sensappmongo", sensappmongo);
        model.getRelationships().put("sensappadminsensapp", sensappadminsensapp);
        model.getRelationshipInstances().add(sensappsc1);
        model.getRelationshipInstances().add(sensappmongo1);
        model.getRelationshipInstances().add(sensappadminsc1);
        model.getRelationshipInstances().add(sensappadminsensapp1);

        return model;
    }

}