/**
 * This file is part of CloudML [ http://cloudml.org ]
 *
 * Copyright (C) 2012 - SINTEF ICT Contact: Franck Chauvel
 * <franck.chauvel@sintef.no>
 *
 * Module: root
 *
 * CloudML is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * CloudML is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CloudML. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package test.cloudml.codecs.kmf;

/**
 * Match CloudML objects against KMY POJO Object
 */
public class Matcher {

    public boolean match(net.cloudml.core.Provider kprovider, org.cloudml.core.Provider cprovider) {
        boolean result = true;
        result &= cprovider.getName().equals(kprovider.getName());
        return result;
    }

    public boolean match(net.cloudml.core.Component kc, org.cloudml.core.VM cvm) {
        boolean result = true;
        if (kc instanceof net.cloudml.core.Component) {
            result = match((net.cloudml.core.VM) kc, cvm);
        }
        return result;
    }

    public boolean match(net.cloudml.core.VM kvm, org.cloudml.core.VM cvm) {
        boolean result = true;
        result &= cvm.getName().equals(kvm.getName())
                && match(kvm.getProvider(), cvm.getProvider());
        return result;
    }

    public boolean match(net.cloudml.core.VMInstance kvi, org.cloudml.core.VMInstance cvi) {
        boolean result = true;
        result &= cvi.getName().equals(kvi.getName())
                && match(kvi.getType(), cvi.getType());
        return result;
    }

    public boolean match(net.cloudml.core.Relationship kr, org.cloudml.core.Relationship cr) {
        boolean result = true;
        result &= cr.getRequiredPort().getName().equals(kr.getRequiredPort().getName())
                && cr.getProvidedPort().getName().equals(kr.getProvidedPort().getName());
        return result;
    }

    public boolean match(net.cloudml.core.RelationshipInstance kri, org.cloudml.core.RelationshipInstance cri) {
        boolean result = true;
        result &= cri.getRequiredPortInstance().getName().equals(kri.getRequiredPortInstance().getName())
                && cri.getProvidedPortInstance().getName().equals(kri.getProvidedPortInstance().getName());
        return result;
    }

    public boolean match(net.cloudml.core.InternalComponent kic, org.cloudml.core.InternalComponent cic) {
        boolean result = cic.getName().equals(kic.getName());
        return result;
    }

    public boolean match(net.cloudml.core.Component kc, org.cloudml.core.InternalComponent cic) {
        boolean result = true;
        if (kc instanceof net.cloudml.core.InternalComponent) {
            result = match((net.cloudml.core.InternalComponent) kc, cic);
        }
        return result;
    }

    public boolean match(net.cloudml.core.InternalComponentInstance kici, org.cloudml.core.InternalComponentInstance cici) {
        boolean result = cici.getName().equals(kici.getName())
                && match(kici.getType(), cici.getType())
                && match(kici.getDestination(), cici.getDestination());
        return result;
    }
}
