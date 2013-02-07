/*
 * Copyright 2013 the original author or authors.
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

package org.springframework.migrationanalyzer.render.support.xml;

import java.io.StringWriter;

final class XmlUtils {

    private XmlUtils() {
    }

    static String escape(String input) {
        StringWriter output = new StringWriter(input.length() * 2);
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '"') {
                output.append("&quot;");
            } else if (c == '\'') {
                output.append("&apos;");
            } else if (c == '<') {
                output.append("&lt;");
            } else if (c == '>') {
                output.append("&gt;");
            } else if (c == '&') {
                output.append("&amp;");
            } else {
                output.append(c);
            }
        }
        return output.toString();
    }

}
