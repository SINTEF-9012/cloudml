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
package test.cloudml.codecs.kmf;

import net.cloudml.core.ProvidedExecutionPlatformInstance;
import net.cloudml.core.RequiredExecutionPlatform;

import java.util.ArrayList;
import java.util.List;

/**
 * Match CloudML objects against KMY POJO Object
 */
public class Matcher {

    public boolean match(net.cloudml.core.Provider kprovider, org.cloudml.core.Provider cprovider) {
        if(kprovider != null && cprovider != null)
            return cprovider.getName().equals(kprovider.getName());
        else return false;
    }

    public boolean match(net.cloudml.core.Component kc, org.cloudml.core.VM cvm) {
        if(kc != null && cvm != null){
            if (kc instanceof net.cloudml.core.VM) {
                return matchVM((net.cloudml.core.VM) kc, cvm);
            }
        }
        return false;
    }

    public boolean matchVM(net.cloudml.core.VM kvm, org.cloudml.core.VM cvm) {
        if(kvm != null && cvm != null){
            return cvm.getName().equals(kvm.getName())
                    && match(kvm.getProvider(), cvm.getProvider());
        }
        return false;
    }

    public boolean matchVMInstance(net.cloudml.core.VMInstance kvi, org.cloudml.core.VMInstance cvi) {
        if(kvi != null && cvi != null){
            return cvi.getName().equals(kvi.getName())
                    && match(kvi.getType(), cvi.getType());
        }
        return false;
    }

    public boolean matchRelationship(net.cloudml.core.Relationship kr, org.cloudml.core.Relationship cr) {
        if(kr != null && cr != null){
            return cr.getRequiredPort().getName().equals(kr.getRequiredPort().getName())
                    && cr.getProvidedPort().getName().equals(kr.getProvidedPort().getName());
        }
        return false;
    }

    public boolean matchRelationshipInstance(net.cloudml.core.RelationshipInstance kri, org.cloudml.core.RelationshipInstance cri) {
        if(kri != null && cri != null){
            return cri.getRequiredPortInstance().getName().equals(kri.getRequiredPortInstance().getName())
                    && cri.getProvidedPortInstance().getName().equals(kri.getProvidedPortInstance().getName());
        }
        return false;
    }

    public boolean matchIC(net.cloudml.core.InternalComponent kic, org.cloudml.core.InternalComponent cic) {
        if(kic != null && cic != null){
            return cic.getName().equals(kic.getName());
        }
        return false;
    }

    public boolean matchIC(net.cloudml.core.Component kc, org.cloudml.core.InternalComponent cic) {
        if(kc != null && cic != null){
            if (kc instanceof net.cloudml.core.InternalComponent) {
                return matchIC((net.cloudml.core.InternalComponent) kc, cic);
            }
        }
        return false;
    }

    public boolean matchRequiredExecutionPlatformInstance(net.cloudml.core.RequiredExecutionPlatformInstance krepi, org.cloudml.core.RequiredExecutionPlatformInstance repi){
        if(krepi != null && repi != null){
            return krepi.getName().equals(repi.getName()) &&
                    matchRequiredExecutionPlatform((RequiredExecutionPlatform) krepi.getType(), repi.getType());
        }
        return false;
    }

    public boolean matchRequiredExecutionPlatform(net.cloudml.core.RequiredExecutionPlatform krep, org.cloudml.core.RequiredExecutionPlatform rep){
        if(krep != null && rep != null){
            return krep.getName().equals(rep.getName());
        }
        return false;
    }

    public boolean matchProvidedExecutionPlatform(net.cloudml.core.ProvidedExecutionPlatform kpep, org.cloudml.core.ProvidedExecutionPlatform pep){
        if(kpep != null && pep != null){
            return kpep.getName().equals(pep.getName());
        }
        return false;
    }

    public boolean matchProvidedExecutionPlatformInstances(List<net.cloudml.core.ProvidedExecutionPlatformInstance> kpepis, List<org.cloudml.core.ProvidedExecutionPlatformInstance> pepis){
        if(kpepis != null && pepis != null){
            boolean matched=true;
            for(net.cloudml.core.ProvidedExecutionPlatformInstance kpepi: kpepis){
                for(org.cloudml.core.ProvidedExecutionPlatformInstance pepi: pepis){
                    if(kpepi.getName().equals(pepi.getName()) &&
                        matchProvidedExecutionPlatform((net.cloudml.core.ProvidedExecutionPlatform)kpepi.getType(), pepi.getType())){
                        matched=true;
                        break;
                    }
                }
                if(!matched)
                    return false;
            }
            if(!matched)
                return false;
        }
        return true;
    }

    public boolean matchICI(net.cloudml.core.InternalComponentInstance kici, org.cloudml.core.InternalComponentInstance cici) {
        if(kici != null && cici != null){
            return cici.getName().equals(kici.getName())
                    && matchIC(kici.getType(), cici.getType())
                    && matchRequiredExecutionPlatformInstance(kici.getRequiredExecutionPlatformInstance(), cici.getRequiredExecutionPlatformInstance())
                    && matchProvidedExecutionPlatformInstances(kici.getProvidedExecutionPlatformInstances(), cici.getProvidedExecutionPlatformInstances());
        }
        return false;
    }

    public boolean matchExecuteInstance(net.cloudml.core.ExecuteInstance kei, org.cloudml.core.ExecuteInstance ei) {
        if(kei != null && ei != null){
            List<net.cloudml.core.ProvidedExecutionPlatformInstance> kpepis = new ArrayList<net.cloudml.core.ProvidedExecutionPlatformInstance>();
            List<org.cloudml.core.ProvidedExecutionPlatformInstance> pepis=new ArrayList<org.cloudml.core.ProvidedExecutionPlatformInstance>();
            kpepis.add(kei.getProvidedExecutionPlatformInstance());
            pepis.add(ei.getProvidedExecutionPlatformInstance());
            return kei.getName().equals(ei.getName())
                    && matchProvidedExecutionPlatformInstances(kpepis,pepis)
                    && matchRequiredExecutionPlatformInstance(kei.getRequiredExecutionPlatformInstance(), ei.getRequiredExecutionPlatformInstance());
        }
        return false;
    }
}
