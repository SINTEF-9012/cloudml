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
 */
object CommandParser extends RegexParsers {

  var factory = null.asInstanceOf[CommandFactory]
  
  // TIPS: comments ('#') are trimmed as white spaces
  protected override val whiteSpace = """(\s|#.*)+""".r

  /**
   * Rule to capture integer
   */
  def integer: Parser[Int] =
    """\d+""".r ^^ { case text => Integer.parseInt(text) }

  def identifier: Parser[String] =
    """[a-zA-Z0-9_$\.\-:]+""".r ^^ { case text => text }

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
    "start" ~> identifier ^^ { case i => factory.startComponent(i) } |
  "stop" ~> identifier ^^ { case i => factory.stopComponent(i) } |
  "attach" ~> identifier ~ ("to" ~> identifier) ^^ { case p ~ r => factory.attach(p, r) } |
  "detach" ~> identifier ~ ("from" ~> identifier) ^^ { case p ~ r => factory.detach(p, r) } |
  "install" ~> identifier ~ ("on" ~> identifier) ^^ { case s ~ p => factory.install(p, s) } |
  "uninstall" ~> identifier ~ ("from" ~> identifier) ^^ { case s ~ p => factory.uninstall(p, s) } |
  "instantiate" ~> identifier ~ ("as" ~> identifier) ^^ { case tn ~ an => factory.instantiate(tn, an) } |
  "destroy" ~> identifier  ^^ { case an => factory.destroy(an) } |
  "snapshot" ~> file ^^ {case f => factory.snapshot(f)} |
  "load" ~> modelType ~ ("from" ~> file) ^^ { 
    case Deployment ~ p => factory.loadDeployment(p)
    case Credentials ~ p => factory.loadCredentials(p)
  } |
  "store" ~> modelType ~ ("to" ~> file) ^^ { 
    case Deployment ~ path => factory.storeDeployment(path) 
    case Credentials ~ path => factory.storeCredentials(path)
  } |
  "upload" ~> file ~ ("on" ~> identifier) ~ ("at" ~> file) ^^ {
    case l ~ a ~ r => factory.upload(a, l, r)
  } |
  "list" ~> dataType ^^ { 
    case ArtefactType =>  factory.listTypes() 
    case ArtefactInstance => factory.listInstances()
  } |
  "view" ~> dataType ~ identifier ^^ { 
    case ArtefactType ~ id => factory.viewType(id)
    case ArtefactInstance ~ id => factory.viewInstance(id)
  } |
  "deploy"  ^^^ { factory.deploy() }

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
