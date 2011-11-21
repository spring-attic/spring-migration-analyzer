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
import org.objectweb.asm.MethodVisitor;

public class DelegatingMethodVisitorTests {

    private final StubResultGatheringMethodVisitor delegate = new StubResultGatheringMethodVisitor();

    private final DelegatingMethodVisitor visitor;

    public DelegatingMethodVisitorTests() {
        Set<MethodVisitor> delegates = new HashSet<MethodVisitor>();
        delegates.add(this.delegate);
        this.visitor = new DelegatingMethodVisitor(delegates, null);
    }

    @Test
    public void getResults() {
        assertNotNull(this.visitor.getResults());
        assertTrue(this.delegate.getGetResultsCalled());
    }

    @Test
    public void visitAnnotation() {
        this.visitor.visitAnnotation(null, false);
        assertTrue(this.delegate.getVisitAnnotationCalled());
    }

    @Test
    public void visitAnnotationDefault() {
        this.visitor.visitAnnotationDefault();
        assertTrue(this.delegate.getVisitAnnotationDefaultCalled());
    }

    @Test
    public void visitAttribute() {
        this.visitor.visitAttribute(null);
        assertTrue(this.delegate.getVisitAttributeCalled());
    }

    @Test
    public void visitCode() {
        this.visitor.visitCode();
        assertTrue(this.delegate.getVisitCodeCalled());
    }

    @Test
    public void visitEnd() {
        this.visitor.visitEnd();
        assertTrue(this.delegate.getVisitEndCalled());
    }

    @Test
    public void visitFieldInsn() {
        this.visitor.visitFieldInsn(0, null, null, null);
        assertTrue(this.delegate.getVisitFieldInsnCalled());
    }

    @Test
    public void visitFrame() {
        this.visitor.visitFrame(0, 0, null, 0, null);
        assertTrue(this.delegate.getVisitFrameCalled());
    }

    @Test
    public void visitIincInsn() {
        this.visitor.visitIincInsn(0, 0);
        assertTrue(this.delegate.getVisitIincInsnCalled());
    }

    @Test
    public void visitInsn() {
        this.visitor.visitInsn(0);
        assertTrue(this.delegate.getVisitInsnCalled());
    }

    @Test
    public void visitIntInsn() {
        this.visitor.visitIntInsn(0, 0);
        assertTrue(this.delegate.getVisitIntInsnCalled());
    }

    @Test
    public void visitJumpInsn() {
        this.visitor.visitJumpInsn(0, null);
        assertTrue(this.delegate.getVisitJumpInsnCalled());
    }

    @Test
    public void visitLabel() {
        this.visitor.visitLabel(null);
        assertTrue(this.delegate.getVisitLabelCalled());
    }

    @Test
    public void visitLdcInsn() {
        this.visitor.visitLdcInsn(null);
        assertTrue(this.delegate.getVisitLdcInsnCalled());
    }

    @Test
    public void visitLineNumber() {
        this.visitor.visitLineNumber(0, null);
        assertTrue(this.delegate.getVisitLineNumberCalled());
    }

    @Test
    public void visitLocalVariable() {
        this.visitor.visitLocalVariable(null, null, null, null, null, 0);
        assertTrue(this.delegate.getVisitLocalVariableCalled());
    }

    @Test
    public void visitLookupSwitchInsn() {
        this.visitor.visitLookupSwitchInsn(null, null, null);
        assertTrue(this.delegate.getVisitLookupSwitchInsnCalled());
    }

    @Test
    public void visitMaxs() {
        this.visitor.visitMaxs(0, 0);
        assertTrue(this.delegate.getVisitMaxsCalled());
    }

    @Test
    public void visitMethodInsn() {
        this.visitor.visitMethodInsn(0, null, null, null);
        assertTrue(this.delegate.getVisitMethodInsnCalled());
    }

    @Test
    public void visitMultiANewArrayInsn() {
        this.visitor.visitMultiANewArrayInsn(null, 0);
        assertTrue(this.delegate.getVisitMultiANewArrayInsnCalled());
    }

    @Test
    public void visitParameterAnnotation() {
        this.visitor.visitParameterAnnotation(0, null, false);
        assertTrue(this.delegate.getVisitParameterAnnotationCalled());
    }

    @Test
    public void visitTableSwitchInsn() {
        this.visitor.visitTableSwitchInsn(0, 0, null, null);
        assertTrue(this.delegate.getVisitTableSwitchInsnCalled());
    }

    @Test
    public void visitTryCatchBlock() {
        this.visitor.visitTryCatchBlock(null, null, null, null);
        assertTrue(this.delegate.getVisitTryCatchBlockCalled());
    }

    @Test
    public void visitTypeInsn() {
        this.visitor.visitTypeInsn(0, null);
        assertTrue(this.delegate.getVisitTypeInsnCalled());
    }

    @Test
    public void visitVanInsn() {
        this.visitor.visitVarInsn(0, 0);
        assertTrue(this.delegate.getVisitVarInsnCalled());
    }

}
