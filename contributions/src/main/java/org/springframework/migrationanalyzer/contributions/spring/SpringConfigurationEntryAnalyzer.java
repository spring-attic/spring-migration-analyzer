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

package org.springframework.migrationanalyzer.contributions.spring;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.analyze.support.AnalysisFailedException;
import org.springframework.migrationanalyzer.analyze.support.EntryAnalyzer;
import org.springframework.migrationanalyzer.contributions.xml.NodeAnalyzer;
import org.springframework.migrationanalyzer.contributions.xml.StandardXmlArtifactAnalyzer;
import org.springframework.migrationanalyzer.contributions.xml.ValueAnalyzer;
import org.springframework.migrationanalyzer.contributions.xml.XmlArtifactAnalyzer;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

@SuppressWarnings("rawtypes")
@Component
final class SpringConfigurationEntryAnalyzer implements EntryAnalyzer<Object> {

    private static final String FILE_NAME_SUFFIX = ".xml";

    private static final String BEANS_NAMESPACE_URI = "http://www.springframework.org/schema/beans";

    private static final String BEANS_NODE_NAME = "beans";

    private static final Map<String, String> NAMESPACE_MAPPING;

    static {
        NAMESPACE_MAPPING = new HashMap<String, String>();
        NAMESPACE_MAPPING.put("beans", "http://www.springframework.org/schema/beans");
        NAMESPACE_MAPPING.put("aop", "http://www.springframework.org/schema/aop");
        NAMESPACE_MAPPING.put("context", "http://www.springframework.org/schema/context");
        NAMESPACE_MAPPING.put("jee", "http://www.springframework.org/schema/jee");
        NAMESPACE_MAPPING.put("jms", "http://www.springframework.org/schema/jms");
        NAMESPACE_MAPPING.put("lang", "http://www.springframework.org/schema/lang");
        NAMESPACE_MAPPING.put("jms", "http://www.springframework.org/schema/jms");
        NAMESPACE_MAPPING.put("util", "http://www.springframework.org/schema/util");
        NAMESPACE_MAPPING.put("oxm", "http://www.springframework.org/schema/oxm");
        NAMESPACE_MAPPING.put("osgi", "http://www.springframework.org/schema/osgi");
        NAMESPACE_MAPPING.put("tx", "http://www.springframework.org/schema/tx");
        NAMESPACE_MAPPING.put("webflow", "http://www.springframework.org/schema/webflow-config");
    }

    private static final String CLASS_EXPRESSION = //
    "//beans:bean/@class | " + //
        "//aop:declare-parents/@implement-interface | " + //
        "//aop:declare-parents/@default-impl | " + //
        "//context:load-time-weaver/@weaver-class | " + //
        "//context:component-scan/@name-generator | " + //
        "//context:component-scan/@scope-resolver | " + //
        "//jee:jndi-lookup/@expected-type | " + //
        "//jee:jndi-lookup/@proxy-interface | " + //
        "//jee:remote-slsb/@home-interface | " + //
        "//jee:remote-slsb/@business-interface | " + //
        "//jee:local-slsb/@business-interface | " + //
        "//jms:listener-container/@container-class | " + //
        "//lang:jruby/@script-interfaces | " + //
        "//lang:bsh/@script-interfaces | " + //
        "//oxm:class-to-be-bound/@name | " + //
        "//oxm:jibx-marshaller/@target-class | " + //
        "//osgi:reference/@interface | " + //
        "//osgi:service/@interface | " + //
        "//util:list/@list-class | " + //
        "//util:map/@map-class | " + //
        "//util:set/@set-class | " + //
        "//webflow:flow-builder/@class | " + //
        "//webflow:attribute/@type | " + //
        "//osgi:service/osgi:interfaces/beans:value | " + //
        "//osgi:reference/osgi:interfaces/beans:value";

    private final Set<SpringConfigurationClassValueAnalyzer> classValueAnalyzers;

    @Autowired
    SpringConfigurationEntryAnalyzer(Set<SpringConfigurationClassValueAnalyzer> analyzers) {
        this.classValueAnalyzers = analyzers;
    }

