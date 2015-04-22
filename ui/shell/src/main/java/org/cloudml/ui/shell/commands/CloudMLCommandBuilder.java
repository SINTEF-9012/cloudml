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

import org.antlr.v4.runtime.tree.TerminalNode;
import org.cloudml.facade.commands.CloudMlCommand;
import org.cloudml.facade.commands.*;
import org.cloudml.ui.shell.commands.builder.ShellCommandsBaseVisitor;
import org.cloudml.ui.shell.commands.builder.ShellCommandsParser;
import org.cloudml.ui.shell.terminal.Terminal;

import java.util.ArrayList;
import java.util.Arrays;

import static org.cloudml.facade.commands.ValidateCommand.REPORT_ONLY_ERRORS;
import static org.cloudml.facade.commands.ValidateCommand.REPORT_WARNINGS_AND_ERRORS;

/**
 * Traverse subparts of the ANTLR parse tree and output the related
 * CloudMLCommand objects.
 */
public class CloudMLCommandBuilder extends ShellCommandsBaseVisitor<CloudMlCommand> {

    @Override
    public CloudMlCommand visitAnalyseRobustness(ShellCommandsParser.AnalyseRobustnessContext ctx) {
        if (ctx.filter() != null) {
            return new AnalyseRobustness(ctx.filter().observe.getText(), ctx.filter().control.getText());
        }
        return new AnalyseRobustness();
    }

    @Override
    public CloudMlCommand visitDeploy(ShellCommandsParser.DeployContext ctx) {
        return new Deploy();
    }

    @Override
    public CloudMlCommand visitUpload(ShellCommandsParser.UploadContext ctx) {
        return new Upload(ctx.local.getText(), ctx.ID().getText(), ctx.remote.getText());
    }

    @Override
    public CloudMlCommand visitConnect(ShellCommandsParser.ConnectContext ctx) {
        return new Attach(ctx.customer.getText(), ctx.provider.getText());
    }

    @Override
    public CloudMlCommand visitDisconnect(ShellCommandsParser.DisconnectContext ctx) {
        return new Detach(ctx.customer.getText(), ctx.provider.getText());
    }

    @Override
    public CloudMlCommand visitDestroy(ShellCommandsParser.DestroyContext ctx) {
        return new Destroy(ctx.ID().getText());
    }

    @Override
    public CloudMlCommand visitInstall(ShellCommandsParser.InstallContext ctx) {
        return new Install(ctx.component.getText(), ctx.platform.getText());
    }

    @Override
    public CloudMlCommand visitInstantiate(ShellCommandsParser.InstantiateContext ctx) {
        return new Instantiate(ctx.type.getText(), ctx.instance.getText());
    }

    @Override
    public CloudMlCommand visitList(ShellCommandsParser.ListContext ctx) {
        final String level = ctx.level().getText();
        if (level.startsWith("instance")) {
            return new ListComponentInstances();
        } else {
            return new ListComponents();
        }
    }

    @Override
    public CloudMlCommand visitLoad(ShellCommandsParser.LoadContext ctx) {
        final String kind = ctx.kind().getText();
        if (kind.equalsIgnoreCase("deployment")) {
            return new LoadDeployment(ctx.PATH().getText());
        } else {
            return new LoadCredentials(ctx.PATH().getText());
        }
    }

    @Override
    public CloudMlCommand visitStore(ShellCommandsParser.StoreContext ctx) {
        final String kind = ctx.kind().getText();
        if (kind.equalsIgnoreCase("deployment")) {
            return new StoreDeployment(ctx.PATH().getText());
        } else {
            return new StoreCredentials(ctx.PATH().getText());
        }
    }

    @Override
    public CloudMlCommand visitView(ShellCommandsParser.ViewContext ctx) {
        final String level = ctx.level().getText();
        if (level.startsWith("instance")) {
            return new ViewComponentInstance(ctx.ID().getText());
        } else {
            return new ViewComponent(ctx.ID().getText());
        }
    }

    @Override
    public CloudMlCommand visitShotImage(ShellCommandsParser.ShotImageContext ctx) {
        return new ShotImage(ctx.PATH().getText());
    }

    @Override
    public CloudMlCommand visitSnapshot(ShellCommandsParser.SnapshotContext ctx) {
        return new Snapshot(ctx.ID().getText());
    }

    @Override
    public CloudMlCommand visitScaleOut(ShellCommandsParser.ScaleOutContext ctx) {
        return new ScaleOut(ctx.ID().getText());
    }

    @Override
    public CloudMlCommand visitMultipleScaleOut(ShellCommandsParser.MultipleScaleOutContext ctx){
          return new ScaleOut(ctx.ID().getText(), Integer.parseInt(ctx.times.getText()));
    }


    @Override
    public CloudMlCommand visitImage(ShellCommandsParser.ImageContext ctx) {
        return new Image(ctx.ID().getText());
    }

    @Override
    public CloudMlCommand visitReset(ShellCommandsParser.ResetContext ctx) {
        return new Reset();
    }

    @Override
    public CloudMlCommand visitStop(ShellCommandsParser.StopContext ctx) {
        return new StopComponent(ctx.ID().getText());
    }

    @Override
    public CloudMlCommand visitUninstall(ShellCommandsParser.UninstallContext ctx) {
        return new Uninstall(ctx.platform.getText(), ctx.component.getText());
    }

    @Override
    public CloudMlCommand visitStart(ShellCommandsParser.StartContext ctx) {
        ArrayList<String> al=new ArrayList<String>();
        for(TerminalNode t:ctx.ID()){
            al.add(t.getText());
        }
        return new StartComponent(al);
    }

    @Override
    public CloudMlCommand visitValidate(ShellCommandsParser.ValidateContext ctx) {
        if (ctx.noWarnings != null) {
            return new ValidateCommand(REPORT_ONLY_ERRORS);
        }
        return new ValidateCommand(REPORT_WARNINGS_AND_ERRORS);
    }

    @Override
    public CloudMlCommand visitDebugMode(ShellCommandsParser.DebugModeContext ctx) {
        return new DebugMode(ctx.state.getText().equals("true"));
    }

    @Override
    public CloudMlCommand visitBurst(ShellCommandsParser.BurstContext ctx) {
        return new Burst(ctx.ID(0).getText(),ctx.ID(1).getText());
    }

}
