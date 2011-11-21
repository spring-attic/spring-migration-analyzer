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
import org.objectweb.asm.FieldVisitor;
import org.springframework.migrationanalyzer.analyze.util.IgnoredByClassPathScan;

@IgnoredByClassPathScan
final class DelegatingFieldVisitor implements ResultGatheringFieldVisitor<Object> {

    private final Set<FieldVisitor> delegates;

    private final AnnotationVisitor annotationVisitor;

    DelegatingFieldVisitor(Set<FieldVisitor> delegates, AnnotationVisitor annotationVisitor) {
        this.delegates = delegates;
        this.annotationVisitor = annotationVisitor;
    }

    @Override
    public Set<Object> getResults() {
        Set<Object> results = new HashSet<Object>();

        for (FieldVisitor delegate : this.delegates) {
            if (delegate instanceof ResultGatheringFieldVisitor<?>) {
                Set<?> delegateResults = ((ResultGatheringFieldVisitor<?>) delegate).getResults();
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

        for (FieldVisitor delegate : this.delegates) {
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
        for (FieldVisitor delegate : this.delegates) {
            delegate.visitAttribute(attr);
        }
    }

    @Override
    public void visitEnd() {
        for (FieldVisitor delegate : this.delegates) {
            delegate.visitEnd();
        }
    }
}
