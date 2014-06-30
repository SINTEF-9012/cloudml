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
package org.cloudml.ui.shell;

import org.cloudml.ui.shell.terminal.Terminal;
import org.cloudml.ui.shell.terminal.Message;
import java.util.ArrayList;
import java.util.List;
import org.cloudml.facade.commands.CloudMlCommand;
import org.cloudml.facade.events.AbstractEventHandler;
import org.cloudml.facade.events.Event;
import org.cloudml.ui.shell.terminal.OutputDevice;

import static org.cloudml.ui.shell.terminal.Message.format;

/**
 * Contains all the message received by the shell
 */
public class Mailbox {

    private final OutputDevice terminal;
    private final List<Event> content;
    private final List<CloudMlCommand> followed;

    Mailbox(OutputDevice terminal) {
        this.terminal = terminal;
        this.content = new ArrayList<Event>();
        this.followed = new ArrayList<CloudMlCommand>();
    }
    
    void showMessages(int depth) {
         if (hasNewMessages()) {
            final int max = (depth == -1) ? size() : depth;
            terminal.print(format("Last messages:").eol());
            for (int index = 1; index <= max; index++) {
                final int reversedIndex = size() - index;
                Event event = contents().get(reversedIndex);
                terminal.print(format("  %03d: %s", index, event));
            }
        
        } else {
            terminal.print(format("No new message").eol());
        
        }
    }

    void followUp(CloudMlCommand command) {
        followed.add(command);
    }

    void discard(CloudMlCommand command) {
        followed.remove(command);
    }

    List<Event> contents() {
        List<Event> result = new ArrayList<Event>();
        synchronized (content) {
            result.addAll(content);
            content.clear();
        }
        return result;
    }

    int size() {
        int result = 0;
        synchronized (content) {
            result = content.size();
        }
        return result;
    }

    boolean hasNewMessages() {
        return !content.isEmpty();
    }

    class EventHandler extends AbstractEventHandler {

        public void handle(Event event) {
            if (isRelevant(event)) {
                terminal.print(Message.format(event.toString()).eol());

            } else {
                synchronized (content) {
                    content.add(event);
                }
            }
        }

        private boolean isRelevant(Event event) {
            for (CloudMlCommand any: followed) {
                if (event.wasTriggeredBy(any)) {
                    return true;
                }
            }
            return false;
        }
    }

}