    @Override
    public Set<Object> analyze(final FileSystemEntry fileSystemEntry) throws AnalysisFailedException {
        final Set<Object> results = new HashSet<Object>();

        if (isSpringConfiguration(fileSystemEntry)) {
            results.add(new SpringConfiguration(fileSystemEntry, new File(fileSystemEntry.getName()).getName()));

            XmlArtifactAnalyzer xmlAnalyzer = new StandardXmlArtifactAnalyzer(fileSystemEntry.getInputStream(), NAMESPACE_MAPPING);
            xmlAnalyzer.analyzeValues(CLASS_EXPRESSION, new SpringConfigurationClassValueAnalyzerDelegatingValueAnalyzer(results, fileSystemEntry));

            xmlAnalyzer.analyzeNodes("//jee:local-slsb | //jee:remote-slsb", new EjbIntegrationRecordingNodeAnalyzer(results, fileSystemEntry));

            xmlAnalyzer.analyzeNodes("//tx:jta-transaction-manager", new JtaIntegrationRecordingNodeAnalyzer(fileSystemEntry, results));
        }

        return results;
    }

    private boolean isSpringConfiguration(FileSystemEntry fileSystemEntry) {
        if (fileSystemEntry.getName().endsWith(FILE_NAME_SUFFIX)) {
            try {
                DocumentBuilderFactory xmlFact = DocumentBuilderFactory.newInstance();
                xmlFact.setNamespaceAware(true);

                DocumentBuilder builder = xmlFact.newDocumentBuilder();
                builder.setEntityResolver(new EmptyInputSourceEntityResolver());
                builder.setErrorHandler(new NoOpErrorHandler());
                Document document = builder.parse(new InputSource(fileSystemEntry.getInputStream()));

                Node firstChild = document.getFirstChild();

                return (BEANS_NAMESPACE_URI.equals(firstChild.getNamespaceURI()) && (BEANS_NODE_NAME.equals(firstChild.getNodeName())));
            } catch (IOException e) {
                return false;
            } catch (SAXException e) {
                return false;
            } catch (ParserConfigurationException e) {
                return false;
            }
        }

        return false;
    }

    private static final class EmptyInputSourceEntityResolver implements EntityResolver {

        @Override
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            return new InputSource(new StringReader(""));
        }
    }

    private static final class NoOpErrorHandler implements ErrorHandler {

        @Override
        public void error(SAXParseException e) throws SAXException {
        }

        @Override
        public void fatalError(SAXParseException e) throws SAXException {
        }

        @Override
        public void warning(SAXParseException e) throws SAXException {
        }
    }

    private final class SpringConfigurationClassValueAnalyzerDelegatingValueAnalyzer implements ValueAnalyzer {

        private final Set<Object> results;

        private final FileSystemEntry fileSystemEntry;

        private SpringConfigurationClassValueAnalyzerDelegatingValueAnalyzer(Set<Object> results, FileSystemEntry fileSystemEntry) {
            this.results = results;
            this.fileSystemEntry = fileSystemEntry;
        }

        @Override
        public void analyse(String value) {
            for (SpringConfigurationClassValueAnalyzer<?> classValueAnalyzer : SpringConfigurationEntryAnalyzer.this.classValueAnalyzers) {
                Object result = classValueAnalyzer.analyze(value, this.fileSystemEntry);
                if (result != null) {
                    this.results.add(result);
                }
            }
        }
    }

    private static final class JtaIntegrationRecordingNodeAnalyzer implements NodeAnalyzer {

        private final FileSystemEntry fileSystemEntry;

        private final Set<Object> results;

        private JtaIntegrationRecordingNodeAnalyzer(FileSystemEntry fileSystemEntry, Set<Object> results) {
            this.fileSystemEntry = fileSystemEntry;
            this.results = results;
        }

        @Override
        public void analyze(Node node) {
            this.results.add(new SpringJtaIntegration(new File(this.fileSystemEntry.getName()).getName(), this.fileSystemEntry));
        }
    }

    private static final class EjbIntegrationRecordingNodeAnalyzer implements NodeAnalyzer {

        private final Set<Object> results;

        private final FileSystemEntry fileSystemEntry;

        private EjbIntegrationRecordingNodeAnalyzer(Set<Object> results, FileSystemEntry fileSystemEntry) {
            this.results = results;
            this.fileSystemEntry = fileSystemEntry;
        }

        @Override
        public void analyze(Node node) {
            this.results.add(new SpringEjbIntegration(new File(this.fileSystemEntry.getName()).getName(), this.fileSystemEntry));
        }
    }
}