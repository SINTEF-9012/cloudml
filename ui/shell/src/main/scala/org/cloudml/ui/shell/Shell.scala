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
import scala.collection.mutable
import java.io.File
import java.io.FileReader
import java.io.PrintStream
import java.io.InputStream
import java.util.logging.LogManager
import org.cloudml.facade.Facade
import org.cloudml.facade.CloudML
import org.cloudml.facade.commands.CommandFactory
import org.cloudml.facade.events.Event
import org.cloudml.facade.commands.CloudMlCommand
import org.cloudml.facade.Factory
import org.cloudml.ui.shell.configuration.Loader

/**
 * The CloudML Shell
 *
 * It allows the user to interact with the models and, in turn, with the
 * application deployed in the Cloud. This shell is based on the well-known
 * "command pattern": the input of the user is first parsed to get a Command
 * object, which can be stored or execute, or forwarded to the cloudML facade.
 *
 * @author Franck Chauvel - SINTEF ICT
 * @since 0.0.1
 */
class Shell(val cloudML: CloudML) extends ShellCommandHandler {

  val configuration = Loader.getInstance().getConfiguration()
  
  val terminal = new Terminal()
  
  val manager = new EventManager(terminal)
  cloudML.register(manager.EventHandler)

  val history = mutable.ListBuffer[ShellCommand]()

  /**
   * Check whether the shell session is completed, i.e., whether the last command is
   * an exit command
   */
  def isDone =
    history.last == Exit

  /**
   * Entry point of the shell: starts prompting the user and trigger appropriate action
   * until the user enter the 'exit' command
   */
  def start: Unit = {
    terminal.println(configuration.getVersion())
    terminal.println(configuration.getCopyright())
    terminal.println(Console.CYAN, configuration.getLogo().stripMargin('|'))
    terminal.println(configuration.getDisclaimer())
    do {
      execute(prompt)
    } while (!isDone)
  }

  def prompt: String = {
    terminal.newLine
    val count = manager.countNewMessages
    if (count > 0) {
      terminal.println(count + " new message(s).", Console.GREEN)
    }
    terminal.prompt
  }

  /**
   * Execute a single command, represented as a text
   *
   * @param text the text of the command to execute
   */
  def execute(text: String) {
    val command = parse(text)
    history.append(command)
    command.execute(this)
  }

  /**
   * Prompt the user for a command, parse it and trigger the associated
   * behaviour using a command Pattern.
   *
   * @return the command that was entered by the user.
   */
  def parse(text: String): ShellCommand = {
    CommandParser.parse(CommandParser.command, text) match {
      case CommandParser.Success(cmd, _) => cmd
      case CommandParser.NoSuccess(message, _) => {
        terminal.println("Illegal commmand: " + message)
        parse(prompt)
      }
    }
  }

  /**
   * Pre-process the commands and intercepts the ones that are shell-dependent,
   * such as help, version, dump, replay, etc.
   *
   * @param the command to execute
   */
  def process(command: ShellCommand): Unit = {
    history += command
    command.execute(this)
  }

  /**
   * Display the list of available commands
   */
  def help(command: Option[String]): Unit = {
    val sep = System.getProperty("line.separator")
    command match {
      case None => {
        terminal.println("The available commands are:")
        configuration.getCommands().foreach { c =>
          terminal.println(" - " + c.getSyntax())
        }
        terminal.println("\nUse 'help \"<command>\"' to see detailed information about a given command.")
      }
      case Some(cmd) => {
        val selected = configuration.getCommands().filter{ c => c.getName().matches(cmd) }
        if (selected.isEmpty) {
          terminal.print("No command matching \"" + cmd + "\"");
        } else {
          selected.foreach { c =>
          	terminal.println(" - Name: " + c.getName())
          	terminal.println(" - Syntax: " + c.getSyntax())
          	terminal.println(" - Description:")
          	terminal.println(c.getDescription().lines.map{ l => "    " + l }.mkString(sep))
          	terminal.println(" - Examples:")
          	c.getExamples().foreach{ ex => 
          	  terminal.println("    - syntax: " + ex.getSyntax())
          	  terminal.println(ex.getDescription().lines.map{ l => "       " + l }.mkString(sep))	
          	}
          }
        }
      }
    }

  }

