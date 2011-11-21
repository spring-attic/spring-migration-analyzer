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
import org.springframework.migrationanalyzer.analyze.util.IgnoredByClassPathScan;

@IgnoredByClassPathScan
public class StubResultGatheringFieldVisitor implements ResultGatheringFieldVisitor<Object> {

    private volatile boolean getResultsCalled = false;

    private volatile boolean visitAnnotationCalled = false;

    private volatile boolean visitAttributeCalled = false;

    private volatile boolean visitEndCalled = false;

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
    public void visitAttribute(Attribute attr) {
        this.visitAttributeCalled = true;
    }

    @Override
    public void visitEnd() {
        this.visitEndCalled = true;
    }

    public boolean getGetResultsCalled() {
        return this.getResultsCalled;
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
}
