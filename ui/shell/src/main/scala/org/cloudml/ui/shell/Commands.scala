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

import org.cloudml.facade.Facade
import org.cloudml.codecs.JsonCodec
import org.cloudml.codecs.XmiCodec
import org.cloudml.facade.commands.CloudMlCommand
import org.cloudml.facade.commands.CloudMlCommand
import org.cloudml.facade.commands.CloudMlCommand

/**
 * The required behaviour of object able to handle/execute command sent by the
 * shell.
 *
 * @author Franck Chauvel - SINTEF ICT
 * @since 1.0
 */
trait ShellCommandHandler {

  /**
   * Terminate the current CloudML Shell session
   */
  def exit(): Unit

  /**
   *  Display the help message, including the list of existing commands
   */
  def help(command: Option[String]): Unit

  /**
   * Display the current version of the cloudML shell
   */
  def version(): Unit

  /**
   * Show the list of the last n commands received by the Shell
   *
   * @param depth the number of commands which shall be retrieved, by default
   * the complete history is retrieved
   */
  def history(depth: Option[Int]): Unit

  /**
   * Store the last n command in the given file
   *
   * @param depth the number of the commands which shall be stored, by default
   * the complete history will be stored
   *
   * @param location the location where the commands shall be written
   */
  def dump(depth: Option[Int], location: String): Unit

  /**
   * Replay the CloudML shell commands stored in the given text file
   *
   * @param location the place where the script to replay is located
   */
  def replay(location: String): Unit

  /**
   * Show the the last n events that were received by the Shell
   *
   * @param depth the number of events to display, by default all events will
   * be displayed
   */
  def journal(depth: Option[Int]): Unit

  /**
   * Forward a CloudML command to the actual facade object
   * 
   * @param the command to foward to the facade
   * 
   * @param inBackground true if the shell must wait for this command to complete
   */
  def forward(command: CloudMlCommand, inBackground: Boolean): Unit

}

/**
 * Interface of Command object which are built from the user inputs
 *
 * @author Franck Chauvel - SINTEF ICT
 * @since 0.0.1
 *
 */
trait ShellCommand {

  /**
   * By default, commands must be stored on a dump
   */
  val skipOnDump = false

  /**
   * Trigger this command on a given cloudML engine
   * @param receiver the cloudML engine which has to execute this command
   */
  def execute(receiver: ShellCommandHandler): Unit = {}

}

/**
 * The Facade commands are the counterparts of the ShellCommands,
 */
case class FacadeCommand(val command: CloudMlCommand, val runInBackground: Boolean) extends ShellCommand {

  override def execute(receiver: ShellCommandHandler): Unit =
    receiver.forward(command, runInBackground)

}

/**
 * Optional trait which make the command to be ignored when the history is
 * dumped on a file
 *
 * @author Franck Chauvel - SINTEF ICT
 * @since 0.0.1
 */
trait SkipOnDump extends ShellCommand {

  override val skipOnDump = true;

}

/**
 * The exit command: Terminate the CloudML Shell session
 */
object Exit extends ShellCommand with SkipOnDump {

  override def execute(receiver: ShellCommandHandler): Unit =
    receiver.exit

}

/**
 * The 'help' command: show the list of available commands
 */
case class Help(val command: Option[String]) extends ShellCommand with SkipOnDump {

  override def execute(receiver: ShellCommandHandler): Unit =
    receiver.help(command)

}

/**
 * The 'version' command: show the version of the software currently used
 */
object Version extends ShellCommand with SkipOnDump {

  override def execute(receiver: ShellCommandHandler): Unit =
    receiver.version

}

/**
 * The 'history' command: show the last commands that were typed by the user
 */
case class ShowHistory(val depth: Option[Int]) extends ShellCommand with SkipOnDump {

  override def execute(receiver: ShellCommandHandler): Unit =
    receiver.history(depth)

}

/**
 * The 'dump' command: dump some of the previous commands into a text file
 *
 * @param depth the number of commands which must be read from the history
 *
 * @param location the path to the file where the selected commands must be stored
 */
case class DumpSession(val depth: Option[Int], location: String) extends ShellCommand with SkipOnDump {

  override def execute(receiver: ShellCommandHandler): Unit =
    receiver.dump(depth, location)

}

/**
 * The 'replay' command: replay all the commands stored in a given file
 *
 * @param file the location of the file containing the command to replay
 */
case class Replay(val file: String) extends ShellCommand {

  override def execute(receiver: ShellCommandHandler): Unit =
    receiver.replay(file);

}

/**
 * The 'show journal 4' command which show the last messages received by the
 * CloudML shell
 *
 *  @param depth the number of event to show, by default all events will be 
 *  displayed
 */
case class Journal(val depth: Option[Int]) extends ShellCommand with SkipOnDump {

  override def execute(receiver: ShellCommandHandler): Unit =
    receiver.journal(depth)

}