  /**
   * @see {@link org.cloudml.shell.ShellCommandHandler#exit}
   */
  def exit: Unit = {
    terminal.println(configuration.getClosingMessage())
  }

  /**
   * @see {@link org.cloudml.shell.ShellCommandHandler#version}
   */
  def version: Unit =
    terminal.println(configuration.getVersion())

  /**
   * @see {@link org.cloudml.shell.ShellCommandHandler#history}
   */
  def history(depth: Option[Int]) = {
    val cmds = history.take(depth.getOrElse(history.size))
    val numbered = (1 until cmds.size).zip(cmds.reverse)
    terminal.println("Previous commands:")
    numbered.foreach { nc =>
      terminal.println(" - %2d: %s".format(nc._1, ShellCommandPrinter(nc._2)))
    }
  }

  def journal(depth: Option[Int]) = {
    val a = manager.countNewMessages
    if (a > 0) {
    val n = math.min(a, depth.getOrElse(a))
    terminal.println("Last messages:")
    manager.messages.take(n).foreach { m =>
      terminal.println(" - " + InlineEventPrinter(m))
    }
    } else {
      terminal.println("No new message.")
    }
  }

  /**
   * @see {@link org.cloudml.shell.ShellCommandHandler#dump}
   */
  def dump(depth: Option[Int], file: String) = {
    val cmd = history.take(depth.getOrElse(history.size))
    val script = new PrintStream(new File(file))
    script.println("#")
    script.println("# Script generated by " + configuration.getVersion())
    script.println("# Please edit carefully")
    script.println("#")
    cmd.filter { c => !c.skipOnDump }.foreach { c => script.println(ShellCommandPrinter(c)) }
    script.close()
    terminal.println("%d command(s) stored in '%s' (%d skipped)".format(cmd.size, file, cmd.filter { c => c.skipOnDump }.size))
  }

  /**
   * @see {@link org.cloudml.shell.ShellCommandHandler#replay}
   */
  def replay(file: String) = {
    CommandParser.parse(CommandParser.script, new FileReader(file)) match {
      case CommandParser.Success(cs, _) =>
        cs.foreach { c => process(c) }
      case CommandParser.NoSuccess(message, _) =>
        terminal.println("Illegal script: " + message)
    }
  }

  /**
   * Forward the command to the cloudML facade and wait for success event to
   * arrive
   *
   * {@link org.cloudml.shell.ShellCommandHandler#replay}
   */
  def forward(cmd: CloudMlCommand, inBackground: Boolean): Unit = {
    if (inBackground) {
      cloudML.fireAndForget(cmd);
    } else {
      manager.enableInterceptionFor(cmd);
      cloudML.fireAndWait(cmd);
      manager.disableInterceptionFor(cmd);
    }
  }

}

/**
 * Entry point of the Application. CloudML can start in two ways:
 * <ul>
 * <li><pre>cloudML my-command</pre> execute the given command in a batch mode
 * <li><pre>'cloudML -i'</pre> starts the interactive mode (i.e., the shell) </li>
 * </ul>
 */
object Runner {

  def main(args: Array[String]): Unit = {
    // Configure the logging framework
    val logConfig = classOf[Shell].getClassLoader().getResourceAsStream("logging.properties")
    LogManager.getLogManager().readConfiguration(logConfig);
    
    // Instantiate the facade and the shell
    val cloudML = Factory.getInstance().getCloudML()
    val shell = new Shell(cloudML)
    CommandParser.factory_=(new CommandFactory())
    
    // Check the command line arguments
    if (args.size >= 1 && args(0) == "-i") { // Interactive mode
      shell.start

    } else { // Batch mode
      val command = args.toList.mkString(" ")
      shell.execute(command)
    }
    
    // Properly stop the facade
    cloudML.terminate()
  }

}