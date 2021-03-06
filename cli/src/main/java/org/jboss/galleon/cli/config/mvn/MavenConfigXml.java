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
package org.jboss.galleon.cli.config.mvn;

import java.io.IOException;
import java.nio.file.Paths;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import org.jboss.galleon.ProvisioningException;
import org.jboss.galleon.util.ParsingUtils;
import org.jboss.staxmapper.XMLExtendedStreamReader;

/**
 *
 * @author jdenise@redhat.com
 */
public class MavenConfigXml {

    public static final String REPOSITORIES = "repositories";
    public static final String LOCAL_REPOSITORY = "local-repository";
    public static final String SETTINGS = "settings";
    public static final String NAME = "name";
    public static final String TYPE = "type";
    public static final String REPOSITORY = "repository";
    public static final String MAVEN = "maven";

    public static void read(XMLExtendedStreamReader reader, MavenConfig config)
            throws ProvisioningException, XMLStreamException, IOException {
        while (reader.hasNext()) {
            switch (reader.nextTag()) {
                case XMLStreamConstants.END_ELEMENT: {
                    // DONE.
                    return;
                }
                case XMLStreamConstants.START_ELEMENT: {
                    switch (reader.getLocalName()) {
                        case REPOSITORIES: {
                            readRepositories(reader, config);
                            break;
                        }
                        case LOCAL_REPOSITORY: {
                            config.setLocalRepository(Paths.get(reader.getElementText()));
                            break;
                        }
                        case SETTINGS: {
                            config.setSettings(Paths.get(reader.getElementText()));
                            break;
                        }
                        default: {
                            throw ParsingUtils.unexpectedContent(reader);
                        }
                    }
                    break;
                }
                default: {
                    throw ParsingUtils.unexpectedContent(reader);
                }
            }
        }
    }

    private static void readRepositories(XMLExtendedStreamReader reader, MavenConfig config)
            throws ProvisioningException, XMLStreamException, IOException {
        while (reader.hasNext()) {
            switch (reader.nextTag()) {
                case XMLStreamConstants.END_ELEMENT: {
                    if (reader.getLocalName().equals(REPOSITORIES)) {
                        return;
                    }
                    break;
                }
                case XMLStreamConstants.START_ELEMENT: {
                    switch (reader.getLocalName()) {
                        case REPOSITORY: {
                            MavenRemoteRepository repo = new MavenRemoteRepository(reader.getAttributeValue(null, NAME),
                                    reader.getAttributeValue(null, TYPE), reader.getElementText());
                            config.addRemoteRepository(repo);
                            break;
                        }
                        default: {
                            throw ParsingUtils.unexpectedContent(reader);
                        }
                    }
                    break;
                }
                default: {
                    throw ParsingUtils.unexpectedContent(reader);
                }
            }
        }
    }
}
