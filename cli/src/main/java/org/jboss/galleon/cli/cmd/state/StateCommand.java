/*
 * Copyright 2016-2018 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.galleon.cli.cmd.state;

import org.jboss.galleon.cli.cmd.plugin.StateProvisionCommand;
import java.util.ArrayList;
import java.util.List;
import org.aesh.command.Command;
import org.aesh.command.CommandException;
import org.aesh.command.CommandResult;
import org.aesh.command.GroupCommand;
import org.aesh.command.GroupCommandDefinition;
import org.aesh.command.container.CommandContainer;
import org.aesh.command.impl.registry.AeshCommandRegistryBuilder;
import org.aesh.command.parser.CommandLineParserException;
import org.jboss.galleon.cli.PmCommandInvocation;
import org.jboss.galleon.cli.PmSession;
import org.jboss.galleon.cli.cmd.state.configuration.ConfigCommand;
import org.jboss.galleon.cli.cmd.state.fp.FPCommand;
import org.jboss.galleon.cli.cmd.state.pkg.PackageCommand;

/**
 *
 * @author jdenise@redhat.com
 */
@GroupCommandDefinition(description = "", name = "state")
public class StateCommand implements GroupCommand<PmCommandInvocation, Command> {

    private final StateUpdateCommand updateCommand;
    //private final DiffCommand diffCommand;
    private final StateProvisionCommand provisionCommand;
    public StateCommand(PmSession pmSession) {
        //this.diffCommand = new DiffCommand(pmSession);
        this.provisionCommand = new StateProvisionCommand(pmSession);
        this.updateCommand = new StateUpdateCommand(pmSession);
    }

    @Override
    public CommandResult execute(PmCommandInvocation commandInvocation) throws CommandException, InterruptedException {
        commandInvocation.println("subcommand missing");
        return CommandResult.FAILURE;
    }

    @Override
    public List<CommandContainer<Command<PmCommandInvocation>, PmCommandInvocation>> getParsedCommands() throws CommandLineParserException {
        List<CommandContainer<Command<PmCommandInvocation>, PmCommandInvocation>> commands = new ArrayList<>();
        commands.add(updateCommand.createCommand());
        //commands.add(diffCommand.createCommand());
        commands.add(provisionCommand.createCommand());
        return commands;
    }

    public static void addActionCommands(AeshCommandRegistryBuilder builder) throws CommandLineParserException {
        builder.command(ConfigCommand.class);
        builder.command(FPCommand.class);
        builder.command(PackageCommand.class);
    }

    @Override
    public List<Command> getCommands() {
        List<Command> commands = new ArrayList<>();
        commands.add(new StateCheckUpdatesCommand());
        commands.add(new StateClearHistoryCommand());
        commands.add(new StateEditCommand());
        commands.add(new StateGetHistoryLimitCommand());
        commands.add(new StateInfoCommand());
        commands.add(new StateNewCommand());
        commands.add(new StateExploreCommand());
        commands.add(new StateExportCommand());
        commands.add(new StateLeaveCommand());
        commands.add(new StateSetHistoryLimitCommand());
        commands.add(new StateUndoCommand());
        return commands;
    }

}
