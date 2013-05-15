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

import scala.util.matching.Regex
import scala.util.parsing.combinator._

import org.cloudml.facade.commands.CloudMlCommand
import org.cloudml.facade.commands.CommandFactory

/**
 * Parse the command and script files containing commands.
 *
 * @author Franck Chauvel - SINTEF ICT
 * @since 0.0.1
 */
object CommandParser extends RegexParsers {

  var factory = null.asInstanceOf[CommandFactory]// = new CommandFactory()
  
  // TIPS: comments ('#') are trimmed as white spaces
  protected override val whiteSpace = """(\s|#.*)+""".r

  /**
   * Rule to capture integer
   */
  def integer: Parser[Int] =
    """\d+""".r ^^ { case text => Integer.parseInt(text) }

  def identifier: Parser[String] =
    """[a-zA-Z0-9_$\.:]+""".r ^^ { case text => text }

  // Does not permit white spaces in path 
  def file: Parser[String] =
    """(([a-zA-Z]:\\)|/)?[a-zA-Z0-9\-$_\.]+([\\/][a-zA-Z0-9\-$_\.]+)*""".r ^^ { case text => text }
      
  def string: Parser[String] =
    """\"[^\"]+""".r ^^ { case s => s.drop(1) }

  /**
   * Rule to parse a sequence of commands (used to parse scripts)
   */
  def script: Parser[List[ShellCommand]] =
    rep(command) ^^ { case cs => cs }

  /**
   * Rule to parse the command
   */
  def command: Parser[ShellCommand] =
    "exit" ^^^ { Exit } |
  "quit" ^^^ { Exit } |
  "version" ^^^ { Version } |
  "help" ~> opt(string) ^^ { case cmd => Help(cmd) } |
  "history" ~> opt(integer) ^^ {
    case o => ShowHistory(o)
  } |
  "messages" ~> opt(integer) ^^ {
    case oi => Journal(oi)
  } |
  "dump" ~> opt("from" ~> integer) ~ ("to" ~> file) ^^ {
    case o ~ file => DumpSession(o, file)
  } |
  "replay" ~> file ^^ {
    case file => Replay(file)
  } |
  cloudMlCommand ~ opt("&") ^^ { 
    case cmd ~ None => FacadeCommand(cmd, false) 
    case cmd ~ Some(_) => FacadeCommand(cmd, true)
  }

  /**
   * The syntax of the commands, which are forwarded to the CloudML facade
   */
  def cloudMlCommand: Parser[CloudMlCommand] =
    "start" ~> identifier ^^ { case i => factory.createStartArtifact(i) } |
  "stop" ~> identifier ^^ { case i => factory.createStopArtifact(i) } |
  "attach" ~> identifier ~ ("to" ~> identifier) ^^ { case p ~ r => factory.createAttach(p, r) } |
  "detach" ~> identifier ~ ("from" ~> identifier) ^^ { case p ~ r => factory.createDetach(p, r) } |
  "install" ~> identifier ~ ("on" ~> identifier) ^^ { case s ~ p => factory.createInstall(p, s) } |
  "uninstall" ~> identifier ~ ("from" ~> identifier) ^^ { case s ~ p => factory.createUninstall(p, s) } |
  "instantiate" ~> identifier ~ ("as" ~> identifier) ^^ { case tn ~ an => factory.createInstantiate(tn, an) } |
  "destroy" ~> identifier  ^^ { case an => factory.createDestroy(an) } |
  "load" ~> modelType ~ ("from" ~> file) ^^ { 
    case Deployment ~ p => factory.createLoadDeployment(p)
    case Credentials ~ p => factory.createLoadCredentials(p)
  } |
  "store" ~> modelType ~ ("to" ~> file) ^^ { 
    case Deployment ~ path => factory.createStoreDeployment(path) 
    case Credentials ~ path => factory.createStoreCredentials(path)
  } |
  "upload" ~> file ~ ("on" ~> identifier) ~ ("at" ~> file) ^^ {
    case l ~ a ~ r => factory.createUpload(a, l, r)
  } |
  "list" ~> dataType ^^ { 
    case ArtefactType =>  factory.createListArtefactTypes() 
    case ArtefactInstance => factory.createListArtefactInstances()
  } |
  "view" ~> dataType ~ identifier ^^ { 
    case ArtefactType ~ id => factory.createViewArtefactType(id)
    case ArtefactInstance ~ id => factory.createViewArtefactInstance(id)
  } |
  "deploy"  ^^^ { factory.createDeploy() }

  /**
   * The various type of models which can be loaded/stored in different formats
   */
  def modelType: Parser[ModelType] =
    "deployment" ^^^ { Deployment } |
  "credentials" ^^^ { Credentials }
  

  
  def dataType: Parser[DataType] =
    "types" ^^^ { ArtefactType } |
  "type" ^^^ { ArtefactType } |
  "instances" ^^^ { ArtefactInstance } |
  "instance" ^^^ { ArtefactInstance }
}

trait ModelType
object Deployment extends ModelType
object Credentials extends ModelType

trait DataType
object ArtefactType extends DataType
object ArtefactInstance extends DataType
