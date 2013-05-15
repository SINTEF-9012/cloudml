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
package org.cloudml.ui.shell

import scala.collection.JavaConversions._
import scala.collection.mutable._
import org.cloudml.facade.events.EventHandler
import org.cloudml.facade.events.AbstractEventHandler
import org.cloudml.facade.events.Event
import org.cloudml.facade.commands.CloudMlCommand

/**
 * Centralise all the event handlers that the CloudML shell register on the
 * facade.
 *
 * @author Franck Chauvel - SINTEF ICT
 * @since 0.0.1
 *
 */
class EventManager(val term: Terminal) {

  private[this] val wanted = ListBuffer[CloudMlCommand]()
  
  private[this] val mailbox = ListBuffer[Event]()

 
  def countNewMessages: Int =
    mailbox.synchronized {
      mailbox.size
    }

  def messages: List[Event] =
    mailbox.synchronized {
      val result = mailbox.toList
      mailbox.clear
      result
    }
  
  
  def enableInterceptionFor(command: CloudMlCommand): Unit =
    wanted.add(command)
  
  
  def disableInterceptionFor(command: CloudMlCommand): Unit =
    wanted -= command

  /**
   * Event handler to manage failure events. It just print on the terminal the
   * associated message and advice.
   */
  object EventHandler extends AbstractEventHandler {

    override def handle(event: Event): Unit = {
      if (wanted.exists{ c => event.wasTriggeredBy(c)}) {
        term.println(EventPrinter(event))
      } else {
        mailbox.synchronized {
          mailbox += event
        }
      }
    }
    
  }
  
 

}