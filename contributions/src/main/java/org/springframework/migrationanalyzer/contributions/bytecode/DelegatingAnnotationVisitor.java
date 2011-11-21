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
import org.springframework.migrationanalyzer.analyze.util.IgnoredByClassPathScan;

@IgnoredByClassPathScan
final class DelegatingAnnotationVisitor implements ResultGatheringAnnotationVisitor<Object> {

    private final Set<AnnotationVisitor> delegates;

    DelegatingAnnotationVisitor(Set<AnnotationVisitor> delegates) {
        this.delegates = delegates;
    }

    @Override
    public Set<Object> getResults() {
        Set<Object> results = new HashSet<Object>();

        for (AnnotationVisitor delegate : this.delegates) {
            if (delegate instanceof ResultGatheringAnnotationVisitor<?>) {
                Set<?> delegateResults = ((ResultGatheringAnnotationVisitor<?>) delegate).getResults();
                if (delegateResults != null) {
                    results.addAll(delegateResults);
                }
            }
        }

        return results;
    }

    @Override
    public void visit(String name, Object value) {
        for (AnnotationVisitor delegate : this.delegates) {
            delegate.visit(name, value);
        }
    }

    @Override
    public AnnotationVisitor visitAnnotation(String name, String desc) {
        Set<AnnotationVisitor> delegateVisitors = new HashSet<AnnotationVisitor>();

        for (AnnotationVisitor delegate : this.delegates) {
            AnnotationVisitor annotationVisitor = delegate.visitAnnotation(name, desc);
            if (annotationVisitor != null) {
                delegateVisitors.add(annotationVisitor);
            }
        }

        delegateVisitors.add(this);

        return new DelegatingAnnotationVisitor(delegateVisitors);
    }

    @Override
    public AnnotationVisitor visitArray(String name) {

        Set<AnnotationVisitor> delegateVisitors = new HashSet<AnnotationVisitor>();

        for (AnnotationVisitor delegate : this.delegates) {
            AnnotationVisitor annotationVisitor = delegate.visitArray(name);
            if (annotationVisitor != null) {
                delegateVisitors.add(annotationVisitor);
            }
        }

        delegateVisitors.add(this);

        return new DelegatingAnnotationVisitor(delegateVisitors);
    }

    @Override
    public void visitEnd() {
        for (AnnotationVisitor delegate : this.delegates) {
            delegate.visitEnd();
        }
    }

    @Override
    public void visitEnum(String name, String desc, String value) {
        for (AnnotationVisitor delegate : this.delegates) {
            delegate.visitEnum(name, desc, value);
        }
    }
}
