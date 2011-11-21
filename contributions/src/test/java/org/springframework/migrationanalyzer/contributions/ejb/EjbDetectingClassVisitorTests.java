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
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Set;

import org.junit.Test;
import org.objectweb.asm.ClassReader;

public class EjbDetectingClassVisitorTests {

    private final EjbDetectingClassVisitor classVisitor = new EjbDetectingClassVisitor();

    @Test
    public void statelessSessionBean() throws IOException {
        ClassReader reader = new ClassReader(getClass().getResourceAsStream("StatelessEjb.class"));
        reader.accept(this.classVisitor, 0);

        Set<Ejb> ejbs = this.classVisitor.getResults();
        assertEquals(1, ejbs.size());

        Ejb ejb = ejbs.iterator().next();
        assertTrue(ejb instanceof SessionBean);
        assertEquals(SessionType.STATELESS, ((SessionBean) ejb).getSessionType());
        assertEquals(StatelessEjb.class.getName(), ejb.getEjbClass());
    }

    @Test
    public void beanManagedTransactionStatefulSessionBean() throws IOException {
        ClassReader reader = new ClassReader(getClass().getResourceAsStream("BeanManagedTransactionStatefulEjb.class"));
        reader.accept(this.classVisitor, 0);

        Set<Ejb> ejbs = this.classVisitor.getResults();
        assertEquals(1, ejbs.size());

        Ejb ejb = ejbs.iterator().next();
        assertTrue(ejb instanceof SessionBean);
        assertEquals(SessionType.STATEFUL, ((SessionBean) ejb).getSessionType());
        assertEquals("Bean", ((SessionBean) ejb).getTransactionType());
        assertEquals(BeanManagedTransactionStatefulEjb.class.getName(), ejb.getEjbClass());
    }

    @Test
    public void namedMessageDrivenBean() throws IOException {
        ClassReader reader = new ClassReader(getClass().getResourceAsStream("MessageDrivenEjb.class"));
        reader.accept(this.classVisitor, 0);

        Set<Ejb> ejbs = this.classVisitor.getResults();
        assertEquals(1, ejbs.size());

        Ejb ejb = ejbs.iterator().next();
        assertTrue(ejb instanceof MessageDrivenBean);
        assertEquals("the mdb", ((MessageDrivenBean) ejb).getEjbName());
        assertEquals(MessageDrivenEjb.class.getName(), ejb.getEjbClass());
    }

    @Test
    public void methodAnnotationsDeclarativeTransactionEjb() throws IOException {
        ClassReader reader = new ClassReader(getClass().getResourceAsStream("DeclarativeTransactionEjbWithAnnotatedMethods.class"));
        reader.accept(this.classVisitor, 0);

        Set<Ejb> ejbs = this.classVisitor.getResults();
        assertEquals(1, ejbs.size());

        Ejb ejb = ejbs.iterator().next();
        assertTrue(ejb instanceof SessionBean);

    }

    @Test(expected = IllegalStateException.class)
    public void statefulAndStatelessEjbProducesIllegalStateException() throws IOException {
        ClassReader reader = new ClassReader(getClass().getResourceAsStream("StatefulAndStatelessEjb.class"));
        reader.accept(this.classVisitor, 0);
    }

    @Test(expected = IllegalStateException.class)
    public void statefulAndMessageDrivenEjbProducesIllegalStateException() throws IOException {
        ClassReader reader = new ClassReader(getClass().getResourceAsStream("MessageDrivenStatefulEjb.class"));
        reader.accept(this.classVisitor, 0);
    }
}
