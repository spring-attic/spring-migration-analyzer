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
import org.objectweb.asm.MethodVisitor;

public class DelegatingMethodVisitorTests {

    private final ResultGatheringMethodVisitor<?> delegate = mock(ResultGatheringMethodVisitor.class);

    private final DelegatingMethodVisitor visitor;

    public DelegatingMethodVisitorTests() {
        Set<MethodVisitor> delegates = new HashSet<MethodVisitor>();
        delegates.add(this.delegate);
        this.visitor = new DelegatingMethodVisitor(delegates, null);
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
    public void visitAnnotation() {
        this.visitor.visitAnnotation(null, false);
        verify(this.delegate).visitAnnotation(null, false);
    }

    @Test
    public void visitAnnotationDefault() {
        this.visitor.visitAnnotationDefault();
        verify(this.delegate).visitAnnotationDefault();
    }

    @Test
    public void visitAttribute() {
        this.visitor.visitAttribute(null);
        verify(this.delegate).visitAttribute(null);
    }

    @Test
    public void visitCode() {
        this.visitor.visitCode();
        verify(this.delegate).visitCode();
    }

    @Test
    public void visitEnd() {
        this.visitor.visitEnd();
        verify(this.delegate).visitEnd();
    }

    @Test
    public void visitFieldInsn() {
        this.visitor.visitFieldInsn(0, null, null, null);
        verify(this.delegate).visitFieldInsn(0, null, null, null);
    }

    @Test
    public void visitFrame() {
        this.visitor.visitFrame(0, 0, null, 0, null);
        verify(this.delegate).visitFrame(0, 0, null, 0, null);
    }

    @Test
    public void visitIincInsn() {
        this.visitor.visitIincInsn(0, 0);
        verify(this.delegate).visitIincInsn(0, 0);
    }

    @Test
    public void visitInsn() {
        this.visitor.visitInsn(0);
        verify(this.delegate).visitInsn(0);
    }

    @Test
    public void visitIntInsn() {
        this.visitor.visitIntInsn(0, 0);
        verify(this.delegate).visitIntInsn(0, 0);
    }

    @Test
    public void visitJumpInsn() {
        this.visitor.visitJumpInsn(0, null);
        verify(this.delegate).visitJumpInsn(0, null);
    }

    @Test
    public void visitLabel() {
        this.visitor.visitLabel(null);
        verify(this.delegate).visitLabel(null);
    }

    @Test
    public void visitLdcInsn() {
        this.visitor.visitLdcInsn(null);
        verify(this.delegate).visitLdcInsn(null);
    }

    @Test
    public void visitLineNumber() {
        this.visitor.visitLineNumber(0, null);
        verify(this.delegate).visitLineNumber(0, null);
    }

    @Test
    public void visitLocalVariable() {
        this.visitor.visitLocalVariable(null, null, null, null, null, 0);
        verify(this.delegate).visitLocalVariable(null, null, null, null, null, 0);
    }

    @Test
    public void visitLookupSwitchInsn() {
        this.visitor.visitLookupSwitchInsn(null, null, null);
        verify(this.delegate).visitLookupSwitchInsn(null, null, null);
    }

    @Test
    public void visitMaxs() {
        this.visitor.visitMaxs(0, 0);
        verify(this.delegate).visitMaxs(0, 0);
    }

    @Test
    public void visitMethodInsn() {
        this.visitor.visitMethodInsn(0, null, null, null);
        verify(this.delegate).visitMethodInsn(0, null, null, null);
    }

    @Test
    public void visitMultiANewArrayInsn() {
        this.visitor.visitMultiANewArrayInsn(null, 0);
        verify(this.delegate).visitMultiANewArrayInsn(null, 0);
    }

    @Test
    public void visitParameterAnnotation() {
        this.visitor.visitParameterAnnotation(0, null, false);
        verify(this.delegate).visitParameterAnnotation(0, null, false);
    }

    @Test
    public void visitTableSwitchInsn() {
        this.visitor.visitTableSwitchInsn(0, 0, null, null);
        verify(this.delegate).visitTableSwitchInsn(0, 0, null, null);
    }

    @Test
    public void visitTryCatchBlock() {
        this.visitor.visitTryCatchBlock(null, null, null, null);
        verify(this.delegate).visitTryCatchBlock(null, null, null, null);
    }

    @Test
    public void visitTypeInsn() {
        this.visitor.visitTypeInsn(0, null);
        verify(this.delegate).visitTypeInsn(0, null);
    }

    @Test
    public void visitVanInsn() {
        this.visitor.visitVarInsn(0, 0);
        verify(this.delegate).visitVarInsn(0, 0);
    }
}
