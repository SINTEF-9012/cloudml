package org.cloudml.core.actions;

import org.cloudml.core.*;
import org.cloudml.core.collections.ProvidedExecutionPlatformGroup;

import java.util.ArrayList;

/**
 * Created by nicolasf on 11.03.15.
 */
public class CloneVM  extends AbstractAction<VMInstance>{


    private final VMInstance vmi;

    public CloneVM(StandardLibrary library, VMInstance instance) {
        super(library);
        this.vmi = instance;
    }

    @Override
    public VMInstance applyTo(Deployment target) {
        VM existingVM=vmi.asExternal().asVM().getType();
        VM v=target.getComponents().onlyVMs().firstNamed(existingVM.getName()+"-fromImage");
        StandardLibrary lib=getLibrary();
        if(v == null){//in case a type for the snapshot has already been created
            String name=lib.createUniqueComponentInstanceName(target,existingVM);
            v=new VM(name+"-fromImage",existingVM.getProvider());
            v.setGroupName(existingVM.getGroupName());
            v.setRegion(existingVM.getRegion());
            v.setImageId("tempID");
            v.setLocation(existingVM.getLocation());
            v.setMinRam(existingVM.getMinRam());
            v.setMinCores(existingVM.getMinCores());
            v.setMinStorage(existingVM.getMinStorage());
            v.setSecurityGroup(existingVM.getSecurityGroup());
            v.setSshKey(existingVM.getSshKey());
            v.setPrivateKey(existingVM.getPrivateKey());
            v.setProvider(existingVM.getProvider());
            ProvidedExecutionPlatformGroup pepg=new ProvidedExecutionPlatformGroup();
            for(ProvidedExecutionPlatform pep: existingVM.getProvidedExecutionPlatforms()){
                ArrayList<Property> pg=new ArrayList<Property>();
                for(Property property: pep.getOffers()){
                    Property prop=new Property(property.getName());
                    prop.setValue(property.getValue());
                    pg.add(prop);
                }
                ProvidedExecutionPlatform p =new ProvidedExecutionPlatform(name+"-"+pep.getName(),pg);
                pepg.add(p);
            }
            v.setProvidedExecutionPlatforms(pepg.toList());
            target.getComponents().add(v);
        }
        VMInstance ci=lib.provision(target,v).asExternal().asVM();
        return ci;
    }
}
