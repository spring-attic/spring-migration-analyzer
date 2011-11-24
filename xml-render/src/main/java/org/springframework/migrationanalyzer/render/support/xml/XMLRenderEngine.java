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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.migrationanalyzer.analyze.AnalysisResult;
import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.render.RenderEngine;
import org.springframework.migrationanalyzer.render.support.xml.entity.XMLApplication;
import org.springframework.migrationanalyzer.render.support.xml.entity.XMLFileSystemEntry;
import org.springframework.migrationanalyzer.render.support.xml.entity.XMLRoot;

public class XMLRenderEngine implements RenderEngine {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String fileName = "";

    private String outputPath = "";

    public XMLRenderEngine(String outputPath) {
        if (!new File(outputPath).exists()) {
            new File(outputPath).mkdirs();
        }
        this.fileName = outputPath + "/rawdata.xml";
        this.outputPath = outputPath;
    }

    @Override
    public void render(AnalysisResult analysisResult) {
        this.logger.info("Starting XML rendering");
        StringBuffer sb = new StringBuffer();

        XMLRoot root = new XMLRoot();
        XMLApplication app = new XMLApplication(this.outputPath);

        Set<FileSystemEntry> fileSystemEntries = analysisResult.getFileSystemEntries();

        XMLRender x = new XMLRender();

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");

        for (FileSystemEntry fileSystemEntry : fileSystemEntries) {

            XMLFileSystemEntry xmlFileEntry = new XMLFileSystemEntry(fileSystemEntry.getName(), new HashSet<Object>());

            AnalysisResult entryResult = analysisResult.getResultForEntry(fileSystemEntry);

            for (Class<?> resultType : entryResult.getResultTypes()) {

                for (AnalysisResultEntry<?> resultEntry : entryResult.getResultEntries(resultType)) {
                    xmlFileEntry.getResults().add(resultEntry.getResult());
                }

            }
            app.getFileEntries().add(xmlFileEntry);
        }

        root.getApplications().add(app);

        sb.append(x.getXMLRootToString(root));

        writeToFile(sb);

        this.logger.info("Finished XML rendering");

    }

    private void writeToFile(StringBuffer sb) {
        File file = new File(this.fileName);
        if (file.exists()) {
            file.delete();
        }

        try {
            file.createNewFile();
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.println(sb.toString());
            printWriter.flush();
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
