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

import org.cloudml.facade.commands.CloudMlCommand;
import org.cloudml.facade.commands.*;
import org.cloudml.ui.shell.commands.builder.ShellCommandsBaseVisitor;
import org.cloudml.ui.shell.commands.builder.ShellCommandsParser;

/**
 * Traverse subparts of the ANTLR parse tree and output the related
 * CloudMLCommand objects.
 */
public class CloudMLCommandBuilder extends ShellCommandsBaseVisitor<CloudMlCommand> {
    
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
        return new Snapshot(ctx.ID().getText());
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
        return new StartComponent(ctx.ID().getText());
    }
    
    
}
