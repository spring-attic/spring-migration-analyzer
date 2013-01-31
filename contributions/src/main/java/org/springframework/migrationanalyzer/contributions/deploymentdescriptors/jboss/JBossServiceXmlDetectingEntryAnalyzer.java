/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.migrationanalyzer.contributions.deploymentdescriptors.jboss;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry.Callback;
import org.springframework.migrationanalyzer.analyze.support.EntryAnalyzer;
import org.springframework.migrationanalyzer.contributions.deploymentdescriptors.DeploymentDescriptor;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

@Component
final class JBossServiceXmlDetectingEntryAnalyzer implements EntryAnalyzer<DeploymentDescriptor> {

    private static final String NODE_NAME_SERVER = "server";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Set<DeploymentDescriptor> analyze(FileSystemEntry fileSystemEntry) {
        Set<DeploymentDescriptor> result = new HashSet<DeploymentDescriptor>();

        if (isJBossServiceXml(fileSystemEntry)) {
            result.add(new DeploymentDescriptor("JBoss", fileSystemEntry, "*-service.xml"));
        }

        return result;
    }

    private boolean isJBossServiceXml(final FileSystemEntry fileSystemEntry) {
        if ((!fileSystemEntry.getName().endsWith("/jboss-service.xml")) && fileSystemEntry.getName().endsWith("-service.xml")) {
            return fileSystemEntry.doWithInputStream(new Callback<InputStream, Boolean>() {

                @Override
                public Boolean perform(InputStream in) {
                    try {
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        Document document = factory.newDocumentBuilder().parse(in);
                        return (document.getFirstChild() != null) && NODE_NAME_SERVER.equals(document.getFirstChild().getNodeName());
                    } catch (SAXException e) {
                        JBossServiceXmlDetectingEntryAnalyzer.this.logger.debug("Failed to parse file system entry '{}'", fileSystemEntry, e);
                    } catch (IOException e) {
                        JBossServiceXmlDetectingEntryAnalyzer.this.logger.debug("Failed to parse file system entry '{}'", fileSystemEntry, e);
                    } catch (ParserConfigurationException e) {
                        JBossServiceXmlDetectingEntryAnalyzer.this.logger.debug("Failed to parse file system entry '{}'", fileSystemEntry, e);
                    }
                    return false;
                }
            });
        }

        return false;
    }
}
