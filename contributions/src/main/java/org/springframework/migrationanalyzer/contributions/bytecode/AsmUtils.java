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

import org.objectweb.asm.Opcodes;

final class AsmUtils {

    private AsmUtils() {
    }

    /**
     * Converts the given bitwise ORed <code>modifiers</code> into a {@link String}.
     * 
     * <p/>
     * 
     * The returned String should reflect the ordering recommended in the JLS:
     * 
     * <ul>
     * <li>Classes: <code>public protected private abstract static final strictfp</code></li>
     * <li>Fields: <code>public protected private static final transient volatile</code></li>
     * <li>Constructors: <code>public protected private</code></li>
     * <li>Methods: <code>public protected private abstract static final synchronized native strictfp</code></li>
     * </ul>
     * 
     * @param modifiers the modifiers
     * 
     * @return The String representation of the modifiers
     * 
     * @see Opcodes
     */
    // CHECKSTYLE:OFF
    static final String modifiersToString(int modifiers) {
        StringBuilder accessBuilder = new StringBuilder();

        if ((modifiers & Opcodes.ACC_PUBLIC) == Opcodes.ACC_PUBLIC) {
            accessBuilder.append("public ");
        }
        if ((modifiers & Opcodes.ACC_PROTECTED) == Opcodes.ACC_PROTECTED) {
            accessBuilder.append("protected ");
        }
        if ((modifiers & Opcodes.ACC_PRIVATE) == Opcodes.ACC_PRIVATE) {
            accessBuilder.append("private ");
        }
        if ((modifiers & Opcodes.ACC_ABSTRACT) == Opcodes.ACC_ABSTRACT) {
            accessBuilder.append("abstract ");
        }
        if ((modifiers & Opcodes.ACC_STATIC) == Opcodes.ACC_STATIC) {
            accessBuilder.append("static ");
        }
        if ((modifiers & Opcodes.ACC_FINAL) == Opcodes.ACC_FINAL) {
            accessBuilder.append("final ");
        }
        if ((modifiers & Opcodes.ACC_SYNCHRONIZED) == Opcodes.ACC_SYNCHRONIZED) {
            accessBuilder.append("synchronized ");
        }
        if ((modifiers & Opcodes.ACC_NATIVE) == Opcodes.ACC_NATIVE) {
            accessBuilder.append("native ");
        }
        if ((modifiers & Opcodes.ACC_TRANSIENT) == Opcodes.ACC_TRANSIENT) {
            accessBuilder.append("transient ");
        }
        if ((modifiers & Opcodes.ACC_VOLATILE) == Opcodes.ACC_VOLATILE) {
            accessBuilder.append("volatile ");
        }
        if ((modifiers & Opcodes.ACC_STRICT) == Opcodes.ACC_STRICT) {
            accessBuilder.append("strictfp ");
        }

        return accessBuilder.toString().trim();
    }
    // CHECKSTYLE:ON
}
