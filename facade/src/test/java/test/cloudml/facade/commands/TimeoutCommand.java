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
package test.cloudml.facade.commands;

import java.util.logging.Level;
import java.util.logging.Logger;
//import org.cloudml.facade.commands.ManageableCommand;
import org.cloudml.facade.commands.CommandHandler;
/**
 * Request the loading of a given deployment model. The format will be infered
 * from the extension of the given file.
 *
 * @author Brice Morin
 * @since 1.0
 */
public class TimeoutCommand /*extends ManageableCommand*/ {

    static final long sleep = 2000;

  /*  public TimeoutCommand(CommandHandler handler) {
        super(handler);
    }
    
    public void execute(CommandHandler target) {
        try {
            Thread.currentThread().sleep(sleep);
        } catch (InterruptedException ex) {
            Logger.getLogger(TimeoutCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  */
}
