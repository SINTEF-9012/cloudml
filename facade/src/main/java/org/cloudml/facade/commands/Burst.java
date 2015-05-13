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
package org.cloudml.facade.commands;

/**
 * Created by nicolasf on 21.01.15.
 */
public class Burst extends CloudMlCommand {

    private final String ecId;

    private final String providerID;

    public Burst(String vmId, String providerID) {
        this.ecId = vmId;
        this.providerID = providerID;
    }

    public String getEcId() {
        return ecId;
    }

    public String getProviderID() {
        return providerID;
    }

    @Override
    public void execute(CommandHandler handler) {
        handler.handle(this);
    }

    @Override
    public String toString() {
        return String.format("Burst %s to %s", ecId, providerID);
    }
}
