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

package org.springframework.migrationanalyzer.contributions.apiusage;

import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.EmptyVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.migrationanalyzer.contributions.bytecode.DescriptionBuilder;
import org.springframework.migrationanalyzer.contributions.bytecode.ResultGatheringClassVisitor;
import org.springframework.stereotype.Component;

@Component
final class ApiUsageDetectingAsmVisitor extends EmptyVisitor implements ResultGatheringClassVisitor<ApiUsage> {

    private final ApiUsageDetector delegateDetector;

    private final Set<ApiUsage> results = new HashSet<ApiUsage>();

    private static final String DESCRIPTION_FORMAT_ANNOTATED_TYPE = "%s";

    private static final String DESCRIPTION_FORMAT_ANNOTATED_FIELD = "%s is annotated with %s";

    private static final String DESCRIPTION_FORMAT_ANNOTATED_METHOD = "%s is annotated with %s";

    private static final String DESCRIPTION_FORMAT_ANNOTATED_ARGUMENT = "Argument %s of method %s is annotated with %s";

    private static final String DESCRIPTION_FORMAT_EXTENDS_TYPE = "%s";

    private static final String DESCRIPTION_FORMAT_FIELD = "%s";

    private static final String DESCRIPTION_FORMAT_IMPLEMENTS_INTERFACE = "%s";

    private static final String DESCRIPTION_FORMAT_LOCAL_VARIABLE = "%s %s in %s";

    private static final String DESCRIPTION_FORMAT_METHOD_ARGUMENT = "Argument %s of %s";

    private static final String DESCRIPTION_FORMAT_RETURN_ARGUMENT = "%s";

    private static final String DESCRIPTION_FORMAT_THROWS_EXCEPTION = "%s throws %s";

    private volatile String className;

    ApiUsageDetectingAsmVisitor() {
        this.delegateDetector = new StandardApiUsageDetector();
    }

    @Autowired
    ApiUsageDetectingAsmVisitor(ApiUsageDetector detector) {
        this.delegateDetector = detector;
    }

    @Override
    public Set<ApiUsage> getResults() {
        return this.results;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        Type type = Type.getObjectType(name);

        this.className = type.getClassName();

        detectApiUsageInImplementedInterfaces(interfaces);
        Type superType = Type.getObjectType(superName);
        detectApiUsage(superType, ApiUsageType.EXTENDS_TYPE, String.format(DESCRIPTION_FORMAT_EXTENDS_TYPE, superType.getClassName()));
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        Type annotationType = Type.getType(desc);
        String annotationUsageDescription = String.format(DESCRIPTION_FORMAT_ANNOTATED_TYPE, annotationType.getClassName());
        detectApiUsage(annotationType, ApiUsageType.ANNOTATED_TYPE, annotationUsageDescription);

        return null;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        Type fieldType = Type.getType(desc);
        String fieldDescription = DescriptionBuilder.buildFieldDescription(access, name, fieldType);

        detectApiUsage(fieldType, ApiUsageType.FIELD, String.format(DESCRIPTION_FORMAT_FIELD, fieldDescription));

        return new ApiUsageDetectingFieldVisitor(fieldDescription);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

        String methodDescription = DescriptionBuilder.buildMethodDescription(access, name, desc, this.className);

        detectApiUsageInThrownExceptions(exceptions, methodDescription);
        detectApiUsageInMethodArguments(Type.getArgumentTypes(desc), methodDescription);

        Type returnType = Type.getReturnType(desc);
        detectApiUsage(returnType, ApiUsageType.RETURN_ARGUMENT, String.format(DESCRIPTION_FORMAT_RETURN_ARGUMENT, methodDescription));

        return new ApiUsageDetectingMethodVisitor(methodDescription);
    }

    private void detectApiUsageInImplementedInterfaces(String[] typeNames) {
        if (typeNames != null) {
            for (String typeName : typeNames) {
                Type objectType = Type.getObjectType(typeName);
                detectApiUsage(objectType, ApiUsageType.IMPLEMENTS_INTERFACE,
                    String.format(DESCRIPTION_FORMAT_IMPLEMENTS_INTERFACE, objectType.getClassName()));
            }
        }
    }

    private void detectApiUsageInThrownExceptions(String[] typeNames, String methodDescription) {
        if (typeNames != null) {
            for (String typeName : typeNames) {
                Type objectType = Type.getObjectType(typeName);
                detectApiUsage(objectType, ApiUsageType.THROWS_EXCEPTION,
                    String.format(DESCRIPTION_FORMAT_THROWS_EXCEPTION, methodDescription, objectType.getClassName()));
            }
        }
    }

    private void detectApiUsageInMethodArguments(Type[] types, String methodDescription) {
        int arg = 1;
        for (Type type : types) {
            detectApiUsage(type, ApiUsageType.METHOD_ARGUMENT, String.format(DESCRIPTION_FORMAT_METHOD_ARGUMENT, arg, methodDescription));
            arg++;
        }
    }

    private void detectApiUsage(Type type, ApiUsageType usageType, String usageDescription) {
        ApiUsage apiUsage = this.delegateDetector.detectApiUsage(type.getClassName(), usageType, this.className, usageDescription);
        if (apiUsage != null) {
            this.results.add(apiUsage);
        }
    }

    private final class ApiUsageDetectingFieldVisitor extends EmptyVisitor {

        private final String fieldDescription;

        private ApiUsageDetectingFieldVisitor(String fieldDescription) {
            this.fieldDescription = fieldDescription;
        }

        @Override
        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            Type annotationType = Type.getType(desc);

            String annotationUsageDescription = buildAnnotationUsageDescriptionForField(annotationType.getClassName());

            detectApiUsage(annotationType, ApiUsageType.ANNOTATED_FIELD, annotationUsageDescription);

            return null;
        }

        private String buildAnnotationUsageDescriptionForField(String className) {
            return String.format(DESCRIPTION_FORMAT_ANNOTATED_FIELD, this.fieldDescription, className);
        }
    }

    private final class ApiUsageDetectingMethodVisitor extends EmptyVisitor {

        private final String methodDescription;

        private ApiUsageDetectingMethodVisitor(String methodDescription) {
            this.methodDescription = methodDescription;
        }

        @Override
        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            Type annotationType = Type.getType(desc);

            String annotationUsageDescription = buildAnnotationUsageDescriptionForMethod(annotationType.getClassName());

            detectApiUsage(annotationType, ApiUsageType.ANNOTATED_METHOD, annotationUsageDescription);
            return null;
        }

        @Override
        public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
            Type annotationType = Type.getType(desc);
            String annotationUsageDescription = buildAnnotationUsageDescriptionForMethodParameter(parameter, annotationType.getClassName());
            detectApiUsage(annotationType, ApiUsageType.ANNOTATED_METHOD_ARGUMENT, annotationUsageDescription);

            return null;
        }

        @Override
        public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
            Type variableType = Type.getType(desc);
            detectApiUsage(variableType, ApiUsageType.LOCAL_VARIABLE,
                String.format(DESCRIPTION_FORMAT_LOCAL_VARIABLE, variableType.getClassName(), name, this.methodDescription));
        }

        private String buildAnnotationUsageDescriptionForMethod(String className) {
            return String.format(DESCRIPTION_FORMAT_ANNOTATED_METHOD, this.methodDescription, className);
        }

        private String buildAnnotationUsageDescriptionForMethodParameter(int parameter, String className) {
            return String.format(DESCRIPTION_FORMAT_ANNOTATED_ARGUMENT, parameter + 1, this.methodDescription, className);
        }
    }
}
