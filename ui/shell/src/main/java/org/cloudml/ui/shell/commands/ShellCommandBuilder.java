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

package org.cloudml.ui.shell.commands;

import java.util.ArrayList;
import java.util.List;
import org.cloudml.facade.commands.CloudMlCommand;
import org.cloudml.ui.shell.commands.builder.ShellCommandsBaseVisitor;

import org.cloudml.ui.shell.commands.builder.ShellCommandsParser.*;

/**
 * Traverse the ANTLR parse tree and build the related ShellCommand objects
 */
public class ShellCommandBuilder extends ShellCommandsBaseVisitor<ShellCommand> {

    @Override
    public ShellCommand visitScript(ScriptContext ctx) {
        final List<ShellCommand> commands = new ArrayList<ShellCommand>();
        for (CommandContext each: ctx.command()) {
            commands.add(each.accept(this));
        }
        return new Script(commands);
    }

    @Override
    public ShellCommand visitDeploy(DeployContext ctx) {
        return super.visitDeploy(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ShellCommand visitExit(ExitContext ctx) {
        return ShellCommand.exit();
    }

    @Override
    public ShellCommand visitVersion(VersionContext ctx) {
        return ShellCommand.version();
    }

    @Override
    public ShellCommand visitHelp(HelpContext ctx) {
        if (ctx.STRING() != null) {
            return ShellCommand.help(ctx.STRING().getText().replaceAll("\"", ""));
        }
        return ShellCommand.help();
    }

    @Override
    public ShellCommand visitHistory(HistoryContext ctx) {
        if (ctx.INTEGER() != null) {
            final int depth = Integer.parseInt(ctx.INTEGER().getText());
            return ShellCommand.history(depth);
        }
        return ShellCommand.history();
    }

    @Override
    public ShellCommand visitDump(DumpContext ctx) {
        if (ctx.INTEGER() != null) {
            final int depth = Integer.parseInt(ctx.INTEGER().getText());
            return ShellCommand.dumpTo(depth, ctx.PATH().getText());
        }
        return ShellCommand.dumpTo(ctx.PATH().getText());
    }

    @Override
    public ShellCommand visitReplay(ReplayContext ctx) {
        return ShellCommand.replay(ctx.PATH().getText());
    }

    @Override
    public ShellCommand visitQuit(QuitContext ctx) {
        return ShellCommand.exit();
    }

    @Override
    public ShellCommand visitMessages(MessagesContext ctx) {
         if (ctx.INTEGER() != null) {
            final int depth = Integer.parseInt(ctx.INTEGER().getText());
            return ShellCommand.showMessages(depth);
        }
        return ShellCommand.showMessages();
    }

    @Override
    public ShellCommand visitProxy(ProxyContext ctx) {
        final boolean runInBackground = true;
        final CloudMlCommand action = ctx.action().accept(new CloudMLCommandBuilder());
        return ShellCommand.delegate(action, runInBackground);
    }
    
    
    
    

}
