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

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Type;
import org.springframework.migrationanalyzer.contributions.util.StringUtils;

/**
 * A helper class for building descriptions
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Thread-safe
 */
public final class DescriptionBuilder {

    private static final String CONSTRUCTOR_NAME = "<init>";

    private static final String METHOD_ARGUMENTS_SEPARATOR = ", ";

    private DescriptionBuilder() {
    }

    /**
     * Build a field description
     * 
     * @param access The access level of the field
     * @param name The name of the field
     * @param fieldType The type of the field
     * @return The description
     */
    public static String buildFieldDescription(int access, String name, Type fieldType) {
        String accessDescription = AsmUtils.modifiersToString(access);
        return accessDescription + " " + fieldType.getClassName() + " " + name;
    }

    /**
     * Build a method description
     * 
     * @param access The access level of the method
     * @param name The name of the method
     * @param desc The method arguments
     * @param className The name of the enclosing class
     * @return The description
     */
    public static String buildMethodDescription(int access, String name, String desc, String className) {
        String accessDescription = AsmUtils.modifiersToString(access);

        String argumentsDescription = buildMethodArgumentsDescription(desc);

        Type returnType = Type.getReturnType(desc);

        String returnTypeDescription;
        String nameDescription;

        if (CONSTRUCTOR_NAME.equals(name)) {
            returnTypeDescription = "";
            nameDescription = className.substring(className.lastIndexOf('.') + 1);
        } else {
            returnTypeDescription = returnType.getClassName() + " ";
            nameDescription = name;
        }

        return accessDescription + " " + returnTypeDescription + nameDescription + "(" + argumentsDescription + ")";
    }

    /**
     * Build a method argument description
     * 
     * @param methodDescription The method description
     * @return The method argument description
     */
    public static String buildMethodArgumentsDescription(String methodDescription) {

        Type[] argumentTypes = Type.getArgumentTypes(methodDescription);
        List<String> classNames = new ArrayList<String>();

        for (Type argumentType : argumentTypes) {
            classNames.add(argumentType.getClassName());
        }

        return StringUtils.toSeparatedString(classNames, METHOD_ARGUMENTS_SEPARATOR);
    }
}
