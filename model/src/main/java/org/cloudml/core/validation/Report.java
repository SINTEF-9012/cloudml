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
/*
 */
package org.cloudml.core.validation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class Report {

    private final ArrayList<Message> messages;

    public Report() {
        this.messages = new ArrayList<Message>();
    }

    public boolean hasError() {
        return !getErrors().isEmpty();
    }

    public void addError(String errorDescription) {
        this.messages.add(new Message(Level.ERROR, errorDescription));
    }

    public List<Message> getErrors() {
        return selectMessagesWithLevel(Level.ERROR);
    }

    public boolean hasErrorAbout(String keyword) {
        return hasMessageAbout(Level.ERROR, keyword);
    }

    public boolean hasWarningAbout(String keyWord) {
        return hasMessageAbout(Level.WARNING, keyWord);
    }

    public boolean hasMessageAbout(Level level, String keyWord) {
        boolean found = false;
        Iterator<Message> iterator = this.messages.iterator();
        while (iterator.hasNext() && !found) {
            final Message message = iterator.next();
            if (level == message.getLevel() && message.getMessage().contains(keyWord)) {
                found = true;
            }
        }
        return found;
    }

    public boolean hasWarning() {
        return !this.getWarnings().isEmpty();
    }

    public List<Message> getWarnings() {
        return selectMessagesWithLevel(Level.WARNING);
    }

    public List<Message> selectMessagesWithLevel(Level level) {
        ArrayList<Message> errors = new ArrayList<Message>();
        for (Message message : this.messages) {
            if (message.getLevel().equals(level)) {
                errors.add(message);
            }
        }
        return errors;
    }

    public boolean pass(boolean warningAllowed) {
        return !hasError() && (!warningAllowed || hasWarning());
    }

    public void addWarning(String message) {
        this.messages.add(new Message(Level.WARNING, message));
    }
}
