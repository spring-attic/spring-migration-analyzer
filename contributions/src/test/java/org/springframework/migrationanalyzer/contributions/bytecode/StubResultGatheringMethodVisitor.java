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

import java.util.Collections;
import java.util.Set;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Label;
import org.springframework.migrationanalyzer.analyze.util.IgnoredByClassPathScan;

@IgnoredByClassPathScan
public class StubResultGatheringMethodVisitor implements ResultGatheringMethodVisitor<Object> {

    private volatile boolean getResultsCalled = false;

    private volatile boolean visitAnnotationCalled = false;

    private volatile boolean visitAnnotationDefaultCalled = false;

    private volatile boolean visitAttributeCalled = false;

    private volatile boolean visitCodeCalled = false;

    private volatile boolean visitEndCalled = false;

    private volatile boolean visitFieldInsnCalled = false;

    private volatile boolean visitFrameCalled = false;

    private volatile boolean visitIincInsnCalled = false;

    private volatile boolean visitInsnCalled = false;

    private volatile boolean visitIntInsnCalled = false;

    private volatile boolean visitJumpInsnCalled = false;

    private volatile boolean visitLabelCalled = false;

    private volatile boolean visitLdcInsnCalled = false;

    private volatile boolean visitLineNumberCalled = false;

    private volatile boolean visitLocalVariableCalled = false;

    private volatile boolean visitLookupSwitchInsnCalled = false;

    private volatile boolean visitMaxsCalled = false;

    private volatile boolean visitMethodInsnCalled = false;

    private volatile boolean visitMultiANewArrayInsnCalled = false;

    private volatile boolean visitParameterAnnotationCalled = false;

    private volatile boolean visitTableSwitchInsnCalled = false;

    private volatile boolean visitTryCatchBlockCalled = false;

    private volatile boolean visitTypeInsnCalled = false;

    private volatile boolean visitVarInsnCalled = false;

    @Override
    public Set<Object> getResults() {
        this.getResultsCalled = true;
        return Collections.emptySet();
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        this.visitAnnotationCalled = true;
        return new StubResultGatheringAnnotationVisitor();
    }

    @Override
    public AnnotationVisitor visitAnnotationDefault() {
        this.visitAnnotationDefaultCalled = true;
        return new StubResultGatheringAnnotationVisitor();
    }

    @Override
    public void visitAttribute(Attribute attr) {
        this.visitAttributeCalled = true;
    }

    @Override
    public void visitCode() {
        this.visitCodeCalled = true;
    }

    @Override
    public void visitEnd() {
        this.visitEndCalled = true;
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        this.visitFieldInsnCalled = true;
    }

    @Override
    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
        this.visitFrameCalled = true;
    }

    @Override
    public void visitIincInsn(int var, int increment) {
        this.visitIincInsnCalled = true;
    }

    @Override
    public void visitInsn(int opcode) {
        this.visitInsnCalled = true;
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        this.visitIntInsnCalled = true;
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        this.visitJumpInsnCalled = true;
    }

    @Override
    public void visitLabel(Label label) {
        this.visitLabelCalled = true;
    }

    @Override
    public void visitLdcInsn(Object cst) {
        this.visitLdcInsnCalled = true;
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        this.visitLineNumberCalled = true;
    }

    @Override
    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        this.visitLocalVariableCalled = true;
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        this.visitLookupSwitchInsnCalled = true;
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        this.visitMaxsCalled = true;
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        this.visitMethodInsnCalled = true;
    }

    @Override
    public void visitMultiANewArrayInsn(String desc, int dims) {
        this.visitMultiANewArrayInsnCalled = true;
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
        this.visitParameterAnnotationCalled = true;
        return new StubResultGatheringAnnotationVisitor();
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {
        this.visitTableSwitchInsnCalled = true;
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        this.visitTryCatchBlockCalled = true;
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        this.visitTypeInsnCalled = true;
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        this.visitVarInsnCalled = true;
    }

    public boolean getGetResultsCalled() {
        return this.getResultsCalled;
    }

    public boolean getVisitAnnotationCalled() {
        return this.visitAnnotationCalled;
    }

    public boolean getVisitAnnotationDefaultCalled() {
        return this.visitAnnotationDefaultCalled;
    }

    public boolean getVisitAttributeCalled() {
        return this.visitAttributeCalled;
    }

    public boolean getVisitCodeCalled() {
        return this.visitCodeCalled;
    }

    public boolean getVisitEndCalled() {
        return this.visitEndCalled;
    }

    public boolean getVisitFieldInsnCalled() {
        return this.visitFieldInsnCalled;
    }

    public boolean getVisitFrameCalled() {
        return this.visitFrameCalled;
    }

    public boolean getVisitIincInsnCalled() {
        return this.visitIincInsnCalled;
    }

    public boolean getVisitInsnCalled() {
        return this.visitInsnCalled;
    }

    public boolean getVisitIntInsnCalled() {
        return this.visitIntInsnCalled;
    }

    public boolean getVisitJumpInsnCalled() {
        return this.visitJumpInsnCalled;
    }

    public boolean getVisitLabelCalled() {
        return this.visitLabelCalled;
    }

    public boolean getVisitLdcInsnCalled() {
        return this.visitLdcInsnCalled;
    }

    public boolean getVisitLineNumberCalled() {
        return this.visitLineNumberCalled;
    }

    public boolean getVisitLocalVariableCalled() {
        return this.visitLocalVariableCalled;
    }

    public boolean getVisitLookupSwitchInsnCalled() {
        return this.visitLookupSwitchInsnCalled;
    }

    public boolean getVisitMaxsCalled() {
        return this.visitMaxsCalled;
    }

    public boolean getVisitMethodInsnCalled() {
        return this.visitMethodInsnCalled;
    }

    public boolean getVisitMultiANewArrayInsnCalled() {
        return this.visitMultiANewArrayInsnCalled;
    }

    public boolean getVisitParameterAnnotationCalled() {
        return this.visitParameterAnnotationCalled;
    }

    public boolean getVisitTableSwitchInsnCalled() {
        return this.visitTableSwitchInsnCalled;
    }

    public boolean getVisitTryCatchBlockCalled() {
        return this.visitTryCatchBlockCalled;
    }

    public boolean getVisitTypeInsnCalled() {
        return this.visitTypeInsnCalled;
    }

    public boolean getVisitVarInsnCalled() {
        return this.visitVarInsnCalled;
    }
}
