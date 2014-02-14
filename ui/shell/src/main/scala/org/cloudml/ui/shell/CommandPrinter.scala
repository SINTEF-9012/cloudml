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


import org.cloudml.facade.commands._

/**
 * Pretty print the command specific to the CloudML Shell
 * 
 * @author Franck Chauvel 
 * @since 1.0
 */
object ShellCommandPrinter extends (ShellCommand => String) {

  def apply(sc: ShellCommand): String =
    sc match {
      case Help(None) => "help"
      case Help(Some(t)) => "help \"%s\"".format(t)
      case Exit => "exit"
      case ShowHistory(None) => "history"
      case ShowHistory(Some(d)) => "history %d".format(d)
      case Version => "version"
      case DumpSession(None, p) => "dump session %s".format(p)
      case DumpSession(Some(d), p) => "dump from %d session %s".format(d, p)
      case Replay(p) => "replay from %s".format(p)
      case Journal(Some(d)) => "messages " + d
      case Journal(None) => "messages"
      case FacadeCommand(fc, true) => CommandPrinter(fc) + " &"
      case FacadeCommand(fc, false) => CommandPrinter(fc)
      case _ => "Unknown command"
    }

}


/**
 * Pretty printer for the CloudML command (i.e., the command accepted by the
 * facade).
 * 
 * @author Franck Chauvel
 * @since 1.0
 */
object CommandPrinter extends (CloudMlCommand => String) {
    
  def apply(cc: CloudMlCommand): String = {
    cc match {
      case StartArtifact(id) => "start " + id 
      case StopArtifact(id) => "stop " + id
      case Install(s, e) => "install " + s + " on " + e
      case Uninstall(s, e) => "uninstall " + s + " from " + e
      case Attach(c, p) => "attach " + c + " to " + p
      case Detach(c, p) => "detach " + c + " from " + p
      case Destroy(i) => "destroy " + i
      case Instantiate(t, i) => "instantiate " + t + " as " + i
      case Upload(l, a, r) => "upload " + l + " on " + a + " at " + r
      case LoadDeployment(p) => "load deployment " + p
      case StoreDeployment(p) => "store deployment in " + p
      case l: ListComponents => "list types"
      case l: ListComponentInstances => "list instances"
      case ViewArtefactType(id) => "view instance " + id
      case ViewArtefactInstance(id) => "view type " + id
      case c: Deploy => "deploy"
      case LoadCredentials(p) => "load credentials " + p
      case StoreCredentials(p) => "store credentials " + p
      case _ => "Unknown facade command"
    }
  }
  
}


// Extractors to be able to pattern match the CloudMlCommands

object StartArtifact {
  
  def unapply(sa: StartComponent): Option[String] =
    Some(sa.getComponentId())
    
}

object StopArtifact {
  
  def unapply(sa: StopComponent): Option[String] =
    Some(sa.getComponentId())
    
}

object Install {
  
  def unapply(ic: Install): Option[(String, String)] =
    Some(ic.getSoftware(), ic.getEnvironment())
}

object Uninstall {
  
  def unapply(uc: Uninstall): Option[(String, String)] =
    Some(uc.getSoftware(), uc.getEnvironment())
  
}

object Attach {
  
  def unapply(ac: Attach): Option[(String, String)] =
    Some(ac.getConsumerId(), ac.getProviderId())
}

object Detach {
  
  def unapply(dc: Detach): Option[(String, String)] =
    Some(dc.getConsumerId(), dc.getProviderId())
}

object Instantiate {
  
  def unapply(ic: Instantiate): Option[(String, String)] =
    Some(ic.getTypeId(), ic.getInstanceId())
}


object Destroy {
  
  def unapply(dc: Destroy): Option[String] =
    Some(dc.getInstanceId())
  
}

object Upload {
  
  def unapply(uc: Upload): Option[(String, String, String)] =
    Some(uc.getLocalPath(), uc.getArtifactId(), uc.getRemotePath())
}

object LoadDeployment {
  
  def unapply(ldc: LoadDeployment): Option[String] =
    Some(ldc.getPathToModel())
}

object StoreDeployment {
  
  def unapply(sdc: StoreDeployment): Option[String] =
    Some(sdc.getDestination())
}


object ViewArtefactInstance {
  
  def unapply(vai: ViewComponentInstance): Option[String] =
    Some(vai.getComponentId())

}


object ViewArtefactType {
  
  def unapply(vat: ViewComponent): Option[String] =
    Some(vat.getComponentId())
  
}


object LoadCredentials {
  
  
  def unapply(sc: LoadCredentials): Option[String] =
    Some(sc.getPathToCredentials())
  
}


object StoreCredentials {
  
  def unapply(sc: StoreCredentials): Option[String] =
    Some(sc.getDestination())
  
}

