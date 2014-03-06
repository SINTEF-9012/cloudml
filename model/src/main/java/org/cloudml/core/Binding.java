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
 * 
 */

package org.cloudml.core;

import org.cloudml.core.validation.CanBeValidated;
import org.cloudml.core.validation.Report;
import org.cloudml.core.visitors.Visitable;
import org.cloudml.core.visitors.Visitor;

/**
 * Binding between to Artefact
 *
 * @author Nicolas Ferry
 *
 */
public class Binding extends WithProperties implements Visitable, CanBeValidated {

    private ClientPort client;
    private ServerPort server;
    private Resource clientResource;
    private Resource serverResource;

    public Binding() {
    }

    public Binding(ClientPort client, ServerPort server) {
        this.client = client;
        this.server = server;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitBinding(this);
    }

    @Override
    public Report validate() {
        final Report report = new Report();
        if (client == null) {
            report.addError("Missing client end ('null' found)");
        }
        if (server == null) {
            report.addError("Missing server end ('null' found)");
        }
        if (client != null && server != null) {
            if (client.isLocal() && server.isRemote()) {
                report.addError("Illegal binding between a local client and a remote server");
            }
            if (client.isRemote() && server.isLocal()) {
                report.addError("Illegal binding between a remote client and a local server");                
            }
        }
        return report;
    }

    public BindingInstance instanciates(String name) {
        return new BindingInstance(name, this);
    }

    public BindingInstance instanciates(ClientPortInstance client, ServerPortInstance server) {
        return new BindingInstance(client, server, this);
    }

    public void setClient(ClientPort p) {
        this.client = p;
    }

    public void setServer(ServerPort p) {
        this.server = p;
    }

    public ClientPort getClient() {
        return this.client;
    }

    public ServerPort getServer() {
        return this.server;
    }

    public void setClientResource(Resource clientResource) {
        this.clientResource = clientResource;
    }

    public Resource getClientResource() {
        return clientResource;
    }

    public void setServerResource(Resource serverResource) {
        this.serverResource = serverResource;
    }

    public Resource getServerResource() {
        return serverResource;
    }

    @Override
    public String toString() {
        return client + "->" + server;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Binding) {
            Binding otherBinding = (Binding) other;
            return (client.getName().equals(otherBinding.getClient().getName()) && server.getName().equals(otherBinding.getServer().getName()));
        }
        else {
            return false;
        }
    }
}
