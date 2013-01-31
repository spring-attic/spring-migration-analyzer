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

package org.springframework.migrationanalyzer.contributions.apiusage.jboss;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry.ExceptionCallback;
import org.springframework.migrationanalyzer.analyze.support.AnalysisFailedException;
import org.springframework.migrationanalyzer.analyze.support.EntryAnalyzer;
import org.springframework.migrationanalyzer.contributions.apiusage.ApiUsage;
import org.springframework.migrationanalyzer.contributions.apiusage.ApiUsageDetector;
import org.springframework.migrationanalyzer.contributions.apiusage.ApiUsageType;
import org.springframework.migrationanalyzer.contributions.xml.StandardXmlArtifactAnalyzer;
import org.springframework.migrationanalyzer.contributions.xml.ValueAnalyzer;
import org.springframework.migrationanalyzer.contributions.xml.XmlArtifactAnalyzer;
import org.springframework.stereotype.Component;

@Component
final class JBossServiceXmlEntryAnalyzer implements EntryAnalyzer<ApiUsage> {

    private static final String XPATH_EXPRESSION_SERVER_MBEAN_CODE = "/server/mbean/@code";

    private final ApiUsageDetector apiUsageDetector;

    @Autowired
    JBossServiceXmlEntryAnalyzer(ApiUsageDetector detector) {
        this.apiUsageDetector = detector;
    }

    @Override
    public Set<ApiUsage> analyze(final FileSystemEntry fileSystemEntry) throws AnalysisFailedException {
        if (fileSystemEntry.getName().endsWith("-service.xml")) {
            return fileSystemEntry.doWithInputStream(new ExceptionCallback<InputStream, Set<ApiUsage>, AnalysisFailedException>() {

                @Override
                public Set<ApiUsage> perform(InputStream in) throws AnalysisFailedException {
                    return analyzeJBossServiceXml(in, fileSystemEntry.getName());

                }
            });
        }

        return Collections.<ApiUsage> emptySet();
    }

    private Set<ApiUsage> analyzeJBossServiceXml(InputStream in, final String name) throws AnalysisFailedException {
        final Set<ApiUsage> results = new HashSet<ApiUsage>();

        XmlArtifactAnalyzer analyzer = new StandardXmlArtifactAnalyzer(in);
        analyzer.analyzeValues(XPATH_EXPRESSION_SERVER_MBEAN_CODE, new ValueAnalyzer() {

            @Override
            public void analyse(String value) {
                ApiUsage usage = JBossServiceXmlEntryAnalyzer.this.apiUsageDetector.detectApiUsage(value, ApiUsageType.DEPLOYMENT_DESCRIPTOR, name,
                    value);
                if (usage != null) {
                    results.add(usage);
                }
            }
        });
        return results;

    }
}
