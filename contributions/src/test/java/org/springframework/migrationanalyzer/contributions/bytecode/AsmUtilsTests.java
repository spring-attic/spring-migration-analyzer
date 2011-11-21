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

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.objectweb.asm.Opcodes;

public class AsmUtilsTests {

    @Test
    public void abstractAccess() {
        assertEquals("abstract", AsmUtils.modifiersToString(Opcodes.ACC_ABSTRACT));
    }

    @Test
    public void publicAccess() {
        assertEquals("public", AsmUtils.modifiersToString(Opcodes.ACC_PUBLIC));
    }

    @Test
    public void protectedAccess() {
        assertEquals("protected", AsmUtils.modifiersToString(Opcodes.ACC_PROTECTED));
    }

    @Test
    public void privateAccess() {
        assertEquals("private", AsmUtils.modifiersToString(Opcodes.ACC_PRIVATE));
    }

    @Test
    public void transientAccess() {
        assertEquals("transient", AsmUtils.modifiersToString(Opcodes.ACC_TRANSIENT));
    }

    @Test
    public void synchronizedAccess() {
        assertEquals("synchronized", AsmUtils.modifiersToString(Opcodes.ACC_SYNCHRONIZED));
    }

    @Test
    public void finalAccess() {
        assertEquals("final", AsmUtils.modifiersToString(Opcodes.ACC_FINAL));
    }

    @Test
    public void staticAccess() {
        assertEquals("static", AsmUtils.modifiersToString(Opcodes.ACC_STATIC));
    }

    @Test
    public void volatileAccess() {
        assertEquals("volatile", AsmUtils.modifiersToString(Opcodes.ACC_VOLATILE));
    }

    @Test
    public void classAccessOrdering() {
        assertEquals(
            "public protected private abstract static final strictfp",
            AsmUtils.modifiersToString(Opcodes.ACC_PUBLIC | Opcodes.ACC_PROTECTED | Opcodes.ACC_PRIVATE | Opcodes.ACC_ABSTRACT | Opcodes.ACC_STATIC
                | Opcodes.ACC_FINAL | Opcodes.ACC_STRICT));
    }

    @Test
    public void fieldAccessOrdering() {
        assertEquals(
            "public protected private static final transient volatile",
            AsmUtils.modifiersToString(Opcodes.ACC_PUBLIC | Opcodes.ACC_PROTECTED | Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL
                | Opcodes.ACC_TRANSIENT | Opcodes.ACC_VOLATILE));
    }

    @Test
    public void methodAccessOrdering() {
        assertEquals(
            "public protected private abstract static final synchronized native strictfp",
            AsmUtils.modifiersToString(Opcodes.ACC_PUBLIC | Opcodes.ACC_PROTECTED | Opcodes.ACC_PRIVATE | Opcodes.ACC_ABSTRACT | Opcodes.ACC_STATIC
                | Opcodes.ACC_FINAL | Opcodes.ACC_SYNCHRONIZED | Opcodes.ACC_NATIVE | Opcodes.ACC_STRICT));
    }

}
