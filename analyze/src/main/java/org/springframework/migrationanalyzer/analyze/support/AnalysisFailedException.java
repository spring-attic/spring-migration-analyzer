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

package org.springframework.migrationanalyzer.analyze.support;

/**
 * Thrown to indicate a failure has occurred during analysis
 * 
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Thread-safe
 * 
 */
public final class AnalysisFailedException extends Exception {

    private static final long serialVersionUID = 8909642220750013631L;

    /**
     * Creates a new AnalysisFailedException with the given explanation <code>message</code> and failure
     * <code>cause</code>.
     * 
     * @param message The explanation message
     * @param cause The failure cause
     */
    public AnalysisFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new AnalysisFailedException with the given failure <code>cause</code>.
     * 
     * @param cause The failure cause
     */
    public AnalysisFailedException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new AnalysisFailedException with the given failure <code>message</code>.
     * 
     * @param message The reason message
     */
    public AnalysisFailedException(String message) {
        super(message);
    }
}
