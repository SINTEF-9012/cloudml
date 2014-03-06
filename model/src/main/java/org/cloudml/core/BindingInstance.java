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

import org.cloudml.core.validation.CanBeValidated;
import org.cloudml.core.validation.Report;
import org.cloudml.core.visitors.Visitable;
import org.cloudml.core.visitors.Visitor;

/**
 * Bindings between ArtefactPortInstances
 *
 * @author Nicolas Ferry
 *
 */
public class BindingInstance extends WithProperties implements Visitable, CanBeValidated {

    private ClientPortInstance client;
    private ServerPortInstance server;
    private Binding type;

    public BindingInstance() {
    }

    public BindingInstance(String name, Binding type) {
        this.name = name;
        this.type = type;
    }

    public BindingInstance(ClientPortInstance client, ServerPortInstance server, Binding type) {
        this.client = client;
        this.server = server;
        this.type = type;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitBindingInstance(this);
    }

    @Override
    public Report validate() {
        final Report report = new Report();
        validateName(report);
        validateType(report);
        validateClientEnd(report);
        validateServerEnd(report);
        return report;
    }
    
    private void validateName(Report report) {
        if (name == null) {
            report.addError("Binding instance has no name ('null' found)");
        } else if (name.isEmpty()) {
            report.addError("Binding instance has no name (empty string found)");
        }
    }
    
    private void validateType(Report report) {
        if (type == null) {
            report.addError("Binding instance without type ('null' found)");
        }
    }
    
    private void validateClientEnd(Report report) {
        if (client == null) {
            report.addError("Binding instance has a pending client end ('null' found)");
        }
        else if (type != null) {
            if (!client.getType().equals(type.getClient())) {
                final String message = String.format(
                        "illegal binding instance that does not matches its type (client found '%s' but expected '%s')", 
                        client.getType().getNameOrDefaultIfNull(), 
                        type.getClient().getNameOrDefaultIfNull());
                report.addError(message);
            }
        }
    }
    
     private void validateServerEnd(Report report) {
        if (server == null) {
            report.addError("Binding instance has a pending server end ('null' found)");
        } 
        else if (type != null) {
            if (!server.getType().equals(type.getServer())) {
                final String message = String.format("illegal binding instance that does not matches its type (server found '%s' but expected '%s')", server.getType().getNameOrDefaultIfNull(), type.getServer().getNameOrDefaultIfNull());
                report.addError(message);
            }
        }
    }

    public void setClient(ClientPortInstance p) {
        this.client = p;
    }

    public void setServer(ServerPortInstance p) {
        this.server = p;
    }

    public ClientPortInstance getClient() {
        return this.client;
    }

    public ServerPortInstance getServer() {
        return this.server;
    }

    public Binding getType() {
        return type;
    }
    
    public void setType(Binding type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return client + "->" + server;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof BindingInstance) {
            BindingInstance otherBinding = (BindingInstance) other;
            return (client.equals(otherBinding.getClient()) && server.equals(otherBinding.getServer()));
        }
        else {
            return false;
        }
    }
}
