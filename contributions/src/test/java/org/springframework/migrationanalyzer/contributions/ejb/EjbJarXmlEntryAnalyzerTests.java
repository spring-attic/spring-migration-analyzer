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

package org.springframework.migrationanalyzer.contributions.ejb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry.ExceptionCallback;
import org.springframework.migrationanalyzer.util.IoUtils;

public class EjbJarXmlEntryAnalyzerTests {

    private final EjbJarXmlEntryAnalyzer analyzer = new EjbJarXmlEntryAnalyzer();

    @Test
    public void ejb20Analysis() throws Exception {
        analysis("2_0-ejb-jar.xml");
    }

    @Test
    public void ejb21Analysis() throws Exception {
        analysis("2_1-ejb-jar.xml");
    }

    @Test
    public void ejb30Analysis() throws Exception {
        analysis("3_0-ejb-jar.xml");
    }

    @Test
    public void ejb31Analysis() throws Exception {
        analysis("3_1-ejb-jar.xml");
    }

    private void analysis(String name) throws Exception {
        Set<Ejb> analysis = this.analyzer.analyze(createFileSystemEntry(name));
        assertNotNull(analysis);
        assertEquals(4, analysis.size());

        List<SessionBean> sessionBeans = new ArrayList<SessionBean>();

        for (Object object : analysis) {
            if (object instanceof EntityBean) {
                assertEntityBean((EntityBean) object);
            } else if (object instanceof MessageDrivenBean) {
                assertMessageDrivenBean((MessageDrivenBean) object);
            } else {
                sessionBeans.add((SessionBean) object);
            }
        }

        Iterator<SessionBean> iterator = sessionBeans.iterator();
        SessionBean sessionBean = iterator.next();
        if (sessionBean.getSessionType() == SessionType.STATELESS) {
            assertSessionBean("Stateless", sessionBean);
            assertSessionBean("Stateful", iterator.next());
        } else {
            assertSessionBean("Stateful", sessionBean);
            assertSessionBean("Stateless", iterator.next());
        }
    }

    @Test
    public void nonEjbJarXmlFilesAreIgnored() throws Exception {
        Set<Ejb> analysis = this.analyzer.analyze(createFileSystemEntry("web.xml"));
        assertNotNull(analysis);
        assertEquals(0, analysis.size());
    }

    private void assertSessionBean(String prefix, SessionBean sessionBean) {
        assertEquals("test." + prefix + "TestBean", sessionBean.getEjbClass());
        assertEquals("test." + prefix + "TestHome", sessionBean.getHome());
        assertEquals("test." + prefix + "Test", sessionBean.getRemote());
        assertEquals(6, sessionBean.getTransactionPropagationTypes().size());
    }

    private void assertEntityBean(EntityBean entityBean) {
        assertEquals("test.Entity", entityBean.getEjbClass());
        assertEquals("Entity", entityBean.getEjbName());
        assertEquals("test.EntityLocal", entityBean.getLocal());
        assertEquals("test.EntityLocalHome", entityBean.getLocalHome());
        assertEquals("Container", entityBean.getPersistenceType());
        assertEquals("test.EntityKey", entityBean.getPrimaryKeyClass());
        assertEquals("2.x", entityBean.getCmpVersion());
        assertTrue(entityBean.isReentrant());
        assertEquals(new HashSet<String>(Arrays.asList("alpha", "bravo")), entityBean.getCmpFields());
    }

    private void assertMessageDrivenBean(MessageDrivenBean mdb) {
        assertEquals("Container", mdb.getTransactionType());
        assertEquals("test.MessageDrivenBean", mdb.getEjbClass());
        assertEquals("MDB", mdb.getEjbName());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private FileSystemEntry createFileSystemEntry(final String name) throws Exception {
        FileSystemEntry fileSystemEntry = mock(FileSystemEntry.class);
        when(fileSystemEntry.getName()).thenReturn(name);

        when(fileSystemEntry.doWithInputStream(any(ExceptionCallback.class))).thenAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                FileInputStream in = new FileInputStream("src/test/resources/org/springframework/migrationanalyzer/contributions/ejb/" + name);
                try {
                    return ((ExceptionCallback) invocation.getArguments()[0]).perform(in);
                } finally {
                    IoUtils.closeQuietly(in);
                }
            }
        });

        return fileSystemEntry;
    }
}
