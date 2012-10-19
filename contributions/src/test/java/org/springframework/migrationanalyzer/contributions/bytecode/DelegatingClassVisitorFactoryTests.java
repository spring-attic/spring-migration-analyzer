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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

public class DelegatingClassVisitorFactoryTests {

    private final ResultGatheringAnnotationVisitor<?> annotationVisitor = mock(ResultGatheringAnnotationVisitor.class);

    private final ResultGatheringFieldVisitor<?> fieldVisitor = mock(ResultGatheringFieldVisitor.class);

    private final ResultGatheringMethodVisitor<?> methodVisitor = mock(ResultGatheringMethodVisitor.class);

    private final ResultGatheringClassVisitor<?> classVisitor = mock(ResultGatheringClassVisitor.class);

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private final DelegatingClassVisitorFactory factory = new DelegatingClassVisitorFactory(new HashSet<ResultGatheringVisitor>(Arrays.asList(
        this.annotationVisitor, this.fieldVisitor, this.methodVisitor, this.classVisitor)));

    @Test
    public void create() {
        ResultGatheringClassVisitor<Object> visitor = this.factory.create();
        assertNotNull(visitor);
        assertTrue(visitor instanceof DelegatingClassVisitor);

        visitor.visitAnnotation(null, true).visitEnd();
        verify(this.annotationVisitor).visitEnd();

        visitor.visitField(0, null, null, null, null).visitEnd();
        verify(this.fieldVisitor).visitEnd();

        visitor.visitMethod(0, null, null, null, null).visitEnd();
        verify(this.methodVisitor).visitEnd();

        visitor.visit(0, 0, null, null, null, null);
        verify(this.classVisitor).visit(0, 0, null, null, null, null);
    }

}
