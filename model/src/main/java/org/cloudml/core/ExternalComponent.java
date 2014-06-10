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
package org.cloudml.core;

import org.cloudml.core.visitors.Visitor;

public class ExternalComponent extends Component {

    private String login;
    private String passwd;
    private String endPoint;
    private String credentials;
    protected Provider provider;
    private String location;
    private String serviceType;

    public ExternalComponent(String name) {
        super(name);
    }

    public ExternalComponent(String name, Provider provider) {
        super(name);
        rejectInvalidProvider(provider);
        this.provider = provider;
    }
    
    private void rejectInvalidProvider(Provider provider) {
        if (provider == null) {
            final String error = String.format("'null' is not a valid provider! (ext. component '%s')", getQualifiedName());
            throw new IllegalArgumentException(error);
        }
    }   
  
    
    @Override
    public final boolean isInternal() {
        return false;
    }
    
    public boolean isVM() {
        return false; 
    }
    
    public VM asVM() {
        if (!isVM()) {
            throw new IllegalStateException("Unable to cast a regular external component type into a VM type");
        }
        return (VM) this;
    }

    public String getLogin() {
        return this.login;
    }

    public String getPasswd() {
        return this.passwd;
    }

    public String getServiceType() {
        return this.serviceType;
    }

    public boolean isProvidedBy(Provider provider) {
        return this.provider.equals(provider);
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        rejectInvalidProvider(provider);
        this.provider = provider;
    }

    public String getEndPoint() {
        return this.endPoint;
    }

    public String getLocation(){
        return this.location;
    }

  
    public void setLogin(String login) {
        this.login = login;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    public void setLocation(String location){
        this.location=location;
    }

    public void setServiceType(String type){
        this.serviceType=type;
    }

    @Override
    public String toString() {
        return "Type " + getName();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ExternalComponent) {
            ExternalComponent otherComp = (ExternalComponent) other;
            return isNamed(otherComp.getName());
        }
        else {
            return false;
        }
    }

    public ExternalComponentInstance<? extends ExternalComponent> instantiates(String name) {
        return new ExternalComponentInstance<ExternalComponent>(name, this);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitExternalComponent(this);
    }

}
