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
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@SuppressWarnings("rawtypes")
@Component
final class DelegatingClassVisitorFactory implements ResultGatheringClassVisitorFactory {

    private final Set<ResultGatheringVisitor> resultGatheringVisitors;

    @Autowired
    DelegatingClassVisitorFactory(Set<ResultGatheringVisitor> resultGatheringVisitors) {
        this.resultGatheringVisitors = resultGatheringVisitors;
    }

    @Override
    public ResultGatheringClassVisitor<Object> create() {
        Set<AnnotationVisitor> annotationVisitorInstances = new HashSet<AnnotationVisitor>();
        Set<FieldVisitor> fieldVisitorInstances = new HashSet<FieldVisitor>();
        Set<MethodVisitor> methodVisitorInstances = new HashSet<MethodVisitor>();
        Set<ResultGatheringClassVisitor> classVisitorInstances = new HashSet<ResultGatheringClassVisitor>();

        for (ResultGatheringVisitor visitor : this.resultGatheringVisitors) {
            visitor.clear();

            if (visitor instanceof ResultGatheringAnnotationVisitor) {
                annotationVisitorInstances.add((ResultGatheringAnnotationVisitor) visitor);
            }
            if (visitor instanceof ResultGatheringFieldVisitor) {
                fieldVisitorInstances.add((ResultGatheringFieldVisitor) visitor);
            }
            if (visitor instanceof ResultGatheringMethodVisitor) {
                methodVisitorInstances.add((ResultGatheringMethodVisitor) visitor);
            }
            if (visitor instanceof ResultGatheringClassVisitor) {
                classVisitorInstances.add((ResultGatheringClassVisitor) visitor);
            }
        }

        DelegatingAnnotationVisitor annotationVisitor = new DelegatingAnnotationVisitor(annotationVisitorInstances);
        DelegatingFieldVisitor fieldVisitor = new DelegatingFieldVisitor(fieldVisitorInstances, annotationVisitor);
        DelegatingMethodVisitor methodVisitor = new DelegatingMethodVisitor(methodVisitorInstances, annotationVisitor);
        return new DelegatingClassVisitor(classVisitorInstances, annotationVisitor, fieldVisitor, methodVisitor);
    }
}
