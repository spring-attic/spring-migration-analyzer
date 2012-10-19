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
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

final class DelegatingMethodVisitor implements ResultGatheringMethodVisitor<Object> {

    private final Set<MethodVisitor> delegates;

    private final AnnotationVisitor annotationVisitor;

    DelegatingMethodVisitor(Set<MethodVisitor> delegates, AnnotationVisitor annotationVisitor) {
        this.delegates = delegates;
        this.annotationVisitor = annotationVisitor;
    }

    @Override
    public Set<Object> getResults() {
        Set<Object> results = new HashSet<Object>();

        for (MethodVisitor delegate : this.delegates) {
            if (delegate instanceof ResultGatheringMethodVisitor<?>) {
                Set<?> delegateResults = ((ResultGatheringMethodVisitor<?>) delegate).getResults();
                if (delegateResults != null) {
                    results.addAll(delegateResults);
                }
            }
        }

        return results;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        Set<AnnotationVisitor> delegateVisitors = new HashSet<AnnotationVisitor>();

        for (MethodVisitor delegate : this.delegates) {
            AnnotationVisitor delegateAnnotationVisitor = delegate.visitAnnotation(desc, visible);
            if (delegateAnnotationVisitor != null) {
                delegateVisitors.add(delegateAnnotationVisitor);
            }
        }

        delegateVisitors.add(this.annotationVisitor);

        return new DelegatingAnnotationVisitor(delegateVisitors);
    }

    @Override
    public AnnotationVisitor visitAnnotationDefault() {
        Set<AnnotationVisitor> delegateVisitors = new HashSet<AnnotationVisitor>();

        for (MethodVisitor delegate : this.delegates) {
            AnnotationVisitor delegateAnnotationVisitor = delegate.visitAnnotationDefault();
            if (delegateAnnotationVisitor != null) {
                delegateVisitors.add(delegateAnnotationVisitor);
            }
        }

        delegateVisitors.add(this.annotationVisitor);

        return new DelegatingAnnotationVisitor(delegateVisitors);
    }

    @Override
    public void visitAttribute(Attribute attr) {
        for (MethodVisitor delegate : this.delegates) {
            delegate.visitAttribute(attr);
        }
    }

    @Override
    public void visitCode() {
        for (MethodVisitor delegate : this.delegates) {
            delegate.visitCode();
        }
    }

    @Override
    public void visitEnd() {
        for (MethodVisitor delegate : this.delegates) {
            delegate.visitEnd();
        }
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        for (MethodVisitor delegate : this.delegates) {
            delegate.visitFieldInsn(opcode, owner, name, desc);
        }
    }

    @Override
    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
        for (MethodVisitor delegate : this.delegates) {
            delegate.visitFrame(type, nLocal, local, nStack, stack);
        }
    }

    @Override
    public void visitIincInsn(int var, int increment) {
        for (MethodVisitor delegate : this.delegates) {
            delegate.visitIincInsn(var, increment);
        }
    }

    @Override
    public void visitInsn(int opcode) {
        for (MethodVisitor delegate : this.delegates) {
            delegate.visitInsn(opcode);
        }
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        for (MethodVisitor delegate : this.delegates) {
            delegate.visitIntInsn(opcode, operand);
        }
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        for (MethodVisitor delegate : this.delegates) {
            delegate.visitJumpInsn(opcode, label);
        }
    }

    @Override
    public void visitLabel(Label label) {
        for (MethodVisitor delegate : this.delegates) {
            delegate.visitLabel(label);
        }
    }

    @Override
    public void visitLdcInsn(Object cst) {
        for (MethodVisitor delegate : this.delegates) {
            delegate.visitLdcInsn(cst);
        }
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        for (MethodVisitor delegate : this.delegates) {
            delegate.visitLineNumber(line, start);
        }
    }

    @Override
    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        for (MethodVisitor delegate : this.delegates) {
            delegate.visitLocalVariable(name, desc, signature, start, end, index);
        }
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        for (MethodVisitor delegate : this.delegates) {
            delegate.visitLookupSwitchInsn(dflt, keys, labels);
        }
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        for (MethodVisitor delegate : this.delegates) {
            delegate.visitMaxs(maxStack, maxLocals);
        }
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        for (MethodVisitor delegate : this.delegates) {
            delegate.visitMethodInsn(opcode, owner, name, desc);
        }
    }

    @Override
    public void visitMultiANewArrayInsn(String desc, int dims) {
        for (MethodVisitor delegate : this.delegates) {
            delegate.visitMultiANewArrayInsn(desc, dims);
        }
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
        Set<AnnotationVisitor> delegateVisitors = new HashSet<AnnotationVisitor>();

        for (MethodVisitor delegate : this.delegates) {
            AnnotationVisitor delegateAnnotationVisitor = delegate.visitParameterAnnotation(parameter, desc, visible);
            if (delegateAnnotationVisitor != null) {
                delegateVisitors.add(delegateAnnotationVisitor);
            }
        }

        delegateVisitors.add(this.annotationVisitor);

        return new DelegatingAnnotationVisitor(delegateVisitors);
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {
        for (MethodVisitor delegate : this.delegates) {
            delegate.visitTableSwitchInsn(min, max, dflt, labels);
        }
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        for (MethodVisitor delegate : this.delegates) {
            delegate.visitTryCatchBlock(start, end, handler, type);
        }
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        for (MethodVisitor delegate : this.delegates) {
            delegate.visitTypeInsn(opcode, type);
        }
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        for (MethodVisitor delegate : this.delegates) {
            delegate.visitVarInsn(opcode, var);
        }
    }
}
