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
package org.jboss.galleon.cli.cmd.mvn;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.xml.stream.XMLStreamException;
import org.aesh.command.CommandDefinition;
import org.aesh.command.option.Argument;
import org.jboss.galleon.cli.CommandExecutionException;
import org.jboss.galleon.cli.PmCommandInvocation;
import org.jboss.galleon.cli.PmSessionCommand;
import org.jboss.galleon.cli.cmd.CliErrors;

/**
 *
 * @author jdenise@redhat.com
 */
@CommandDefinition(name = "set-local-repository", description = "Set or reset path to local repository path")
public class MavenSetLocalRepository extends PmSessionCommand {

    @Argument(description = "Path to local maven repository. no value to reset to default value", required = false)
    private File path;

    @Override
    protected void runCommand(PmCommandInvocation session) throws CommandExecutionException {
        Path p = path == null ? null : path.toPath();
        try {
            if (p != null) {
                if (!Files.exists(p)) {
                    throw new CommandExecutionException("Local repository directory " + p + " doesn't exist.");
                }
            }
            session.getPmSession().getPmConfiguration().getMavenConfig().setLocalRepository(p);
        } catch (XMLStreamException | IOException ex) {
            throw new CommandExecutionException(session.getPmSession(), CliErrors.setLocalRepositoryFailed(), ex);
        }
    }

}
