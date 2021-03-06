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

import java.nio.file.Path;
import org.aesh.command.CommandDefinition;
import org.jboss.galleon.cli.AbstractStateCommand;
import org.jboss.galleon.cli.CommandExecutionException;
import org.jboss.galleon.cli.PmCommandInvocation;
import org.jboss.galleon.cli.PmSession;
import org.jboss.galleon.cli.cmd.CliErrors;
import org.jboss.galleon.cli.model.FeatureContainer;
import org.jboss.galleon.cli.path.FeatureContainerPathConsumer;
import org.jboss.galleon.cli.path.PathParser;

@CommandDefinition(name = "explore", description = "Explore an installation", activator = NoStateCommandActivator.class)
public class StateExploreCommand extends AbstractStateCommand {

    @Override
    protected void runCommand(PmCommandInvocation session) throws CommandExecutionException {
        PmSession pm = session.getPmSession();
        Path install;
        try {
            if (pm.getContainer() != null) {
                throw new CommandExecutionException("Already entered, use leave command");
            }
            FeatureContainer container = getFeatureContainer(pm, null);
            pm.setExploredContainer(container);
            install = getInstallationDirectory(session.getAeshContext());
            pm.setCurrentPath(FeatureContainerPathConsumer.ROOT);
        } catch (Exception ex) {
            if (ex instanceof CommandExecutionException) {
                throw (CommandExecutionException) ex;
            }
            throw new CommandExecutionException(session.getPmSession(), CliErrors.exploreFailed(), ex);
        }
        session.setPrompt(PmSession.buildPrompt("" + PathParser.PATH_SEPARATOR));
        session.println("Exploring " + install.getFileName() + ". Use 'state leave' to leave exploration.");
    }
}
