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

import org.aesh.command.CommandDefinition;
import org.jboss.galleon.cli.CommandExecutionException;
import org.jboss.galleon.cli.PmCommandInvocation;
import org.jboss.galleon.cli.PmSessionCommand;
import org.jboss.galleon.cli.cmd.Headers;
import org.jboss.galleon.cli.cmd.Table;
import org.jboss.galleon.cli.cmd.Table.Cell;
import org.jboss.galleon.cli.config.mvn.MavenConfig;
import org.jboss.galleon.cli.config.mvn.MavenRemoteRepository;

/**
 *
 * @author jdenise@redhat.com
 */
@CommandDefinition(name = "info", description = "Display maven config")
public class MavenInfo extends PmSessionCommand {

    @Override
    protected void runCommand(PmCommandInvocation session) throws CommandExecutionException {
        Table t = new Table(Headers.CONFIGURATION_ITEM, Headers.VALUE);
        MavenConfig config = session.getPmSession().getPmConfiguration().getMavenConfig();
        t.addLine("Maven xml settings", (config.getSettings() == null ? "No settings file set"
                : config.getSettings().normalize().toString()));
        t.addLine("Local repository", config.getLocalRepository().normalize().toString());
        Cell repositories = new Cell();
        Cell title = new Cell("Remote repositories");
        if (config.getRemoteRepositories().isEmpty()) {
            repositories.addLine("None");
        } else {
            for (MavenRemoteRepository rep : session.getPmSession().
                    getPmConfiguration().getMavenConfig().getRemoteRepositories()) {
                repositories.addLine(rep.getName());
                repositories.addLine(" url=" + rep.getUrl());
                repositories.addLine(" type=" + rep.getType());
            }
        }
        t.addCellsLine(title, repositories);
        t.sort(Table.SortType.ASCENDANT);
        session.println(t.build());
    }

}
