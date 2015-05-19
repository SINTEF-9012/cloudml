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
 * Created by nicolasf on 19.05.15.
 */
public class onlineMigration extends CloudMlCommand {
    public String getSource() {
        return source;
    }

    private final String source;

    public String getDestination() {
        return destination;
    }

    private final String destination;

    public int getNbThread() {
        return nbThread;
    }

    private final int nbThread;

    public int getVdpSize() {
        return vdpSize;
    }

    private final int vdpSize;

    public onlineMigration(final String source, final String destination, final int nbThread, final int vdpSize){
        this.source=source;
        this.destination=destination;
        this.nbThread=nbThread;
        this.vdpSize=vdpSize;
    }


    @Override
    public void execute(CommandHandler target) {
        target.handle(this);
    }

    @Override
    public String toString() {
        return "onlineMigration from"+source+" to "+destination;
    }
}
