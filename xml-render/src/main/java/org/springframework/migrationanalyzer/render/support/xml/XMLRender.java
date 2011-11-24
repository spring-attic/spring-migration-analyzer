/*
 * Copyright 2011 the original author or authors.
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

package org.springframework.migrationanalyzer.render.support.xml;

import java.util.Set;

import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.contributions.apiusage.ApiUsage;
import org.springframework.migrationanalyzer.contributions.deploymentdescriptors.DeploymentDescriptor;
import org.springframework.migrationanalyzer.contributions.ejb.EntityBean;
import org.springframework.migrationanalyzer.contributions.ejb.SessionBean;
import org.springframework.migrationanalyzer.render.support.xml.entity.XMLApplication;
import org.springframework.migrationanalyzer.render.support.xml.entity.XMLFileSystemEntry;
import org.springframework.migrationanalyzer.render.support.xml.entity.XMLRoot;

import com.thoughtworks.xstream.XStream;

public class XMLRender {

    XStream xstream = null;
    
    public XMLRender() {
        this.xstream = new XStream();
        this.xstream.alias("analysisResultEntry", AnalysisResultEntry.class);
        this.xstream.alias("fileSystemEntry", FileSystemEntry.class);
        this.xstream.alias("apiUsage", ApiUsage.class);
        this.xstream.alias("deploymentDescriptor", DeploymentDescriptor.class);
        this.xstream.alias("entityBean", EntityBean.class);
        // xstream.alias("messageDrivenBean");
        this.xstream.alias("sessionBean", SessionBean.class);
        // xstream.alias("springConfiguration", SpringConfiguration.class);
        this.xstream.alias("fileSystemEntry", XMLFileSystemEntry.class);
        this.xstream.alias("app", XMLApplication.class);
        this.xstream.alias("root", XMLRoot.class);
    }

    /*
     * @Test public void testXStream() { ApiUsage a = new ApiUsage(ApiUsageType.FIELD, "apiName", "user",
     * "usageDescription", "guidanceView", MigrationCost.LOW); XStream xstream = new XStream();
     * xstream.alias("ApiUsage", ApiUsage.class); String xml = xstream.toXML(a); System.out.println(xml); }
     */

    public String getXMLRootToString(XMLRoot root) {
        return this.xstream.toXML(root);
    }

    public <T> String getXMLString(Class<?> resultType, Set<AnalysisResultEntry<T>> entries) {
        StringBuffer sb = new StringBuffer();
        for (AnalysisResultEntry<T> analysisResultEntry : entries) {
            T result = analysisResultEntry.getResult();

            // System.out.println(xstream.toXML(analysisResultEntry));

            sb.append(this.xstream.toXML(result));
        }

        return sb.toString();
    }
}