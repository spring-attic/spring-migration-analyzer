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

import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

final class DelegatingClassVisitor implements ResultGatheringClassVisitor<Object> {

    @SuppressWarnings("rawtypes")
    private final Set<ResultGatheringClassVisitor> delegates;

    private final ResultGatheringAnnotationVisitor<?> annotationVisitor;

    private final ResultGatheringFieldVisitor<?> fieldVisitor;

    private final ResultGatheringMethodVisitor<?> methodVisitor;

    @SuppressWarnings("rawtypes")
    DelegatingClassVisitor(Set<ResultGatheringClassVisitor> delegates, ResultGatheringAnnotationVisitor<?> annotationVisitor,
        ResultGatheringFieldVisitor<?> fieldVisitor, ResultGatheringMethodVisitor<?> methodVisitor) {
        this.delegates = delegates;
        this.annotationVisitor = annotationVisitor;
        this.fieldVisitor = fieldVisitor;
        this.methodVisitor = methodVisitor;
    }

    @Override
    public Set<Object> getResults() {
        Set<Object> results = new HashSet<Object>();

        for (ResultGatheringClassVisitor<?> delegate : this.delegates) {
            Set<?> delegateResults = delegate.getResults();
            if (delegateResults != null) {
                results.addAll(delegateResults);
            }
        }

        results.addAll(this.annotationVisitor.getResults());
        results.addAll(this.fieldVisitor.getResults());
        results.addAll(this.methodVisitor.getResults());

        return results;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        for (ResultGatheringClassVisitor<?> delegate : this.delegates) {
            delegate.visit(version, access, name, signature, superName, interfaces);
        }
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        Set<AnnotationVisitor> delegateVisitors = new HashSet<AnnotationVisitor>();

        for (ClassVisitor delegate : this.delegates) {
            AnnotationVisitor delegateAnnotationVisitor = delegate.visitAnnotation(desc, visible);
            if (delegateAnnotationVisitor != null) {
                delegateVisitors.add(delegateAnnotationVisitor);
            }
        }

        delegateVisitors.add(this.annotationVisitor);

        return new DelegatingAnnotationVisitor(delegateVisitors);
    }

    @Override
    public void visitAttribute(Attribute attr) {
        for (ClassVisitor delegate : this.delegates) {
            delegate.visitAttribute(attr);
        }
    }

    @Override
    public void visitEnd() {
        for (ClassVisitor delegate : this.delegates) {
            delegate.visitEnd();
        }
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        Set<FieldVisitor> delegateVisitors = new HashSet<FieldVisitor>();

        for (ClassVisitor delegate : this.delegates) {
            FieldVisitor delegateFieldVisitor = delegate.visitField(access, name, desc, signature, value);
            if (delegateFieldVisitor != null) {
                delegateVisitors.add(delegateFieldVisitor);
            }
        }

        delegateVisitors.add(this.fieldVisitor);

        return new DelegatingFieldVisitor(delegateVisitors, this.annotationVisitor);
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        for (ClassVisitor delegate : this.delegates) {
            delegate.visitInnerClass(name, outerName, innerName, access);
        }
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        Set<MethodVisitor> delegateVisitors = new HashSet<MethodVisitor>();

        for (ClassVisitor delegate : this.delegates) {
            MethodVisitor delegateMethodVisitor = delegate.visitMethod(access, name, desc, signature, exceptions);
            if (delegateMethodVisitor != null) {
                delegateVisitors.add(delegateMethodVisitor);
            }
        }

        delegateVisitors.add(this.methodVisitor);

        return new DelegatingMethodVisitor(delegateVisitors, this.annotationVisitor);
    }

    @Override
    public void visitOuterClass(String owner, String name, String desc) {
        for (ClassVisitor delegate : this.delegates) {
            delegate.visitOuterClass(owner, name, desc);
        }
    }

    @Override
    public void visitSource(String source, String debug) {
        for (ClassVisitor delegate : this.delegates) {
            delegate.visitSource(source, debug);
        }
    }
}
