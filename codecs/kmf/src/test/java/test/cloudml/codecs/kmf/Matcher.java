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

    public boolean matchICI(net.cloudml.core.InternalComponentInstance kici, org.cloudml.core.InternalComponentInstance cici) {
        if(kici != null && cici != null){
            return cici.getName().equals(kici.getName())
                    && matchIC(kici.getType(), cici.getType())
                    && matchVMInstance(kici.getDestination(), cici.getDestination());
        }
        return false;
    }
}
