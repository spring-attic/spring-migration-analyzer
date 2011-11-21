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
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.springframework.migrationanalyzer.analyze.util.IgnoredByClassPathScan;

@IgnoredByClassPathScan
public class StubResultGatheringClassVisitor implements ResultGatheringClassVisitor<Object> {

    private volatile boolean getResultsCalled = false;

    private volatile boolean visitCalled = false;

    private volatile boolean visitAnnotationCalled = false;

    private volatile boolean visitAttributeCalled = false;

    private volatile boolean visitEndCalled = false;

    private volatile boolean visitFieldCalled = false;

    private volatile boolean visitInnerClassCalled = false;

    private volatile boolean visitMethodCalled = false;

    private volatile boolean visitOuterClassCalled = false;

    private volatile boolean visitSourceCalled = false;

    @Override
    public Set<Object> getResults() {
        this.getResultsCalled = true;
        return Collections.emptySet();
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.visitCalled = true;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        this.visitAnnotationCalled = true;
        return new StubResultGatheringAnnotationVisitor();
    }

    @Override
    public void visitAttribute(Attribute attr) {
        this.visitAttributeCalled = true;
    }

    @Override
    public void visitEnd() {
        this.visitEndCalled = true;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        this.visitFieldCalled = true;
        return new StubResultGatheringFieldVisitor();
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        this.visitInnerClassCalled = true;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        this.visitMethodCalled = true;
        return new StubResultGatheringMethodVisitor();
    }

    @Override
    public void visitOuterClass(String owner, String name, String desc) {
        this.visitOuterClassCalled = true;
    }

    @Override
    public void visitSource(String source, String debug) {
        this.visitSourceCalled = true;
    }

    public boolean getGetResultsCalled() {
        return this.getResultsCalled;
    }

    public boolean getVisitCalled() {
        return this.visitCalled;
    }

    public boolean getVisitAnnotationCalled() {
        return this.visitAnnotationCalled;
    }

    public boolean getVisitAttributeCalled() {
        return this.visitAttributeCalled;
    }

    public boolean getVisitEndCalled() {
        return this.visitEndCalled;
    }

    public boolean getVisitFieldCalled() {
        return this.visitFieldCalled;
    }

    public boolean getVisitInnerClassCalled() {
        return this.visitInnerClassCalled;
    }

    public boolean getVisitMethodCalled() {
        return this.visitMethodCalled;
    }

    public boolean getVisitOuterClassCalled() {
        return this.visitOuterClassCalled;
    }

    public boolean getVisitSourceCalled() {
        return this.visitSourceCalled;
    }
}
