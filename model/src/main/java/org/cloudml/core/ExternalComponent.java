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

import java.util.List;

/**
 * Created by Nicolas Ferry on 19.02.14.
 */

public class ExternalComponent extends Component{

    private String login;
    private String passwd;
    private String endPoint;
    private String credentials;

    protected Provider provider;

    public ExternalComponent(){}

    public ExternalComponent(String name){
        super(name);
    }

    public ExternalComponent(String name, Provider provider){
        super(name);
        this.provider=provider;
    }

    public ExternalComponent(String name, List<Property> properties) {
        super(name, properties);
    }

    public ExternalComponent(String name, List<Property> properties, Provider provider) {
        super(name, properties);
        this.provider=provider;
    }

    public String getLogin(){
        return this.login;
    }

    public String getPasswd(){
        return this.passwd;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider p) {
        provider = p;
    }


    public String getEndPoint(){
        return this.endPoint;
    }

    public String getCredentials(){
        return this.credentials;
    }

    public void setLogin(String login){
        this.login=login;
    }

    public void setPasswd(String passwd){
        this.passwd=passwd;
    }

    public void setEndPoint(String endPoint){
        this.endPoint=endPoint;
    }

    public void setCredentials(String credentials){
        this.credentials=credentials;
    }

    @Override
    public String toString() {
        return "Type " + name;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ExternalComponent) {
            ExternalComponent otherComp = (ExternalComponent) other;
            return name.equals(otherComp.getName());
        } else {
            return false;
        }
    }

    public ExternalComponentInstance instantiates(String name) {
        return new ExternalComponentInstance(name, this);
    }
}
