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

        VM ml=new VM("ML");
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
        ProvidedExecutionPlatform mlPep = new ProvidedExecutionPlatform();
        mlPep.setName("mlProvided");
        mlPep.setOwner(ml);
        ml.getProvidedExecutionPlatforms().add(mlPep);

        VM sl=new VM("SL");
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
        ProvidedExecutionPlatform slPep = new ProvidedExecutionPlatform();
        slPep.setName("mlProvided");
        slPep.setOwner(sl);
        sl.getProvidedExecutionPlatforms().add(slPep);

        VMInstance vmi1=new VMInstance("sensapp-ML1",ml);
        ProvidedExecutionPlatformInstance vmi1pep=new ProvidedExecutionPlatformInstance("mlProvided1",mlPep);
        vmi1pep.setOwner(vmi1);
        vmi1.getProvidedExecutionPlatformInstances().add(vmi1pep);
        VMInstance vmi2=new VMInstance("sensapp-SL1",sl);
        ProvidedExecutionPlatformInstance vmi2pep=new ProvidedExecutionPlatformInstance("slProvided1", slPep);
        vmi2pep.setOwner(vmi2);
        vmi2.getProvidedExecutionPlatformInstances().add(vmi2pep);

        InternalComponent ic1=new InternalComponent("SensApp");
        RequiredExecutionPlatform rep=new RequiredExecutionPlatform("scRequired");
        rep.setOwner(ic1);
        ic1.setRequiredExecutionPlatform(rep);
        RequiredPort rp2=new RequiredPort("mongoDBRequired",ic1,true);
        rp2.setPortNumber(0);
        rp2.setIsLocal(true);
        ic1.getRequiredPorts().add(rp2);
        ProvidedPort pp=new ProvidedPort("rest", ic1, false);
        pp.setPortNumber(8080);
        ic1.getProvidedPorts().add(pp);
        Resource r=new Resource("sensAppWar");
        r.setRetrieveCommand("wget -P ~ http://github.com/downloads/SINTEF-9012/sensapp/sensapp.war; wget -P ~ http://ec2-54-228-116-115.eu-west-1.compute.amazonaws.com/scripts/linux/ubuntu/sensapp/sensapp.sh");
        r.setInstallCommand("cd ~; sudo bash sensapp.sh");
        ic1.getResources().add(r);

        InternalComponent sensappAdmin=new InternalComponent("SensAppAdmin");
        RequiredExecutionPlatform repiAdmin=new RequiredExecutionPlatform("scRequired");
        repiAdmin.setOwner(sensappAdmin);
        sensappAdmin.setRequiredExecutionPlatform(repiAdmin);
        RequiredPort rest=new RequiredPort("restRequired",sensappAdmin, false);
        rest.setPortNumber(8080);
        rest.setIsLocal(false);
        sensappAdmin.getRequiredPorts().add(rest);
        Resource war=new Resource("sensAppAdminWar");
        war.setRetrieveCommand("wget -P ~ http://ec2-54-228-116-115.eu-west-1.compute.amazonaws.com/resources/sensappAdmin/SensAppGUI.tar; wget -P ~ http://ec2-54-228-116-115.eu-west-1.compute.amazonaws.com/scripts/linux/ubuntu/sensappAdmin/startsensappgui.sh ; wget -P ~ http://ec2-54-228-116-115.eu-west-1.compute.amazonaws.com/scripts/linux/ubuntu/sensappAdmin/sensappgui.sh ; wget -P ~ http://ec2-54-228-116-115.eu-west-1.compute.amazonaws.com/resources/sensappAdmin/localTopology.json; wget http://ec2-54-228-116-115.eu-west-1.compute.amazonaws.com/resources/sources.list; sudo mv sources.list /etc/apt/sources.list");
        war.setInstallCommand("cd ~; sudo bash sensappgui.sh");
        war.setStartCommand("cd ~; sudo bash startsensappgui.sh");
        sensappAdmin.getResources().add(war);

        InternalComponent jetty1=new InternalComponent("JettySC");
        Resource binary=new Resource("jettyBin");
        binary.setRetrieveCommand("wget -P ~ http://ec2-54-228-116-115.eu-west-1.compute.amazonaws.com/scripts/linux/ubuntu/jetty/jetty.sh");
        binary.setInstallCommand("cd ~; sudo bash jetty.sh");
        jetty1.getResources().add(binary);
        ProvidedExecutionPlatform jettyProc=new ProvidedExecutionPlatform("sc");
        jettyProc.setOwner(jetty1);
        jetty1.getProvidedExecutionPlatforms().add(jettyProc);
        RequiredExecutionPlatform jettyRep=new RequiredExecutionPlatform("ml");
        jettyRep.setOwner(jetty1);
        jetty1.setRequiredExecutionPlatform(jettyRep);

        InternalComponent mongo=new InternalComponent("mongoDB");
        Resource mongoBin=new Resource("MongoDBBin");
        mongoBin.setRetrieveCommand("wget -P ~ http://ec2-54-228-116-115.eu-west-1.compute.amazonaws.com/scripts/linux/ubuntu/mongoDB/mongoDB.sh");
        mongoBin.setInstallCommand("cd ~; sudo bash mongoDB.sh");
        mongo.getResources().add(mongoBin);
        ProvidedPort mongoProv=new ProvidedPort("mongoDB",mongo,false);
        mongoProv.setPortNumber(0);
        mongo.getProvidedPorts().add(mongoProv);
        RequiredExecutionPlatform mongoRep=new RequiredExecutionPlatform("ml");
        mongoRep.setOwner(mongo);
        mongo.setRequiredExecutionPlatform(mongoRep);

        InternalComponentInstance ici1=ic1.instantiates("sensApp1");
        RequiredExecutionPlatformInstance repi=new RequiredExecutionPlatformInstance("scRequired1", rep);
        repi.setOwner(ici1);
        RequiredPortInstance rpi2=new RequiredPortInstance("mongoDBRequired1",rp2,ici1);
        ici1.setRequiredExecutionPlatformInstance(repi);
        ici1.getRequiredPortInstances().add(rpi2);
        ProvidedPortInstance ppi=new ProvidedPortInstance("rest1",pp,ici1);
        ici1.getProvidedPortInstances().add(ppi);

        InternalComponentInstance sensAppAdmin=sensappAdmin.instantiates("sensAppAdmin1");
        RequiredExecutionPlatformInstance repiAdmini=new RequiredExecutionPlatformInstance("scRequired2",repiAdmin);
        repiAdmini.setOwner(sensAppAdmin);
        sensAppAdmin.setRequiredExecutionPlatformInstance(repiAdmini);
        RequiredPortInstance resti=new RequiredPortInstance("restRequired1",rest,sensAppAdmin);
        sensAppAdmin.getRequiredPortInstances().add(resti);

        InternalComponentInstance jettyi=jetty1.instantiates("jettySC1");
        RequiredExecutionPlatformInstance repi1=new RequiredExecutionPlatformInstance("ml1",jettyRep);
        repi1.setOwner(jettyi);
        jettyi.setRequiredExecutionPlatformInstance(repi1);
        ProvidedExecutionPlatformInstance pepi1=new ProvidedExecutionPlatformInstance("sc1",jettyProc);
        pepi1.setOwner(jettyi);
        jettyi.getProvidedExecutionPlatformInstances().add(pepi1);

        InternalComponentInstance jettyi2=jetty1.instantiates("jettySC2");
        RequiredExecutionPlatformInstance repi2=new RequiredExecutionPlatformInstance("ml2",jettyRep);
        repi2.setOwner(jettyi2);
        jettyi2.setRequiredExecutionPlatformInstance(repi2);
        ProvidedExecutionPlatformInstance pepi2=new ProvidedExecutionPlatformInstance("sc2",jettyProc);
        pepi2.setOwner(jettyi2);
        jettyi2.getProvidedExecutionPlatformInstances().add(pepi2);

        InternalComponentInstance mongo1=mongo.instantiates("mongoDB1");
        RequiredExecutionPlatformInstance mongoRepi=new RequiredExecutionPlatformInstance("ml2",jettyRep);
        mongoRepi.setOwner(mongo1);
        mongo1.setRequiredExecutionPlatformInstance(mongoRepi);
        ProvidedPortInstance mon=new ProvidedPortInstance("mongoDB1",mongoProv,mongo1);
        mongo1.getProvidedPortInstances().add(mon);

        ExecuteInstance sensappsc=new ExecuteInstance("SensAppSC", pepi1, repi);

        ExecuteInstance sensappadminsc=new ExecuteInstance("SensAppAdminSC", pepi2, repiAdmini);

        ExecuteInstance jetty1ml=new ExecuteInstance("jetty1ml", vmi1pep, repi1);

        ExecuteInstance jetty2sl=new ExecuteInstance("jetty2sl", vmi2pep, repi2);

        ExecuteInstance mongoml=new ExecuteInstance("mongoML", vmi1pep, mongoRepi);

        Relationship sensappmongo=new Relationship("SensAppMongoDB");
        sensappmongo.setProvidedPort(mongoProv);
        sensappmongo.setRequiredPort(rp2);


        Relationship sensappadminsensapp=new Relationship("SensAppAdminSensApp");
        sensappadminsensapp.setProvidedPort(pp);
        sensappadminsensapp.setRequiredPort(rest);
        Resource br=new Resource("client");
        br.setRetrieveCommand("wget -P ~ http://ec2-54-228-116-115.eu-west-1.compute.amazonaws.com/scripts/linux/ubuntu/sensappAdmin/configuresensappgui.sh");
        br.setInstallCommand("cd ~; sudo bash configuresensappgui.sh");
        sensappadminsensapp.setClientResource(br);

        RelationshipInstance sensappmongo1=sensappmongo.instantiates("sensAppMongoDB1");
        sensappmongo1.setProvidedPortInstance(mon);
        sensappmongo1.setRequiredPortInstance(rpi2);

        RelationshipInstance sensappadminsensapp1=sensappadminsensapp.instantiates("sensAppAdminSensApp1");
        sensappadminsensapp1.setProvidedPortInstance(ppi);
        sensappadminsensapp1.setRequiredPortInstance(resti);

        CloudMLModel model=new CloudMLModel("SensAppAdmnin");
        model.getProviders().add(amazon);
        model.getProviders().add(flexiant);
        model.getComponents().put("ml",ml);
        model.getComponents().put("sl",sl);
        model.getComponents().put("ic1",ic1);
        model.getComponents().put("sensappAdmin",sensappAdmin);
        model.getComponents().put("mongo",mongo);
        model.getComponents().put("jetty1",jetty1);
        model.getComponentInstances().add(vmi1);
        model.getComponentInstances().add(vmi2);
        model.getComponentInstances().add(sensAppAdmin);
        model.getComponentInstances().add(ici1);
        model.getComponentInstances().add(jettyi);
        model.getComponentInstances().add(jettyi2);
        model.getComponentInstances().add(mongo1);
        model.getRelationships().put("sensappmongo",sensappmongo);
        model.getRelationships().put("sensappadminsensapp",sensappadminsensapp);
        model.getExecuteInstances().add(sensappsc);
        model.getExecuteInstances().add(sensappadminsc);
        model.getExecuteInstances().add(jetty1ml);
        model.getExecuteInstances().add(jetty2sl);
        model.getExecuteInstances().add(mongoml);
        model.getRelationshipInstances().add(sensappmongo1);
        model.getRelationshipInstances().add(sensappadminsensapp1);


        return model;
    }

}