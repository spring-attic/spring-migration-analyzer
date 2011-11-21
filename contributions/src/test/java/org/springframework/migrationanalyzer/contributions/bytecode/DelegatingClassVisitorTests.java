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

package org.springframework.migrationanalyzer.contributions.bytecode;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class DelegatingClassVisitorTests {

    private final StubResultGatheringClassVisitor delegate = new StubResultGatheringClassVisitor();

    private final DelegatingClassVisitor visitor;

    @SuppressWarnings("rawtypes")
    public DelegatingClassVisitorTests() {
        Set<ResultGatheringClassVisitor> delegates = new HashSet<ResultGatheringClassVisitor>();
        delegates.add(this.delegate);
        this.visitor = new DelegatingClassVisitor(delegates, new StubResultGatheringAnnotationVisitor(), new StubResultGatheringFieldVisitor(),
            new StubResultGatheringMethodVisitor());
    }

    @Test
    public void getResults() {
        assertNotNull(this.visitor.getResults());
        assertTrue(this.delegate.getGetResultsCalled());
    }

    @Test
    public void visit() {
        this.visitor.visit(0, 0, "java/lang/Object", null, null, null);
        assertTrue(this.delegate.getVisitCalled());
    }

    @Test
    public void visitAnnotation() {
        this.visitor.visitAnnotation(null, false);
        assertTrue(this.delegate.getVisitAnnotationCalled());
    }

    @Test
    public void visitAttribute() {
        this.visitor.visitAttribute(null);
        assertTrue(this.delegate.getVisitAttributeCalled());
    }

    @Test
    public void visitEnd() {
        this.visitor.visitEnd();
        assertTrue(this.delegate.getVisitEndCalled());
    }

    @Test
    public void visitField() {
        this.visitor.visitField(0, null, null, null, null);
        assertTrue(this.delegate.getVisitFieldCalled());
    }

    @Test
    public void visitInnerClass() {
        this.visitor.visitInnerClass(null, null, null, 0);
        assertTrue(this.delegate.getVisitInnerClassCalled());
    }

    @Test
    public void visitMethod() {
        this.visitor.visitMethod(0, null, null, null, null);
        assertTrue(this.delegate.getVisitMethodCalled());
    }

    @Test
    public void visitOuterClass() {
        this.visitor.visitOuterClass(null, null, null);
        assertTrue(this.delegate.getVisitOuterClassCalled());
    }

    @Test
    public void visitSource() {
        this.visitor.visitSource(null, null);
        assertTrue(this.delegate.getVisitSourceCalled());
    }
}
