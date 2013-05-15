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

import org.cloudml.facade.events._

object EventPrinter extends AbstractEventHandler with (Event => String) {

  private[this] var result = ""
  
  def apply(e: Event): String = {
    result = ""
    e.accept(this)
    result
  }
  
  override def handle(event: Event): Unit = {
    result = "Unknown event"
  }
 
  override def handle(message: Message): Unit = {
    result = message.getCategory().getLabel() + ": " + message.getBody()
  }
  
  override def handle(artefacts: ArtefactTypeList): Unit = {
    artefacts.getArtefactTypes().map{ a => a.getName() }.mkString(" - ", "", "\n")
  }
  
  override def handle(artefacts: ArtefactInstanceList): Unit = {
    artefacts.getArtefactInstances().map{ a => a.getName() }.mkString(" - ", "", "\n")
  }
  
  override def handle(data: ArtefactInstanceData): Unit = {
    "Artefact instance (ID: " + data.getArtefactInstance().getName() + ")"
  }
  
  override def handle(data: ArtefactTypeData): Unit = {
    "Artefact type (ID: " + data.getArtefactType().getName() + ")"
  }
  
}


object InlineEventPrinter extends AbstractEventHandler with (Event => String) {

  private[this] var result = ""
  
  def apply(e: Event): String = {
    result = ""
    e.accept(this)
    result
  }
  
  override def handle(event: Event): Unit = {
    result = "Unknown event"
  }
 
  override def handle(message: Message): Unit = {
    result = message.getCategory().getLabel() + ": " + message.getBody()
  }
  
  override def handle(artefacts: ArtefactTypeList): Unit = {
    "List of artefact types"
  }
  
  override def handle(artefacts: ArtefactInstanceList): Unit = {
    "List of artefact instances"
  }
  
  override def handle(data: ArtefactInstanceData): Unit = {
    "Artefact instance (ID: " + data.getArtefactInstance().getName() + ")"
  }
  
  override def handle(data: ArtefactTypeData): Unit = {
    "Artefact type (ID: " + data.getArtefactType().getName() + ")"
  }
  
}
