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
import org.objectweb.asm.AnnotationVisitor;

public class DelegatingAnnotationVisitorTests {

    private final ResultGatheringAnnotationVisitor<?> delegate = mock(ResultGatheringAnnotationVisitor.class);

    private final DelegatingAnnotationVisitor visitor;

    public DelegatingAnnotationVisitorTests() {
        Set<AnnotationVisitor> delegates = new HashSet<AnnotationVisitor>();
        delegates.add(this.delegate);
        this.visitor = new DelegatingAnnotationVisitor(delegates);
    }

    @Test
    public void getResults() {
        assertNotNull(this.visitor.getResults());
        verify(this.delegate).getResults();
    }

    @Test
    public void clear() {
        this.visitor.clear();
        verify(this.delegate).clear();
    }

    @Test
    public void visit() {
        this.visitor.visit(null, null);
        verify(this.delegate).visit(null, null);
    }

    @Test
    public void visitAnnotation() {
        this.visitor.visitAnnotation(null, null);
        verify(this.delegate).visitAnnotation(null, null);
    }

    @Test
    public void visitArray() {
        this.visitor.visitArray(null);
        verify(this.delegate).visitArray(null);
    }

    @Test
    public void visitEnd() {
        this.visitor.visitEnd();
        verify(this.delegate).visitEnd();
    }

    @Test
    public void visitEnum() {
        this.visitor.visitEnum(null, null, null);
        verify(this.delegate).visitEnum(null, null, null);
    }

}
