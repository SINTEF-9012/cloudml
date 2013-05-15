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
import jline.console.ConsoleReader
import jline.console.completer.Completer
import jline.console.history.MemoryHistory
import jline.console.completer.StringsCompleter
import jline.console.completer.FileNameCompleter
import jline.console.completer.ArgumentCompleter
import org.cloudml.ui.shell.configuration.Loader

/**
 * Gather all the parameters needed to print and read from the Console
 *
 * @author Franck Chauvel - SINTEF ICT
 * @since 0.0.1
 */
class Terminal {

  val configuration = Loader.getInstance().getConfiguration()
  
  // Configuration of the JLine console reader
  jline.TerminalFactory.configure("auto")

  private[this] val reader = new ConsoleReader()
  reader.setHistory(new MemoryHistory())

  private[this] val completors = List[Completer](
    new StringsCompleter(configuration.getCommands().map { c => c.getSyntax() }),
    new FileNameCompleter())

  reader.addCompleter(new ArgumentCompleter(completors))

  
  def newLine: Unit = {
    reader.println()
  }
  
  /**
   * Prompt the user for a command
   */
  def prompt: String = {
    reader.println("")
    reader.readLine(Console.CYAN + configuration.getPrompt() + Console.RESET)
  }

  /**
   * Display a given message to the user, without adding carriage return
   *
   * @param color the colour code to use to display the message on the terminal
   *
   * @param message the message to show to the suer
   */
  def print(message: String, color: String = Console.WHITE): Unit =
    reader.print(color + message + Console.RESET)

  /**
   * Display a message on the terminal and add a carriage return at the end
   *
   * @param color the colour to use to display the message
   * 
   * @param message the message to show
   */
  def println(message: String, color: String = Console.WHITE): Unit = {
    reader.println(color + message + Console.RESET)  
    reader.flush
  }

}