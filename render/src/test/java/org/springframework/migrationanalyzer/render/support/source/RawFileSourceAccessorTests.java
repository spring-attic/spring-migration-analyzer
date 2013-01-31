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

package org.springframework.migrationanalyzer.render.support.source;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.StringReader;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry.Callback;

public class RawFileSourceAccessorTests {

    private final SourceAccessor sourceAccessor = new RawFileSourceAccessor();

    @Test
    public void sourceAvailableForXmlFile() {
        assertEquals("the source for the file foo.xml", this.sourceAccessor.getSource(createFileSystemEntry("foo.xml")));
    }

    @Test
    public void sourceAvailableForXmiFile() {
        assertEquals("the source for the file foo.xmi", this.sourceAccessor.getSource(createFileSystemEntry("foo.xmi")));
    }

    @Test
    public void sourceUnavailableForClassFile() {
        assertNull(this.sourceAccessor.getSource(createFileSystemEntry("foo.class")));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private FileSystemEntry createFileSystemEntry(final String name) {
        FileSystemEntry fileSystemEntry = mock(FileSystemEntry.class);
        when(fileSystemEntry.getName()).thenReturn(name);
        when(fileSystemEntry.doWithReader(any(Callback.class))).thenAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return ((Callback) invocation.getArguments()[0]).perform(new StringReader("the source for the file " + name));
            }

        });
        return fileSystemEntry;
    }
}
