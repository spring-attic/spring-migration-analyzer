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

package org.springframework.migrationanalyzer.contributions.transactions;

import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.EmptyVisitor;
import org.springframework.migrationanalyzer.contributions.bytecode.DescriptionBuilder;
import org.springframework.migrationanalyzer.contributions.bytecode.ResultGatheringClassVisitor;

abstract class ProgrammaticTransactionDemarcationDetector extends EmptyVisitor implements
    ResultGatheringClassVisitor<ProgrammaticTransactionDemarcation> {

    private String className;

    private final Set<ProgrammaticTransactionDemarcation> programmaticDemarcation = new HashSet<ProgrammaticTransactionDemarcation>();

    protected abstract ProgrammaticTransactionDemarcation detectProgrammaticTransactionDemarcation(String className, String methodName,
        String containingClassName, String containingMethodDescription);

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.className = Type.getObjectType(name).getClassName();
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

        String methodDescription = DescriptionBuilder.buildMethodDescription(access, name, desc, this.className);
        return new StandardMethodVisitor(methodDescription);
    }

    @Override
    public Set<ProgrammaticTransactionDemarcation> getResults() {
        return this.programmaticDemarcation;
    }

    @Override
    public void clear() {
        this.programmaticDemarcation.clear();
    }

    private final class StandardMethodVisitor extends EmptyVisitor {

        private final String methodDescription;

        StandardMethodVisitor(String methodDescription) {
            this.methodDescription = methodDescription;
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc) {

            ProgrammaticTransactionDemarcation detectedDemarcation = detectProgrammaticTransactionDemarcation(
                Type.getObjectType(owner).getClassName(), name, ProgrammaticTransactionDemarcationDetector.this.className, this.methodDescription);

            if (detectedDemarcation != null) {
                ProgrammaticTransactionDemarcationDetector.this.programmaticDemarcation.add(detectedDemarcation);
            }
        }
    }

}
