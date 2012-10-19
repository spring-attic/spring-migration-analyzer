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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class DelegatingClassVisitorTests {

    private final ResultGatheringClassVisitor<?> delegate = mock(ResultGatheringClassVisitor.class);

    private final ResultGatheringAnnotationVisitor<?> annotationVisitor = mock(ResultGatheringAnnotationVisitor.class);

    private final ResultGatheringFieldVisitor<?> fieldVisitor = mock(ResultGatheringFieldVisitor.class);

    private final ResultGatheringMethodVisitor<?> methodVisitor = mock(ResultGatheringMethodVisitor.class);

    private final DelegatingClassVisitor visitor;

    @SuppressWarnings("rawtypes")
    public DelegatingClassVisitorTests() {
        Set<ResultGatheringClassVisitor> delegates = new HashSet<ResultGatheringClassVisitor>();
        delegates.add(this.delegate);
        this.visitor = new DelegatingClassVisitor(delegates, this.annotationVisitor, this.fieldVisitor, this.methodVisitor);
    }

    @Test
    public void getResults() {
        assertNotNull(this.visitor.getResults());
        verify(this.delegate).getResults();
    }

    @Test
    public void visit() {
        this.visitor.visit(0, 0, "java/lang/Object", null, null, null);
        verify(this.delegate).visit(0, 0, "java/lang/Object", null, null, null);
    }

    @Test
    public void visitAnnotation() {
        this.visitor.visitAnnotation(null, false).visitEnd();
        verify(this.delegate).visitAnnotation(null, false);
        verify(this.annotationVisitor).visitEnd();
    }

    @Test
    public void visitAttribute() {
        this.visitor.visitAttribute(null);
        verify(this.delegate).visitAttribute(null);
    }

    @Test
    public void visitEnd() {
        this.visitor.visitEnd();
        verify(this.delegate).visitEnd();

    }

    @Test
    public void visitField() {
        this.visitor.visitField(0, null, null, null, null).visitEnd();
        verify(this.delegate).visitField(0, null, null, null, null);
        verify(this.fieldVisitor).visitEnd();
    }

    @Test
    public void visitInnerClass() {
        this.visitor.visitInnerClass(null, null, null, 0);
        verify(this.delegate).visitInnerClass(null, null, null, 0);
    }

    @Test
    public void visitMethod() {
        this.visitor.visitMethod(0, null, null, null, null).visitEnd();
        verify(this.delegate).visitMethod(0, null, null, null, null);
        verify(this.methodVisitor).visitEnd();
    }

    @Test
    public void visitOuterClass() {
        this.visitor.visitOuterClass(null, null, null);
        verify(this.delegate).visitOuterClass(null, null, null);
    }

    @Test
    public void visitSource() {
        this.visitor.visitSource(null, null);
        verify(this.delegate).visitSource(null, null);
    }
}
