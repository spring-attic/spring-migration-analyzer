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

package org.springframework.migrationanalyzer.analyze.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.migrationanalyzer.util.IoUtils;
import org.springframework.migrationanalyzer.util.ZipUtils;

/**
 * Standard implementation of <code>ClassPathScanner</code>.
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Not thread-safe.
 * 
 */
public final class StandardClassPathScanner implements ClassPathScanner {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public <T> Set<Class<? extends T>> findImplementations(Class<? extends T> candidateInterface, URLClassLoader classLoader) {
        ClassLoaderClassPathScanningTemplate<T> template = new ClassLoaderClassPathScanningTemplate<T>(candidateInterface, classLoader);
        scan(classLoader, template);
        return template.getResources();
    }

    @Override
    public Set<String> findResources(Pattern pattern, URLClassLoader classLoader) {
        RegexResourceClassPathScannerTemplate template = new RegexResourceClassPathScannerTemplate(pattern);
        scan(classLoader, template);
        return template.getResources();
    }

    private void scan(URLClassLoader classLoader, ClassPathScanningTemplate template) {
        for (URL url : classLoader.getURLs()) {
            try {
                File file = new File(url.toURI());

                if (file.isDirectory()) {
                    scan(file, file, template);
                } else if (isJar(file)) {
                    scan(new JarFile(file), template);
                } else {
                    this.logger.warn("Will not scan classpath entry '{}' as it is not a JAR file or directory", file.getAbsolutePath());
                }
            } catch (IOException e) {
                this.logger.error("Unable to scan classpath entry '{}'", url.toString(), e);
            } catch (URISyntaxException e) {
                this.logger.error("Unable to scan classpath entry '{}'", url.toString(), e);
            }
        }
    }

    private void scan(JarFile jarFile, ClassPathScanningTemplate template) {
        this.logger.debug("Scanning classes in classloader entry '{}'", jarFile);

        for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements();) {
            JarEntry entry = entries.nextElement();
            template.process(entry.getName());
        }
    }

    private void scan(File directory, File root, ClassPathScanningTemplate template) {
        this.logger.debug("Scanning classes in classloader entry '{}'", directory.getAbsolutePath());

        for (File child : directory.listFiles()) {
            if (child.isDirectory()) {
                scan(child, root, template);
            } else {
                String path = child.getAbsolutePath().substring(root.getAbsolutePath().length() + 1);
                template.process(path);
            }
        }
    }

    private boolean isJar(File file) throws IOException {
        InputStream input = null;
        try {
            input = new FileInputStream(file);
            return ZipUtils.isZipFile(input);
        } finally {
            IoUtils.closeQuietly(input);
        }
    }

}
